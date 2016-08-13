package com.Room;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.Stu.StuAddDiaLog;
import com.Tools.DBMsg;
import com.Tools.FileControl;
import com.Tools.SqlHelper;
import com.User.UserMsgModel;

public class RoomMenu extends JFrame implements ActionListener{
	private FileControl FileCon=null;
	private ResultSet rs=null;
	private JPanel jp;
	private JButton Add,Delete,update;//������ɾ����ˢ��
	private JButton MsgFileGetIn,MsgFileGetOut;
	private JTable jtb=null;
	private JScrollPane jsp=null;
	
	private JPanel jp1,jp2;
	private JLabel jl;
	private JButton jb1,jb2;
	private JTextField jtf;
	
	private UserMsgModel UMM;
	private DBMsg 	dbMsg=new DBMsg();
	
	private String DBTable="XustPostTable";
	private String []TableParas=dbMsg.XustPostTable;
	private PostAddDiaLog CAd=null;
	private String[]	XustPosts;
	private String	UserType;
	public String[] ReturnUserPosts(){
		String UserPost="";
		SqlHelper sqlhelp= SqlHelper.getInstance();
		try {
			String sql="select UserPost from XustPost";
			rs=sqlhelp.queryExecute(sql);
			while(rs.next()){
				UserPost+=rs.getString(1)+" ";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlhelp.DBclose();
		}
		return UserPost.split(" ");
	}
	
	
	public  void UpdataClassMSG(){
		String sql="",sql2="";
		SqlHelper sqlhelp=SqlHelper.getInstance();
		XustPosts=ReturnUserPosts();
	
		for (int i = 0; i < XustPosts.length; i++) {
			int NUM=0;
			sql="select *from DetailMsg where UserPost="+"'"+XustPosts[i]+"'";
			try {
				rs=sqlhelp.queryExecute(sql);
				while(rs.next()){
					NUM++;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally {
				sqlhelp.DBclose();
			}
			sql2="update XustPost set PersonNum="+"'"+NUM+"'"+" where UserPost="+"'"+XustPosts[i]+"'";
				try {
				sqlhelp.queryExe(sql2);
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally {
				sqlhelp.DBclose();
			}
		}
	}
	public RoomMenu(String UserType){
		this.UserType=UserType;
		UpdataClassMSG();
		Add=new JButton("����");
		Add.addActionListener(this);
		Delete=new JButton("ɾ��");
		Delete.addActionListener(this);
		
		jp1=new JPanel();
		jtf=new JTextField(10);
		jb1=new JButton("��ѯ");
		jb1.addActionListener(this);
		update=new JButton("ˢ��");
		update.addActionListener(this);
		MsgFileGetIn=new JButton("����");
		MsgFileGetIn.addActionListener(this);
		MsgFileGetOut=new JButton("����");
		MsgFileGetOut.addActionListener(this);
		jl=new JLabel("�����벿��");
		jp1.add(MsgFileGetIn);
		jp1.add(jl);
		jp1.add(jtf);
		jp1.add(jb1);
		jp1.add(update);
		jp1.add(MsgFileGetOut);
		
		jp2=new JPanel();
		
		
		
		jp2.add(Add);
		jp2.add(Delete);

		
		//��������ģ�Ͷ���
		  UMM=new UserMsgModel();
		  String sql ="select * from XustPost";
		  UMM.queryUser(sql,this.TableParas,DBTable);
		
		jtb=new JTable(UMM);//��ģ�ͼ��뵽JTable
		
		jsp=new JScrollPane(jtb);
		
		if(!UserType.equals("����Ա")){
			Add.setEnabled(false);
			Delete.setEnabled(false);
			MsgFileGetIn.setEnabled(false);
			MsgFileGetOut.setEnabled(false);
		}
		
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new RoomMenu("����Ա");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==MsgFileGetIn){
			//����
			FileCon=new FileControl();
			FileCon.ReadInFile(this.DBTable);
		}else if(e.getSource()==MsgFileGetOut){
			//����
			FileCon=new FileControl();
			FileCon.SaveToFile(this.DBTable);
		}
		
		if(e.getSource()==Add){
			CAd=new PostAddDiaLog(this, "�½�����", false);
			
			
		}else if(e.getSource()==Delete){
			//ɾ��
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			//ת����String �����model
			String UserPost=(String)UMM.getValueAt(rowNum, 0);
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"�Ƿ����ɾ����", "��ѡ��..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String sql ="delete from XustPost where UserPost=?";
						String[] paras={UserPost};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "ɾ���ɹ���");
						}else {
							JOptionPane.showMessageDialog(this, "ɾ��ʧ�ܣ�");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from XustPost";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
						
					   }
			
			
		}else if(e.getSource()==update){
			/*��ˢ�°༶��������ˢ���б�
			 * */
			UpdataClassMSG();
			
			UMM=new UserMsgModel();
			 String sql3 ="select * from XustPost";
			  UMM.queryUser(sql3,this.TableParas,DBTable);
			jtb.setModel(UMM);//��ȡ�µ�����ģ�� 		
		}
		if(e.getSource()==jb1){
			String UserPost =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(UserPost.equals("")){
				  sql ="select * from XustPost";
				  UMM.queryUser(sql,this.TableParas,DBTable);
				  jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
			}else{
			 sql="select * from XustPost where UserPost='"+UserPost+"'";
			 /*
			  * ���ƽ����������ҵ�ID�������������ʾ��
			  * */
			 SqlHelper sqlhelp=SqlHelper.getInstance();
				String[] UserPosts={UserPost};
				 if(sqlhelp.CheckExist(UserPosts, "XustPostTable")){
					 UMM.queryUser(sql,this.TableParas,DBTable);
					 jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
				 }
				 else
					 JOptionPane.showMessageDialog(this, "�޸ò��ż�¼��");
				 
			}
		
			
		}
		
	}

}

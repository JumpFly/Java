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
	FileControl FileCon=null;
	ResultSet rs=null;
	JPanel jp;
	JButton Add,Delete,update;//������ɾ����ˢ��
	JButton MsgFileGetIn,MsgFileGetOut;
	JTable jtb=null;
	JScrollPane jsp=null;
	
	JPanel jp1,jp2;
	JLabel jl;
	JButton jb1,jb2;
	JTextField jtf;
	
	UserMsgModel UMM;
	DBMsg 	dbMsg=new DBMsg();
	
	String DBTable="ClassTable";
	String []TableParas=dbMsg.ClassTable;
	ClassAddDiaLog CAd=null;

	public  String[] ReturnClassIDs(){
		String ClassIDs="";
		SqlHelper sqlhelp= new SqlHelper();
		try {
			String sql="select ClassID from Class";
			rs=sqlhelp.queryExecute(sql);
			while(rs.next()){
				ClassIDs+=rs.getString(1)+" ";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlhelp.DBclose();
		}
		return ClassIDs.split(" ");
	}
	
	
	public  void UpdataClassMSG(){
		String sql="",sql2="";
		SqlHelper sqlhelp=new SqlHelper();
		String[]	ClassIDs=ReturnClassIDs();
	
		for (int i = 0; i < ClassIDs.length; i++) {
			int NUM=0;
			sql="select *from Student where ClassID="+"'"+ClassIDs[i]+"'";
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
			sql2="update Class set PersonNum="+"'"+NUM+"'"+" where ClassID="+"'"+ClassIDs[i]+"'";
				try {
				sqlhelp.queryExe(sql2);
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally {
				sqlhelp.DBclose();
			}
		}
	}
	public RoomMenu(){
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
		jl=new JLabel("��������");
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
		  String sql ="select * from Class";
		  UMM.queryUser(sql,this.TableParas,DBTable);
		
		jtb=new JTable(UMM);//��ģ�ͼ��뵽JTable
		
		jsp=new JScrollPane(jtb);
		
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new RoomMenu();

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
			CAd=new ClassAddDiaLog(this, "�½��༶", false);
			
			
		}else if(e.getSource()==Delete){
			//ɾ��
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			//ת����String �����model
			String ID=(String)UMM.getValueAt(rowNum, 0);
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"�Ƿ����ɾ����", "��ѡ��..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String sql ="delete from Class where ClassID=?";
						String[] paras={ID};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "ɾ���ɹ���");
						}else {
							JOptionPane.showMessageDialog(this, "ɾ��ʧ�ܣ�");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from Class";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
						
					   }
			
			
		}else if(e.getSource()==update){
			/*��ˢ�°༶��������ˢ���б�
			 * */
			UpdataClassMSG();
			
			UMM=new UserMsgModel();
			 String sql3 ="select * from Class";
			  UMM.queryUser(sql3,this.TableParas,DBTable);
			jtb.setModel(UMM);//��ȡ�µ�����ģ�� 		
		}
		if(e.getSource()==jb1){
			String ID =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(ID.equals("")){
				  sql ="select * from Class";
				  UMM.queryUser(sql,this.TableParas,DBTable);
			}else{
			 sql="select * from Class where ClassID="+ID;
			 /*
			  * �п���ø���һ�½����������ҵ�ID�������������ʾ��
			  * */
			 
			  UMM.queryUser(sql,this.TableParas,DBTable);
			}
		
			jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
		}
		
	}

}

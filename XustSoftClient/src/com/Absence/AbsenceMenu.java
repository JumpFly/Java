package com.Absence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class AbsenceMenu extends JFrame implements ActionListener{

	private FileControl FileCon=null;
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
	private String UserType;
	private String DBTable="AbsenceTable";
	private String []TableParas=dbMsg.AbsenceTable;
	public AbsenceMenu(String Type){
		this.UserType=Type;
		
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
		jl=new JLabel("������ѧ��");
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
		  String sql ="select * from Absence";
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
		this.setTitle("ȱ�ڹ���");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new AbsenceMenu("����Ա");
		
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
			
			AbsenceDiaLog test=new AbsenceDiaLog(this, "ȱ�ڼ�¼", false);
			
			
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
						String sql ="delete from Absence where UserNum=?";
						String[] paras={ID};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "ɾ���ɹ���");
						}else {
							JOptionPane.showMessageDialog(this, "ɾ��ʧ�ܣ�");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from Absence";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
						
					   }
			
			
		}else if(e.getSource()==update){
			
			UMM=new UserMsgModel();
			 String sql2 ="select * from Absence";
			  UMM.queryUser(sql2,this.TableParas,DBTable);
			jtb.setModel(UMM);//��ȡ�µ�����ģ�� 		
		}
		if(e.getSource()==jb1){
			String UserNum =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(UserNum.equals("")){
				  sql ="select * from Absence";
				  UMM.queryUser(sql,this.TableParas,DBTable);
				  jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
			}else{
			 sql="select * from Absence where UserNum='"+UserNum+"'";
			
			 SqlHelper sqlhelp=SqlHelper.getInstance();
			String[] UserNums={UserNum};
			 if(sqlhelp.CheckExist(UserNums, "AbsenceTable_Up")){
				 UMM.queryUser(sql,this.TableParas,DBTable);
				 jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
			 }
				
			 else
				 JOptionPane.showMessageDialog(this, "�޸�ѧ�ż�¼��");
			 
			 
			}
		
			
		}
		
	}

}

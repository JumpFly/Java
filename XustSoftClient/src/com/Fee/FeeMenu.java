package com.Fee;

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.Stu.StuAddDiaLog;
import com.Tools.*;
import com.User.UserMsgModel;

public class FeeMenu extends JFrame implements ActionListener{

	FileControl FileCon=null;
	ResultSet rs=null;
	Date newDa=null;
	JPanel jp;
	JButton Add,Delete,update;//������ɾ����ˢ��
	JButton FeeGetIn,FeeGetOut;
	JButton MsgFileGetIn,MsgFileGetOut;
	JTable jtb=null;
	JScrollPane jsp=null;
	
	JPanel jp1,jp2;
	JLabel jl;
	JButton jb1,jb2;
	JTextField jtf;
	
	UserMsgModel UMM;
	DBMsg 	dbMsg=new DBMsg();
	String UserType;
	String DBTable="FeeTable";
	String []TableParas=dbMsg.FeeTable;
	FeeAddDiaLog test=null;
	public FeeMenu(String Type){
		this.UserType=Type;
		Add=new JButton("����");
		Add.addActionListener(this);
		Delete=new JButton("ɾ��");
		Delete.addActionListener(this);
		FeeGetIn =new JButton("�շ�");
		FeeGetIn.addActionListener(this);
		FeeGetOut =new JButton("�˷�");
		FeeGetOut.addActionListener(this);
	
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
		jp2.add(FeeGetIn);
		jp2.add(FeeGetOut);
		jp2.add(Delete);

		
		//��������ģ�Ͷ���
		  UMM=new UserMsgModel();
		  String sql ="select * from Fee";
		  UMM.queryUser(sql,this.TableParas,DBTable);
		
		jtb=new JTable(UMM);//��ģ�ͼ��뵽JTable
		
		jsp=new JScrollPane(jtb);
		
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setTitle("���ù���");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new FeeMenu("����Ա");
		
	}

	public void GetMoney(String ID,float money){
		String sql="update Person set UserCoin=UserCoin-? where UserID=?";
		
		String[] IDs={ID};
		SqlHelper sqlhelp=new SqlHelper();
		if(!sqlhelp.CheckExist(IDs, "PersonTable")){
			JOptionPane.showMessageDialog(this, "Person�����޴��û���");
			return;
		}  
		try {
			String[] paras={String.valueOf(money),ID};
			if(sqlhelp.EditExec(sql, paras, "PersonTable"))
				JOptionPane.showMessageDialog(this, "��ͬѧ�շѳɹ���");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void BackMoney(String ID,int month){
		String sql="update Fee set BackFee=BackFee+? where UserID=? and Month=?";
		String MsgDa="";

		String[] IDs={ID};
		SqlHelper sqlhelp=new SqlHelper();
		if(!sqlhelp.CheckExist(IDs, "AbsenceTable_Up")){
			JOptionPane.showMessageDialog(this, "���û���ȱ��,�����˷ѣ�");
			return;
		}  
		try {
			int Num=0;
			String sql2="select * from Absence where UserID="+ID;
			rs=sqlhelp.queryExecute(sql2);
			while(rs.next()){
				MsgDa=rs.getString(2);
				newDa=new SimpleDateFormat("yyyy-MM-dd").parse(MsgDa);
				int MM=newDa.getMonth()+1;
				if(MM==month){
					Num++;
				}
			}
			
			
			String[] paras={String.valueOf(Num*10),ID,String.valueOf(month)};
			if(sqlhelp.EditExec(sql, paras, "FeeTable_Up"))
				JOptionPane.showMessageDialog(this, "��ͬѧ�˷ѳɹ���");
			String sql3="delete from Absence where UserID=? and UserDate like "+"'%"+month+"%'";
				sqlhelp.EditExec(sql3, IDs, "delete");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==FeeGetIn){
			if(!this.UserType.equals("������Ա")&&!this.UserType.equals("����Ա")){
				JOptionPane.showMessageDialog(this, this.UserType+"�޴�Ȩ�ޣ�");
				return;
			}
			//ɾ��
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			//ת����String �����model
			String ID=(String)UMM.getValueAt(rowNum, 0);
			float money=0;
			for (int i = 2; i < 7; i++) {
				money+=(float)UMM.getValueAt(rowNum, i);
				
			}
			GetMoney(ID, money);
			
			
			
			
		}else if(e.getSource()==FeeGetOut){
			if(!this.UserType.equals("������Ա")&&!this.UserType.equals("����Ա")){
				JOptionPane.showMessageDialog(this, this.UserType+"�޴�Ȩ�ޣ�");
				return;
			}
			//ɾ��
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			//ת����String �����model
			String ID=(String)UMM.getValueAt(rowNum, 0);
			int month=(int)UMM.getValueAt(rowNum, 1);
			BackMoney(ID, month);
		}
		
		
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
			if(!this.UserType.equals("������Ա")&&!this.UserType.equals("����Ա")){
				JOptionPane.showMessageDialog(this, this.UserType+"�޴�Ȩ�ޣ�");
			}
			else{
				 test=new FeeAddDiaLog(this, "���ù���", false);
			}
			
		}else if(e.getSource()==Delete){
			if(!this.UserType.equals("������Ա")&&!this.UserType.equals("����Ա")){
				JOptionPane.showMessageDialog(this, this.UserType+"�޴�Ȩ�ޣ�");
				return;
			}
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
						String sql ="delete from Fee where UserID=?";
						String[] paras={ID};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "ɾ���ɹ���");
						}else {
							JOptionPane.showMessageDialog(this, "ɾ��ʧ�ܣ�");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from Fee";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
						
					   }
			
			
		}else if(e.getSource()==update){
			
			UMM=new UserMsgModel();
			 String sql2 ="select * from Fee";
			  UMM.queryUser(sql2,this.TableParas,DBTable);
			jtb.setModel(UMM);//��ȡ�µ�����ģ�� 		
		}else if(e.getSource()==FeeGetIn){
		//	\\�շ�
		}else if(e.getSource()==FeeGetOut){
			
		//	\\�˷�	
		}
		if(e.getSource()==jb1){
			String ID =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(ID.equals("")){
				  sql ="select * from Fee";
				  UMM.queryUser(sql,this.TableParas,DBTable);
			}else{
			 sql="select * from Fee where UserID="+ID;
			 /*
			  * �п���ø���һ�½����������ҵ�ID�������������ʾ��
			  * */
			 
			  UMM.queryUser(sql,this.TableParas,DBTable);
			}
		
			jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
		}
		
	}

}

package com.Stu;
import java.awt.BorderLayout;
import com.User.*;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.Tools.*;
public class StuUpDiaLog extends JDialog implements ActionListener{

	ResultSet rs=null;
	String [] ClassIDs=null;
	JComboBox DownList=null;	
	JLabel jl[] =new JLabel[6];
	JTextField jtf[]=new JTextField[5];
	JButton Choose,Cancel,ResBtn;//ȷ�ϡ�ȡ��������
	 JPanel jp1,jp2,jp3;
	String StuTable[] = {"ѧ��","����","�Ա�","����","��ѧ����","�༶��"};
	Font myFont=null;
	Date NowDate;
	String FormDate;
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	
	public String[] ReturnClassIDs(){
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
	
	public StuUpDiaLog(Frame owner,String title,boolean model ,UserMsgModel umm,int rowNum ){
		super(owner,title,model); //���๹�췽����ģʽ�Ի���Ч��
myFont =new Font("����",Font.PLAIN,18);
		
		NowDate=new Date();
		FormDate=df.format(NowDate);
		ClassIDs=ReturnClassIDs();
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(6, 1));
		jp2.setLayout(new GridLayout(6, 1));
		
		for(int i=0;i<jl.length;i++){
			jl[i]=new JLabel(StuTable[i]);
			jl[i].setFont(myFont); //��������
			jp1.add(jl[i]);
		}
		//��ʾģ���б�ѡ�е����е��û���Ϣ
		for(int i=0;i<jtf.length;i++){
			jtf[i]=new JTextField();
			jtf[i].setText(umm.getValueAt(rowNum, i)+"");
			jp2.add(jtf[i]);
		}
		jtf[0].setEditable(false);//jtf0�����޸�
		jtf[4].setText((String)umm.getValueAt(rowNum, 4));
		DownList=new JComboBox(ClassIDs);
		jp2.add(DownList);
		
		Choose=new JButton("ȷ��");
		Cancel=new JButton("ȡ��");
		ResBtn=new JButton("����");
		Choose.addActionListener(this);
		Cancel.addActionListener(this);
		ResBtn.addActionListener(this);
		
		jp3.add(ResBtn);
		jp3.add(Choose);
		jp3.add(Cancel);
		
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		this.setLocation(800, 300);
		this.setSize(300,400);
		this.setResizable(false);
		this.setVisible(true);
		 this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	
	
	}
	public static void main(String[] args) {
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Choose){
			String ClassID=  (String)DownList.getSelectedItem();
			String strsql=
					"update Student set UserName=? ,UserSex=? ,UserAge=? ,UserDate=? ,ClassID=? where UserID=?";
			
			String []paras=
				{jtf[1].getText(),jtf[2].getText(),jtf[3].getText(),jtf[4].getText(),ClassID,jtf[0].getText()};
				/*
				 * �����༶����Ҫ���±����Ϣ����������������
				 * */
			
			UserMsgModel temp=new UserMsgModel();
			if(!temp.EditUser(strsql, paras,"StuTable_update")){
				JOptionPane.showMessageDialog(this, "�޸�ʧ�ܣ�");
			  }else{
				  JOptionPane.showMessageDialog(this, "�޸ĳɹ���");
					 
			  }
			this.dispose();
			}
			if(e.getSource()==Cancel){
				this.dispose();
			 	}
			if(e.getSource()==ResBtn){
				for(int i=1;i<4;i++){
					jtf[i].setText("");
				}
			 }
	}
	
		
	}
	
	


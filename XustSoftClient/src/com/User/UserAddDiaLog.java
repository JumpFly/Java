package com.User;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.Tools.*;
public class UserAddDiaLog extends JDialog implements ActionListener{

	private JLabel jl[] =new JLabel[6];
	private JTextField jtf1,jtf2,jtf3,jtf4,jtf5;
	private JButton Choose,Cancel;//ȷ�ϡ�ȡ��
	private JComboBox DownList=null;
	private JScrollPane jsp;
	private JPanel jp1,jp2,jp3;
	private String strs[]={"�û��˺�","�û�����","�û�����","�û�����","�û�����","������"};
	private String [] Type={"�ǻ�Ա","����Ա","��Ա"};
	
	public static void main(String[] args) {
		UserAddDiaLog ua=new UserAddDiaLog(null,"123",true);
	}
	public UserAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //���๹�췽����ģʽ�Ի���Ч��
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(6, 1));
		jp2.setLayout(new GridLayout(6, 1));
		
		
		for(int i=0;i<strs.length;i++){
			
			jl[i]=new JLabel(strs[i]);
			jp1.add(jl[i]);

			}
		
		jtf1=new JTextField();
		jtf2=new JTextField();
		jtf3=new JTextField();
		jtf4=new JTextField();
		jtf5=new JTextField();
		DownList=new JComboBox(Type);
		
		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(DownList);
		jp2.add(jtf3);
		jp2.add(jtf4);
		jp2.add(jtf5);
		DownList.addActionListener(this);
		jtf5.setText("������������������");
		jtf5.setEnabled(false);
		
		Choose=new JButton("ȷ��");
		Cancel=new JButton("ȡ��");
		Choose.addActionListener(this);
		Cancel.addActionListener(this);
		
		
		jp3.add(Choose);
		jp3.add(Cancel);
		
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		this.setLocation(800, 300);
		this.setSize(320,320);
		this.setVisible(true);
		this.setResizable(false);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(DownList.getSelectedIndex()!=0){	
			jtf5.setEnabled(true);
		}else {
			jtf5.setText("������������������");
			jtf5.setEnabled(false);
		}
		if(e.getSource()==Choose){
			if(TextFieldIsEmpty()==false)
			{
				JOptionPane.showMessageDialog(this, "����д������");
				return;
			}
			UserMsgModel temp=new UserMsgModel();
			String sql="insert into Person values(?,?,?,?,?)";
			String Type=  (String)DownList.getSelectedItem();
		String []paras=
			{jtf1.getText().trim(),jtf2.getText().trim(),Type,jtf3.getText().trim(),jtf4.getText().trim()};
		System.out.println(jtf1.getText()+"  "+jtf2.getText()+"   "+Type+"   "+jtf3.getText()+"  "+jtf4.getText());
		if(!Type.equals("�ǻ�Ա")){
			String VerifMsg=jtf5.getText().trim();
			if(VerifMsg.equals(""))
				JOptionPane.showMessageDialog(this, "����д�����룡");
			else if(!VerifMsg.equals("xustsoft"))
				JOptionPane.showMessageDialog(this, "���������");
			else {
				SqlHelper sqlhelp =SqlHelper.getInstance();
				String[] IDs={jtf1.getText().trim()};
				String[] Nums={jtf3.getText().trim()};
				if(sqlhelp.CheckExist(IDs,"PersonTable")==true||sqlhelp.CheckExist(Nums,"PersonTable_Num")==true){
					JOptionPane.showMessageDialog(this, "��ID�򹤺��Ѵ��ڣ�");
				}else{
				if(!temp.EditUser(sql, paras,"PersonTable")){
					JOptionPane.showMessageDialog(this, "���ʧ�ܣ�");
				  }else{
					  JOptionPane.showMessageDialog(this, "��ӳɹ���");
						 
				  }
				}
			}
		}else {
			SqlHelper sqlhelp =SqlHelper.getInstance();
			String[] IDs={jtf1.getText().trim(),jtf3.getText().trim()};
			if(sqlhelp.CheckExist(IDs,"PersonTable")==true){
				JOptionPane.showMessageDialog(this, "��ID�򹤺��Ѵ��ڣ�");
			}
			else{
			if(!temp.EditUser(sql, paras,"PersonTable")){
				JOptionPane.showMessageDialog(this, "���ʧ�ܣ�");
			  }else{
				  JOptionPane.showMessageDialog(this, "��ӳɹ���");
				  String sql2="insert into DetailMsg (UserID,UserNum,UserMail) values('"+jtf1.getText().trim()+"','"+jtf3.getText().trim()+"','"+jtf4.getText().trim()+"')";
				  sqlhelp.queryExe(sql2); 
				  sqlhelp.DBclose();
			  }
			}
		
		}
		}
		
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	public boolean TextFieldIsEmpty(){
		if(jtf1.getText().trim().equals("")||jtf2.getText().trim().equals("")||jtf3.getText().trim().equals("")||jtf4.getText().trim().equals(""))
			return false;
		else
			return true;
	}
	
}

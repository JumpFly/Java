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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.Tools.*;
public class UserAddDiaLog extends JDialog implements ActionListener{

	JLabel jl[] =new JLabel[3];
	JTextField jtf1,jtf2;
	JButton Choose,Cancel;//ȷ�ϡ�ȡ��
	JList jlist; //�б��
	 JScrollPane jsp;
	 JPanel jp1,jp2,jp3;
	 String strs[]={"�û��˺�","�û�����","�û�����"};
	String [] Type={"����Ա","У������","������Ա","�ҳ�"};
	
	public static void main(String[] args) {
		UserAddDiaLog ua=new UserAddDiaLog(null,"123",false);
	}
	public UserAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //���๹�췽����ģʽ�Ի���Ч��
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(3, 1));
		jp2.setLayout(new GridLayout(3, 1));
		
		
		for(int i=0;i<strs.length;i++){
			
			jl[i]=new JLabel(strs[i]);
			jp1.add(jl[i]);

			}
		
		jtf1=new JTextField();
		jtf2=new JTextField();
		jlist=new JList(Type);
		jlist.setVisibleRowCount(2);//���û�������ʾ������
		jsp=new JScrollPane(jlist);
		
		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jsp);
	
		
		
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
		this.setSize(400,220);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Choose){
			UserMsgModel temp=new UserMsgModel();
			String sql="insert into Person values(?,?,?,?)";
			String Type=  (String)jlist.getSelectedValue();
		String []paras=
			{jtf1.getText(),jtf2.getText(),Type,""};
		System.out.println(jtf1.getText()+"  "+jtf2.getText()+"   "+Type);
		if(!Type.equals("�ҳ�")){
			JOptionPane.showMessageDialog(this, "�ǹ���Աֻ����ע��ҳ����ͣ�");
		}else {
			SqlHelper sqlhelp =new SqlHelper();
			String[] IDs={jtf1.getText()};
			if(sqlhelp.CheckExist(IDs,"PersonTable")==true){
				JOptionPane.showMessageDialog(this, "��ID�Ѵ��ڣ�");
			}else{
			if(!temp.EditUser(sql, paras,"PersonTable")){
				JOptionPane.showMessageDialog(this, "���ʧ�ܣ�");
			  }else{
				  
				  JOptionPane.showMessageDialog(this, "��ӳɹ���");
					 
			  }
		}
		
		}
		}
		
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	
}

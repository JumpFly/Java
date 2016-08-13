package com.Room;
import com.User.*;
import java.awt.BorderLayout;
import java.awt.Font;
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
public class ClassAddDiaLog extends JDialog implements ActionListener{

	JLabel jl[] =new JLabel[3];
	JTextField jtf1,jtf2,jtf3;
	JButton Choose,Cancel;//ȷ�ϡ�ȡ��
	 JScrollPane jsp;
	 JPanel jp1,jp2,jp3;
	 String[] ClassTable = {"�༶��","�༶��","����"};
		Font myFont=null;
		
	public static void main(String[] args) {
		ClassAddDiaLog ua=new ClassAddDiaLog(null,"123",false);
	}
	
	public ClassAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //���๹�췽����ģʽ�Ի���Ч��
		myFont =new Font("����",Font.PLAIN,18);
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(3, 1));
		jp2.setLayout(new GridLayout(3, 1));
		
		
		for(int i=0;i<ClassTable.length;i++){
			
			jl[i]=new JLabel(ClassTable[i]);
			jl[i].setFont(myFont); //��������
			jp1.add(jl[i]);

			}
		
		jtf1=new JTextField();
		jtf2=new JTextField();
		jtf3=new JTextField();
		
		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jtf3);
	
		
		
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
			String sql="insert into Class values(?,?,?)";
		String []paras=
			{jtf1.getText(),jtf2.getText(),jtf3.getText()};
		System.out.println(jtf1.getText()+"  "+jtf2.getText()+"   "+jtf3.getText());
		
			SqlHelper sqlhelp =SqlHelper.getInstance();
			String ClassID="'"+jtf1.getText().trim()+"'";
			String[] IDs={ClassID};
			if(sqlhelp.CheckExist(IDs,"ClassTable")==true){
				JOptionPane.showMessageDialog(this, "��ID�Ѵ��ڣ�");
			}else{
			if(!temp.EditUser(sql, paras,"ClassTable")){
				JOptionPane.showMessageDialog(this, "���ʧ�ܣ�");
			  }else{
				  
				  JOptionPane.showMessageDialog(this, "��ӳɹ���");
				  this.dispose(); 
			  }
		 }
		}
		
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	
}

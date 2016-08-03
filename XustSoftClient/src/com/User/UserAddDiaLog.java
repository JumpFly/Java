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
	JButton Choose,Cancel;//确认、取消
	JList jlist; //列表框
	 JScrollPane jsp;
	 JPanel jp1,jp2,jp3;
	 String strs[]={"用户账号","用户密码","用户类型"};
	String [] Type={"管理员","校长助理","财务人员","家长"};
	
	public static void main(String[] args) {
		UserAddDiaLog ua=new UserAddDiaLog(null,"123",false);
	}
	public UserAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		
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
		jlist.setVisibleRowCount(2);//设置滑动框显示多少行
		jsp=new JScrollPane(jlist);
		
		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jsp);
	
		
		
		Choose=new JButton("确定");
		Cancel=new JButton("取消");
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
		if(!Type.equals("家长")){
			JOptionPane.showMessageDialog(this, "非管理员只允许注册家长类型！");
		}else {
			SqlHelper sqlhelp =new SqlHelper();
			String[] IDs={jtf1.getText()};
			if(sqlhelp.CheckExist(IDs,"PersonTable")==true){
				JOptionPane.showMessageDialog(this, "该ID已存在！");
			}else{
			if(!temp.EditUser(sql, paras,"PersonTable")){
				JOptionPane.showMessageDialog(this, "添加失败！");
			  }else{
				  
				  JOptionPane.showMessageDialog(this, "添加成功！");
					 
			  }
		}
		
		}
		}
		
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	
}

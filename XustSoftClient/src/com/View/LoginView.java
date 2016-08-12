package com.View;
/*
 * 网络工程1402 陈志威
 * 项目起始于2016.7.2
 * 项目完成于2016.7.4
 * 耗时3天
 * 挂接sql server2008R2
 * 
 * */
import com.User.*; 
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Tools.*;

public class LoginView extends JFrame implements ActionListener{
	
	ResultSet rs=null;
	UserAddDiaLog AddDialog=null;
	UserMailForPassword FindPassword=null;
	JLabel BG=null; //设置背景图
	JLabel UserID,Pass,UserType;
	JButton jb1,jb2,jb3;//登录注册 忘记密码
	JTextField jtf_ID=null;
	JPasswordField jpf_pass=null;
	JPanel myJPanel,myJPanel2;
	String [] Type={"非会员","管理员","会员"};
	JComboBox DownList=null;	
	Font myFont=null;
	
	public LoginView(){

		myFont =new Font("黑体",Font.PLAIN,20);
		jtf_ID=new JTextField(20);
		jtf_ID.addActionListener(this);
		jpf_pass=new JPasswordField(20);
		jpf_pass.addActionListener(this);
		jb1=new JButton("登录");
		jb2=new JButton("注册");
		jb3=new JButton("忘记密码");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		BG=new JLabel(new ImageIcon("images/BG.png"));
		
		UserID=new JLabel("登录  ID：",JLabel.CENTER);
		UserID.setFont(myFont); //设置字体
		UserID.setForeground(Color.blue);//设置字体颜色方法
		
		Pass=new JLabel("登录密码：",JLabel.CENTER);
	    Pass.setFont(myFont); //设置字体
		Pass.setForeground(Color.blue);//设置字体颜色方法
		
		UserType=new JLabel("用户类型：",JLabel.CENTER);
		UserType.setFont(myFont); //设置字体
		UserType.setForeground(Color.blue);//设置字体颜色方法
		DownList=new JComboBox(Type);
		
		this.setLayout(new GridLayout(2, 1));
		
		UserID.setBounds(18, 20, 150, 60);
		Pass.setBounds(18, 85, 150, 60);
		UserType.setBounds(18, 135, 150, 80);
		jtf_ID.setBounds(150, 25, 180, 50);
		jpf_pass.setBounds(150, 95, 180, 50);
		DownList.setBounds(150, 160, 180, 50);
		jb1.setBounds(20, 225, 100, 40);
		jb2.setBounds(145, 225, 100, 40);
		jb3.setBounds(270, 225, 100, 40);
		this.add(BG);
	
		myJPanel=new JPanel(null);
		myJPanel.add(UserID);
		myJPanel.add(Pass);
		myJPanel.add(jb1);
		myJPanel.add(jb2);
		myJPanel.add(jb3);
		myJPanel.add(jtf_ID);
		myJPanel.add(jpf_pass);
		myJPanel.add(UserType);
		myJPanel.add(DownList);
		this.add(myJPanel);
		
	 //设置窗口
	  ImageIcon icon =new ImageIcon("images/Lock.png");
	  this.setIconImage(icon.getImage());//窗口图标
	  this.setTitle("用户登录");
	     this.setSize(410,600);
	     this.setLocation(800,200);
	     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   this.setResizable(false);
	    //显示窗体
	     this.setVisible(true);
	}  
	  
	public static void main(String[] args) {
		LoginView LV=new LoginView();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb1){
			String Pass=new String(jpf_pass.getPassword()),UserType="",UserPass="";
			String UserID=jtf_ID.getText().trim();
			String Type=  (String)DownList.getSelectedItem();
			String UserCoins="";
			SqlHelper sqlhelp= new SqlHelper();
			try {
				rs=sqlhelp.queryExec(UserID);		
				while(rs.next()){
					UserPass=rs.getString(2);
					UserType=rs.getString(3);
				}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if(Pass.equals(UserPass)&&Type.equals(UserType)){
				JOptionPane.showMessageDialog(this, "登录成功！");
				System.out.println(UserID+"  "+UserPass+"  "+UserType);
				sqlhelp.DBclose();
				ControlMenu CCM=new ControlMenu(UserID,UserType);
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(this, "ID/密码/类型 错误！");
			}
		}	
		if(e.getSource()==jb2){
			AddDialog=new UserAddDiaLog(null, "注册用户", true);
		}
		if(e.getSource()==jb3){
			FindPassword=new UserMailForPassword(null, "找回密码", true);
		}
		
	}
		
	}

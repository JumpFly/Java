package com.View;
import com.Stu.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.Room.RoomMenu;
import com.Absence.*;
import com.Fee.FeeMenu;
public class ControlMenu extends JFrame implements ActionListener {

	JPanel panel1=null;
	String UserID,UserType;
	Date NowDate;
	String FormDate;
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar ca=Calendar.getInstance();//初始化日历
	int month; // get的0-11 代表1-12月
	
	JLabel Photo=null,BG=null;
	JLabel ID,Type,LDate=new JLabel();
	JButton jb1,jb2,jb3,jb4;
	Font myFont=null;
	
	StuMenu test1=null;
	RoomMenu test2=null;
	FeeMenu FM=null;
	AbsenceMenu test4=null;
	
	public String getTime(){
		ca=Calendar.getInstance();
		NowDate=(Date)ca.getTime();
		FormDate=df.format(NowDate);
		return FormDate;
	}
	 private class RemindTask extends TimerTask {
         public void run() {
          LDate.setText("Date  ："+ getTime());
         }
    }
	
	 
	public ControlMenu(String UserID,String UserType){
	
		myFont =new Font("黑体",Font.PLAIN,20);
		this.UserID=UserID;
		this.UserType=UserType;
		NowDate=new Date();
		ca.setTime(NowDate); //设置日历时间
		this.month=ca.get(Calendar.MONTH)+1;
		FormDate=df.format(NowDate);
		Photo=new JLabel(new ImageIcon("images/XUST.png"));
		BG=new JLabel(new ImageIcon("images/BG3.jpg"));
		ID=new JLabel("UserID   :"+ UserID);
		ID.setFont(myFont); //设置字体
		ID.setForeground(Color.blue);
		
		Type=new JLabel("UserType ："+ UserType);
		Type.setFont(myFont); 
		Type.setForeground(Color.blue);
		LDate=new JLabel("Date  ："+ FormDate);
		LDate.setFont(myFont); 
		LDate.setForeground(Color.blue);
		
		jb1=new JButton("学生管理");
		jb1.addActionListener(this);
		jb2=new JButton("部门管理");
		jb2.addActionListener(this);
		jb3=new JButton("财务管理");
		jb3.addActionListener(this);
		jb4=new JButton("缺勤查询");
		jb4.addActionListener(this);
		
		this.setLayout(null);
		BG.setBounds(0, 0, 410, 600);
		Photo.setBounds(0, 0, 100, 100);
		ID.setBounds(110, 10, 180, 30);
		Type.setBounds(110,45, 180, 30);
		LDate.setBounds(110, 75, 300, 30);
		
		jb1.setBounds(8,130, 100, 50);
		jb2.setBounds(8,240, 100, 50);
		jb3.setBounds(8,350, 100, 50);
		jb4.setBounds(8,460, 100, 50);
		
		this.add(Photo);
		this.add(ID);
	
		this.add(Type);
		this.add(LDate);
		this.add(jb1);
		this.add(jb2);
		this.add(jb3);
		this.add(jb4);
		this.add(BG);
		 //设置窗口
		  ImageIcon icon =new ImageIcon("images/Shield.jpg");
		  this.setIconImage(icon.getImage());//窗口图标
		  this.setTitle("管理界面");
		  this.setSize(410,600);
		  this.setLocation(900,200);
		  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  this.setResizable(true);
		  this.setVisible(true);
		     
		  Timer timer = new Timer();
		  timer.schedule(new RemindTask(), 0, 1000);
	}
	
	

	public static void main(String[] args) {
		ControlMenu CCM=new ControlMenu("jumpfly","管理员");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb1){
			
				 test1=new StuMenu(UserType);
			
			
		}
		if(e.getSource()==jb2){
			
				test2=new RoomMenu(UserType);
				
		}
		if(e.getSource()==jb3){
			
				FM=new FeeMenu(UserType);
			
		}
		if(e.getSource()==jb4){
			test4=new AbsenceMenu(UserType);
		}
		
	}

}

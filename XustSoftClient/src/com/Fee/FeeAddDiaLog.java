package com.Fee;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.Tools.*;
public class FeeAddDiaLog extends JDialog implements ActionListener{

	JLabel jl[] =new JLabel[9];
	JTextField jtf[]=new JTextField[8];
	JButton Choose,Cancel,ResBtn;//确认、取消
	 JScrollPane jsp;
	 JPanel jp1,jp2,jp3;
	 String[] FeeTable = {"学号","月份","住宿费","伙食费","书本费","空调费","暖气费","退费","父母职称"};
	 String[] HomeType={"单职工","双职工","无职"};
	 JComboBox DownList=null;		
	 Font myFont=null;
		Date NowDate;
		String FormDate;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca=Calendar.getInstance();//初始化日历
		int month; // get的0-11 代表1-12月
		
	public static void main(String[] args) {
		FeeAddDiaLog ua=new FeeAddDiaLog(null,"123",false);
	}
	
	public FeeAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		myFont =new Font("黑体",Font.PLAIN,18);
		NowDate =new Date();
		ca.setTime(NowDate); //设置日历时间
		this.month=ca.get(Calendar.MONTH)+1;
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(9, 1));
		jp2.setLayout(new GridLayout(9, 1));
		
		
		for(int i=0;i<FeeTable.length;i++){
			
			jl[i]=new JLabel(FeeTable[i]);
			jl[i].setFont(myFont); //设置字体
			jp1.add(jl[i]);

			}
		for(int i=0;i<jtf.length;i++){
			
			jtf[i]=new JTextField();
			jp2.add(jtf[i]);

			}
		
		
		
		
		jtf[1].setText(this.month+"");
		DownList=new JComboBox(HomeType);
		jp2.add(DownList);
		
		Choose=new JButton("确定");
		Cancel=new JButton("取消");
		ResBtn=new JButton("重置");
		Choose.addActionListener(this);
		Cancel.addActionListener(this);
		ResBtn.addActionListener(this);
		
		jp3.add(Choose);
		jp3.add(Cancel);
		jp3.add(ResBtn);
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		this.setLocation(800, 100);
		this.setSize(300,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Choose){
			String Hometype=  (String)DownList.getSelectedItem();
			UserMsgModel temp=new UserMsgModel();
			String sql="insert into Fee values(?,?,?,?,?,?,?,?,?)";
			String []paras=
			{jtf[0].getText(),jtf[1].getText(),jtf[2].getText(),jtf[3].getText(),jtf[4].getText(),jtf[5].getText(),jtf[6].getText(),jtf[7].getText(),Hometype};
				
			for(int i=0;i<paras.length;i++)
				System.out.print(paras[i]+" ");
		
			SqlHelper sqlhelp =new SqlHelper();
			/*
			 * 注意：缺勤记录 以学号+月份作为主键
			 * */
			String[] IDs={jtf[0].getText(),jtf[1].getText()};
			if(sqlhelp.CheckExist(IDs,"FeeTable")==true){
				JOptionPane.showMessageDialog(this, "该记录已存在！");
			}else{
			if(!temp.EditUser(sql, paras,"FeeTable")){
				JOptionPane.showMessageDialog(this, "添加失败！");
			  }else{
				  
				  JOptionPane.showMessageDialog(this, "添加成功！");
				  
			  }
		 }
		}
		if(e.getSource()==ResBtn){
			for(int i=0;i<jtf.length;i++){
				jtf[i].setText("");
				}
			jtf[1].setText(this.month+"");
		 	}
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	
}

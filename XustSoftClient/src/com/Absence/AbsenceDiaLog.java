package com.Absence;
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
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.Tools.*;
public class AbsenceDiaLog extends JDialog implements ActionListener{

	private JLabel jl[] =new JLabel[3];
	private JTextField jtf1,jtf2,jtf3;
	private JButton Choose,Cancel,ResBtn;//确认、取消
	private JScrollPane jsp;
	private JPanel jp1,jp2,jp3;
	private String[] AbsenceTable =DBMsg.AbsenceTable;
	private Font myFont=null;
	private Date NowDate;
	private String FormDate;
	private SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		
	public static void main(String[] args) {
		AbsenceDiaLog ua=new AbsenceDiaLog(null,"123",false);
	}
	
	public AbsenceDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		myFont =new Font("黑体",Font.PLAIN,18);
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(3, 1));
		jp2.setLayout(new GridLayout(3, 1));
		
		
		for(int i=0;i<AbsenceTable.length;i++){
			
			jl[i]=new JLabel(AbsenceTable[i]);
			jl[i].setFont(myFont); //设置字体
			jp1.add(jl[i]);

			}
		NowDate=new Date();
		FormDate=df.format(NowDate);
		
		jtf1=new JTextField();
		jtf2=new JTextField();
		jtf2.setText(FormDate);
		jtf3=new JTextField();
		
		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jtf3);
	
		
		
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
		
		this.setLocation(800, 300);
		this.setSize(400,220);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
			if(e.getSource()==Choose){
				if(jtf1.getText().trim().equals("")||jtf2.getText().trim().equals("")||jtf3.getText().trim().equals(""))
				{
					JOptionPane.showMessageDialog(this, "请填写完整！");
					return;
				}
			
			
				
				
			UserMsgModel temp=new UserMsgModel();
			String sql="insert into Absence values(?,?,?)";
		String []paras=
			{jtf1.getText(),jtf2.getText(),jtf3.getText()};
		System.out.println(jtf1.getText()+"  "+jtf2.getText()+"   "+jtf3.getText());
		
		
			/*
			 * 注意：缺勤记录 以学号+日期作为主键
			 * */
			SqlHelper sqlhelp =SqlHelper.getInstance();
			String[] IDs={jtf1.getText().trim(),jtf2.getText().trim()};
			String[] UserNum={jtf1.getText().trim()};
			
			if(!sqlhelp.CheckExist(UserNum,"DetailTable_Up")){
				JOptionPane.showMessageDialog(this, "该学号不存在！");
				return;
			}
			
			if(sqlhelp.CheckExist(IDs,"AbsenceTable")==true){
				JOptionPane.showMessageDialog(this, "该学号日期记录已存在！");
			}else{
			if(!temp.EditUser(sql, paras,"AbsenceTable")){
				JOptionPane.showMessageDialog(this, "添加失败！");
			  }else{
				  JOptionPane.showMessageDialog(this, "添加成功！");
				  this.dispose();
			  }
		 }
		}
		if(e.getSource()==ResBtn){
			jtf1.setText("");
			jtf2.setText(FormDate);
			jtf3.setText("");
		 	}
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	
}

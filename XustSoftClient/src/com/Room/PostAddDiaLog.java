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
public class PostAddDiaLog extends JDialog implements ActionListener{

	private  JLabel jl[] =new JLabel[2];
	private  JTextField jtf1,jtf2;
	private  JButton Choose,Cancel;//确认、取消
	private  JScrollPane jsp;
	private  JPanel jp1,jp2,jp3;
	private  String[] XustPostTable =DBMsg.XustPostTable;
	private  Font myFont=null;
		
	public static void main(String[] args) {
		PostAddDiaLog ua=new PostAddDiaLog(null,"123",false);
	}
	
	public PostAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		myFont =new Font("黑体",Font.PLAIN,18);
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(2, 1));
		jp2.setLayout(new GridLayout(2, 1));
		
		
		for(int i=0;i<XustPostTable.length;i++){
			
			jl[i]=new JLabel(XustPostTable[i]);
			jl[i].setFont(myFont); //设置字体
			jp1.add(jl[i]);

			}
		
		jtf1=new JTextField();
		jtf2=new JTextField();
		
		jp2.add(jtf1);
		jp2.add(jtf2);
	
		
		
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
			if(jtf1.getText().trim().equals("")||jtf2.getText().trim().equals("")){
				JOptionPane.showMessageDialog(this, "请填写完整！");
				return;
			}
			UserMsgModel temp=new UserMsgModel();
			String sql="insert into XustPost values(?,?)";
		String []paras=
			{jtf1.getText().trim(),jtf2.getText().trim()};
		System.out.println(jtf1.getText()+"  "+jtf2.getText());
		
			SqlHelper sqlhelp =SqlHelper.getInstance();
			String XustPost=jtf1.getText().trim();
			String[] XustPosts={XustPost};
			if(sqlhelp.CheckExist(XustPosts,"XustPostTable")==true){
				JOptionPane.showMessageDialog(this, "该部门已存在！");
			}else{
			if(!temp.EditUser(sql, paras,"XustPostTable")){
				JOptionPane.showMessageDialog(this, "添加失败！");
			  }else{
				  
				  JOptionPane.showMessageDialog(this, "添加成功！");
				  this.dispose(); 
			  }
		 }
		}
		
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	
}

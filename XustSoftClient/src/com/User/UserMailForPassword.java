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

import javax.mail.Flags.Flag;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.Tools.*;
public class UserMailForPassword extends JDialog implements ActionListener{

	private JLabel jl[] =new JLabel[5];
	private JLabel Tip=null;
	private JTextField jtf1,jtf2,jtf3,jtf4;
	private String newPass="",tempstt="";
	private JButton GetVerif,Choose,Cancel;//确认、取消
	private JComboBox DownList=null;
	private JScrollPane jsp;
	private JPanel jp1,jp2,jp3;
	private String strs[]={"用户账号","新密码","用户邮箱","用户类型","验证码"};
	private String [] Type={"非会员","管理员","会员"};
	private VerifMail vmMail=null;//发送验证码邮件
	private String verify;//验证码
	private SqlHelper sqlhelp=SqlHelper.getInstance();
	private ResultSet rs=null;
	
	private String UserID=null;
	private String UserMail=null;
	private String UserType=null;
	
	private class MySwingWorker extends SwingWorker<Void, Void>{
		private byte Count=60;
		private boolean Flag =false;
		public boolean GetFlag() {
			return Flag;
		}
		
		public MySwingWorker(){
			
		}
		@Override
		protected Void doInBackground() throws Exception {

			 vmMail=new VerifMail(jtf3.getText().trim());
			 verify=vmMail.getVerify();
			  Flag=vmMail.SendMail();
			while(Count>=0)
			{
				 Thread.sleep(1000);
				 GetVerif.setText(Count+" s 可重发");
				 Count--;
			}
			return null;
		}
		@Override
		protected void done() {
			
			super.done();
			 Count=60;
			 GetVerif.setText("获取验证码");
			 GetVerif.setEnabled(true);
		}
	
		
	}
/*	
	private class MyNewpassWorker extends SwingWorker<Void, Void>{
		private boolean Flag =true;
		public boolean GetFlag() {
			return Flag;
		}
		public void SetFlag(boolean flag) {
			 Flag=flag;
		}
		public MyNewpassWorker(){
			
		}
		@Override
		protected Void doInBackground() throws Exception {
			while(Flag){
				if(jtf2.getText()=="")
					newPass="";
					else{
						tempstt=jtf2.getText().trim();
						newPass+=tempstt.charAt(tempstt.length()-1);
						String temp="";
						for (int i = 0; i < tempstt.length(); i++) {
							temp+="*";
						}
						jtf2.setText(temp);
						System.out.println(newPass);
					}
			}
			return null;
		}
		@Override
		protected void done() {
			
			super.done();
		}
	
		
	}
*/
	
	
	public static void main(String[] args) {
		UserMailForPassword ua=new UserMailForPassword(null,"123",true);
	}
	public UserMailForPassword(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(5, 1));
		jp2.setLayout(new GridLayout(5, 1));
		
		
		for(int i=0;i<strs.length;i++){
			
			jl[i]=new JLabel(strs[i]);
			jp1.add(jl[i]);

			}
		
		jtf1=new JTextField();
		jtf2=new JTextField();
	//	Newpassword=new JPasswordField();
		jtf3=new JTextField();
		jtf4=new JTextField();
		DownList=new JComboBox(Type);
	
		
		jp2.add(jtf1);
		jp2.add(jtf2);
	//	jp2.add(Newpassword);
		jp2.add(jtf3);
		jp2.add(DownList);
		jp2.add(jtf4);
		DownList.addActionListener(this);
		GetVerif=new JButton("获取验证码");
		Choose=new JButton("确定");
		Cancel=new JButton("取消");
		GetVerif.addActionListener(this);
		Choose.addActionListener(this);
		Choose.setEnabled(false);
		Cancel.addActionListener(this);
	
		jp3.add(GetVerif);
		jp3.add(Choose);
		jp3.add(Cancel);
		
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		this.setLocation(800, 300);
		this.setSize(320,320);
		this.setVisible(true);
		this.setResizable(false);
//		 NewpassWorker=new MyNewpassWorker();
//		NewpassWorker.execute();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==GetVerif){
			//先验证填写信息是否正确
			 UserID=jtf1.getText().trim();
			 UserMail=jtf3.getText().trim();
			 UserType=(String)DownList.getSelectedItem();
			try {
				rs=sqlhelp.queryExec(UserID);
				if(!rs.next()){
					JOptionPane.showMessageDialog(this, "该账号不存在！");
				}else {
						
					String	UType=rs.getString(3);
					String	UMail=rs.getString(4);
					if(UserMail.equals(UMail)&&UserType.equals(UType)){
						 
						 GetVerif.setEnabled(false);
						 Choose.setEnabled(true);
						 MySwingWorker swingWorker=new MySwingWorker();
						 swingWorker.execute();
						 boolean flag=swingWorker.GetFlag();
						 if(flag==false){
							 JOptionPane.showMessageDialog(this, "发送中途失败"); 
						 }
						 
						 JOptionPane.showMessageDialog(this, "已发送至邮箱！"); 
						
					}else {
						 JOptionPane.showMessageDialog(this, "信息填写有误！"); 
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally {
				sqlhelp.DBclose();
			}
			
		}
		if(e.getSource()==Choose){
			if(jtf4.getText().equals("")){
				JOptionPane.showMessageDialog(this, "请填写验证码！");
			}else if(!jtf4.getText().equals(verify)){
				JOptionPane.showMessageDialog(this, "验证码错误！");
			}else {
				String NewPass=jtf2.getText().trim();
				String []paras=
					{NewPass,UserID};
				String sql="update Person set  PassWord=?  where UserID=?";
				if(!sqlhelp.EditExec(sql, paras, "PersonTable"))
				{JOptionPane.showMessageDialog(this, "密码修改失败！");}
				else{
					  JOptionPane.showMessageDialog(this, "密码修改成功！");
				}
			}
//		----------------------------------------------	
//			
//			UserMsgModel temp=new UserMsgModel();
//			String sql="insert into Person values(?,?,?,?)";
//			String Type=  (String)DownList.getSelectedItem();
//		String []paras=
//			{jtf1.getText(),jtf2.getText(),Type,jtf3.getText()};
//		System.out.println(jtf1.getText()+"  "+jtf2.getText()+"   "+Type);
//		if(!Type.equals("非会员")){
//			String VerifMsg=jtf3.getText().trim();
//			if(VerifMsg.equals(""))
//				JOptionPane.showMessageDialog(this, "请填写邀请码！");
//			else if(!VerifMsg.equals("xustsoft"))
//				JOptionPane.showMessageDialog(this, "邀请码错误！");
//			else {
//				SqlHelper sqlhelp =new SqlHelper();
//				String[] IDs={jtf1.getText()};
//				if(sqlhelp.CheckExist(IDs,"PersonTable")==true){
//					JOptionPane.showMessageDialog(this, "该ID已存在！");
//				}else{
//				if(!temp.EditUser(sql, paras,"PersonTable")){
//					JOptionPane.showMessageDialog(this, "添加失败！");
//				  }else{
//					  JOptionPane.showMessageDialog(this, "添加成功！");
//						 
//				  }
//				}
//			}
//		}else {
//			SqlHelper sqlhelp =new SqlHelper();
//			String[] IDs={jtf1.getText()};
//			if(sqlhelp.CheckExist(IDs,"PersonTable")==true){
//				JOptionPane.showMessageDialog(this, "该ID已存在！");
//			}else{
//			if(!temp.EditUser(sql, paras,"PersonTable")){
//				JOptionPane.showMessageDialog(this, "添加失败！");
//			  }else{
//				  JOptionPane.showMessageDialog(this, "添加成功！");
//					 
//			  }
//			}
//		
//		}
		}
		
		if(e.getSource()==Cancel){
			this.dispose();
			
//			NewpassWorker.SetFlag(false);
		 	}
	}

	
}

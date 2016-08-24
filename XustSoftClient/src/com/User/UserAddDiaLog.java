package com.User;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.commons.codec.digest.DigestUtils;

import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Tools.*;
import com.View.ControlMenu;
import com.View.LoginView;
public class UserAddDiaLog extends JDialog implements ActionListener{

	private JLabel jl[] =new JLabel[7];
	private JTextField jtf1,jtf3,jtf4,jtf5;
	private JButton Choose,Cancel,ReSet;//确认、取消、重置
	private JPasswordField password1,password2;
	private JComboBox DownList=null;
	private JScrollPane jsp;
	private JPanel jp1,jp2,jp3;
	private String strs[]={"用户账号","用户密码","确认密码","用户类型","用户工号","用户邮箱","邀请码"};
	private String [] Type={"非会员","会员"};
	private SendLogOnWorker myLogOnWorker;
	public static void main(String[] args) {
		UserAddDiaLog ua=new UserAddDiaLog(null,"123",true);
	}
	public UserAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(7, 1));
		jp2.setLayout(new GridLayout(7, 1));
		
		
		for(int i=0;i<strs.length;i++){
			
			jl[i]=new JLabel(strs[i]);
			jp1.add(jl[i]);

			}
		
		jtf1=new JTextField();
		password1=new JPasswordField();
		password2=new JPasswordField();
		jtf3=new JTextField();
		jtf4=new JTextField();
		jtf5=new JTextField();
		DownList=new JComboBox(Type);
		
		jp2.add(jtf1);
		jp2.add(password1);
		jp2.add(password2);
		jp2.add(DownList);
		jp2.add(jtf3);
		jp2.add(jtf4);
		jp2.add(jtf5);
		DownList.addActionListener(this);
		jtf5.setText("部分类型需填邀请码");
		jtf5.setEnabled(false);
		
		Choose=new JButton("确定");
		Cancel=new JButton("取消");
		ReSet=new JButton("重置");
		Choose.addActionListener(this);
		Cancel.addActionListener(this);
		ReSet.addActionListener(this);
		
		jp3.add(Choose);
		jp3.add(Cancel);
		jp3.add(ReSet);
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
			jtf5.setText("部分类型需填邀请码");
			jtf5.setEnabled(false);
		}
		if(e.getSource()==Choose){
			if(TextFieldIsEmpty()==false)
			{
				JOptionPane.showMessageDialog(this, "请填写完整！");
				return;
			}else if (!String.copyValueOf(password1.getPassword()).equals(String.copyValueOf(password2.getPassword()))) {
				
				JOptionPane.showMessageDialog(this, "确认密码不统一！");
				return;
			}
			
			String Type=  (String)DownList.getSelectedItem();
		String []paras=
			{jtf1.getText().trim(),String.copyValueOf(password1.getPassword()),Type,jtf3.getText().trim(),jtf4.getText().trim()};
		System.out.println(jtf1.getText()+"  "+String.copyValueOf(password1.getPassword())+"   "+Type+"   "+jtf3.getText()+"  "+jtf4.getText());
		if(!Type.equals("非会员")){
			String VerifMsg=jtf5.getText().trim();
			if(VerifMsg.equals("")){
				JOptionPane.showMessageDialog(this, "请填写邀请码！");
				return ;}
			else if(!VerifMsg.equals("XustSoft")){
				JOptionPane.showMessageDialog(this, "邀请码错误！");
				return ;}

		}
		
		myLogOnWorker=new SendLogOnWorker(jtf1.getText().trim(),String.copyValueOf(password1.getPassword()), Type,jtf3.getText().trim(),jtf4.getText().trim());
		myLogOnWorker.execute();

		}
		
		if(e.getSource()==Cancel){
			ClearFieldText();
			this.dispose();
		 	}
		if(e.getSource()==ReSet){
			ClearFieldText();
		 	}
	}

	public void ClearFieldText(){
		jtf1.setText("");
		jtf3.setText("");
		jtf4.setText("");
		DownList.setSelectedIndex(0);
		password1.setText("");
		password2.setText("");
	}
	public boolean TextFieldIsEmpty(){
		if(jtf1.getText().trim().equals("")||password1.getPassword()==null||password2.getPassword()==null||jtf3.getText().trim().equals("")||jtf4.getText().trim().equals(""))
			return false;
		else
			return true;
	}

	
	private class SendLogOnWorker extends SwingWorker<Void, Void>{
	
		private String Pass,UserID,Type,UserNum,UserMail;
		public SendLogOnWorker(String UserID,String Pass,String Type,String UserNum,String UserMail){
			this.Pass=Pass;
			this.UserID=UserID;
			this.Type=Type;
			this.UserNum=UserNum;
			this.UserMail=UserMail;
			
		}
		@Override
		protected Void doInBackground()  {
			
			SecretMsg LogOnMsg=new SecretMsg();
			LogOnMsg.setMsgType(Msg.Logon_MSG);
			try {
				byte[] enUserID = AESCoder.encrypt(UserID.getBytes(), SecretInfo.getKey());
				byte[] enPassWord = AESCoder.encrypt(Pass.getBytes(), SecretInfo.getKey());
				byte[] enUserType = AESCoder.encrypt(Type.getBytes(), SecretInfo.getKey());
				byte[] enUserNum = AESCoder.encrypt(UserNum.getBytes(), SecretInfo.getKey());
				byte[] enUserMail = AESCoder.encrypt(UserMail.getBytes(), SecretInfo.getKey());
				LogOnMsg.setEnUserID(enUserID);
				LogOnMsg.setEnPassWord(enPassWord);
				LogOnMsg.setEnUserType(enUserType);
				LogOnMsg.setEnUserNum(enUserNum);
				LogOnMsg.setEnUserMail(enUserMail);
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(LogOnMsg);//发送注册信息
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
		
				
				if(sMsg.getMsgType()==Msg.LogonRespond_MSG){
			
					//消息解密
					byte[] DeMsg=AESCoder.decrypt(sMsg.getEnMsg(), SecretInfo.getKey());
					String msgString=new String(DeMsg);
					//签名验证--用Server的证书公钥
					String sha1Hex2 = DigestUtils.sha1Hex(DeMsg);
					boolean flag=CertificateCoder.verify(sha1Hex2.getBytes(), sMsg.getSign(), ServerMsg.certificatePath);
					if(flag==false){
						JOptionPane.showMessageDialog(null, "与服务器通信被拦截修改！中断连接");
						ss.close();
						Thread.sleep(5000);
						System.exit(0);
					}else{
						if(msgString.equals(Msg.EXISTmsg))
							JOptionPane.showMessageDialog(UserAddDiaLog.this, "该ID或工号已存在！");
						else if(msgString.equals(Msg.OKmsg)){
							JOptionPane.showMessageDialog(UserAddDiaLog.this, "注册成功！");
						
							UserAddDiaLog.this.dispose();
						}else {
							JOptionPane.showMessageDialog(UserAddDiaLog.this, "注册失败！");
							}
						}
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			return null;
		}
		
	}
	
}

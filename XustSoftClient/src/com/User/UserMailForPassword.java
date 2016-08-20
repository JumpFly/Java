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

import org.apache.commons.codec.digest.DigestUtils;

import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Tools.*;
public class UserMailForPassword extends JDialog implements ActionListener{

	private JLabel jl[] =new JLabel[5];
	private JLabel Tip=null;
	private JTextField jtf1,jtf3,jtf4;
	private JPasswordField passwordField;
	private String newPass="",tempstt="";
	private JButton GetVerif,Choose,Cancel;//ȷ�ϡ�ȡ��
	private JComboBox DownList=null;
	private JScrollPane jsp;
	private JPanel jp1,jp2,jp3;
	private String strs[]={"�û��˺�","������","�û�����","�û�����","��֤��"};
	private String [] Type={"�ǻ�Ա","����Ա","��Ա"};
	private VerifMail vmMail=null;//������֤���ʼ�
	private String verify;//��֤��
	private SqlHelper sqlhelp=SqlHelper.getInstance();
	private ResultSet rs=null;
	
	private String UserID=null;
	private String UserMail=null;
	private String UserType=null;

	private class MySwingWorker extends SwingWorker<Void, Void>{
		private byte Count=60;
		
		public MySwingWorker(){
			
		}
		@Override
		protected Void doInBackground() throws Exception {
			 GetVerif.setEnabled(false);
			
			while(Count>=0)
			{
				 Thread.sleep(1000);
				 GetVerif.setText(Count+" s ���ط�");
				 Count--;
			}
			return null;
		}
		@Override
		protected void done() {
			
			super.done();
			 Count=60;
			 GetVerif.setText("��ȡ��֤��");
			 GetVerif.setEnabled(true);
		}
	
		
	}

	
	
	public static void main(String[] args) {
		UserMailForPassword ua=new UserMailForPassword(null,"123",true);
	}
	public UserMailForPassword(Frame owner,String title,boolean model ){
		super(owner,title,model); //���๹�췽����ģʽ�Ի���Ч��
		
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
		passwordField=new JPasswordField();

		jtf3=new JTextField();
		jtf4=new JTextField();
		DownList=new JComboBox(Type);
	
		
		jp2.add(jtf1);
		jp2.add(passwordField);

		jp2.add(jtf3);
		jp2.add(DownList);
		jp2.add(jtf4);
		DownList.addActionListener(this);
		GetVerif=new JButton("��ȡ��֤��");
		Choose=new JButton("ȷ��");
		Cancel=new JButton("ȡ��");
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

		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==GetVerif){
			//����֤��д��Ϣ�Ƿ���ȷ
			 UserID=jtf1.getText().trim();
			 UserMail=jtf3.getText().trim();
			 UserType=(String)DownList.getSelectedItem();
			
			 Choose.setEnabled(true);
			 CallServerEmailWorker callEmail=new CallServerEmailWorker(UserID, UserType, UserMail);
			 callEmail.execute();
//			try {
//				rs=sqlhelp.queryExec(UserID);
//				if(!rs.next()){
//					JOptionPane.showMessageDialog(this, "���˺Ų����ڣ�");
//				}else {
//						
//					String	UType=rs.getString(3);
//					String	UMail=rs.getString(4);
//					if(UserMail.equals(UMail)&&UserType.equals(UType)){
//						 
//						 GetVerif.setEnabled(false);
//						 Choose.setEnabled(true);
//						 MySwingWorker swingWorker=new MySwingWorker();
//						 swingWorker.execute();
//						
//						 if(flag==false){
//							 JOptionPane.showMessageDialog(this, "������;ʧ��"); 
//						 }
//						 
//						 JOptionPane.showMessageDialog(this, "�ѷ��������䣡"); 
//						
//					}else {
//						 JOptionPane.showMessageDialog(this, "��Ϣ��д����"); 
//					}
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}finally {
//				sqlhelp.DBclose();
//			}
			
		}
		if(e.getSource()==Choose){
			if(jtf4.getText().equals("")){
				JOptionPane.showMessageDialog(this, "����д��֤�룡");
				return;
			}else {
				String NewPass=String.copyValueOf(passwordField.getPassword());
				CallChangePassWorker changePass=new CallChangePassWorker(jtf4.getText().trim(),UserID, NewPass);
						changePass.execute();
			}

		}
		
		if(e.getSource()==Cancel){
			this.dispose();
			
		 	}
	}
	private class CallChangePassWorker extends SwingWorker<Void, Void>{
		private String verify;
		private String UserID;
		private String NewPass;
		public CallChangePassWorker( String verify,String UserID,String NewPass){
			this.verify=verify;
			this.UserID=UserID;
			this.NewPass=NewPass;
		}
		@Override
		protected Void doInBackground() throws Exception {
			SecretMsg changePassMsg=new SecretMsg();
			changePassMsg.setMsgType(Msg.ChangePassword_MSG);	
			byte[] enVerify = AESCoder.encrypt(this.verify.getBytes(), SecretInfo.getKey());
			byte[] enUserID = AESCoder.encrypt(this.UserID.getBytes(), SecretInfo.getKey());
			byte[] enNewPass = AESCoder.encrypt(this.NewPass.getBytes(), SecretInfo.getKey());
			changePassMsg.setEnMsg(enVerify);
			changePassMsg.setEnUserID(enUserID);
			changePassMsg.setEnPassWord(enNewPass);
			if(MapHoldReceiveThread.IsEmpty())
				return null;	
			Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
			ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
			oos.writeObject(changePassMsg);//����Ҫ��Email��Ϣ
			
			ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
			SecretMsg sMsg=(SecretMsg)ois.readObject();
			if(sMsg.getMsgType()==Msg.ChangePassRespond_MSG){
				
				//��Ϣ����
				byte[] DeMsg=AESCoder.decrypt(sMsg.getEnMsg(), SecretInfo.getKey());
				String msgString=new String(DeMsg);
				//ǩ����֤--��Server��֤�鹫Կ
				String sha1Hex2 = DigestUtils.sha1Hex(DeMsg);
				boolean flag=CertificateCoder.verify(sha1Hex2.getBytes(), sMsg.getSign(), ServerMsg.certificatePath);
				if(flag==false){
					JOptionPane.showMessageDialog(null, "�������ͨ�ű������޸ģ��ж�����");
					ss.close();
					Thread.sleep(5000);
					System.exit(0);
				}else if(msgString.equals(Msg.OKmsg)){
					JOptionPane.showMessageDialog(UserMailForPassword.this, "�����޸ĳɹ�����");
					UserMailForPassword.this.dispose();
				}	
				else 
					 JOptionPane.showMessageDialog(UserMailForPassword.this, "�����޸�ʧ��!!"); 
			}
			return null;
		}
		@Override
		protected void done() {
			
			super.done();
		}
	
		
	}

	private class CallServerEmailWorker extends SwingWorker<Void, Void>{
		 
		private String UserID;
		private String UserMail;
		private String UserType;
		public CallServerEmailWorker(String UserID,String UserType,String UserMail){
			this.UserID=UserID;
			this.UserType=UserType;
			this.UserMail=UserMail;
		}
		@Override
		protected Void doInBackground() throws Exception {
			SecretMsg sendMailMsg=new SecretMsg();
			sendMailMsg.setMsgType(Msg.SendEmail_MSG);	
			byte[] enUserID = AESCoder.encrypt(UserID.getBytes(), SecretInfo.getKey());
			byte[] enUserType = AESCoder.encrypt(UserType.getBytes(), SecretInfo.getKey());
			byte[] enUserMail = AESCoder.encrypt(UserMail.getBytes(), SecretInfo.getKey());
			sendMailMsg.setEnUserID(enUserID);
			sendMailMsg.setEnUserType(enUserType);
			sendMailMsg.setEnUserMail(enUserMail);
			if(MapHoldReceiveThread.IsEmpty())
				return null;
			Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
			ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
			oos.writeObject(sendMailMsg);//����Ҫ��Email��Ϣ
			
			ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
			SecretMsg sMsg=(SecretMsg)ois.readObject();
			if(sMsg.getMsgType()==Msg.SendEmailRespond_MSG){
				
				//��Ϣ����
				byte[] DeMsg=AESCoder.decrypt(sMsg.getEnMsg(), SecretInfo.getKey());
				String msgString=new String(DeMsg);
				//ǩ����֤--��Server��֤�鹫Կ
				String sha1Hex2 = DigestUtils.sha1Hex(DeMsg);
				boolean flag=CertificateCoder.verify(sha1Hex2.getBytes(), sMsg.getSign(), ServerMsg.certificatePath);
				if(flag==false){
					JOptionPane.showMessageDialog(null, "�������ͨ�ű������޸ģ��ж�����");
					ss.close();
					Thread.sleep(5000);
					System.exit(0);
				}else if(msgString.equals(Msg.OKmsg)){
						JOptionPane.showMessageDialog(UserMailForPassword.this, "�ѷ��������䣡��");
						 MySwingWorker swingWorker=new MySwingWorker();
						 swingWorker.execute();
				}
				else 
					 JOptionPane.showMessageDialog(UserMailForPassword.this, "��Ϣ����ȷ/������;ʧ��"); 
			}
			return null;
		}
		@Override
		protected void done() {
			
			super.done();
		}
	
		
	}

	
}

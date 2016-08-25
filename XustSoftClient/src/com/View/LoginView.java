package com.View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.commons.codec.digest.DigestUtils;
import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.ClientReceiveThread;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Secret.RSACoder;
import com.Tools.SecretInfo;
import com.Tools.ServerMsg;
import com.Tools.SqlHelper;
import com.User.UserAddDiaLog;
import com.User.UserMailForPassword;
public class LoginView extends JFrame implements ActionListener{
	
	
	private UserAddDiaLog AddDialog=null;
	private UserMailForPassword FindPassword=null;
	private JLabel BG=null; //设置背景图
	private JLabel UserID,Pass,UserType;
	private JButton jb1,jb2,jb3;//登录注册 忘记密码
	private JTextField jtf_ID=null;
	private JPasswordField jpf_pass=null;
	private JPanel myJPanel,myJPanel2;
	private String [] Type={"非会员","管理员","会员"};
	private JComboBox DownList=null;	
	private Font myFont=null;
	private boolean IsSafe=false;
	
	private Dialog InfoDialog;
	private BuildSafeChannel mySwingWorker;
	private SendLoginWorker sendLoginWorker;
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
		
		mySwingWorker=new BuildSafeChannel();
		
	 //设置窗口
	  ImageIcon icon =new ImageIcon("images/Lock.png");
	  this.setIconImage(icon.getImage());//窗口图标
	  this.setTitle("用户登录");
	     this.setSize(410,600);
	     this.setLocation(800,200);
	     this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   this.setResizable(false);
	    //显示窗体
	     this.setVisible(true);
	}  
	  
	public static void main(String[] args) throws Exception{
		Map<String, Object> keyMap = RSACoder.initKey();
		SecretInfo.setPublicKey(RSACoder.getPublicKey(keyMap));//非对称RSA公钥
		SecretInfo.setPrivateKey(RSACoder.getPrivateKey(keyMap));//非对称RSA私钥
		SecretInfo.setKey(AESCoder.initKey());//生成对称AES密钥
		EventQueue.invokeLater(new Runnable()
        {
           public void run()
           {
        	   LoginView LV=new LoginView();
           }
        });
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb1){
			if(!IsSafe){
				jb1.setEnabled(false);
				jb2.setEnabled(false);
				jb3.setEnabled(false);
				InfoDialog=new Dialog();
				mySwingWorker.execute();
				return;
			}
			
			String Pass=new String(jpf_pass.getPassword());
			String UserID=jtf_ID.getText().trim();
			String Type=  (String)DownList.getSelectedItem();
			
			sendLoginWorker=new SendLoginWorker(Pass, UserID, Type);
			sendLoginWorker.execute();
	
		}	
		if(e.getSource()==jb2){
			if(!IsSafe){
				jb1.setEnabled(false);
				jb2.setEnabled(false);
				jb3.setEnabled(false);
				InfoDialog=new Dialog();
				mySwingWorker.execute();
				return;
			}
			AddDialog=new UserAddDiaLog(null, "注册用户", true);
		}
		if(e.getSource()==jb3){
			if(!IsSafe){
				jb1.setEnabled(false);
				jb2.setEnabled(false);
				jb3.setEnabled(false);
				InfoDialog=new Dialog();
				mySwingWorker.execute();
			
				return;
			}
			FindPassword=new UserMailForPassword(null, "找回密码", true);
		}
		
	}
	
	private class SendLoginWorker extends SwingWorker<Void, Void>{
		private String Pass,UserID,Type;
		public SendLoginWorker(String Pass,String UserID,String Type){
			this.Pass=Pass;
			this.UserID=UserID;
			this.Type=Type;
			
		}
		@Override
		protected Void doInBackground() throws Exception {
			
			SecretMsg loginMsg=new SecretMsg();
			loginMsg.setMsgType(Msg.Login_MSG);
			try {
				byte[] enUserID = AESCoder.encrypt(UserID.getBytes(), SecretInfo.getKey());
				byte[] enPassWord = AESCoder.encrypt(Pass.getBytes(), SecretInfo.getKey());
				byte[] enUserType = AESCoder.encrypt(Type.getBytes(), SecretInfo.getKey());
				loginMsg.setEnUserID(enUserID);
				loginMsg.setEnPassWord(enPassWord);
				loginMsg.setEnUserType(enUserType);
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(loginMsg);//发送登录信息
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.LoginRespond_MSG){
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
						if(msgString.equals(Msg.OKmsg)){
							JOptionPane.showMessageDialog(null, "登录成功！");
							ControlMenu CCM=new ControlMenu(UserID,Type);
							CCM.setVisible(true);
							LoginView.this.dispose();
							}else {
								JOptionPane.showMessageDialog(null, "ID/密码/类型 错误！");
							}
						}
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			return null;
		}
		
	}
	
	
	private class BuildSafeChannel extends SwingWorker<Void, Void>{
		private Socket clientSocket;
		private byte[] EnKey;//对称密钥
		private byte[] EnSign;//即为Client生成的公钥
		
		public BuildSafeChannel(){
		}
		
		private void EncryptKey() throws Exception{
			EnKey=CertificateCoder.encryptByPublicKey(SecretInfo.getKey(), ServerMsg.certificatePath);
			EnSign=CertificateCoder.encryptByPublicKey(SecretInfo.getPublicKey(), ServerMsg.certificatePath);
			
		}
		@Override
		protected Void doInBackground() throws Exception {
			EncryptKey();
			clientSocket=new Socket();
			clientSocket.connect(new InetSocketAddress(ServerMsg.ServerIP, ServerMsg.ServerTCP_Port), 10000);
			//10秒超时
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
			
			SecretMsg BuildSafeMsg=new SecretMsg();
			BuildSafeMsg.setMsgType(Msg.BuildSafe_MSG);
			BuildSafeMsg.setEnMsg(EnKey);
			BuildSafeMsg.setSign(EnSign);
			oos.writeObject(BuildSafeMsg);
			
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			SecretMsg sMsg=(SecretMsg)ois.readObject();
			if(sMsg.getMsgType()==Msg.SafeRespond_MSG){
				System.out.println("receive the OK");
				//消息解密
				byte[] DeMsg=AESCoder.decrypt(sMsg.getEnMsg(), SecretInfo.getKey());
				String msgString=new String(DeMsg);
				//签名验证--用Server的证书公钥
				String sha1Hex2 = DigestUtils.sha1Hex(DeMsg);
				boolean flag=CertificateCoder.verify(sha1Hex2.getBytes(), sMsg.getSign(), ServerMsg.certificatePath);
				if(flag==false){
					JOptionPane.showMessageDialog(null, "与服务器通信被拦截修改！中断连接");
					return null;
				}else{
					if(msgString.equals(Msg.OKmsg)){
						IsSafe=true;
						   JOptionPane.showMessageDialog(null, "通道完成！可继续操作");
						   ClientReceiveThread cThread=new ClientReceiveThread(clientSocket);
						   cThread.start();
						   MapHoldReceiveThread.addClientReceiveThread(cThread);
						   MapHoldReceiveThread.setOis(ois);
						   MapHoldReceiveThread.setOos(oos);
						   return null;
					}
				}
			}
			
			return null;
		}
		   protected void done()   {
			   if(IsSafe){
				   InfoDialog.Close();
				 	jb1.setEnabled(true);
					jb2.setEnabled(true);
					jb3.setEnabled(true); 
			   }
		    
		       
	   }   
			
	}
	
 }

class Dialog extends  JDialog {
	  private JProgressBar progressBar;
	  private JLabel Note;
	  public  Dialog(){
		  
		  progressBar=new JProgressBar();
		  progressBar.setIndeterminate(true);
		  this.add(progressBar);
		  this.setTitle("正在与服务器建立安全通道..");
		  this.setSize(350, 75);
		  this.setResizable(false);
		  this.setVisible(true);
		  this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		 
	  }
	  public void Close(){
		  this.dispose();
	  }
	
}

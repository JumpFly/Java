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
	private JLabel BG=null; //���ñ���ͼ
	private JLabel UserID,Pass,UserType;
	private JButton jb1,jb2,jb3;//��¼ע�� ��������
	private JTextField jtf_ID=null;
	private JPasswordField jpf_pass=null;
	private JPanel myJPanel,myJPanel2;
	private String [] Type={"�ǻ�Ա","����Ա","��Ա"};
	private JComboBox DownList=null;	
	private Font myFont=null;
	private boolean IsSafe=false;
	
	private Dialog InfoDialog;
	private BuildSafeChannel mySwingWorker;
	private SendLoginWorker sendLoginWorker;
	public LoginView(){

		myFont =new Font("����",Font.PLAIN,20);
		jtf_ID=new JTextField(20);
		jtf_ID.addActionListener(this);
		jpf_pass=new JPasswordField(20);
		jpf_pass.addActionListener(this);
		jb1=new JButton("��¼");
		jb2=new JButton("ע��");
		jb3=new JButton("��������");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		BG=new JLabel(new ImageIcon("images/BG.png"));
		
		UserID=new JLabel("��¼  ID��",JLabel.CENTER);
		UserID.setFont(myFont); //��������
		UserID.setForeground(Color.blue);//����������ɫ����
		
		Pass=new JLabel("��¼���룺",JLabel.CENTER);
	    Pass.setFont(myFont); //��������
		Pass.setForeground(Color.blue);//����������ɫ����
		
		UserType=new JLabel("�û����ͣ�",JLabel.CENTER);
		UserType.setFont(myFont); //��������
		UserType.setForeground(Color.blue);//����������ɫ����
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
		
	 //���ô���
	  ImageIcon icon =new ImageIcon("images/Lock.png");
	  this.setIconImage(icon.getImage());//����ͼ��
	  this.setTitle("�û���¼");
	     this.setSize(410,600);
	     this.setLocation(800,200);
	     this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   this.setResizable(false);
	    //��ʾ����
	     this.setVisible(true);
	}  
	  
	public static void main(String[] args) throws Exception{
		Map<String, Object> keyMap = RSACoder.initKey();
		SecretInfo.setPublicKey(RSACoder.getPublicKey(keyMap));//�ǶԳ�RSA��Կ
		SecretInfo.setPrivateKey(RSACoder.getPrivateKey(keyMap));//�ǶԳ�RSA˽Կ
		SecretInfo.setKey(AESCoder.initKey());//���ɶԳ�AES��Կ
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
			AddDialog=new UserAddDiaLog(null, "ע���û�", true);
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
			FindPassword=new UserMailForPassword(null, "�һ�����", true);
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
				oos.writeObject(loginMsg);//���͵�¼��Ϣ
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.LoginRespond_MSG){
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
					}else{
						if(msgString.equals(Msg.OKmsg)){
							JOptionPane.showMessageDialog(null, "��¼�ɹ���");
							ControlMenu CCM=new ControlMenu(UserID,Type);
							CCM.setVisible(true);
							LoginView.this.dispose();
							}else {
								JOptionPane.showMessageDialog(null, "ID/����/���� ����");
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
		private byte[] EnKey;//�Գ���Կ
		private byte[] EnSign;//��ΪClient���ɵĹ�Կ
		
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
			//10�볬ʱ
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
				//��Ϣ����
				byte[] DeMsg=AESCoder.decrypt(sMsg.getEnMsg(), SecretInfo.getKey());
				String msgString=new String(DeMsg);
				//ǩ����֤--��Server��֤�鹫Կ
				String sha1Hex2 = DigestUtils.sha1Hex(DeMsg);
				boolean flag=CertificateCoder.verify(sha1Hex2.getBytes(), sMsg.getSign(), ServerMsg.certificatePath);
				if(flag==false){
					JOptionPane.showMessageDialog(null, "�������ͨ�ű������޸ģ��ж�����");
					return null;
				}else{
					if(msgString.equals(Msg.OKmsg)){
						IsSafe=true;
						   JOptionPane.showMessageDialog(null, "ͨ����ɣ��ɼ�������");
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
		  this.setTitle("�����������������ȫͨ��..");
		  this.setSize(350, 75);
		  this.setResizable(false);
		  this.setVisible(true);
		  this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		 
	  }
	  public void Close(){
		  this.dispose();
	  }
	
}

package com.Stu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Tools.DBMsg;
import com.Tools.SecretInfo;
import com.Tools.ServerMsg;
import com.Tools.SqlHelper;

public class UserUPMsg extends JDialog implements ActionListener{
	private ResultSet rs=null;
	private String[] UserPosts={"学习部","秘书处","宣传部","技术部"};
	private JLabel[] myLabels;
	private String[] paras={"账号","学号","姓名","性别","专业","部门","邮箱","入会时间"};
	private JRadioButton male,female; //单选框
	private ButtonGroup RaidoGroup;
	private  Font font ;
	private JTextField[] myTextFields;
	private JPanel MsgPanel,SouthPanel;
	private JButton Choose,Cancel,ResBtn;//确认、取消、重置
	private JTextArea MottoArea;
	private JScrollPane myJsp;
	private static final int TEXT_ROWS = 5;
	private static final int TEXT_COLUMNS = 20;
	private GridBagConstraints constraints;
	private GridBagLayout Baglayout;
	private JComboBox DownList=null;	
	private String UserNum="";
	
	public void addTextField(int indexX ,int indexY,int gridwidth,JTextField jtf){

		constraints.gridx=indexX;
		constraints.gridy=indexY;
		constraints.gridwidth=gridwidth;
		Baglayout.setConstraints(jtf, constraints);
		MsgPanel.add(jtf);
	
	}
	public void addRadioButton(int indexX ,int indexY,JRadioButton radioButton){

		constraints.gridx=indexX;
		constraints.gridy=indexY;
		constraints.gridwidth=1;
		constraints.anchor=constraints.CENTER;
		Baglayout.setConstraints(radioButton, constraints);
		MsgPanel.add(radioButton);
	
	}
/*	public void FillMsg(){
		if(!UserNum.equals("")){
			SqlHelper sqlHelper=SqlHelper.getInstance();
			String sql="select * from DetailMsg where UserNum="+"'"+UserNum+"'";
			try {
				
				rs=sqlHelper.queryExecute(sql);
				while(rs.next()){
					String stt=rs.getString(1);
					myTextFields[0].setText(stt==null||stt.equals("")?"null":stt.trim());
					stt=rs.getString(2);
					myTextFields[1].setText(stt==null||stt.equals("")?"null":stt.trim());
					 stt=rs.getString(3);
					myTextFields[2].setText(stt==null||stt.equals("")?"null":stt.trim());
					stt=rs.getString(4);
					String sex=(stt==null||stt.equals(""))?"null":stt.trim();
					if(sex.equals("男"))
					male.setSelected(true);
					if(sex.equals("女"))
						female.setSelected(true);
					stt=rs.getString(5);
					myTextFields[3].setText(stt==null||stt.equals("")?"null":stt.trim());
					 stt=rs.getString(6);
					String post=(stt==null||stt.equals(""))?"null":stt.trim();
					if(!post.equals("NULL"))
					for(int i=0;i<UserPosts.length;i++){
						if(UserPosts[i].equals(post)){
							DownList.setSelectedIndex(i);
							break;
							}
					}
					stt=rs.getString(7);
					myTextFields[4].setText(stt==null||stt.equals("")?"null":stt.trim());
					stt=rs.getString(8);
					myTextFields[5].setText(stt==null||stt.equals("")?"null":stt.trim());
					stt=rs.getString(9);
					MottoArea.setText(stt==null||stt.equals("")?"null":stt.trim());
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlHelper.DBclose();
			}
		}else{
			return;
		}
	}
	*/
//	public String[] ReturnUserPosts(){
//		String UserPost="";
//		SqlHelper sqlhelp= SqlHelper.getInstance();
//		try {
//			String sql="select UserPost from XustPost";
//			rs=sqlhelp.queryExecute(sql);
//			while(rs.next()){
//				UserPost+=rs.getString(1)+" ";
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			sqlhelp.DBclose();
//		}
//		return UserPost.split(" ");
//	}
	public UserUPMsg(Frame owner,boolean model,String UserNum){
		this.UserNum=UserNum;
		font=new Font("Serif", Font.BOLD, 20);
		myLabels=new JLabel[paras.length];
		myTextFields=new JTextField[paras.length-2];
		
		MsgPanel=new JPanel();
		 Baglayout = new GridBagLayout();
		 constraints = new GridBagConstraints();
		MsgPanel.setLayout(Baglayout);
		{
		int indexX=0,indexY=0;
		for(int i=0;i<paras.length;i++){
			if(i<paras.length-2)
			myTextFields[i]=new JTextField(15);
			myLabels[i]=new JLabel(paras[i]);
			myLabels[i].setFont(font);
			if(indexX>3)
				indexX=0;
			if((i&1)==0&&i!=0)
				indexY++;
			
			constraints.gridx=indexX;
			constraints.gridy=indexY;
			Baglayout.setConstraints(myLabels[i], constraints);
			MsgPanel.add(myLabels[i]);
			indexX+=2;
		}
		}
		
		myTextFields[0].setEnabled(false);//ID
		myTextFields[0].setText("*******");
		myTextFields[1].setEnabled(false);//学号
		myTextFields[4].setEnabled(false);//邮箱
		
		for(int i=0,n=0;i<4;i++){
			addTextField(1, i,1, myTextFields[n++]);	
			if(i!=1&&i!=2)
			addTextField(3, i,2, myTextFields[n++]);	
		}
		male=new JRadioButton("男");
		female=new JRadioButton("女");
		RaidoGroup= new ButtonGroup();
		RaidoGroup.add(male);
		RaidoGroup.add(female);
		
		addRadioButton(3, 1, male);
		addRadioButton(4, 1, female);
		
		AskPostNameWorker askServerPostName=new AskPostNameWorker();
		askServerPostName.execute();
		
		DownList=new JComboBox(UserPosts);
	
		constraints.gridx=3;
		constraints.gridy=2;
		constraints.gridwidth=2;
		constraints.weightx=100;
		constraints.weighty=100;
		constraints.anchor=constraints.WEST;
		Baglayout.setConstraints(DownList, constraints);
		MsgPanel.add(DownList);
		
		MottoArea= new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
		MottoArea.setLineWrap(true);//换行策略
		Border mottoBorder=BorderFactory.createEtchedBorder();
		MottoArea.setBorder(BorderFactory.createTitledBorder(mottoBorder, "Motto"));
	
	 
		Border PanelBorder=BorderFactory.createEtchedBorder();
		MsgPanel.setBorder(BorderFactory.createTitledBorder(PanelBorder, "个人信息"));
		  
		SouthPanel=new JPanel();
		Choose=new JButton("确定");
		Cancel=new JButton("取消");
		ResBtn=new JButton("重置");
		Choose.addActionListener(this);
		Cancel.addActionListener(this);
		ResBtn.addActionListener(this);
		SouthPanel.add(Choose);
		SouthPanel.add(Cancel);
		SouthPanel.add(ResBtn);
		this.add(MsgPanel,BorderLayout.NORTH);
		this.add(MottoArea,BorderLayout.CENTER);
		this.add(SouthPanel,BorderLayout.SOUTH);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("个人信息");
		
		AskUserDetailWorker askUserDetail=new AskUserDetailWorker();
		askUserDetail.execute();
		MottoArea.setFont(font);
		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Cancel){
			this.dispose();
		 }
		if(e.getSource()==ResBtn){
			for(int i=2;i<myTextFields.length;i++){
				if(i!=4)
				myTextFields[i].setText("");
			}
			male.setSelected(false);
			female.setSelected(false);
			MottoArea.setText("");
		 }
		if(e.getSource()==Choose){
			String Sex=male.isSelected()?"男":"NULL";
			if(!Sex.equals("男"))
			Sex=female.isSelected()?"女":"NULL";
			String UserPost=  (String)DownList.getSelectedItem();
	
			UPUserWorker callServerUp=new UPUserWorker(this.UserNum,myTextFields[2].getText().trim(),Sex, myTextFields[3].getText().trim(), UserPost, myTextFields[5].getText().trim(),MottoArea.getText());
			callServerUp.execute();

			}
		
	}
	public static void main(String[] args) {
		 new UserUPMsg(null,true,"1408020215");
	}
	private class UPUserWorker extends SwingWorker<Void, Void>{
		
		private String UserNum,UserName,UserSex,UserMajor,UserMotto,UserPost,UserDate;
		public UPUserWorker(String UserNum,String UserName,String UserSex,String UserMajor,String UserPost,String UserDate,String UserMotto){
			this.UserNum=UserNum;
			this.UserName=UserName;
			this.UserSex=UserSex;
			this.UserMajor=UserMajor;
			this.UserPost=UserPost;
			this.UserMotto=UserMotto;
			this.UserDate=UserDate;
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.UPUserMsg);
			byte[] enUserNum = AESCoder.encrypt(this.UserNum.getBytes(), SecretInfo.getKey());
			byte[] enUserName = AESCoder.encrypt(this.UserName.getBytes(), SecretInfo.getKey());
			byte[] enUserSex = AESCoder.encrypt(this.UserSex.getBytes(), SecretInfo.getKey());
			byte[] enUserMajor = AESCoder.encrypt(this.UserMajor.getBytes(), SecretInfo.getKey());
			byte[] enUserPost = AESCoder.encrypt(this.UserPost.getBytes(), SecretInfo.getKey());
			byte[] enUserDate = AESCoder.encrypt(this.UserDate.getBytes(), SecretInfo.getKey());
			byte[] enUserMotto = AESCoder.encrypt(this.UserMotto.getBytes(), SecretInfo.getKey());
			
			tableMsg.setEnUserNum(enUserNum);
			tableMsg.setEnUserName(enUserName);
			tableMsg.setEnUserMajor(enUserMajor);
			tableMsg.setEnUserSex(enUserSex);
			tableMsg.setEnPost(enUserPost);
			tableMsg.setEnDate(enUserDate);
			tableMsg.setEnUserMotto(enUserMotto);
			
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondUPUserMsg){
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
						if(msgString.equals(Msg.OKmsg))
							JOptionPane.showMessageDialog(UserUPMsg.this, "修改成功！请刷新");
						else 
							JOptionPane.showMessageDialog(UserUPMsg.this, "修改失败！");

						}
					UserUPMsg.this.dispose();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	private class AskPostNameWorker extends SwingWorker<Void, Void>{
		
		public AskPostNameWorker(){
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.AskPostNameMsg);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.ReturnPostNameMsg){
					UserPosts=sMsg.getPostName();
					DownList.removeAllItems();
					String stt;
					for(int i=0;i<UserPosts.length;i++){
						 stt=new String(Base64.decodeBase64(UserPosts[i]));
						DownList.addItem(stt);
					}
						
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	private class AskUserDetailWorker extends SwingWorker<Void, Void>{
		
		public AskUserDetailWorker(){
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.AskUserDetailMsg);
			byte[] enUserNum = AESCoder.encrypt(UserNum.getBytes(), SecretInfo.getKey());
			tableMsg.setEnUserNum(enUserNum);
			
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.ReturnUserDetailMsg){
					Vector UserDetailMsg=sMsg.getEnrowData();
					DBMsg.TransInfo(UserDetailMsg);
					
						Vector detail=(Vector)UserDetailMsg.get(0);
						String stt=(String)detail.get(1);
						myTextFields[1].setText(stt==null||stt.equals("")?"null":stt.trim());
						stt=(String)detail.get(2);
						myTextFields[2].setText(stt==null||stt.equals("")?"null":stt.trim());
						stt=(String)detail.get(3);
						String sex=(stt==null||stt.equals(""))?"null":stt.trim();
						if(sex.equals("男"))
						male.setSelected(true);
						if(sex.equals("女"))
							female.setSelected(true);
						stt=(String)detail.get(4);
						myTextFields[3].setText(stt==null||stt.equals("")?"null":stt.trim());
						stt=(String)detail.get(5);
						String post=(stt==null||stt.equals("")?"null":stt.trim());
						if(!post.equals("null")){
							String postBase64=Base64.encodeBase64String(post.getBytes());
						for(int i=0;i<UserPosts.length;i++){
							if(UserPosts[i].equals(post)){
								DownList.setSelectedIndex(i);
								break;
								}
						}}
						stt=(String)detail.get(6);
						myTextFields[4].setText(stt==null||stt.equals("")?"null":stt.trim());
						stt=(String)detail.get(7);
						myTextFields[5].setText(stt==null||stt.equals("")?"null":stt.trim());
						stt=(String)detail.get(8);
						MottoArea.setText(stt==null||stt.equals("")?"null":stt.trim());
						
						return null;
				
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			return null;
		}
	}
}

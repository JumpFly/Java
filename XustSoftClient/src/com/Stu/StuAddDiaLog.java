package com.Stu;
import com.User.*;
import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.commons.codec.digest.DigestUtils;

import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Tools.*;
public class StuAddDiaLog extends JDialog implements ActionListener{
	private ResultSet rs=null;
	private String [] UserPosts=null;	
	private JButton Choose,Cancel,ResBtn;//确认、取消、重置
	private JPanel jp1,jp2,jp3;
	private String DetailMsgTable[]= DBMsg.DetailMsgTable;
	private JLabel jl[] =new JLabel[DetailMsgTable.length+1];
	private JTextField jtf[]=new JTextField[DetailMsgTable.length+1];
	private Font myFont=null;
	private Date NowDate;
	private String FormDate;
	private SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	
	
	
	public StuAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		myFont =new Font("黑体",Font.PLAIN,18);
		
		NowDate=new Date();
		FormDate=df.format(NowDate);
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(jl.length, 1));
		jp2.setLayout(new GridLayout(jl.length, 1));
		
		jl[0]=new JLabel("账号");
		jl[0].setFont(myFont); //设置字体
		jp1.add(jl[0]);
		jtf[0]=new JTextField();
		jp2.add(jtf[0]);
		
		for(int i=1;i<jl.length;i++){
			jl[i]=new JLabel(DetailMsgTable[i-1]);
			jl[i].setFont(myFont); //设置字体
			jp1.add(jl[i]);
			jtf[i]=new JTextField();
			jp2.add(jtf[i]);
		}
		
	
		Choose=new JButton("确定");
		Cancel=new JButton("取消");
		ResBtn=new JButton("重置");
		Choose.addActionListener(this);
		Cancel.addActionListener(this);
		ResBtn.addActionListener(this);
		
		jp3.add(ResBtn);
		jp3.add(Choose);
		jp3.add(Cancel);
		
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		this.setLocation(800, 300);
		this.setSize(300,400);
		this.setResizable(false);
		this.setVisible(true);
		 this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	public static void main(String[] args) {
		StuAddDiaLog Sa=new StuAddDiaLog(null,"123",false);

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Cancel){
			this.dispose();
		 }
		if(e.getSource()==ResBtn){
			Resset();
		 }
		if(e.getSource()==Choose){
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"是否确定？", "请选择..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						if(TextFiledIsEmpty())
						{
							JOptionPane.showMessageDialog(this, "账号/学号/邮箱不能为空！");
							return;
						}
						CallAddWorker callAdd=new CallAddWorker(jtf[0].getText().trim(),jtf[1].getText().trim(),jtf[2].getText().trim(),jtf[3].getText().trim(),jtf[4].getText().trim(),jtf[5].getText().trim());
						callAdd.execute();
				
					}
			}
		
	}
	private void Resset(){
		for(int i=0;i<jtf.length;i++){
			jtf[i].setText("");
		}
	}
	private boolean TextFiledIsEmpty(){
		boolean flag=false;
		String jtf0=jtf[0].getText().trim();
		String jtf1=jtf[1].getText().trim();
		String jtf5=jtf[5].getText().trim();
		if(jtf0.toLowerCase().equals("null")||jtf1.toLowerCase().equals("null")||jtf5.toLowerCase().equals("null"))
			flag= true;
		if(jtf0.equals("")||jtf1.equals("")||jtf5.equals(""))
			flag= true;
		return flag;
	}

	private class CallAddWorker extends SwingWorker<Void, Vector<String>>{
		
		private String UserID,UserNum,UserName,UserSex,UserMajor,UserMail;
		public CallAddWorker(String UserID,String UserNum,String UserName,String UserSex,String UserMajor,String UserMail){
			this.UserID=UserID;
			this.UserNum=UserNum;
			this.UserName=UserName;
			this.UserSex=UserSex;
			this.UserMajor=UserMajor;
			this.UserMail=UserMail;
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.AddMsg);
			tableMsg.setMenu(Msg.StuMenu);
			byte[] enUserID = AESCoder.encrypt(UserID.getBytes(), SecretInfo.getKey());
			byte[] enUserNum = AESCoder.encrypt(UserNum.getBytes(), SecretInfo.getKey());
			byte[] enUserMail = AESCoder.encrypt(UserMail.getBytes(), SecretInfo.getKey());
			byte[] enUserName = AESCoder.encrypt(UserName.getBytes(), SecretInfo.getKey());
			byte[] enUserSex = AESCoder.encrypt(UserSex.getBytes(), SecretInfo.getKey());
			byte[] enUserMajor = AESCoder.encrypt(UserMajor.getBytes(), SecretInfo.getKey());
			tableMsg.setEnUserID(enUserID);
			tableMsg.setEnUserNum(enUserNum);
			tableMsg.setEnUserName(enUserName);
			tableMsg.setEnUserMail(enUserMail);
			tableMsg.setEnUserMajor(enUserMajor);
			tableMsg.setEnUserSex(enUserSex);
			try {
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondStuAddMsg){
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
							JOptionPane.showMessageDialog(StuAddDiaLog.this, "该ID或工号已存在！");
						else if(msgString.equals(Msg.OKmsg)){
							JOptionPane.showMessageDialog(StuAddDiaLog.this, "添加成功！");
							Resset();
							StuAddDiaLog.this.dispose();
						}else {
							JOptionPane.showMessageDialog(StuAddDiaLog.this, "添加失败！");
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

package com.Absence;
import com.User.*;
import java.awt.BorderLayout;
import java.awt.Font;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apache.commons.codec.digest.DigestUtils;

import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Post.PostAddDiaLog;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Tools.*;
public class AbsenceDiaLog extends JDialog implements ActionListener{

	private JLabel jl[] =new JLabel[3];
	private JTextField jtf1,jtf2,jtf3;
	private JButton Choose,Cancel,ResBtn;//ȷ�ϡ�ȡ��
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
		super(owner,title,model); //���๹�췽����ģʽ�Ի���Ч��
		myFont =new Font("����",Font.PLAIN,18);
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(3, 1));
		jp2.setLayout(new GridLayout(3, 1));
		
		
		for(int i=0;i<AbsenceTable.length;i++){
			
			jl[i]=new JLabel(AbsenceTable[i]);
			jl[i].setFont(myFont); //��������
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
	
		
		
		Choose=new JButton("ȷ��");
		Cancel=new JButton("ȡ��");
		ResBtn=new JButton("����");
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
					JOptionPane.showMessageDialog(this, "����д������");
					return;
				}
				CallAddWorker callAdd=new CallAddWorker(jtf1.getText().trim(), jtf2.getText(), jtf3.getText());
				callAdd.execute();

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
	private class CallAddWorker extends SwingWorker<Void, Vector<String>>{
		
		private String UserNum,Date,Remark;
		public CallAddWorker(String UserNum,String Date,String Remark){
			this.UserNum=UserNum;
			this.Date=Date;
			this.Remark=Remark;
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.AddMsg);
			tableMsg.setMenu(Msg.AbsenceMenu);
			byte[] enUserNum = AESCoder.encrypt(UserNum.getBytes(), SecretInfo.getKey());
			byte[] enDate = AESCoder.encrypt(Date.getBytes(), SecretInfo.getKey());
			byte[] enRemark = AESCoder.encrypt(Remark.getBytes(), SecretInfo.getKey());
			tableMsg.setEnUserNum(enUserNum);
			tableMsg.setEnDate(enDate);
			tableMsg.setEnRemark(enRemark);
			
			try {
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondAbsenceAddMsg){
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
						if(msgString.equals(Msg.EXISTmsg))
							JOptionPane.showMessageDialog(null, "������¼�Ѵ��ڣ�");
						else if(msgString.equals(Msg.OKmsg)){
							JOptionPane.showMessageDialog(null, "��ӳɹ���");
							AbsenceDiaLog.this.dispose();
						}else {
							JOptionPane.showMessageDialog(null, "���ʧ�ܣ�");
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

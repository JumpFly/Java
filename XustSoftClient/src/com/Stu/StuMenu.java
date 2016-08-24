package com.Stu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import com.Absence.AbsenceMenu;
import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Tools.DBMsg;
import com.Tools.FileControl;
import com.Tools.SecretInfo;
import com.Tools.ServerMsg;
import com.Tools.SqlHelper;
public class StuMenu extends JFrame implements ActionListener{
	private ResultSet rs=null;
	private FileControl FileCon=null;
	private JPanel jp;
	private JButton CheckIn,EnterIn,update;//����¼�롢����¼��	
	private JButton MsgFileGetIn,MsgFileGetOut;
	private JTable jtb=null;
	private JScrollPane jsp=null;
		
	private JPanel jp1,jp2;
	private JLabel jl;
	private JButton jb1,jb2,jb3,jb4;
	private JTextField jtf;
	private DefaultTableModel model;
	private DBMsg 	dbMsg=new DBMsg();
		
		String DBTable="DetailTable";
		String []TableParas=dbMsg.DetailMsgTable;
		String [] UserPosts=null;
		StuAddDiaLog stuAddDiaLog=null;
		UserUPMsg  uum;
		String UserType;

	public static void main(String[] args) {
		StuMenu test2=new StuMenu("����Ա");

	}

	
	public StuMenu(String UserType){
		
		this.UserType=UserType;
		CheckIn=new JButton("����¼��");
		CheckIn.addActionListener(this);
		EnterIn=new JButton("����¼��");
		EnterIn.addActionListener(this);
		
		jp1=new JPanel();
		jtf=new JTextField(10);
		jb1=new JButton("��ѯ");
		jb1.addActionListener(this);
		update=new JButton("ˢ��");
		update.addActionListener(this);
		MsgFileGetIn=new JButton("����");
		MsgFileGetIn.addActionListener(this);
		MsgFileGetOut=new JButton("����");
		MsgFileGetOut.addActionListener(this);
		
		jl=new JLabel("������ѧ��");
		jp1.add(MsgFileGetIn);
		jp1.add(jl);
		jp1.add(jtf);
		jp1.add(jb1);
		jp1.add(update);
		jp1.add(MsgFileGetOut);
		jp2=new JPanel();
		
		jb3=new JButton("�޸�");
		jb3.addActionListener(this);
		jb4=new JButton("ɾ��");
		jb4.addActionListener(this);
		
		jp2.add(CheckIn);
		jp2.add(EnterIn);
		jp2.add(jb3);
		jp2.add(jb4);
		

		   model=new DefaultTableModel();
		jtb=new JTable();//��ģ�ͼ��뵽JTable
		jtb.setModel(model);
		
		jtb.setRowHeight(40);
		jsp=new JScrollPane(jtb);
		
		
		if(!UserType.equals("����Ա")){
			CheckIn.setEnabled(false);
			EnterIn.setEnabled(false);
			jb3.setEnabled(false);
			jb4.setEnabled(false);
			MsgFileGetIn.setEnabled(false);
			MsgFileGetOut.setEnabled(false);
		}
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setTitle("��Ա����");
		pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		CallTableWorker callserver=new CallTableWorker();
		callserver.execute();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==MsgFileGetIn){
			//����
//			FileCon=new FileControl();
//			FileCon.ReadInFile(this.DBTable);
		}else if(e.getSource()==MsgFileGetOut){
			//����
			JFileChooser Fch2=new JFileChooser();
			Fch2.setDialogTitle("���Ϊ..");
			//Ĭ����ʾ
			final int value=Fch2.showSaveDialog(null);
			Fch2.setVisible(true);
			if(value==Fch2.CANCEL_OPTION)
				return;
			String filepath=Fch2.getSelectedFile().getAbsolutePath();
			FileGetOutWorker getOutWorker=new FileGetOutWorker(filepath);
			getOutWorker.execute();

		}
		
		
		if(e.getSource()==CheckIn){
			AbsenceMenu test=new AbsenceMenu(UserType);
			
		}else if(e.getSource()==EnterIn){
		 stuAddDiaLog=new StuAddDiaLog(this, "����¼��", true);
		 CallTableWorker updata=new CallTableWorker();
			updata.execute();
		}
		if(e.getSource()==update){
			CallTableWorker updata=new CallTableWorker();
			updata.execute();
	
		}
		
		if(e.getSource()==jb1){//��ѯ
			String UserNum =jtf.getText();
			if(UserNum.equals("")){
				JOptionPane.showMessageDialog(this, "�����빤�ţ�");
				return;
			}
			FindTheOneWorker callserver=new FindTheOneWorker(UserNum);
			callserver.execute();
	
		}
		
		if(e.getSource()==jb3){
			int rowNum=this.jtb.getSelectedRow();
			
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			String UserNum=(String)model.getValueAt(rowNum, 0);
	
			uum=new UserUPMsg(this, true, UserNum);

		}
		
		if(e.getSource()==jb4){
			//ɾ��
			//getSelectedRow();����ѡ�е��У���û��ѡ���򷵻�-1
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			//ת����String �����model
			String UserNum=(String)model.getValueAt(rowNum, 0);
			//���ѡ�е����е� ��һ���ֶΣ��У�
			/*
			 * ��������ȷ���Ƿ�Ҫɾ��
			 * */
			int	res=JOptionPane.showConfirmDialog(null, 
					"�Ƿ����ɾ����", "��ѡ��..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						AskDeleteWorker askServerDelete=new AskDeleteWorker(UserNum);
						askServerDelete.execute();
						
					   }
		}
		
	}
	private class FileGetOutWorker extends SwingWorker<Void, Void>{
		
		private Vector rowData;
		private String filepath;
		private FileWriter fw=null;
		private BufferedWriter bw=null;
		public FileGetOutWorker(String filepath){
			this.filepath=filepath;
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.FileGetOutMsg);
			tableMsg.setMenu(Msg.StuMenu);
			try {
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				if(sMsg.getMsgType()==Msg.RespondStuFileOutMsg){
						this.rowData=sMsg.getEnrowData();
						DBMsg.TransInfo(this.rowData);
						try{
						fw=new FileWriter(filepath);
						bw=new BufferedWriter(fw);
						for(Object V:this.rowData){
							Vector Details=(Vector)V;
							String RowMsg="";
							for (int i = 0; i < Details.size(); i++) {
								RowMsg+=Details.get(i)+" ";
							}
							RowMsg+="\r\n";
							bw.write(RowMsg);
						}
						JOptionPane.showMessageDialog(null, "������ɣ�");
						} catch (Exception e2) {
								e2.printStackTrace();
							}  finally{
								try {
									bw.close();
									fw.close();
								} catch (Exception e3) {
									e3.printStackTrace();
								}
							}	
					
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	private class FindTheOneWorker extends SwingWorker<Void, Void>{
		
		private Vector columnNames;
		private Vector rowData;
		private String UserNumBase64;
		public FindTheOneWorker(String UserNum){
			this.UserNumBase64=Base64.encodeBase64String(UserNum.getBytes());
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.FindTheOneMsg_MSG);
			tableMsg.setMenu(Msg.StuMenu);
			tableMsg.setFindOne(this.UserNumBase64);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				if(sMsg.getMsgType()==Msg.RespondStu_MSG){
					if(sMsg.getOKorNo().equals(Msg.OKmsg)){
						this.columnNames=sMsg.getColumnNames();
						this.rowData=sMsg.getEnrowData();
						DBMsg.TransInfo(this.rowData);
						model.setDataVector(this.rowData , this.columnNames);
					}else{
						 JOptionPane.showMessageDialog(null, "�޸�ѧ�ż�¼��");
					}
					
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	
	private class CallTableWorker extends SwingWorker<Void, Void>{
		
		private Vector columnNames;
		private Vector rowData;
		public CallTableWorker(){
			
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.StuMenuCallTable_MSG);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondStu_MSG){
					this.columnNames=sMsg.getColumnNames();
					this.rowData=sMsg.getEnrowData();
					DBMsg.TransInfo(this.rowData);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void done() {
			super.done();
			model.setDataVector(this.rowData , this.columnNames);
		}

	}
	private class AskDeleteWorker extends SwingWorker<Void, Void>{
		private String UserNum;
		public AskDeleteWorker(String UserNum){
			this.UserNum=UserNum;
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.DeleteMsg);
			tableMsg.setMenu(Msg.StuMenu);
			byte[] enUserNum = AESCoder.encrypt(this.UserNum.getBytes(), SecretInfo.getKey());
			tableMsg.setEnUserNum(enUserNum);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondStuDeleteMsg){
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
							JOptionPane.showMessageDialog(StuMenu.this, "ɾ���ɹ�����ˢ��");
							CallTableWorker updata=new CallTableWorker();
							updata.execute();}
						else 
							JOptionPane.showMessageDialog(StuMenu.this, "ɾ��ʧ�ܣ�");
						}
					
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	
}

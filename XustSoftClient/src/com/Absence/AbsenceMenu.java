package com.Absence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
import org.apache.commons.codec.digest.DigestUtils;

import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Stu.StuAddDiaLog;
import com.Stu.StuMenu;
import com.Tools.DBMsg;
import com.Tools.FileControl;
import com.Tools.SecretInfo;
import com.Tools.ServerMsg;
import com.Tools.SqlHelper;

public class AbsenceMenu extends JFrame implements ActionListener{

	private FileControl FileCon=null;
	private JPanel jp;
	private JButton Add,Delete,update;//������ɾ����ˢ��
	private JButton MsgFileGetIn,MsgFileGetOut;
	private JTable jtb=null;
	private JScrollPane jsp=null;
	
	private JPanel jp1,jp2;
	private JLabel jl;
	private JButton jb1,jb2;
	private JTextField jtf;
	private DefaultTableModel model;
	private DBMsg 	dbMsg=new DBMsg();
	private String UserType;
	private String DBTable="AbsenceTable";
	private String []TableParas=dbMsg.AbsenceTable;
	public AbsenceMenu(String Type){
		this.UserType=Type;
		
		Add=new JButton("����");
		Add.addActionListener(this);
		Delete=new JButton("ɾ��");
		Delete.addActionListener(this);
		
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
		jp2.add(Add);
		jp2.add(Delete);

		model=new DefaultTableModel();
		jtb=new JTable();//��ģ�ͼ��뵽JTable
		jtb.setModel(model);
		jtb.setRowHeight(40);
		
		jsp=new JScrollPane(jtb);
		
		if(!UserType.equals("����Ա")){
			Add.setEnabled(false);
			Delete.setEnabled(false);
			MsgFileGetIn.setEnabled(false);
			MsgFileGetOut.setEnabled(false);
		}
		
		CallTableWorker updata=new CallTableWorker();
		updata.execute();
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setTitle("ȱ�ڹ���");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new AbsenceMenu("����Ա");
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
		
		if(e.getSource()==Add){
			AbsenceDiaLog test=new AbsenceDiaLog(this, "ȱ�ڼ�¼", false);
		}else if(e.getSource()==Delete){
			
			//ɾ��
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			//ת����String �����model
			String Num=(String)model.getValueAt(rowNum, 0);
			String date=(String)model.getValueAt(rowNum, 1);
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"�Ƿ����ɾ����", "��ѡ��..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						AskDeleteWorker askServerDelete=new AskDeleteWorker(Num, date);
						askServerDelete.execute();

					   }
			
			
		}else if(e.getSource()==update){
			
			CallTableWorker updata=new CallTableWorker();
			updata.execute();		
		}
		if(e.getSource()==jb1){
			String UserNum =jtf.getText().trim();
			if(UserNum.equals("")){
				JOptionPane.showMessageDialog(this, "�����빤�ţ�");
				return;
			}
			FindTheOneWorker callserver=new FindTheOneWorker(UserNum);
			callserver.execute();
		
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
			tableMsg.setMenu(Msg.AbsenceMenu);
			try {
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				if(sMsg.getMsgType()==Msg.RespondAbsenceFileOutMsg){
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
			tableMsg.setMenu(Msg.AbsenceMenu);
			tableMsg.setFindOne(this.UserNumBase64);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondAbsence_MSG){
					if(sMsg.getOKorNo().equals(Msg.OKmsg)){
						this.columnNames=sMsg.getColumnNames();
						this.rowData=sMsg.getEnrowData();
						DBMsg.TransInfo(this.rowData);
						model.setDataVector(this.rowData , this.columnNames);
					}else{
						 JOptionPane.showMessageDialog(null, "�޸ü�¼��");
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
			tableMsg.setMsgType(Msg.AbsenceMenuCallTable_MSG);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondAbsence_MSG){
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
		private String UserNum,UserDate;
		public AskDeleteWorker(String UserNum,String UserDate){
			this.UserNum=UserNum;
			this.UserDate=UserDate;
		}
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.DeleteMsg);
			tableMsg.setMenu(Msg.AbsenceMenu);
			byte[] enUserNum = AESCoder.encrypt(this.UserNum.getBytes(), SecretInfo.getKey());
			byte[] enUserDate = AESCoder.encrypt(this.UserDate.getBytes(), SecretInfo.getKey());
			tableMsg.setEnUserNum(enUserNum);
			tableMsg.setEnDate(enUserDate);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondAbsenceDeleteMsg){
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
						if(msgString.equals(Msg.OKmsg))
							JOptionPane.showMessageDialog(AbsenceMenu.this, "ɾ���ɹ�����ˢ��");
						else 
							JOptionPane.showMessageDialog(AbsenceMenu.this, "ɾ��ʧ�ܣ�");

						}
				
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
}

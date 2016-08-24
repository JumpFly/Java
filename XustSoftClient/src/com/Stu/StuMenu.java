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
	private JButton CheckIn,EnterIn,update;//考勤录入、档案录入	
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
		StuMenu test2=new StuMenu("管理员");

	}

	
	public StuMenu(String UserType){
		
		this.UserType=UserType;
		CheckIn=new JButton("考勤录入");
		CheckIn.addActionListener(this);
		EnterIn=new JButton("档案录入");
		EnterIn.addActionListener(this);
		
		jp1=new JPanel();
		jtf=new JTextField(10);
		jb1=new JButton("查询");
		jb1.addActionListener(this);
		update=new JButton("刷新");
		update.addActionListener(this);
		MsgFileGetIn=new JButton("导入");
		MsgFileGetIn.addActionListener(this);
		MsgFileGetOut=new JButton("导出");
		MsgFileGetOut.addActionListener(this);
		
		jl=new JLabel("请输入学号");
		jp1.add(MsgFileGetIn);
		jp1.add(jl);
		jp1.add(jtf);
		jp1.add(jb1);
		jp1.add(update);
		jp1.add(MsgFileGetOut);
		jp2=new JPanel();
		
		jb3=new JButton("修改");
		jb3.addActionListener(this);
		jb4=new JButton("删除");
		jb4.addActionListener(this);
		
		jp2.add(CheckIn);
		jp2.add(EnterIn);
		jp2.add(jb3);
		jp2.add(jb4);
		

		   model=new DefaultTableModel();
		jtb=new JTable();//把模型加入到JTable
		jtb.setModel(model);
		
		jtb.setRowHeight(40);
		jsp=new JScrollPane(jtb);
		
		
		if(!UserType.equals("管理员")){
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
		this.setTitle("成员管理");
		pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		CallTableWorker callserver=new CallTableWorker();
		callserver.execute();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==MsgFileGetIn){
			//导入
//			FileCon=new FileControl();
//			FileCon.ReadInFile(this.DBTable);
		}else if(e.getSource()==MsgFileGetOut){
			//导出
			JFileChooser Fch2=new JFileChooser();
			Fch2.setDialogTitle("另存为..");
			//默认显示
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
		 stuAddDiaLog=new StuAddDiaLog(this, "档案录入", true);
		 CallTableWorker updata=new CallTableWorker();
			updata.execute();
		}
		if(e.getSource()==update){
			CallTableWorker updata=new CallTableWorker();
			updata.execute();
	
		}
		
		if(e.getSource()==jb1){//查询
			String UserNum =jtf.getText();
			if(UserNum.equals("")){
				JOptionPane.showMessageDialog(this, "请输入工号！");
				return;
			}
			FindTheOneWorker callserver=new FindTheOneWorker(UserNum);
			callserver.execute();
	
		}
		
		if(e.getSource()==jb3){
			int rowNum=this.jtb.getSelectedRow();
			
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			String UserNum=(String)model.getValueAt(rowNum, 0);
	
			uum=new UserUPMsg(this, true, UserNum);

		}
		
		if(e.getSource()==jb4){
			//删除
			//getSelectedRow();返回选中的行，若没有选中则返回-1
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			//转换成String 传入进model
			String UserNum=(String)model.getValueAt(rowNum, 0);
			//获得选中的这行的 第一个字段（列）
			/*
			 * 弹出窗口确认是否要删除
			 * */
			int	res=JOptionPane.showConfirmDialog(null, 
					"是否打算删除？", "请选择..", JOptionPane.YES_NO_OPTION);
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
						JOptionPane.showMessageDialog(null, "导出完成！");
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
						 JOptionPane.showMessageDialog(null, "无该学号记录！");
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
							JOptionPane.showMessageDialog(StuMenu.this, "删除成功！请刷新");
							CallTableWorker updata=new CallTableWorker();
							updata.execute();}
						else 
							JOptionPane.showMessageDialog(StuMenu.this, "删除失败！");
						}
					
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	
}

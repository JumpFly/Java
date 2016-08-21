package com.Stu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
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

import com.Absence.AbsenceMenu;
import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Tools.DBMsg;
import com.Tools.FileControl;
import com.Tools.SecretInfo;
import com.Tools.SqlHelper;
import com.User.UserMsgModel;
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
		
	private UserMsgModel UMM;
	private DefaultTableModel model;
	private DBMsg 	dbMsg=new DBMsg();
		
		String DBTable="DetailTable";
		String []TableParas=dbMsg.DetailMsgTable;
		String [] UserPosts=null;
		StuAddDiaLog stuAddDiaLog=null;
		UserUPMsg  uum;
		String UserType;
		
//		
//		public String[] ReturnUserPosts(){
//			String UserPost="";
//			SqlHelper sqlhelp= SqlHelper.getInstance();
//			try {
//				String sql="select UserPost from XustPost";
//				rs=sqlhelp.queryExecute(sql);
//				while(rs.next()){
//					UserPost+=rs.getString(1)+" ";
//				}
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				sqlhelp.DBclose();
//			}
//			return UserPost.split(" ");
//		}
//		
//		public  void UpdataClassMSG(){
//			String sql="",sql2="";
//			SqlHelper sqlhelp=SqlHelper.getInstance();
//			UserPosts=ReturnUserPosts();
//		
//			for (int i = 0; i < UserPosts.length; i++) {
//				int NUM=0;
//				sql="select *from DetailMsg where UserPost="+"'"+UserPosts[i]+"'";
//				try {
//					rs=sqlhelp.queryExecute(sql);
//					while(rs.next()){
//						NUM++;
//					}
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}finally {
//					sqlhelp.DBclose();
//				}
//				sql2="update XustPost set PersonNum="+"'"+NUM+"'"+" where UserPost="+"'"+UserPosts[i]+"'";
//					try {
//					sqlhelp.queryExe(sql2);
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}finally {
//					sqlhelp.DBclose();
//				}
//			}
//		}	
//		
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
			FileCon=new FileControl();
			FileCon.ReadInFile(this.DBTable);
		}else if(e.getSource()==MsgFileGetOut){
			//����
			FileCon=new FileControl();
			FileCon.SaveToFile(this.DBTable);
		}
		
		
		if(e.getSource()==CheckIn){
			AbsenceMenu test=new AbsenceMenu(UserType);
			
		}else if(e.getSource()==EnterIn){
		 stuAddDiaLog=new StuAddDiaLog(this, "����¼��", true);
			
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
			String UserID=(String)UMM.getValueAt(rowNum, 0);
	
			uum=new UserUPMsg(this, true, UserID);
				UMM=new UserMsgModel();
				String sql2 ="select * from DetailMsg";
				UMM.queryUser(sql2,this.TableParas,DBTable);
				jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
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
			String UserNum=(String)UMM.getValueAt(rowNum, 0);
			//���ѡ�е����е� ��һ���ֶΣ��У�
			/*
			 * ��������ȷ���Ƿ�Ҫɾ��
			 * */
			int	res=JOptionPane.showConfirmDialog(null, 
					"�Ƿ����ɾ����", "��ѡ��..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String sql ="delete from DetailMsg where UserNum=?";
						String[] paras={UserNum};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "ɾ���ɹ�����ˢ��");
						}else {
							JOptionPane.showMessageDialog(this, "ɾ��ʧ�ܣ�");
								
						}
						UMM=new UserMsgModel();
						 String sql2 ="select * from DetailMsg";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
						
					   }
			
			
		}
		
	}

	private class FindTheOneWorker extends SwingWorker<Void, Vector<String>>{
		
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
	
	private class CallTableWorker extends SwingWorker<Void, Vector<String>>{
		
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
	
	
}

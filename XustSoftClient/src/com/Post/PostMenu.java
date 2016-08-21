package com.Post;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
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

import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Stu.StuAddDiaLog;
import com.Tools.DBMsg;
import com.Tools.FileControl;
import com.Tools.SqlHelper;
import com.User.UserMsgModel;

public class PostMenu extends JFrame implements ActionListener{
	private FileControl FileCon=null;
	private ResultSet rs=null;
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
	private UserMsgModel UMM;
	private DBMsg 	dbMsg=new DBMsg();
	
	private String DBTable="XustPostTable";
	private String []TableParas=dbMsg.XustPostTable;
	private PostAddDiaLog CAd=null;
	private String	UserType;
	
	public PostMenu(String UserType){
		this.UserType=UserType;
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
		jl=new JLabel("�����벿��");
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
		
		CallTableWorker callserver=new CallTableWorker();
		callserver.execute();
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new PostMenu("����Ա");

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
		
		if(e.getSource()==Add){
			CAd=new PostAddDiaLog(this, "�½�����", false);
			
			
		}else if(e.getSource()==Delete){
			//ɾ��
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			    return;
			}
			//ת����String �����model
			String UserPost=(String)UMM.getValueAt(rowNum, 0);
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"�Ƿ����ɾ����", "��ѡ��..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String sql ="delete from XustPost where UserPost=?";
						String[] paras={UserPost};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "ɾ���ɹ���");
						}else {
							JOptionPane.showMessageDialog(this, "ɾ��ʧ�ܣ�");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from XustPost";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//��ȡ�µ�����ģ�� 
						
					   }
			
			
		}else if(e.getSource()==update){

			CallTableWorker callserver=new CallTableWorker();
			callserver.execute();	
		}
		if(e.getSource()==jb1){
			String UserPost =jtf.getText();
			FindTheOneWorker callserverfind=new FindTheOneWorker(UserPost);
			callserverfind.execute();	
			
		}
		
	}
	
	private class FindTheOneWorker extends SwingWorker<Void, Vector<String>>{
		
		private Vector columnNames;
		private Vector rowData;
		private String PostBase64;
		public FindTheOneWorker(String Post){
			this.PostBase64=Base64.encodeBase64String(Post.getBytes());
		}
	
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.FindTheOneMsg_MSG);
			tableMsg.setMenu(Msg.PostMenu);
			tableMsg.setFindOne(this.PostBase64);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				
				
				if(sMsg.getMsgType()==Msg.RespondPost_MSG){
					if(sMsg.getOKorNo().equals(Msg.OKmsg)){
						this.columnNames=sMsg.getColumnNames();
						this.rowData=sMsg.getEnrowData();
						DBMsg.TransInfo(this.rowData);
						model.setDataVector(this.rowData , this.columnNames);
					}else{
						 JOptionPane.showMessageDialog(null, "�޸ò��ż�¼��");
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
			tableMsg.setMsgType(Msg.PostMenuCallTable_MSG);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondPost_MSG){
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

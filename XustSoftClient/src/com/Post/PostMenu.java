package com.Post;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
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

import com.Absence.AbsenceMenu;
import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Secret.AESCoder;
import com.Secret.CertificateCoder;
import com.Stu.StuAddDiaLog;
import com.Tools.DBMsg;
import com.Tools.FileControl;
import com.Tools.SecretInfo;
import com.Tools.ServerMsg;
import com.Tools.SqlHelper;

public class PostMenu extends JFrame implements ActionListener{
	private FileControl FileCon=null;
	private ResultSet rs=null;
	private JPanel jp;
	private JButton Add,Delete,update;//新增、删除、刷新
	private JButton MsgFileGetIn,MsgFileGetOut;
	private JTable jtb=null;
	private JScrollPane jsp=null;
	
	private JPanel jp1,jp2;
	private JLabel jl;
	private JButton jb1,jb2;
	private JTextField jtf;
	private DefaultTableModel model;
	private DBMsg 	dbMsg=new DBMsg();
	
	private String DBTable="XustPostTable";
	private String []TableParas=dbMsg.XustPostTable;
	private PostAddDiaLog CAd=null;
	private String	UserType;
	
	public PostMenu(String UserType){
		this.UserType=UserType;
		Add=new JButton("新增");
		Add.addActionListener(this);
		Delete=new JButton("删除");
		Delete.addActionListener(this);
		
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
		jl=new JLabel("请输入部门");
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
			jtb=new JTable();//把模型加入到JTable
			jtb.setModel(model);
			
			jtb.setRowHeight(40);
		
		jsp=new JScrollPane(jtb);
		
		if(!UserType.equals("管理员")){
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
		new PostMenu("管理员");

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
		
		if(e.getSource()==Add){
			CAd=new PostAddDiaLog(this, "新建部门", false);
			
			
		}else if(e.getSource()==Delete){
			//删除
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			//转换成String 传入进model
			String UserPost=(String)model.getValueAt(rowNum, 0);
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"是否打算删除？", "请选择..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						AskDeleteWorker askServerDelete=new AskDeleteWorker(UserPost);
						askServerDelete.execute();
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
			tableMsg.setMenu(Msg.PostMenu);
			try {
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				if(sMsg.getMsgType()==Msg.RespondPostFileOutMsg){
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
						 JOptionPane.showMessageDialog(null, "无该部门记录！");
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
	private class AskDeleteWorker extends SwingWorker<Void, Void>{
		private String UserPost;
		public AskDeleteWorker(String UserPost){
			this.UserPost=UserPost;
		}
		@Override
		protected synchronized Void doInBackground() throws Exception {
			
			SecretMsg tableMsg=new SecretMsg();
			tableMsg.setMsgType(Msg.DeleteMsg);
			tableMsg.setMenu(Msg.PostMenu);
			byte[] enUserPost = AESCoder.encrypt(this.UserPost.getBytes(), SecretInfo.getKey());
			tableMsg.setEnPost(enUserPost);
			try {
			
				if(MapHoldReceiveThread.IsEmpty())
					return null;
				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
				oos.writeObject(tableMsg);
				
				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
				SecretMsg sMsg=(SecretMsg)ois.readObject();
				
				if(sMsg.getMsgType()==Msg.RespondPostDeleteMsg){
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
							JOptionPane.showMessageDialog(PostMenu.this, "删除成功！请刷新");
						else 
							JOptionPane.showMessageDialog(PostMenu.this, "删除失败！");

						}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	
}

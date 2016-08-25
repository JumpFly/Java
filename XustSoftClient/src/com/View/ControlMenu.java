package com.View;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.Absence.AbsenceMenu;
import com.Common.Msg;
import com.Common.SecretMsg;
import com.Model.MapHoldReceiveThread;
import com.Post.PostMenu;
import com.Secret.AESCoder;
import com.Stu.StuMenu;
import com.Tools.DBMsg;
import com.Tools.SecretInfo;
public class ControlMenu extends JFrame implements ActionListener {

	
	private TrayIcon trayIcon = null; // 托盘图标
	private SystemTray tray = null; // 本操作系统托盘的实例
	private JFileChooser Fch1;
	private JPanel panel1=null;
	private String UserID,UserType;
	private Date NowDate;
	private String FormDate;
	private SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Calendar ca=Calendar.getInstance();//初始化日历
	private int month; // get的0-11 代表1-12月
	private  int xOld = 0,yOld=0;  
	private Dialog	InfoDialog;
	private JLabel Photo=null,BG=null;
	private JLabel ID,Type,LDate=new JLabel();
	private JButton jb1,jb2,jb3,jb4,panelButton1,panelButton2,TransFile;
	private Font myFont=null;
	
	private StuMenu test1=null;
	private PostMenu test2=null;
	private AbsenceMenu test4=null;
	
	public String getTime(){
		ca=Calendar.getInstance();
		NowDate=(Date)ca.getTime();
		FormDate=df.format(NowDate);
		return FormDate;
	}
	 private class RemindTask extends TimerTask {
         public void run() {
          LDate.setText("Date  ："+ getTime());
         }
    }
	
	 private void tray() {

		 tray = SystemTray.getSystemTray(); // 获得本操作系统托盘的实例
		 ImageIcon icon = new ImageIcon("images/ShieldSmall.png"); // 将要显示到托盘中的图标

		 PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
		 MenuItem show = new MenuItem("显示面板");
		 MenuItem exit = new MenuItem("退出程序");
		 pop.add(show);
		 pop.add(exit);
		 trayIcon = new TrayIcon(icon.getImage(), "XustSoftClub", pop);

		 trayIcon.addMouseListener(new MouseAdapter() {
		 public void mouseClicked(MouseEvent e) {
		 if (e.getClickCount() == 2) { // 鼠标双击
		 setExtendedState(JFrame.NORMAL);
		 setVisible(true); // 显示窗口
		 toFront();
		 }
		 }
		 });
		 show.addActionListener(new ActionListener() { // 点击“显示窗口”菜单后将窗口显示出来
		 public void actionPerformed(ActionEvent e) {
		 setExtendedState(JFrame.NORMAL);
		 setVisible(true); // 显示窗口
		 toFront();
		 }
		 });
		 exit.addActionListener(new ActionListener() { // 点击“退出系统”菜单后退出程序
		 public void actionPerformed(ActionEvent e) {
				//在此处发送给Server 请求关闭链接
				if(!MapHoldReceiveThread.IsEmpty()){
					Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
					if(ss!=null)
						try {
							ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
							SecretMsg quitMsg=new SecretMsg();
							quitMsg.setMsgType(Msg.ClientQuitMsg);
							oos.writeObject(quitMsg);//发送退出信息
							ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
							SecretMsg sMsg=(SecretMsg)ois.readObject();
							if(sMsg.getMsgType()==Msg.RespondQuitMsg){
								MapHoldReceiveThread.RemoveSocket();
								System.out.println(" Client exist");
								 tray.remove(trayIcon); 
							 System.exit(0); // 退出程序
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}catch (ClassNotFoundException e2) {
							e2.printStackTrace();
						}
				}else{
					 tray.remove(trayIcon); 
				 System.exit(0); // 退出程序
				}
		 }
		 });

		 } 
	public ControlMenu(String UserID,String UserType){
		if (SystemTray.isSupported()) { // 如果操作系统支持托盘
			this.tray();
			try {
				tray.add(trayIcon); // 将托盘图标添加到系统的托盘实例中
				} catch (AWTException ex) {
				ex.printStackTrace();
				}
			}
		
		myFont =new Font("黑体",Font.PLAIN,20);
		this.UserID=UserID;
		this.UserType=UserType;
		NowDate=new Date();
		ca.setTime(NowDate); //设置日历时间
		this.month=ca.get(Calendar.MONTH)+1;
		FormDate=df.format(NowDate);
		Photo=new JLabel(new ImageIcon("images/XUST.png"));
		BG=new JLabel(new ImageIcon("images/BG3.jpg"));
		ID=new JLabel("UserID   :"+ UserID);
		ID.setFont(myFont); //设置字体
		ID.setForeground(Color.blue);
		
		Type=new JLabel("UserType ："+ UserType);
		Type.setFont(myFont); 
		Type.setForeground(Color.blue);
		LDate=new JLabel("Date  ："+ FormDate);
		LDate.setFont(myFont); 
		LDate.setForeground(Color.blue);
		
		jb1=new JButton("资讯中心");
		jb1.addActionListener(this);
		jb2=new JButton("学生管理");
		jb2.addActionListener(this);
		jb3=new JButton("部门管理");
		jb3.addActionListener(this);
		jb4=new JButton("缺勤查询");
		jb4.addActionListener(this);
		panelButton1=new JButton(new ImageIcon("images/panelButton1.png"));
		panelButton2=new JButton(new ImageIcon("images/panelButton2.png"));
		panelButton1.addActionListener(this);
		panelButton2.addActionListener(this);
		TransFile=new JButton("上传文件");
		TransFile.addActionListener(this);
		
		this.setLayout(null);
		BG.setBounds(0, 0, 410, 600);
		Photo.setBounds(0, 0, 100, 100);
		ID.setBounds(110, 10, 180, 30);
		Type.setBounds(110,45, 180, 30);
		LDate.setBounds(110, 75, 300, 30);
		
		jb1.setBounds(8,130, 100, 50);
		jb2.setBounds(8,240, 100, 50);
		jb3.setBounds(8,350, 100, 50);
		jb4.setBounds(8,460, 100, 50);
		panelButton2.setBounds(340, 0, 35, 35);
		panelButton1.setBounds(375, 0, 35, 35);
		TransFile.setBounds(230, 130, 100, 48);
		this.add(Photo);
		this.add(ID);
	
		this.add(Type);
		this.add(LDate);
		this.add(jb1);
		this.add(jb2);
		this.add(jb3);
		this.add(jb4);
		this.add(panelButton1);
		this.add(panelButton2);
		this.add(TransFile);
		this.add(BG);
		 //设置窗口
		  ImageIcon icon =new ImageIcon("images/Shield.png");
		  this.setIconImage(icon.getImage());//窗口图标
		  this.setTitle("管理界面");
		  this.setSize(410,600);
		  this.setLocation(900,200);
		  this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		  this.setResizable(false);
		  this.setUndecorated(true);
		  Timer timer = new Timer();
		  timer.schedule(new RemindTask(), 0, 1000);
		  this.addMouseListener(new MouseAdapter() {
			  @Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				   xOld = e.getX();  
	                yOld = e.getY();  
			}
		});
	        this.addMouseMotionListener(new MouseMotionAdapter() {  
	            @Override  
	            public void mouseDragged(MouseEvent e) {  
	                int xOnScreen = e.getXOnScreen();  
	                int yOnScreen = e.getYOnScreen();  
	                int xx = xOnScreen - xOld;  
	                int yy = yOnScreen - yOld;  
	                ControlMenu.this.setLocation(xx, yy);  
	            }  
	        }); 
	}
	
	

	public static void main(String[] args) {
		ControlMenu CCM=new ControlMenu("jumpfly","管理员");
		CCM.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==panelButton1){
			this.dispose();
		}
		if(e.getSource()==panelButton2){
			this.setExtendedState(JFrame.ICONIFIED);
		}
		if(e.getSource()==TransFile){
			 Fch1=new JFileChooser();
			Fch1.setDialogTitle("请选择文件...");
			//默认方式
			final int value=Fch1.showOpenDialog(null);
			//显示
			Fch1.setVisible(true);
			if(value==Fch1.CANCEL_OPTION)
				return;
			//得到选中文件的绝对路径
			String filepath=Fch1.getSelectedFile().getAbsolutePath();
			String fileName=Fch1.getSelectedFile().getName();
//			
//			
//			TransFileWorker transFileWorker=new TransFileWorker(filepath,fileName);
//			transFileWorker.execute();
//			InfoDialog=new Dialog();
//				
		}
		if(e.getSource()==jb1){
			
		
			
			
		}
		if(e.getSource()==jb2){
//			if(UserType.equals("非会员")){
//				JOptionPane.showMessageDialog(this, "非会员无权查看");
//				return;
//			}
			 test1=new StuMenu(UserType);
				
		}
		if(e.getSource()==jb3){
//			if(UserType.equals("非会员")){
//				JOptionPane.showMessageDialog(this, "非会员无权查看");
//				return;
//			}
			test2=new PostMenu(UserType);
			
		}
		if(e.getSource()==jb4){
//			if(UserType.equals("非会员")){
//				JOptionPane.showMessageDialog(this, "非会员无权查看");
//				return;
//			}
			test4=new AbsenceMenu(UserType);
		}
		
	}
//
//	private class TransFileWorker extends SwingWorker<Void, Void>{
//		
//		private String filepath;
//		private String fileName;
//		private FileInputStream In=null;
//		public TransFileWorker(String filepath,String fileName){
//			this.filepath=filepath;
//			this.fileName=fileName;
//		}
//	
//		@Override
//		protected synchronized Void doInBackground() throws Exception {
//			
//			SecretMsg tableMsg=new SecretMsg();
//			tableMsg.setMsgType(Msg.TransFileMsg);
//			try {
//				if(MapHoldReceiveThread.IsEmpty())
//					return null;
//				Socket ss=MapHoldReceiveThread.getClientConSerThread().getSocket();
//				ObjectOutputStream oos=new ObjectOutputStream(ss.getOutputStream());
//				byte[] enFileName=AESCoder.encrypt(this.fileName.getBytes(), SecretInfo.getKey());
//				tableMsg.setEnMsg(enFileName);
//				
//				
//				oos.writeObject(tableMsg);
//				OutputStream out=ss.getOutputStream();
//				AESCoder.encryptTransFile(this.filepath, out, SecretInfo.getKey());
//				 JOptionPane.showMessageDialog(null, "上传完成！");
//				   InfoDialog.Close();
////				ObjectInputStream ois = new ObjectInputStream(ss.getInputStream());
////				SecretMsg sMsg=(SecretMsg)ois.readObject();
////				if(sMsg.getMsgType()==Msg.RespondTransFileMsg){
////					
////					
////					
////				}
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//			return null;
//		}
//	}
//private	class Dialog extends  JDialog {
//		  private JProgressBar progressBar;
//		  private JLabel Note;
//		  public  Dialog(){
//			
//			  progressBar=new JProgressBar();
//			  progressBar.setBackground(Color.BLUE);
//			  progressBar.setIndeterminate(true);
//			  this.add(progressBar);
//			  this.setTitle("文件正在上传..");
//			  this.setSize(350, 75);
//			  this.setResizable(false);
//			  this.setVisible(true);
//			  this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//			 
//		  }
//		  public void Close(){
//			  progressBar.setIndeterminate(false);
//			  progressBar.setStringPainted(true);
//			  progressBar.setValue(100);
//			 this.dispose();
//		  }
//		
//	}
	
}

package com.Stu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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
import javax.swing.border.Border;

import com.Tools.SqlHelper;
import com.User.UserMsgModel;

public class UserUPMsg extends JDialog implements ActionListener{
	private ResultSet rs=null;
	private String[] UserPosts;
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
	public void FillMsg(){
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
	public String[] ReturnUserPosts(){
		String UserPost="";
		SqlHelper sqlhelp= SqlHelper.getInstance();
		try {
			String sql="select UserPost from XustPost";
			rs=sqlhelp.queryExecute(sql);
			while(rs.next()){
				UserPost+=rs.getString(1)+" ";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlhelp.DBclose();
		}
		return UserPost.split(" ");
	}
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
		
		
		UserPosts=ReturnUserPosts();
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
		
		
		FillMsg();
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
			Sex=female.isSelected()?"女":"NULL";
			String UserPost=  (String)DownList.getSelectedItem();
			String strsql=
					"update DetailMsg set UserNum=?,UserName=? ,UserSex=? ,UserMajor=? ,UserPost=?  ,UserDate=? ,UserMotto=? where UserID=?";
			
			String []paras=
				{myTextFields[1].getText().trim(),myTextFields[2].getText().trim(),Sex,myTextFields[3].getText().trim(),UserPost,myTextFields[5].getText().trim(),MottoArea.getText(),myTextFields[0].getText().trim()};
			
			UserMsgModel temp=new UserMsgModel();
			if(!temp.EditUser(strsql, paras,"DetailMsg_Up")){
				JOptionPane.showMessageDialog(this, "修改失败！");
			  }else{
				  JOptionPane.showMessageDialog(this, "修改成功！");
					 
			  }
			this.dispose();
			}
		
	}
	public static void main(String[] args) {
		 new UserUPMsg(null,true,"1408020215");
	}
}

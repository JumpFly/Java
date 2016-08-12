package com.View;
/*
 * ���繤��1402 ��־��
 * ��Ŀ��ʼ��2016.7.2
 * ��Ŀ�����2016.7.4
 * ��ʱ3��
 * �ҽ�sql server2008R2
 * 
 * */
import com.User.*; 
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Tools.*;

public class LoginView extends JFrame implements ActionListener{
	
	ResultSet rs=null;
	UserAddDiaLog AddDialog=null;
	UserMailForPassword FindPassword=null;
	JLabel BG=null; //���ñ���ͼ
	JLabel UserID,Pass,UserType;
	JButton jb1,jb2,jb3;//��¼ע�� ��������
	JTextField jtf_ID=null;
	JPasswordField jpf_pass=null;
	JPanel myJPanel,myJPanel2;
	String [] Type={"�ǻ�Ա","����Ա","��Ա"};
	JComboBox DownList=null;	
	Font myFont=null;
	
	public LoginView(){

		myFont =new Font("����",Font.PLAIN,20);
		jtf_ID=new JTextField(20);
		jtf_ID.addActionListener(this);
		jpf_pass=new JPasswordField(20);
		jpf_pass.addActionListener(this);
		jb1=new JButton("��¼");
		jb2=new JButton("ע��");
		jb3=new JButton("��������");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		BG=new JLabel(new ImageIcon("images/BG.png"));
		
		UserID=new JLabel("��¼  ID��",JLabel.CENTER);
		UserID.setFont(myFont); //��������
		UserID.setForeground(Color.blue);//����������ɫ����
		
		Pass=new JLabel("��¼���룺",JLabel.CENTER);
	    Pass.setFont(myFont); //��������
		Pass.setForeground(Color.blue);//����������ɫ����
		
		UserType=new JLabel("�û����ͣ�",JLabel.CENTER);
		UserType.setFont(myFont); //��������
		UserType.setForeground(Color.blue);//����������ɫ����
		DownList=new JComboBox(Type);
		
		this.setLayout(new GridLayout(2, 1));
		
		UserID.setBounds(18, 20, 150, 60);
		Pass.setBounds(18, 85, 150, 60);
		UserType.setBounds(18, 135, 150, 80);
		jtf_ID.setBounds(150, 25, 180, 50);
		jpf_pass.setBounds(150, 95, 180, 50);
		DownList.setBounds(150, 160, 180, 50);
		jb1.setBounds(20, 225, 100, 40);
		jb2.setBounds(145, 225, 100, 40);
		jb3.setBounds(270, 225, 100, 40);
		this.add(BG);
	
		myJPanel=new JPanel(null);
		myJPanel.add(UserID);
		myJPanel.add(Pass);
		myJPanel.add(jb1);
		myJPanel.add(jb2);
		myJPanel.add(jb3);
		myJPanel.add(jtf_ID);
		myJPanel.add(jpf_pass);
		myJPanel.add(UserType);
		myJPanel.add(DownList);
		this.add(myJPanel);
		
	 //���ô���
	  ImageIcon icon =new ImageIcon("images/Lock.png");
	  this.setIconImage(icon.getImage());//����ͼ��
	  this.setTitle("�û���¼");
	     this.setSize(410,600);
	     this.setLocation(800,200);
	     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   this.setResizable(false);
	    //��ʾ����
	     this.setVisible(true);
	}  
	  
	public static void main(String[] args) {
		LoginView LV=new LoginView();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb1){
			String Pass=new String(jpf_pass.getPassword()),UserType="",UserPass="";
			String UserID=jtf_ID.getText().trim();
			String Type=  (String)DownList.getSelectedItem();
			String UserCoins="";
			SqlHelper sqlhelp= new SqlHelper();
			try {
				rs=sqlhelp.queryExec(UserID);		
				while(rs.next()){
					UserPass=rs.getString(2);
					UserType=rs.getString(3);
				}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if(Pass.equals(UserPass)&&Type.equals(UserType)){
				JOptionPane.showMessageDialog(this, "��¼�ɹ���");
				System.out.println(UserID+"  "+UserPass+"  "+UserType);
				sqlhelp.DBclose();
				ControlMenu CCM=new ControlMenu(UserID,UserType);
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(this, "ID/����/���� ����");
			}
		}	
		if(e.getSource()==jb2){
			AddDialog=new UserAddDiaLog(null, "ע���û�", true);
		}
		if(e.getSource()==jb3){
			FindPassword=new UserMailForPassword(null, "�һ�����", true);
		}
		
	}
		
	}

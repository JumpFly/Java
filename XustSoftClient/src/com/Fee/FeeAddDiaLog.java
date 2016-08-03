package com.Fee;
import com.User.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.Tools.*;
public class FeeAddDiaLog extends JDialog implements ActionListener{

	JLabel jl[] =new JLabel[9];
	JTextField jtf[]=new JTextField[8];
	JButton Choose,Cancel,ResBtn;//ȷ�ϡ�ȡ��
	 JScrollPane jsp;
	 JPanel jp1,jp2,jp3;
	 String[] FeeTable = {"ѧ��","�·�","ס�޷�","��ʳ��","�鱾��","�յ���","ů����","�˷�","��ĸְ��"};
	 String[] HomeType={"��ְ��","˫ְ��","��ְ"};
	 JComboBox DownList=null;		
	 Font myFont=null;
		Date NowDate;
		String FormDate;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca=Calendar.getInstance();//��ʼ������
		int month; // get��0-11 ����1-12��
		
	public static void main(String[] args) {
		FeeAddDiaLog ua=new FeeAddDiaLog(null,"123",false);
	}
	
	public FeeAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //���๹�췽����ģʽ�Ի���Ч��
		myFont =new Font("����",Font.PLAIN,18);
		NowDate =new Date();
		ca.setTime(NowDate); //��������ʱ��
		this.month=ca.get(Calendar.MONTH)+1;
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(9, 1));
		jp2.setLayout(new GridLayout(9, 1));
		
		
		for(int i=0;i<FeeTable.length;i++){
			
			jl[i]=new JLabel(FeeTable[i]);
			jl[i].setFont(myFont); //��������
			jp1.add(jl[i]);

			}
		for(int i=0;i<jtf.length;i++){
			
			jtf[i]=new JTextField();
			jp2.add(jtf[i]);

			}
		
		
		
		
		jtf[1].setText(this.month+"");
		DownList=new JComboBox(HomeType);
		jp2.add(DownList);
		
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
		
		this.setLocation(800, 100);
		this.setSize(300,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Choose){
			String Hometype=  (String)DownList.getSelectedItem();
			UserMsgModel temp=new UserMsgModel();
			String sql="insert into Fee values(?,?,?,?,?,?,?,?,?)";
			String []paras=
			{jtf[0].getText(),jtf[1].getText(),jtf[2].getText(),jtf[3].getText(),jtf[4].getText(),jtf[5].getText(),jtf[6].getText(),jtf[7].getText(),Hometype};
				
			for(int i=0;i<paras.length;i++)
				System.out.print(paras[i]+" ");
		
			SqlHelper sqlhelp =new SqlHelper();
			/*
			 * ע�⣺ȱ�ڼ�¼ ��ѧ��+�·���Ϊ����
			 * */
			String[] IDs={jtf[0].getText(),jtf[1].getText()};
			if(sqlhelp.CheckExist(IDs,"FeeTable")==true){
				JOptionPane.showMessageDialog(this, "�ü�¼�Ѵ��ڣ�");
			}else{
			if(!temp.EditUser(sql, paras,"FeeTable")){
				JOptionPane.showMessageDialog(this, "���ʧ�ܣ�");
			  }else{
				  
				  JOptionPane.showMessageDialog(this, "��ӳɹ���");
				  
			  }
		 }
		}
		if(e.getSource()==ResBtn){
			for(int i=0;i<jtf.length;i++){
				jtf[i].setText("");
				}
			jtf[1].setText(this.month+"");
		 	}
		if(e.getSource()==Cancel){
			this.dispose();
		 	}
	}

	
}

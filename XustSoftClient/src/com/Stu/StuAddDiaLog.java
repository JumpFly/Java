package com.Stu;
import com.User.*;
import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.Tools.*;
public class StuAddDiaLog extends JDialog implements ActionListener{
	private ResultSet rs=null;
	private String [] UserPosts=null;	
	private JButton Choose,Cancel,ResBtn;//确认、取消、重置
	private JPanel jp1,jp2,jp3;
	private String DetailMsgTable[]= DBMsg.DetailMsgTable;
	private JLabel jl[] =new JLabel[DetailMsgTable.length+1];
	private JTextField jtf[]=new JTextField[DetailMsgTable.length+1];
	private Font myFont=null;
	private Date NowDate;
	private String FormDate;
	private SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	
	
	
	public StuAddDiaLog(Frame owner,String title,boolean model ){
		super(owner,title,model); //父类构造方法，模式对话框效果
		myFont =new Font("黑体",Font.PLAIN,18);
		
		NowDate=new Date();
		FormDate=df.format(NowDate);
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp1.setLayout(new GridLayout(jl.length, 1));
		jp2.setLayout(new GridLayout(jl.length, 1));
		
		jl[0]=new JLabel("账号");
		jl[0].setFont(myFont); //设置字体
		jp1.add(jl[0]);
		jtf[0]=new JTextField();
		jp2.add(jtf[0]);
		
		for(int i=1;i<jl.length;i++){
			jl[i]=new JLabel(DetailMsgTable[i-1]);
			jl[i].setFont(myFont); //设置字体
			jp1.add(jl[i]);
			jtf[i]=new JTextField();
			jp2.add(jtf[i]);
		}
		
	
		Choose=new JButton("确定");
		Cancel=new JButton("取消");
		ResBtn=new JButton("重置");
		Choose.addActionListener(this);
		Cancel.addActionListener(this);
		ResBtn.addActionListener(this);
		
		jp3.add(ResBtn);
		jp3.add(Choose);
		jp3.add(Cancel);
		
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		this.setLocation(800, 300);
		this.setSize(300,400);
		this.setResizable(false);
		this.setVisible(true);
		 this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	public static void main(String[] args) {
		StuAddDiaLog Sa=new StuAddDiaLog(null,"123",false);

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Cancel){
			this.dispose();
		 }
		if(e.getSource()==ResBtn){
			for(int i=0;i<jtf.length;i++){
				jtf[i].setText("");
			}
		 }
		if(e.getSource()==Choose){
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"是否确定？", "请选择..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						if(TextFiledIsEmpty())
						{
							JOptionPane.showMessageDialog(this, "账号/学号/邮箱不能为空！");
							return;
						}
					UserMsgModel temp=new UserMsgModel();
					
					String sql="insert into DetailMsg (UserID,UserNum,UserName,UserSex,UserMajor,UserMail)values(?,?,?,?,?,?)";
					String []paras=
					{jtf[0].getText().trim(),jtf[1].getText().trim(),jtf[2].getText().trim(),jtf[3].getText().trim(),jtf[4].getText().trim(),jtf[5].getText().trim()};
					System.out.println(jtf[0].getText()+" "+jtf[1].getText()+" "+jtf[2].getText()+" "+jtf[3].getText()+" "+jtf[4].getText()+" "+jtf[5].getText());
			
					SqlHelper sqlhelp =SqlHelper.getInstance();
					String[] IDs={jtf[0].getText().trim(),jtf[1].getText().trim(),};
					if(sqlhelp.CheckExist(IDs,"PersonTable")==true){
						JOptionPane.showMessageDialog(this, "该ID或学号已存在！");
					}else{
						if(!temp.EditUser(sql, paras,"DetailMsg_Up")){
							JOptionPane.showMessageDialog(this, "添加失败！");
						  }else{
							  JOptionPane.showMessageDialog(this, "添加成功！");
							  String sql2="insert into Person values(?,?,?,?,?)";
							  String []paras2=
									{jtf[0].getText().trim(),"123456","会员",jtf[1].getText().trim(),jtf[5].getText().trim()};
									
							  sqlhelp.EditExec(sql2, paras2, "PersonTable");
						  }
					}
					
					}
			}
		
	}
	public boolean TextFiledIsEmpty(){
		boolean flag=false;
		String jtf0=jtf[0].getText().trim();
		String jtf1=jtf[0].getText().trim();
		String jtf5=jtf[0].getText().trim();
		if(jtf0.toLowerCase().equals("null")||jtf1.toLowerCase().equals("null")||jtf5.toLowerCase().equals("null"))
			flag= true;
		if(jtf0.equals("")||jtf1.equals("")||jtf5.equals(""))
			flag= true;
		return flag;
	}

}

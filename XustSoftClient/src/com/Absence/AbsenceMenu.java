package com.Absence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.Stu.StuAddDiaLog;
import com.Tools.DBMsg;
import com.Tools.FileControl;
import com.Tools.SqlHelper;
import com.User.UserMsgModel;

public class AbsenceMenu extends JFrame implements ActionListener{

	private FileControl FileCon=null;
	private JPanel jp;
	private JButton Add,Delete,update;//新增、删除、刷新
	private JButton MsgFileGetIn,MsgFileGetOut;
	private JTable jtb=null;
	private JScrollPane jsp=null;
	
	private JPanel jp1,jp2;
	private JLabel jl;
	private JButton jb1,jb2;
	private JTextField jtf;
	
	private UserMsgModel UMM;
	private DBMsg 	dbMsg=new DBMsg();
	private String UserType;
	private String DBTable="AbsenceTable";
	private String []TableParas=dbMsg.AbsenceTable;
	public AbsenceMenu(String Type){
		this.UserType=Type;
		
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
		jl=new JLabel("请输入学号");
		jp1.add(MsgFileGetIn);
		jp1.add(jl);
		jp1.add(jtf);
		jp1.add(jb1);
		jp1.add(update);
		jp1.add(MsgFileGetOut);
		
		jp2=new JPanel();
		
		
		
		jp2.add(Add);
		jp2.add(Delete);

		
		//创建数据模型对象
		  UMM=new UserMsgModel();
		  String sql ="select * from Absence";
		  UMM.queryUser(sql,this.TableParas,DBTable);
		
		jtb=new JTable(UMM);//把模型加入到JTable
		
		jsp=new JScrollPane(jtb);
		
		if(!UserType.equals("管理员")){
			Add.setEnabled(false);
			Delete.setEnabled(false);
			MsgFileGetIn.setEnabled(false);
			MsgFileGetOut.setEnabled(false);
		}
		
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setTitle("缺勤管理");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new AbsenceMenu("管理员");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==MsgFileGetIn){
			//导入
			FileCon=new FileControl();
			FileCon.ReadInFile(this.DBTable);
		}else if(e.getSource()==MsgFileGetOut){
			//导出
			FileCon=new FileControl();
			FileCon.SaveToFile(this.DBTable);
		}
		
		if(e.getSource()==Add){
			
			AbsenceDiaLog test=new AbsenceDiaLog(this, "缺勤记录", false);
			
			
		}else if(e.getSource()==Delete){
			
			//删除
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			//转换成String 传入进model
			String ID=(String)UMM.getValueAt(rowNum, 0);
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"是否打算删除？", "请选择..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String sql ="delete from Absence where UserNum=?";
						String[] paras={ID};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "删除成功！");
						}else {
							JOptionPane.showMessageDialog(this, "删除失败！");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from Absence";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//获取新的数据模型 
						
					   }
			
			
		}else if(e.getSource()==update){
			
			UMM=new UserMsgModel();
			 String sql2 ="select * from Absence";
			  UMM.queryUser(sql2,this.TableParas,DBTable);
			jtb.setModel(UMM);//获取新的数据模型 		
		}
		if(e.getSource()==jb1){
			String UserNum =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(UserNum.equals("")){
				  sql ="select * from Absence";
				  UMM.queryUser(sql,this.TableParas,DBTable);
				  jtb.setModel(UMM);//获取新的数据模型 
			}else{
			 sql="select * from Absence where UserNum='"+UserNum+"'";
			
			 SqlHelper sqlhelp=SqlHelper.getInstance();
			String[] UserNums={UserNum};
			 if(sqlhelp.CheckExist(UserNums, "AbsenceTable_Up")){
				 UMM.queryUser(sql,this.TableParas,DBTable);
				 jtb.setModel(UMM);//获取新的数据模型 
			 }
				
			 else
				 JOptionPane.showMessageDialog(this, "无该学号记录！");
			 
			 
			}
		
			
		}
		
	}

}

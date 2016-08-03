package com.Stu;
import com.Absence.*;
import java.awt.BorderLayout;
import com.User.*;
import com.Absence.AbsenceDiaLog;
import com.Stu.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.Tools.*;
public class StuMenu extends JFrame implements ActionListener{
		ResultSet rs=null;
		FileControl FileCon=null;
		JPanel jp;
		JButton CheckIn,EnterIn,update;//考勤录入、档案录入	
		JButton MsgFileGetIn,MsgFileGetOut;
		JTable jtb=null;
		JScrollPane jsp=null;
		
		JPanel jp1,jp2;
		JLabel jl;
		JButton jb1,jb2,jb3,jb4;
		JTextField jtf;
		
		UserMsgModel UMM;
		DBMsg 	dbMsg=new DBMsg();
		
		String DBTable="StuTable";
		String []TableParas=dbMsg.StuTable;
		String [] ClassIDs=null;
		StuAddDiaLog stuAddDiaLog=null;
		StuUpDiaLog uud=null;
		
		
		public String[] ReturnClassIDs(){
			String ClassIDs="";
			SqlHelper sqlhelp= new SqlHelper();
			try {
				String sql="select ClassID from Class";
				rs=sqlhelp.queryExecute(sql);
				while(rs.next()){
					ClassIDs+=rs.getString(1)+" ";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlhelp.DBclose();
			}
			return ClassIDs.split(" ");
		}
		
		public  void UpdataClassMSG(){
			String sql="",sql2="";
			SqlHelper sqlhelp=new SqlHelper();
			String[]	ClassIDs=ReturnClassIDs();
		
			for (int i = 0; i < ClassIDs.length; i++) {
				int NUM=0;
				sql="select *from Student where ClassID="+"'"+ClassIDs[i]+"'";
				try {
					rs=sqlhelp.queryExecute(sql);
					while(rs.next()){
						NUM++;
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}finally {
					sqlhelp.DBclose();
				}
				sql2="update Class set PersonNum="+"'"+NUM+"'"+" where ClassID="+"'"+ClassIDs[i]+"'";
					try {
					sqlhelp.queryExe(sql2);
				} catch (Exception e2) {
					e2.printStackTrace();
				}finally {
					sqlhelp.DBclose();
				}
			}
		}	
		
	public static void main(String[] args) {
		StuMenu test2=new StuMenu();

	}
	
	public StuMenu(){
		
	
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
		
		jl=new JLabel("请输入账号");
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
		
		//创建数据模型对象
		  UMM=new UserMsgModel();
		  String sql ="select * from Student";
		  UMM.queryUser(sql,this.TableParas,DBTable);
		
		jtb=new JTable(UMM);//把模型加入到JTable
		
		jsp=new JScrollPane(jtb);
		
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setTitle("学生管理");
		this.setSize(500,400);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
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
		
		
		if(e.getSource()==CheckIn){
			AbsenceMenu test=new AbsenceMenu("校长助理");
			
		}else if(e.getSource()==EnterIn){
		 stuAddDiaLog=new StuAddDiaLog(this, "档案录入", true);
			
		}
		if(e.getSource()==update){
			/*刷新班级人数,刷新列表
			 * */
			UpdataClassMSG();
			UMM=new UserMsgModel();
			 String sql3 ="select * from Student";
			  UMM.queryUser(sql3,this.TableParas,DBTable);
			jtb.setModel(UMM);//获取新的数据模型 		
		}
		
		if(e.getSource()==jb1){
			String ID =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(ID.equals("")){
				  sql ="select * from Student";
				  UMM.queryUser(sql,this.TableParas,DBTable);
			}else{
			 sql="select * from Student where UserID="+ID;
			 
			  UMM.queryUser(sql,this.TableParas,DBTable);
			}
		
			jtb.setModel(UMM);//获取新的数据模型 
		}
		
		if(e.getSource()==jb3){
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			uud=new StuUpDiaLog(this, "修改用户数据", true, UMM, rowNum);
				UMM=new UserMsgModel();
				String sql2 ="select * from Student";
				UMM.queryUser(sql2,this.TableParas,DBTable);
				jtb.setModel(UMM);//获取新的数据模型 
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
			String ID=(String)UMM.getValueAt(rowNum, 0);
			//获得选中的这行的 第一个字段（列）
			/*
			 * 弹出窗口确认是否要删除
			 * */
			int	res=JOptionPane.showConfirmDialog(null, 
					"是否打算删除？", "请选择..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String sql ="delete from Student where UserID=?";
						String[] paras={ID};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "删除成功！请刷新");
						}else {
							JOptionPane.showMessageDialog(this, "删除失败！");
								
						}
						
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from Student";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//获取新的数据模型 
						
					   }
			
			
		}
		
	}


	
}

package com.Fee;

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.Stu.StuAddDiaLog;
import com.Tools.*;
import com.User.UserMsgModel;

public class FeeMenu extends JFrame implements ActionListener{

	FileControl FileCon=null;
	ResultSet rs=null;
	Date newDa=null;
	JPanel jp;
	JButton Add,Delete,update;//新增、删除、刷新
	JButton FeeGetIn,FeeGetOut;
	JButton MsgFileGetIn,MsgFileGetOut;
	JTable jtb=null;
	JScrollPane jsp=null;
	
	JPanel jp1,jp2;
	JLabel jl;
	JButton jb1,jb2;
	JTextField jtf;
	
	UserMsgModel UMM;
	DBMsg 	dbMsg=new DBMsg();
	String UserType;
	String DBTable="FeeTable";
	String []TableParas=dbMsg.FeeTable;
	FeeAddDiaLog test=null;
	public FeeMenu(String Type){
		this.UserType=Type;
		Add=new JButton("新增");
		Add.addActionListener(this);
		Delete=new JButton("删除");
		Delete.addActionListener(this);
		FeeGetIn =new JButton("收费");
		FeeGetIn.addActionListener(this);
		FeeGetOut =new JButton("退费");
		FeeGetOut.addActionListener(this);
	
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
		jp2.add(FeeGetIn);
		jp2.add(FeeGetOut);
		jp2.add(Delete);

		
		//创建数据模型对象
		  UMM=new UserMsgModel();
		  String sql ="select * from Fee";
		  UMM.queryUser(sql,this.TableParas,DBTable);
		
		jtb=new JTable(UMM);//把模型加入到JTable
		
		jsp=new JScrollPane(jtb);
		
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setTitle("费用管理");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new FeeMenu("管理员");
		
	}

	public void GetMoney(String ID,float money){
		String sql="update Person set UserCoin=UserCoin-? where UserID=?";
		
		String[] IDs={ID};
		SqlHelper sqlhelp=new SqlHelper();
		if(!sqlhelp.CheckExist(IDs, "PersonTable")){
			JOptionPane.showMessageDialog(this, "Person表中无此用户！");
			return;
		}  
		try {
			String[] paras={String.valueOf(money),ID};
			if(sqlhelp.EditExec(sql, paras, "PersonTable"))
				JOptionPane.showMessageDialog(this, "该同学收费成功！");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void BackMoney(String ID,int month){
		String sql="update Fee set BackFee=BackFee+? where UserID=? and Month=?";
		String MsgDa="";

		String[] IDs={ID};
		SqlHelper sqlhelp=new SqlHelper();
		if(!sqlhelp.CheckExist(IDs, "AbsenceTable_Up")){
			JOptionPane.showMessageDialog(this, "此用户无缺勤,无须退费！");
			return;
		}  
		try {
			int Num=0;
			String sql2="select * from Absence where UserID="+ID;
			rs=sqlhelp.queryExecute(sql2);
			while(rs.next()){
				MsgDa=rs.getString(2);
				newDa=new SimpleDateFormat("yyyy-MM-dd").parse(MsgDa);
				int MM=newDa.getMonth()+1;
				if(MM==month){
					Num++;
				}
			}
			
			
			String[] paras={String.valueOf(Num*10),ID,String.valueOf(month)};
			if(sqlhelp.EditExec(sql, paras, "FeeTable_Up"))
				JOptionPane.showMessageDialog(this, "该同学退费成功！");
			String sql3="delete from Absence where UserID=? and UserDate like "+"'%"+month+"%'";
				sqlhelp.EditExec(sql3, IDs, "delete");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==FeeGetIn){
			if(!this.UserType.equals("财务人员")&&!this.UserType.equals("管理员")){
				JOptionPane.showMessageDialog(this, this.UserType+"无此权限！");
				return;
			}
			//删除
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			//转换成String 传入进model
			String ID=(String)UMM.getValueAt(rowNum, 0);
			float money=0;
			for (int i = 2; i < 7; i++) {
				money+=(float)UMM.getValueAt(rowNum, i);
				
			}
			GetMoney(ID, money);
			
			
			
			
		}else if(e.getSource()==FeeGetOut){
			if(!this.UserType.equals("财务人员")&&!this.UserType.equals("管理员")){
				JOptionPane.showMessageDialog(this, this.UserType+"无此权限！");
				return;
			}
			//删除
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			//转换成String 传入进model
			String ID=(String)UMM.getValueAt(rowNum, 0);
			int month=(int)UMM.getValueAt(rowNum, 1);
			BackMoney(ID, month);
		}
		
		
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
			if(!this.UserType.equals("财务人员")&&!this.UserType.equals("管理员")){
				JOptionPane.showMessageDialog(this, this.UserType+"无此权限！");
			}
			else{
				 test=new FeeAddDiaLog(this, "费用管理", false);
			}
			
		}else if(e.getSource()==Delete){
			if(!this.UserType.equals("财务人员")&&!this.UserType.equals("管理员")){
				JOptionPane.showMessageDialog(this, this.UserType+"无此权限！");
				return;
			}
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
						String sql ="delete from Fee where UserID=?";
						String[] paras={ID};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "删除成功！");
						}else {
							JOptionPane.showMessageDialog(this, "删除失败！");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from Fee";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//获取新的数据模型 
						
					   }
			
			
		}else if(e.getSource()==update){
			
			UMM=new UserMsgModel();
			 String sql2 ="select * from Fee";
			  UMM.queryUser(sql2,this.TableParas,DBTable);
			jtb.setModel(UMM);//获取新的数据模型 		
		}else if(e.getSource()==FeeGetIn){
		//	\\收费
		}else if(e.getSource()==FeeGetOut){
			
		//	\\退费	
		}
		if(e.getSource()==jb1){
			String ID =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(ID.equals("")){
				  sql ="select * from Fee";
				  UMM.queryUser(sql,this.TableParas,DBTable);
			}else{
			 sql="select * from Fee where UserID="+ID;
			 /*
			  * 有空最好改善一下交互，若查找的ID不存在则给出提示。
			  * */
			 
			  UMM.queryUser(sql,this.TableParas,DBTable);
			}
		
			jtb.setModel(UMM);//获取新的数据模型 
		}
		
	}

}

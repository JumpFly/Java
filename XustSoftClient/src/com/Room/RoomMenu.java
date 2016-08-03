package com.Room;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

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

public class RoomMenu extends JFrame implements ActionListener{
	FileControl FileCon=null;
	ResultSet rs=null;
	JPanel jp;
	JButton Add,Delete,update;//新增、删除、刷新
	JButton MsgFileGetIn,MsgFileGetOut;
	JTable jtb=null;
	JScrollPane jsp=null;
	
	JPanel jp1,jp2;
	JLabel jl;
	JButton jb1,jb2;
	JTextField jtf;
	
	UserMsgModel UMM;
	DBMsg 	dbMsg=new DBMsg();
	
	String DBTable="ClassTable";
	String []TableParas=dbMsg.ClassTable;
	ClassAddDiaLog CAd=null;

	public  String[] ReturnClassIDs(){
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
	public RoomMenu(){
		UpdataClassMSG();
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
		jl=new JLabel("请输入班号");
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
		  String sql ="select * from Class";
		  UMM.queryUser(sql,this.TableParas,DBTable);
		
		jtb=new JTable(UMM);//把模型加入到JTable
		
		jsp=new JScrollPane(jtb);
		
		
		this.add(jsp);
		this.add(jp1,"North");
		this.add(jp2,"South");
		this.setSize(500,400);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new RoomMenu();

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
			CAd=new ClassAddDiaLog(this, "新建班级", false);
			
			
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
						String sql ="delete from Class where ClassID=?";
						String[] paras={ID};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "删除成功！");
						}else {
							JOptionPane.showMessageDialog(this, "删除失败！");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from Class";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//获取新的数据模型 
						
					   }
			
			
		}else if(e.getSource()==update){
			/*先刷新班级人数，再刷新列表
			 * */
			UpdataClassMSG();
			
			UMM=new UserMsgModel();
			 String sql3 ="select * from Class";
			  UMM.queryUser(sql3,this.TableParas,DBTable);
			jtb.setModel(UMM);//获取新的数据模型 		
		}
		if(e.getSource()==jb1){
			String ID =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(ID.equals("")){
				  sql ="select * from Class";
				  UMM.queryUser(sql,this.TableParas,DBTable);
			}else{
			 sql="select * from Class where ClassID="+ID;
			 /*
			  * 有空最好改善一下交互，若查找的ID不存在则给出提示。
			  * */
			 
			  UMM.queryUser(sql,this.TableParas,DBTable);
			}
		
			jtb.setModel(UMM);//获取新的数据模型 
		}
		
	}

}

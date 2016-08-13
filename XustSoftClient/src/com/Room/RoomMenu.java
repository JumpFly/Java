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
	
	private UserMsgModel UMM;
	private DBMsg 	dbMsg=new DBMsg();
	
	private String DBTable="XustPostTable";
	private String []TableParas=dbMsg.XustPostTable;
	private PostAddDiaLog CAd=null;
	private String[]	XustPosts;
	private String	UserType;
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
	
	
	public  void UpdataClassMSG(){
		String sql="",sql2="";
		SqlHelper sqlhelp=SqlHelper.getInstance();
		XustPosts=ReturnUserPosts();
	
		for (int i = 0; i < XustPosts.length; i++) {
			int NUM=0;
			sql="select *from DetailMsg where UserPost="+"'"+XustPosts[i]+"'";
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
			sql2="update XustPost set PersonNum="+"'"+NUM+"'"+" where UserPost="+"'"+XustPosts[i]+"'";
				try {
				sqlhelp.queryExe(sql2);
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally {
				sqlhelp.DBclose();
			}
		}
	}
	public RoomMenu(String UserType){
		this.UserType=UserType;
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

		
		//创建数据模型对象
		  UMM=new UserMsgModel();
		  String sql ="select * from XustPost";
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
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		new RoomMenu("管理员");

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
			CAd=new PostAddDiaLog(this, "新建部门", false);
			
			
		}else if(e.getSource()==Delete){
			//删除
			int rowNum=this.jtb.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this, "请选中一行");
			    return;
			}
			//转换成String 传入进model
			String UserPost=(String)UMM.getValueAt(rowNum, 0);
			
			int	res=JOptionPane.showConfirmDialog(null, 
					"是否打算删除？", "请选择..", JOptionPane.YES_NO_OPTION);
					if(res==JOptionPane.YES_OPTION){
						String sql ="delete from XustPost where UserPost=?";
						String[] paras={UserPost};
						UserMsgModel temp=new UserMsgModel();
						if(temp.EditUser(sql,paras,"delete")){
							JOptionPane.showMessageDialog(this, "删除成功！");
						}else {
							JOptionPane.showMessageDialog(this, "删除失败！");
								
						}
						
						UMM=new UserMsgModel();
						 String sql2 ="select * from XustPost";
						  UMM.queryUser(sql2,this.TableParas,DBTable);
						jtb.setModel(UMM);//获取新的数据模型 
						
					   }
			
			
		}else if(e.getSource()==update){
			/*先刷新班级人数，再刷新列表
			 * */
			UpdataClassMSG();
			
			UMM=new UserMsgModel();
			 String sql3 ="select * from XustPost";
			  UMM.queryUser(sql3,this.TableParas,DBTable);
			jtb.setModel(UMM);//获取新的数据模型 		
		}
		if(e.getSource()==jb1){
			String UserPost =jtf.getText();
			String sql;
			 UMM=new UserMsgModel();
			if(UserPost.equals("")){
				  sql ="select * from XustPost";
				  UMM.queryUser(sql,this.TableParas,DBTable);
				  jtb.setModel(UMM);//获取新的数据模型 
			}else{
			 sql="select * from XustPost where UserPost='"+UserPost+"'";
			 /*
			  * 改善交互，若查找的ID不存在则给出提示。
			  * */
			 SqlHelper sqlhelp=SqlHelper.getInstance();
				String[] UserPosts={UserPost};
				 if(sqlhelp.CheckExist(UserPosts, "XustPostTable")){
					 UMM.queryUser(sql,this.TableParas,DBTable);
					 jtb.setModel(UMM);//获取新的数据模型 
				 }
				 else
					 JOptionPane.showMessageDialog(this, "无该部门记录！");
				 
			}
		
			
		}
		
	}

}

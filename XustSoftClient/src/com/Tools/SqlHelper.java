package com.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

//对数据库进行操作的类
public class SqlHelper {

	//操作数据库所需：
	private PreparedStatement ps=null;
	private Connection ct=null;
	private ResultSet rs=null;
	private String url="jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=XustDB";
	private String DBuser="sa";
	private String DBpass="wei1995922";
	private String driver ="com.microsoft.jdbc.sqlserver.SQLServerDriver";
	
	
	 private static SqlHelper instance=null;
	    public static SqlHelper getInstance(){
	        if(instance==null){
	            synchronized(SqlHelper.class){
	                if(instance==null){
	                    instance=new SqlHelper();
	                }
	            }
	        }
	        return instance;
	    }
	    private SqlHelper(){}
	
	
	//关闭数据库资源
	public void DBclose(){

		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(ct!=null) ct.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	//查询账号为UserID 的用户记录的信息 并返回
		public ResultSet queryExec(String UserID){
			String pass=null;
			String sql="select * from Person where UserID=?";
			try {
				Class.forName(driver);
				ct=DriverManager.getConnection(url,DBuser,DBpass);
				ps=ct.prepareStatement(sql);
				ps.setString(1, UserID);
				
				rs=ps.executeQuery();
		
				
			} catch (Exception e) {
			e.printStackTrace();
			}finally{
				//不在此处关闭  在使用完rs 之后 关闭
			}
			
			return rs;
		}
	
	//查询
	public ResultSet queryExecute(String sql){
		try {
			Class.forName(driver);
			ct=DriverManager.getConnection(url,DBuser,DBpass);
			ps=ct.prepareStatement(sql);
			
			rs=ps.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			/*此处不应该关闭，函数返回结果集给model,若关闭了则无法返回
			 * 应该在model中处理完后 关闭*/
		}
		return rs;
	}
	
	
	
	public void queryExe(String sql){
		try {
			Class.forName(driver);
			ct=DriverManager.getConnection(url,DBuser,DBpass);
			ct.prepareStatement(sql);
			ps=ct.prepareStatement(sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			/*此处不应该关闭，函数返回结果集给model,若关闭了则无法返回
			 * 应该在model中处理完后 关闭*/
		}
		
	}
	
	/*用于检测主键重复性，即该条信息是否已经存在
	 */
	public boolean CheckExist(String[] IDs,String DBTable){
		boolean b=false;
		String sql;
		try {
			Class.forName(driver);
			ct=DriverManager.getConnection(url,DBuser,DBpass);
		
			switch (DBTable) {
			case "DetailTable":
				sql="select * from DetailMsg where UserID="+"'"+IDs[0]+"' or"+" UserNum='"+IDs[1]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
			case "DetailTable_Up":
				//检测是否有该学号的记录情况
				sql="select * from DetailMsg where UserNum="+"'"+IDs[0]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
			case "AbsenceTable":
			
				sql="select * from Absence where UserNum="+"'"+IDs[0]+"'"+"and UserDate="+"'"+IDs[1]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
			case "AbsenceTable_Up":
				//检测是否有该学号的缺勤情况
				sql="select * from Absence where UserNum="+"'"+IDs[0]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
		
			case "XustPostTable":
				sql="select * from XustPost where UserPost="+"'"+IDs[0]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
			case "PersonTable":
				sql="select * from Person where  UserID="+"'"+IDs[0]+"' or"+" UserNum='"+IDs[1]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
		
			default:
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.DBclose();
	}
		return b;
	}
	
	
	//添加、修改
	public boolean EditExec(String sql,String []paras,String DBTable){
		boolean b=true;
		try {
			Class.forName(driver);
			ct=DriverManager.getConnection(url,DBuser,DBpass);
			ps=ct.prepareStatement(sql);
			
			switch (DBTable) {
		
			case "PersonTable":
				for(int i=0;i<paras.length;i++){
					ps.setString(i+1, paras[i]);
				}break;
			
			case "AbsenceTable":
				for(int i=0;i<paras.length;i++){
					ps.setString(i+1, paras[i]);
				}break;

			case "XustPostTable":
				
					ps.setString(1, paras[0]);
					int Num=Integer.parseInt(paras[1]);
					ps.setInt(2, Num);
				break;
		
			case "DetailMsg_Up"://修改信息
				for(int i=0;i<paras.length;i++){
						ps.setString(i+1, paras[i]);
					
				}break;
			case "delete":
				for(int i=0;i<paras.length;i++){
					ps.setString(i+1, paras[i]);
				}break;
			default:
				break;
			}
			
			if(ps.executeUpdate()!=1){
				b=false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
				this.DBclose();
		}
		
		return b;
	}
	
}

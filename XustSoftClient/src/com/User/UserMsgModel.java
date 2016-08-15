package com.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import com.Tools.*;
//UserMsg 表的一个模型
public class UserMsgModel extends AbstractTableModel {

		//rowDate 存放 行数据
		//columnNames  列名
	private DBMsg dbMsg=new DBMsg();
	private Vector rowData, columnNames;
		//操作数据库所需：
	private PreparedStatement ps=null;
	private Connection ct=null;
	private ResultSet rs=null;
	private String url="jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=SchoolDB";
	private String DBuser="sa";
	private String DBpass="wei1995922";
	private String driver ="com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public boolean EditUser(String sql,String []paras,String DBTable){
		SqlHelper sqlhelp =SqlHelper.getInstance();
		return sqlhelp.EditExec(sql, paras,DBTable);
		
	}
			
	//初始化model
	public void queryUser(String sql,String []paras,String DBTable){

		SqlHelper sqlhelp =null;
	
		columnNames =new Vector();
		//设置列名
		for(int i=0;i<paras.length;i++)
			columnNames.add(paras[i]);
		
		try {
			sqlhelp =SqlHelper.getInstance();
		ResultSet	rs=sqlhelp.queryExecute(sql);
			
		rowData=dbMsg.ReturnData(rs, DBTable);
			
		} catch (Exception e) {
		    e.printStackTrace();
		}finally{
			sqlhelp.DBclose();
			
		}
	
		
	}

	
	public 	UserMsgModel(){
	
		
	}
	

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return (String)this.columnNames.get(column);
	}


	@Override//获取共多少行
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rowData.size();
	}

	@Override//获取共多少列
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnNames.size();
	}

	@Override//获取某行某列的数据
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return ((Vector)this.rowData.get(rowIndex)).get(columnIndex);
	}

}

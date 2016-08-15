package com.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import com.Tools.*;
//UserMsg ���һ��ģ��
public class UserMsgModel extends AbstractTableModel {

		//rowDate ��� ������
		//columnNames  ����
	private DBMsg dbMsg=new DBMsg();
	private Vector rowData, columnNames;
		//�������ݿ����裺
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
			
	//��ʼ��model
	public void queryUser(String sql,String []paras,String DBTable){

		SqlHelper sqlhelp =null;
	
		columnNames =new Vector();
		//��������
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


	@Override//��ȡ��������
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rowData.size();
	}

	@Override//��ȡ��������
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnNames.size();
	}

	@Override//��ȡĳ��ĳ�е�����
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return ((Vector)this.rowData.get(rowIndex)).get(columnIndex);
	}

}

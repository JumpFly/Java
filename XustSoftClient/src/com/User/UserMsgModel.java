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
		DBMsg dbMsg=new DBMsg();
		Vector rowData, columnNames;
		//�������ݿ����裺
				PreparedStatement ps=null;
				Connection ct=null;
				ResultSet rs=null;
				String url="jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=SchoolDB";
				String DBuser="sa";
				String DBpass="wei1995922";
				String driver ="com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public boolean EditUser(String sql,String []paras,String DBTable){
		SqlHelper sqlhelp =new SqlHelper();
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
			sqlhelp =new SqlHelper();
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

package com.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

//�����ݿ���в�������
public class SqlHelper {

	//�������ݿ����裺
	PreparedStatement ps=null;
	Connection ct=null;
	ResultSet rs=null;
	String url="jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=XustDB";
	String DBuser="sa";
	String DBpass="wei1995922";
	String driver ="com.microsoft.jdbc.sqlserver.SQLServerDriver";
	
	
	//�ر����ݿ���Դ
	public void DBclose(){

		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(ct!=null) ct.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	//��ѯ�˺�ΪUserID ���û���¼����Ϣ ������
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
				//���ڴ˴��ر�  ��ʹ����rs ֮�� �ر�
			}
			
			return rs;
		}
	
	//��ѯ
	public ResultSet queryExecute(String sql){
		try {
			Class.forName(driver);
			ct=DriverManager.getConnection(url,DBuser,DBpass);
			ps=ct.prepareStatement(sql);
			
			rs=ps.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			/*�˴���Ӧ�ùرգ��������ؽ������model,���ر������޷�����
			 * Ӧ����model�д������ �ر�*/
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
			/*�˴���Ӧ�ùرգ��������ؽ������model,���ر������޷�����
			 * Ӧ����model�д������ �ر�*/
		}
		
	}
	
	/*���ڼ�������ظ��ԣ���������Ϣ�Ƿ��Ѿ�����
	 */
	public boolean CheckExist(String[] IDs,String DBTable){
		boolean b=false;
		String sql;
		try {
			Class.forName(driver);
			ct=DriverManager.getConnection(url,DBuser,DBpass);
		
			switch (DBTable) {
			case "StuTable":
				sql="select * from Student where UserID="+"'"+IDs[0]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;

			case "AbsenceTable":
			
				sql="select * from Absence where UserID="+"'"+IDs[0]+"'"+"and UserDate="+"'"+IDs[1]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
			case "AbsenceTable_Up":
				//����Ƿ��и�ѧ�ŵ�ȱ�����
				sql="select * from Absence where UserID="+"'"+IDs[0]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
			case "FeeTable":
				
				sql="select * from Fee where UserID="+"'"+IDs[0]+"'"+"and Month="+"'"+IDs[1]+"'";
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;

			case "ClassTable":
				sql="select * from Class where ClassID="+IDs[0];
				ps=ct.prepareStatement(sql);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("Exist");
					b=true;
				}
				break;
			case "PersonTable":
				sql="select * from Person where UserID="+IDs[0];
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
	
	
	//��ӡ��޸�
	public boolean EditExec(String sql,String []paras,String DBTable){
		boolean b=true;
		try {
			Class.forName(driver);
			ct=DriverManager.getConnection(url,DBuser,DBpass);
			ps=ct.prepareStatement(sql);
			
			switch (DBTable) {
			
			case "FeeTable_Up":
				for(int i=0;i<paras.length;i++){
					if(i==0)
					ps.setFloat(i+1, Float.parseFloat(paras[i]));
					if(i==1)
						ps.setString(i+1, paras[i]);
					if(i==2)
						ps.setInt(i+1,Integer.parseInt(paras[i]));		
				}break;
			case "FeeTable":
				for(int i=0;i<paras.length;i++){
					if(i==0||i==8)
					ps.setString(i+1, paras[i]);
					if(i==1)
					ps.setInt(i+1,Integer.parseInt(paras[i]));	
					if(i>1&&i<8)
					ps.setFloat(i+1, Float.parseFloat(paras[i]));	
				}break;
				
			case "PersonTable":
				for(int i=0;i<paras.length;i++){
					ps.setString(i+1, paras[i]);
				}break;
			case "PersonTable_Up"://�շ�ʱʹ��
				for(int i=0;i<paras.length;i++){
					if(i==0)
						ps.setFloat(i+1, Float.parseFloat(paras[i]));
					else{
						ps.setString(i+1, paras[i]);
					}
				}break;
			case "AbsenceTable":
				for(int i=0;i<paras.length;i++){
					ps.setString(i+1, paras[i]);
				}break;
			case "ClassTable":
				for(int i=0;i<paras.length;i++){
					ps.setString(i+1, paras[i]);
				}break;
			case "StuTable"://�������Ϣ
				for(int i=0;i<paras.length;i++){
					if(i==3){
						int Age=Integer.parseInt(paras[i]);
						ps.setInt(i+1, Age);
					}else 	{
						ps.setString(i+1, paras[i]);
					}
				}break;
			case "StuTable_update"://�޸���Ϣ,��3λ������ i=2
				for(int i=0;i<paras.length;i++){
					if(i==2){
						int Age=Integer.parseInt(paras[i]);
						ps.setInt(i+1, Age);
					}else 	{
						ps.setString(i+1, paras[i]);
					}
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

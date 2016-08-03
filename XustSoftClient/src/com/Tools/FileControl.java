package com.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FileControl {
	ResultSet rs=null;
	JFileChooser Fch1;
	FileWriter fw=null;
	BufferedWriter bw=null;
	FileReader fr=null;
	BufferedReader br=null;
	String filename;
	SqlHelper sqlhelp=null;
	String[] Msg=null;
	MessagePone mPone=null;
	Vector<String>	MsgData=new Vector();
	public String TransName(String DBTable){
		String Table="";
		switch (DBTable) {
		case "FeeTable":
			Table="Fee";
			break;
		case "AbsenceTable":
			Table="Absence";
			break;
		case "PersonTable":
			Table="Person";
			break;
		case "ClassTable":
			Table="Class";
			break;
		case "StuTable":
			Table="Student";
			break;

		default:
			break;
		}
		return Table;
	}
	public String ReturnSql(String DBTable){
		String sql="";
		switch (DBTable) {
		case "FeeTable":
			sql="insert into Fee values(?,?,?,?,?,?,?,?,?)";
			break;
		case "AbsenceTable":
			sql="insert into Absence values(?,?,?)";
			break;
		case "PersonTable":
			sql="insert into Fee values(?,?,?,?,?,?,?,?,?)";
			break;
		case "ClassTable":
			 sql="insert into Person values(?,?,?,?)";
			break;
		case "StuTable":
			sql="insert into Student values(?,?,?,?,?,?)";
			break;

		default:
			break;
		}
		return sql;
	}
	
	public Vector ReturnData(ResultSet rs,String DBTable){
		MsgData.clear();
		try {
		switch (DBTable) {
		case "StuTable":
			while(rs.next()){
			String msString="";
			msString+=rs.getString(1).trim()+" ";
			msString+=rs.getString(2).trim()+" ";
			msString+=rs.getString(3).trim()+" ";
			msString+=rs.getInt(4)+" ";
			msString+=rs.getString(5).trim()+" ";
			msString+=rs.getString(6).trim();
			MsgData.add(msString);
		}break;
		
		case "ClassTable":
			while(rs.next()){
				String msString="";
				msString+=rs.getString(1).trim()+" ";
				msString+=rs.getString(2).trim()+" ";
				msString+=rs.getInt(3);
				MsgData.add(msString);
			}break;
		case "FeeTable":
			while(rs.next()){
				String msString="";
				msString+=rs.getString(1).trim()+" ";
				msString+=rs.getInt(2)+" ";
				msString+=rs.getFloat(3)+" ";
				msString+=rs.getFloat(4)+" ";
				msString+=rs.getFloat(5)+" ";
				msString+=rs.getFloat(6)+" ";
				msString+=rs.getFloat(7)+" ";
				msString+=rs.getFloat(8)+" ";
				msString+=rs.getString(9).trim();
				MsgData.add(msString);
			}break;
		case "AbsenceTable":
			while(rs.next()){
				String msString="";
				msString+=rs.getString(1).trim()+" ";
				msString+=rs.getString(2).trim()+" ";
				msString+=rs.getString(3).trim();
				MsgData.add(msString);
			}break;
		case "PersonTable":
			while(rs.next()){
				String msString="";
				msString+=rs.getString(1).trim()+" ";
				msString+=rs.getString(2).trim()+" ";
				msString+=rs.getString(3).trim()+" ";
				msString+=rs.getString(4).trim();
				MsgData.add(msString);
			}break;
		default:
			break;
		}
	} catch (Exception e) {
			e.printStackTrace();
  }	
		return MsgData;
	}
	
	
	//导出
	public void SaveToFile(String DBTable){
		
		String sql="select * from "+TransName(DBTable);
		JFileChooser Fch2=new JFileChooser();
		Fch2.setDialogTitle("另存为..");
		//默认显示
		Fch2.showSaveDialog(null);
		Fch2.setVisible(true);
		String filepath=Fch2.getSelectedFile().getAbsolutePath();
		 sqlhelp=new SqlHelper();
		 try {
			 	fw=new FileWriter(filepath);
				bw=new BufferedWriter(fw);
				rs=sqlhelp.queryExecute(sql);
				String RowMsg="";
				Vector<String> SaveFileMsg=ReturnData(rs, DBTable);
				for (int i = 0; i < SaveFileMsg.size(); i++) {
					RowMsg=SaveFileMsg.get(i)+"\r\n";
					bw.write(RowMsg);
				}
				mPone=new MessagePone("导出完成！");
			} catch (Exception e2) {
				e2.printStackTrace();
			}  finally{
				try {
					bw.close();
					fw.close();
					sqlhelp.DBclose();
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		 
	}
	//导入
	public void ReadInFile(String DBTable){
		String sql="";
		Fch1=new JFileChooser();
		Fch1.setDialogTitle("请选择文件...");
		//默认方式
		Fch1.showOpenDialog(null);
		//显示
		Fch1.setVisible(true);
		//得到选中文件的绝对路径
		 filename=Fch1.getSelectedFile().getAbsolutePath();
		 sqlhelp=new SqlHelper();
		 try {
				fr=new FileReader(filename);
				br=new BufferedReader(fr);
				//文件内信息放入JTextArea
				String s="";
				while((s=br.readLine())!=null){
			         Msg=s.split(" ");
			         if(sqlhelp.CheckExist(Msg, DBTable)){
			        	System.out.println("Msg Exist"); 
			         }else{
			        	 sql= ReturnSql(DBTable);
			        	 sqlhelp.EditExec(sql, Msg, DBTable);
			         }
				}
				mPone=new MessagePone("导入完成！请刷新");
			} catch (Exception e2) {
				e2.printStackTrace();
			}  finally{
				try {
					br.close();
					fr.close();
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		 
		 
		 
	}

	private class MessagePone extends JFrame{
		public MessagePone(String message){
			JOptionPane.showMessageDialog(this,message);
			
		}
	}
}

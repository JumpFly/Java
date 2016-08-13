package com.Tools;

import java.sql.ResultSet;
import java.util.Vector;

public class DBMsg {
	Vector	rowData=new Vector();
	public static final String[] DetailMsgTable = {"学号","姓名","性别","专业","邮箱"};
	public static final String[] ClassTable = {"班级号","班级名","人数"};
	public static final String[] FeeTable = {"学号","月份","住宿费","伙食费","书本费","空调费","暖气费","退费","父母职称"};
	public static final String[] AbsenceTable = {"学号","日期","班级号"};
	public static final String[] PersonTable = {"用户ID","密码","用户类型","用户存款"};
	
	public Vector ReturnData(ResultSet rs,String DBTable){
		rowData.clear();
		try {
		switch (DBTable) {
		case "DetailTable":
			while(rs.next()){
			Vector hang=new Vector();
			hang.add(rs.getString(2));
			hang.add(rs.getString(3));
			hang.add(rs.getString(4));
			hang.add(rs.getString(5));
			hang.add(rs.getString(7));
			rowData.add(hang);
		}break;
		
		case "ClassTable":
			while(rs.next()){
				Vector hang=new Vector();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getInt(3));
				rowData.add(hang);
			}break;
		case "FeeTable":
			while(rs.next()){
				Vector hang=new Vector();
				hang.add(rs.getString(1));
				hang.add(rs.getInt(2));
				hang.add(rs.getFloat(3));
				hang.add(rs.getFloat(4));
				hang.add(rs.getFloat(5));
				hang.add(rs.getFloat(6));
				hang.add(rs.getFloat(7));
				hang.add(rs.getFloat(8));
				hang.add(rs.getString(9));
				rowData.add(hang);
			}break;
		case "AbsenceTable":
			while(rs.next()){
				Vector hang=new Vector();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				rowData.add(hang);
			}break;
		case "PersonTable":
			while(rs.next()){
				Vector hang=new Vector();
				hang.add(rs.getString(1));
				hang.add("********");
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));
				rowData.add(hang);
			}break;
		default:
			break;
		}
	} catch (Exception e) {
			e.printStackTrace();
  }	
		return rowData;
	}
	
}

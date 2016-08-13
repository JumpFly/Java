package com.Tools;

import java.sql.ResultSet;
import java.util.Vector;

public class DBMsg {
	Vector	rowData=new Vector();
	public static final String[] DetailMsgTable = {"ѧ��","����","�Ա�","רҵ","����"};
	public static final String[] ClassTable = {"�༶��","�༶��","����"};
	public static final String[] FeeTable = {"ѧ��","�·�","ס�޷�","��ʳ��","�鱾��","�յ���","ů����","�˷�","��ĸְ��"};
	public static final String[] AbsenceTable = {"ѧ��","����","�༶��"};
	public static final String[] PersonTable = {"�û�ID","����","�û�����","�û����"};
	
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

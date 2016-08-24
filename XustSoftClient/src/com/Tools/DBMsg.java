package com.Tools;

import java.sql.ResultSet;
import java.util.Vector;

import org.apache.commons.codec.binary.Hex;

import com.Secret.AESCoder;

public class DBMsg {
	private Vector	rowData=new Vector();
	public static final String[] DetailMsgTable = {"学号","姓名","性别","专业","邮箱"};
	public static final String[] XustPostTable = {"协会部门","人数"};
	
	public static final String[] AbsenceTable = {"学号","日期","备注"};
//	
//	public Vector ReturnData(ResultSet rs,String DBTable){
//		rowData.clear();
//		String stt="";
//		try {
//		switch (DBTable) {
//		case "DetailTable":
//			while(rs.next()){
//			Vector hang=new Vector();
//			hang.add(rs.getString(2));
//			hang.add(rs.getString(3));
//			hang.add(rs.getString(4));
//			hang.add(rs.getString(5));
//			hang.add(rs.getString(7));
//			rowData.add(hang);
//		}break;
//		
//		case "XustPostTable":
//			while(rs.next()){
//				Vector hang=new Vector();
//				hang.add(rs.getString(1));
//				hang.add(rs.getInt(2));
//				rowData.add(hang);
//			}break;
//	
//		case "AbsenceTable":
//			while(rs.next()){
//				Vector hang=new Vector();
//				stt=rs.getString(1);
//				hang.add(stt==null?"":stt.trim());
//				stt=rs.getString(2);
//				hang.add(stt==null?"":stt.trim());
//				stt=rs.getString(3);
//				hang.add(stt==null?"":stt.trim());
//				rowData.add(hang);
//			}break;
//
//		default:
//			break;
//		}
//	} catch (Exception e) {
//			e.printStackTrace();
//  }	
//		return rowData;
//	}
	public static void TransInfo(Vector rowData){
		//用于将server传来的信息对称解密,注意加密后的byte数组是不能强制转换成字符串的,所以用到Hex
		for(Object V:rowData){
			Vector hang=(Vector)V;
			for(int i=0;i<hang.size();i++){
				String stt=(String)hang.get(i);
				byte[] Deinfo;
				try {
					Deinfo=AESCoder.decrypt(Hex.decodeHex(stt.toCharArray()), SecretInfo.getKey());
						String info=new String(Deinfo);
						hang.set(i,info);
		
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
	
		}

	}
}

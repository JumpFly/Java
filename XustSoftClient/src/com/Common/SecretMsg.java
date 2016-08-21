package com.Common;

import java.util.Vector;

public class SecretMsg implements java.io.Serializable{
	private int MsgType;
	private byte[] EnMsg;
	private byte[] Sign;

	private byte[] EnUserID; 
	private byte[] EnPassWord;
	private byte[] EnUserType;

	private byte[] EnUserNum; 
	private byte[] EnUserMail;
	private Vector columnNames; 
	private Vector EnrowData;
	private String FindOne;
	private String FindTwo;

	private String OKorNo;
	private int Menu;
	
	
	public byte[] getEnUserID() {
		return EnUserID;
	}
	public void setEnUserID(byte[] enUserID) {
		EnUserID = enUserID;
	}
	public byte[] getEnPassWord() {
		return EnPassWord;
	}
	public void setEnPassWord(byte[] enPassWord) {
		EnPassWord = enPassWord;
	}
	public byte[] getEnUserType() {
		return EnUserType;
	}
	public void setEnUserType(byte[] enUserType) {
		EnUserType = enUserType;
	}
	public int getMsgType() {
		return MsgType;
	}
	public void setMsgType(int msgType) {
		MsgType = msgType;
	}
	public byte[] getEnMsg() {
		return EnMsg;
	}
	public void setEnMsg(byte[] enMsg) {
		EnMsg = enMsg;
	}

	public byte[] getSign() {
		return Sign;
	}
	public void setSign(byte[] sign) {
		Sign = sign;
	}
	public byte[] getEnUserNum() {
		return EnUserNum;
	}
	public void setEnUserNum(byte[] enUserNum) {
		EnUserNum = enUserNum;
	}
	public byte[] getEnUserMail() {
		return EnUserMail;
	}
	public void setEnUserMail(byte[] enUserMail) {
		EnUserMail = enUserMail;
	}
	public Vector getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(Vector columnNames) {
		this.columnNames = columnNames;
	}
	public Vector getEnrowData() {
		return EnrowData;
	}
	public void setEnrowData(Vector enrowData) {
		EnrowData = enrowData;
	}
	public String getFindOne() {
		return FindOne;
	}
	public void setFindOne(String findOne) {
		FindOne = findOne;
	}

	public String getFindTwo() {
		return FindTwo;
	}
	public void setFindTwo(String findTwo) {
		FindTwo = findTwo;
	}
	public int getMenu() {
	return Menu;
	}
	public void setMenu(int menu) {
	Menu = menu;
	}
	public String getOKorNo() {
		return OKorNo;
	}
	public void setOKorNo(String oKorNo) {
		OKorNo = oKorNo;
	}
}

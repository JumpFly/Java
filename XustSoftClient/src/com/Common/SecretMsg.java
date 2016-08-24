package com.Common;

import java.util.Vector;

public class SecretMsg implements java.io.Serializable{
	private int MsgType;
	private byte[] EnMsg;
	private byte[] Sign;
	private byte[] EnDate;
	private byte[] EnRemark;
	private byte[] EnPost;
	private byte[] EnPostNum;
	private byte[] EnUserID; 
	private byte[] EnPassWord;
	private byte[] EnUserType;
	private byte[] EnUserName; 
	private byte[] EnUserMajor;
	private byte[] EnUserSex;
	private byte[] EnUserMotto;	
	private byte[] EnUserNum; 
	private byte[] EnUserMail;
	private Vector columnNames; 
	private Vector EnrowData;
	private String FindOne;
	private String FindTwo;
	private String OKorNo;
	private int Menu;
	
	private String[] PostName;	
	public String[] getPostName() {
		return PostName;
	}
	public void setPostName(String[] postName) {
		PostName = postName;
	}
	
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

	public byte[] getEnUserName() {
		return EnUserName;
	}
	public void setEnUserName(byte[] enUserName) {
		EnUserName = enUserName;
	}
	public byte[] getEnUserMajor() {
		return EnUserMajor;
	}
	public void setEnUserMajor(byte[] enUserMajor) {
		EnUserMajor = enUserMajor;
	}
	public byte[] getEnUserSex() {
		return EnUserSex;
	}
	public void setEnUserSex(byte[] enUserSex) {
		EnUserSex = enUserSex;
	}
	public byte[] getEnPost() {
		return EnPost;
	}
	public void setEnPost(byte[] enPost) {
		EnPost = enPost;
	}
	public byte[] getEnPostNum() {
		return EnPostNum;
	}
	public void setEnPostNum(byte[] enPostNum) {
		EnPostNum = enPostNum;
	}

	public byte[] getEnDate() {
		return EnDate;
	}
	public void setEnDate(byte[] enDate) {
		EnDate = enDate;
	}

	public byte[] getEnRemark() {
		return EnRemark;
	}
	public void setEnRemark(byte[] enRemark) {
		EnRemark = enRemark;
	}

	public byte[] getEnUserMotto() {
		return EnUserMotto;
	}
	public void setEnUserMotto(byte[] enUserMotto) {
		EnUserMotto = enUserMotto;
	}
}

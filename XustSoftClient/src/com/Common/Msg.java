package com.Common;
import java.io.DataInputStream;
import java.net.DatagramSocket;


public interface Msg {
	public static final int BuildSafe_MSG = 1;
	public static final int Login_MSG = 2;
	public static final int SafeRespond_MSG = 3;
	public static final int LoginRespond_MSG = 4;//Server应答是否通过登录
	
	
	public static final int Logon_MSG = 5;//注册
	public static final int LogonRespond_MSG = 6;//Server应答是否通过注册
	public static final int ChangePassword_MSG = 7;//改密码
	public static final int ChangePassRespond_MSG = 8;//Server应答是否通过改密
	public static final int SendEmail_MSG = 9;//发送邮件验证码
	public static final int SendEmailRespond_MSG = 10;//发送邮件验证码
	public static final int UpDateUserMsg_MSG = 11;//更新User信息
	public static final int FindTheOneMsg_MSG = 12;//界面查找某人信息
	public static final int StuMenuCallTable_MSG = 13;//要求返回表数据
	public static final int PostMenuCallTable_MSG = 14;//要求返回表数据
	public static final int AbsenceMenuCallTable_MSG = 15;//要求返回表数据
	public static final int RespondStu_MSG = 16;//返回表数据
	public static final int RespondPost_MSG = 17;//返回表数据
	public static final int RespondAbsence_MSG = 18;//返回表数据
	public static final int StuMenu=19;	
	public static final int PostMenu=20;	
	public static final int AbsenceMenu=21;	
	public static final int AddMsg=22;	
	public static final int RespondStuAddMsg=23;	
	public static final int RespondPostAddMsg=24;	
	public static final int RespondAbsenceAddMsg=25;		
	public static final int UPUserMsg=26;
	public static final int RespondUPUserMsg=27;
	public static final int AskPostNameMsg=28;
	public static final int ReturnPostNameMsg=29;
	public static final int AskUserDetailMsg=30;
	public static final int ReturnUserDetailMsg=31;
	public static final int DeleteMsg=32;	
	public static final int RespondStuDeleteMsg=33;	
	public static final int RespondPostDeleteMsg=34;	
	public static final int RespondAbsenceDeleteMsg=35;	
	public static final int FileGetOutMsg=36;	
	public static final int RespondStuFileOutMsg=37;	
	public static final int RespondPostFileOutMsg=38;	
	public static final int RespondAbsenceFileOutMsg=39;	
	public static final int ClientQuitMsg=40;	
	public static final int RespondQuitMsg=41;	
	public static final int TransFileMsg=42;	
	public static final int RespondTransFileMsg=43;	
	
	public static final String OKmsg="OK";
	public static final String NOmsg="NO";
	public static final String EXISTmsg="EXIST";

	
	
}

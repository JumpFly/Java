package com.Tools;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class VerifMail {
	 String ToAdress,ToText;
	
	static final String smtpHost="mail.smtp.host";
	static final String FromHost="smtp.sina.cn";
	static final String FromUser="a7ge77@sina.cn";
	static final String FromPass="a7777777";
	static final String ToSubject="XustSoft VerifyMail";
	
	
	String Verify=null;
	Transport transport=null;
	Address address=null,toAddress=null;
	MimeMessage message=null;
	Session session=null;
	public VerifMail(String ToAdress){
		this.ToAdress=ToAdress;
		CreateVerifty();
		CreateText();
	}
	public void CreateVerifty(){
		Verify="";
    	int key;
    	for(int n=0;n<4;n++){
    		 key=(int)(Math.random()*3+1);
    		switch (key) {
    		case 1:
    			char num1=(char)(Math.random()*26+65);
    			Verify+=num1;
    			break;
    		case 2:
    			char num2=(char)(Math.random()*10+48);
    			Verify+=num2;
    			break;
    		case 3:
    			char num3=(char)(Math.random()*26+97);
    			Verify+=num3;
    			break;
    		default:
    			break;
    		}
    	}
    	
	}
	public void CreateText(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca=Calendar.getInstance();//初始化日历
	
		String TextHead="Dear "+ToAdress;
		String TextBody="We come to know that you intend to retrieve your PassWord\n"
				+ "Please Get The Verification Code To Reset";
		String verifty=getVerify();
		String Now=df.format(new Date());
		String root="------------XustSoft";
		String toText=TextHead+"\n"+TextBody+"\n \n"+verifty+"\n"+root+"\n";
		this.setToText(toText);
	}
	 public String getVerify() {
			return Verify;
		}
	 public String getToText() {
			return ToText;
		}
		public void setToText(String toText) {
			ToText = toText;
		}
	public boolean SendMail(){
		boolean flag=false;
		 Properties props = new Properties();   
		 props.put(smtpHost, FromHost);  
		 props.put("mail.smtp.auth", "true");      
		 //基本的邮件会话         
		  session = Session.getDefaultInstance(props);
		 //构造信息体      
		  message = new MimeMessage(session);    
		 //发件地址          
		 
		try {
			 address = new InternetAddress(FromUser);  
			 message.setFrom(address);       
			 //收件地址       
			 toAddress = new InternetAddress(ToAdress);   
			 message.setRecipient(MimeMessage.RecipientType.TO, toAddress);    
			 //主题        
			 message.setSubject(ToSubject);     
			 //正文      
			 message.setText(ToText);    
			 message.saveChanges(); // implicit with send()      
			 //Exception in thread "main" javax.mail.NoSuchProviderException: smtp      
			 session.setDebug(true);      
			 transport = session.getTransport("smtp");      
			 transport.connect(FromHost, FromUser, FromPass);
			 //发送     
			 transport.sendMessage(message, message.getAllRecipients());
			 flag=true;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			} 
		}      
		return flag; 
	}
	public static void main(String[] args)throws Exception {
		
		VerifMail vmMail=new VerifMail("517093454@qq.com");
		boolean flag=vmMail.SendMail();
		System.out.println(flag);
		 
	}

}

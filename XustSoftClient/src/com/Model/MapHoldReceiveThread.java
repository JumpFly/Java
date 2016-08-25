package com.Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


public class MapHoldReceiveThread {
	//仅用来在适当时候返回线程以获取socket通道写入信息
	 private static HashMap  hm=new HashMap<String,ClientReceiveThread>();
	
	private static ObjectOutputStream oos;
	 private static  ObjectInputStream ois;
	 
	//把创好的ClientReceiveThread 放入hm
     public static void addClientReceiveThread(ClientReceiveThread crt)
     {
    	 hm.put("ClientReceiveThread", crt); 
     }
       //获取该线程
     public static ClientReceiveThread getClientConSerThread(){
    	 
    	return (ClientReceiveThread)hm.get("ClientReceiveThread"); 
     }
   
     public static ObjectOutputStream getOos() {
 		return oos;
 	}
 	public static void setOos(ObjectOutputStream oos) {
 		MapHoldReceiveThread.oos = oos;
 	}
 	public static ObjectInputStream getOis() {
 		return ois;
 	}
 	public static void setOis(ObjectInputStream ois) {
 		MapHoldReceiveThread.ois = ois;
 	}
 	public static void RemoveSocket() {
 		hm.remove("ClientReceiveThread");
 	}
     public static boolean IsEmpty(){
    	
     	return hm.isEmpty(); 
      }
     
}

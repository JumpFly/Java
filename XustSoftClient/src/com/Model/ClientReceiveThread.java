package com.Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientReceiveThread extends Thread{
	private Socket mySocket=null;
	volatile boolean stop=true;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public void End(){
		this.stop=false;
	}
	public ClientReceiveThread( Socket clientSocket){
		this.mySocket=clientSocket;
		this.ois=MapHoldReceiveThread.getOis();
		this.oos=MapHoldReceiveThread.getOos();
	}
	public Socket getSocket() {
		return mySocket;
	}
	@Override
	public void run() {
		while(stop){
		//	.。。。。用于接收服务器发来的通知并作出响应
			
		}
		
	}
	
}

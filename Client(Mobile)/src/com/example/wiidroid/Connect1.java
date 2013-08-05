package com.example.wiidroid;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Connect1 extends Thread {
	private String message=null;
	String IP;
	int PORT;
	public Connect1(String message,String IP,int Post){
		this.message=message;
		this.IP=IP;
		this.PORT=Post;
	}
	
public void run(){
	
	Socket socket=null;
	try {
	      socket = new Socket(IP,PORT);
		DataOutputStream out = new DataOutputStream(
				socket.getOutputStream());
		out.writeUTF(message);
		out.flush();
		out.close();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		
		if (socket!=null)
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}

}



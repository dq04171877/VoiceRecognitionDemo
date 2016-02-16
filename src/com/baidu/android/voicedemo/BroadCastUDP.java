package com.baidu.android.voicedemo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.baidu.android.common.logging.Log;

public class BroadCastUDP implements Runnable {

	private String a = "kaishi";
	private DatagramSocket udpSocket;
	private DatagramPacket dataPacket;
	private String LOG_TAG = "BroadcastActivity";
	
	public static int PORT = 3530;

	public void setPort(int port) {
		PORT = port;
	}

	public int getPort() {
		return PORT;
	}

	public void setsend(String str) {
		a = str;
	}

	public void run() {
		synchronized (this) {
			try {
				udpSocket = new DatagramSocket(PORT);
				dataPacket = new DatagramPacket(a.getBytes(),
						a.getBytes().length,
						InetAddress.getByName("255.255.255.255"), PORT);// 202.113.13.13
				// a.getBytes().length,
				udpSocket.send(dataPacket);
				Log.e("BroadCast", a + PORT);
				udpSocket.close();
			} catch (Exception e) {

				Log.e(LOG_TAG, e.toString());
			}
		}
	}

}

package com.ghostswitch;

public class SwitchNative {
	static{
		System.loadLibrary("notification");
	}
	public static native void connectServer(String address, int port_number);
	public static native void closeSock();
	public static native int getPacket();
}


package com.ghostswitch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

public class MyIntentService extends IntentService{
	int programType;
	int actionType;

	String address;
	int port_number;

	// thread
	ClientThread clientThread;
	// bluetooth
	BluetoothAdapter mAdapter;
	private ArrayList<BluetoothDevice> mDevices;
	//SPP's UUID
	private UUID MY_UUID = UUID.fromString("1111111-0000-1000-1111-00AAEECCAAFF");
	// my device name
	private static final String NAME = "BLUETOOTH_ANDROID";


	public MyIntentService(String name) {
		super(name);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u

	}

	public MyIntentService(){
		// Activity��startService(intent);�ŌĂяo�����R���X�g���N�^�͂�����
		super("MyIntentService");

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// �񓯊��������s�����\�b�h�B�^�X�N��onHandleIntent���\�b�h���Ŏ��s����
		Log.d("IntentService","onHandleIntent Start");
		
		programType = intent.getExtras().getInt("programType");
		actionType = intent.getExtras().getInt("actionType");
		// get parameters
		address = intent.getExtras().getString("address");
		port_number = intent.getExtras().getInt("port");

		mDevices = new ArrayList<BluetoothDevice>();
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> devices = mAdapter.getBondedDevices();

		// �y�A�����O�ς݃f�o�C�X�̃��X�g
		for (BluetoothDevice device : devices) {
			mDevices.add(device);
			// Toast�ŕ\������
			Toast.makeText(this, "Name:" + device.getName(), Toast.LENGTH_LONG).show();
		}

	
		Log.v("############",address + ", " + port_number);

		if(actionType == 0){
			SwitchNative.connectServer(address, port_number);

			int temp=1;
			temp = SwitchNative.getPacket();
			Log.v("MyIntent", "temp: " + temp);
		}
		else if(actionType == 1){
			SwitchNative.connectServer(address, port_number);

			int temp=1;
			temp = SwitchNative.getPacket();
			
			if (mDevices != null) {
				for (int i = 0; i < mDevices.size(); i++) {
					clientThread = new ClientThread(mDevices.get(i));
					clientThread.start();
				}
			}

		}
		else if(actionType == 2){

			BluetoothServerSocket mmServerSocket = null;
			BluetoothSocket socket = null;

			try {
				// MY_UUID��SPP��UUID���w��
				mmServerSocket = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
			}
			try {
				while(true){
					socket = mmServerSocket.accept();
					if(socket != null)
						break;
				}
			} catch (Exception e) {
			}
			Log.v("Server", "Connected!");

		}
		

		WakeLock wakelock;
		KeyguardLock keylock;


		//�X���[�v��Ԃ��畜�A����
		wakelock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.FULL_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.ON_AFTER_RELEASE, "disableLock");
		wakelock.acquire();

		//�X�N���[�����b�N����������
		KeyguardManager keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		keylock = keyguard.newKeyguardLock("disableLock");
		keylock.disableKeyguard();

		if(actionType != 2){
			try {
				Thread.sleep(2000);
			}catch(InterruptedException e){
			}
		}

		switch(programType){
		case 0:
			// �p�b�P�[�W��, �N���X�����Z�b�g
			intent.setClassName("com.netim", "com.netim.MainActivity");
			// Activity�ȊO����Activity���ĂԂ��߂̃t���O
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// �A�v�����N��
			startActivity(intent);
			break;
		case 1:
			// �p�b�P�[�W��, �N���X�����Z�b�g
			intent.setClassName("com.andrive", "com.andrive.StartActivity");
			// Activity�ȊO����Activity���ĂԂ��߂̃t���O
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			// �A�v�����N��
			startActivity(intent);
			break;
		case 2:
			// �p�b�P�[�W��, �N���X�����Z�b�g
			intent.setClassName("org.prowl.torque", "org.prowl.torque.landing.FrontPage");
			// Activity�ȊO����Activity���ĂԂ��߂̃t���O
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// �A�v�����N��
			startActivity(intent);
			break;
		case 3:
			// �p�b�P�[�W��, �N���X�����Z�b�g
			intent.setClassName("jp.co.incrementp.mapfan2013", "jp.co.incrementp.mapfan2013.control.download.MFDownloadActivity");
			// Activity�ȊO����Activity���ĂԂ��߂̃t���O
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			// �A�v�����N��
			startActivity(intent);
			break;
		case 4:
			// �p�b�P�[�W��, �N���X�����Z�b�g
			intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
			// Activity�ȊO����Activity���ĂԂ��߂̃t���O
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			// �A�v�����N��
			startActivity(intent);
			break;

		}


		//		// �X���[�v�\��Ԃ֖߂�
		//		if (wakelock.isHeld()) {
		//			wakelock.release();
		//		}
		//		keylock.reenableKeyguard();
	}


	/**
	 * Client�p��Thread
	 */
	private class ClientThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ClientThread(BluetoothDevice device) {

			BluetoothSocket tmp = null;
			mmDevice = device;

			try {
				// SPP��UUID���w��
				// ���̏����ɂ� android.permission.BLUETOOTH_ADMIN �̃p�[�~�b�V�������K�v
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (Exception e) {
			}
			mmSocket = tmp;
		}

		public void run() {

			// Discovery���[�h���I������
			mAdapter.cancelDiscovery();

			try {
				// �T�[�o�ɐڑ�
				mmSocket.connect();
			} catch (IOException connectException) {

				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// �ڑ������ƌĂяo�����
			//manageConnectedSocket(mmSocket);
		}

		/**
		 * �ڑ����I������ۂɌĂ΂��
		 */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}
}
package com.ghostswitch;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class StandbyActivity extends Activity {
	private static final int REQUEST_ENABLE_BLUETOOTH = 1;

	int programType;
	int actionType;
	//address and port
	String address;
	String port;
	
	BluetoothAdapter Bt;

	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.standby); 
		Log.v("Log", "StandbyActivityg");
		programType = getIntent().getExtras().getInt("programType");
		actionType = getIntent().getExtras().getInt("actionType");
		// get parameters
		address = getIntent().getExtras().getString("address");
		port = getIntent().getExtras().getString("port");

		if(actionType == 1 || actionType == 2){
			//BluetoothAdapter�擾
			Bt = BluetoothAdapter.getDefaultAdapter();
			if(!Bt.equals(null)){
				//Bluetooth�Ή��[���̏ꍇ�̏���
				Toast.makeText(this, "Bluetooth is supported.", Toast.LENGTH_LONG).show();
			}else{
				//Bluetooth��Ή��[���̏ꍇ�̏���
				Toast.makeText(this, "Bluetooth is unsupported", Toast.LENGTH_LONG).show();
				finish();
			}

			boolean btEnable = Bt.isEnabled();
			if(btEnable == true){
				//Bluetooth��ON�������ꍇ�̏���
			}else{
				//OFF�������ꍇ�AON�ɂ��邱�Ƃ𑣂��_�C�A���O��\�������ʂɑJ��
				Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
			}
		}

		//MyIntentService���N������
		Intent intent = new Intent(this, MyIntentService.class);
		intent.putExtra("programType", programType);
		intent.putExtra("actionType", actionType);
		intent.putExtra("address", address);
		intent.putExtra("port", Integer.parseInt(port));
		this.startService(intent);
	}  


	@Override
	protected void onActivityResult(int requestCode, int ResultCode, Intent date){
		//�_�C�A���O��ʂ��猋�ʂ��󂯂���̏������L�q
		if(requestCode == REQUEST_ENABLE_BLUETOOTH){
			if(ResultCode == Activity.RESULT_OK){
				//Bluetooth��ON�ɂ��ꂽ�ꍇ�̏���
				Toast.makeText(this, "Bluetooth On.", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this, "Bluetooth Off.", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}

	@Override  
	public void onStart() {  
		super.onStart();  
	}  

	@Override  
	public void onResume() {  
		super.onResume();  
	}  

	@Override  
	public void onPause() {  
		super.onPause();  
	}  

	@Override  
	public void onStop() {  
		super.onStop();  
	}  

	@Override  
	public void onDestroy() {  
		super.onDestroy();  
	}  

}  
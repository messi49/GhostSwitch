package com.ghostswitch;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener{
	int programType;
	int actionType;

	BufferedInputStream istream;
	String path = "/mnt/sdcard/ghost.txt";
	String text;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* select start activity */
		ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// add items
		type_adapter.add("GhostEye");
		type_adapter.add("Andrive");
		type_adapter.add("Torque");
		type_adapter.add("Map");
		type_adapter.add("Browser");
		Spinner type_spinner = (Spinner) findViewById(R.id.startActivity);
		// set adapter
		type_spinner.setAdapter(type_adapter);
		// set listener
		type_spinner.setOnItemSelectedListener(this);

		/* select action pattern */
		ArrayAdapter<String> actionPattern = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		actionPattern.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// add items
		actionPattern.add("---");
		actionPattern.add("Client");
		actionPattern.add("Server");
		Spinner action_spinner = (Spinner) findViewById(R.id.action_pattern);
		// set adapter
		action_spinner.setAdapter(actionPattern);
		// set listener
		action_spinner.setOnItemSelectedListener(this);

		/* start */
		Button btn = (Button)findViewById(R.id.start);
		btn.setOnClickListener(this);

		try {
			istream = new BufferedInputStream(new FileInputStream(path));//入力ストリーム取得
			byte[] buffer = new byte[256]; 
			istream.read(buffer); //読み込み
			text = new String(buffer).trim(); //余分なデータを消去
			istream.close(); //ストリームを閉じる
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(text != null){
			String[] strAry = text.split(",");
			Log.v("log", "" + strAry.length);
			if(strAry.length > 1){
				if(!strAry[0].equals("null") && !strAry[1].equals("null")){
					// pass intent to GhostEyeActivity
					intent = new Intent(getApplication(), StandbyActivity.class);

					// address and port
					intent.putExtra("address", strAry[0]);
					intent.putExtra("port", strAry[1]);

				}
			}
		}



		//		//MyIntentServiceを起動する
		//		Intent intent = new Intent(this, MyIntentService.class);
		//		this.startService(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Spinner spinner = (Spinner) arg0;
		// get selected item
		String item = (String) spinner.getSelectedItem();
		Toast.makeText(this, item, Toast.LENGTH_LONG).show();

		if(item == "GhostEye"){
			Log.v("Itme", "GhostEye");
			programType = 0;
		}
		else if(item == "Andrive"){
			Log.v("Itme", "Andrive");
			programType = 1;
		}
		else if(item == "Torque"){
			Log.v("Itme", "Torque");
			programType = 2;
		}
		else if(item == "Map"){
			Log.v("Itme", "Map");
			programType = 3;
		}
		else if(item == "Browser"){
			Log.v("Itme", "Browser");
			programType = 4;
		}
		else if(item == "---"){
			Log.v("Itme", "---");
			actionType = 0;
		}
		else if(item == "Client"){
			Log.v("Itme", "Client");
			actionType = 1;
		}
		else if(item == "Server"){
			Log.v("Itme", "Server");
			actionType = 2;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onClick(View v) {
		Toast.makeText(this, "Start Push!", Toast.LENGTH_LONG).show();
		//StandbyActivityを起動する
		intent.putExtra("programType", programType);
		intent.putExtra("actionType", actionType);
//		intent.putExtra("address", "172.25.15.57");
//		intent.putExtra("port", "12344");
		this.startActivity(intent);
	}
}

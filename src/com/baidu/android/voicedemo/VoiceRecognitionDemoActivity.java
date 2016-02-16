package com.baidu.android.voicedemo;

import com.baidu.android.common.logging.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VoiceRecognitionDemoActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo);

	};

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	public void onClick1(View v) {
		Intent i = new Intent(this, ApiDemoActivity.class);
		Log.e("onClick", "arg1");
		startActivity(i);
		
	}

	public void onClick2(View v) {
		Intent ciku = new Intent(this, SettingActivity.class);
		
		startActivity(ciku);
	}

	public void onClick3(View v) {
		Intent ciku = new Intent(this, FileListActivity.class);
		Log.e("onClick", "arg1");
		startActivity(ciku);
	}
	public void onClick4(View v) {
		Intent ciku = new Intent(this, DanmuActivity.class);
		Log.e("onClick4", "arg1");
		startActivity(ciku);
	}

}

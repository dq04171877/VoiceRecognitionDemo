package com.baidu.android.voicedemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DanmuActivity extends Activity {
	private EditText mDanmuText;
	private Button mDanmuButtonSend;
	private EditText mDanmuTextPort;
	private Button mDanmuButtonPort;
	private Button mDanmuButtonClear;
	private String str;
	private FileText mFile = new FileText();
	private int port = 0;
	private BroadCastUDP udp = new BroadCastUDP();
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonDanmuSend:
				if (str == null) {
					Toast.makeText(getApplicationContext(), "请输入弹幕内容",
							Toast.LENGTH_SHORT).show();
				} else {

					udp.setsend("2" + str);
					Thread send = new Thread(udp);
					send.start();
					Toast.makeText(getApplicationContext(), "已成功发送",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.buttonDanmuPort:
				Toast.makeText(getApplicationContext(), "端口为" + udp.getPort(),
						Toast.LENGTH_SHORT).show();
				if (port != 0) {
					udp.setPort(port);
					Toast.makeText(getApplicationContext(), "端口为" + udp.getPort(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.buttonDanmuClear:
				str = null;
				mDanmuText.setText(null);
			default:
				break;
			}

		}
	};

	// public onClickPort(View v){if(port!=null)}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_danmu);
		FileLab.get(this).addFile(mFile);
		mFile.setmDate();
		mDanmuText = (EditText) findViewById(R.id.textDanmu);
		mDanmuButtonSend = (Button) findViewById(R.id.buttonDanmuSend);
		mDanmuButtonSend.setOnClickListener(mClickListener);
		mDanmuButtonPort = (Button) findViewById(R.id.buttonDanmuPort);
		mDanmuButtonPort.setOnClickListener(mClickListener);
		mDanmuButtonClear= (Button) findViewById(R.id.buttonDanmuClear);
		mDanmuButtonClear.setOnClickListener(mClickListener);
		mDanmuTextPort = (EditText) findViewById(R.id.textDanmuPort);
		mDanmuTextPort.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				/*
				 * String pat = "[0-9]+"; Pattern pattern =
				 * Pattern.compile(pat); Matcher m = pattern.matcher(arg0);
				 * boolean flag = m.matches(); if (flag) { port =
				 * Integer.parseInt(arg0.toString()); if (port < 65535 && port >
				 * 1024) { udp.setPort(port);
				 * Toast.makeText(getApplicationContext(), "绔彛宸叉敼涓�" + port,
				 * Toast.LENGTH_SHORT).show(); } else {
				 * Toast.makeText(getApplicationContext(), "璇疯緭鍏ユ暟瀛楁垨浣跨敤榛樿绔彛",
				 * Toast.LENGTH_SHORT) .show(); } }
				 */
				try {
					port = Integer.parseInt(arg0.toString());
				} catch (Exception e) {
					Log.e("port", "1");
				}
			}
		});
		mDanmuText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				str = arg0.toString();
				mFile.setmEditText(arg0.toString());
			}
		});
	}
}

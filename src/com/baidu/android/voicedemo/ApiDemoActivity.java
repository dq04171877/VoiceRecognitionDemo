package com.baidu.android.voicedemo;

import com.baidu.android.common.logging.Log;
import com.baidu.voicerecognition.android.Candidate;
import com.baidu.voicerecognition.android.DataUploader;
import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.baidu.voicerecognition.android.VoiceRecognitionClient.VoiceClientStatusChangeListener;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ApiDemoActivity extends FragmentActivity {
	private ControlPanelFragment mControlPanel;

	private VoiceRecognitionClient mASREngine;

	/** 正在识别中 */
	private boolean isRecognition = false;

	/** 音量更新间隔 */
	private static final int POWER_UPDATE_INTERVAL = 100;

	/** 识别回调接口 */
	private MyVoiceRecogListener mListener = new MyVoiceRecogListener();

	/** 主线程Handler */
	private Handler mHandler;
	private BroadCastUDP sendBroadCastUDP = new BroadCastUDP();
	private EditText mResult = null;
	private EditText mTitleText = null;
	private String mTitle = null;
	private Button mSave;
	private String str = " ";
	private FileText mFile;
	private int nString = 0;
	private int port = 0;
	private Button mDanmuButtonPort;
	private EditText mDanmuTextPort;
	/**
	 * 音量更新任务
	 */
	private Runnable mUpdateVolume = new Runnable() {
		public void run() {
			if (isRecognition) {
				long vol = mASREngine.getCurrentDBLevelMeter();
				mControlPanel.volumeChange((int) vol);
				mHandler.removeCallbacks(mUpdateVolume);
				mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
			}
		}
	};

	private OnClickListener mClickListener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonapisave:
				mFile.setmEditText(str);
				if (mTitle != null) {
					mFile.setmTitle(mTitle);
				}
				Toast.makeText(getApplicationContext(), "已保存",
						Toast.LENGTH_SHORT).show();
				Log.e("onClick", str);
				break;
			case R.id.buttonapiport:
				Toast.makeText(getApplicationContext(),
						"端口为" + sendBroadCastUDP.getPort(), Toast.LENGTH_SHORT)
						.show();
				if (port != 0) {
					sendBroadCastUDP.setPort(port);
					Toast.makeText(getApplicationContext(),
							"端口为" + sendBroadCastUDP.getPort(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	};

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.api_demo_activity);

		mTitleText = (EditText) findViewById(R.id.api_titletext);

		mSave = (Button) findViewById(R.id.buttonapisave);
		mSave.setOnClickListener(mClickListener);

		mDanmuButtonPort = (Button) findViewById(R.id.buttonapiport);
		mDanmuButtonPort.setOnClickListener(mClickListener);

		mDanmuTextPort = (EditText) findViewById(R.id.apiport);
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
				try {
					port = Integer.parseInt(arg0.toString());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "请输入1000-65536的整数",
							Toast.LENGTH_SHORT).show();
					Log.e("port", "1");
				}

			}
		});

		mASREngine = VoiceRecognitionClient.getInstance(this);
		mASREngine.setTokenApis(Constants.API_KEY, Constants.SECRET_KEY);

		uploadContacts();

		mFile = new FileText();

		FileLab.get(this).addFile(mFile);

		mFile.setmDate();

		mResult = (EditText) findViewById(R.id.recognition_text);
		mHandler = new Handler();
		Toast.makeText(getApplicationContext(), "新文件已建立", Toast.LENGTH_SHORT)
				.show();
		mControlPanel = (ControlPanelFragment) (getSupportFragmentManager()
				.findFragmentById(R.id.control_panel));
		mControlPanel
				.setOnEventListener(new ControlPanelFragment.OnEventListener() {

					@Override
					public boolean onStopListening() {
						mASREngine.speakFinish();
						return true;
					}

					@Override
					public boolean onStartListening() {
						mResult.setText(null);
						VoiceRecognitionConfig config = new VoiceRecognitionConfig();
						config.setProp(Config.CURRENT_PROP);
						config.setLanguage(Config.getCurrentLanguage());
						config.enableContacts(); // 启用通讯录
						config.enableVoicePower(Config.SHOW_VOL); // 音量反馈。
						if (Config.PLAY_START_SOUND) {
							config.enableBeginSoundEffect(R.raw.bdspeech_recognition_start); // 设置识别开始提示音
						}
						if (Config.PLAY_END_SOUND) {
							config.enableEndSoundEffect(R.raw.bdspeech_speech_end); // 设置识别结束提示音
						}
						config.setSampleRate(VoiceRecognitionConfig.SAMPLE_RATE_8K); // 设置采样率,需要与外部音频一致
						// 下面发起识别
						int code = mASREngine.startVoiceRecognition(mListener,
								config);
						if (code != VoiceRecognitionClient.START_WORK_RESULT_WORKING) {
							mResult.setText(getString(R.string.error_start,
									code));
						}

						return code == VoiceRecognitionClient.START_WORK_RESULT_WORKING;
					}

					@Override
					public boolean onCancel() {
						mASREngine.stopVoiceRecognition();
						return true;
					}
				});
		mTitleText.addTextChangedListener(new TextWatcher() {

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
			public void onTextChanged(CharSequence title, int arg1, int arg2,
					int arg3) {
				mTitle = title.toString();

			}
		});

	}

	/**
	 * 重写用于处理语音识别回调的监听器
	 */
	class MyVoiceRecogListener implements VoiceClientStatusChangeListener {
		@Override
		public void onClientStatusChange(int status, Object obj) {
			switch (status) {
			// 语音识别实际开始，这是真正开始识别的时间点，需在界面提示用户说话。
			case VoiceRecognitionClient.CLIENT_STATUS_START_RECORDING:
				isRecognition = true;
				mHandler.removeCallbacks(mUpdateVolume);
				mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
				mControlPanel
						.statusChange(ControlPanelFragment.STATUS_RECORDING_START);
				Log.e("MyVoiceRecogListener", "STATUS_START_RECORDING");
				break;
			case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_START: // 检测到语音起点
				mControlPanel
						.statusChange(ControlPanelFragment.STATUS_SPEECH_START);
				Log.e("MyVoiceRecogListener", "STATUS_SPEECH_START");
				break;
			// 已经检测到语音终点，等待网络返回
			case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_END:
				mControlPanel
						.statusChange(ControlPanelFragment.STATUS_SPEECH_END);
				Thread sendend = new Thread(sendBroadCastUDP);
				sendend.start();
				Log.e("MyVoiceRecogListener", "SPEECH_END");
				break;
			// 语音识别完成，显示obj中的结果
			case VoiceRecognitionClient.CLIENT_STATUS_FINISH:
				mControlPanel.statusChange(ControlPanelFragment.STATUS_FINISH);
				isRecognition = false;
				updateRecognitionResult(obj);
				// mResult.append(str);
				Log.e("MyVoiceRecogListener", "STATUS_FINISH");
				break;
			// 处理连续上屏
			case VoiceRecognitionClient.CLIENT_STATUS_UPDATE_RESULTS:
				updateRecognitionResult(obj);
				sendBroadCastUDP.setsend("1" + str);
				Thread send = new Thread(sendBroadCastUDP);
				send.start();
				Log.e("MyVoiceRecogListener", " UPDATE_RESULTS");
				break;
			// 用户取消
			case VoiceRecognitionClient.CLIENT_STATUS_USER_CANCELED:
				mControlPanel.statusChange(ControlPanelFragment.STATUS_FINISH);
				isRecognition = false;
				Log.e("MyVoiceRecogListener", "STATUS_USER_CANCELED");
				break;
			default:
				break;
			}

		}

		@Override
		public void onError(int errorType, int errorCode) {
			isRecognition = false;
			mResult.setText(getString(R.string.error_occur,
					Integer.toHexString(errorCode)));
			mControlPanel.statusChange(ControlPanelFragment.STATUS_FINISH);
		}

		@Override
		public void onNetworkStatusChange(int status, Object obj) {
			// 这里不做任何操作不影响简单识别
		}
	}

	/**
	 * 将识别结果更新到UI上，搜索模式结果类型为List<String>,输入模式结果类型为List<List<Candidate>>
	 * 
	 * @param result
	 */
	private void updateRecognitionResult(Object result) {
		if (result != null && result instanceof List) {
			List results = (List) result;
			if (results.size() > 0) {
				if (results.get(0) instanceof List) {
					List<List<Candidate>> sentences = (List<List<Candidate>>) result;
					StringBuffer sb = new StringBuffer();
					for (List<Candidate> candidates : sentences) {
						if (candidates != null && candidates.size() > 0) {
							sb.append(candidates.get(0).getWord());
						}
					}
					str = sb.toString();
					mResult.setText(str);

				} else {
					str = results.get(0).toString();
					mResult.setText(str);
					// file.FileWrite(str);

				}
			}
		}
	}

	/**
	 * 上传通讯录
	 * */
	private void uploadContacts() {
		DataUploader dataUploader = new DataUploader(ApiDemoActivity.this);
		dataUploader.setApiKey(Constants.API_KEY, Constants.SECRET_KEY);

		String jsonString = "[{\"name\":\"兆维\", \"frequency\":1}, {\"name\":\"林新汝\", \"frequency\":2}]";
		try {
			dataUploader.uploadContactsData(jsonString.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

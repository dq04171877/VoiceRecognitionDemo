package com.baidu.android.voicedemo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.common.logging.Log;

public class FileText {
	private UUID mId;
	private String mTitle = "无标题";
	private String mDate = null;
	private String mEditText = "文件为空";
	

	public String getmEditText() {
			Log.e("FileText", "getmFileRecotrue"+mEditText);
			return mEditText;
	}

	public void setmEditText(String Text) {	
		this.mEditText = Text;
		Log.e("setmEditText", Text+mEditText);
	}

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DATE = "date";
	private static final String JSON_EditText = "EditText";

	public FileText() {
		mId = UUID.randomUUID();
		setmDate();
	}

	public FileText(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
			mDate = json.getString(JSON_DATE);
			mEditText = json.getString(JSON_EditText);
		/*	for (int n = 0; n <= nString; n++) {
				mFileReco += json
						.getString(JSON_EditText + Integer.toString(n));
			}*/
			Log.e(mEditText, "FileTextjson");

		}
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_DATE, mDate);	
		json.put(JSON_EditText, mEditText);
		
		Log.e("JSON_EditText", mEditText);
		/*for (int n = 0; n <= nString; n++) {
			if (mEditText[n] != null) {
				json.put(JSON_EditText + Integer.toString(n), mEditText[n]);
				Log.e(JSON_EditText + Integer.toString(n), mId.toString());
				Log.e("tojson", mEditText[n].toString());
			} else {
				json.put(JSON_EditText, "error");
				Log.e("tojson", "2");
			}
		}*/
		return json;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate() {
		GregorianCalendar now = new GregorianCalendar();
		mDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1)
				+ "-" + now.get(Calendar.DAY_OF_MONTH) + " "
				+ now.get(Calendar.HOUR_OF_DAY) + ":"
				+ now.get(Calendar.MINUTE);
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public UUID getmId() {
		return mId;
	}

}

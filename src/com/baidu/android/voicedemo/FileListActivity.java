package com.baidu.android.voicedemo;

import android.support.v4.app.Fragment;
import android.util.Log;

public class FileListActivity extends SingleFragmentActivity {

	protected Fragment createFragment() {
		Log.e("filelistactivity", "fragment");
		return new FileListFragment();//
	}

}

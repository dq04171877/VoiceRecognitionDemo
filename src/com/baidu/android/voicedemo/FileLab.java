package com.baidu.android.voicedemo;

import java.util.ArrayList;
import java.util.UUID;

import com.baidu.android.common.logging.Log;

import android.content.Context;

public class FileLab {
	private static FileLab sFileLab;//
	private Context mAppContext;//
	private static final String TAG = "FileLab";
	private static final String FILENAME = "files.json";
	private ArrayList<FileText> mFiles;//
	private FileIntentJSONSerializer mSerializer;

	private FileLab(Context appContext) {//
		mAppContext = appContext;
		mSerializer = new FileIntentJSONSerializer(mAppContext, FILENAME);
		try{
			mFiles=mSerializer.loadFiles();
		}catch(Exception e){
			mFiles=new ArrayList<FileText>();
			Log.e(TAG,"Error loading crimes:",e);
		}
	}

	public void addFile(FileText c) {
		mFiles.add(c);
	}
	public void deleteFile(FileText c){
		mFiles.remove(c);
	}

	public boolean saveFiles() {
		try {
			mSerializer.saveFiles(mFiles);
			Log.d(TAG, "files saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving files", e);
			return false;
		}
	}

	public static FileLab get(Context c) {//
		if (sFileLab == null) {
			sFileLab = new FileLab(c.getApplicationContext());

		}
		return sFileLab;
	}

	public ArrayList<FileText> getFiles() {
		return mFiles;
	}

	public FileText getFile(UUID id) {
		for (FileText c : mFiles) {
			if (c.getmId().equals(id)) {
				return c;
			}
		}
		return null;
	}

}

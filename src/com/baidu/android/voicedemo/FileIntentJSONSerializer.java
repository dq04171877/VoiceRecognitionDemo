package com.baidu.android.voicedemo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class FileIntentJSONSerializer {
	private Context mContext;
	private String mFilename;

	public FileIntentJSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
	}

	public ArrayList<FileText> loadFiles() throws IOException, JSONException {
		ArrayList<FileText> files = new ArrayList<FileText>();
		BufferedReader reader = null;
		try {
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
					.nextValue();
			for (int i = 0; i < array.length(); i++) {
				files.add(new FileText(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {

		} finally {
			if (reader != null)
				reader.close();
		}
		return files;
	}

	public void saveFiles(ArrayList<FileText> files) throws JSONException,
			IOException {
		JSONArray array = new JSONArray();
		for (FileText c : files) {
			array.put(c.toJSON());
			Writer writer = null;
			try {
				OutputStream out = mContext.openFileOutput(mFilename,
						Context.MODE_PRIVATE);
				writer = new OutputStreamWriter(out);
				writer.write(array.toString());
			} finally {
				if (writer != null)
					writer.close();
			}
		}
	}
}

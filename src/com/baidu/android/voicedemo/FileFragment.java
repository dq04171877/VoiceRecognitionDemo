package com.baidu.android.voicedemo;

import java.util.UUID;

import com.baidu.android.common.logging.Log;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FileFragment extends Fragment {

	private FileText mFile;//
	private EditText mTitleField;
	private EditText mDateEditText;
	private Button mShareButton = null;
	private Button mSaveButton = null;
	private EditText mEditText;
	private OnClickListener mClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.file_share:
				if (mFile.getmEditText() != null) {
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("text/plain");
					i.putExtra(Intent.EXTRA_TEXT, mFile.getmEditText());
					startActivity(i);
				} else {
					Toast.makeText(getActivity(), "无内容可发送", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.file_save:
				break;
			}
		}
	};

	public static final String EXTRA_FILE_ID = "com.baidu.android.voicedemo.file_id";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		UUID fileId = (UUID) getArguments().getSerializable(EXTRA_FILE_ID);//
		mFile = FileLab.get(getActivity()).getFile(fileId);//
		Log.e("FileFragment", "oncreate");
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstancestate) {
		View v = inflater.inflate(R.layout.fragment_file, parent, false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		mTitleField = (EditText) v.findViewById(R.id.file_title);
		mTitleField.setText(mFile.getmTitle());

		mDateEditText = (EditText) v.findViewById(R.id.file_date);
		mDateEditText.setText(mFile.getmDate());

		mShareButton = (Button) v.findViewById(R.id.file_share);
		mShareButton.setEnabled(true);
		mShareButton.setOnClickListener(mClickListener);

		mSaveButton = (Button) v.findViewById(R.id.file_save);
		mSaveButton.setEnabled(true);
		mSaveButton.setOnClickListener(mClickListener);

		mEditText = (EditText) v.findViewById(R.id.file_recognition);
		mEditText.setText(mFile.getmEditText());

		mTitleField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				mFile.setmTitle(arg0.toString());
			}

		});

		Log.e("FileActivity", "oncreateView");
		return v;

	}

	public static FileFragment newInstance(UUID fileId) {//
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_FILE_ID, fileId);
		FileFragment fragment = new FileFragment();
		fragment.setArguments(args);
		Log.e("FileActivity", "FileFragment");
		return fragment;

	}

	public void onPause() {
		super.onPause();
		FileLab.get(getActivity()).saveFiles();
	}
}

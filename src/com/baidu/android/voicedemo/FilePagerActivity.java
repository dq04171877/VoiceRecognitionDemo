package com.baidu.android.voicedemo;

import java.util.ArrayList;
import java.util.UUID;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class FilePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;//
	private ArrayList<FileText> mFiles;//

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//
		mViewPager = new ViewPager(this);//
		mViewPager.setId(R.id.viewPager);//
		setContentView(mViewPager);//
		mFiles = FileLab.get(this).getFiles();//
		FragmentManager fm = getSupportFragmentManager();//
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			public int getCount() {
				return mFiles.size();
			}

			@Override
			public Fragment getItem(int pos) {
				FileText file = mFiles.get(pos);
				return FileFragment.newInstance(file.getmId());
			}
		});
		UUID fileId = (UUID) getIntent().getSerializableExtra(//
				FileFragment.EXTRA_FILE_ID);
		for (int i = 0; i < mFiles.size(); i++) {
			if (mFiles.get(i).getmId().equals(fileId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int pos) {
						FileText file = mFiles.get(pos);
						if (file.getmTitle() != null) {
							setTitle(file.getmTitle());
						}

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
	}
}

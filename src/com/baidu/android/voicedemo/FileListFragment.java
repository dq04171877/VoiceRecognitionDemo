package com.baidu.android.voicedemo;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileListFragment extends ListFragment {
	private ArrayList<FileText> mFiles;//
	private boolean mSubtitleVisible;
	private static final String TAG="CrimeListFragment";
	

	public void onCreate(Bundle savedInstanceState) {
		Log.e("FileListFragment", "onCreate");
		super.onCreate(savedInstanceState);//
		getActivity().setTitle(R.string.files_title_list);//
		setHasOptionsMenu(true);//
		
		
		mFiles = FileLab.get(getActivity()).getFiles();
		FileAdapter adapter = new FileAdapter(mFiles);
		setListAdapter(adapter);
		setRetainInstance(true);
		mSubtitleVisible = false;
		Log.e("FileListFragment", " ");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("FileListFragment", "onActivityResult");
		((FileAdapter) getListAdapter()).notifyDataSetChanged();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {//
		Log.e("FileListFragment", "onCreateOptionsMenu ");
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_file_list, menu);
		MenuItem showSubtitle=menu.findItem(R.id.menu_item_show_subtitle);
		if(mSubtitleVisible&&showSubtitle!=null){
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
		Log.e("FileListFragment", "onCreateOptionsMenu1 ");
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("FileListFragment", " onOptionsItemSelected ");
		switch (item.getItemId()) {
		case R.id.menu_item_new_file:
			FileText file = new FileText();
			FileLab.get(getActivity()).addFile(file);
			Intent i = new Intent(getActivity(),ApiDemoActivity.class);
			i.putExtra(FileFragment.EXTRA_FILE_ID, file.getmId());
			startActivity(i);
			return true;
		case R.id.menu_item_show_subtitle:
			if (getActivity().getActionBar().getSubtitle() == null) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				mSubtitleVisible = true;
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				mSubtitleVisible = false;
				item.setTitle(R.string.show_subtitle);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.e("FileListFragment", " onCreateContextMenu ");
		getActivity().getMenuInflater().inflate(R.menu.file_list_item_context,
				menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		Log.e("onContextItemSelected", "dk0");
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		FileAdapter adapter = (FileAdapter) getListAdapter();
		FileText file = adapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.menu_item_delete_file:
			FileLab.get(getActivity()).deleteFile(file);
			adapter.notifyDataSetChanged();
			return true;
		}
		Log.e("onContextItemSelected", "dk");
		return super.onContextItemSelected(item);
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.e(TAG,"0");//
		FileText c = ((FileAdapter) getListAdapter()).getItem(position);
		Log.e(TAG,c.getmTitle());
	//	File c = ((FileAdapter) getListAdapter()).getItem(position);//
		Intent i = new Intent(getActivity(), FilePagerActivity.class);
		i.putExtra(FileFragment.EXTRA_FILE_ID, c.getmId());
		Log.e("onListItemClick", "dk");
		startActivity(i);
		
	}

	private class FileAdapter extends ArrayAdapter<FileText> {
		public FileAdapter(ArrayList<FileText> files) {
			
			super(getActivity(), 0, files);
			Log.e("FileListFragment", "FileAdapter ");
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Log.e("FileListFragment7", "getView ");
			if (convertView == null) {
				Log.e("FileListFragment", "getView ");
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_file, null);
			}
			FileText c = getItem(position);
			Log.e("FileListFragment0", "getView ");
			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.file_list_item_titleTextView);
			titleTextView.setText(c.getmTitle());
			Log.e("FileListFragment1", "getView ");
			TextView dateTextView = (TextView) convertView
					.findViewById(R.id.file_list_item_dateTextView);
			dateTextView.setText(c.getmDate().toString());
			Log.e("FileListFragment2", "getView ");
			
			return convertView;
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		Log.e("FileListFragment", "onCreateView0 ");
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		Log.e("FileListFragment", "onCreateView1 ");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mSubtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		Log.e("FileListFragment", "onCreateView2 ");
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		Log.e("FileListFragment", "onCreateView3 ");
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
			Log.e("FileListFragment", "onCreateView4");
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			Log.e("FileListFragment", "onCreateView5");
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public boolean onActionItemClicked(ActionMode arg0,
						MenuItem arg1) {
					Log.e("FileListFragment", "onCreateView6 ");
					switch (arg1.getItemId()) {
					case R.id.menu_item_delete_file:
						FileAdapter adapter = (FileAdapter) getListAdapter();
						FileLab fileLab = FileLab.get(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								fileLab.deleteFile(adapter.getItem(i));
							}
						}
						arg0.finish();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
					}
				}

				@Override
				public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
					MenuInflater inflater=arg0.getMenuInflater();
					inflater.inflate(R.menu.file_list_item_context, arg1);
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode arg0) {

				}

				@Override
				public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
					return false;
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode arg0,
						int arg1, long arg2, boolean arg3) {

				}
			});
		}
		Log.e("FileListFragment", "onCreateView7");
		return v;
	}
	public void onResume(){
		super.onResume();
		((FileAdapter)getListAdapter()).notifyDataSetChanged();
	}
}

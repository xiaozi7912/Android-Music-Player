package org.larry.xz_musicplayer.adapter;

import java.util.ArrayList;

import org.larry.xz_musicplayer.R;
import org.larry.xz_musicplayer.R.layout;
import org.larry.xz_musicplayer.model.FileInfoModel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter {
	private static final String LOG_TAG = "FileAdapter";
	private Activity mActivity = null;
	private ArrayList<FileInfoModel> mDataList = null;

	public FileAdapter(Activity activity, ArrayList<FileInfoModel> dataList) {
		// TODO Auto-generated constructor stub
		mActivity = activity;
		mDataList = dataList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		ViewHolder holder = null;

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = inflater.inflate(R.layout.item_file, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.item_file_icon);
			holder.textTitle = (TextView) convertView.findViewById(R.id.item_file_title);
			convertView.setTag(holder);
		}

		FileInfoModel selectedItem = mDataList.get(position);
		holder.textTitle.setText(selectedItem.title);
		return convertView;
	}

	class ViewHolder {
		public ImageView icon = null;
		public TextView textTitle = null;
	}
}

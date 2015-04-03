package org.larry.xz_musicplayer.adapter;

import java.util.ArrayList;

import org.larry.xz_musicplayer.R;
import org.larry.xz_musicplayer.model.AccountModel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private static final String LOG_TAG = "AccountAdapter";

	private Activity mActivity = null;
	private ArrayList<AccountModel> mDataList = null;

	public AccountAdapter(Activity activity, ArrayList<AccountModel> dataList) {
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
			convertView = inflater.inflate(R.layout.item_account, null);
			holder = new ViewHolder();
			holder.textId = (TextView) convertView.findViewById(R.id.item_account_text_id);
			holder.textEmail = (TextView) convertView.findViewById(R.id.item_account_text_email);
			holder.textAccesstoken = (TextView) convertView.findViewById(R.id.item_account_text_accesstoken);
			convertView.setTag(holder);
		}

		AccountModel selectedItem = mDataList.get(position);
		holder.textId.setText(String.valueOf(selectedItem.id));
		holder.textEmail.setText(selectedItem.email);
		holder.textAccesstoken.setText(selectedItem.accessToken);
		return convertView;
	}

	class ViewHolder {
		TextView textId = null;
		TextView textEmail = null;
		TextView textAccesstoken = null;
	}
}

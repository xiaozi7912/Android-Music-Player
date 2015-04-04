package org.larry.xz_musicplayer.adapter;

import org.larry.xz_musicplayer.R;

import android.accounts.Account;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class PickAccountAdapter extends BaseAdapter {
	private Activity mActivity = null;
	private Account[] mAccounts = null;

	public PickAccountAdapter(Activity activity, Account[] accounts) {
		// TODO Auto-generated constructor stub
		mActivity = activity;
		mAccounts = accounts;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAccounts.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAccounts[position];
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
			convertView = inflater.inflate(R.layout.item_pickaccount, null);
			holder = new ViewHolder();
			holder.textName = (TextView) convertView.findViewById(R.id.item_pickaccount_name);
			convertView.setTag(holder);
		}

		Account selectedItem = mAccounts[position];
		holder.textName.setText(selectedItem.name);
		return convertView;
	}

	class ViewHolder {
		TextView textName = null;
	}
}

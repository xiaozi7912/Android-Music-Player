package org.larry.xz_musicplayer.fragment;

import java.util.ArrayList;

import org.larry.xz_musicplayer.R;
import org.larry.xz_musicplayer.adapter.AccountAdapter;
import org.larry.xz_musicplayer.adapter.PickAccountAdapter;
import org.larry.xz_musicplayer.model.AccountModel;
import org.larry.xz_musicplayer.utility.GetAccesstokenTask;
import org.larry.xz_musicplayer.utility.MyEventListener;
import org.larry.xz_musicplayer.utility.SQLManager;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AccountFragment extends Fragment {
	private static final String LOG_TAG = "AccountFragment";
	private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = REQUEST_CODE_PICK_ACCOUNT + 1;
	private static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR + 1;

	private Activity mActivity = null;
	private Handler mHandler = new Handler();
	private String mAccount = null;
	private SQLManager mSqlManager = null;
	private MyEventListener mEventListener = null;

	private Button mBtnAdd = null;
	private ListView mListView = null;
	private AlertDialog mAlertDialog = null;

	private ArrayList<AccountModel> mAccountList = null;

	public AccountFragment(MyEventListener eventListener) {
		// TODO Auto-generated constructor stub
		mEventListener = eventListener;
	}

	public static AccountFragment newInstance(MyEventListener eventListener) {
		return new AccountFragment(eventListener);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mSqlManager = new SQLManager(mActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_account, null);
		mBtnAdd = (Button) rootView.findViewById(R.id.f_account_btnAdd);
		mListView = (ListView) rootView.findViewById(R.id.f_account_list);

		mBtnAdd.setOnClickListener(onClickListener);
		mListView.setOnItemClickListener(onItemClickListener);
		updateListView();
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		Log.i(LOG_TAG, "onActivityResult");
		if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
			Log.i(LOG_TAG, "REQUEST_CODE_PICK_ACCOUNT");
			if (resultCode == Activity.RESULT_OK) {
				Log.i(LOG_TAG, "REQUEST_CODE_PICK_ACCOUNT RESULT_OK");
				mAccount = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				getAccesstoken();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(LOG_TAG, "REQUEST_CODE_PICK_ACCOUNT RESULT_CANCELED");
			}
		} else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR || requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
				&& resultCode == Activity.RESULT_OK) {
			// Receiving a result that follows a GoogleAuthException, try auth
			// again
			getAccesstoken();
		}
	}

	public void handleException(final Exception e) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (e instanceof GooglePlayServicesAvailabilityException) {
					int statusCode = ((GooglePlayServicesAvailabilityException) e).getConnectionStatusCode();
					Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, mActivity, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
					dialog.show();
				} else if (e instanceof UserRecoverableAuthException) {
					Intent intent = ((UserRecoverableAuthException) e).getIntent();
					startActivityForResult(intent, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
				}
			}
		});
	}

	public void updateListView() {
		mAccountList = mSqlManager.Account().list();
		AccountAdapter adapter = new AccountAdapter(mActivity, mAccountList);
		mListView.setAdapter(adapter);
	}

	private void pickUserAccount() {
		Log.i(LOG_TAG, "pickUserAccount");
		String[] accountTypes = new String[] { "com.google" };
		Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
		startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}

	private void getAccesstoken() {
		Log.i(LOG_TAG, "getAccesstoken");
		new GetAccesstokenTask(mActivity, this, mAccount).execute();
	}

	private void getUserProfile() {
		Log.i(LOG_TAG, "getUserProfile");

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pickUserAccount();
		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			AccountModel selectedItem = mAccountList.get(position);
			mAccount = selectedItem.email;
			getAccesstoken();
			mEventListener.onAccountSelected(selectedItem);
		}
	};
}

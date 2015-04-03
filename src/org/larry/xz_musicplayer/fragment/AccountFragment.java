package org.larry.xz_musicplayer.fragment;

import java.io.IOException;

import org.larry.xz_musicplayer.R;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.R.integer;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class AccountFragment extends Fragment {
	private static final String LOG_TAG = "AccountFragment";
	private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
	private static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1002;

	private Activity mActivity = null;
	private Handler mHandler = new Handler();
	private String mAccount = null;
	private String mScopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/drive";

	private Button mBtnAdd = null;
	private ListView mListView = null;

	public static AccountFragment newInstance() {
		return new AccountFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_account, null);
		mBtnAdd = (Button) rootView.findViewById(R.id.f_account_btnAdd);
		mListView = (ListView) rootView.findViewById(R.id.f_account_list);

		mBtnAdd.setOnClickListener(onClickListener);
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
			if (resultCode == mActivity.RESULT_OK) {
				mAccount = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				getAccesstoken();
			} else if (resultCode == mActivity.RESULT_CANCELED) {

			}
		} else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR || requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
				&& resultCode == mActivity.RESULT_OK) {
			getAccesstoken();
		}
	}

	private void handleException(final Exception e) {
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

	private void pickUserAccount() {
		String[] accountTypes = new String[] { "com.google" };
		Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
		startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}

	private void getAccesstoken() {
		if (mAccount == null) {
			pickUserAccount();
		} else {
			new GetAccesstokenTask().execute();
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pickUserAccount();
		}
	};

	private class GetAccesstokenTask extends AsyncTask {
		private static final String LOG_TAG = "GetAccesstokenTask";

		public GetAccesstokenTask() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				String accessToken = fetchToken();
				if (accessToken != null) {
					Log.i(LOG_TAG, "doInBackground");
					Log.v(LOG_TAG, "accessToken : " + accessToken);
					Log.i(LOG_TAG, "--------------------------------------------------");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		private String fetchToken() throws IOException {
			try {
				return GoogleAuthUtil.getToken(mActivity, mAccount, mScopes);
			} catch (UserRecoverableAuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handleException(e);
			} catch (GoogleAuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}

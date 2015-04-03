package org.larry.xz_musicplayer.fragment;

import java.io.IOException;
import java.util.ArrayList;

import org.larry.xz_musicplayer.R;
import org.larry.xz_musicplayer.adapter.AccountAdapter;
import org.larry.xz_musicplayer.model.AccountModel;
import org.larry.xz_musicplayer.utility.SQLManager;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.model.people.Person;

import android.R.integer;
import android.R.plurals;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts.People;
import android.provider.ContactsContract.Profile;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AccountFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {
	private static final String LOG_TAG = "AccountFragment";
	private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = REQUEST_CODE_PICK_ACCOUNT + 1;
	private static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR + 1;
	private static final int REQUEST_CODE_AUTH_ERROR = REQUEST_CODE_RECOVER_FROM_AUTH_ERROR + 1;

	private Activity mActivity = null;
	private Handler mHandler = new Handler();
	private String mAccount = null;
	private String mScopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/drive";
	private SQLManager mSqlManager = null;
	private GoogleApiClient mClientApi = null;

	private Button mBtnAdd = null;
	private ListView mListView = null;

	private ArrayList<AccountModel> mAccountList = null;

	public static AccountFragment newInstance() {
		return new AccountFragment();
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
		if (mClientApi != null) {
			mClientApi.disconnect();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		Log.i(LOG_TAG, "onActivityResult");
		if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
			Log.i(LOG_TAG, "REQUEST_CODE_PICK_ACCOUNT");
			if (resultCode == mActivity.RESULT_OK) {
				Log.i(LOG_TAG, "REQUEST_CODE_PICK_ACCOUNT RESULT_OK");
				mAccount = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				getAccesstoken();
			} else if (resultCode == mActivity.RESULT_CANCELED) {
				Log.i(LOG_TAG, "REQUEST_CODE_PICK_ACCOUNT RESULT_CANCELED");
			}
		}
		if (requestCode == REQUEST_CODE_AUTH_ERROR) {
			Log.i(LOG_TAG, "REQUEST_CODE_AUTH_ERROR");
			if (resultCode == mActivity.RESULT_OK) {
				Log.i(LOG_TAG, "REQUEST_CODE_AUTH_ERROR RESULT_OK");
				getAccesstoken();
			} else if (resultCode == mActivity.RESULT_CANCELED) {
				Log.i(LOG_TAG, "REQUEST_CODE_AUTH_ERROR RESULT_CANCELED");
			} else {
				Log.i(LOG_TAG, "REQUEST_CODE_AUTH_ERROR ????");
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onConnectionFailed");
		if (result.hasResolution()) {
			Log.i(LOG_TAG, "hasResolution");
			try {
				result.startResolutionForResult(mActivity, REQUEST_CODE_AUTH_ERROR);
			} catch (SendIntentException e) {
				Log.e(LOG_TAG, e.getMessage());
				mClientApi.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onConnected");
		Toast.makeText(mActivity, "User is connected!", Toast.LENGTH_LONG).show();
		getUserProfile();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onConnectionSuspended");
		mClientApi.connect();
	}

	private void updateListView() {
		mAccountList = mSqlManager.Account().list();
		AccountAdapter adapter = new AccountAdapter(mActivity, mAccountList);
		mListView.setAdapter(adapter);
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
		Log.i(LOG_TAG, "getAccesstoken");
		if (mAccount == null) {
			pickUserAccount();
		} else {
			// new GetAccesstokenTask().execute();

			mClientApi = new GoogleApiClient.Builder(mActivity).addApi(Plus.API, Plus.PlusOptions.builder().build())
					.addScope(Plus.SCOPE_PLUS_PROFILE).addApi(Drive.API).addScope(Drive.SCOPE_FILE).setAccountName(mAccount)
					.addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
			mClientApi.connect();
		}
	}

	private void getUserProfile() {
		Log.i(LOG_TAG, "getUserProfile");

		Log.v(LOG_TAG, "isConnected : " + mClientApi.isConnected());
		Log.v(LOG_TAG, "getAccountName : " + Plus.AccountApi.getAccountName(mClientApi));
		if (Plus.PeopleApi.getCurrentPerson(mClientApi) != null) {
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mClientApi);

			AccountModel existsAccount = mSqlManager.Account().get(mAccount);
			ContentValues values = new ContentValues();
			values.put(SQLManager.ACCOUNT_EMAIL, mAccount);
			values.put(SQLManager.ACCOUNT_PICTURE, currentPerson.getImage().getUrl());

			if (existsAccount != null) {
				mSqlManager.Account().update(existsAccount.id, values);
			} else {
				mSqlManager.Account().insert(values);
			}
		} else {
			Log.e(LOG_TAG, "getCurrentPerson fail");
		}
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
			AccountModel selected = mAccountList.get(position);
			mAccount = selected.email;
			getAccesstoken();
		}
	};
}

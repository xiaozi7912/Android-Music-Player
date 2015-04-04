package org.larry.xz_musicplayer.utility;

import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONObject;
import org.larry.xz_musicplayer.fragment.AccountFragment;
import org.larry.xz_musicplayer.model.AccountModel;
import org.larry.xz_musicplayer.model.UserProfileModel;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.plus.Plus;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

public class GetAccesstokenTask extends AsyncTask {
	private static final String LOG_TAG = "GetAccesstokenTask";
	private Activity mActivity = null;
	private AccountFragment mFragment = null;
	private SQLManager mSqlManager = null;
	private GoogleRestClient mRestClient = null;

	private String mScopes = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/drive";
	private String mAccount = null;

	public GetAccesstokenTask(Activity activity, AccountFragment fragment, String account) {
		// TODO Auto-generated constructor stub
		mActivity = activity;
		mFragment = fragment;
		mSqlManager = new SQLManager(mActivity);
		mAccount = account;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		String accessToken = null;
		try {
			accessToken = GoogleAuthUtil.getToken(mActivity, mAccount, mScopes);

			Log.v(LOG_TAG, "accessToken : " + accessToken);
			Log.i(LOG_TAG, "--------------------------------------------------");
		} catch (UserRecoverableAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mFragment.handleException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accessToken;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result != null) {
			final String accessToken = result.toString();
			mRestClient = new GoogleRestClient(accessToken);

			mRestClient.get(GoogleRestClient.USER_PROFILE_API, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, headers, response);
					UserProfileModel userProfile = new UserProfileModel(response);
					AccountModel existAccount = mSqlManager.Account().get(mAccount);
					ContentValues values = new ContentValues();

					values.put(SQLManager.ACCOUNT_EMAIL, mAccount);
					values.put(SQLManager.ACCOUNT_PICTURE, userProfile.picture);
					values.put(SQLManager.ACCOUNT_ACCESSTOKEN, accessToken);
					if (existAccount != null) {
						mSqlManager.Account().update(existAccount.id, values);
					} else {
						mSqlManager.Account().insert(values);
					}
					mFragment.updateListView();
					Log.v(LOG_TAG, "mAccount : " + mAccount);
					Log.v(LOG_TAG, "id : " + userProfile.id);
					Log.v(LOG_TAG, "displayName : " + userProfile.displayName);
					Log.v(LOG_TAG, "picture : " + userProfile.picture);
					Log.i(LOG_TAG, "--------------------------------------------------");
				}
			});
		}
	}
}

package org.larry.xz_musicplayer;

import java.util.ArrayList;

import org.larry.xz_musicplayer.adapter.MyPagerAdapter;
import org.larry.xz_musicplayer.fragment.AccountFragment;
import org.larry.xz_musicplayer.fragment.FileBrowserFragment;
import org.larry.xz_musicplayer.fragment.PlayListFragment;
import org.larry.xz_musicplayer.model.AccountModel;
import org.larry.xz_musicplayer.utility.GetAccesstokenTask;
import org.larry.xz_musicplayer.utility.MyEventListener;
import org.larry.xz_musicplayer.utility.SQLManager;

import com.astuetz.PagerSlidingTabStrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements MyEventListener {
	private static final String LOG_TAG = "MainActivity";
	private Activity mActivity = this;
	private PagerSlidingTabStrip mTabStrip = null;
	private ViewPager mViewPager = null;
	private MyPagerAdapter mAdapter = null;
	private Handler mHandler = new Handler();
	private SQLManager mSqlManager = null;

	private AccountFragment mFAccount = AccountFragment.newInstance(this);
	private FileBrowserFragment mFBrowser = FileBrowserFragment.newInstance(this);
	private PlayListFragment mFPlayList = PlayListFragment.newInstance(this);

	private int mUpdateTokenTime = 1000 * 60 * 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSqlManager = new SQLManager(mActivity);
		startUpdateTokenTask();
		initView();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		int currentPage = mViewPager.getCurrentItem();
		if (currentPage == 1) {
			mFBrowser.onBackPressed();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onAccountSelected(AccountModel account) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "onAccountSelected");

		mFBrowser.initRestClient(account.accessToken);
		mViewPager.setCurrentItem(1);
		Log.v(LOG_TAG, "id : " + account.id);
		Log.v(LOG_TAG, "email : " + account.email);
		Log.v(LOG_TAG, "picture : " + account.picture);
		Log.i(LOG_TAG, "--------------------------------------------------");
	}

	@Override
	public void onRootFolderPressedBack() {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(0);
	}

	private void startUpdateTokenTask() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<AccountModel> accountList = mSqlManager.Account().list();
				for (AccountModel account : accountList) {
					new GetAccesstokenTask(mActivity, mFAccount, account.email).execute();
				}
				mHandler.postDelayed(this, mUpdateTokenTime);
			}
		});
	}

	private void initView() {
		mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.main_pagerTabs);
		mViewPager = (ViewPager) findViewById(R.id.main_viewPager);

		mViewPager.setOnPageChangeListener(onPageChangeListener);

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(mFAccount);
		fragments.add(mFBrowser);
		fragments.add(mFPlayList);
		mAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapter);
		mTabStrip.setViewPager(mViewPager);
	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};
}

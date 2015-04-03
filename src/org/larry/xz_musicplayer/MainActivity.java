package org.larry.xz_musicplayer;

import org.larry.xz_musicplayer.adapter.MyPagerAdapter;
import org.larry.xz_musicplayer.fragment.AccountFragment;

import com.astuetz.PagerSlidingTabStrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	private static final String LOG_TAG = "MainActivity";

	private PagerSlidingTabStrip mTabStrip = null;
	private ViewPager mViewPager = null;
	private MyPagerAdapter mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		AccountFragment fragment = (AccountFragment) mAdapter.getItem(0);
		fragment.onActivityResult(requestCode, resultCode, data);
	}

	private void initView() {
		mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.main_pagerTabs);
		mViewPager = (ViewPager) findViewById(R.id.main_viewPager);

		mViewPager.setOnPageChangeListener(onPageChangeListener);

		mAdapter = new MyPagerAdapter(getSupportFragmentManager());
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

package org.larry.xz_musicplayer;

import org.larry.xz_musicplayer.adapter.MyPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	private static final String LOG_TAG = "MainActivity";

	private PagerSlidingTabStrip mTabStrip = null;
	private ViewPager mViewPager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.main_pagerTabs);
		mViewPager = (ViewPager) findViewById(R.id.main_viewPager);

		mViewPager.setOnPageChangeListener(onPageChangeListener);

		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
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

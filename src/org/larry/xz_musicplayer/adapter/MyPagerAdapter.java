package org.larry.xz_musicplayer.adapter;

import org.larry.xz_musicplayer.fragment.AccountFragment;
import org.larry.xz_musicplayer.fragment.FileBrowserFragment;
import org.larry.xz_musicplayer.fragment.PlayListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	private static final String LOG_TAG = "MyPagerAdapter";
	private final String[] mTitles = { "Account", "File Browser", "PlayList" };

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			return AccountFragment.newInstance();
		case 1:
			return FileBrowserFragment.newInstance();
		case 2:
			return PlayListFragment.newInstance();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTitles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return mTitles[position];
	}
}

package org.larry.xz_musicplayer.fragment;

import org.larry.xz_musicplayer.R;
import org.larry.xz_musicplayer.utility.MyEventListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayListFragment extends Fragment {
	private static final String LOG_TAG = "PlayListFragment";

	private Activity mActivity = null;
	private MyEventListener mEventListener = null;

	public PlayListFragment(MyEventListener eventListener) {
		// TODO Auto-generated constructor stub
		mEventListener = eventListener;
	}

	public static PlayListFragment newInstance(MyEventListener eventListener) {
		return new PlayListFragment(eventListener);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_playlist, null);
		return rootView;
	}
}

package org.larry.xz_musicplayer.fragment;

import org.larry.xz_musicplayer.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayListFragment extends Fragment {
	private static final String LOG_TAG = "AccountFragment";

	public static PlayListFragment newInstance() {
		return new PlayListFragment();
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

package org.larry.xz_musicplayer.fragment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.larry.xz_musicplayer.R;
import org.larry.xz_musicplayer.adapter.FileAdapter;
import org.larry.xz_musicplayer.model.FileInfoModel;
import org.larry.xz_musicplayer.utility.GoogleRestClient;
import org.larry.xz_musicplayer.utility.MyEventListener;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.ContentValues;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FileBrowserFragment extends Fragment {
	private static final String LOG_TAG = "FileBrowserFragment";

	private Activity mActivity = null;
	private MyEventListener mEventListener = null;
	private GoogleRestClient mRestClient = null;
	private Handler mHandler = new Handler();
	private MediaPlayer mPlayer = null;

	private TextView mTextPath = null;
	private ListView mListView = null;
	private ProgressBar mProgressBar = null;
	private Button mBtnPrev = null;
	private Button mBtnPlay = null;
	private Button mBtnNext = null;
	private ArrayList<FileInfoModel> mFileInfoList = new ArrayList<FileInfoModel>();
	private ArrayList<FileInfoModel> mFileCacheList = new ArrayList<FileInfoModel>();
	private ArrayList<String> mFolderPathList = new ArrayList<String>();

	private static final String MIMETYPE_FOLDER = "application/vnd.google-apps.folder";
	private static final String MIMETYPE_AUDIO = "audio/";
	private Boolean mIsPlaying = false;
	private int mPrevPlayIndex = 0;
	private int mCurrPlayIndex = 0;
	private int mNextPlayIndex = 0;
	private int mDuration = 0;
	private int mUpdateTime = 0;

	public FileBrowserFragment(MyEventListener eventListener) {
		// TODO Auto-generated constructor stub
		mEventListener = eventListener;
	}

	public static FileBrowserFragment newInstance(MyEventListener eventListener) {
		return new FileBrowserFragment(eventListener);
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
		View rootView = inflater.inflate(R.layout.fragment_filebrowser, null);
		mTextPath = (TextView) rootView.findViewById(R.id.f_browser_text_path);
		mListView = (ListView) rootView.findViewById(R.id.f_browser_list);
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.f_browser_progressBar);
		mBtnPrev = (Button) rootView.findViewById(R.id.f_browser_btn_prev);
		mBtnPlay = (Button) rootView.findViewById(R.id.f_browser_btn_play);
		mBtnNext = (Button) rootView.findViewById(R.id.f_browser_btn_next);

		mListView.setOnItemClickListener(onItemClickListener);
		mBtnPrev.setOnClickListener(onClickListener);
		mBtnPlay.setOnClickListener(onClickListener);
		mBtnNext.setOnClickListener(onClickListener);
		return rootView;
	}

	public void onBackPressed() {
		int currSize = mFolderPathList.size();
		if (currSize == 1) {
			mEventListener.onRootFolderPressedBack();
		} else {
			int lastIndex = mFolderPathList.size() - 1;
			mFolderPathList.remove(lastIndex);
			updateListView(mFolderPathList.get(mFolderPathList.size() - 1));
		}
	}

	public void initRestClient(String accessToken) {
		Log.i(LOG_TAG, "initRestClient");
		mRestClient = new GoogleRestClient(accessToken);
		String rootId = "root";
		mFolderPathList.clear();
		mFolderPathList.add(rootId);
		updateListView(rootId);
	}

	private void updateListView(String folderId) {
		Log.i(LOG_TAG, "updateListView");
		mFileInfoList.clear();
		RequestParams params = new RequestParams();
		params.put("q", String.format("'%s' in parents and trashed = false and (mimeType contains '%s' or mimeType contains '%s')", folderId,
				MIMETYPE_FOLDER, MIMETYPE_AUDIO));
		mRestClient.listFile(params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				JSONArray files = response.optJSONArray("items");
				for (int i = 0; i < files.length(); i++) {
					FileInfoModel fileInfo = new FileInfoModel(files.optJSONObject(i));
					mFileInfoList.add(fileInfo);
					Log.v(LOG_TAG, "id : " + fileInfo.id);
					Log.v(LOG_TAG, "title : " + fileInfo.title);
					Log.v(LOG_TAG, "mimeType : " + fileInfo.mimeType);
					Log.v(LOG_TAG, "url : " + fileInfo.url);
					Log.i(LOG_TAG, "--------------------------------------------------");
				}
				FileAdapter adapter = new FileAdapter(mActivity, mFileInfoList);
				mListView.setAdapter(adapter);
			}
		});
	}

	private void initMusic(final FileInfoModel fileInfo) {
		Log.i(LOG_TAG, "playMusic");
		Log.v(LOG_TAG, "id : " + fileInfo.id);
		Log.v(LOG_TAG, "mimeType : " + fileInfo.mimeType);
		Log.v(LOG_TAG, "title : " + fileInfo.title);
		Log.v(LOG_TAG, "path : " + fileInfo.path);
		Log.i(LOG_TAG, "--------------------------------------------------");

		stopMusic();
		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mPlayer.setOnCompletionListener(onCompletionListener);
		mProgressBar.setProgress(0);

		FileInfoModel cachedFile = hasCachedFile(fileInfo.id);
		if (cachedFile != null) {
			playMusic(cachedFile.path);
		} else {
			mRestClient.getFile(fileInfo.url, new FileAsyncHttpResponseHandler(mActivity) {

				@Override
				public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, File response) {
					// TODO Auto-generated method stub
					fileInfo.path = response.getPath();
					mFileCacheList.add(fileInfo);
					playMusic(response.getPath());
				}
			});
		}
	}

	private void playMusic(String filePath) {
		try {
			mPlayer.setDataSource(filePath);
			mPlayer.prepare();
			mPlayer.start();
			mDuration = mPlayer.getDuration();
			mUpdateTime = mDuration / 100;
			mBtnPlay.performClick();

			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int currentProgress = mProgressBar.getProgress();
					if (!(currentProgress * mUpdateTime >= mDuration)) {
						currentProgress++;
						mProgressBar.setProgress(currentProgress);
					}
					if (mIsPlaying) {
						mHandler.postDelayed(this, mUpdateTime);
					}
				}
			}, mUpdateTime);

			Log.v(LOG_TAG, "mDuration : " + mDuration);
			Log.v(LOG_TAG, "mUpdateTime : " + mUpdateTime);
			Log.i(LOG_TAG, "--------------------------------------------------");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopMusic() {
		Log.i(LOG_TAG, "stopMusic");
		mIsPlaying = false;
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	private FileInfoModel hasCachedFile(String fileId) {
		FileInfoModel result = null;
		for (FileInfoModel cachedFile : mFileCacheList) {
			if (cachedFile.id.equals(fileId)) {
				result = cachedFile;
				break;
			}
		}
		return result;
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			mCurrPlayIndex = position;
			FileInfoModel selectedItem = mFileInfoList.get(position);
			if (selectedItem.mimeType.equals(MIMETYPE_FOLDER)) {
				mFolderPathList.add(selectedItem.id);
				updateListView(selectedItem.id);
			} else {
				// mFilePlayList.clear();
				// mFilePlayList.add(selectedItem);
				initMusic(selectedItem);
			}
		}
	};

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.f_browser_btn_prev:
				if (mCurrPlayIndex != 0) {
					mCurrPlayIndex--;
				}
				initMusic(mFileInfoList.get(mCurrPlayIndex));
				break;
			case R.id.f_browser_btn_play:
				if (mIsPlaying) {
					mIsPlaying = false;
					mBtnPlay.setText("Play");
					mPlayer.pause();
				} else {
					mIsPlaying = true;
					mBtnPlay.setText("Pause");
					mPlayer.start();
				}
				break;
			case R.id.f_browser_btn_next:
				if (mCurrPlayIndex != (mFileInfoList.size() - 1)) {
					mCurrPlayIndex++;
				}
				initMusic(mFileInfoList.get(mCurrPlayIndex));
				break;
			}
		}
	};

	private OnCompletionListener onCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mp.release();
			mPlayer = null;
		}
	};
}

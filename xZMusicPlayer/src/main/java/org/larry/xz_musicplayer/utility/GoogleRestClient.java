package org.larry.xz_musicplayer.utility;

import org.apache.http.client.ResponseHandler;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GoogleRestClient {
	private static final String LOG_TAG = "GoogleRestClient";
	private AsyncHttpClient client = new AsyncHttpClient();

	private static final String PLUS_BASE_API = "https://www.googleapis.com/plus/v1";
	private static final String DRIVE_BASE_API = "https://www.googleapis.com/drive/v2";

	public static final String USER_PROFILE_API = PLUS_BASE_API + "/people/me";
	public static final String DRIVE_CHILDREN_API = DRIVE_BASE_API + "/files/%s/children";
	public static final String DRIVE_FILE_LIST_API = DRIVE_BASE_API + "/files";
	public static final String DRIVE_FILE_GET_API = DRIVE_BASE_API + "/files/%s";

	public GoogleRestClient(String accessToken) {
		// TODO Auto-generated constructor stub
		client.addHeader("Authorization", "Bearer " + accessToken);
	}

	public void listChildren(String folderId, AsyncHttpResponseHandler responseHandler) {
		String url = String.format(DRIVE_CHILDREN_API, folderId);
		client.get(url, responseHandler);
	}

	public void listFile(RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(DRIVE_FILE_LIST_API, params, responseHandler);
	}

	public void getFile(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public void getMetadata(String fileId, AsyncHttpResponseHandler responseHandler) {
		String url = String.format(DRIVE_FILE_GET_API, fileId);
		client.get(url, responseHandler);
	}

	public void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}
}

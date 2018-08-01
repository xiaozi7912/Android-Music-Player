package org.larry.xz_musicplayer.model;

import org.json.JSONObject;

public class FileInfoModel {
	public String id = null;
	public String title = null;
	public String mimeType = null;
	public String url = null;
	public Boolean isPlaying = false;
	public String path = null;
	public String parentId = null;

	public FileInfoModel(JSONObject jObject) {
		// TODO Auto-generated constructor stub
		this.id = jObject.optString("id");
		this.title = jObject.optString("title");
		this.mimeType = jObject.optString("mimeType");
		this.url = jObject.optString("downloadUrl");
	}
}

package org.larry.xz_musicplayer.model;

import org.json.JSONObject;

public class UserProfileModel {
	public String id = null;
	public String displayName = null;
	public String picture = null;

	public UserProfileModel() {
		// TODO Auto-generated constructor stub
	}

	public UserProfileModel(JSONObject jObject) {
		// TODO Auto-generated constructor stub
		this.id = jObject.optString("id");
		this.displayName = jObject.optString("displayName");
		this.picture = jObject.optJSONObject("image").optString("url");
		if (this.picture != null) {
			this.picture = this.picture.split("\\?")[0];
		}
	}
}

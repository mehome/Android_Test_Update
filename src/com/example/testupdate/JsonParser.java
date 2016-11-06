package com.example.testupdate;

import org.json.JSONObject;

import android.text.TextUtils;

public class JsonParser {
	public static UpdateInfo getInfo(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		UpdateInfo info = new UpdateInfo();
		try {
			JSONObject object = new JSONObject(json);
			info.setVersion(object.getString("version"));
			info.setDescription(object.getString("description"));
			info.setIsforce(object.getBoolean("isforce"));
			info.setUrl(object.getString("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
}
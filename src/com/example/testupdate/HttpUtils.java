package com.example.testupdate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
	public static String getVersion(String urls) {
		try {
			URL url = new URL(urls);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(3000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				InputStream in = conn.getInputStream();
				byte[] by = new byte[1024];
				while (in.read(by, 0, by.length) != -1) {
					baos.write(by);
				}
				String result = baos.toString();
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DownLoadEntity getApk(String urls) {
		DownLoadEntity entity = null;
		try {
			URL url = new URL(urls);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(3000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				entity = new DownLoadEntity();
				entity.setIs(conn.getInputStream());
				entity.setLength(conn.getContentLength());
				return entity;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
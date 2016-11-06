package com.example.testupdate;

public class UpdateInfo {
	private String version;
	private String url;
	private String description;
	private boolean isforce;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIsforce() {
		return isforce;
	}

	public void setIsforce(boolean isforce) {
		this.isforce = isforce;
	}
}
package com.eyun.appmanager.domain;

import java.io.Serializable;

public class VersionAndroidVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private VersionAndroid currentVersion;// 当前版本
	private VersionAndroid newestVersion;// 最新版本

	public VersionAndroid getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(VersionAndroid currentVersion) {
		this.currentVersion = currentVersion;
	}

	public VersionAndroid getNewestVersion() {
		return newestVersion;
	}

	public void setNewestVersion(VersionAndroid newestVersion) {
		this.newestVersion = newestVersion;
	}

}

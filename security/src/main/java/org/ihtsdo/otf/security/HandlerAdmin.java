package org.ihtsdo.otf.security;

public class HandlerAdmin {

	private String appName;
	private String dirName;

	public final String getAppName() {
		if (appName == null) {
			appName = "";
		}
		return appName;
	}

	public final void setAppName(final String appNameIn) {
		appName = appNameIn;
	}

	public final String getDirName() {
		if (dirName == null) {
			dirName = "";
		}
		return dirName;
	}

	public final void setDirName(final String dirNameIn) {
		dirName = dirNameIn;
	}
}

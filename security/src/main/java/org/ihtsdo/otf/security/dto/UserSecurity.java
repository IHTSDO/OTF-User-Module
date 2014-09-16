package org.ihtsdo.otf.security.dto;

import java.util.logging.Logger;

public abstract class UserSecurity {

	public UserSecurity() {
		super();
	}

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(UserSecurity.class
			.getName());

	private OtfDirectories dirs = new OtfDirectories();
	private OtfApplications apps = new OtfApplications();

	private String handlerAdminDir;
	private String defaultpw;

	public static final String SETTINGS = "OTFSettings";
	public static final String ALL = "*";

	public abstract void init();

	public abstract void reset();

	public final OtfApplications getApps() {
		return apps;
	}

	public final void setApps(final OtfApplications appsIn) {
		apps = appsIn;
	}

	public final OtfDirectories getDirs() {
		return dirs;
	}

	public final void setDirs(final OtfDirectories dirsIn) {
		dirs = dirsIn;
	}

	public final String getHandlerAdminDir() {
		return handlerAdminDir;
	}

	public final void setHandlerAdminDir(String handlerAdminDirIn) {
		handlerAdminDir = handlerAdminDirIn;
	}

	public final String getDefaultpw() {
		return defaultpw;
	}

	public final void setDefaultpw(String defaultpwIn) {
		defaultpw = defaultpwIn;
	}

}

package org.ihtsdo.otf.security.dto;

public class UserSecurity {

	public UserSecurity() {
		super();
	}

	private OtfDirectories dirs = new OtfDirectories();
	private OtfApplications apps = new OtfApplications();
	private String defaultpw;
	private String usersApp = "Users";
	private String membersApp = "Members";

	public String getDefaultpw() {
		return defaultpw;
	}

	public void setDefaultpw(String defaultpwIn) {
		defaultpw = defaultpwIn;
	}

	public String getUsersApp() {
		return usersApp;
	}

	public void setUsersApp(String usersAppIn) {
		usersApp = usersAppIn;
	}

	public String getMembersApp() {
		return membersApp;
	}

	public void setMembersApp(String membersAppIn) {
		membersApp = membersAppIn;
	}

	public OtfApplications getApps() {
		return apps;
	}

	public void setApps(OtfApplications appsIn) {
		apps = appsIn;
	}

	public OtfDirectories getDirs() {
		return dirs;
	}

	public void setDirs(OtfDirectories dirsIn) {
		dirs = dirsIn;
	}

}

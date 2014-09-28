package org.ihtsdo.otf.security;

import java.util.Collection;
import java.util.List;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.UserSecurity;

public interface UserSecurityModel {

	void init();

	void reset();

	UserSecurity getModel();

	void setModel(UserSecurity userSecurityIn);

	UserSecurity getFullModel();

	void buildFullModel();

	void buildModel();

	OtfAccount getUserAccountByName(final String accnameIn);

	OtfAccount getUserAccountById(final String idIn);

	Collection<OtfAccount> getUsers();

	Collection<OtfAccountMin> getUsersMin();

	boolean accountExists(final String accNameIn);

	// String getDirNameForUser(final String accNameIn);

	// String getAppNameForUser(final String accNameIn);

	String getUsersDirName();

	OtfDirectory getDirByName(final String dirName);

	OtfDirectory getMembersDir();

	OtfDirectory getUsersDir();

	OtfSettings getSettings();

	// Should be just the member name or object....
	OtfGroup getMemberByName(final String accNameIn);

	OtfGroup getGroupById(final String idIn);

	OtfGroup getMemberById(final String idIn);

	List<OtfGroup> getGroupsByAppName(final String appnameIn);

	List<OtfGroup> getGroupsByDirName(final String dirnameIn);

	List<String> getDirsByAppName(String appname);

	List<String> getUserNames();

	List<String> getAdminUsers();

	List<String> getMembers();

	List<String> getApps();

	List<String> getAppsNotAdmin();

	// Map<String, List<String>> getAppsMap();

	// Map<String, OtfAccount> getAllAccounts();

	void setHandlerAdminDir(String handlerAdminDirIn);

	String getHandlerAdminDir();

	String getAdminApp();

	void setAdminApp(String adminAppIn);

	String getMembersApp();

	void setMembersApp(String membersAppIn);

	String getUsersApp();

	void setUsersApp(String usersAppIn);

}

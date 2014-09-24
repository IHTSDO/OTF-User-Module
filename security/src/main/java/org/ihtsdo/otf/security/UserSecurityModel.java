package org.ihtsdo.otf.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.UserSecurity;

public interface UserSecurityModel {

	UserSecurity getModel();

	void setModel(UserSecurity userSecurityIn);

	UserSecurity getFullModel();

	void buildFullModel();

	void buildModel();

	OtfAccount getUserAccountByName(final String accnameIn);

	OtfAccount getUserAccountById(final String idIn);

	Collection<OtfAccount> getUsers(final String dirnameIn);

	Collection<OtfAccountMin> getUsersMin(final String dirnameIn);

	List<String> getAdminUsers();

	boolean accountExists(final String accNameIn);

	String getDirNameForUser(final String accNameIn);

	String getAppNameForUser(final String accNameIn);

	String getUsersDirName();

	OtfDirectory getDirByName(final String dirName);

	OtfDirectory getMembersDir();

	OtfDirectory getUsersDir();

	OtfSettings getSettings();

	List<String> getMembers();

	List<String> getAppsNotAdmin();

	// Should be just the member name or object....
	OtfGroup getMemberByName(final String accNameIn);

	OtfGroup getGroupById(final String idIn);

	OtfGroup getMemberById(final String idIn);

	Map<String, List<String>> getAppsMap();

	List<OtfGroup> getGroupsByAppName(final String appnameIn);

	List<OtfGroup> getGroupsByDirName(final String dirnameIn);

	List<String> getDirsByAppName(String appname);

}

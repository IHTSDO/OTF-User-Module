package org.ihtsdo.otf.security.stormpath;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityModel;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.OtfSettings;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.dto.UserSecurityCached;
import org.ihtsdo.otf.security.dto.UserSecurityModelCached;

public class StormpathUserSecurityModel extends AbstractUserSecurityModel {

	private static final Logger LOG = Logger
			.getLogger(UserSecurityModelCached.class.getName());

	private UserSecurityStormpath localModel;

	@Override
	public UserSecurity getModel() {
		if (model == null) {
			model = new UserSecurityStormpath();
		}
		return null;
	}

	@Override
	public UserSecurity getFullModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildFullModel() {
		// TODO Auto-generated method stub

	}

	private UserSecurityStormpath getLocalModel() {
		if (localModel == null) {
			if (getModel() != null
					&& (getModel() instanceof UserSecurityCached)) {
				localModel = (UserSecurityStormpath) model;
			}
		}
		return localModel;
	}

	@Override
	public void buildModel() {
		// TODO Auto-generated method stub
	}

	@Override
	public OtfAccount getUserAccountById(String idIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<OtfAccount> getUsers(String dirnameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<OtfAccountMin> getUsersMin(String dirnameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAdminUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean accountExists(String accNameIn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDirNameForUser(String accNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAppNameForUser(String accNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsersDirName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfDirectory getMembersDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfDirectory getUsersDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfSettings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAppsNotMembersOrUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfGroup getMemberByName(String accNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfGroup getGroupById(String idIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfGroup getMemberById(String idIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getAppsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OtfGroup> getGroupsByAppName(String appnameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfAccount getUserAccountByName(String nameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDirsByAppName(String appnameIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OtfDirectory getDirByName(String dirNameIn) {
		// TODO Auto-generated method stub
		return null;
	}

}

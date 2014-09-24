package org.ihtsdo.otf.security.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.AbstractUserSecurityModel;

public class UserSecurityModelCached extends AbstractUserSecurityModel {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(UserSecurityModelCached.class.getName());

	private UserSecurityCached localModel;

	@Override
	public UserSecurity getModel() {
		if (model == null) {
			model = new UserSecurityCached();
		}
		// LOG.info("Model = " + model);
		return model;
	}

	@Override
	public UserSecurity getFullModel() {
		return getModel();
	}

	private UserSecurityCached getLocalModel() {
		if (localModel == null) {
			if (getModel() != null
					&& (getModel() instanceof UserSecurityCached)) {
				localModel = (UserSecurityCached) model;
			}
		}
		return localModel;
	}

	@Override
	public void buildFullModel() {
		buildModel();

	}

	@Override
	public void buildModel() {
		// Not necessary with Cached as always full model
	}

	@Override
	public OtfAccount getUserAccountById(String idIn) {
		return getLocalModel().getUserAccountById(idIn, "*");
	}

	@Override
	public Collection<OtfAccount> getUsers(String dirnameIn) {
		return getLocalModel().getUsers(dirnameIn);
	}

	@Override
	public Collection<OtfAccountMin> getUsersMin(String dirnameIn) {
		return getLocalModel().getMinUsers(dirnameIn);
	}

	@Override
	public List<String> getAdminUsers() {
		return getLocalModel().getAdminUsers();
	}

	@Override
	public boolean accountExists(String accNameIn) {
		return getLocalModel().accountExists(accNameIn);
	}

	@Override
	public String getDirNameForUser(String accNameIn) {
		Collection<String> dirnames = getLocalModel().getDirNamesForUser(
				accNameIn);

		if (dirnames != null && dirnames.size() > 0) {
			return dirnames.iterator().next();
		}
		return null;

	}

	@Override
	public String getAppNameForUser(String accNameIn) {
		return getLocalModel().getFirstAppForUser(accNameIn);
	}

	@Override
	public String getUsersDirName() {
		return getLocalModel().getUsersDir().getName();
	}

	@Override
	public OtfDirectory getMembersDir() {
		return getLocalModel().getMembersDir();
	}

	@Override
	public OtfDirectory getUsersDir() {
		return getLocalModel().getUsersDir();
	}

	@Override
	public OtfSettings getSettings() {
		return getLocalModel().getSettings();
	}

	@Override
	public List<String> getMembers() {
		return getLocalModel().getMembers();
	}

	@Override
	public OtfGroup getMemberByName(String accNameIn) {
		return getLocalModel().getMemberByName(accNameIn);
	}

	@Override
	public OtfGroup getGroupById(String idIn) {
		return getLocalModel().getGroupById(idIn);
	}

	@Override
	public OtfGroup getMemberById(String idIn) {
		return getLocalModel().getMemberById(idIn);
	}

	@Override
	public Map<String, List<String>> getAppsMap() {
		return getLocalModel().getAppsMap();
	}

	@Override
	public List<OtfGroup> getGroupsByAppName(String appnameIn) {
		return getLocalModel().getGroupsByAppName(appnameIn);
	}

	@Override
	public List<OtfGroup> getGroupsByDirName(final String dirnameIn) {
		return getLocalModel().getGroupsByDirName(dirnameIn);
	}

	@Override
	public OtfAccount getUserAccountByName(String nameIn) {
		return getLocalModel().getUserAccountByName(nameIn, "*");
	}

	@Override
	public List<String> getDirsByAppName(String appnameIn) {
		return getLocalModel().getDirsByAppName(appnameIn);
	}

	@Override
	public OtfDirectory getDirByName(String dirNameIn) {
		return getLocalModel().getDirByName(dirNameIn);
	}

	@Override
	public List<String> getAppsNotAdmin() {
		return getLocalModel().getAppsNotAdmin();
	}

}

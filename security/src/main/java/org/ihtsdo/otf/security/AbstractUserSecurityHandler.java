package org.ihtsdo.otf.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.objectcache.ObjectCache;

public abstract class AbstractUserSecurityHandler implements
		UserSecurityHandler {

	public static final String USH_KEY = "UserSecurityHandler";
	// private UserSecurity userSecurity;

	private UserSecurityModel userSecurityModel;

	public static final String NAME_NOT_UNIQUE = "Name is not unique";
	public static final String NAME_NOT_SET = "Name is not set";
	public static final String DIR_NOT_FOUND = "Directory not found";

	public static final String REMOTE_COMMIT_OK = "Remote Commit OK";
	public static final String REMOTE_COMMIT_NOT_OK = "Remote Commit NOTOK";

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(AbstractUserSecurityHandler.class.getName());

	@Override
	public abstract void saveUserSecurity() throws Exception;

	public abstract OtfAccount authAccountLocal(String acNameIn, String pwIn,
			String tokenIn);

	@Override
	public abstract void init(Properties propsIn) throws Exception;

	public abstract void localReload();

	@Override
	public abstract void buildUserSecurity() throws Exception;

	public abstract String addUpdateMemberLocal(OtfGroup grpIn,
			OtfDirectory mDirectory, boolean isNew);

	public abstract String addUpdateGroupLocal(OtfGroup grpIn,
			OtfDirectory mDirectory, boolean isNew);

	public abstract String addUpdateAppLocal(final OtfApplication appIn,
			boolean isNew);

	public abstract String addUpdateAccountLocal(final OtfAccount accIn,
			final String parentDirNameIn, boolean isNew);

	public abstract String addUpdateDirLocal(final OtfDirectory parentIn,
			boolean isNew);

	public abstract String addExistDirToAppLocal(final OtfDirectory dir,
			final OtfApplication app, boolean defAcSt, boolean defGrpSt,
			int order);

	@Override
	public abstract UserSecurityModel getLocalUserSecurityModel();

	@Override
	public OtfAccount getUser(final String acNameIn, final String pwIn,
			final String tokenIn) {
		if (stringOK(pwIn) || stringOK(tokenIn)) {
			// first auth
			return authAccount(acNameIn, pwIn, tokenIn);
		}
		if (!stringOK(pwIn) && !stringOK(tokenIn)) {
			return getUserSecurityModel().getUserAccountByName(acNameIn);
		}
		return null;
	}

	@Override
	public OtfAccount authAccount(final String acNameIn, final String pwIn,
			final String tokenIn) {
		OtfAccount acc = authAccountLocal(acNameIn, pwIn, tokenIn);
		if (acc != null) {
			acc.setAuth(true);
		}
		return acc;
	}

	public final String addExistDirToApp(final OtfDirectory dir,
			final OtfApplication app, final boolean defAcSt,
			final boolean defGrpSt, final int order) {
		app.getAccountStores().put(dir.getName(), dir);
		return addExistDirToAppLocal(dir, app, defAcSt, defGrpSt, order);

	}

	// @Override
	// public final UserSecurity getUserSecurity() {
	// if (userSecurity == null) {
	// userSecurity = (UserSecurity) ObjectCache.INSTANCE.get(getKey());
	// }
	// return userSecurity;
	// }
	//
	// @Override
	// public final void setUserSecurity(final UserSecurity userSecurityIn) {
	// ObjectCache.INSTANCE.put(getKey(), userSecurityIn);
	// if (userSecurityIn != null) {
	// userSecurityIn.initCachedValues();
	// }
	// }

	public void removeUserSecurityModel() {
		userSecurityModel = null;
		ObjectCache.INSTANCE.remove(getKey());
	}

	@Override
	public UserSecurityModel getUserSecurityModel(UserSecurity userSecurityIn) {
		UserSecurityModel usm = getLocalUserSecurityModel();
		usm.setModel(userSecurityIn);
		setUserSecurityModel(usm);
		return userSecurityModel;
	}

	@Override
	public UserSecurityModel getUserSecurityModel(UserSecurity userSecurityIn,
			HandlerAdmin handlerAdmin) {
		UserSecurityModel usm = getLocalUserSecurityModel();
		usm.setHandlerAdmin(handlerAdmin);
		usm.setModel(userSecurityIn);
		setUserSecurityModel(usm);
		return userSecurityModel;
	}

	@Override
	public UserSecurityModel getUserSecurityModel() {
		if (userSecurityModel == null) {
			userSecurityModel = (UserSecurityModel) ObjectCache.INSTANCE
					.get(getKey());
		}
		if (userSecurityModel == null) {
			setUserSecurityModel(getLocalUserSecurityModel());
		}
		return userSecurityModel;
	}

	@Override
	public void setUserSecurityModel(UserSecurityModel userSecurityModelIn) {
		userSecurityModel = userSecurityModelIn;
		ObjectCache.INSTANCE.put(getKey(), userSecurityModelIn);
		if (userSecurityModelIn != null) {
			userSecurityModelIn.getModel().init();
		}
	}

	private String getKey() {
		return new StringBuilder().append(USH_KEY).append("-")
				.append(getClass().getName()).toString();
	}

	@Override
	public final void reload() {
		removeUserSecurityModel();
		localReload();
		try {
			buildUserSecurity();
			getUserSecurityModel().getModel().reset();
		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}
		getUserSecurityModel().getModel();

		// reload cached values
		getUserSecurityModel().resetSettings();
		getUserSecurityModel().resetAllAccounts();
		getUserSecurityModel().resetAppsMap();
		getUserSecurityModel().resetMembers();
	}

	@Override
	public final String addUpdateAccount(final OtfAccount accIn) {
		boolean isNew = accIn.isNew();

		if (isNew && getUserSecurityModel().accountExists(accIn.getName())) {
			// names must be unique
			return NAME_NOT_UNIQUE;
		}
		if (!stringOK(accIn.getName())) {
			LOG.info("account " + NAME_NOT_SET);
			return NAME_NOT_SET;
		}

		String parentIn = accIn.getParentDir();

		if (!stringOK(parentIn)) {
			// use the std one in settings.
			parentIn = getUserSecurityModel().getUsersDirName();
		}

		if (isNew) {
			accIn.getId();
		}

		return addUpdateAccountLocal(accIn, parentIn, isNew);
	}

	@Override
	public final String addUpdateMember(final OtfGroup grpIn) {
		// get members parent dir
		OtfDirectory mDirectory = getUserSecurityModel().getMembersDir();
		// is new
		boolean isNew = grpIn.isNew();

		if (isNew && mDirectory.getGroups().groupExists(grpIn.getName())) {
			// names must be unique
			return NAME_NOT_UNIQUE;
		}
		if (!stringOK(grpIn.getName())) {
			return NAME_NOT_SET;
		}
		mDirectory.getGroups().getGroups().put(grpIn.getName(), grpIn);
		return addUpdateMemberLocal(grpIn, mDirectory, isNew);
	}

	@Override
	public final String addUpdateGroup(final OtfGroup grpIn) {
		String pDir = grpIn.getParentDirName();
		boolean isNew = grpIn.isNew();
		OtfDirectory mDirectory = getUserSecurityModel().getDirByName(pDir);
		if (isNew && mDirectory.getGroups().groupExists(grpIn.getName())) {
			// names must be unique
			return NAME_NOT_UNIQUE;
		}
		if (!stringOK(grpIn.getName())) {
			return NAME_NOT_SET;
		}

		if (isNew) {
			// add to model
			grpIn.getId();
			mDirectory.getGroups().getGroups().put(grpIn.getName(), grpIn);
		}

		String rVal = addUpdateGroupLocal(grpIn, mDirectory, isNew);
		return rVal;

	}

	@Override
	public final String addUpdateSettings(final OtfGroup grpIn) {
		return null;
	}

	@Override
	public final String addUpdateDir(OtfDirectory dirIn) {
		boolean isNew = dirIn.isNew();

		if (isNew && getUserSecurityModel().dirExists(dirIn.getName())) {
			return NAME_NOT_UNIQUE;
		}
		if (!stringOK(dirIn.getName())) {
			return NAME_NOT_SET;
		}

		getUserSecurityModel().getModel().getDirs().getDirectories()
				.put(dirIn.getName(), dirIn);

		return addUpdateDirLocal(dirIn, isNew);
	}

	@Override
	public final String addUpdateApp(final OtfApplication appIn) {
		boolean isNew = appIn.isNew();
		if (isNew && getUserSecurityModel().appExists(appIn.getName())) {
			// names must be unique
			return NAME_NOT_UNIQUE;
		}

		if (!stringOK(appIn.getName())) {
			return NAME_NOT_SET;
		}

		if (isNew) {
			// add to model
			appIn.getId();
			// add new Directory
			OtfDirectory dir = new OtfDirectory();
			dir.getId();
			dir.setName(appIn.getName());
			dir.setDescription("Auto created Directory for " + appIn.getName());

			String dirRval = addUpdateDir(dir);
			if (!dirRval.equals(NAME_NOT_UNIQUE)
					&& !dirRval.equals(NAME_NOT_SET)) {
				appIn.getAccountStores().put(dir.getName(), dir);
				getUserSecurityModel().getModel().getApps().getApplications()
						.put(appIn.getName(), appIn);
			} else {
				return "DIRECTORY " + dirRval;
			}
		}

		return addUpdateAppLocal(appIn, isNew);
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

	@Override
	public void postbuildUserSecurity() {

		if (getUserSecurityModel() != null
				&& getUserSecurityModel().getSettings() != null) {

			boolean isInited = getUserSecurityModel().getSettings().isinited();
			LOG.info("The Model is initialized");
			if (!isInited) {
				// See if there is a User Dir
				String userDir = getUserSecurityModel().getUsersApp();
				boolean userDirFound = false;
				OtfDirectory udir = getUserSecurityModel()
						.getDirByName(userDir);
				// if no User Dir create it
				if (udir == null) {
					udir = new OtfDirectory();
					udir.setName(userDir);
					addUpdateDir(udir);
				}

				List<String> apps = getUserSecurityModel().getApps();

				for (String appname : apps) {
					List<String> appDirs = getUserSecurityModel()
							.getDirsByAppName(appname);
					if (appDirs.contains(userDir)) {
						userDirFound = true;
						break;
					}

				}
				// if user dir not part of an app then get an app
				if (!userDirFound) {
					OtfApplication existApp = null;
					// if users app exists add it to that
					if (apps.contains(userDir)) {
						existApp = getUserSecurityModel().getAppbyName(userDir);
					}

					if (existApp == null) {

						List<String> adminapps = new ArrayList<String>();
						adminapps.add(getUserSecurityModel().getMembersApp());
						adminapps.add(getUserSecurityModel().getAdminApp());
						adminapps.add(getUserSecurityModel().getHandlerAdmin()
								.getAppName());

						// get the first not AdminHandler App
						for (String appname : apps) {
							if (!adminapps.contains(appname)) {
								existApp = getUserSecurityModel().getAppbyName(
										appname);
								break;
							}
						}
					}

					if (existApp != null) {
						// add the dir to it.
						existApp.getAccountStores().put(udir.getName(), udir);
						// update
						addExistDirToApp(udir, existApp, true, false, 0);
					}
				}
				// Then update settings setting inited to true
				getUserSecurityModel().getSettings().setInited("true");
				getUserSecurityModel().getSettings().updateSettings();
				addUpdateGroup(getUserSecurityModel().getSettings().getGrp());
			}
		}
	}
}

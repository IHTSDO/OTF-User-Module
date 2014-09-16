package org.ihtsdo.otf.security;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
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

	public abstract OtfAccount authAccountLocal(String acNameIn, String pwIn);

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

	@Override
	public abstract UserSecurityModel getLocalUserSecurityModel();

	@Override
	public OtfAccount getUser(String acNameIn, String pwIn) {
		if (pwIn != null && pwIn.length() > 0) {
			// first auth
			return authAccount(acNameIn, pwIn);
		}
		if (pwIn == null || pwIn.length() == 0) {
			return getUserSecurityModel().getUserAccountByName(acNameIn);
		}
		return null;
	}

	@Override
	public OtfAccount authAccount(String acNameIn, String pwIn) {

		// move all this to the local method
		// check acc exists
		// OtfAccount acc = getUserSecurity().getUserAccountByName(acNameIn,
		// UserSecurity.ALL);
		// if (acc == null) {
		// return null;
		// }
		// // check if uuid/token
		// if (acc.checkAuthToken(pwIn)) {
		// return acc;
		// }

		OtfAccount acc = authAccountLocal(acNameIn, pwIn);
		if (acc != null) {
			acc.setAuth(true);
		}
		return acc;
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

		// if (parentIn == null) {
		// Collection<String> dirnames = getUserSecurityModel().getModel()
		// .getDirNamesForUser(accIn.getName());
		// if (!dirnames.isEmpty()) {
		// if (dirnames.size() > 1) {
		// LOG.severe("MORE THAN 1 directory found for user called "
		// + accIn.getName() + " num dirs = "
		// + dirnames.size());
		// }
		// // get the user using the 1st...there should only be one.....
		String parentIn = null;
		// }

		if (!isNew) {
			parentIn = getUserSecurityModel()
					.getDirNameForUser(accIn.getName());
		}

		if (parentIn == null) {
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
		OtfDirectory mDirectory = getUserSecurityModel().getModel().getDirs()
				.getDirByName(pDir);
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

		if (isNew
				&& getUserSecurityModel().getModel().getDirs()
						.dirExists(dirIn.getName())) {
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
		if (isNew
				&& getUserSecurityModel().getModel().getApps()
						.appExists(appIn.getName())) {
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

}

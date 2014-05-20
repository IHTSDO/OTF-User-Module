package org.ihtsdo.otf.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
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
	private UserSecurity userSecurity;

	public static final String NAME_NOT_UNIQUE = "Name is not unique";
	public static final String DIR_NOT_FOUND = "Directory not found";

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(AbstractUserSecurityHandler.class.getName());

	@Override
	public abstract void saveUserSecurity() throws IOException;

	@Override
	public abstract OtfAccount authAccount(String acNameIn, String pwIn);

	@Override
	public abstract void init(Properties propsIn) throws Exception;

	public abstract void localReload();

	@Override
	public abstract void buildUserSecurity() throws Exception;

	public abstract String addUpdateMemberLocal(OtfGroup grpIn,
			OtfDirectory mDirectory, boolean isNew);

	public abstract String addUpdateAppLocal(final OtfApplication appIn,
			boolean isNew);

	public abstract String addUpdateAccountLocal(final OtfAccount accIn,
			final OtfDirectory parentIn, boolean isNew);

	@Override
	public final UserSecurity getUserSecurity() {
		if (userSecurity == null) {
			userSecurity = (UserSecurity) ObjectCache.INSTANCE.get(getKey());
		}
		return userSecurity;
	}

	@Override
	public final void setUserSecurity(final UserSecurity userSecurityIn) {
		ObjectCache.INSTANCE.put(getKey(), userSecurityIn);
		if (userSecurityIn != null) {
			userSecurityIn.initCachedValues();
		}
	}

	public void removeUserSecurity() {
		userSecurity = null;
		ObjectCache.INSTANCE.remove(getKey());

	}

	private String getKey() {
		return new StringBuilder().append(USH_KEY).append("-")
				.append(getClass().getName()).toString();
	}

	@Override
	public final void reload() {
		removeUserSecurity();
		localReload();
		try {
			buildUserSecurity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getUserSecurity();
	}

	@Override
	public final String addUpdateAccount(final OtfAccount accIn,
			OtfDirectory parentIn) {
		boolean isNew = accIn.isNew();

		if (isNew && getUserSecurity().accountExists(accIn.getName())) {
			// names must be unique
			return NAME_NOT_UNIQUE;
		}

		if (parentIn == null) {
			Collection<String> dirnames = getUserSecurity().getDirNamesForUser(
					accIn.getName());
			if (dirnames.size() > 0) {
				if (dirnames.size() > 1) {
					LOG.severe("MORE THAN 1 directory found for user called "
							+ accIn.getName() + " num dirs = "
							+ dirnames.size());
				}
				// get the user using the 1st...there should only be one.....
				parentIn = getUserSecurity().getDirs().getDirByName(
						dirnames.iterator().next());
			}
		}

		return addUpdateAccountLocal(accIn, parentIn, isNew);
	}

	@Override
	public final String addUpdateMember(final OtfGroup grpIn) {
		// get members parent dir
		OtfDirectory mDirectory = getUserSecurity().getMembersDir();
		// is new
		boolean isNew = grpIn.isNew();

		if (isNew && mDirectory.getGroups().groupExists(grpIn.getName())) {
			// names must be unique
			return NAME_NOT_UNIQUE;
		}
		mDirectory.getGroups().getGroups().put(grpIn.getName(), grpIn);
		return addUpdateMemberLocal(grpIn, mDirectory, isNew);
	}

	@Override
	public final String addUpdateApp(final OtfApplication appIn) {
		boolean isNew = appIn.isNew();
		if (isNew && getUserSecurity().getApps().appExists(appIn.getName())) {
			// names must be unique
			return NAME_NOT_UNIQUE;
		}

		return addUpdateAppLocal(appIn, isNew);
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

}

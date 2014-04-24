package org.ihtsdo.otf.security;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.UserSecurity;
import org.ihtsdo.otf.security.objectcache.ObjectCache;

public abstract class AbstractUserSecurityHandler implements
		UserSecurityHandler {

	public static final String USH_KEY = "UserSecurityHandler";
	private UserSecurity userSecurity;

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

	@Override
	public abstract void buildUserSecurity() throws Exception;

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
	}

	private String getKey() {
		return new StringBuilder().append(USH_KEY).append("-")
				.append(getClass().getName()).toString();
	}

	@Override
	public final void reload() {
		userSecurity = null;
		try {
			buildUserSecurity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getUserSecurity();
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

}

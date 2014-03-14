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
	public UserSecurity getUserSecurity() {
		if (userSecurity == null) {
			userSecurity = (UserSecurity) ObjectCache.INSTANCE.get(getKey());
		}
		return userSecurity;
	}

	@Override
	public void setUserSecurity(UserSecurity userSecurityIn) {
		ObjectCache.INSTANCE.put(getKey(), userSecurityIn);
		reload();
	}

	private String getKey() {
		return new StringBuilder().append(USH_KEY).append("-")
				.append(getClass().getName()).toString();
	}

	@Override
	public void reload() {
		userSecurity = null;
		getUserSecurity();
	}

	@Override
	public abstract void saveUserSecurity() throws IOException;

	@Override
	public abstract OtfAccount authAccount(String acNameIn, String pwIn);

	@Override
	public abstract void init(Properties propsIn) throws Exception;

}

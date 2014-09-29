package org.ihtsdo.otf.security;

import java.util.Properties;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;

public interface UserSecurityHandler {

	// UserSecurity getUserSecurity();
	//
	// void setUserSecurity(UserSecurity userSecurityIn);

	void saveUserSecurity() throws Exception;

	UserSecurityModel getUserSecurityModel();

	void setUserSecurityModel(UserSecurityModel userSecurityModelIn);

	UserSecurityModel getUserSecurityModel(UserSecurity userSecurityIn);

	UserSecurityModel getUserSecurityModel(UserSecurity userSecurityIn,
			String handlerAdminDir);

	UserSecurityModel getLocalUserSecurityModel();

	OtfAccount authAccount(String acName, String pw, String token);

	OtfAccount getUser(String acName, String pw, String token);

	void init(Properties props) throws Exception;

	void reload();

	void buildUserSecurity() throws Exception;

	void postbuildUserSecurity();

	String addUpdateAccount(OtfAccount acc);

	String addUpdateMember(OtfGroup grp);

	String addUpdateSettings(OtfGroup grp);

	String addUpdateGroup(OtfGroup grp);

	String addUpdateApp(OtfApplication app);

	String addUpdateDir(OtfDirectory dir);

	String requestUpdateUserPassword(String username, String emailAddr);

	/**
	 * If the token is the only value (all others are null), then return the
	 * username else check the token & if OK use the password (which must be not
	 * null and longer than 0) to update the stored one for the username.
	 * 
	 * @param username
	 * @param password
	 * @param token
	 * @return A number denoting success or failure. A negative number is a
	 *         failure. 0 is where the username and token agree and thus the
	 *         user should be allowed to input a password. 1 is a successful
	 *         change of the password.
	 */
	int updateUserPassword(String username, String password, String token);

}

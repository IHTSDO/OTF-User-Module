package org.ihtsdo.otf.security;

import java.util.Properties;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;

public interface UserSecurityHandler {

	UserSecurity getUserSecurity();

	void setUserSecurity(UserSecurity userSecurityIn);

	void saveUserSecurity() throws Exception;

	OtfAccount authAccount(String acName, String pw);

	OtfAccount getUser(String acName, String pw);

	void init(Properties props) throws Exception;

	void reload();

	void buildUserSecurity() throws Exception;

	String addUpdateAccount(OtfAccount acc, OtfDirectory parent);

	String addUpdateMember(OtfGroup grp);

	String addUpdateSettings(OtfGroup grp);

	String addUpdateGroup(OtfGroup grp);

	String addUpdateApp(OtfApplication app);

	String addUpdateDir(OtfDirectory dir);

}

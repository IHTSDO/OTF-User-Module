package org.ihtsdo.otf.security;

import java.io.IOException;
import java.util.Properties;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfApplication;
import org.ihtsdo.otf.security.dto.OtfDirectory;
import org.ihtsdo.otf.security.dto.OtfGroup;
import org.ihtsdo.otf.security.dto.UserSecurity;

public interface UserSecurityHandler {

	UserSecurity getUserSecurity();

	void setUserSecurity(UserSecurity userSecurityIn);

	void saveUserSecurity() throws IOException;

	OtfAccount authAccount(String acName, String pw);

	void init(Properties props) throws Exception;

	void reload();

	void buildUserSecurity() throws Exception;

	boolean addUpdateAccount(OtfAccount acc, OtfDirectory parent);

	boolean addUpdateMember(OtfGroup grp);

	boolean addUpdateApp(OtfApplication app);

}

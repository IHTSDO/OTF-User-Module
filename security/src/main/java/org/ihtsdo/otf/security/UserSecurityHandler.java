package org.ihtsdo.otf.security;

import java.io.IOException;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.UserSecurity;

public interface UserSecurityHandler {

	UserSecurity getUserSecurity();

	void setUserSecurity(UserSecurity userSecurityIn);

	void saveUserSecurity() throws IOException;

	OtfAccount authAccount(String acName, String pw);

}

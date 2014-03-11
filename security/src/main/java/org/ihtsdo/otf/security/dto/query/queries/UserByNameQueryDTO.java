package org.ihtsdo.otf.security.dto.query.queries;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OftAccountMin;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UserByNameQueryDTO extends AbstractSecurityQuery {

	OftAccountMin user;
	private String userName;
	private String pw;

	public UserByNameQueryDTO() {
		super();
	}

	public UserByNameQueryDTO(UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public UserByNameQueryDTO(UserSecurityHandler userSecurityIn,
			String userNameIn, String pwIn) {
		super(userSecurityIn);
		userName = userNameIn;
		pw = pwIn;
	}

	public OftAccountMin getUser() {
		if (user == null) {
			OtfAccount oacc = null;
			// if pw is set
			if (pw != null && pw.length() > 0) {
				// first auth
				oacc = ush.authAccount(userName, pw);
			}
			if (pw == null || pw.length() == 0) {
				oacc = ush.getUserSecurity().getUserAccountByName(userName);
			}
			if (oacc != null) {
				user = new OftAccountMin(oacc);
			}
		}
		return user;
	}

	public void setUser(OftAccountMin userIn) {
		user = userIn;
	}

}

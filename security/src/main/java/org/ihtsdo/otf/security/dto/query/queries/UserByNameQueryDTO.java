package org.ihtsdo.otf.security.dto.query.queries;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccountMin;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UserByNameQueryDTO extends AbstractSecurityQuery {

	private OtfAccountMin user;
	private String userName;
	private String pw;

	public UserByNameQueryDTO() {
		super();
	}

	public UserByNameQueryDTO(final UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public UserByNameQueryDTO(final UserSecurityHandler userSecurityIn,
			final String userNameIn, final String pwIn) {
		super(userSecurityIn);
		userName = userNameIn;
		pw = pwIn;
	}

	public final OtfAccountMin getUser() {
		if (user == null) {
			OtfAccount oacc = null;
			// if pw is set
			if (pw != null && pw.length() > 0) {
				// first auth
				oacc = ush.authAccount(userName, pw);
			}
			if (pw == null || pw.length() == 0) {
				oacc = ush.getUserSecurity()
						.getUserAccountByName(userName, "*");
			}
			if (oacc != null) {
				user = new OtfAccountMin(oacc);
			}
		}
		return user;
	}

	public final void setUser(final OtfAccountMin userIn) {
		user = userIn;
	}

}

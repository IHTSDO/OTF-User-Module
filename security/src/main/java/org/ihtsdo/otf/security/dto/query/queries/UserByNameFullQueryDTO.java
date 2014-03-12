package org.ihtsdo.otf.security.dto.query.queries;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UserByNameFullQueryDTO extends AbstractSecurityQuery {

	OtfAccount user;
	private String userName;
	private String pw;

	public UserByNameFullQueryDTO() {
		super();
	}

	public UserByNameFullQueryDTO(UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public UserByNameFullQueryDTO(UserSecurityHandler userSecurityIn,
			String userNameIn, String pwIn) {
		super(userSecurityIn);
		userName = userNameIn;
		pw = pwIn;
	}

	public OtfAccount getUser() {
		if (user == null) {
			// if pw is set
			if (pw != null && pw.length() > 0) {
				// first auth
				user = ush.authAccount(userName, pw);
			}
			if (pw == null || pw.length() == 0) {
				user = ush.getUserSecurity().getUserAccountByName(userName);
			}

		}
		return user;
	}

	public void setUser(OtfAccount userIn) {
		user = userIn;
	}

}

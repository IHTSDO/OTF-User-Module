package org.ihtsdo.otf.security.dto.query.queries;

import org.ihtsdo.otf.security.UserSecurityHandler;
import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.query.AbstractSecurityQuery;

public class UserByNameFullQueryDTO extends AbstractSecurityQuery {

	private OtfAccount user;
	private String userName;
	private String pw;

	public UserByNameFullQueryDTO() {
		super();
	}

	public UserByNameFullQueryDTO(final UserSecurityHandler userSecurityIn) {
		super(userSecurityIn);
	}

	public UserByNameFullQueryDTO(final UserSecurityHandler userSecurityIn,
			final String userNameIn, final String pwIn) {
		super(userSecurityIn);
		userName = userNameIn;
		pw = pwIn;
	}

	public final OtfAccount getUser() {
		if (user == null) {
			user = ush.getUser(userName, pw, null);
		}
		return user;
	}

	public final void setUser(final OtfAccount userIn) {
		user = userIn;
	}

}

package org.ihtsdo.otf.security.dto.query;

import org.ihtsdo.otf.security.UserSecurityHandler;

public abstract class AbstractSecurityQuery {

	protected UserSecurityHandler ush;

	public AbstractSecurityQuery(UserSecurityHandler ushIn) {
		super();
		ush = ushIn;
	}

	public AbstractSecurityQuery() {
		super();
	}

}

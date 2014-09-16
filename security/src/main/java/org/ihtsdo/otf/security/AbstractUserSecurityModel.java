package org.ihtsdo.otf.security;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.UserSecurity;

public abstract class AbstractUserSecurityModel implements UserSecurityModel {

	protected UserSecurity model;

	@Override
	public abstract UserSecurity getModel();

	@Override
	public void setModel(UserSecurity userSecurityIn) {
		model = userSecurityIn;
	}

	@Override
	public abstract UserSecurity getFullModel();

	@Override
	public abstract void buildFullModel();

	@Override
	public abstract void buildModel();

	@Override
	public abstract OtfAccount getUserAccountByName(final String name);

}

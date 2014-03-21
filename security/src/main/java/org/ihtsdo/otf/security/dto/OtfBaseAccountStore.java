package org.ihtsdo.otf.security.dto;

public abstract class OtfBaseAccountStore extends OtfBaseNameDesc {

	private OtfAccounts accounts = new OtfAccounts();

	public final OtfAccounts getAccounts() {
		return accounts;
	}

	public final void setAccounts(final OtfAccounts accountsIn) {
		accounts = accountsIn;
	}

}

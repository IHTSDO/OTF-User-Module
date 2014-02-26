package org.ihtsdo.otf.security.dto;

public abstract class OtfBaseAccountStore extends OtfBaseNameDesc {

	private OtfAccounts accounts = new OtfAccounts();

	public OtfAccounts getAccounts() {
		return accounts;
	}

	public void setAccounts(OtfAccounts accountsIn) {
		accounts = accountsIn;
	}

}

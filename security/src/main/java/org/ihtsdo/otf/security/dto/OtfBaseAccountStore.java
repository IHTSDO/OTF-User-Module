package org.ihtsdo.otf.security.dto;

public abstract class OtfBaseAccountStore extends OtfBaseNameDesc {

	private OtfAccounts accounts = new OtfAccounts();

	@Override
	public String getHtmlForm() {
		return super.getHtmlForm();
	}

	public final OtfAccounts getAccounts() {
		return accounts;
	}

	public final void setAccounts(final OtfAccounts accountsIn) {
		accounts = accountsIn;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(super.toString()).append("\n");
		sbuild.append(accounts.toString());
		return sbuild.toString();

	}

}

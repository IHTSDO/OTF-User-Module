package org.ihtsdo.otf.security.dto;

public abstract class OtfBaseAccountStore extends OtfBaseNameDesc {

	private OtfAccounts accounts = new OtfAccounts();

	@Override
	public String getHtmlForm(String formName) {
		return super.getHtmlForm(formName);
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

	@Override
	public void validateParams() {
		super.validateParams();
	}

	@Override
	public void setValsFromParams() {
		super.setValsFromParams();
	}

}

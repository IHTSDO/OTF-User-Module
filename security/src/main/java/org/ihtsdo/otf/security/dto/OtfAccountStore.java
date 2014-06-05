package org.ihtsdo.otf.security.dto;

import org.ihtsdo.otf.security.xml.XmlStatics;

public class OtfAccountStore extends OtfBaseNameDesc {

	private int priority = -1;
	private boolean defaultGroupStore;
	private boolean defaultAccountStore;
	private String type;

	private OtfAccounts accounts = new OtfAccounts();

	public OtfAccountStore() {
		super();
	}

	public final OtfAccounts getAccounts() {
		return accounts;
	}

	public final void setAccounts(final OtfAccounts accountsIn) {
		accounts = accountsIn;
	}

	public final int getPriority() {
		return priority;
	}

	public final void setPriority(final int priorityIn) {
		priority = priorityIn;
	}

	public final boolean isDefaultGroupStore() {
		return defaultGroupStore;
	}

	public final void setDefaultGroupStore(final boolean defaultGroupStoreIn) {
		defaultGroupStore = defaultGroupStoreIn;
	}

	public final boolean isDefaultAccountStore() {
		return defaultAccountStore;
	}

	public final void setDefaultAccountStore(final boolean defaultAccountStoreIn) {
		defaultAccountStore = defaultAccountStoreIn;
	}

	public final String getType() {
		if (type == null || type.length() == 0) {
			type = XmlStatics.DIR;
		}
		return type;
	}

	public final void setType(final String typeIn) {
		type = typeIn;
	}

	public final boolean isDir() {
		return getType().equals(XmlStatics.DIR);
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("AccountStore: ").append("Type:").append(type)
				.append(", Priority:").append(priority)
				.append(", DefaultGroupStore:").append(defaultGroupStore)
				.append(", DefaultAccountStore:").append(defaultAccountStore)
				.append(", ");
		sbuild.append(super.toString());
		sbuild.append(accounts.toString());
		return sbuild.toString();
	}

	@Override
	public void processParams() {

	}

	@Override
	public void validateParams() {
		super.validateParams();
	}

	@Override
	public void addTableRows() {
		super.addTableRows();

	}

	@Override
	public String getTableTitle() {
		if (isNew()) {
			return "Add New:";
		} else {
			return "Update:";
		}
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();
	}

	@Override
	public void setValsFromParams() {
		super.setValsFromParams();
	}

	@Override
	public String getHtmlForm(String formName) {
		return super.getHtmlForm(formName);
	}
}

package org.ihtsdo.otf.security.dto;

public class OtfAccountStore extends OtfBaseName {

	int priority = -1;
	boolean DefaultGroupStore;
	boolean DefaultAccountStore;

	public OtfAccountStore() {
		super();
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priorityIn) {
		priority = priorityIn;
	}

	public boolean isDefaultGroupStore() {
		return DefaultGroupStore;
	}

	public void setDefaultGroupStore(boolean defaultGroupStoreIn) {
		DefaultGroupStore = defaultGroupStoreIn;
	}

	public boolean isDefaultAccountStore() {
		return DefaultAccountStore;
	}

	public void setDefaultAccountStore(boolean defaultAccountStoreIn) {
		DefaultAccountStore = defaultAccountStoreIn;
	}

}

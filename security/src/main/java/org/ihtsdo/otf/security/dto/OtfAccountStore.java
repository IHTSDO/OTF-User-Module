package org.ihtsdo.otf.security.dto;

import org.ihtsdo.otf.security.xml.XmlStatics;

public class OtfAccountStore extends OtfBaseName {

	int priority = -1;
	boolean DefaultGroupStore;
	boolean DefaultAccountStore;
	String type;

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

	public String getType() {
		if (type == null || type.length() == 0) {
			type = XmlStatics.DIR;
		}
		return type;
	}

	public void setType(String typeIn) {
		type = typeIn;
	}

	public boolean isDir() {
		return getType().equals(XmlStatics.DIR);
	}

}

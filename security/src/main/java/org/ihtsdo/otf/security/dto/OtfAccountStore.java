package org.ihtsdo.otf.security.dto;

import org.ihtsdo.otf.security.xml.XmlStatics;

public class OtfAccountStore extends OtfBaseName {

	private int priority = -1;
	private boolean defaultGroupStore;
	private boolean defaultAccountStore;
	private String type;

	public OtfAccountStore() {
		super();
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

}

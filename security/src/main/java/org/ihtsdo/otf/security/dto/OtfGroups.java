package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfGroups {
	private final HashMap<String, OtfGroup> groups = new HashMap<String, OtfGroup>();

	public OtfGroups() {
		super();
	}

	public final HashMap<String, OtfGroup> getGroups() {
		return groups;
	}

	public final OtfGroup getGroupByName(final String name) {
		return groups.get(name);
	}

}

package org.ihtsdo.otf.security.dto;

import java.util.HashMap;
import java.util.Map;

public class OtfGroups {
	private final Map<String, OtfGroup> groups = new HashMap<String, OtfGroup>();

	public OtfGroups() {
		super();
	}

	public final Map<String, OtfGroup> getGroups() {
		return groups;
	}

	public final OtfGroup getGroupByName(final String name) {
		return groups.get(name);
	}

}

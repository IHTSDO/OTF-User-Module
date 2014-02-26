package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfGroups {
	private final HashMap<String, OtfGroup> groups = new HashMap<String, OtfGroup>();

	public OtfGroups() {
		super();
	}

	public HashMap<String, OtfGroup> getGroups() {
		return groups;
	}

}

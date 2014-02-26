package org.ihtsdo.otf.security.dto;

public class OtfDirectory extends OtfBaseAccountStore {

	public OtfDirectory() {
		super();
	}

	private OtfGroups groups = new OtfGroups();

	public OtfGroups getGroups() {
		return groups;
	}

	public void setGroups(OtfGroups groupsIn) {
		groups = groupsIn;
	}

}

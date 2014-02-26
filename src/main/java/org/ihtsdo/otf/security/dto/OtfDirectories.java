package org.ihtsdo.otf.security.dto;

import java.util.HashMap;

public class OtfDirectories {

	private HashMap<String, OtfDirectory> directories = new HashMap<String, OtfDirectory>();

	public OtfDirectories() {
		super();

	}

	public HashMap<String, OtfDirectory> getDirectories() {
		return directories;
	}

	public void setDirectories(HashMap<String, OtfDirectory> directoriesIn) {
		directories = directoriesIn;
	}

}

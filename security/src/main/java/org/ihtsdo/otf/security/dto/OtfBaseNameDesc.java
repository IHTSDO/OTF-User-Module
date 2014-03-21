package org.ihtsdo.otf.security.dto;

public abstract class OtfBaseNameDesc extends OtfBaseName {

	private String description;

	public final String getDescription() {
		return description;
	}

	public final void setDescription(final String descriptionIn) {
		description = descriptionIn;
	}

}

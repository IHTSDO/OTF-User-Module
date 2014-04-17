package org.ihtsdo.otf.security.dto;

public abstract class OtfBaseNameDesc extends OtfBaseName {

	private String description;

	public final String getDescription() {
		if (description == null) {
			description = "";
		}
		return description;
	}

	public final void setDescription(final String descriptionIn) {
		description = descriptionIn;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(super.toString());
		sbuild.append(", Description:").append(description);
		return sbuild.toString();

	}

}

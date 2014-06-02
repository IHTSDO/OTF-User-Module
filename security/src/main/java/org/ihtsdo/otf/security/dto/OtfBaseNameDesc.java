package org.ihtsdo.otf.security.dto;


public abstract class OtfBaseNameDesc extends OtfBaseName {

	private String description;
	public static final String DESC_NAME = "Description:";

	@Override
	public String getHtmlForm(String formName) {
		return super.getHtmlForm(formName);
	}

	@Override
	public void addTableRows() {
		super.addTableRows();
		getTableRows().add(
				getHtmlRowTextArea(DESC_NAME, getDescription(), DESC_NAME));
	}

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

	@Override
	public void processParams() {

	}

	@Override
	public void validateParams() {
		super.validateParams();

	}

	@Override
	public void setValsFromParams() {
		super.setValsFromParams();
		setDescription(getNotNullParam(DESC_NAME));
	}

}

package org.ihtsdo.otf.security.web.dto;

import java.util.Map;

public class WebDtoMember extends WebDtoBase {

	private String name;
	private String description;

	public final String getName() {
		return name;
	}

	public final void setName(String nameIn) {
		name = nameIn;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String descriptionIn) {
		description = descriptionIn;
	}

	@Override
	public String getHtmlForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processParams(Map<String, String> paramsIn) {
		// TODO Auto-generated method stub

	}

}

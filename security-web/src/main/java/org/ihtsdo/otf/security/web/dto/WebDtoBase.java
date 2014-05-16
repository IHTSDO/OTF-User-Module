package org.ihtsdo.otf.security.web.dto;

import java.util.Map;

public abstract class WebDtoBase {

	private String id;
	private Map<String, String> params;

	public abstract String getHtmlForm();

	public abstract void processParams(Map<String, String> params);

	public final String getId() {
		return id;
	}

	public final void setId(String idIn) {
		id = idIn;
	}

	public final Map<String, String> getParams() {
		return params;
	}

	public final void setParams(Map<String, String> paramsIn) {
		params = paramsIn;
		processParams(params);
	}

}

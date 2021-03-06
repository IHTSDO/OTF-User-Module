package org.ihtsdo.otf.security.dto;

import java.util.UUID;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class OtfBaseId extends OtfBaseWeb {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfBaseName.class
			.getName());

	public static final String ID_KEY = "BaseWebId";

	@JsonIgnore
	private String idref;
	private String id;

	public final String getIdref() {
		if (!stringOK(idref)) {
			idref = getIdIfSet();
		}
		return idref;
	}

	public final void setIdref(String idrefIn) {
		idref = idrefIn;
		if (stringOK(idref)) {
			setId(idref);
		}
	}

	@JsonIgnore
	public boolean isNew() {
		return !stringOK(id);
	}

	@JsonIgnore
	public final String getIdIfSet() {
		if (isNew()) {
			return "";
		} else {
			return getId();
		}
	}

	@JsonIgnore
	public final String getId() {
		if (!stringOK(id)) {
			id = new StringBuilder().append(UUID.randomUUID().toString())
					.toString();
		}
		return id;
	}

	public final void setId(String idIn) {
		id = idIn;
	}

	@Override
	public void addHiddenRows() {
		// first add the name of the class
		getHiddenRows().put(INPUT_KEY_NAME,
				getHtmlInputHidden(INPUT_KEY_NAME, getInputKey()));
		// add id if set
		getHiddenRows().put(ID_KEY, getHtmlInputHidden(ID_KEY, getIdIfSet()));
	}

	@JsonIgnore
	public String getInputKey() {
		return getClass().getName();
	}

	@Override
	public void setValsFromParams() {
		setId(getNotNullParam(ID_KEY));
	}
}

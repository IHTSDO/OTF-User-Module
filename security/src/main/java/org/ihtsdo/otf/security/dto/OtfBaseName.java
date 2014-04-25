package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class OtfBaseName extends OtfBaseWeb {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfBaseName.class
			.getName());

	protected String name;
	private Status status;
	@JsonIgnore
	private String idref;
	private String id;
	private List<String> statvals;

	public static final String NAME_NAME = "Name:";
	public static final String STATUS_NAME = "Status:";

	@Override
	public String getHtmlForm() {
		return super.getHtmlForm();
	}

	@Override
	public void addTableRows() {

		getTableRows().add(getHtmlRowTextInput(NAME_NAME, getName()));
		getTableRows().add(
				getHtmlRowOptions(STATUS_NAME, getStatvals(), getStatus()
						.toString(), STATUS_NAME));
	}

	@Override
	public void addHiddenRows() {

		// first add the name of the class
		getHiddenRows().add(
				getHtmlInputHidden(getClass().getName(), INPUT_KEY_NAME));
		// add id if set
		getHiddenRows().add(getHtmlInputHidden(getIdIfSet(), "id"));
	}

	public String getName() {
		if (name == null) {
			name = "";
		}
		return name;
	}

	public final void setName(final String nameIn) {
		name = nameIn;
	}

	public final Status getStatus() {
		if (status == null) {
			status = Status.ENABLED;
		}
		return status;
	}

	// public void setStatus(Status statusIn) {
	// status = statusIn;
	// }

	public final void setStatus(final String statusIn) {
		if (statusIn != null && statusIn.length() > 0) {
			try {
				status = Status.valueOf(statusIn);
			} catch (IllegalArgumentException iae) {
				// Ignore as will be set to enabled by default
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Name:").append(name).append(", idRef:")
				.append(getIdref()).append(", id:").append(getId())
				.append(", Status:").append(status);
		return sbuild.toString();
	}

	public final String getIdref() {
		if (idref == null) {
			idref = "";
		}
		return idref;
	}

	public final void setIdref(String idrefIn) {
		idref = idrefIn;
	}

	public boolean isNew() {
		return id == null;
	}

	public final String getIdIfSet() {
		if (isNew()) {
			return "";
		} else {
			return getId();
		}
	}

	public final String getId() {
		if (id == null) {
			id = new StringBuilder().append(UUID.randomUUID().toString())
					.toString();
		}
		return id;
	}

	public final void setId(String idIn) {
		id = idIn;
	}

	public final List<String> getStatvals() {
		if (statvals == null) {

			statvals = new ArrayList<String>();
			statvals.add(Status.DISABLED.toString());
			statvals.add(Status.ENABLED.toString());
			// statvals.add(Status.UNVERIFIED.toString());
		}
		return statvals;
	}

	public final void setStatvals(List<String> statvalsIn) {
		statvals = statvalsIn;
	}

}

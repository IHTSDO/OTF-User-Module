package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class OtfBaseName extends OtfBaseId implements
		Comparable<OtfBaseName> {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfBaseName.class
			.getName());

	protected String name;
	private Status status;

	private List<String> statvals;

	public static final String NAME_NAME = "Name:";
	public static final String STATUS_NAME = "Status:";

	@Override
	public String getHtmlForm(String formName) {
		return super.getHtmlForm(formName);
	}

	@Override
	public void addTableRows() {

		String nameRow = getHtmlRowTextInput(NAME_NAME, getName(), getErrors()
				.get(NAME_NAME));

		if (!isNew()) {
			// Set to disabled
			StringBuilder sbuild = new StringBuilder();
			String toRepl = CSS_TEXT_INPUT + "\"";
			String repl = sbuild.append(toRepl).append(INPUT_DISABLE)
					.toString();
			nameRow = nameRow.replace(toRepl, repl);
		}

		getTableRows().add(nameRow);
		getTableRows().add(
				getHtmlRowOptions(STATUS_NAME, getStatvals(), getStatus()
						.toString(), STATUS_NAME, null, null));
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();
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
		if (status.equals(Status.UNVERIFIED)) {
			return Status.DISABLED;
		}
		return status;
	}

	// public void setStatus(Status statusIn) {
	// status = statusIn;
	// }

	public final void setStatus(final String statusIn) {
		if (statusIn != null && statusIn.length() > 0) {
			try {
				// Filter out Unverified which is not to be set/unset by this
				// app but instead via email to Stormpath etc.
				if (!status.equals(Status.UNVERIFIED)) {
					status = Status.valueOf(statusIn);
				}
			} catch (IllegalArgumentException iae) {
				// Ignore as will be set to enabled by default
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Name:").append(name).append(", idRef:")
				.append(getIdref()).append(", id:").append(getIdIfSet())
				.append(", Status:").append(status);
		return sbuild.toString();
	}

	@JsonIgnore
	public final List<String> getStatvals() {
		if (statvals == null) {

			statvals = new ArrayList<String>();
			statvals.add(Status.DISABLED.toString());
			statvals.add(Status.ENABLED.toString());
		}
		return statvals;
	}

	public final void setStatvals(List<String> statvalsIn) {
		statvals = statvalsIn;
	}

	@Override
	public int compareTo(OtfBaseName toComp) {
		return getName().toLowerCase()
				.compareTo(toComp.getName().toLowerCase());
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof OtfBaseName))
			return false;
		if (obj == this)
			return true;
		// check class
		if (!getClass().getName().equals(obj.getClass().getName())) {
			return false;
		}
		OtfBaseName rhs = (OtfBaseName) obj;
		return new EqualsBuilder().append(name, rhs.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(name).toHashCode();
	}

	@Override
	public void setValsFromParams() {
		super.setValsFromParams();
		setName(getNotNullParam(NAME_NAME));
		setStatus(getNotNullParam(STATUS_NAME));
	}

	@Override
	public void processParams() {

	}

	@Override
	public void validateParams() {
		// Check Name not empty
		checkWebFieldNotEmpty(getNotNullParam(NAME_NAME), NAME_NAME);
	}

}

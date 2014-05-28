package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class OtfBaseName extends OtfBaseId implements
		Comparable<OtfBaseName> {

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

		getTableRows().add(
				getHtmlRowTextInput(NAME_NAME, getName(),
						getErrors().get(NAME_NAME)));
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
			// statvals.add(Status.UNVERIFIED.toString());
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
	public void setValsFromParams() {
		// TODO Auto-generated method stub

	}

}

package org.ihtsdo.otf.security.dto;

import java.util.Map;

public class OftAccountMin extends OtfBaseName {

	private String email;
	private String givenName;
	private String middleName;
	private String surname;

	public static final String EMAIL_NAME = "Email:";
	public static final String GIVEN_NAME = "Given Name:";
	public static final String MID_NAME = "Middle Name:";
	public static final String SUR_NAME = "Surname:";

	public OftAccountMin() {
		super();
	}

	public OftAccountMin(final OftAccountMin orig) {
		super();
		name = orig.getName();
		email = orig.getEmail();
		givenName = orig.getGivenName();
		middleName = orig.getMiddleName();
		surname = orig.getSurname();
		setIdref(orig.getIdref());
		setId(orig.getId());
		setStatus(orig.getStatus().toString());
	}

	@Override
	public final String getName() {
		if (name == null || name.length() == 0) {
			name = getEmail();
		}
		return name;
	}

	public final String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public final void setEmail(final String emailIn) {
		email = emailIn;
	}

	public final String getGivenName() {
		if (givenName == null || givenName.length() == 0) {
			givenName = "NoGivenNameSet";
		}
		return givenName;
	}

	public final void setGivenName(final String givenNameIn) {
		givenName = givenNameIn;
	}

	public final String getMiddleName() {
		if (middleName == null) {
			middleName = "";
		}
		return middleName;
	}

	public final void setMiddleName(final String middleNameIn) {
		middleName = middleNameIn;
	}

	public final String getSurname() {
		if (surname == null || surname.length() == 0) {
			surname = getName();
		}
		return surname;
	}

	public final void setSurname(final String surnameIn) {
		surname = surnameIn;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("idRef:").append(getIdref()).append(", GivenName:")
				.append(givenName).append(", MiddleName:").append(middleName)
				.append(", Surname:").append(surname).append(", Email:")
				.append(email).append(", Status:")
				.append(getStatus().toString());
		return sbuild.toString();

	}

	@Override
	public void processParams(Map<String, String> paramsIn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableRows() {
		super.addTableRows();
		getTableRows().add(getHtmlRowTextInput(EMAIL_NAME, getEmail()));
		getTableRows().add(getHtmlRowTextInput(GIVEN_NAME, getGivenName()));
		getTableRows().add(getHtmlRowTextInput(MID_NAME, getMiddleName()));
		getTableRows().add(getHtmlRowTextInput(SUR_NAME, getSurname()));

	}

	@Override
	public String getTableTitle() {
		if (isNew()) {
			return "Add new User";
		} else {
			return "Update user " + getName();
		}
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();

	}

}

package org.ihtsdo.otf.security.dto;

import java.util.List;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldBasic;

public class OtfAccountMin extends OtfBaseName {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfAccountMin.class
			.getName());

	private String email;
	private String givenName;
	private String middleName;
	private String surname;

	private String authToken = "";
	private boolean auth;

	private final long expirytime = -1;
	// 16 hours
	private static final long TTL = 57600000;
	// 5 minutes
	// private static final long TTL = 300000;

	public static final String EMAIL_NAME = "Email:";
	public static final String GIVEN_NAME = "Given Name:";
	public static final String MID_NAME = "Middle Name:";
	public static final String SUR_NAME = "Surname:";

	public OtfAccountMin() {
		super();
	}

	public OtfAccountMin(final OtfAccountMin orig) {
		super();
		name = orig.getName();
		email = orig.getEmail();
		givenName = orig.getGivenName();
		middleName = orig.getMiddleName();
		surname = orig.getSurname();
		setIdref(orig.getIdref());
		setId(orig.getId());
		setStatus(orig.getStatus().toString());
		setAuthToken(orig.getAuthToken());
		setAuth(orig.isAuth());

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
		if (givenName == null) {
			givenName = "";
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
		if (surname == null) {
			surname = "";
		}
		return surname;
	}

	public final void setSurname(final String surnameIn) {
		surname = surnameIn;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(super.toString());
		sbuild.append("idRef:").append(getIdref()).append(", GivenName:")
				.append(givenName).append(", MiddleName:").append(middleName)
				.append(", Surname:").append(surname).append(", Email:")
				.append(email).append(", Status:")
				.append(getStatus().toString());
		return sbuild.toString();

	}

	@Override
	public void processParams() {
		resetErrors();
		validateParams();
		if (isNew()) {
			// update the vals as object can be dumped
			setValsFromParams();
		} else {
			// If no errors then update
			if (errors.isEmpty()) {
				LOG.info("Before " + this.toString());
				setValsFromParams();
				LOG.info("After " + this.toString());
			}
		}

	}

	@Override
	public void validateParams() {
		super.validateParams();
		OtfCustomFieldBasic cfb = new OtfCustomFieldBasic();
		// Check if changed
		String nameIn = getNotNullParam(NAME_NAME);
		// Only check if changed
		if (!nameIn.equals(getName())) {
			List<String> uNames = cfb.getUsersList();
			checkWebFieldInList(nameIn, NAME_NAME, uNames, false,
					"Name must be unique");
		}
		// Email
		String emailIn = getNotNullParam(EMAIL_NAME);
		checkWebFieldNotEmpty(emailIn, EMAIL_NAME);
		if (!emailIn.equals(getEmail())) {

			List<String> emailAddr = cfb.getUserEmailList();
			checkWebFieldInList(emailIn, EMAIL_NAME, emailAddr, false,
					"Email must be unique");
		}
	}

	@Override
	public void addTableRows() {
		super.addTableRows();
		getTableRows().add(
				getHtmlRowTextInput(EMAIL_NAME, getEmail(),
						getErrors().get(EMAIL_NAME)));
		getTableRows().add(
				getHtmlRowTextInput(GIVEN_NAME, getGivenName(), getErrors()
						.get(GIVEN_NAME)));
		getTableRows().add(
				getHtmlRowTextInput(MID_NAME, getMiddleName(),
						getErrors().get(MID_NAME)));
		getTableRows().add(
				getHtmlRowTextInput(SUR_NAME, getSurname(),
						getErrors().get(SUR_NAME)));
	}

	@Override
	public void setValsFromParams() {
		super.setValsFromParams();
		setEmail(getNotNullParam(EMAIL_NAME));
		setGivenName(getNotNullParam(GIVEN_NAME));
		setMiddleName(getNotNullParam(MID_NAME));
		setSurname(getNotNullParam(SUR_NAME));
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

	@Override
	@JsonIgnore
	public String getHtmlForm(String formName) {
		return super.getHtmlForm(formName);
	}

	@JsonIgnore
	public final String getAuthToken() {
		// Temporary until Oauth etc.
		// Check expired
		// if (isExpired()) {
		// authToken = "";
		// setNewExpiryTime();
		// }
		// if (!stringOK(authToken)) {
		// authToken = UuidConverter.format(UUID.randomUUID());
		// }
		return authToken;
	}

	public final void setAuthToken(String authTokenIn) {
		authToken = authTokenIn;
	}

	public final String getToken() {
		if (isAuth()) {
			return getAuthToken();
		} else
			return "";
	}

	@JsonIgnore
	public final boolean isAuth() {
		return auth;
	}

	public final void setAuth(boolean authIn) {
		auth = authIn;
	}

	// @JsonIgnore
	// public final long getExpirytime() {
	// if (expirytime == -1) {
	// setNewExpiryTime();
	// }
	// return expirytime;
	// }
	//
	// public final void setExpiryTtl(long expirytimeIn) {
	// expirytime = expirytimeIn + TTL;
	// }
	//
	// public final void setExpirytime(long expirytimeIn) {
	// expirytime = expirytimeIn;
	// }
	//
	// private void setNewExpiryTime() {
	// setExpiryTtl(System.currentTimeMillis());
	// }

	// @JsonIgnore
	// private boolean isExpired() {
	// if (getExpirytime() > System.currentTimeMillis()) {
	// return false;
	// }
	// return true;
	// }

	@JsonIgnore
	public boolean checkAuthToken(String token) {
		if (token.equals(authToken)) {
			setAuth(true);
			getAuthToken();
			return true;
		}
		setAuth(false);
		return false;
	}
}

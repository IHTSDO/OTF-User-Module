package org.ihtsdo.otf.security.dto;

import java.util.logging.Logger;

public class OtfCustomFieldApplication {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(OtfCustomFieldApplication.class.getName());

	private String[] vals;

	private String encodedVal;

	private String app;
	private String role;
	private String member;

	public OtfCustomFieldApplication(String[] valsIn) {
		super();
		setVals(valsIn);
	}

	public String getEncodedVal() {
		if (encodedVal == null || encodedVal.length() == 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(getApp()).append(OtfCustomField.SEP).append(getRole())
					.append(OtfCustomField.SEP).append(getMember());
		}
		return encodedVal;
	}

	public void setEncodedVal(final String encodedValIn) {
		encodedVal = encodedValIn;
		String[] vals = encodedVal.split(OtfCustomField.SEP);
		int len = vals.length;
		if (len < 2) {
			// problem
			LOG.severe("Not Enough separated Values encodedVal = " + encodedVal);
		}
		if (len == 2) {
			// Member not set assume *
			setApp(vals[0]);
			setRole(vals[1]);
			setMember("*");
		}
		if (len > 2) {
			setApp(vals[0]);
			setRole(vals[1]);
			setMember(vals[2]);
		}
	}

	public String getApp() {
		if (app == null) {
			app = "";
		}
		return app;
	}

	public void setApp(String appIn) {
		app = appIn;
	}

	public String getRole() {
		if (role == null) {
			role = "";
		}
		return role;
	}

	public void setRole(String roleIn) {
		role = roleIn;
	}

	public String getMember() {
		if (member == null || member.length() == 0) {
			member = "*";
		}
		return member;
	}

	public void setMember(String memberIn) {
		member = memberIn;
	}

	public String[] getVals() {

		if (vals == null || vals.length == 0) {
			vals = new String[4];
			vals[0] = OtfCustomField.CustomType.APP.name();
			vals[1] = getApp();
			vals[2] = getRole();
			vals[3] = getMember();
		}
		return vals;
	}

	public void setVals(String[] valsIn) {
		vals = valsIn;
		setApp(vals[1]);
		setRole(vals[2]);
		if (vals.length > 2) {
			setMember(vals[3]);
		}
	}

}

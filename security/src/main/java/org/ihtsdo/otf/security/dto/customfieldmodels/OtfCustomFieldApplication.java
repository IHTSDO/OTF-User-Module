package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldApplication extends OtfCustomFieldModel {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(OtfCustomFieldApplication.class.getName());

	private String app;
	private String role;
	private String member;

	public OtfCustomFieldApplication(String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.APP;
	}

	@Override
	public void model2Vals() {
		vals = new String[4];
		vals[0] = getType().name();
		vals[1] = getApp();
		vals[2] = getRole();
		vals[3] = getMember();

	}

	@Override
	public void modelFromVals() {
		setApp(vals[1]);
		setRole(vals[2]);
		if (vals.length > 2) {
			setMember(vals[3]);
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

}

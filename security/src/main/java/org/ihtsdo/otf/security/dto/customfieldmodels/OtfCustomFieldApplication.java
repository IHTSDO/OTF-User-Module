package org.ihtsdo.otf.security.dto.customfieldmodels;

import org.ihtsdo.otf.security.dto.OtfCustomField;

public class OtfCustomFieldApplication extends OtfCustomFieldModel {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	// private static final Logger LOG = Logger
	// .getLogger(OtfCustomFieldApplication.class.getName());

	private String app;
	private String role;
	private String member;

	public OtfCustomFieldApplication(final String[] valsIn) {
		super(valsIn);
		type = OtfCustomField.CustomType.APP;
	}

	@Override
	public final void model2Vals() {
		vals = new String[4];
		vals[0] = getType().name();
		vals[1] = getApp();
		vals[2] = getRole();
		vals[3] = getMember();

	}

	@Override
	public final void modelFromVals() {
		setApp(vals[1]);
		setRole(vals[2]);
		if (vals.length > 2) {
			setMember(vals[3]);
		}
	}

	public final String getApp() {
		if (app == null) {
			app = "";
		}
		return app;
	}

	public final void setApp(final String appIn) {
		app = appIn;
	}

	public final String getRole() {
		if (role == null) {
			role = "";
		}
		return role;
	}

	public final void setRole(final String roleIn) {
		role = roleIn;
	}

	public final String getMember() {
		if (member == null || member.length() == 0) {
			member = "*";
		}
		return member;
	}

	public final void setMember(final String memberIn) {
		member = memberIn;
	}

}

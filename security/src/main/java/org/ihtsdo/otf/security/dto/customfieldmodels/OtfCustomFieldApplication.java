package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.HashMap;
import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

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

	public OtfCustomFieldApplication() {
		super();

	}

	public OtfCustomFieldApplication(final String keyIn, final String[] valsIn) {
		super(keyIn, valsIn);

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

	@Override
	public String getCollectionTitle() {
		// TODO Auto-generated method stub
		return "Application Roles";
	}

	@Override
	public String getNewTitle() {
		// TODO Auto-generated method stub
		return "Add new Application Role";
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = new HashMap<String, String>();
		retval.put("App:", getHtmlTextInput("App", getApp()));
		retval.put("Role:", getHtmlTextInput("Role", getRole()));
		retval.put("Member:", getHtmlTextInput("KeyValVal", getMember()));
		return retval;
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.APP;
		return type;
	}

}

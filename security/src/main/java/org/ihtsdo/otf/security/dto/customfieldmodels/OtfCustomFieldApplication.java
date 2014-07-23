package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.Map;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.dto.OtfCustomData;
import org.ihtsdo.otf.security.dto.OtfCustomField;
import org.ihtsdo.otf.security.dto.OtfCustomField.CustomType;

public class OtfCustomFieldApplication extends OtfCustomFieldCachedVals {

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

	public static final String APP_ROLE_APP = "App";
	public static final String APP_ROLE_ROLE = "Role";
	public static final String APP_ROLE_MEMBER = "Member";

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
		return "Application Roles";
	}

	@Override
	public String getNewTitle() {
		return "Add new Application Role";
	}

	private final String getReplaceRoleJs(String roleName) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("replaceRole(this,").append("'")
				.append(HIDDEN_DIV_ROLES_ID).append("',").append("'")
				.append(".").append(OtfCustomData.cssClass).append("',")
				.append("'").append("select[name=").append(roleName)
				.append("]'").append(")");
		return sbuild.toString();
	}

	@Override
	public Map<String, String> getLabelValuesMap() {
		Map<String, String> retval = getStdLabelValuesMap();
		String id = getId();
		String appId = getUniqueControlName(APP_ROLE_APP, id);
		String roleId = getUniqueControlName(APP_ROLE_ROLE, id);
		String memId = getUniqueControlName(APP_ROLE_MEMBER, id);
		retval.put("App:",
				getAppsOptionsSelect(getApp(), getReplaceRoleJs(roleId), appId));
		retval.put("Role:",
				getRolesOptionsSelect(getRole(), getApp(), roleId, null));
		retval.put("Member:", getMemberOptionsSelect(getMember(), memId));
		return retval;
	}

	@Override
	public void valFromParamDTO(final OtfCustomFieldParamDTO ocfpIn) {
		switch (ocfpIn.getLocalKey()) {
		case APP_ROLE_APP:
			setApp((ocfpIn.getVal()));
			break;
		case APP_ROLE_ROLE:
			setRole((ocfpIn.getVal()));
			break;
		case APP_ROLE_MEMBER:
			setMember((ocfpIn.getVal()));
			break;
		default:
			LOG.info("valFromParamDTO localKey not found and = "
					+ ocfpIn.getLocalKey());
		}
	}

	@Override
	public CustomType getType() {
		type = OtfCustomField.CustomType.CD_TYPE_APP;
		return type;
	}

}

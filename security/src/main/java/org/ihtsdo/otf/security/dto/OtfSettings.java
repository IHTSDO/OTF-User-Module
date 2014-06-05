package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;
import org.ihtsdo.otf.security.dto.query.SecurityService;

public class OtfSettings extends OtfBaseId {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfSettings.class
			.getName());

	public final static String DEF_PW_LABEL = "Default Password:";
	public final static String USER_APP_LABEL = "Users Application:";
	public final static String MEMBER_APP_LABEL = "Members Application:";
	private Map<String, OtfCustomFieldSetting> settings;

	private String defPw;
	private String users;
	private String members;

	private OtfGroup grp;

	public OtfSettings() {
		super();
	}

	public OtfSettings(OtfGroup grpIn) {
		super();
		setGrp(grpIn);
		setSettings();
	}

	public void updateGrpSettings() {

		for (String key : getSettings().keySet()) {
			OtfCustomFieldSetting setF = getSettings().get(key);
			OtfCustomField ocf = getGrp().getCustData().getCustFields()
					.get(setF.getKey());
			ocf.setValsFromModelVals();
			ocf.setValueFromVals();

		}

		// LOG.info("updateGrpSettings grp = " + getGrp());

	}

	private void setSettings() {
		List<OtfCustomField> setList = getGrp().getCustData().getSettings();
		for (OtfCustomField cf : setList) {
			OtfCustomFieldSetting cfSet = (OtfCustomFieldSetting) cf.getModel();
			getSettings().put(cfSet.getKeyVal(), cfSet);
		}
		setFieldsFromSettings();
	}

	@Override
	public final void processParams() {
		// LOG.info("SETTINGS: ");
		// printParams();
		resetErrors();
		validateParams();
		// If no errors then update
		if (errors.size() == 0) {
			// LOG.info("Before " + this.toString());
			setValsFromParams();
			setSettingsFromFields();
			// LOG.info("After " + this.toString());
			updateGrpSettings();
		}

	}

	@Override
	public void validateParams() {
		String defpIn = getNotNullParam(DEF_PW_LABEL);
		String membIn = getNotNullParam(MEMBER_APP_LABEL);
		String usersIn = getNotNullParam(USER_APP_LABEL);

		// LOG.info(" Settings validate defp = " + defpIn);
		// LOG.info("membIn = " + membIn);
		// LOG.info("usersIn = " + usersIn);

		checkWebFieldNotEmpty(defpIn, DEF_PW_LABEL);
		checkWebFieldNotEmpty(membIn, MEMBER_APP_LABEL);
		checkWebFieldNotEmpty(usersIn, USER_APP_LABEL);

		// LOG.info("Errors 1 size = " + errors.size());

		// Check Password length etc
		// TODO: FInd out how & where StormPath Sets it's pw length
		// complexity/pattern etc & if this is available
		// Check memb app in List
		List<String> appNames = new OtfCustomFieldSetting().getAppNames();
		checkWebFieldInList(membIn, MEMBER_APP_LABEL, appNames, true,
				"Member App must exist as an application");
		// Check user app in list.
		checkWebFieldInList(usersIn, USER_APP_LABEL, appNames, true,
				"Users App must exist as an application");
		// LOG.info("Errors 2 size = " + errors.size());
	}

	@Override
	public void addTableRows() {

		// LOG.info("addTableRows getTableRows() size = " +
		// getTableRows().size());
		// LOG.info("addTableRows getTableRows() getErrors() size = "
		// + getErrors().size());
		getTableRows().add(
				getHtmlRowTextInput(DEF_PW_LABEL, getDefPw(),
						getErrors().get(DEF_PW_LABEL)));
		getTableRows().add(
				getHtmlRowTextInput(USER_APP_LABEL, getUsers(), getErrors()
						.get(USER_APP_LABEL)));
		getTableRows().add(
				getHtmlRowTextInput(MEMBER_APP_LABEL, getMembers(), getErrors()
						.get(MEMBER_APP_LABEL)));
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();
	}

	@Override
	public String getTableTitle() {
		return "Update Settings";
	}

	public final Map<String, OtfCustomFieldSetting> getSettings() {
		if (settings == null) {
			settings = new HashMap<String, OtfCustomFieldSetting>();
		}
		return settings;
	}

	public final void setSettings(Map<String, OtfCustomFieldSetting> settingsIn) {
		settings = settingsIn;
		setFieldsFromSettings();

	}

	private void setFieldsFromSettings() {
		defPw = getSettings().get(OtfCustomFieldSetting.DEFPW).getVal().trim();
		users = getSettings().get(OtfCustomFieldSetting.USERS).getVal().trim();
		members = getSettings().get(OtfCustomFieldSetting.MEMBERS).getVal()
				.trim();
	}

	public final void setSettingsFromFields() {
		getSettings().get(OtfCustomFieldSetting.DEFPW).updateVal(getDefPw());
		getSettings().get(OtfCustomFieldSetting.USERS).updateVal(getUsers());
		getSettings().get(OtfCustomFieldSetting.MEMBERS)
				.updateVal(getMembers());
	}

	public final String getDefPw() {
		if (defPw == null) {
			defPw = "";
		}
		return defPw;
	}

	public final void setDefPw(String defPwIn) {
		defPw = defPwIn;
	}

	public final String getUsers() {
		if (users == null) {
			users = "";
		}
		return users;
	}

	public final void setUsers(String usersIn) {
		users = usersIn;
	}

	public final String getMembers() {
		if (members == null) {
			members = "";
		}
		return members;
	}

	public final void setMembers(String membersIn) {
		members = membersIn;
	}

	@Override
	@JsonIgnore
	public String getInputKey() {
		return SecurityService.SETTINGS;
	}

	@Override
	public void setValsFromParams() {
		setDefPw(getNotNullParam(DEF_PW_LABEL));
		setMembers(getNotNullParam(MEMBER_APP_LABEL));
		setUsers(getNotNullParam(USER_APP_LABEL));
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Settings CustomFields:\n");
		List<String> keys = new ArrayList<String>(settings.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append("Cust Field Key = ").append(key).append("\n");
			sbuild.append(settings.get(key).toString()).append("\n");
		}
		return sbuild.toString();

	}

	public final OtfGroup getGrp() {

		return grp;
	}

	public final void setGrp(OtfGroup grpIn) {
		grp = grpIn;
		grp.setGrptype(OtfGroup.TYPE_SETTING);
	}

}

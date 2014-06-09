package org.ihtsdo.otf.security.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
	public final static String ADMIN_APP_LABEL = "Admin Application:";
	public final static String MOD_DATE_LABEL = "Last Modified Date:";
	private Map<String, OtfCustomFieldSetting> settings;

	private String defPw;
	private String users;
	private String members;
	private String admin;
	private String modDate;

	private DateFormat df = null;

	private OtfGroup grp;

	public OtfSettings() {
		super();
	}

	public OtfSettings(OtfGroup grpIn) {
		super();
		setGrp(grpIn);
		setSettings();
	}

	public void updateSettings() {
		setModDate("");
		updateGrpSettings();
	}

	public void updateGrpSettings() {

		for (String key : getSettings().keySet()) {
			// LOG.info("updateGrpSettings key = " + key);
			OtfCustomFieldSetting setF = getSettings().get(key);
			OtfCustomField ocf = getGrp().getCustData().getCustFields()
					.get(setF.getKey());

			if (ocf == null) {
				ocf = new OtfCustomField();
				ocf.setKey(setF.getKey());
				ocf.setModel(setF);
				getGrp().getCustData().getCustFields().put(setF.getKey(), ocf);
			}

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
		getTableRows().add(
				getHtmlRowTextInput(ADMIN_APP_LABEL, getAdmin(), getErrors()
						.get(ADMIN_APP_LABEL)));

		getTableRows().add(getHtmlRowPlainText(MOD_DATE_LABEL, getModDate()));
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
		defPw = getSetting(OtfCustomFieldSetting.DEFPW).getVal().trim();
		users = getSetting(OtfCustomFieldSetting.USERS).getVal().trim();
		members = getSetting(OtfCustomFieldSetting.MEMBERS).getVal().trim();

		admin = getSetting(OtfCustomFieldSetting.ADMIN).getVal().trim();
		modDate = getSetting(OtfCustomFieldSetting.MOD_DATE).getVal().trim();

	}

	public final void setSettingsFromFields() {
		getSetting(OtfCustomFieldSetting.DEFPW).updateVal(getDefPw());
		getSetting(OtfCustomFieldSetting.USERS).updateVal(getUsers());
		getSetting(OtfCustomFieldSetting.MEMBERS).updateVal(getMembers());

		getSetting(OtfCustomFieldSetting.ADMIN).updateVal(getAdmin());
		getSetting(OtfCustomFieldSetting.MOD_DATE).updateVal(getModDate());
	}

	private OtfCustomFieldSetting getSetting(String key) {
		OtfCustomFieldSetting rval = getSettings().get(key);
		if (rval == null) {
			rval = new OtfCustomFieldSetting();
			rval.setKeyVal(key);
			rval.setVal("");
			getSettings().put(key, rval);
		}

		return rval;
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
		setAdmin(getNotNullParam(ADMIN_APP_LABEL));
		setModDate("");
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

	public final String getAdmin() {
		// LOG.info("Admin = " + admin);
		return admin;
	}

	public final void setAdmin(String adminIn) {
		admin = adminIn;
	}

	public final String getModDate() {
		if (!stringOK(modDate)) {
			modDate = getDf().format(new Date());
		}
		return modDate;
	}

	public final void setModDate(String modDateIn) {
		modDate = modDateIn;
	}

	public final DateFormat getDf() {
		if (df == null) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
			df = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SS'Z'");
			df.setTimeZone(tz);
		}
		// "2014-04-01T17:00:09.189Z",
		return df;
	}

	public final void setDf(DateFormat dfIn) {
		df = dfIn;
	}

}

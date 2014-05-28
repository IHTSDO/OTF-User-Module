package org.ihtsdo.otf.security.dto;

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

	public OtfSettings() {
		super();

	}

	public OtfSettings(Map<String, OtfCustomFieldSetting> settingsIn) {
		super();
		setSettings(settingsIn);
	}

	@Override
	public Map<String, List<String>> processParams(Map<String, String> paramsIn) {
		LOG.info("SETTINGS: ");
		printParams();

		String defpIn = paramsIn.get(DEF_PW_LABEL);
		String membIn = paramsIn.get(MEMBER_APP_LABEL);
		String usersIn = paramsIn.get(USER_APP_LABEL);

		LOG.info("defp = " + defpIn);
		LOG.info("membIn = " + membIn);
		LOG.info("usersIn = " + usersIn);

		checkWebFieldNotEmpty(defpIn, DEF_PW_LABEL);
		checkWebFieldNotEmpty(membIn, MEMBER_APP_LABEL);
		checkWebFieldNotEmpty(usersIn, USER_APP_LABEL);

		LOG.info("Errors 1 size = " + errors.size());

		// Check Password length etc

		// Check memb in List

		List<String> appNames = new OtfCustomFieldSetting().getAppNames();

		checkWebFieldInList(membIn, MEMBER_APP_LABEL, appNames, true,
				"Member App must exist as an application");
		checkWebFieldInList(usersIn, USER_APP_LABEL, appNames, true,
				"Users App must exist as an application");
		// LOG.info("Errors 2 size = " + errors.size());
		return errors;
	}

	@Override
	public void addTableRows() {

		LOG.info("addTableRows getTableRows() size = " + getTableRows().size());
		LOG.info("addTableRows getTableRows() getErrors() size = "
				+ getErrors().size());
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
		return settings;
	}

	public final void setSettings(Map<String, OtfCustomFieldSetting> settingsIn) {
		settings = settingsIn;

		defPw = getSettings().get(OtfCustomFieldSetting.DEFPW).getVal().trim();

		users = getSettings().get(OtfCustomFieldSetting.USERS).getVal().trim();
		members = getSettings().get(OtfCustomFieldSetting.MEMBERS).getVal()
				.trim();
	}

	public final void setSettingsFromFields() {
		settings.put(OtfCustomFieldSetting.DEFPW, getDefPwField());
		settings.put(OtfCustomFieldSetting.USERS, getUsersField());
		settings.put(OtfCustomFieldSetting.MEMBERS, getMemberField());
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

	public final OtfCustomFieldSetting getDefPwField() {
		OtfCustomFieldSetting ocfs = new OtfCustomFieldSetting();
		ocfs.setKey(OtfCustomFieldSetting.DEFPW);
		ocfs.setVal(getDefPw());
		return ocfs;
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

	public final OtfCustomFieldSetting getUsersField() {
		OtfCustomFieldSetting ocfs = new OtfCustomFieldSetting();
		ocfs.setKey(OtfCustomFieldSetting.USERS);
		ocfs.setVal(getUsers());
		return ocfs;
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

	public final OtfCustomFieldSetting getMemberField() {
		OtfCustomFieldSetting ocfs = new OtfCustomFieldSetting();
		ocfs.setKey(OtfCustomFieldSetting.MEMBERS);
		ocfs.setVal(getMembers());
		return ocfs;
	}

	@Override
	@JsonIgnore
	public String getInputKey() {
		return SecurityService.SETTINGS;
	}

	@Override
	public void setValsFromParams() {
		// TODO Auto-generated method stub

	}

}

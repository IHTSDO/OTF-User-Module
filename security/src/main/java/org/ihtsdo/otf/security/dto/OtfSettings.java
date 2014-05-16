package org.ihtsdo.otf.security.dto;

import java.util.Map;

import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldSetting;

public class OtfSettings extends OtfBaseId {

	public final static String DEF_PW_LABEL = "Default Password:";
	public final static String USER_APP_LABEL = "Users Application:";
	public final static String MEMBER_APP_LABEL = "Members Application:";
	private Map<String, OtfCustomFieldSetting> settings;

	private String defPw;
	private String users;
	private String members;

	public OtfSettings(Map<String, OtfCustomFieldSetting> settingsIn) {
		super();
		setSettings(settingsIn);
	}

	@Override
	public void processParams(Map<String, String> paramsIn) {

	}

	@Override
	public void addTableRows() {

		getTableRows().add(getHtmlRowTextInput(DEF_PW_LABEL, getDefPw()));
		getTableRows().add(getHtmlRowTextInput(USER_APP_LABEL, getUsers()));
		getTableRows().add(getHtmlRowTextInput(MEMBER_APP_LABEL, getMembers()));

	}

	@Override
	public void addHiddenRows() {
		// TODO Auto-generated method stub

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

}

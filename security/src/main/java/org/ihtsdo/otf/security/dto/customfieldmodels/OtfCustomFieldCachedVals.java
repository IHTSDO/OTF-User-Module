package org.ihtsdo.otf.security.dto.customfieldmodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ihtsdo.otf.security.dto.OtfAccount;
import org.ihtsdo.otf.security.dto.OtfCachedListsDTO;

public abstract class OtfCustomFieldCachedVals extends OtfCustomFieldModel {

	// private OtfCachedListsDTO cache;
	private List<String> members;
	private Map<String, List<String>> appsMap;
	private Map<String, List<String>> dirsMap;
	private List<String> appsNotUserMembers;
	private List<String> usersList;
	private List<String> userEmailList;

	public static final String HIDDEN_DIV_ROLES_ID = "hiddenR0lesId";

	public OtfCustomFieldCachedVals() {
		super();
	}

	public OtfCustomFieldCachedVals(final String keyIn, final String[] valsIn) {
		super(keyIn, valsIn);
	}

	public final List<String> getMembers() {
		if (members == null) {
			members = OtfCachedListsDTO.getMembersList();
		}
		return members;
	}

	public String getMemberOptionsSelect(String selVal, String controlName) {
		return getObw().getHtmlRowOptions(null, getMembers(), selVal,
				controlName, null, null);
	}

	public String getRolesOptionsSelect(String selVal, String appName,
			String controlName, String id) {
		return getObw().getHtmlRowOptions(null, getRolesByApp(appName), selVal,
				controlName, null, id);
	}

	public final Map<String, List<String>> getAppsMap() {
		if (appsMap == null) {
			appsMap = OtfCachedListsDTO.getAppsMap();
		}
		return appsMap;
	}

	public final Map<String, List<String>> getDirsMap() {
		if (dirsMap == null) {
			dirsMap = OtfCachedListsDTO.getDirsMap();
		}
		return dirsMap;
	}

	public final List<String> getRolesByApp(String appName) {
		List<String> roles = null;
		if (stringOK(appName)) {
			roles = getAppsMap().get(appName);
		}
		if (roles == null) {
			roles = new ArrayList<String>();
		}
		return roles;
	}

	public final List<String> getRolesByDir(String dirName) {
		List<String> roles = null;
		if (stringOK(dirName)) {
			roles = getDirsMap().get(dirName);
		}
		if (roles == null) {
			roles = new ArrayList<String>();
		}
		return roles;
	}

	public String getAppsOptionsSelect(String selVal, String onChangeJS,
			String controlName) {
		return getObw().getHtmlRowOptions(null, getAppsNotUserMembers(),
				selVal, controlName, onChangeJS, null);
	}

	public final List<String> getAppNames() {
		List<String> names = new ArrayList<String>();
		names.addAll(getAppsMap().keySet());
		Collections.sort(names);
		return names;

	}

	public final List<String> getAppsNotUserMembers() {
		if (appsNotUserMembers == null) {
			appsNotUserMembers = OtfCachedListsDTO.getAppsNotUserMemberList();
		}
		return appsNotUserMembers;
	}

	public final String getHiddenDivRoleOptions() {
		StringBuilder sbuild = new StringBuilder();

		for (String app : getAppsNotUserMembers()) {
			sbuild.append(
					(getRolesOptionsSelect("", app, app, app
							+ HIDDEN_DIV_ROLES_ID))).append("\n");
		}
		return getObw().getHiddenDiv(sbuild.toString(), HIDDEN_DIV_ROLES_ID);
	}

	public String getAdminServletContextURL() {
		return OtfCachedListsDTO.getAdminServletContextUrl();
	}

	@Override
	public String getNewFormURLWithContext() {
		StringBuilder sbuild = new StringBuilder();
		return sbuild.append(getAdminServletContextURL())
				.append(getNewFormURL()).toString();

	}

	public final List<String> getUsersList() {
		if (usersList == null) {
			usersList = new ArrayList<String>(OtfCachedListsDTO
					.getAllAccountsMap().keySet());
		}
		return usersList;
	}

	public final List<String> getUserEmailList() {
		if (userEmailList == null) {
			userEmailList = new ArrayList<String>();
			for (OtfAccount acc : OtfCachedListsDTO.getAllAccountsMap()
					.values()) {
				userEmailList.add(acc.getEmail());
			}
		}
		return userEmailList;
	}

}

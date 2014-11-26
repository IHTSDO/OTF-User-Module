package org.ihtsdo.otf.security.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldBasic;
import org.ihtsdo.otf.security.dto.query.SecurityService;

public class OtfDirectory extends OtfAccountStore {

	private Map<String, OtfAccount> allAccounts;

	public OtfDirectory() {
		super();
	}

	private OtfGroups groups = new OtfGroups();

	public final OtfGroups getGroups() {
		return groups;
	}

	public final void setGroups(final OtfGroups groupsIn) {
		groups = groupsIn;
	}

	public final Map<String, OtfAccount> getDirAccounts() {
		return getAccounts().getAccounts();
	}

	public final OtfAccount getAccountByName(final String name) {
		return getAllAccounts().get(name);
	}

	public final Map<String, OtfAccount> getAllAccounts() {
		if (allAccounts == null) {
			allAccounts = new HashMap<String, OtfAccount>();
		}
		if (getGroups().getGroups().isEmpty()) {
			allAccounts = getDirAccounts();
		}
		if (!getGroups().getGroups().isEmpty()) {
			for (String key : getDirAccounts().keySet()) {
				allAccounts.put(key, getDirAccounts().get(key));
			}
			// Add Any Accounts in child groups.
			for (OtfGroup grp : getGroups().getGroups().values()) {

				for (String key : grp.getAccounts().getAccounts().keySet()) {
					allAccounts.put(key,
							grp.getAccounts().getAccounts().get(key));
				}
			}
		}
		return allAccounts;
	}

	public final void setAllAccounts(final Map<String, OtfAccount> allAccountsIn) {
		allAccounts = allAccountsIn;
	}

	@Override
	public final String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Directory:\n");
		sbuild.append(super.toString());
		sbuild.append(groups.toString());
		return sbuild.toString();

	}

	@Override
	public final void processParams() {
		// printParams();
		resetErrors();
		validateParams();

		// If no errors then update
		if (errors.isEmpty()) {
			setValsFromParams();
		}
	}

	@Override
	public final void validateParams() {
		resetErrors();
		super.validateParams();
		OtfCustomFieldBasic cfb = new OtfCustomFieldBasic();
		// Check if changed
		String nameIn = getNotNullParam(NAME_NAME);
		// Only check if changed
		if (!nameIn.equals(getName())) {
			List<String> appNames = cfb.getAppNames();
			checkWebFieldInList(nameIn, NAME_NAME, appNames, false,
					"Name must be unique");
		}

	}

	@Override
	public final String getTableTitle() {
		if (isNew()) {
			return "Add New Directory";
		} else {
			return "Update Directory:" + getName();
		}
	}

	@Override
	public final void addHiddenRows() {
		super.addHiddenRows();
	}

	@Override
	@JsonIgnore
	public final String getInputKey() {
		return SecurityService.DIR;
	}

	@Override
	public final void setValsFromParams() {
		super.setValsFromParams();
	}
}

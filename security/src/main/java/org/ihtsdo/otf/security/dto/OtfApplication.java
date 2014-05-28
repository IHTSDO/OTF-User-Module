package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.query.SecurityService;

public class OtfApplication extends OtfBaseNameDesc {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfApplication.class
			.getName());
	private final Map<String, OtfAccountStore> accountStores = new HashMap<String, OtfAccountStore>();

	public OtfApplication() {
		super();

	}

	public final Map<String, OtfAccountStore> getAccountStores() {
		return accountStores;
	}

	@Override
	public String toString() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(super.toString());
		List<String> keys = new ArrayList<String>(accountStores.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sbuild.append(accountStores.get(key).toString());
		}
		return sbuild.toString();

	}

	@Override
	public Map<String, List<String>> processParams(Map<String, String> paramsIn) {
		LOG.info("APPLICATION: ");
		printParams();

		return errors;
	}

	@Override
	public void addTableRows() {
		super.addTableRows();
		// getTableRows().add(getHtmlRowTextInput("Name:", getName()));

	}

	@Override
	public String getTableTitle() {
		if (isNew()) {
			return "Add New Application";
		} else {
			return "Update Application : " + getName();
		}
	}

	@Override
	public void addHiddenRows() {
		super.addHiddenRows();

	}

	@Override
	@JsonIgnore
	public String getInputKey() {
		return SecurityService.APPS;
	}

	@Override
	public void setValsFromParams() {
		// TODO Auto-generated method stub

	}

}

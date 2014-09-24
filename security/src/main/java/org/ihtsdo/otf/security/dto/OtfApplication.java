package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldBasic;
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
	public void processParams() {
		// printParams();
		resetErrors();
		validateParams();

		// If no errors then update
		if (errors.isEmpty()) {
			LOG.info("Before " + this.toString());
			setValsFromParams();
			LOG.info("After " + this.toString());
		}

	}

	@Override
	public void validateParams() {
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
	public void addTableRows() {
		super.addTableRows();
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
		super.setValsFromParams();
	}

}

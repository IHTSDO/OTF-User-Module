package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;
import java.util.logging.Logger;

import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.tenant.Tenant;

public class StormPathBaseDTO {

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(StormPathBaseDTO.class
			.getName());

	private Client client;
	private Tenant tenant;
	private String keyPath = "C:/Users/adamf/stormpath/apiKey.properties";
	private Properties apiProps;
	private OtfClientBuilder otfCb;

	// Static Strings
	/** The default Password. */
	public static String DEFPW = "defaultpw";
	/** The App containing the users. */
	public static String USERS_APP = "users";
	/** The App containing the members. */
	public static String MEMBERS_APP = "members";

	public void load() {
		client = getOtfCb().setApiKeyFileLocation(getKeyPath()).build();
		setApiProps(getOtfCb().getClientApiKeyProperties());
		tenant = client.getCurrentTenant();
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client clientIn) {
		client = clientIn;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenantIn) {
		tenant = tenantIn;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPathIn) {
		keyPath = keyPathIn;
	}

	public Properties getApiProps() {
		return apiProps;
	}

	public void setApiProps(Properties apiPropsIn) {
		apiProps = apiPropsIn;
	}

	public OtfClientBuilder getOtfCb() {
		if (otfCb == null) {
			otfCb = new OtfClientBuilder();
		}
		return otfCb;
	}

	public void setOtfCb(OtfClientBuilder otfCbIn) {
		otfCb = otfCbIn;
	}

}

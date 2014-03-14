package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;
import java.util.logging.Logger;

import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.directory.CustomData;
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
	private Properties apiProps;
	private Properties settingsProps;
	private OtfClientBuilder otfCb;

	// Static Strings
	/** The default Password. */
	public static String DEFPW = "defaultpw";
	/** The App containing the users. */
	public static String USERS_APP = "users";
	/** The App containing the members. */
	public static String MEMBERS_APP = "members";
	/** File path to the Storm Path Properties file **/
	public static final String KEY_PATH = "keyPath";

	public static final String API_KEY_ID = "apiKey.id";
	public static final String API_KEY_SECRET = "apiKey.secret";

	public StormPathBaseDTO(Properties settingsPropsIn) {
		super();
		settingsProps = settingsPropsIn;
	}

	public void load() throws IllegalArgumentException {

		if (settingsProps.containsKey(API_KEY_ID)
				&& settingsProps.containsKey(API_KEY_SECRET)) {
			getOtfCb().loadApiKeyProperties(settingsProps);
		} else {
			if (settingsProps.containsKey(KEY_PATH)) {
				getOtfCb().loadApiKeyProperties(
						settingsProps.getProperty(KEY_PATH));
			}
		}
		setApiProps(getOtfCb().getClientApiKeyProperties());
		if (apiProps == null) {
			throw new IllegalArgumentException(
					"API props is null settingsProps = " + settingsProps);
		}

		client = getOtfCb().build();

		tenant = client.getCurrentTenant();
	}

	public CustomData getCustomData(String href) {
		return getClient().getResource(href, CustomData.class);
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

	public Properties getSettingsProps() {
		return settingsProps;
	}

	public void setSettingsProps(Properties settingsPropsIn) {
		settingsProps = settingsPropsIn;
	}

}

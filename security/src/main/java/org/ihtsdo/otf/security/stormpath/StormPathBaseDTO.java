package org.ihtsdo.otf.security.stormpath;

import static com.stormpath.sdk.cache.Caches.forResource;
import static com.stormpath.sdk.cache.Caches.newCacheManager;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.group.Group;
import com.stormpath.sdk.impl.api.ClientApiKeyBuilder;
import com.stormpath.sdk.impl.client.DefaultClientBuilder;
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

	private ApiKey apiKey;

	private Properties settingsProps;
	private DefaultClientBuilder otfCb;

	private boolean cacheSP = false;

	// Static Strings
	/** The default Password. */
	public static final String DEFPW = "defaultpw";
	/** The App containing the users. */
	public static final String USERS_APP = "users";
	/** The App containing the members. */
	public static final String MEMBERS_APP = "members";
	/** File path to the Storm Path Properties file **/
	public static final String KEY_PATH = "keyPath";

	public static final String API_KEY_ID = "apiKey.id";
	public static final String API_KEY_SECRET = "apiKey.secret";

	public StormPathBaseDTO(final Properties settingsPropsIn) {
		super();
		settingsProps = settingsPropsIn;
	}

	public StormPathBaseDTO(final Properties settingsPropsIn, boolean cacheSPIn) {
		super();
		settingsProps = settingsPropsIn;
		cacheSP = cacheSPIn;
	}

	public final void load() throws IllegalArgumentException {
		getOtfCb();

		if (cacheSP) {
			getOtfCb().setCacheManager(
					newCacheManager()
							.withDefaultTimeToLive(1, TimeUnit.DAYS)
							// general default
							.withDefaultTimeToIdle(2, TimeUnit.HOURS)
							// general default
							.withCache(
									forResource(Account.class)
											// Account-specific cache settings
											.withTimeToLive(1, TimeUnit.HOURS)
											.withTimeToIdle(30,
													TimeUnit.MINUTES))
							.withCache(forResource(Group.class) // Group-specific
																// cache
																// settings
									.withTimeToLive(2, TimeUnit.HOURS)).build() // build
																				// the
																				// CacheManager
					);
		}

		client = getOtfCb().build();

		tenant = client.getCurrentTenant();
	}

	public final Client getClient() {
		return client;
	}

	public final void setClient(final Client clientIn) {
		client = clientIn;
	}

	public final Tenant getTenant() {
		return tenant;
	}

	public final void setTenant(final Tenant tenantIn) {
		tenant = tenantIn;
	}

	public final void resetTenant() {
		String href = getTenant().getHref();
		Tenant tnew = getResourceByHrefTenant(href);
		setTenant(tnew);

	}

	public final DefaultClientBuilder getOtfCb() {
		if (otfCb == null) {
			otfCb = new DefaultClientBuilder();
			if (getApiKey() != null) {
				otfCb.setApiKey(getApiKey());
			}
		}
		return otfCb;
	}

	public final void setOtfCb(final DefaultClientBuilder otfCbIn) {
		otfCb = otfCbIn;
	}

	public final Properties getSettingsProps() {
		return settingsProps;
	}

	public final void setSettingsProps(final Properties settingsPropsIn) {
		settingsProps = settingsPropsIn;
	}

	public final CustomData getResourceByHrefCustomData(final String href) {
		return getClient().getResource(href, CustomData.class);
	}

	public final Account getResourceByHrefAccount(final String href) {
		return getClient().getResource(href, Account.class);
	}

	public final Directory getResourceByHrefDirectory(final String href) {
		return getClient().getResource(href, Directory.class);
	}

	public final Group getResourceByHrefGroup(final String href) {
		return getClient().getResource(href, Group.class);
	}

	public final Application getResourceByHrefApp(final String href) {
		return getClient().getResource(href, Application.class);
	}

	public final Tenant getResourceByHrefTenant(final String href) {
		return getClient().getResource(href, Tenant.class);
	}

	public final boolean isCacheSP() {
		return cacheSP;
	}

	public final void setCacheSP(boolean cacheSPIn) {
		cacheSP = cacheSPIn;
	}

	public final ApiKey getApiKey() {
		if (apiKey == null) {
			setApiKey();
		}
		return apiKey;
	}

	public final ApiKey getApiKey(String key, String secret) {
		apiKey = new ClientApiKeyBuilder().setId(key).setSecret(secret).build();
		return apiKey;
	}

	public final ApiKey getApiKey(String filepath) {
		apiKey = new ClientApiKeyBuilder().setFileLocation(filepath).build();
		return apiKey;
	}

	public final void setApiKey(ApiKey apiKeyIn) {
		apiKey = apiKeyIn;
	}

	public final void setApiKey() throws IllegalArgumentException {

		if (settingsProps.containsKey(API_KEY_ID)
				&& settingsProps.containsKey(API_KEY_SECRET)) {
			String id = settingsProps.getProperty(API_KEY_ID);
			String secret = settingsProps.getProperty(API_KEY_SECRET);
			getApiKey(id, secret);
		} else {
			if (settingsProps.containsKey(KEY_PATH)) {

				String keypath = settingsProps.getProperty(KEY_PATH);
				getApiKey(keypath);
			}
		}
		if (apiKey == null) {
			throw new IllegalArgumentException(
					"apiKey is null settingsProps = " + settingsProps);
		}
	}
}

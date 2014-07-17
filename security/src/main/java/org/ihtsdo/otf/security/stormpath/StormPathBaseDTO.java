package org.ihtsdo.otf.security.stormpath;

import static com.stormpath.sdk.cache.Caches.forResource;
import static com.stormpath.sdk.cache.Caches.newCacheManager;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.group.Group;
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

	public final void load() throws IllegalArgumentException {

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

		client = getOtfCb().setCacheManager(
				newCacheManager()
						.withDefaultTimeToLive(1, TimeUnit.DAYS)
						// general default
						.withDefaultTimeToIdle(2, TimeUnit.HOURS)
						// general default
						.withCache(
								forResource(Account.class)
										// Account-specific cache settings
										.withTimeToLive(1, TimeUnit.HOURS)
										.withTimeToIdle(30, TimeUnit.MINUTES))
						.withCache(forResource(Group.class) // Group-specific
															// cache settings
								.withTimeToLive(2, TimeUnit.HOURS)).build() // build
																			// the
																			// CacheManager
				).build();

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

	public final Properties getApiProps() {
		return apiProps;
	}

	public final void setApiProps(final Properties apiPropsIn) {
		apiProps = apiPropsIn;
	}

	public final OtfClientBuilder getOtfCb() {
		if (otfCb == null) {
			otfCb = new OtfClientBuilder();
		}
		return otfCb;
	}

	public final void setOtfCb(final OtfClientBuilder otfCbIn) {
		otfCb = otfCbIn;
	}

	public final Properties getSettingsProps() {
		return settingsProps;
	}

	public final void setSettingsProps(final Properties settingsPropsIn) {
		settingsProps = settingsPropsIn;
	}

	public final CustomData getResourceByHref_CustomData(final String href) {
		return getClient().getResource(href, CustomData.class);
	}

	public final Account getResourceByHref_Account(final String href) {
		return getClient().getResource(href, Account.class);
	}

	public final Directory getResourceByHref_Directory(final String href) {
		return getClient().getResource(href, Directory.class);
	}

	public final Group getResourceByHref_Group(final String href) {
		return getClient().getResource(href, Group.class);
	}

	public final Application getResourceByHref_App(final String href) {
		return getClient().getResource(href, Application.class);
	}
}

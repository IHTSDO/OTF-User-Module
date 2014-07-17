package org.ihtsdo.otf.security;

import java.util.logging.Logger;

import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.tenant.Tenant;

public class ImportExport {

	public ImportExport() {
		super();
		
	}

	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(BasicTest.class
			.getName());

	private String fileName;

	private static Client client;
	private Tenant tenant;
	private String keyPath = "C:/Users/adamf/stormpath/apiKey.properties";

	public final String getFileName() {
		return fileName;
	}

	public final void setFileName(final String fileNameIn) {
		fileName = fileNameIn;
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(final Client clientIn) {
		client = clientIn;
	}

	public final Tenant getTenant() {
		return tenant;
	}

	public final void setTenant(final Tenant tenantIn) {
		tenant = tenantIn;
	}

	public final String getKeyPath() {
		return keyPath;
	}

	public final void setKeyPath(final String keyPathIn) {
		keyPath = keyPathIn;
	}

}

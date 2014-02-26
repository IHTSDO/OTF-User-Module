package org.ihtsdo.otf.security;

import java.util.logging.Logger;

import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.tenant.Tenant;

public class ImportExport {

	public ImportExport() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileNameIn) {
		fileName = fileNameIn;
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client clientIn) {
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

}

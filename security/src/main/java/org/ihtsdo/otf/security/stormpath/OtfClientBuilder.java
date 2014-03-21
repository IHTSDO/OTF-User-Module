package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;

import com.stormpath.sdk.client.ClientBuilder;

public class OtfClientBuilder extends ClientBuilder {

	public OtfClientBuilder() {
		super();
	}

	public final Properties getClientApiKeyProperties() {
		return loadApiKeyProperties();
	}

	protected final Properties loadApiKeyProperties(final Properties propsIn) {
		super.setApiKeyProperties(propsIn);
		return super.loadApiKeyProperties();
	}

	protected final Properties loadApiKeyProperties(final String pathIn) {
		setApiKeyFileLocation(pathIn);
		return super.loadApiKeyProperties();
	}

}

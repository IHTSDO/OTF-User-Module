package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;

import com.stormpath.sdk.client.ClientBuilder;

public class OtfClientBuilder extends ClientBuilder {

	public OtfClientBuilder() {
		super();
	}

	public Properties getClientApiKeyProperties() {
		return loadApiKeyProperties();
	}

	protected Properties loadApiKeyProperties(Properties propsIn) {
		super.setApiKeyProperties(propsIn);
		return super.loadApiKeyProperties();
	}

	protected Properties loadApiKeyProperties(String pathIn) {
		setApiKeyFileLocation(pathIn);
		return super.loadApiKeyProperties();
	}

}

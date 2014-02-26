package org.ihtsdo.otf.security.stormpath;

import java.util.Properties;

import com.stormpath.sdk.client.ClientBuilder;

public class OtfClientBuilder extends ClientBuilder {

	public OtfClientBuilder() {
		super();
	}

	public Properties getClientApiKeyProperties() {
		return super.loadApiKeyProperties();
	}

}

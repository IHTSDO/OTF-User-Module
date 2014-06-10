package org.ihtsdo.otf.security.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesLoader {

	private static final Logger LOG = Logger.getLogger(ModelMover.class
			.getName());

	// Any commandline arguments
	private String[] args;
	// The list of key names/value to use to select values for settings
	private List<String> keyValues = new ArrayList<String>();
	// The keyName/Value for looking for a setting for a file based property
	private String fileKey;
	// The properties object for storing all the settings.
	private Properties settings;

	public PropertiesLoader() {
		super();

	}

	public PropertiesLoader(List<String> keyValuesIn) {
		super();
		keyValues = keyValuesIn;
	}

	public PropertiesLoader(List<String> keyValuesIn, String fileKeyIn) {
		super();
		keyValues = keyValuesIn;
		fileKey = fileKeyIn;
	}

	public PropertiesLoader(String[] argsIn, List<String> keyValuesIn) {
		super();
		args = argsIn;
		keyValues = keyValuesIn;
	}

	public PropertiesLoader(String[] argsIn, List<String> keyValuesIn,
			String fileKeyIn) {
		super();
		args = argsIn;
		keyValues = keyValuesIn;
		fileKey = fileKeyIn;
	}

	public final List<String> getKeyValues() {
		if (keyValues == null) {
			keyValues = new ArrayList<String>();
		}
		return keyValues;
	}

	public final void setKeyValues(List<String> keyValuesIn) {
		keyValues = keyValuesIn;
	}

	public final String[] getArgs() {
		return args;
	}

	public final void setArgs(String[] argsIn) {
		args = argsIn;
	}

	public void addKeyValToProps(String key, String val, Properties props) {
		String keyT = key.trim();
		if (keyValues.contains(keyT)) {
			props.setProperty(keyT, val.trim());
		}
	}

	private void addEnvVars(Properties setProps) {
		Map<String, String> env = System.getenv();
		for (String key : env.keySet()) {
			String val = env.get(key);
			addKeyValToProps(key, val, setProps);
		}
	}

	private void addSysProps(Properties setProps) {
		Properties sysProps = System.getProperties();
		for (Object keyO : sysProps.keySet()) {
			String key = keyO.toString();
			String val = sysProps.getProperty(key);
			addKeyValToProps(key, val, setProps);
		}

	}

	private void addArgs(Properties setProps) {
		if (args != null) {
			for (String arg : args) {
				String[] argZ = arg.split("=");
				if (argZ.length == 2) {
					String key = argZ[0].trim();
					String val = argZ[1].trim();
					addKeyValToProps(key, val, setProps);
				}
			}
		}

	}

	private final Properties addPropsFromFile(final Properties setProps) {
		Properties fileProps = null;
		if (stringOK(fileKey)) {
			String setProp = setProps.getProperty(fileKey);
			if (setProp != null) {
				LOG.info("Loading props from " + setProp);
				try {
					fileProps = new Properties();
					fileProps.load(new FileInputStream(setProp));
				} catch (IOException e) {
					LOG.log(Level.SEVERE,
							"Tried to load parameters properties files from \n"
									+ setProp, e);
				}
			}
		}

		return fileProps;
	}

	public final Properties getSettings() {
		getKeyValues();
		if (settings == null) {
			// First create the initial properties
			Properties setProps = new Properties();
			// Look into Env Vars
			addEnvVars(setProps);
			// Look into System props
			addSysProps(setProps);
			// Look into commandline args
			addArgs(setProps);
			// IF settings is set then load the props and apply the initial
			// props to
			// it.
			settings = addPropsFromFile(setProps);
			if (settings == null) {
				settings = new Properties();
			}
			// Over write vals from file with vals from args.
			for (Object keyO : setProps.keySet()) {
				String key = keyO.toString();
				String val = setProps.getProperty(key);
				settings.setProperty(key, val);
			}

			return settings;
		}

		return settings;
	}

	public final void setSettings(Properties settingsIn) {
		settings = settingsIn;
	}

	public final String getFileKey() {
		return fileKey;
	}

	public final void setFileKey(String fileKeyIn) {
		fileKey = fileKeyIn;
	}

	public final static boolean stringOK(String toCheck) {
		return toCheck != null & toCheck.length() > 0;
	}

}

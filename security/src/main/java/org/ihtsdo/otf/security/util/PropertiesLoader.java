package org.ihtsdo.otf.security.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesLoader {

	private static final Logger LOG = Logger.getLogger(PropertiesLoader.class
			.getName());

	// Any commandline arguments
	private String[] args;
	// The list of key names/value to use to select values for settings
	private List<String> keyValues = new ArrayList<String>();
	// The keyName/Value for looking for a setting for a file based property
	private String fileKey;
	// The properties object for storing all the settings.
	private Properties settings;
	private Properties initProps;
	private Properties sysVarArgProps;

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

	public PropertiesLoader(List<String> keyValuesIn, String fileKeyIn,
			Properties initPropsIn) {
		super();
		keyValues = keyValuesIn;
		fileKey = fileKeyIn;
		initProps = initPropsIn;
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

				// Test file exists etc.
				File fprop = new File(setProp);

				if (!fprop.exists()) {
					LOG.info("Properties File does not exist at " + setProp);
					return null;
				}
				if (fprop.isDirectory()) {
					LOG.info("Properties File is a direcotry at " + setProp);
					return null;
				}
				if (!fprop.canRead()) {
					LOG.info("Properties File can not be read from " + setProp);
					return null;
				}

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
			settings = addPropsFromFile(getSysVarArgProps());
			if (settings == null) {
				settings = addPropsFromFile(getInitProps());
			}
			if (settings == null) {
				settings = new Properties();
			}
			// Over write vals from file with vals from args.
			for (Object keyO : getSysVarArgProps().keySet()) {
				String key = keyO.toString();
				String val = getSysVarArgProps().getProperty(key);
				settings.setProperty(key, val);
			}

			// add from init props if not exist
			for (Object keyO : getInitProps().keySet()) {
				if (!settings.containsKey(keyO)) {
					String key = keyO.toString();
					String val = getInitProps().getProperty(key);
					settings.setProperty(key, val);
				}
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

	public final Properties getInitProps() {
		if (initProps == null) {
			initProps = new Properties();
		}
		return initProps;
	}

	public final void setInitProps(Properties initPropsIn) {
		initProps = initPropsIn;
	}

	public final Properties getSysVarArgProps() {
		if (sysVarArgProps == null) {
			sysVarArgProps = new Properties();
			addEnvVars(sysVarArgProps);
			// Look into System props
			addSysProps(sysVarArgProps);
			// Look into commandline args
			addArgs(sysVarArgProps);
		}
		return sysVarArgProps;
	}

	public final void setSysVarArgProps(Properties sysVarArgPropsIn) {
		sysVarArgProps = sysVarArgPropsIn;
	}

	public static final void logProps(Properties props) {

		StringBuilder sbuild = new StringBuilder();

		List<String> keys = new ArrayList<String>();

		for (Object keyO : props.keySet()) {
			keys.add(keyO.toString());
		}

		Collections.sort(keys);

		sbuild.append("Properties num found = ").append(keys.size())
				.append(":\n");
		for (String key : keys) {
			String val = props.getProperty(key);
			sbuild.append("Key = ").append(key).append(" || Value = ")
					.append(val).append("\n");

		}

		LOG.info(sbuild.toString());

	}
}

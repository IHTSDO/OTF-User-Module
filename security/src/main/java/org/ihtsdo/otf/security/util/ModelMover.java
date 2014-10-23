package org.ihtsdo.otf.security.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.UserSecurityCompare;
import org.ihtsdo.otf.security.stormpath.StormPathBaseDTO;
import org.ihtsdo.otf.security.stormpath.StormPathUserSecurityHandler;
import org.ihtsdo.otf.security.xml.XmlUserSecurityHandler;

public class ModelMover {

	private static final Logger LOG = Logger.getLogger(ModelMover.class
			.getName());

	private Properties settings;

	private PropertiesLoader propLoad;

	public static final String SET_PROPS = "settings";
	public static final String XML_FN_IN = "xmlFnIn";
	public static final String XML_FN_OUT = "xmlFnOut";
	public static final String REBUILD_SP = "rebuild";
	public static final String LOG_EVENTS = "log";

	private XmlUserSecurityHandler xmlUsIn;
	private XmlUserSecurityHandler xmlUsOut;
	private StormPathUserSecurityHandler spu;

	public ModelMover() {
		super();

	}

	public static void main(final String[] args) {
		ModelMover mm = new ModelMover();
		mm.run(args);
	}

	public final void run(final String[] args) {
		init(args);
		run();
	}

	public final void run() {
		if (checkSpSettings()) {

			boolean logging = PropertiesLoader.stringOK(settings
					.getProperty(LOG_EVENTS));

			boolean rebuildSP = PropertiesLoader.stringOK(settings
					.getProperty(REBUILD_SP));
			if (rebuildSP) {
				if (logging) {
					LOG.info("About to clear StormPath");
				}
				clearSP();
			}

			String xmlIn = settings.getProperty(XML_FN_IN);
			boolean loadFromXML = PropertiesLoader.stringOK(xmlIn);
			if (loadFromXML) {
				LOG.info("About to Xml2Storm");
				xml2Storm(logging);
			}
			String xmlOut = settings.getProperty(XML_FN_OUT);
			boolean saveToXML = PropertiesLoader.stringOK(xmlOut);
			if (saveToXML) {
				LOG.info("About to saveToXML");
				storm2Xml(logging);
			}
		} else {
			LOG.severe("StormPath Settings Not Set");
		}

	}

	private String stormToString(final boolean log) {
		String ustr = getSpu().getUserSecurityModel().getModel().toString();
		if (log) {
			LOG.info("Storm as String : \n" + ustr);
			LOG.info(UserSecurityCompare.remSpaceLineEnds(ustr));
		}

		return ustr;
	}

	private void storm2Xml(final boolean log) {
		setSpu(null);
		StormPathUserSecurityHandler locspu = getSpu();

		try {
			locspu.buildUserSecurity();
			getXmlUsOut().getUserSecurityModel().setModel(
					(locspu.getUserSecurityModel().getModel()));
			getXmlUsOut().saveUserSecurity();
			if (log) {
				LOG.info("storm2Xml : \n"
						+ getXmlUsOut().getXMLFromUserSecurityAsString());
			}

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	private void xml2Storm(final boolean log) {

		StormPathUserSecurityHandler locspu = getSpu();
		try {
			getXmlUsIn();
			// if (log) {
			// LOG.info("Xml2Storm : \n"
			// + xmlUsIn.getXMLFromUserSecurityAsString());
			// }

			// Make sure settings have been init'ed by called defpw
			xmlUsIn.getUserSecurityModel().getSettings().getDefPw();
			locspu.sendUserSecuritytoStormPath(xmlUsIn.getUserSecurityModel()
					.getModel());

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	private void clearSP() {
		setSpu(null);
		StormPathUserSecurityHandler spuNew = getSpu();
		try {
			spuNew.clearSP();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}
		setSpu(null);
	}

	public final void init(final String[] args) {
		getPropLoad(args);
		getSettings();
	}

	public final Properties getSettings() {
		if (settings == null) {
			settings = getPropLoad().getSettings();
		}
		return settings;
	}

	public final void setSettings(final Properties settingsIn) {
		settings = settingsIn;
	}

	public final PropertiesLoader getPropLoad() {

		if (propLoad == null) {
			propLoad = new PropertiesLoader(getKeyVals(), SET_PROPS);
		}
		return propLoad;
	}

	public final PropertiesLoader getPropLoad(final String[] argsIn) {
		getPropLoad();
		propLoad.setArgs(argsIn);
		return propLoad;
	}

	public final void setPropLoad(final String[] args) {
	}

	private List<String> getKeyVals() {
		List<String> keyVals = new ArrayList<String>();
		keyVals.add(SET_PROPS);
		keyVals.add(XML_FN_IN);
		keyVals.add(XML_FN_OUT);

		// SP stuff
		keyVals.add(StormPathBaseDTO.KEY_PATH);
		keyVals.add(StormPathBaseDTO.API_KEY_ID);
		keyVals.add(StormPathBaseDTO.API_KEY_SECRET);
		keyVals.add(REBUILD_SP);
		// Logging
		keyVals.add(LOG_EVENTS);

		return keyVals;
	}

	public final boolean checkSpSettings() {
		boolean kpOK = PropertiesLoader.stringOK(settings
				.getProperty(StormPathBaseDTO.KEY_PATH));

		if (kpOK) {
			return true;
		}

		else {
			boolean apiIdOk = PropertiesLoader.stringOK(settings
					.getProperty(StormPathBaseDTO.API_KEY_ID));

			boolean apikeyOk = PropertiesLoader.stringOK(settings
					.getProperty(StormPathBaseDTO.API_KEY_SECRET));
			if (apiIdOk && apikeyOk) {
				return true;
			}
		}
		return false;
	}

	public final XmlUserSecurityHandler getXmlUsIn() {
		if (xmlUsIn == null) {
			Properties xmlP = new Properties();
			xmlP.setProperty(XmlUserSecurityHandler.CONF_PROPS_FN,
					settings.getProperty(XML_FN_IN));
			xmlUsIn = new XmlUserSecurityHandler(xmlP);
		}
		return xmlUsIn;
	}

	public final void setXmlUsIn(final XmlUserSecurityHandler xmlUsInIn) {
		xmlUsIn = xmlUsInIn;
	}

	public final XmlUserSecurityHandler getXmlUsOut() {
		if (xmlUsOut == null) {

			String xmlIn = settings.getProperty(XML_FN_IN);
			String xmlout = settings.getProperty(XML_FN_OUT);
			if (PropertiesLoader.stringOK(xmlout)) {
				if (xmlout.equals(xmlIn)) {
					xmlUsOut = getXmlUsIn();
					return xmlUsOut;
				} else {
					xmlUsOut = new XmlUserSecurityHandler();
					xmlUsOut.setConfigFN(settings.getProperty(XML_FN_OUT));
				}
			}
		}
		return xmlUsOut;
	}

	public final void setXmlUsOut(final XmlUserSecurityHandler xmlUsOutIn) {
		xmlUsOut = xmlUsOutIn;
	}

	public final StormPathUserSecurityHandler getSpu() {
		if (spu == null) {
			spu = new StormPathUserSecurityHandler(getSettings());
		}

		return spu;
	}

	public final void setSpu(final StormPathUserSecurityHandler spuIn) {
		spu = spuIn;
	}

}

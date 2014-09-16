package org.ihtsdo.otf.security;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.stormpath.StormPathBaseDTO;
import org.ihtsdo.otf.security.stormpath.StormPathUserSecurityHandler;
import org.ihtsdo.otf.security.xml.XmlUserSecurityHandler;

public class StormTest {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(XmlUserSecurityHandler.class
			.getName());

	private static String fn = "./TextFiles/Example.xml";
	private static String apiKeyFile = "C:/Users/adamf/stormpath/apiKey.properties";
	private static XmlUserSecurityHandler xmlUs;
	private static StormPathUserSecurityHandler spu;

	public static void main(final String[] args) {
		StormTest sTest = new StormTest();
		sTest.init();

	}

	private void init() {

		// build();
		// rebuild();
		// clearSP();
		// Xml2Storm(false);
		// rebuildSpFromXML();
		storm2Xml(true);
		stormToString(true);

	}

	private void rebuildSpFromXML() {
		clearSP();
		Xml2Storm(false);
	}

	private void build() {
		storm2Xml(false);
		Xml2Storm(false);
		storm2Xml(true);
	}

	private void rebuild() {
		clearSP();
		build();
	}

	private String stormToString(final boolean log) {
		String ustr = getSpu().getUserSecurityModel().getFullModel().toString();
		if (log) {
			LOG.info("Storm as String : \n" + ustr);
			LOG.info(UserSecurityCompare.remSpaceLineEnds(ustr));
		}

		return ustr;
	}

	private void storm2Xml(final boolean log) {
		StormPathUserSecurityHandler spu = getSpu();
		XmlUserSecurityHandler xmlUs = new XmlUserSecurityHandler();
		try {
			spu.buildUserSecurity();
			xmlUs.getUserSecurityModel().setModel(
					(spu.getUserSecurityModel().getFullModel()));
			if (log) {
				LOG.info("storm2Xml : \n"
						+ xmlUs.getXMLFromUserSecurityAsString());
			}

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	private void Xml2Storm(final boolean log) {

		StormPathUserSecurityHandler spu = getSpu();
		try {
			getXmlUs();
			if (log) {
				LOG.info("Xml2Storm : \n"
						+ xmlUs.getXMLFromUserSecurityAsString());
			}
			spu.sendUserSecuritytoStormPath(xmlUs.getUserSecurityModel()
					.getFullModel());

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	private void clearSP() {
		StormPathUserSecurityHandler spu = getSpu();
		try {
			spu.clearSP();
		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}
	}

	private void sortXML() {
		try {
			getXmlUs();
			LOG.info("using XML :\n" + xmlUs.getXMLFromUserSecurity());

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}
	}

	public static XmlUserSecurityHandler getXmlUs() {
		if (xmlUs == null) {
			Properties xmlP = new Properties();
			xmlP.setProperty(XmlUserSecurityHandler.CONF_PROPS_FN, fn);
			xmlUs = new XmlUserSecurityHandler(xmlP);
		}

		return xmlUs;
	}

	public static void setXmlUs(final XmlUserSecurityHandler xmlUsIn) {
		xmlUs = xmlUsIn;
	}

	public static StormPathUserSecurityHandler getSpu() {
		if (spu == null) {
			Properties spuP = new Properties();
			spuP.setProperty(StormPathBaseDTO.KEY_PATH, apiKeyFile);
			spu = new StormPathUserSecurityHandler(spuP);
		}

		return spu;
	}

	public static void setSpu(final StormPathUserSecurityHandler spuIn) {
		spu = spuIn;
	}

}

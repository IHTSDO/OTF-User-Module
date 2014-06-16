package org.ihtsdo.otf.security;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ihtsdo.otf.security.stormpath.StormPathBaseDTO;
import org.ihtsdo.otf.security.stormpath.StormPathUserSecurity;
import org.ihtsdo.otf.security.xml.XmlUserSecurity;

public class StormTest {
	// TODO: Full test XML > SP > XML > Compare. Obviously StormPath admin etc
	// info will be different.
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(XmlUserSecurity.class
			.getName());

	private static String fn = "./TextFiles/Example.xml";
	private static String apiKeyFile = "C:/Users/adamf/stormpath/apiKey.properties";
	private static XmlUserSecurity xmlUs;
	private static StormPathUserSecurity spu;

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
		String ustr = getSpu().getUserSecurity().toString();
		if (log) {
			LOG.info("Storm as String : \n" + ustr);
			LOG.info(UserSecurityCompare.remSpaceLineEnds(ustr));
		}

		return ustr;
	}

	private void storm2Xml(final boolean log) {
		StormPathUserSecurity spu = getSpu();
		XmlUserSecurity xmlUs = new XmlUserSecurity();
		try {
			spu.buildUserSecurity();
			xmlUs.setUserSecurity(spu.getUserSecurity());
			if (log) {
				LOG.info("storm2Xml : \n"
						+ xmlUs.getXMLFromUserSecurityAsString());
			}

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	private void Xml2Storm(final boolean log) {

		StormPathUserSecurity spu = getSpu();
		try {
			getXmlUs();
			if (log) {
				LOG.info("Xml2Storm : \n"
						+ xmlUs.getXMLFromUserSecurityAsString());
			}
			spu.sendUserSecuritytoStormPath(xmlUs.getUserSecurity());

		} catch (Exception e) {

			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

	}

	private void clearSP() {
		StormPathUserSecurity spu = getSpu();
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

	public static XmlUserSecurity getXmlUs() {
		if (xmlUs == null) {
			Properties xmlP = new Properties();
			xmlP.setProperty(XmlUserSecurity.CONF_PROPS_FN, fn);
			xmlUs = new XmlUserSecurity(xmlP);
		}

		return xmlUs;
	}

	public static void setXmlUs(final XmlUserSecurity xmlUsIn) {
		xmlUs = xmlUsIn;
	}

	public static StormPathUserSecurity getSpu() {
		if (spu == null) {
			Properties spuP = new Properties();
			spuP.setProperty(StormPathBaseDTO.KEY_PATH, apiKeyFile);
			spu = new StormPathUserSecurity(spuP);
		}

		return spu;
	}

	public static void setSpu(final StormPathUserSecurity spuIn) {
		spu = spuIn;
	}

}

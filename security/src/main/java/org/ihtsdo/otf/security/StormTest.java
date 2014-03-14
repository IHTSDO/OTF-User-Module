package org.ihtsdo.otf.security;

import java.util.Properties;
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

	public static void main(String[] args) {
		StormTest sTest = new StormTest();
		sTest.init();

	}

	private void init() {

		// build();
		rebuild();
		// clearSP();
		// Xml2Storm(false);
		// storm2Xml(true);

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

	private void storm2Xml(boolean log) {
		StormPathUserSecurity spu = getSpu();
		XmlUserSecurity xmlUs = new XmlUserSecurity();
		try {
			spu.buildUserSecurity();
			xmlUs.setUserSecurity(spu.getUserSecurity());
			if (log) {
				LOG.info("storm2Xml : \n"
						+ xmlUs.getXMLFromUserSecurityAsStringSortByName());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void Xml2Storm(boolean log) {

		StormPathUserSecurity spu = getSpu();
		try {
			getXmlUs();
			if (log) {
				LOG.info("Xml2Storm : \n"
						+ xmlUs.getXMLFromUserSecurityAsStringSortByName());
			}
			spu.sendUserSecuritytoStormPath(xmlUs.getUserSecurity());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void clearSP() {
		StormPathUserSecurity spu = getSpu();
		try {
			spu.clearSP();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sortXML() {
		try {
			getXmlUs();
			LOG.info("using XML :\n"
					+ xmlUs.getXMLFromUserSecurityAsStringSortByName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public static void setXmlUs(XmlUserSecurity xmlUsIn) {
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

	public static void setSpu(StormPathUserSecurity spuIn) {
		spu = spuIn;
	}

}

package org.ihtsdo.otf.security;

import java.util.logging.Logger;

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

	String fn = "./TextFiles/Example.xml";

	public static void main(String[] args) {
		StormTest sTest = new StormTest();
		sTest.init();

	}

	private void init() {

		// build();
		rebuild();
		// clearSP();
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
		StormPathUserSecurity spu = new StormPathUserSecurity();
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

		XmlUserSecurity xmlUs = new XmlUserSecurity(fn);
		StormPathUserSecurity spu = new StormPathUserSecurity();
		try {
			xmlUs.initFromFile();
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
		StormPathUserSecurity spu = new StormPathUserSecurity();
		try {
			spu.clearSP();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sortXML() {
		XmlUserSecurity xmlUs = new XmlUserSecurity(fn);

		try {
			xmlUs.initFromFile();
			LOG.info("using XML :\n"
					+ xmlUs.getXMLFromUserSecurityAsStringSortByName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

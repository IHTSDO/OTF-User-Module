package org.ihtsdo.otf.security.xml.base;

/**
 * @author Adam Flinton
 */

import java.util.Hashtable;

public class XPathDTO {

	private String xPath = "";
	private String startChars = "";
	private Hashtable<Integer, String> tokens = new Hashtable<Integer, String>();

	public XPathDTO() {
		super();
		
	}

	public final String getStartChars() {
		return startChars;
	}

	public final void setStartChars(final String startCharsIn) {
		this.startChars = startCharsIn;
	}

	public final Hashtable<Integer, String> getTokens() {
		return tokens;
	}

	public final void setTokens(final Hashtable<Integer, String> tokensIn) {
		this.tokens = tokensIn;
	}

	public final String getXPath() {
		return xPath;
	}

	public final void setXPath(final String path) {
		xPath = path;
	}

	public final String getSubXpath(final int tok) {
		String xp = "";
		int i = 1;
		while (i <= tok) {
			xp = xp + tokens.get(Integer.valueOf(i));
			if (i < tok) {
				xp = xp + "/";
			}
			i++;
		}

		return xp;
	}

}

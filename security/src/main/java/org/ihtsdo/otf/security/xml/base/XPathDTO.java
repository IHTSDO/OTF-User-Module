package org.ihtsdo.otf.security.xml.base;
/**
 * @author Adam Flinton
 */

import java.util.Hashtable;

public class XPathDTO {
	
	public String xPath = "";
	public String startChars = "";
	public Hashtable tokens = new Hashtable();

	public XPathDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStartChars() {
		return startChars;
	}

	public void setStartChars(String startChars) {
		this.startChars = startChars;
	}

	public Hashtable getTokens() {
		return tokens;
	}

	public void setTokens(Hashtable tokens) {
		this.tokens = tokens;
	}

	public String getXPath() {
		return xPath;
	}

	public void setXPath(String path) {
		xPath = path;
	}
	
	public String getSubXpath(int tok){
		String xp ="";
		int i = 1;
		while (i <= tok){
			xp = xp+(String)tokens.get(Integer.valueOf(i));
			if(i < tok){
				xp = xp +"/";
			}
			i++;
		}
		
		
		
		
		return xp;
	}
	
	

}

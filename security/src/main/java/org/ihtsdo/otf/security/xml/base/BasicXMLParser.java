/*
 * Created on Nov 12, 2004
 *
 */
package org.ihtsdo.otf.security.xml.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Note Built to handle situations where there is a single element & if looking
 * for a attrib, then a single attrib. If multiple needed then simply add the
 * elements to a vector or similar collection & query that
 */

/**
 * @author Adam Flinton
 */
public class BasicXMLParser {

	// private static final Log log = LogFactory.getLog(BasicXMLParser.class);
	private static final Logger LOG = Logger.getLogger(BasicXMLParser.class
			.getName());

	// private final String testURL = "C:/Data/test2.xml";

	private final String testTag = "BSVResponse";

	// String TestTag = "wsa:MessageID";
	private final String testString = "";

	// private final String testAttrib = "ErrorStatus";

	private String tagFQN = "";

	private String tagname = "";

	// GetFileURL gfu = new GetFileURL();

	/**
	 * 
	 */
	public BasicXMLParser() {
		super();
		// try {
		// // TestString = gfu.fileToString(TestURL);
		// // System.out.println("Test URL = "+TestURL);
		// // System.out.println("TestString = "+TestString);
		// } catch (Exception excep) {
		// System.out.println("Exception thrown Test URL = " + testURL
		// + " Exception = " + E.getMessage());
		//
		// }
	}

	public static void main(final String[] args) {

		BasicXMLParser sht = new BasicXMLParser();
		sht.handleXML();

	}

	public final void handleXML() {

		String rv = getElementValue(testString, testTag);
		// String Attval = getXMLAttribute(TestString, TestTag, TestAttrib);
		System.out.println("RetVal = " + rv);

	}

	public static String getFirstAttValue(final String xml, final String attName) {
		String retval = "";

		String searchS = attName + "=\"";
		try {
			int starti = xml.indexOf(searchS);
			int openi = starti + searchS.length();
			// System.out.println("tag1 int = "+tag1);
			if (starti > -1) {
				int closei = xml.indexOf("\"", openi);
				retval = xml.substring(openi, closei);

			}
		} catch (Exception e) {
			// log.error("Exception = " + e.getMessage(),e);
			// log.error("tag " + tag + " from " + xml);
			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

		return retval;

	}

	public final String getFirstElement(final String xml, final String tag) {
		String retval = "";
		// System.out.println("tag = " +tag);
		// System.out.println("tag index = " +xml.indexOf(tag));
		tagname = tag;
		setFQN(xml, xml.indexOf(tag));
		tagname = tagFQN;
		String startTag = "<" + tagname;

		// log.error("tagname = " +tagname);
		// System.out.println("tagname = " +tagname);
		/*
		 * test for an empty tag e.g. <bob/> if indexOf /> is not -1 but is less
		 * than >
		 */

		try {
			int tag1 = xml.indexOf(startTag);
			// System.out.println("tag1 int = "+tag1);
			if (tag1 > -1) {
				int emptyEli = xml.indexOf("/>", tag1);
				if (emptyEli != -1 && emptyEli < xml.indexOf('>', tag1)) {
					emptyEli = emptyEli + 2;
					retval = xml.substring(tag1, emptyEli);

				} else {

					int pos1 = xml.indexOf("</" + tagname, tag1);
					// System.out.println("pos1 int = "+pos1);
					int pos2 = xml.indexOf(">", pos1);
					// System.out.println("pos2 int = "+pos2);
					pos2 = pos2 + 1;
					retval = xml.substring(tag1, pos2);
				}
				// System.out.println("XMl = "+xml.substring(tag1,pos2));
			}

		} catch (Exception e) {
			// log.error("Exception = " + e.getMessage(),e);
			// log.error("tag " + tag + " from " + xml);
			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}
		return retval;

	}

	public final Vector<String> getElements(final String xml, final String tag) {
		Vector<String> vect = new Vector<String>();

		String retval = "";
		// System.out.println("tag = " +tag);
		// System.out.println("tag index = " +xml.indexOf(tag));
		tagname = tag;
		setFQN(xml, xml.indexOf(tag));
		tagname = tagFQN;
		String startTag = "<" + tagname;
		int startI = 0;

		/*
		 * test for an empty tag e.g. <bob/> if indexOf /> is not -1 but is less
		 * than >
		 */
		// log.error("tagname = " +tagname);
		// System.out.println("tagname = " +tagname);
		try {
			int tag1 = xml.indexOf(startTag, startI);
			// System.out.println("tag1AAAAA int = "+tag1);
			while (tag1 > -1) {
				// int start = getEndtagStart(xml, tag1);
				int emptyEli = xml.indexOf("/>", tag1);
				if (emptyEli != -1 && emptyEli < xml.indexOf('>', tag1)) {
					emptyEli = emptyEli + 2;
					retval = xml.substring(tag1, emptyEli);
					vect.add(retval);
					startI = emptyEli;
					tag1 = xml.indexOf(startTag, startI);
				} else {
					int pos1 = xml.indexOf("</" + tagname, tag1);
					// System.out.println("pos1 int = "+pos1);
					int pos2 = xml.indexOf(">", pos1);
					// System.out.println("pos2 int = "+pos2);
					pos2 = pos2 + 1;
					retval = xml.substring(tag1, pos2);
					// System.out.println("retval = "+xml.substring(tag1,pos2));
					vect.add(retval);
					startI = pos2;
					tag1 = xml.indexOf(startTag, startI);
				}
			}
		} catch (Exception e) {
			// log.error("Exception = " + e.getMessage(),e);
			// log.error("tag " + tag + " from " + xml);
			LOG.log(Level.SEVERE, "An exception has occurred", e);
		}

		return vect;

	}

	public final String getElementValue(final String xml, final String tag) {
		String retval = "";

		tagname = tag;
		// log.error("tagname = " +tagname);
		// System.out.println("tagname = " +tagname);
		try {
			int tag1 = xml.indexOf(tagname);
			// System.out.println("tag1 int = "+tag1);
			// log.error("GetElementValue tag1 int = "+tag1);
			// int start = goforward(eEnd1,xml,tag1);
			// start = start +1;
			// int end = goforward(eStart,xml,tag1);
			// log.error("tag1 = " + tag1);
			if (tag1 > -1) {
				int start = getStartTagEnd(xml, tag1);
				// log.error("GetElementValue start = " + start);
				int end = getEndtagStart(xml);
				// log.error("GetElementValue end = " + end);
				if (end > -1) {
					// log.error("end greater than -1 = " + end);
					retval = xml.substring(start, end);
					// log.error("GetElementValue retval = "+retval);
					// log.error("GetElementValue xml = "+xml);
				}
				if (end == -1) {
					// assume an empty tag e.g. <bob/>
					// log.error("end =-1 thus = " + end);
					retval = "";
				}

			} else {
				retval = xml;
			}

		} catch (Exception e) {
			// log.error("Exception = " + e.getMessage(),e);
			LOG.log(Level.SEVERE, "Exception = " + e.getMessage(), e);
			LOG.severe("tag " + tag + " from " + xml);
			// LOG.log(Level.SEVERE, "An exception has occurred", e);
		}
		return retval;
	}

	public final String setElementValue(final String xml, final String tag,
			final String newValue) {
		tagname = tag;
		String retval = "";
		// System.out.println("tagname = " +tagname);
		try {
			int tag1 = xml.indexOf(tagname);
			// System.out.println("tag1 int = "+tag1);

			// int start = goforward(eEnd1,xml,tag1);
			// start = start +1;
			// int end = goforward(eStart,xml,tag1);
			// log.error("tag1 = " + tag1);
			if (tag1 > -1) {
				int start = getStartTagEnd(xml, tag1);
				// log.error("start = " + start);
				int end = getEndtagStart(xml);
				// log.error("end = " + end);
				if (end > -1) {
					// log.error("end greater than -1 = " + end);
					String toReplace = xml.substring(start, end);
					LOG.severe("toReplace = " + toReplace);
					// set the value to NewValue
					retval = replace(xml, toReplace, newValue);
				}
				if (end == -1) {
					// assume an empty tag e.g. <bob/>
					// log.error("end =-1 thus = " + end);
					retval = "";
				}

			} else {
				retval = xml;
			}

		} catch (Exception e) {
			// log.error("Exception = " + e.getMessage(),e);
			LOG.log(Level.SEVERE, "Exception = " + e.getMessage(), e);
			LOG.severe("tag " + tag + " from " + xml);

		}
		return retval;

	}

	public final String getXMLAttribute(final String xml, final String tag,
			final String attributeName) {
		String eq = "=";
		String retval = "";
		String attdelim = "\"";
		// attributeName = attributeName + eq;
		int tag1 = xml.indexOf(tag);
		String elem = getStartElement(xml, tag1);
		System.out.println("getStartElement Elem = " + elem);
		int tag2 = elem.indexOf(attributeName + eq);
		// writeNchars("Attrib",Elem,Tag2);
		int startAtt = goforward(attdelim, elem, tag2);
		startAtt = startAtt + 1;
		int endAtt = goforward(attdelim, elem, startAtt);

		// writeNchars("AttribSTVAL",Elem,StartAtt);
		// writeNchars("AttribEndVAL",Elem,EndAtt);

		retval = elem.substring(startAtt, endAtt);
		System.out.println("getXMLAttribute RetVal = " + retval);

		return retval;

	}

	private int goback(final String find, final String xml, final int index) {

		int x = index;

		String tagS = "";
		tagS = tagS + xml.charAt(x);
		// System.out.println("tagS1= "+tagS +" int = "+x);

		// Count back to next find
		while (!tagS.equalsIgnoreCase(find)) {
			// System.out.println("tagS= "+tagS +" int = "+x);
			tagS = "";
			x--;
			tagS = tagS + xml.charAt(x);

		}

		// tagS = tagS +xml.charAt(x -1);
		// System.out.println("tagS= "+tagS +" int = "+x);
		// writeNchars("x",xml,x);

		return x;
	}

	private int goforward(final String find, final String xml, final int index) {

		int x = index;

		String tagS = "";
		tagS = tagS + xml.charAt(x);
		// System.out.println("tagS1= "+tagS +" int = "+x);
		// Count forward to next find
		while (!tagS.equalsIgnoreCase(find)) {
			// System.out.println("tagS= "+tagS +" int = "+x);
			tagS = "";
			x++;
			tagS = tagS + xml.charAt(x);
		}
		tagS = tagS + xml.charAt(x - 1);
		// System.out.println("tagS= "+tagS +" int = "+x);
		// writeNchars("x",xml,x);
		return x;
	}

	private int getStartTagEnd(final String xml, final int index) {
		int start = -1;
		setFQN(xml, index);
		String eEnd1 = ">";
		try {
			start = goforward(eEnd1, xml, index);
			start = start + 1;
		} catch (Exception excep) {
			// log.error("getEndtagStart error thrown ", E);
			LOG.log(Level.SEVERE, "getEndtagStart error thrown ", excep);
		}

		return start;

	}

	private int getEndtagStart(final String xml) {
		int loc = -1;
		try {
			String eEnd = "</" + tagFQN;
			loc = xml.indexOf(eEnd);
		} catch (Exception excep) {
			// log.error("getEndtagStart error thrown ", E);
			LOG.log(Level.SEVERE, "getEndtagStart error thrown ", excep);
		}

		return loc;
	}

	private String getStartElement(final String xml, final int index) {
		String retval = "";
		String eStart = "<";

		int start = goback(eStart, xml, index);
		// writeNchars("Start_Element",xml,start);
		int end = getStartTagEnd(xml, index);
		// writeNchars("End_Element",xml,end);

		retval = xml.substring(start, end);
		// System.out.println("getStartElement Retval = " +retval);

		return retval;
	}

	public final String insertElementValue(final String xml, final String tag,
			final String insContent) {

		String retval = "";
		int tag1 = xml.indexOf(tag);

		int loc = getStartTagEnd(xml, tag1);

		retval = insert(xml, insContent, loc);

		return retval;
	}

	public final String removeElementValue(final String xml, final String tag,
			final String remContent) {

		String retval = "";
		int tag1 = xml.indexOf(tag);

		int loc = getStartTagEnd(xml, tag1);
		retval = remove(xml, remContent, loc);

		return retval;
	}

	public final String removeAttribValue(final String xml,
			final String attributeName) {

		String attdelim = "\"";
		String atName = new StringBuilder().append(attributeName).append('=')
				.toString();

		StringBuilder sbuild = new StringBuilder();
		while (xml.indexOf(atName) > -1) {

			int startAtt1 = xml.indexOf(atName);
			int stopAtt1 = goforward(attdelim, xml, startAtt1);
			stopAtt1 = stopAtt1 + 1;

			// System.out.println("retval = "+xml.substring(startAtt1,
			// stopAtt1);

			int stopAtt2 = goforward(attdelim, xml, stopAtt1);
			stopAtt2 = stopAtt2 + 1;
			// System.out.println("stopAtt2 = "+stopAtt2);
			// System.out.println("retval2 = "+xml.substring(startAtt1,
			// stopAtt2));

			// System.out.println("substring1 = "+xml.substring(startAtt1,
			// stopAtt2));

			String startXML = xml.substring(0, startAtt1);
			// System.out.println("substring = "+startXML);
			String endXML = xml.substring(stopAtt2);
			// System.out.println("substring2 = "+xml.substring(startAtt1,
			// stopAtt2));
			sbuild.append(startXML);
			sbuild.append(endXML);
		}

		return sbuild.toString();

	}

	public final String removeCDATA(final String xml) {

		String cStart = "<![CDATA[";
		String cEnd = "]]>";
		String retval = "";

		int loc = xml.indexOf(cStart);

		String frontchop = remove(xml, cStart, loc);

		loc = frontchop.indexOf(cEnd);

		retval = remove(frontchop, cEnd, loc);

		// System.out.println("XML before CD Rem"+xml);
		// System.out.println("After CDREM"+retval);

		return retval;
	}

	private String insert(final String str, final String toInsert,
			final int location) {

		StringBuffer buffer = new StringBuffer(str);
		return buffer.insert(location, toInsert.toCharArray()).toString();

	}

	private String remove(final String str, final String toRemove,
			final int location) {

		StringBuffer buffer = new StringBuffer(str);
		return buffer.replace(location, location + toRemove.length(), "")
				.toString();

	}

	// Debug method

	private void setFQN(final String xml, final int index) {

		String eStart = "<";

		int starttag = goback(eStart, xml, index);
		int starttag1 = starttag + 1;
		String tagFQN1 = xml.substring(starttag1, index);
		tagFQN = tagFQN1 + tagname;
		// System.out.println("tagFQN = "+tagFQN);

	}

	/*
	 * private void writeNchars(String name, String xml, int charnum) {
	 * 
	 * int i = 0; int j = 20; String out = "";
	 * 
	 * try { while (i < j) {
	 * 
	 * out = out + xml.charAt(charnum);
	 * 
	 * charnum++; i++;
	 * 
	 * }
	 * 
	 * System.out.println("Name = " + name + " position = " + charnum +
	 * " String + " + j + " = " + out); } catch (Exception excep) {
	 * 
	 * System.out.println("Error throw and = " + E);
	 * 
	 * } }
	 */
	static String replace(final String str, final String pattern,
			final String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

}

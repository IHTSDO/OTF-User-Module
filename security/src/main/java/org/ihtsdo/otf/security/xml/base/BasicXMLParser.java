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

	//private static final Log log = LogFactory.getLog(BasicXMLParser.class);
	private static final Logger log = Logger.getLogger(BasicXMLParser.class.getName());

	String TestURL = "C:/Data/test2.xml";

	String TestTag = "BSVResponse";

	// String TestTag = "wsa:MessageID";
	String TestString = "";

	String TestAttrib = "ErrorStatus";

	String TagFQN = "";

	String tagname = "";

	//GetFileURL gfu = new GetFileURL();

	/**
	 * 
	 */
	public BasicXMLParser() {
		super();
		try {
			// TestString = gfu.fileToString(TestURL);
			// System.out.println("Test URL = "+TestURL);
			// System.out.println("TestString = "+TestString);
		} catch (Exception E) {
			System.out.println("Exception thrown Test URL = " + TestURL
					+ " Exception = " + E.getMessage());

		}
	}

	public static void main(String[] args) {

		BasicXMLParser sht = new BasicXMLParser();
		sht.handleXML();

	}

	public void handleXML() {

		String rv = getElementValue(TestString, TestTag);
		//String Attval = getXMLAttribute(TestString, TestTag, TestAttrib);
		System.out.println("RetVal = " + rv);

	}
	
	public static String getFirstAttValue(String xml, String attName){
		String retval = "";
		
		String searchS = attName+"=\"";
		try {
			int starti = xml.indexOf(searchS);
			int openi = starti+searchS.length();
			// System.out.println("Tag1 int = "+Tag1);
			if (starti > -1) {
				int closei =  xml.indexOf("\"",openi);
				retval = xml.substring(openi, closei);
				
			}
		} catch (Exception e) {
			//log.error("Exception = " + e.getMessage(),e);
			//log.error("tag " + tag + " from " + xml);
			e.printStackTrace();
		}
		
		
		
		
		return retval;
		
	}
	
	public String getFirstElement(String xml, String tag){
		String retval = "";
		//System.out.println("tag = " +tag);
		//System.out.println("tag index = " +xml.indexOf(tag));
		tagname = tag;
		setFQN(xml,xml.indexOf(tag));
		tagname = TagFQN;
		String StartTag = "<"+tagname;
		
		//log.error("tagname = " +tagname);
		// System.out.println("tagname = " +tagname);
		/* test for an empty tag e.g. <bob/>
		 * if indexOf /> is not -1 but is less than >
		 */
		
		try {
			int Tag1 = xml.indexOf(StartTag);
			// System.out.println("Tag1 int = "+Tag1);
			if (Tag1 > -1) {
				int EmptyEli =  xml.indexOf("/>",Tag1);
				if(EmptyEli != -1 && EmptyEli < xml.indexOf('>',Tag1)) {
					EmptyEli = EmptyEli+2;
					retval = xml.substring(Tag1,EmptyEli);	
					
				}
				else{ 
				
				int pos1 = xml.indexOf("</"+tagname,Tag1);
			//	System.out.println("pos1 int = "+pos1);
				int pos2 = xml.indexOf(">",pos1);
			//	System.out.println("pos2 int = "+pos2);
				pos2 = pos2+1;
				retval = xml.substring(Tag1,pos2);
				}
				//System.out.println("XMl = "+xml.substring(Tag1,pos2));
				}

		} catch (Exception e) {
			//log.error("Exception = " + e.getMessage(),e);
			//log.error("tag " + tag + " from " + xml);
			e.printStackTrace();
		}
		return retval;
		
	}
	
	public Vector getElements(String xml, String tag){
		Vector v = new Vector();
		
		
		String retval = "";
		//System.out.println("tag = " +tag);
		//System.out.println("tag index = " +xml.indexOf(tag));
		tagname = tag;
		setFQN(xml,xml.indexOf(tag));
		tagname = TagFQN;
		String StartTag = "<"+tagname;
		int startI = 0;
		
		
		/* test for an empty tag e.g. <bob/>
		 * if indexOf /> is not -1 but is less than >
		 */
		//log.error("tagname = " +tagname);
		// System.out.println("tagname = " +tagname);
		try {
			int Tag1 = xml.indexOf(StartTag,startI);
			// System.out.println("Tag1AAAAA int = "+Tag1);
			 while (Tag1 > -1) {
				//int start = getEndTagStart(xml, Tag1);
				int EmptyEli =  xml.indexOf("/>",Tag1);
				if(EmptyEli != -1 && EmptyEli < xml.indexOf('>',Tag1)) {
					EmptyEli = EmptyEli+2;
					retval = xml.substring(Tag1,EmptyEli);	
					v.add(retval);
					startI = EmptyEli;
					Tag1 = xml.indexOf(StartTag,startI);
				}
				else{ 
				int pos1 = xml.indexOf("</"+tagname,Tag1);
			//	System.out.println("pos1 int = "+pos1);
				int pos2 = xml.indexOf(">",pos1);
			//	System.out.println("pos2 int = "+pos2);
				pos2 = pos2+1;
				retval = xml.substring(Tag1,pos2);
			//	System.out.println("retval = "+xml.substring(Tag1,pos2));
				v.add(retval);
				startI = pos2;
				Tag1 = xml.indexOf(StartTag,startI);
				}
			 }
		} catch (Exception e) {
			//log.error("Exception = " + e.getMessage(),e);
			//log.error("tag " + tag + " from " + xml);
			e.printStackTrace();
		}
		
		
		
		return v;
		
	}
	


	public String getElementValue(String xml, String tag) {
		String retval = "";

		tagname = tag;
		//log.error("tagname = " +tagname);
		// System.out.println("tagname = " +tagname);
		try {
			int Tag1 = xml.indexOf(tagname);
			// System.out.println("Tag1 int = "+Tag1);
			//log.error("GetElementValue Tag1 int = "+Tag1);
			// int start = goforward(EEnd1,xml,Tag1);
			// start = start +1;
			// int end = goforward(EStart,xml,Tag1);
			// log.error("Tag1 = " + Tag1);
			if (Tag1 > -1) {
				int start = getStartTagEnd(xml, Tag1);
				// log.error("GetElementValue start = " + start);
				int end = getEndTagStart(xml);
				// log.error("GetElementValue end = " + end);
				if (end > -1) {
					// log.error("end greater than -1 = " + end);
					retval = xml.substring(start, end);
				//	log.error("GetElementValue retval = "+retval);
				//	log.error("GetElementValue xml = "+xml);
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
			//log.error("Exception = " + e.getMessage(),e);
			log.log(Level.SEVERE,"Exception = " + e.getMessage(), e);
			log.severe("tag " + tag + " from " + xml);
			//e.printStackTrace();
		}
		return retval;
	}
	
	public String setElementValue(String xml, String tag,String NewValue) {
		tagname = tag;
		String retval = "";
		// System.out.println("tagname = " +tagname);
		try {
			int Tag1 = xml.indexOf(tagname);
			// System.out.println("Tag1 int = "+Tag1);

			// int start = goforward(EEnd1,xml,Tag1);
			// start = start +1;
			// int end = goforward(EStart,xml,Tag1);
			// log.error("Tag1 = " + Tag1);
			if (Tag1 > -1) {
				int start = getStartTagEnd(xml, Tag1);
				// log.error("start = " + start);
				int end = getEndTagStart(xml);
				// log.error("end = " + end);
				if (end > -1) {
					// log.error("end greater than -1 = " + end);
					String toReplace = xml.substring(start, end);
					log.severe("toReplace = " + toReplace);
					//set the value to NewValue
					retval = replace(xml, toReplace, NewValue);
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
			//log.error("Exception = " + e.getMessage(),e);
			log.log(Level.SEVERE,"Exception = " + e.getMessage(), e);
			log.severe("tag " + tag + " from " + xml);
			
		}
		return retval;
		
	}
	
	public String getXMLAttribute(String xml, String tag, String attributeName) {
		String eq = "=";
		String retval = "";
		String attdelim = "\"";
		attributeName = attributeName + eq;
		int Tag1 = xml.indexOf(tag);
		String Elem = getStartElement(xml, Tag1);
		System.out.println("getStartElement Elem = " + Elem);
		int Tag2 = Elem.indexOf(attributeName);
		// writeNchars("Attrib",Elem,Tag2);
		int StartAtt = goforward(attdelim, Elem, Tag2);
		StartAtt = StartAtt + 1;
		int EndAtt = goforward(attdelim, Elem, StartAtt);

		// writeNchars("AttribSTVAL",Elem,StartAtt);
		// writeNchars("AttribEndVAL",Elem,EndAtt);

		retval = Elem.substring(StartAtt, EndAtt);
		System.out.println("getXMLAttribute RetVal = " + retval);

		return retval;

	}

	private int goback(String find, String xml, int index) {

		int x = index;

		String TagS = "";
		TagS = TagS + xml.charAt(x);
		// System.out.println("TagS1= "+TagS +" int = "+x);

		// Count back to next find
		while (TagS.equalsIgnoreCase(find) == false) {
			// System.out.println("TagS= "+TagS +" int = "+x);
			TagS = "";
			x--;
			TagS = TagS + xml.charAt(x);

		}

		// TagS = TagS +xml.charAt(x -1);
		// System.out.println("TagS= "+TagS +" int = "+x);
		// writeNchars("x",xml,x);

		return x;
	}

	private int goforward(String find, String xml, int index) {

		int x = index;

		String TagS = "";
		TagS = TagS + xml.charAt(x);
		// System.out.println("TagS1= "+TagS +" int = "+x);
		// Count forward to next find
		while (TagS.equalsIgnoreCase(find) == false) {
			// System.out.println("TagS= "+TagS +" int = "+x);
			TagS = "";
			x++;
			TagS = TagS + xml.charAt(x);
		}
		TagS = TagS + xml.charAt(x - 1);
		// System.out.println("TagS= "+TagS +" int = "+x);
		// writeNchars("x",xml,x);
		return x;
	}

	private int getStartTagEnd(String xml, int index) {
		int start = -1;
		setFQN(xml, index);
		String EEnd1 = ">";
		try {
			start = goforward(EEnd1, xml, index);
			start = start + 1;
		} catch (Exception E) {
			//log.error("getEndTagStart error thrown ", E);
			log.log(Level.SEVERE,"getEndTagStart error thrown ", E);
		}

		return start;

	}

	private int getEndTagStart(String xml) {
		int loc = -1;
		try {
			String EEnd = "</" + TagFQN;
			loc = xml.indexOf(EEnd);
		} catch (Exception E) {
			//log.error("getEndTagStart error thrown ", E);
			log.log(Level.SEVERE,"getEndTagStart error thrown ", E);
		}

		return loc;
	}

	private String getStartElement(String xml, int index) {
		String retval = "";
		String EStart = "<";

		int start = goback(EStart, xml, index);
		// writeNchars("Start_Element",xml,start);
		int end = getStartTagEnd(xml, index);
		// writeNchars("End_Element",xml,end);

		retval = xml.substring(start, end);
		// System.out.println("getStartElement Retval = " +retval);

		return retval;
	}

	public String insertElementValue(String xml, String tag, String InsContent) {

		String retval = "";
		int Tag1 = xml.indexOf(tag);

		int loc = getStartTagEnd(xml, Tag1);

		retval = insert(xml, InsContent, loc);

		return retval;
	}

	public String removeElementValue(String xml, String tag, String RemContent) {

		String retval = "";
		int Tag1 = xml.indexOf(tag);

		int loc = getStartTagEnd(xml, Tag1);
		retval = remove(xml, RemContent, loc);

		return retval;
	}

	public String removeAttribValue(String xml, String attributeName) {

		String eq = "=";
		String retval = "";
		String attdelim = "\"";
		attributeName = attributeName + eq;

		while (xml.indexOf(attributeName) > -1) {

			int StartAtt1 = xml.indexOf(attributeName);
			int StopAtt1 = goforward(attdelim, xml, StartAtt1);
			StopAtt1 = StopAtt1 + 1;

			// System.out.println("StartAtt1 = "+StartAtt1);
			// System.out.println("StopAtt1 = "+StopAtt1);

			retval = xml.substring(StartAtt1, StopAtt1);
			// System.out.println("retval = "+retval);

			int StopAtt2 = goforward(attdelim, xml, StopAtt1);
			StopAtt2 = StopAtt2 + 1;
			// System.out.println("StopAtt2 = "+StopAtt2);
			retval = xml.substring(StartAtt1, StopAtt2);
			// System.out.println("retval2 = "+retval);

			// System.out.println("substring1 = "+xml.substring(StartAtt1,
			// StopAtt2));

			String StartXML = xml.substring(0, StartAtt1);
			// System.out.println("substring = "+StartXML);
			String EndXML = xml.substring(StopAtt2);
			// System.out.println("substring2 = "+xml.substring(StartAtt1,
			// StopAtt2));
			xml = StartXML + EndXML;

		}

		retval = xml;
		//System.out.println("retval = " + retval);

		return retval;

	}

	public String removeCDATA(String xml) {

		String CStart = "<![CDATA[";
		String CEnd = "]]>";
		String retval = "";

		int loc = xml.indexOf(CStart);

		String frontchop = remove(xml, CStart, loc);

		loc = frontchop.indexOf(CEnd);

		retval = remove(frontchop, CEnd, loc);

		// System.out.println("XML before CD Rem"+xml);
		// System.out.println("After CDREM"+retval);

		return retval;
	}

	private String insert(String str, String toInsert, int location) {

		StringBuffer buffer = new StringBuffer(str);
		return buffer.insert(location, toInsert.toCharArray()).toString();

	}

	private String remove(String str, String toRemove, int location) {

		StringBuffer buffer = new StringBuffer(str);
		return buffer.replace(location, location + toRemove.length(), "")
				.toString();

	}

	// Debug method

	private void setFQN(String xml, int index) {

		String EStart = "<";

		int starttag = goback(EStart, xml, index);
		int starttag1 = starttag + 1;
		String TagFQN1 = xml.substring(starttag1, index);
		TagFQN = TagFQN1 + tagname;
		// System.out.println("TagFQN = "+TagFQN);

	}

/*	private void writeNchars(String name, String xml, int charnum) {

		int i = 0;
		int j = 20;
		String out = "";

		try {
			while (i < j) {

				out = out + xml.charAt(charnum);

				charnum++;
				i++;

			}

			System.out.println("Name = " + name + " position = " + charnum
					+ " String + " + j + " = " + out);
		} catch (Exception E) {

			System.out.println("Error throw and = " + E);

		}
	}
	*/
    static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
    
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }


}

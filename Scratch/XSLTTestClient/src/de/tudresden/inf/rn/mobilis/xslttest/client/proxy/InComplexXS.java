package de.tudresden.inf.rn.mobilis.xslttest.client.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPInfo;

public class InComplexXS implements XMPPInfo {

	private long inVal = Long.MIN_VALUE;


	public InComplexXS( long inVal ) {
		super();
		this.inVal = inVal;
	}

	public InComplexXS(){}



	@Override
	public void fromXML( XmlPullParser parser ) throws Exception {
		boolean done = false;
			
		do {
			switch (parser.getEventType()) {
			case XmlPullParser.START_TAG:
				String tagName = parser.getName();
				
				if (tagName.equals(getChildElement())) {
					parser.next();
				}
				else if (tagName.equals( "inVal" ) ) {
					this.inVal = Long.parseLong( parser.nextText() );
				}
				else
					parser.next();
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals(getChildElement()))
					done = true;
				else
					parser.next();
				break;
			case XmlPullParser.END_DOCUMENT:
				done = true;
				break;
			default:
				parser.next();
			}
		} while (!done);
	}

	public static final String CHILD_ELEMENT = "InComplexXS";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "http://mobilis.inf.tu-dresden.de#services/XSLTTestService#type:InComplexXS";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<inVal>" )
			.append( this.inVal )
			.append( "</inVal>" );

		return sb.toString();
	}



	public long getInVal() {
		return this.inVal;
	}

	public void setInVal( long inVal ) {
		this.inVal = inVal;
	}

}
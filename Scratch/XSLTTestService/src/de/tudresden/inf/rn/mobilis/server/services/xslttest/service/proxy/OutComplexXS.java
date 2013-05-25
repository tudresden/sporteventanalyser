package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPInfo;

public class OutComplexXS implements XMPPInfo {

	private long outVal = Long.MIN_VALUE;


	public OutComplexXS( long outVal ) {
		super();
		this.outVal = outVal;
	}

	public OutComplexXS(){}



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
				else if (tagName.equals( "outVal" ) ) {
					this.outVal = Long.parseLong( parser.nextText() );
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

	public static final String CHILD_ELEMENT = "OutComplexXS";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "http://mobilis.inf.tu-dresden.de#services/XSLTTestService#type:OutComplexXS";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<outVal>" )
			.append( this.outVal )
			.append( "</outVal>" );

		return sb.toString();
	}



	public long getOutVal() {
		return this.outVal;
	}

	public void setOutVal( long outVal ) {
		this.outVal = outVal;
	}

}
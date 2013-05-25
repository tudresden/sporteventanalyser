package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPInfo;

public class OutComplexXSSequence implements XMPPInfo {

	private List< Long > outSeq = new ArrayList< Long >();


	public OutComplexXSSequence( List< Long > outSeq ) {
		super();
		for ( long entity : outSeq ) {
			this.outSeq.add( entity );
		}
	}

	public OutComplexXSSequence(){}



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
				else if (tagName.equals( "outSeq" ) ) {
					outSeq.add( Long.parseLong( parser.nextText() ) );
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

	public static final String CHILD_ELEMENT = "OutComplexXSSequence";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "http://mobilis.inf.tu-dresden.de#services/XSLTTestService#type:OutComplexXSSequence";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		for( long entry : outSeq ) {
			sb.append( "<outSeq>" );
			sb.append( entry );
			sb.append( "</outSeq>" );
		}

		return sb.toString();
	}



	public List< Long > getOutSeq() {
		return this.outSeq;
	}

	public void setOutSeq( List< Long > outSeq ) {
		this.outSeq = outSeq;
	}

}
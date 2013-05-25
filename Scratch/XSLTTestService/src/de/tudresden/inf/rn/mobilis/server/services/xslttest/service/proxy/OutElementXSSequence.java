package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class OutElementXSSequence extends XMPPBean {

	private List< Long > outSeq = new ArrayList< Long >();


	public OutElementXSSequence( List< Long > outSeq ) {
		super();
		for ( long entity : outSeq ) {
			this.outSeq.add( entity );
		}

		this.setType( XMPPBean.TYPE_RESULT );
	}

	public OutElementXSSequence(){
		this.setType( XMPPBean.TYPE_RESULT );
	}


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
				else if (tagName.equals("error")) {
					parser = parseErrorAttributes(parser);
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

	public static final String CHILD_ELEMENT = "OutElementXSSequence";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "xsltt:iq:inoutwithfault";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public XMPPBean clone() {
		OutElementXSSequence clone = new OutElementXSSequence( outSeq );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		for( long entry : outSeq ) {
			sb.append( "<outSeq>" );
			sb.append( entry );
			sb.append( "</outSeq>" );
		}

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public List< Long > getOutSeq() {
		return this.outSeq;
	}

	public void setOutSeq( List< Long > outSeq ) {
		this.outSeq = outSeq;
	}

}
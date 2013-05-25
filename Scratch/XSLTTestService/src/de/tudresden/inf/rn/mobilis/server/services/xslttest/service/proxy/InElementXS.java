package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class InElementXS extends XMPPBean {

	private long inVal = Long.MIN_VALUE;


	public InElementXS( long inVal ) {
		super();
		this.inVal = inVal;

		this.setType( XMPPBean.TYPE_RESULT );
	}

	public InElementXS(){
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
				else if (tagName.equals( "inVal" ) ) {
					this.inVal = Long.parseLong( parser.nextText() );
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

	public static final String CHILD_ELEMENT = "InElementXS";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "xsltt:iq:outinwithfault";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public XMPPBean clone() {
		InElementXS clone = new InElementXS( inVal );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<inVal>" )
			.append( this.inVal )
			.append( "</inVal>" );

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public long getInVal() {
		return this.inVal;
	}

	public void setInVal( long inVal ) {
		this.inVal = inVal;
	}

}
package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class OutElementXS extends XMPPBean {

	private long outVal = Long.MIN_VALUE;


	public OutElementXS( long outVal ) {
		super();
		this.outVal = outVal;

		this.setType( XMPPBean.TYPE_GET );
	}

	public OutElementXS(){
		this.setType( XMPPBean.TYPE_GET );
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
				else if (tagName.equals( "outVal" ) ) {
					this.outVal = Long.parseLong( parser.nextText() );
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

	public static final String CHILD_ELEMENT = "OutElementXS";

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
		OutElementXS clone = new OutElementXS( outVal );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<outVal>" )
			.append( this.outVal )
			.append( "</outVal>" );

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public long getOutVal() {
		return this.outVal;
	}

	public void setOutVal( long outVal ) {
		this.outVal = outVal;
	}

}
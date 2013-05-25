package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class InElementXSSequence extends XMPPBean {

	private List< Long > inSeq = new ArrayList< Long >();


	public InElementXSSequence( List< Long > inSeq ) {
		super();
		for ( long entity : inSeq ) {
			this.inSeq.add( entity );
		}

		this.setType( XMPPBean.TYPE_GET );
	}

	public InElementXSSequence(){
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
				else if (tagName.equals( "inSeq" ) ) {
					inSeq.add( Long.parseLong( parser.nextText() ) );
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

	public static final String CHILD_ELEMENT = "InElementXSSequence";

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
		InElementXSSequence clone = new InElementXSSequence( inSeq );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		for( long entry : inSeq ) {
			sb.append( "<inSeq>" );
			sb.append( entry );
			sb.append( "</inSeq>" );
		}

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public InElementXSSequence buildOutFaultSimple(String detailedErrorText){
		InElementXSSequence fault = ( InElementXSSequence )this.clone();

		fault.setTo( this.getFrom() );
    	fault.setId(this.getId());
		fault.setType( XMPPBean.TYPE_ERROR );
		fault.errorType = "cancel";
		fault.errorCondition = "conflict";
		fault.errorText = "Fault outgoing";

		if(null != detailedErrorText && detailedErrorText.length() > 0)
			fault.errorText += " Detail: " + detailedErrorText;
		
		return fault;
	}





	public List< Long > getInSeq() {
		return this.inSeq;
	}

	public void setInSeq( List< Long > inSeq ) {
		this.inSeq = inSeq;
	}

}
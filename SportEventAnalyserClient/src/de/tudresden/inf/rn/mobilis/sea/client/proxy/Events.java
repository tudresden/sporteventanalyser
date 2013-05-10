package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import org.xmlpull.v1.XmlPullParser;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

import java.util.List;import java.util.ArrayList;

public class Events extends XMPPBean {

	private List< Event > Sender = new ArrayList< Event >();


	public Events( List< Event > Sender ) {
		super();
		for ( Event entity : Sender ) {
			this.Sender.add( entity );
		}

		this.setType( XMPPBean.TYPE_SET );
	}

	public Events(){
		this.setType( XMPPBean.TYPE_SET );
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
				else if (tagName.equals( Event.CHILD_ELEMENT ) ) {
					Event entity = new Event();

					entity.fromXML( parser );
					this.Sender.add( entity );
					
					parser.next();
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

	public static final String CHILD_ELEMENT = "Events";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "sea:iq:eventnoti";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public XMPPBean clone() {
		Events clone = new Events( Sender );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		for( Event entry : this.Sender ) {
			sb.append( "<" + Event.CHILD_ELEMENT + ">" );
			sb.append( entry.toXML() );
			sb.append( "</" + Event.CHILD_ELEMENT + ">" );
		}

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public List< Event > getSender() {
		return this.Sender;
	}

	public void setSender( List< Event > Sender ) {
		this.Sender = Sender;
	}

}
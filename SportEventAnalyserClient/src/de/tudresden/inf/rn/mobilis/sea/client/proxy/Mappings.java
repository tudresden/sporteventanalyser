package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class Mappings extends XMPPBean {

	private List< Mapping > Mappings = new ArrayList< Mapping >();


	public Mappings( List< Mapping > Mappings ) {
		super();
		for ( Mapping entity : Mappings ) {
			this.Mappings.add( entity );
		}

		this.setType( XMPPBean.TYPE_RESULT );
	}

	public Mappings(){
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
				else if (tagName.equals( Mapping.CHILD_ELEMENT ) ) {
					Mapping entity = new Mapping();

					entity.fromXML( parser );
					this.Mappings.add( entity );
					
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

	public static final String CHILD_ELEMENT = "Mappings";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "sea:iq:playermappings";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public XMPPBean clone() {
		Mappings clone = new Mappings( Mappings );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		for( Mapping entry : this.Mappings ) {
			sb.append( "<" + Mapping.CHILD_ELEMENT + ">" );
			sb.append( entry.toXML() );
			sb.append( "</" + Mapping.CHILD_ELEMENT + ">" );
		}

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public List< Mapping > getMappings() {
		return this.Mappings;
	}

	public void setMappings( List< Mapping > Mappings ) {
		this.Mappings = Mappings;
	}

}
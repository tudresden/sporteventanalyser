package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class Mappings extends XMPPBean {

	private GameField GameFieldSize = new GameField();
	private List< Goal > Goals = new ArrayList< Goal >();
	private List< Mapping > PlayerMappings = new ArrayList< Mapping >();


	public Mappings( GameField GameFieldSize, List< Goal > Goals, List< Mapping > PlayerMappings ) {
		super();
		this.GameFieldSize = GameFieldSize;
		for ( Goal entity : Goals ) {
			this.Goals.add( entity );
		}
		for ( Mapping entity : PlayerMappings ) {
			this.PlayerMappings.add( entity );
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
				else if (tagName.equals( GameField.CHILD_ELEMENT ) ) {
					this.GameFieldSize.fromXML( parser );
				}
				else if (tagName.equals( Goal.CHILD_ELEMENT ) ) {
					Goal entity = new Goal();

					entity.fromXML( parser );
					this.Goals.add( entity );
					
					parser.next();
				}
				else if (tagName.equals( Mapping.CHILD_ELEMENT ) ) {
					Mapping entity = new Mapping();

					entity.fromXML( parser );
					this.PlayerMappings.add( entity );
					
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

	public static final String NAMESPACE = "sea:iq:gamemappings";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public XMPPBean clone() {
		Mappings clone = new Mappings( GameFieldSize, Goals, PlayerMappings );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<" + this.GameFieldSize.getChildElement() + ">" )
			.append( this.GameFieldSize.toXML() )
			.append( "</" + this.GameFieldSize.getChildElement() + ">" );

		for( Goal entry : this.Goals ) {
			sb.append( "<" + Goal.CHILD_ELEMENT + ">" );
			sb.append( entry.toXML() );
			sb.append( "</" + Goal.CHILD_ELEMENT + ">" );
		}

		for( Mapping entry : this.PlayerMappings ) {
			sb.append( "<" + Mapping.CHILD_ELEMENT + ">" );
			sb.append( entry.toXML() );
			sb.append( "</" + Mapping.CHILD_ELEMENT + ">" );
		}

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public GameField getGameFieldSize() {
		return this.GameFieldSize;
	}

	public void setGameFieldSize( GameField GameFieldSize ) {
		this.GameFieldSize = GameFieldSize;
	}

	public List< Goal > getGoals() {
		return this.Goals;
	}

	public void setGoals( List< Goal > Goals ) {
		this.Goals = Goals;
	}

	public List< Mapping > getPlayerMappings() {
		return this.PlayerMappings;
	}

	public void setPlayerMappings( List< Mapping > PlayerMappings ) {
		this.PlayerMappings = PlayerMappings;
	}

}
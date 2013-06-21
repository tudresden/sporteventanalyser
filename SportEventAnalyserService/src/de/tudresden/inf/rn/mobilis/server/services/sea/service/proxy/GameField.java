package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPInfo;

public class GameField implements XMPPInfo {

	private int GameFieldMinX = Integer.MIN_VALUE;
	private int GameFieldMaxX = Integer.MIN_VALUE;
	private int GameFieldMinY = Integer.MIN_VALUE;
	private int GameFieldMaxY = Integer.MIN_VALUE;


	public GameField( int GameFieldMinX, int GameFieldMaxX, int GameFieldMinY, int GameFieldMaxY ) {
		super();
		this.GameFieldMinX = GameFieldMinX;
		this.GameFieldMaxX = GameFieldMaxX;
		this.GameFieldMinY = GameFieldMinY;
		this.GameFieldMaxY = GameFieldMaxY;
	}

	public GameField(){}



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
				else if (tagName.equals( "GameFieldMinX" ) ) {
					this.GameFieldMinX = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "GameFieldMaxX" ) ) {
					this.GameFieldMaxX = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "GameFieldMinY" ) ) {
					this.GameFieldMinY = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "GameFieldMaxY" ) ) {
					this.GameFieldMaxY = Integer.parseInt( parser.nextText() );
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

	public static final String CHILD_ELEMENT = "GameField";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "http://mobilis.inf.tu-dresden.de#services/SportEventAnalyserService#type:GameField";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<GameFieldMinX>" )
			.append( this.GameFieldMinX )
			.append( "</GameFieldMinX>" );

		sb.append( "<GameFieldMaxX>" )
			.append( this.GameFieldMaxX )
			.append( "</GameFieldMaxX>" );

		sb.append( "<GameFieldMinY>" )
			.append( this.GameFieldMinY )
			.append( "</GameFieldMinY>" );

		sb.append( "<GameFieldMaxY>" )
			.append( this.GameFieldMaxY )
			.append( "</GameFieldMaxY>" );

		return sb.toString();
	}



	public int getGameFieldMinX() {
		return this.GameFieldMinX;
	}

	public void setGameFieldMinX( int GameFieldMinX ) {
		this.GameFieldMinX = GameFieldMinX;
	}

	public int getGameFieldMaxX() {
		return this.GameFieldMaxX;
	}

	public void setGameFieldMaxX( int GameFieldMaxX ) {
		this.GameFieldMaxX = GameFieldMaxX;
	}

	public int getGameFieldMinY() {
		return this.GameFieldMinY;
	}

	public void setGameFieldMinY( int GameFieldMinY ) {
		this.GameFieldMinY = GameFieldMinY;
	}

	public int getGameFieldMaxY() {
		return this.GameFieldMaxY;
	}

	public void setGameFieldMaxY( int GameFieldMaxY ) {
		this.GameFieldMaxY = GameFieldMaxY;
	}

}
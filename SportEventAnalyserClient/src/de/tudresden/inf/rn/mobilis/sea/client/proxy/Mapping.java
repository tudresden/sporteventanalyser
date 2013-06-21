package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPInfo;

public class Mapping implements XMPPInfo {

	private int PlayerID = Integer.MIN_VALUE;
	private String PlayerName = null;
	private String TeamName = null;


	public Mapping( int PlayerID, String PlayerName, String TeamName ) {
		super();
		this.PlayerID = PlayerID;
		this.PlayerName = PlayerName;
		this.TeamName = TeamName;
	}

	public Mapping(){}



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
				else if (tagName.equals( "PlayerID" ) ) {
					this.PlayerID = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "PlayerName" ) ) {
					this.PlayerName = parser.nextText();
				}
				else if (tagName.equals( "TeamName" ) ) {
					this.TeamName = parser.nextText();
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

	public static final String CHILD_ELEMENT = "Mapping";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "http://mobilis.inf.tu-dresden.de#services/SportEventAnalyserService#type:Mapping";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<PlayerID>" )
			.append( this.PlayerID )
			.append( "</PlayerID>" );

		sb.append( "<PlayerName>" )
			.append( this.PlayerName )
			.append( "</PlayerName>" );

		sb.append( "<TeamName>" )
			.append( this.TeamName )
			.append( "</TeamName>" );

		return sb.toString();
	}



	public int getPlayerID() {
		return this.PlayerID;
	}

	public void setPlayerID( int PlayerID ) {
		this.PlayerID = PlayerID;
	}

	public String getPlayerName() {
		return this.PlayerName;
	}

	public void setPlayerName( String PlayerName ) {
		this.PlayerName = PlayerName;
	}

	public String getTeamName() {
		return this.TeamName;
	}

	public void setTeamName( String TeamName ) {
		this.TeamName = TeamName;
	}

}
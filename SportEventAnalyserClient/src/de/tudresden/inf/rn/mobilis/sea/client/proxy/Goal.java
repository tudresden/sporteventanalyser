package de.tudresden.inf.rn.mobilis.sea.client.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPInfo;

public class Goal implements XMPPInfo {

	private int GoalMinX = Integer.MIN_VALUE;
	private int GoalMaxX = Integer.MIN_VALUE;


	public Goal( int GoalMinX, int GoalMaxX ) {
		super();
		this.GoalMinX = GoalMinX;
		this.GoalMaxX = GoalMaxX;
	}

	public Goal(){}



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
				else if (tagName.equals( "GoalMinX" ) ) {
					this.GoalMinX = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "GoalMaxX" ) ) {
					this.GoalMaxX = Integer.parseInt( parser.nextText() );
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

	public static final String CHILD_ELEMENT = "Goal";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "http://mobilis.inf.tu-dresden.de#services/SportEventAnalyserService#type:Goal";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<GoalMinX>" )
			.append( this.GoalMinX )
			.append( "</GoalMinX>" );

		sb.append( "<GoalMaxX>" )
			.append( this.GoalMaxX )
			.append( "</GoalMaxX>" );

		return sb.toString();
	}



	public int getGoalMinX() {
		return this.GoalMinX;
	}

	public void setGoalMinX( int GoalMinX ) {
		this.GoalMinX = GoalMinX;
	}

	public int getGoalMaxX() {
		return this.GoalMaxX;
	}

	public void setGoalMaxX( int GoalMaxX ) {
		this.GoalMaxX = GoalMaxX;
	}

}
package de.tudresden.inf.rn.mobilis.server.services.sea.service.proxy;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPInfo;

public class Event implements XMPPInfo {

	private int Sender = Integer.MIN_VALUE;
	private long Timestamp = Long.MIN_VALUE;
	private int PositionX = Integer.MIN_VALUE;
	private int PositionY = Integer.MIN_VALUE;
	private int PositionZ = Integer.MIN_VALUE;
	private int Velocity = Integer.MIN_VALUE;
	private int Acceleration = Integer.MIN_VALUE;
	private int VelocityX = Integer.MIN_VALUE;
	private int VelocityY = Integer.MIN_VALUE;
	private int VelocityZ = Integer.MIN_VALUE;
	private int AccelerationX = Integer.MIN_VALUE;
	private int AccelerationY = Integer.MIN_VALUE;
	private int AccelerationZ = Integer.MIN_VALUE;


	public Event( int Sender, long Timestamp, int PositionX, int PositionY, int PositionZ, int Velocity, int Acceleration, int VelocityX, int VelocityY, int VelocityZ, int AccelerationX, int AccelerationY, int AccelerationZ ) {
		super();
		this.Sender = Sender;
		this.Timestamp = Timestamp;
		this.PositionX = PositionX;
		this.PositionY = PositionY;
		this.PositionZ = PositionZ;
		this.Velocity = Velocity;
		this.Acceleration = Acceleration;
		this.VelocityX = VelocityX;
		this.VelocityY = VelocityY;
		this.VelocityZ = VelocityZ;
		this.AccelerationX = AccelerationX;
		this.AccelerationY = AccelerationY;
		this.AccelerationZ = AccelerationZ;
	}

	public Event(){}



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
				else if (tagName.equals( "Sender" ) ) {
					this.Sender = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "Timestamp" ) ) {
					this.Timestamp = Long.parseLong( parser.nextText() );
				}
				else if (tagName.equals( "PositionX" ) ) {
					this.PositionX = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "PositionY" ) ) {
					this.PositionY = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "PositionZ" ) ) {
					this.PositionZ = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "Velocity" ) ) {
					this.Velocity = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "Acceleration" ) ) {
					this.Acceleration = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "VelocityX" ) ) {
					this.VelocityX = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "VelocityY" ) ) {
					this.VelocityY = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "VelocityZ" ) ) {
					this.VelocityZ = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "AccelerationX" ) ) {
					this.AccelerationX = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "AccelerationY" ) ) {
					this.AccelerationY = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "AccelerationZ" ) ) {
					this.AccelerationZ = Integer.parseInt( parser.nextText() );
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

	public static final String CHILD_ELEMENT = "Event";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "http://mobilis.inf.tu-dresden.de#services/SportEventAnalyserService#type:Event";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<Sender>" )
			.append( this.Sender )
			.append( "</Sender>" );

		sb.append( "<Timestamp>" )
			.append( this.Timestamp )
			.append( "</Timestamp>" );

		sb.append( "<PositionX>" )
			.append( this.PositionX )
			.append( "</PositionX>" );

		sb.append( "<PositionY>" )
			.append( this.PositionY )
			.append( "</PositionY>" );

		sb.append( "<PositionZ>" )
			.append( this.PositionZ )
			.append( "</PositionZ>" );

		sb.append( "<Velocity>" )
			.append( this.Velocity )
			.append( "</Velocity>" );

		sb.append( "<Acceleration>" )
			.append( this.Acceleration )
			.append( "</Acceleration>" );

		sb.append( "<VelocityX>" )
			.append( this.VelocityX )
			.append( "</VelocityX>" );

		sb.append( "<VelocityY>" )
			.append( this.VelocityY )
			.append( "</VelocityY>" );

		sb.append( "<VelocityZ>" )
			.append( this.VelocityZ )
			.append( "</VelocityZ>" );

		sb.append( "<AccelerationX>" )
			.append( this.AccelerationX )
			.append( "</AccelerationX>" );

		sb.append( "<AccelerationY>" )
			.append( this.AccelerationY )
			.append( "</AccelerationY>" );

		sb.append( "<AccelerationZ>" )
			.append( this.AccelerationZ )
			.append( "</AccelerationZ>" );

		return sb.toString();
	}



	public int getSender() {
		return this.Sender;
	}

	public void setSender( int Sender ) {
		this.Sender = Sender;
	}

	public long getTimestamp() {
		return this.Timestamp;
	}

	public void setTimestamp( long Timestamp ) {
		this.Timestamp = Timestamp;
	}

	public int getPositionX() {
		return this.PositionX;
	}

	public void setPositionX( int PositionX ) {
		this.PositionX = PositionX;
	}

	public int getPositionY() {
		return this.PositionY;
	}

	public void setPositionY( int PositionY ) {
		this.PositionY = PositionY;
	}

	public int getPositionZ() {
		return this.PositionZ;
	}

	public void setPositionZ( int PositionZ ) {
		this.PositionZ = PositionZ;
	}

	public int getVelocity() {
		return this.Velocity;
	}

	public void setVelocity( int Velocity ) {
		this.Velocity = Velocity;
	}

	public int getAcceleration() {
		return this.Acceleration;
	}

	public void setAcceleration( int Acceleration ) {
		this.Acceleration = Acceleration;
	}

	public int getVelocityX() {
		return this.VelocityX;
	}

	public void setVelocityX( int VelocityX ) {
		this.VelocityX = VelocityX;
	}

	public int getVelocityY() {
		return this.VelocityY;
	}

	public void setVelocityY( int VelocityY ) {
		this.VelocityY = VelocityY;
	}

	public int getVelocityZ() {
		return this.VelocityZ;
	}

	public void setVelocityZ( int VelocityZ ) {
		this.VelocityZ = VelocityZ;
	}

	public int getAccelerationX() {
		return this.AccelerationX;
	}

	public void setAccelerationX( int AccelerationX ) {
		this.AccelerationX = AccelerationX;
	}

	public int getAccelerationY() {
		return this.AccelerationY;
	}

	public void setAccelerationY( int AccelerationY ) {
		this.AccelerationY = AccelerationY;
	}

	public int getAccelerationZ() {
		return this.AccelerationZ;
	}

	public void setAccelerationZ( int AccelerationZ ) {
		this.AccelerationZ = AccelerationZ;
	}

}
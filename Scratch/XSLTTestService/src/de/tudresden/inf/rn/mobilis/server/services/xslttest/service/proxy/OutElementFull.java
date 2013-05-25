package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;

public class OutElementFull extends XMPPBean {

	private int intVal = Integer.MIN_VALUE;
	private long longVal = Long.MIN_VALUE;
	private boolean booleanVal = false;
	private double doubleVal = Double.MIN_VALUE;
	private float floatVal = Float.MIN_VALUE;
	private byte byteVal = Byte.MIN_VALUE;
	private short shortVal = Short.MIN_VALUE;
	private String stringVal = null;
	private List< OutComplexXSSequence > outComplexSeq = new ArrayList< OutComplexXSSequence >();


	public OutElementFull( int intVal, long longVal, boolean booleanVal, double doubleVal, float floatVal, byte byteVal, short shortVal, String stringVal, List< OutComplexXSSequence > outComplexSeq ) {
		super();
		this.intVal = intVal;
		this.longVal = longVal;
		this.booleanVal = booleanVal;
		this.doubleVal = doubleVal;
		this.floatVal = floatVal;
		this.byteVal = byteVal;
		this.shortVal = shortVal;
		this.stringVal = stringVal;
		for ( OutComplexXSSequence entity : outComplexSeq ) {
			this.outComplexSeq.add( entity );
		}

		this.setType( XMPPBean.TYPE_SET );
	}

	public OutElementFull(){
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
				else if (tagName.equals( "intVal" ) ) {
					this.intVal = Integer.parseInt( parser.nextText() );
				}
				else if (tagName.equals( "longVal" ) ) {
					this.longVal = Long.parseLong( parser.nextText() );
				}
				else if (tagName.equals( "booleanVal" ) ) {
					this.booleanVal = Boolean.parseBoolean( parser.nextText() );
				}
				else if (tagName.equals( "doubleVal" ) ) {
					this.doubleVal = Double.parseDouble( parser.nextText() );
				}
				else if (tagName.equals( "floatVal" ) ) {
					this.floatVal = Float.parseFloat( parser.nextText() );
				}
				else if (tagName.equals( "byteVal" ) ) {
					this.byteVal = Byte.parseByte( parser.nextText() );
				}
				else if (tagName.equals( "shortVal" ) ) {
					this.shortVal = Short.parseShort( parser.nextText() );
				}
				else if (tagName.equals( "stringVal" ) ) {
					this.stringVal = parser.nextText();
				}
				else if (tagName.equals( OutComplexXSSequence.CHILD_ELEMENT ) ) {
					OutComplexXSSequence entity = new OutComplexXSSequence();

					entity.fromXML( parser );
					this.outComplexSeq.add( entity );
					
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

	public static final String CHILD_ELEMENT = "OutElementFull";

	@Override
	public String getChildElement() {
		return CHILD_ELEMENT;
	}

	public static final String NAMESPACE = "xsltt:iq:outonlywithfault";

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public XMPPBean clone() {
		OutElementFull clone = new OutElementFull( intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outComplexSeq );
		clone.cloneBasicAttributes( clone );

		return clone;
	}

	@Override
	public String payloadToXML() {
		StringBuilder sb = new StringBuilder();

		sb.append( "<intVal>" )
			.append( this.intVal )
			.append( "</intVal>" );

		sb.append( "<longVal>" )
			.append( this.longVal )
			.append( "</longVal>" );

		sb.append( "<booleanVal>" )
			.append( this.booleanVal )
			.append( "</booleanVal>" );

		sb.append( "<doubleVal>" )
			.append( this.doubleVal )
			.append( "</doubleVal>" );

		sb.append( "<floatVal>" )
			.append( this.floatVal )
			.append( "</floatVal>" );

		sb.append( "<byteVal>" )
			.append( this.byteVal )
			.append( "</byteVal>" );

		sb.append( "<shortVal>" )
			.append( this.shortVal )
			.append( "</shortVal>" );

		sb.append( "<stringVal>" )
			.append( this.stringVal )
			.append( "</stringVal>" );

		for( OutComplexXSSequence entry : outComplexSeq ) {
			sb.append( "<" + OutComplexXSSequence.CHILD_ELEMENT + ">" );
			sb.append( entry.toXML() );
			sb.append( "</" + OutComplexXSSequence.CHILD_ELEMENT + ">" );
		}

		sb = appendErrorPayload(sb);

		return sb.toString();
	}


	public int getIntVal() {
		return this.intVal;
	}

	public void setIntVal( int intVal ) {
		this.intVal = intVal;
	}

	public long getLongVal() {
		return this.longVal;
	}

	public void setLongVal( long longVal ) {
		this.longVal = longVal;
	}

	public boolean getBooleanVal() {
		return this.booleanVal;
	}

	public void setBooleanVal( boolean booleanVal ) {
		this.booleanVal = booleanVal;
	}

	public double getDoubleVal() {
		return this.doubleVal;
	}

	public void setDoubleVal( double doubleVal ) {
		this.doubleVal = doubleVal;
	}

	public float getFloatVal() {
		return this.floatVal;
	}

	public void setFloatVal( float floatVal ) {
		this.floatVal = floatVal;
	}

	public byte getByteVal() {
		return this.byteVal;
	}

	public void setByteVal( byte byteVal ) {
		this.byteVal = byteVal;
	}

	public short getShortVal() {
		return this.shortVal;
	}

	public void setShortVal( short shortVal ) {
		this.shortVal = shortVal;
	}

	public String getStringVal() {
		return this.stringVal;
	}

	public void setStringVal( String stringVal ) {
		this.stringVal = stringVal;
	}

	public List< OutComplexXSSequence > getOutComplexSeq() {
		return this.outComplexSeq;
	}

	public void setOutComplexSeq( List< OutComplexXSSequence > outComplexSeq ) {
		this.outComplexSeq = outComplexSeq;
	}

}
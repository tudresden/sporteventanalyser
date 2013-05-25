package de.tudresden.inf.rn.mobilis.xslttest.client.proxy;

import java.util.List;

import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
public class XSLTTestProxy {

	private IXSLTTestOutgoing _bindingStub;


	public XSLTTestProxy( IXSLTTestOutgoing bindingStub) {
		_bindingStub = bindingStub;
	}


	public IXSLTTestOutgoing getBindingStub(){
		return _bindingStub;
	}


	public void InOnlyOperation( String toJid ) {
		if ( null == _bindingStub )
			return;

		InElement out = new InElement(  );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out );
	}

	public void InOnlyOperationWithFault( String toJid, int intVal, long longVal, boolean booleanVal, double doubleVal, float floatVal, byte byteVal, short shortVal, String stringVal, List< InComplexXSSequence > inComplexSeq ) {
		if ( null == _bindingStub )
			return;

		InElementFull out = new InElementFull( intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, inComplexSeq );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out );
	}

	public void OutInOperationWithFault( String toJid, String packetId, long inVal ) {
		if ( null == _bindingStub )
			return;

		InElementXS out = new InElementXS( inVal );
		out.setTo( toJid );
		out.setId( packetId );

		_bindingStub.sendXMPPBean( out );
	}

	public XMPPBean InOutOperationWithFault( String toJid, List< Long > inSeq, IXMPPCallback< OutElementXSSequence > callback ) {
		if ( null == _bindingStub || null == callback )
			return null;

		InElementXSSequence out = new InElementXSSequence( inSeq );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out, callback );

		return out;
	}

}
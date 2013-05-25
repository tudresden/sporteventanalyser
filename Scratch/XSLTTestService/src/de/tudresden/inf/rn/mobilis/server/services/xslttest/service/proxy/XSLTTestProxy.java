package de.tudresden.inf.rn.mobilis.server.services.xslttest.service.proxy;

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


	public void OutOnlyOperation( String toJid ) {
		if ( null == _bindingStub )
			return;

		OutElement out = new OutElement(  );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out );
	}

	public void OutOnlyOperationWithFault( String toJid, int intVal, long longVal, boolean booleanVal, double doubleVal, float floatVal, byte byteVal, short shortVal, String stringVal, List< OutComplexXSSequence > outComplexSeq ) {
		if ( null == _bindingStub )
			return;

		OutElementFull out = new OutElementFull( intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outComplexSeq );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out );
	}

	public void OutInOperationWithFault( String toJid, long outVal, IXMPPCallback< InElementXS > callback ) {
		if ( null == _bindingStub || null == callback )
			return;

		OutElementXS out = new OutElementXS( outVal );
		out.setTo( toJid );

		_bindingStub.sendXMPPBean( out, callback );
	}

	public XMPPBean InOutOperationWithFault( String toJid, String packetId, List< Long > outSeq ) {
		if ( null == _bindingStub )
			return null;

		OutElementXSSequence out = new OutElementXSSequence( outSeq );
		out.setTo( toJid );
		out.setId( packetId );

		_bindingStub.sendXMPPBean( out );

		return out;
	}

}
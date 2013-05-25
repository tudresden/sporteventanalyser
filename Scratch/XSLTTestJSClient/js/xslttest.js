var xslttest = {

	HTTPBIND : "http://127.0.0.1:7070/http-bind/",

	createOutComplexXSSequence : function(outSeq) {
		return new Mobilis.xslttest.ELEMENTS.OutComplexXSSequence(outSeq);
	},
	createInComplexXSSequence : function(inSeq) {
		return new Mobilis.xslttest.ELEMENTS.InComplexXSSequence(inSeq);
	},
	createOutComplexXS : function(outVal) {
		return new Mobilis.xslttest.ELEMENTS.OutComplexXS(outVal);
	},
	createInComplexXS : function(inVal) {
		return new Mobilis.xslttest.ELEMENTS.InComplexXS(inVal);
	},
	createOutComplexFull : function(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outVal) {
		return new Mobilis.xslttest.ELEMENTS.OutComplexFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outVal);
	},
	createInComplexFull : function(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, inVal) {
		return new Mobilis.xslttest.ELEMENTS.InComplexFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, inVal);
	},
	createOutElementXSSequence : function(outSeq) {
		return new Mobilis.xslttest.ELEMENTS.OutElementXSSequence(outSeq);
	},
	createInElementXSSequence : function(inSeq) {
		return new Mobilis.xslttest.ELEMENTS.InElementXSSequence(inSeq);
	},
	createOutElementXS : function(outVal) {
		return new Mobilis.xslttest.ELEMENTS.OutElementXS(outVal);
	},
	createInElementXS : function(inVal) {
		return new Mobilis.xslttest.ELEMENTS.InElementXS(inVal);
	},
	createOutElement : function() {
		return new Mobilis.xslttest.ELEMENTS.OutElement();
	},
	createInElement : function() {
		return new Mobilis.xslttest.ELEMENTS.InElement();
	},
	createOutElementFull : function(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outComplexSeq) {
		return new Mobilis.xslttest.ELEMENTS.OutElementFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, outComplexSeq);
	},
	createInElementFull : function(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, inComplexSeq) {
		return new Mobilis.xslttest.ELEMENTS.InElementFull(intVal, longVal, booleanVal, doubleVal, floatVal, byteVal, shortVal, stringVal, inComplexSeq);
	},
	createOutElementSimpleFault : function() {
		return new Mobilis.xslttest.ELEMENTS.OutElementSimpleFault();
	},
	createInElementSimpleFault : function() {
		return new Mobilis.xslttest.ELEMENTS.InElementSimpleFault();
	},

	onOutOnlyOperation : function(OutElement) {
		console.log(OutElement);
		//TODO: Auto-generated OutOnlyOperationHandler
	},
	onOutOnlyOperationWithFault : function(OutElementFull) {
		console.log(OutElementFull);
		Mobilis.xslttest.InOnlyOperationWithFault(xslttest.createInElementFull(0, 1, false, 2.0, 1.23, 127, 32767, "stringValdsadsad", inComplexXSSequence));
		//TODO: Auto-generated OutOnlyOperationWithFaultHandler
	},
	onOutInOperationWithFault : function(OutElementXS) {
		//TODO: Auto-generated OutInOperationWithFaultHandler
		console.log(OutElementXS);
		OutElementXS.sendInFaultSimple();
	},
	onInOutOperationWithFault : function(OutElementXSSequence) {
		console.log(OutElementXSSequence);
		//TODO: Auto-generated InOutOperationWithFaultHandler
	},

	addHandlers : function() {
		Mobilis.xslttest.addOutOnlyOperationHandler(xslttest.onOutOnlyOperation);
		Mobilis.xslttest.addOutOnlyOperationWithFaultHandler(xslttest.onOutOnlyOperationWithFault);
		Mobilis.xslttest.addOutInOperationWithFaultHandler(xslttest.onOutInOperationWithFault);
	},

	connect : function(uFullJid, uPassword, mBareJid, onSuccess) {
		Mobilis.utils.trace("Trying to establish a connection to Mobilis");
		Mobilis.core.connect(uFullJid, uPassword, mBareJid, xslttest.HTTPBIND, function() {
			
			xslttest.addHandlers();
			onSuccess && onSuccess();
		});
	}
}

$(function() {
	$("#Connect").click(function(event) {
		xslttest.connect("seaclient@sea/XSLTTest", "sea", "mobilis@sea", function() {
			
			inASeqs = [];
			inASeqs.push(0);
			inASeqs.push(0);
			inASeqs.push(7);
			
			inBSeqs = [];
			inBSeqs.push(213213213);
			inBSeqs.push(0);
			inBSeqs.push(213213213);
			
			inAComplexXSSequence = xslttest.createInComplexXSSequence(inASeqs);
			inBComplexXSSequence = xslttest.createInComplexXSSequence(inBSeqs);
			
			inComplexXSSequence = [];
			inComplexXSSequence.push(inAComplexXSSequence);
			inComplexXSSequence.push(inBComplexXSSequence);

			Mobilis.xslttest.InOnlyOperationWithFault(xslttest.createInElementFull(0, 1, false, 2.0, 1.23, 127, 32767, "stringVal", inComplexXSSequence));
			
			inSeqs = [];
			inSeqs.push(2);
			inSeqs.push(3);
			Mobilis.xslttest.InOutOperationWithFault(xslttest.createInElementXSSequence(inSeqs), function(inElementXSSequence, fault) {
				// console.log(inElementXSSequence);
				// console.log(fault);
			});
			
			
			Mobilis.xslttest.InOnlyOperation(xslttest.createInElement());
		});
	});
}); 
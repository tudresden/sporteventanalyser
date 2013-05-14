<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:msdl="http://mobilis.inf.tu-dresden.de/msdl/"
	xmlns:xmpp="http://mobilis.inf.tu-dresden.de/xmpp/">

	<!-- TODO: Ein paar mehr Params -->
	<!-- the output folder where the scripts will be created -->
	<xsl:param name="outputFolder" />

	<!-- TODO: Das hier umsetzen! -->
	<!-- determines whether a minimized version of the core scripts should be 
		generated -->
	<xsl:param name="minimized" />

	<!-- prints indent like they are in code -->
	<xsl:output method="text" version="1.0" encoding="UTF-8"
		indent="yes" />

	<!-- http-bind -->
	<xsl:variable name="httpBind" select="'http://127.0.0.1:7070/http-bind/'" />

	<!-- path of the service name -->
	<xsl:variable name="serviceName" select="/msdl:description/msdl:service/@name" />
	<!-- path of the service namespace -->
	<xsl:variable name="serviceNS" select="/msdl:description/msdl:service/@ident" />

	<!-- message exchange pattern definitions like there are in msdl and wsdl 
		2.0 -->
	<xsl:variable name="mepInOut" select="'http://www.w3.org/ns/wsdl/in-out'" />
	<xsl:variable name="mepOutIn" select="'http://www.w3.org/ns/wsdl/out-in'" />
	<xsl:variable name="mepInOnly" select="'http://www.w3.org/ns/wsdl/in-only'" />
	<xsl:variable name="mepOutOnly" select="'http://www.w3.org/ns/wsdl/out-only'" />

	<!-- creates a new line -->
	<xsl:variable name="newline">
		<xsl:text>
</xsl:text>
	</xsl:variable>

	<!-- creates an indent -->
	<xsl:variable name="indent">
		<xsl:text>	</xsl:text>
	</xsl:variable>

	<!-- entry point of the script -->
	<xsl:template match="/">

		<xsl:text>Generated Codefiles of Service &lt; </xsl:text>
		<xsl:value-of select="$serviceName" />
		<xsl:text> &gt;:</xsl:text>
		<xsl:value-of select="$newline" />

		<!-- generate mobilis script -->
		<xsl:apply-templates select="/" mode="generateMobilis" />
		<xsl:value-of select="$newline" />

		<!-- generate mobilis core -->
		<xsl:apply-templates select="/" mode="generateMobilisCore" />
		<xsl:value-of select="$newline" />

		<!-- generate mobilis utils -->
		<xsl:apply-templates select="/" mode="generateMobilisUtils" />
		<xsl:value-of select="$newline" />

		<!-- generate mobilis service script -->
		<xsl:apply-templates select="/" mode="generateMobilisService" />
		<xsl:value-of select="$newline" />
		
		<!-- generate service script -->
		<xsl:apply-templates select="/" mode="generateService" />

	</xsl:template>

	<xsl:template match="/" mode="generateService">
		<xsl:variable name="fileName"
			select="concat($outputFolder, concat(lower-case($serviceName), '.js'))" />
		<xsl:value-of select="$fileName" />
	
		<!-- creates a file with the filename -->
		<xsl:result-document href="{$fileName}">
			<xsl:text>var </xsl:text>
			<xsl:value-of select="lower-case($serviceName)" />
			<xsl:text> = {</xsl:text>
			
			<!-- HTTPBIND -->
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>HTTPBIND : "</xsl:text>
			<xsl:value-of select="$httpBind" />
			<xsl:text>",</xsl:text>
			
			<xsl:value-of select="$newline" />
			
			<!-- generate constructor-wrappers(complexTypes) -->
			<xsl:apply-templates select="/msdl:description/msdl:types/xs:schema/xs:complexType" mode="generateComplexConstructorWrapper" />
			<!-- generate constructor-wrappers(elements) -->
			<xsl:apply-templates select="/msdl:description/msdl:types/xs:schema/xs:element" mode="generateElementConstructorWrapper" />
			
			<xsl:value-of select="$newline" />
			
			<!-- generate handlers -->
			<xsl:apply-templates select="/msdl:description/msdl:interface/msdl:operation/msdl:output" mode="generateHandlers" />
			
			<!-- create addHandlers method -->
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>addHandlers : function() {</xsl:text>
			
			<xsl:for-each select="/msdl:description/msdl:interface/msdl:operation/msdl:output">
				<xsl:variable name="interfaceOperationName" select="../@name" />
				
				<!-- TODO: Auch bei mepInOut? Hier sollte man theoretisch den callback nutzen! -->
				
				<xsl:value-of select="$newline" />
				<xsl:value-of select="$indent" />
				<xsl:value-of select="$indent" />
				<xsl:text>Mobilis.</xsl:text>
				<xsl:value-of select="lower-case($serviceName)" />
				<xsl:text>.add</xsl:text>
				<xsl:value-of select="$interfaceOperationName" />
				<xsl:text>Handler(</xsl:text>
				<xsl:value-of select="lower-case($serviceName)" />
				<xsl:text>.on</xsl:text>
				<xsl:value-of select="$interfaceOperationName" />
				<xsl:text>);</xsl:text>
			</xsl:for-each>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>},</xsl:text>
			
			<xsl:value-of select="$newline" />
			
			<!-- create connect method -->
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>connect : function(uFullJid, uPassword, mBareJid, onSuccess) {</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>Mobilis.utils.trace("Trying to establish a connection to Mobilis");</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>Mobilis.core.connect(uFullJid, uPassword, mBareJid, </xsl:text>
			<xsl:value-of select="lower-case($serviceName)" />
			<xsl:text>.HTTPBIND, function() {</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="lower-case($serviceName)" />
			<xsl:text>.addHandlers();</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>onSuccess &amp;&amp; onSuccess();</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>});</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>}</xsl:text>
			
			<xsl:value-of select="$newline" />
			<xsl:text>}</xsl:text>
			
		</xsl:result-document>
	</xsl:template>

	<!-- generate handlers -->
	<xsl:template match="/msdl:description/msdl:interface/msdl:operation/msdl:output" mode="generateHandlers">
		<xsl:variable name="interfaceOperationName" select="../@name" />
		<xsl:variable name="output" select="substring-after(./@element,':')" />
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:text>on</xsl:text>
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text> : function(</xsl:text>
		<xsl:value-of select="$output" />
		
		<!-- TODO: outfault kann hier natuerlich auch interessant sein! Zusaetzlich hier einbinden! -->
		
		<xsl:text>) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>//TODO: Auto-generated </xsl:text>
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text>Handler</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:text>},</xsl:text>
		
	</xsl:template>

	<!-- generate constructor-wrappers(complexTypes) -->
	<xsl:template match="/msdl:description/msdl:types/xs:schema/xs:complexType" mode="generateComplexConstructorWrapper">
		<xsl:variable name="complexTypeName" select="@name" />
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:text>create</xsl:text>
		<xsl:value-of select="$complexTypeName" />
		<xsl:text> : function(</xsl:text>
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:complexType[@name=$complexTypeName]/xs:sequence/xs:element">
			<xsl:value-of select="@name" />
			<xsl:if test="following-sibling::xs:element">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>return new Mobilis.</xsl:text>
		<xsl:value-of select="lower-case($serviceName)" />
		<xsl:text>.ELEMENTS.</xsl:text>
		<xsl:value-of select="$complexTypeName" />
		<xsl:text>(</xsl:text>
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:complexType[@name=$complexTypeName]/xs:sequence/xs:element">
			<xsl:value-of select="@name" />
			<xsl:if test="following-sibling::xs:element">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>);</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:text>},</xsl:text>
		
	</xsl:template>
	
	<!-- generate constructor-wrappers(elements) -->
	<xsl:template match="/msdl:description/msdl:types/xs:schema/xs:element" mode="generateElementConstructorWrapper">
		<xsl:variable name="elementName" select="@name" />
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:text>create</xsl:text>
		<xsl:value-of select="$elementName" />
		<xsl:text> : function(</xsl:text>
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:element[@name=$elementName]/xs:complexType/xs:sequence/xs:element">
			<xsl:value-of select="@name" />
			<xsl:if test="following-sibling::xs:element">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>return new Mobilis.</xsl:text>
		<xsl:value-of select="lower-case($serviceName)" />
		<xsl:text>.ELEMENTS.</xsl:text>
		<xsl:value-of select="$elementName" />
		<xsl:text>(</xsl:text>
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:element[@name=$elementName]/xs:complexType/xs:sequence/xs:element">
			<xsl:value-of select="@name" />
			<xsl:if test="following-sibling::xs:element">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>);</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:text>},</xsl:text>
		
	</xsl:template>

	<!-- generate mobilis service script -->
	<xsl:template match="/" mode="generateMobilisService">
		<xsl:variable name="fileName"
			select="concat($outputFolder, concat('mobilis/mobilis.', lower-case($serviceName), '.js'))" />
		<xsl:value-of select="$fileName" />

		<!-- creates a file with the filename -->
		<xsl:result-document href="{$fileName}">
			<xsl:text>(function() {</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>var </xsl:text>
			<xsl:value-of select="lower-case($serviceName)" />
			<xsl:text> = {</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			
			<!-- NS -->
			<xsl:text>NS : {</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>SERVICE : "</xsl:text>
			<xsl:value-of select="$serviceNS" />
			<xsl:apply-templates select="/msdl:description/msdl:binding/msdl:operation" mode="generateServiceNamespaces" />
			<xsl:text>"</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>},</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			
			<!-- ELEMENTS/ComplexTypes -->
			<xsl:text>ELEMENTS : {</xsl:text>
			<xsl:apply-templates select="/msdl:description/msdl:types/xs:schema/xs:complexType" mode="generateComplexConstructors" />
			<xsl:apply-templates select="/msdl:description/msdl:types/xs:schema/xs:element" mode="generateElementConstructors" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>},</xsl:text>
			
			<!-- DECORATORS -->
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>DECORATORS : {</xsl:text>
			<xsl:apply-templates select="/msdl:description/msdl:interface/msdl:operation/msdl:output" mode="generateDecorators" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>},</xsl:text>
			
			<!-- In-Operations (mepInOnly, mepInOut, mepOutIn) -->
			<xsl:apply-templates select="/msdl:description/msdl:binding/msdl:operation/msdl:input" mode="generateInOperations" />
			
			<!-- Out-Operations (mepOutOnly, mepOutIn, mepInOut) -->
			<xsl:apply-templates select="/msdl:description/msdl:binding/msdl:operation/msdl:output" mode="generateOutOperations" />
			
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>}</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:text>Mobilis.extend("</xsl:text>
			<xsl:value-of select="lower-case($serviceName)" />
			<xsl:text>", </xsl:text>
			<xsl:value-of select="lower-case($serviceName)" />
			<xsl:text>);</xsl:text>
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$newline" />
			<xsl:text>})();</xsl:text>
		</xsl:result-document>
	</xsl:template>

	<!-- generate out-operations -->
	<xsl:template match="/msdl:description/msdl:binding/msdl:operation/msdl:output" mode="generateOutOperations">
		<xsl:variable name="interfaceOperationName" select="substring-after(../@ref,':')" />

		<xsl:value-of select="$newline" />
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>add</xsl:text>
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text>Handler : function(handler) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>Mobilis.core.addHandler(Mobilis.</xsl:text>
		<xsl:value-of select="lower-case($serviceName)" />
		<xsl:text>.DECORATORS.</xsl:text>
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text>Handler(handler), Mobilis.</xsl:text>
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text>.NS.</xsl:text>
		<xsl:value-of select="upper-case($interfaceOperationName)" />
		<xsl:text>);</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>}</xsl:text>
		<xsl:if test="following-sibling::msdl:input">
			<xsl:text>,</xsl:text>
		</xsl:if>

	</xsl:template>

	<!-- generate in-operations -->
	<xsl:template match="/msdl:description/msdl:binding/msdl:operation/msdl:input" mode="generateInOperations">
		<xsl:variable name="interfaceOperationName" select="substring-after(../@ref,':')" />
		<xsl:variable name="messageElement" select="substring-after(/msdl:description/msdl:interface/msdl:operation[@name=$interfaceOperationName]/msdl:input/@element,':')" />
		<xsl:variable name="xmppType" select="./@xmpp:type" />
		<xsl:variable name="mepPattern" select="/msdl:description/msdl:interface/msdl:operation[@name=$interfaceOperationName]/@pattern" />
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text> : function(</xsl:text>
		<xsl:value-of select="$messageElement" />
		
		<xsl:choose>
			<xsl:when test="mepPattern = $mepInOut">
				<!-- add callback -->
				<xsl:text>, callback</xsl:text>
			</xsl:when>
			<xsl:when test="mepPattern = $mepOutIn">
				<!-- add packetID -->
				<xsl:text>, packetID</xsl:text>
			</xsl:when>
		</xsl:choose>
		
		<!-- TODO: errorcallback (dafuer muss vermutlich der Mobilis.core nochmal angepasst werden!) -->
		
		<xsl:text>) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>var _iq = Mobilis.utils.createMobilisServiceIq(Mobilis.</xsl:text>
		<xsl:value-of select="lower-case($serviceName)" />
		<xsl:text>.NS.SERVICE, {</xsl:text>
		
		<xsl:if test="mepPattern = $mepOutIn">
			<!-- Set ID -->
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>id : packetID,</xsl:text>
		</xsl:if>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>type : "</xsl:text>
		<xsl:value-of select="$xmppType" />
		<xsl:text>"</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>});</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>_iq.c("</xsl:text>
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text>", {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>xmlns : Mobilis.</xsl:text>
		<xsl:value-of select="lower-case($serviceName)" />
		<xsl:text>.NS.</xsl:text>
		<xsl:value-of select="upper-case($interfaceOperationName)" />
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>});</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>Mobilis.utils.appendElement(_iq, </xsl:text>
		<xsl:value-of select="$messageElement" />
		<xsl:text>);</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>Mobilis.core.sendIQ(_iq, </xsl:text>
		
		<xsl:choose>
			<xsl:when test="mepPattern = $mepInOut">
				<!-- add callback -->
				<xsl:text>callback</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>null</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		
		<!-- TODO: Error/Fault-Callback (Outfault) -->
		<xsl:text>, errorcallback ? errorcallback : null);</xsl:text>

		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>},</xsl:text>
		<!-- TODO: Komma nach Bedingung setzen: Existiert noch weitere Operation bzw. ein msdl:output/addHandler? -->
	</xsl:template>
	
	<!-- generate decorators -->
	<xsl:template match="/msdl:description/msdl:interface/msdl:operation/msdl:output" mode="generateDecorators">
		<xsl:variable name="interfaceOperationName" select="../@name" />
		<xsl:variable name="output" select="substring-after(./@element,':')" />
		<!-- TODO: Outfault! -->
		<xsl:variable name="outfault" select="substring-after(../msdl:outfault/@element,':')" />

		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$interfaceOperationName" />
		<xsl:text>Handler : function(_callback) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>return function(_iq) {</xsl:text>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>var $iq = $($.parseXML(_iq)).children();</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>_callback.apply(this, [new Mobilis.</xsl:text>
		<xsl:value-of select="lower-case($serviceName)" />
		<xsl:text>.ELEMENTS.</xsl:text>
		<xsl:value-of select="$output" />
		<xsl:text>($iq.children())</xsl:text>
		
		<!-- append packetID if mepOutIn -->
		<xsl:if test="../@pattern = $mepOutIn">
			<xsl:text>, $iq.attr("id")</xsl:text>
		</xsl:if>
		
		<xsl:text>]);</xsl:text>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>};</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>}</xsl:text>
		<xsl:if test="following-sibling::msdl:output">
			<xsl:text>,</xsl:text>
		</xsl:if>
		
	</xsl:template>

	<!-- generate constructors for elements -->
	<xsl:template match="/msdl:description/msdl:types/xs:schema/xs:element" mode="generateElementConstructors">
		<xsl:variable name="elementName" select="@name" />

		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$elementName" />
		<xsl:text> : function </xsl:text>
		<xsl:value-of select="$elementName" />
		<xsl:text>(</xsl:text>
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:element[@name=$elementName]/xs:complexType/xs:sequence/xs:element">
			<xsl:value-of select="@name" />
			<xsl:if test="following-sibling::xs:element">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>if (arguments[0] instanceof jQuery) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>var _</xsl:text>
		<xsl:value-of select="$elementName" />
		<xsl:text> = this;</xsl:text>
		
		<!-- create array for all elements with unbounded maxOccurs -->
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:element[@name=$elementName]/xs:complexType/xs:sequence/xs:element[@maxOccurs='unbounded']">
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>_</xsl:text>
			<xsl:value-of select="$elementName" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text> = [];</xsl:text>
		</xsl:for-each>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>arguments[0].children().each(function() {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>switch($(this).prop("tagName")) {</xsl:text>
		
		<!-- append elements -->
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:element[@name=$elementName]/xs:complexType/xs:sequence/xs:element">
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>case "</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>": _</xsl:text>
			<xsl:value-of select="$elementName" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="@name" />
			
			<xsl:choose>
				<xsl:when test="compare(./@maxOccurs, 'unbounded') = 0" >
					<xsl:text>.push(</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> = </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="starts-with(./@type,'tns:')" >
					<xsl:text>new Mobilis.</xsl:text>
					<xsl:value-of select="lower-case($serviceName)" />
					<xsl:text>.ELEMENTS.</xsl:text>
					<xsl:value-of select="@name" />
					<xsl:text>($(this))</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>$(this).text()</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="compare(./@maxOccurs, 'unbounded') = 0" >
				<xsl:text>)</xsl:text>
			</xsl:if>
			<xsl:text>; break;</xsl:text>
		</xsl:for-each>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>}</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>});</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>} else {</xsl:text>
		
		<!-- append elements -->
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:element[@name=$elementName]/xs:complexType/xs:sequence/xs:element">
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>this.</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text> = </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>;</xsl:text>
		</xsl:for-each>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>}</xsl:text>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>}</xsl:text>
		<xsl:if test="following-sibling::xs:element">
			<xsl:text>,</xsl:text>
		</xsl:if>
		
	</xsl:template>

	<!-- generate constructors for complex types -->
	<xsl:template match="/msdl:description/msdl:types/xs:schema/xs:complexType" mode="generateComplexConstructors">
		<xsl:variable name="complexTypeName" select="@name" />

		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$complexTypeName" />
		<xsl:text> : function </xsl:text>
		<xsl:value-of select="$complexTypeName" />
		<xsl:text>(</xsl:text>
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:complexType[@name=$complexTypeName]/xs:sequence/xs:element">
			<xsl:value-of select="@name" />
			<xsl:if test="following-sibling::xs:complexType">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>) {</xsl:text>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>if (arguments[0] instanceof jQuery) {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>var _</xsl:text>
		<xsl:value-of select="$complexTypeName" />
		<xsl:text> = this;</xsl:text>
		
		<!-- create array for all elements with unbounded maxOccurs -->
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:complexType[@name=$complexTypeName]/xs:sequence/xs:element[@maxOccurs='unbounded']">
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>_</xsl:text>
			<xsl:value-of select="$complexTypeName" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text> = [];</xsl:text>
		</xsl:for-each>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>arguments[0].children().each(function() {</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>switch($(this).prop("tagName")) {</xsl:text>
		
		<!-- append elements -->
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:complexType[@name=$complexTypeName]/xs:sequence/xs:element">
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>case "</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>": _</xsl:text>
			<xsl:value-of select="$complexTypeName" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="@name" />
			
			<xsl:choose>
				<xsl:when test="compare(./@maxOccurs, 'unbounded') = 0" >
					<xsl:text>.push(</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text> = </xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="starts-with(./@type,'tns:')" >
					<xsl:text>new Mobilis.</xsl:text>
					<xsl:value-of select="lower-case($serviceName)" />
					<xsl:text>.ELEMENTS.</xsl:text>
					<xsl:value-of select="@name" />
					<xsl:text>($(this))</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>$(this).text()</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="compare(./@maxOccurs, 'unbounded') = 0" >
				<xsl:text>)</xsl:text>
			</xsl:if>
			<xsl:text>; break;</xsl:text>
		</xsl:for-each>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>}</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>});</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>} else {</xsl:text>
		
		<!-- append elements -->
		<xsl:for-each select="/msdl:description/msdl:types/xs:schema/xs:complexType[@name=$complexTypeName]/xs:sequence/xs:element">
			<xsl:value-of select="$newline" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:value-of select="$indent" />
			<xsl:text>this.</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text> = </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>;</xsl:text>
		</xsl:for-each>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>}</xsl:text>
		
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:text>},</xsl:text>
	</xsl:template>

	<!-- generate namespaces of service -->
	<xsl:template match="/msdl:description/msdl:binding/msdl:operation" mode="generateServiceNamespaces">
		<xsl:variable name="namespace" select="./@xmpp:ident" />
		<xsl:variable name="interfaceOperationName" select="substring-after(./@ref,':')" />
		
		<xsl:text>",</xsl:text>
		<xsl:value-of select="$newline" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="$indent" />
		<xsl:value-of select="upper-case($interfaceOperationName)" />
		<xsl:text> : "</xsl:text>
		<xsl:value-of select="$namespace" />

	</xsl:template>
	
	<!-- generate mobilis script -->
	<xsl:template match="/" mode="generateMobilis">
		<xsl:variable name="fileName"
			select="concat($outputFolder, 'mobilis/mobilis.js')" />
		<xsl:value-of select="$fileName" />

		<!-- creates a file with the filename -->
		<xsl:result-document href="{$fileName}">
			<xsl:text>(function($) {</xsl:text>
	if ( typeof window.Mobilis === "undefined") {

		/** Class: Mobilis
		 *  Prototype for the Mobilis object that holds the entire framework.
		 *
		 */
		Mobilis = function() {

			if ( typeof $ === "undefined" || $ !== window.jQuery) {
				alert("Please load jQuery library first");
			}

			this.fn = {};

			/** Function: extend
			 *  Extends Mobilis with the provided functionality encapsulated in the obj that is passed.
			 *
			 *  The plug-in adapter of Mobilis. Methods are available under Mobilis.namespace
			 *
			 *  Parameters:
			 *    (String) namespace - Namespace for the plug-in.
			 *    (Object) ojc - Plug-in bbject that holds the functionality.
			 */
			this.extend = function(namespace, obj) {
				if ( typeof this[namespace] === "undefined") {
					if ( typeof this.fn[namespace] === "undefined" &amp;&amp; typeof this[namespace] === "undefined") {
						$.extend(obj, this.fn.extFN);
						this.fn[namespace] = obj;
						this[namespace] = this.fn[namespace];
						if ( typeof this[namespace].init === "function") {
							this[namespace].init();
						}
					} else {
						alert("The namespace '" + namespace + "' is already taken...");
					}
				}
			};

			this.addHandlers = function() {
				for (a in this.fn) {
					if ( typeof this.fn[a].addHandlers !== 'undefined') {
						this.fn[a].addHandlers();
					}
				}
			};

		};

		window.Mobilis = new Mobilis();

		window.Mobilis.extFN = window.Mobilis.fn.extFN = {

			_cache : {},

			setConfig : function(settings) {
				window.Mobilis.setObjectData(this.settings, settings);
				return this;
			},

			_toString : function() {
				if ( typeof this._cache['_toString'] === 'undefined') {
					this._cache['_toString'] = '';
					for (var k in window.Mobilis.fn) {
						if (window.Mobilis.fn[k] === this) {
							this._cache['_toString'] = 'Mobilis.' + k;
							break;
						}
					}
				}
				return this._cache['_toString'];
			}
		};
	}
})(jQuery);

$(window).unload(function() {
	if (Mobilis.connection) {
		if (Mobilis.connection.connected) {
			Mobilis.connection.send($pres({
				type : 'unavailable'
			}));
			Mobilis.core.disconnect('Application Closed');
		};
	}
}); 
		</xsl:result-document>
	</xsl:template>

	<!-- generate mobilis core -->
	<xsl:template match="/" mode="generateMobilisCore">
		<xsl:variable name="fileName"
			select="concat($outputFolder, 'mobilis/mobilis.core.js')" />
		<xsl:value-of select="$fileName" />

		<!-- creates a file with the filename -->
		<xsl:result-document href="{$fileName}">
			<xsl:text>(function() {</xsl:text>

	var core = {

		/** Constants: Mobilis Services Namespaces
		 *
		 *  NS.COORDINATOR - Coordinator Service.
		 *  NS.ADMIN - Admin Service.
		 *  NS.DEPLOYMENT - Deployment Service.
		 */
		NS : {
			COORDINATOR : "http://mobilis.inf.tu-dresden.de#services/CoordinatorService",
			ADMIN : "http://mobilis.inf.tu-dresden.de#services/AdminService",
			DEPLOYMENT : "http://mobilis.inf.tu-dresden.de#services/DeploymentService"
		},

		/** Object: Services
		 *  Object containing Services objects referenced by their namespace.
		 */
		SERVICES : {
			"http://mobilis.inf.tu-dresden.de#services/CoordinatorService" : {
				version : "1.0",
				mode : "single",
				instances : "1",
				agent : "Coordinator"
			}
		},

		/** Object: Server
		 *  Object containing the bare jid of the mobilis server
		 */
		SERVER : {
			mBareJid : null
		},

		/** Object: EXCEPTIONS
		 *  Object containing some Exceptions which may be thrown
		 */
		EXCEPTIONS : {
			NotConnectedException : function() {
				this.name = "NotConnectedException";
				this.message = "Mobilis is not connected to the XMPP-Server";
			},
			ServiceNotFoundException : function(service) {
				this.name = "NotFoundException";
				this.message = "Service could not been found: " + service;
			}
		},

		/** Function: connect
		 *  Establishes a connection to XMPP over BOSH and discovers services from the mobilis server
		 *
		 *  Parameters:
		 *    (String) uFullJid - Full JID of the user (e.g. user@mobilis/jsclient)
		 *    (String) uPassword - password for the jid
		 *    (String) mBareJid - Bare JID of the user (e.g. server@mobilis)
		 *    (Function) onSuccess - Callback function when successfully connected
		 *    (Function) onError - Callback function when an error occured
		 */
		connect : function(uFullJid, uPassword, mBareJid, bind, onSuccess, onError) {
			Mobilis.core.SERVER.mBareJid = mBareJid;
			onError = onError || this.trace;

			var conn = new Strophe.Connection(bind);
			conn.connect(uFullJid, uPassword, function(status) {
				if (status == Strophe.Status.ERROR) {
					onError("Connection error");
				} else if (status == Strophe.Status.CONNECTING) {
					Mobilis.utils.trace("Connecting");
				} else if (status == Strophe.Status.CONNFAIL) {
					Mobilis.utils.trace("Connection failed");
				} else if (status == Strophe.Status.AUTHENTICATING) {
					Mobilis.utils.trace("Authenticating");
				} else if (status == Strophe.Status.AUTHFAIL) {
					onError("Authentication failed");
				} else if (status == Strophe.Status.CONNECTED) {
					Mobilis.utils.trace("Connected");
					conn.send($pres({
					}));
					Mobilis.core.mobilisServiceDiscovery(null, function(iq) {
						$(iq).find("mobilisService").each(function() {
							Mobilis.core.SERVICES[$(this).attr("namespace")] = {
								version : $(this).attr("version"),
								mode : $(this).attr("mode"),
								instances : $(this).attr("instances"),
								agent : $(this).attr("jid").split(Mobilis.core.SERVER.mBareJid + "/")[1]
							};
						});
						Mobilis.utils.trace("Initial Service Discovery successful");
						onSuccess &amp;&amp; onSuccess();
					}, function(iq) {
						onError("Initial Service Discovery failed", iq);
					}, 30000);
				} else if (status == Strophe.Status.DISCONNECTED) {
					Mobilis.utils.trace("Disconnected");
				} else if (status == Strophe.Status.DISCONNECTING) {
					Mobilis.utils.trace("Disconnecting");
				} else if (status == Strophe.Status.ATTACHED) {
					Mobilis.utils.trace("Connection has been attached");
				}
			});

			Mobilis.connection = conn;
		},

		/** Function: disconnect
		 *  Initiates a graceful teardown of the connection
		 *
		 *  Parameters:
		 *    (String) reason - reason for disconnection
		 */
		disconnect : function(reason) {
			Mobilis.utils.trace("Disconnect");
			Mobilis.connection.disconnect(reason);
		},

		/** Function: send
		 *  Send stanza that does not require acknowledgement, such as &lt;msg&gt; or &lt;presence&gt;
		 *
		 *  Parameters:
		 *    (XMLElement) elem - Stanza to send
		 *
		 *  Throws:
		 *    Throws a NotConnectedException if no connection has been established yet
		 */
		send : function(elem) {
			if (!Mobilis.connection)
				throw new Mobilis.core.EXCEPTIONS.NotConnectedException();

			Mobilis.connection.send(elem);
		},

		/** Function: sendIQ
		 *  Sends stanza that requires acknowledgement, such as &lt;iq&gt;. Callback functions are specified for the response stanzas
		 *
		 *  Parameters:
		 *    (XMLElement) elem - Stanza to send
		 *    (Function) resultcallback - Callback for incoming response IQ stanzas of type RESULT
		 *    (Function) errorcallback - Callback for incoming response IQ stanzas of type ERROR, or timeout (Stanza will be null on timeout)
		 *
		 *  Throws:
		 *    Throws a NotConnectedException if no connection has been established yet
		 */
		sendIQ : function(elem, resultcallback, errorcallback) {
			if (!Mobilis.connection)
				throw new Mobilis.core.EXCEPTIONS.NotConnectedException();

			var callbackExpected = true;
			if (!resultcallback) {
				callbackExpected = false;
				resultcallback = function(iq) {
					Mobilis.utils.trace("Callback received!", iq);
				}
			}
			Mobilis.connection.sendIQ(elem, function(iq) {
				callbackExpected = false;
				resultcallback(iq);
			}, function(iq) {
				if (callbackExpected &amp;&amp; errorcallback) {
					// Timeout or ERROR
					errorcallback(iq);
				} else if (iq) {
					Mobilis.utils.trace("Error received", iq);
				} else if (callbackExpected) {
					Mobilis.utils.trace("Callback timed out!");
				}
			}, 3000);
		},

		/** Function: createServiceInstance
		 *  Create Mobilis Service Instance
		 *
		 *  Parameters:
		 *    (Object) constraints - { serviceNamespace, serviceName, servicePassword }
		 *    (Function) resultcallback - Callback for incoming response IQ stanzas of type RESULT
		 *    (Function) errorcallback - Callback for incoming response IQ stanzas of type ERROR, or timeout (Stanza will be null on timeout)
		 */
		createServiceInstance : function(constraints, resultcallback, errorcallback) {
			var customiq = Mobilis.utils.createMobilisServiceIq(Mobilis.core.NS.COORDINATOR, {
				type : "set"
			}).c("createNewServiceInstance", {
				xmlns : Mobilis.core.NS.COORDINATOR
			});
			if (constraints) {
				$.each(constraints, function(k, v) {
					customiq.c(k).t(v).up();
				});
			}

			Mobilis.core.sendIQ(customiq, resultcallback, errorcallback);
		},

		/** Function: mobilisServiceDiscovery
		 *  Performes a Mobilis Service discovery with the Mobilis Server
		 *
		 *  Parameters:
		 *    (Object) constraints - {serviceNamespace, serviceVersion}
		 *    (Function) resultcallback - Callback for incoming response IQ stanzas of type RESULT
		 *    (Function) errorcallback - Callback for incoming response IQ stanzas of type ERROR, or timeout (Stanza will be null on timeout)
		 */
		mobilisServiceDiscovery : function(constraints, resultcallback, errorcallback) {
			var discoiq = Mobilis.utils.createMobilisServiceIq(Mobilis.core.NS.COORDINATOR, {
				type : "get"
			}).c("serviceDiscovery", {
				xmlns : Mobilis.core.NS.COORDINATOR
			});
			if (constraints) {
				$.each(constraints, function(k, v) {
					discoiq.c(k).t(v).up();
				});
			}

			Mobilis.core.sendIQ(discoiq, resultcallback, errorcallback);
		},

		/** Function: addHandler
		 *  Add a stanza handler for the connection. The handler callback will be called for any stanza
		 *  that matches the parameters.
		 *
		 *
		 *  Parameters:
		 *    (Function) handler - Handler callback function
		 *    (String) namespace - Stanza's namespace to match
		 *
		 *  Throws:
		 *    Throws a NotConnectedException if no connection has been established yet
		 */
		addHandler : function(handler, namespace) {
			if (!Mobilis.connection)
				throw new Mobilis.core.EXCEPTIONS.NotConnectedException();

			Mobilis.connection.addHandler(handler, namespace);
		},

		/** Function: getAgentByNamespace
		 *  Get the name of an agent by its namespace
		 *
		 *  Parameters:
		 *    (String) namespace - Namespace of the service's agent
		 *
		 * 	Throws:
		 *    Throws a ServiceNotFoundException if the service is not found
		 */
		getAgentByNamespace : function(namespace) {
			if (!Mobilis.core.SERVICES[namespace])
				throw new Mobilis.core.EXCEPTIONS.ServiceNotFoundException(namespace);

			return Mobilis.core.SERVICES[namespace].agent;
		},

		/** Function: getFullJidFromNamespace
		 *  Get the full jid of a service
		 *
		 *  Parameters:
		 *    (String) namespace - Namespace of the service
		 */
		getFullJidFromNamespace : function(namespace) {
			return Mobilis.core.SERVER.mBareJid + "/" + Mobilis.core.getAgentByNamespace(namespace);
		}
	};

	Mobilis.extend("core", core);

})();
		</xsl:result-document>
	</xsl:template>
	<!-- generate mobilis utils -->
	<xsl:template match="/" mode="generateMobilisUtils">
		<xsl:variable name="fileName"
			select="concat($outputFolder, 'mobilis/mobilis.utils.js')" />
		<xsl:value-of select="$fileName" />

		<!-- creates a file with the filename -->
		<xsl:result-document href="{$fileName}">
			<xsl:text>(function($) {</xsl:text>

	var utils = {

		/** Function: trace
		 *  Prints a log message
		 *
		 *  Parameters:
		 *    (String) message - Message which should be traced
		 *    (Object) obj - Object which should be traced
		 */
		trace : function(message, obj) {
			message &amp;&amp; console.log("Mobilis: " + message);
			obj &amp;&amp; console.log(obj);
		},

		/** Function: createMobilisServiceIq
		 *  Create a new Strophe.Builder with an &lt;iq/&gt; element as root. The stanza will have the specified
		 *  attributes and additionally a "to" attribute filled with the FullJid to the service.
		 *
		 *
		 *  Parameters:
		 *    (String) namespace - Namespace of the service's agent
		 *    (String) attrs - Attributes of the &lt;iq/&gt; element (optional)
		 *
		 *  Returns:
		 *    A new Strophe.Builder object
		 */
		createMobilisServiceIq : function(namespace, attrs) {
			( attrs = attrs || {}).to = Mobilis.core.getFullJidFromNamespace(namespace);
			return $iq(attrs);
		},

		/** Function: appendElement
		 *  Appends an element to the Strophe.Builder.
		 *
		 *
		 *  Parameters:
		 *    (Strophe.Builder) builder - Strophe.Builder where to add element
		 *    (Object) element - Element which should be appended
		 */
		appendElement : function(builder, element) {
			if (element) {
				builder.c(element.constructor.name);
				$.each(element, function(k, v) {
					if ( typeof v === "object") {
						Mobilis.utils.appendElement(builder, v);
					} else {
						builder.c(k).t(String(v)).up();
					}
				});
				builder.up();
			}
		},

		/** Function: getUnixTime
		 *  Returns the current unix time (in ms)
		 *
		 *
		 *  Returns:
		 *    The current unix time (in ms)
		 */
		getUnixTime : function() {
			return parseInt(new Date.getTime() / 1000);
		}
	}

	Mobilis.extend("utils", utils);

})();
		</xsl:result-document>
	</xsl:template>

</xsl:stylesheet>

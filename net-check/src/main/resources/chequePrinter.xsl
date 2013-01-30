<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output 
  method="html"
  encoding="UTF-8"
  doctype-public="-//W3C//DTD HTML 4.01//EN"
  doctype-system="http://www.w3.org/TR/html4/strict.dtd"
  indent="yes" />	
	
	<xsl:template match="check">
		<html>
			<body>
				<h2>Cheque</h2>
				<p>Valeur:</p>
				<xsl:value-of select="value"/>
				<xsl:value-of select="currency"/>
				<p>Emetteur:</p>
				<xsl:value-of select="id" />
				<br/>
				<p>Date:</p>
				<xsl:value-of select="date" />
				<div style="border: 1px solid grey">
					<xsl:value-of select="customer/name"/>
					<xsl:value-of select="customer/lastName"/>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
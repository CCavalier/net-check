<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ns="http://www.ccavalier.fr">
	<xsl:output method="html" encoding="UTF-8"
		doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd"
		indent="yes" />
	<xsl:template match="/*">
		<html>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="child::ns:manager/ns:customers">
		
				<h2>Liste de clients</h2>
				<xsl:for-each select="ns:customer">
					<xsl:sort select="ns:name" />
					<ul>
						<li>
							<b>Nom : </b>
							<xsl:value-of select="ns:name" />
						</li>
						<li>
							<b>Prenom: </b>
							<xsl:value-of select="ns:lastName" />
						</li>
						<li>
							<b>Adresse : </b>
							<xsl:value-of select="ns:location" />
						</li>
					</ul>

					<table border="1">
						<tr bgcolor="#9acd32">
							<th COLSPAN="2">Chèques disponibles</th>
						</tr>
						<tr bgcolor="#9acd32">
							<th>numéro</th>
							<th>devise</th>
						</tr>
						<xsl:for-each select="ns:account/ns:check">
							<tr>
								<td>
									<xsl:value-of select="ns:id" />
								</td>
								<td>
									<xsl:value-of select="ns:currency" />
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</xsl:for-each>
	</xsl:template>
	<xsl:template match="*"></xsl:template>
</xsl:stylesheet>




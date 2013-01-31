<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8"
		doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd"
		indent="yes" />
	<xsl:template match="/*">
		<xsl:apply-templates />
	
	</xsl:template>
	<xsl:template match="child::manager/customers">
		<html>
			<body>
				<h2>Liste de clients</h2>
				<xsl:for-each select="customer">
					<xsl:sort select="name" />
					<ul>
						<li>
							<b>Nom : </b>
							<xsl:value-of select="name" />
						</li>
						<li>
							<b>Prenom: </b>
							<xsl:value-of select="lastName" />
						</li>
						<li>
							<b>Adresse : </b>
							<xsl:value-of select="location" />
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
						<xsl:for-each select="account/check">
							<tr>
								<td>
									<xsl:value-of select="id" />
								</td>
								<td>
									<xsl:value-of select="currency" />
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</xsl:for-each>
				
			</body>
		</html>
	</xsl:template>
	 <xsl:template match="*"></xsl:template>
</xsl:stylesheet>




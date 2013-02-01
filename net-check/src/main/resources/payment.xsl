<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ns="http://www.ccavalier.fr">
	<xsl:output method="html" encoding="UTF-8"
		doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd"
		indent="yes" />
	
	<xsl:template match="/">
		<html>
			<body>
				<h1>Demande de Paiements en Attente</h1>
				<table border="1">
					<tr bgcolor="#9acd32">
						<th>Client</th>
						<th>Devise</th>
						<th>Valeur</th>
						<th>Date</th>
						<th>Gestionnaire</th>
					</tr>
					<xsl:for-each select="ns:paymentChecks/ns:check">
						<tr>
							<td>
								<xsl:value-of select="ns:id" />
							</td>
							<td>
								<xsl:value-of select="ns:currency" />
							</td>
							<td>
								<xsl:value-of select="ns:value" />
							</td>
							<td>
								<xsl:value-of select="ns:date" />
							</td>
							<td>
								<xsl:value-of select="ns:accountOwner" />
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
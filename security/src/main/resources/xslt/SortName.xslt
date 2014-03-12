<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output omit-xml-declaration="yes" indent="yes" method="xml" encoding="UTF-8"/>
  <xsl:strip-space  elements="*"/>
  <xsl:template match="node()|@*" name="identity">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*">
        <xsl:sort select="@name" order="ascending" />
        <xsl:sort select="@value" order="ascending" />
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>  
</xsl:stylesheet>
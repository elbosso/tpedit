<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:db="http://docbook.org/ns/docbook" xmlns:xml="http://www.w3.org/XML/1998/namespace" >
	<xsl:param name="tag">!!</xsl:param>
	<xsl:template match="FunctionalTest">
		<db:article>
			<xsl:attribute name="version">4.0</xsl:attribute>
			<xsl:variable name="nHits">
				<xsl:for-each select="./Category">
					<xsl:for-each select="./Test/Tags/Tag[text()=$tag]/../..">
						<xsl:if test="@new='true'">1</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:variable>
			<xsl:variable name="nhitCount" select="string-length($nHits)"/>
			<xsl:variable name="uHits">
				<xsl:for-each select="./Category">
					<xsl:for-each select="./Test/Tags/Tag[text()=$tag]/../..">
						<xsl:if test="@edited='true'">1</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:variable>
			<xsl:variable name="uhitCount" select="string-length($uHits)"/>
			<xsl:variable name="extraCats">
				<xsl:if test="$nhitCount &gt; 0">1</xsl:if>
				<xsl:if test="$uhitCount &gt; 0">1</xsl:if>
			</xsl:variable>
			<xsl:variable name="extraCatCount" select="string-length($extraCats)"/>
  <db:info>
    <db:title><xsl:value-of select="@application"/></db:title>
    <db:pubdate><xsl:value-of select="@date"/></db:pubdate>
    <db:edition>Revision:</db:edition>
  </db:info>
					<!--Table of contents for categories-->
						<!--fo:table>
						  <fo:table-column column-width="7mm" />
						  <fo:table-column column-width="200mm - 2 * 25mm - 7mm - 3mm" />
						  <fo:table-column column-width="3mm" />
						  <fo:table-body>
							<xsl:if test="$nhitCount &gt; 0">
						      <fo:table-row font-style="italic">
							  	<fo:table-cell>
									<fo:block>0</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-variant="small-caps">
										<fo:basic-link>
											<xsl:attribute name="internal-destination">newtests</xsl:attribute>
											Neue Tests
										</fo:basic-link>
										<fo:leader leader-pattern-width="6pt" leader-alignment="reference-area" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="end">
										<fo:page-number-citation><xsl:attribute name="ref-id">newtests</xsl:attribute></fo:page-number-citation>
									</fo:block>
								</fo:table-cell>
						      </fo:table-row>
							</xsl:if>
							<xsl:if test="$uhitCount &gt; 0">
						      <fo:table-row font-style="italic">
							  	<fo:table-cell>
									<fo:block>
									<xsl:choose>
										<xsl:when test="$nhitCount &gt; 0">
											1
										</xsl:when>
										<xsl:otherwise>
											0
										</xsl:otherwise>
									</xsl:choose>
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block font-variant="small-caps">
										<fo:basic-link>
											<xsl:attribute name="internal-destination">editedtests</xsl:attribute>
											Aktualisierte Tests
										</fo:basic-link>
										<fo:leader leader-pattern-width="6pt" leader-alignment="reference-area" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="end">
										<fo:page-number-citation><xsl:attribute name="ref-id">editedtests</xsl:attribute></fo:page-number-citation>
									</fo:block>
								</fo:table-cell>
						      </fo:table-row>
							</xsl:if>
							
							<xsl:for-each select="./Category">
						      <fo:table-row font-style="italic">
							  	<fo:table-cell>
									<fo:block><xsl:value-of select="./@ID +  $extraCatCount"/></fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<xsl:choose>
										<xsl:when test="@new='true'">
											<fo:block font-weight="bold">(neu)</fo:block>
										</xsl:when>
										<xsl:when test="@edited='true'">
											<fo:block font-weight="bold">(aktualisiert)</fo:block>
										</xsl:when>
										<xsl:otherwise>
											<xsl:variable name="vHits">
												<xsl:for-each select="./Test">
													<xsl:if test="@new='true'">1</xsl:if>
													<xsl:if test="@edited='true'">1</xsl:if>
												</xsl:for-each>
											</xsl:variable>
											<xsl:variable name="vhitCount" select="string-length($vHits)"/>
											<xsl:if test="$vhitCount &gt; 0"><fo:block font-weight="bold">(aktualisiert)</fo:block></xsl:if>
										</xsl:otherwise>
									</xsl:choose>
									<fo:block font-variant="small-caps">
										<fo:basic-link>
											<xsl:attribute name="internal-destination">
												<xsl:value-of select="./@ID"/>
											</xsl:attribute>
											<xsl:value-of select="@name" />
										</fo:basic-link>
										<fo:leader leader-pattern-width="6pt" leader-alignment="reference-area" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block text-align="end">
										<fo:page-number-citation><xsl:attribute name="ref-id"><xsl:value-of select="./@ID"/></xsl:attribute></fo:page-number-citation>
									</fo:block>
								</fo:table-cell>
						      </fo:table-row>
						    </xsl:for-each>
						  </fo:table-body>
						</fo:table-->
					<!--END Table of contents for categories-->
					<!--xsl:if test="$nhitCount &gt; 0">
					<fo:block font-size="medium" padding-bottom="2mm" page-break-after="right">
					<xsl:attribute name="id">newtests</xsl:attribute>
					<fo:block padding-bottom="2mm" padding-top="2mm" background-color="lightgray" text-align="center" font-variant="small-caps" font-weight="bold">Neue Tests</fo:block>
					<fo:block padding-bottom="3mm" padding-top="3mm">
						<fo:table>
						  <fo:table-column column-width="15mm" />
						  <fo:table-column column-width="200mm - 2 * 25mm - 15mm - 3mm" />
						  <fo:table-column column-width="3mm" />
						  <fo:table-body>
						<xsl:for-each select="./Category">
							<xsl:for-each select="./Test">
						<xsl:if test="@new='true'">
						      <fo:table-row font-style="italic">
							<fo:table-cell>
							  <fo:block><xsl:value-of select="../@ID+  $extraCatCount"/>.<xsl:value-of select="./@ID"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
							  <fo:block font-variant="small-caps">
							  	<fo:basic-link>
									<xsl:attribute name="internal-destination">
										<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
									</xsl:attribute>
									<xsl:value-of select="Description" />
								</fo:basic-link>
							    <fo:leader leader-pattern-width="6pt" leader-alignment="reference-area" />
							  </fo:block>
							</fo:table-cell>
							<fo:table-cell>
							  <fo:block text-align="end">
							    <fo:page-number-citation><xsl:attribute name="ref-id"><xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/></xsl:attribute></fo:page-number-citation>
							  </fo:block>
							</fo:table-cell>
						      </fo:table-row>
						</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
						  </fo:table-body>
						</fo:table>
					</fo:block>
					</fo:block>
					</xsl:if-->
					<!--xsl:if test="$uhitCount &gt; 0">
					<fo:block font-size="medium" padding-bottom="2mm" page-break-after="right">
					<xsl:attribute name="id">editedtests</xsl:attribute>
					<fo:block padding-bottom="2mm" padding-top="2mm" background-color="lightgray" text-align="center" font-variant="small-caps" font-weight="bold">Aktualisierte Tests</fo:block>
					<fo:block padding-bottom="3mm" padding-top="3mm">
						<fo:table>
						  <fo:table-column column-width="15mm" />
						  <fo:table-column column-width="200mm - 2 * 25mm - 15mm - 3mm" />
						  <fo:table-column column-width="3mm" />
						  <fo:table-body>
						<xsl:for-each select="./Category">
							<xsl:for-each select="./Test">
						<xsl:if test="@edited='true'">
						      <fo:table-row font-style="italic">
							<fo:table-cell>
							  <fo:block><xsl:value-of select="../@ID+  $extraCatCount"/>.<xsl:value-of select="./@ID"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
							  <fo:block font-variant="small-caps">
							  	<fo:basic-link>
									<xsl:attribute name="internal-destination">
										<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
									</xsl:attribute>
									<xsl:value-of select="Description" />
								</fo:basic-link>
							    <fo:leader leader-pattern-width="6pt" leader-alignment="reference-area" />
							  </fo:block>
							</fo:table-cell>
							<fo:table-cell>
							  <fo:block text-align="end">
							    <fo:page-number-citation><xsl:attribute name="ref-id"><xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/></xsl:attribute></fo:page-number-citation>
							  </fo:block>
							</fo:table-cell>
						      </fo:table-row>
						</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
						  </fo:table-body>
						</fo:table>
					</fo:block>
					</fo:block>
					</xsl:if-->
					<!--db:toc/-->
					<xsl:apply-templates select="./Category"/>
		</db:article>
	</xsl:template>
	<xsl:template match="Category">
	<db:section>
		<xsl:attribute name="xml:id"><xsl:value-of select="concat(parent::FunctionalTest/@application, '.',@name)"/></xsl:attribute>
			<xsl:attribute name="version">4.0</xsl:attribute>
		<!--xsl:attribute name="xml:id">
			<xsl:value-of select="./@ID"/>
		</xsl:attribute-->
		<db:title><xsl:value-of select="@name"/></db:title>
		<xsl:choose>
			<xsl:when test="@new='true'">
				<db:important><db:simpara>neu</db:simpara></db:important>
			</xsl:when>
			<xsl:when test="@edited='true'">
				<db:important><db:simpara>aktualisiert</db:simpara></db:important>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="vHits">
					<xsl:for-each select="./Test/Tags/Tag[text()=$tag]/../..">
						<xsl:if test="@new='true'">1</xsl:if>
						<xsl:if test="@edited='true'">1</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<xsl:variable name="vhitCount" select="string-length($vHits)"/>
				<xsl:if test="$vhitCount &gt; 0"><db:important><db:simpara>aktualisiert</db:simpara></db:important></xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		<db:para><xsl:value-of select="./Description"/></db:para>
		<!--fo:block padding-bottom="3mm" padding-top="3mm">
						<fo:table>
						  <fo:table-column column-width="7mm" />
						  <fo:table-column column-width="200mm - 2 * 25mm - 7mm - 3mm" />
						  <fo:table-column column-width="3mm" />
						  <fo:table-body>
						<xsl:for-each select="./Test">
						      <fo:table-row font-style="italic">
							<fo:table-cell>
							  <fo:block><xsl:value-of select="./@ID"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
							<xsl:if test="@new='true'">
							  <fo:block font-weight="bold">(neu)</fo:block>
							</xsl:if>
							<xsl:if test="@edited='true'">
							  <fo:block font-weight="bold">(aktualisiert)</fo:block>
							</xsl:if>
							  <fo:block font-variant="small-caps">
							  	<fo:basic-link>
									<xsl:attribute name="internal-destination">
										<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
									</xsl:attribute>
									<xsl:value-of select="Description" />
								</fo:basic-link>
							    <fo:leader leader-pattern-width="6pt" leader-alignment="reference-area" />
							  </fo:block>
							</fo:table-cell>
							<fo:table-cell>
							  <fo:block text-align="end">
							    <fo:page-number-citation><xsl:attribute name="ref-id"><xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/></xsl:attribute></fo:page-number-citation>
							  </fo:block>
							</fo:table-cell>
						      </fo:table-row>
						    </xsl:for-each>
						<xsl:for-each select="./TestCombination">
						      <fo:table-row font-style="italic">
							<fo:table-cell>
							  <fo:block><xsl:value-of select="./@ID"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
							  <fo:block font-variant="small-caps">
							    <xsl:value-of select="Description" />
							    <fo:leader 
								       leader-pattern-width="6pt"
								       leader-alignment="reference-area" />
							  </fo:block>
							</fo:table-cell>
							<fo:table-cell>
							  <fo:block text-align="end">
							    <fo:page-number-citation><xsl:attribute name="ref-id"><xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/></xsl:attribute></fo:page-number-citation>
							  </fo:block>
							</fo:table-cell>
						      </fo:table-row>
						    </xsl:for-each>
						  </fo:table-body>
						</fo:table>
		</fo:block-->
		<xsl:apply-templates select="./Test/Tags/Tag[text()=$tag]/../.."/>
		<!--fo:block font-size="medium" padding-bottom="2mm"><xsl:apply-templates select="./TestCombination"/></fo:block-->
		</db:section>
	</xsl:template>
	<xsl:template match="Test">
	<db:section>
		<xsl:attribute name="xml:id"><xsl:value-of select="translate(concat(ancestor::FunctionalTest/@application, '.',parent::Category/@name, '.', ./Description),', /', '---')"/></xsl:attribute>.
			<xsl:attribute name="version">4.0</xsl:attribute>
		<!--xsl:attribute name="xml:id">
			<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
		</xsl:attribute-->
		<db:title><xsl:value-of select="./Description"/></db:title>
		<!--fo:block padding-bottom="1mm" font-weight="bold"><fo:inline padding-right="3mm"><xsl:value-of select="./@ID"/></fo:inline><fo:inline><xsl:value-of select="./Description"/></fo:inline></fo:block-->
		<xsl:if test="@new='true'">
		  <db:important><db:simpara>neu</db:simpara></db:important>
		</xsl:if>
		<xsl:if test="@edited='true'">
		  <db:important><db:simpara>aktualisiert</db:simpara></db:important>
		</xsl:if>
		<xsl:if test="./@RequirementID">
		<db:sidebar>
		<db:title>Anforderungs-Schlüssel</db:title>
		<db:remark>
		<xsl:value-of select="@RequirementID"/>
		</db:remark>
		</db:sidebar>
		</xsl:if>
				<xsl:variable name="nTags">
					<xsl:value-of select="count(./Tags/Tag)"/>
				</xsl:variable>
				<xsl:variable name="nTagsh">
					<xsl:value-of select="count(./Tags/Tag[@hidden='true'])"/>
				</xsl:variable>
				<xsl:if test="$nTags &gt; $nTagsh">
				<db:sidebar>
		<db:title>Schlüsselwörter</db:title>
			<xsl:apply-templates select="./Tags"/>
		</db:sidebar>
				</xsl:if>		
		<xsl:choose>
			<xsl:when test="./@Variants">
				<xsl:if test="not(./@Variants = '')">
				<db:note>
				<db:remark>
				<xsl:value-of select="./@Variants"/>
				</db:remark>
				</db:note>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
		<xsl:variable name="nChildsa">
			<xsl:value-of select="count(./Actions/Action)"/>
		</xsl:variable>
		<xsl:if test="$nChildsa &gt; 0">
			<db:simplesect>
			<db:title>Vorgehen</db:title>
			<db:procedure>
				<xsl:apply-templates select="./Actions"/>
			</db:procedure>
			</db:simplesect>
		</xsl:if>
		<xsl:variable name="nChildsr">
			<xsl:value-of select="count(./ExpectedResults/ExpectedResult)"/>
		</xsl:variable>
		<xsl:if test="$nChildsr &gt; 0">
			<db:simplesect>
			<db:title>Erwartete Resultate</db:title>
			<db:procedure>
				<xsl:apply-templates select="./ExpectedResults"/>
			</db:procedure>
			</db:simplesect>
		</xsl:if>
		<xsl:variable name="nChildsa">
			<xsl:value-of select="count(./Variants/Variant)"/>
		</xsl:variable>
		<xsl:if test="$nChildsa &gt; 0">
			<db:simplesect>
				<db:title>Varianten</db:title>
				<db:para>
					<db:informaltable frame="none" rules="none">
						<!--xsl:processing-instruction name="dbfo">
							keep-together.within-page="always"
						</xsl:processing-instruction-->
				<!--title>title</title-->

					<db:tgroup cols="2">
					  <db:colspec colnum="1" colwidth="8*" />

					  <db:colspec colnum="2" colwidth="2*" />

					  <db:tbody>
						<db:row>
						  <db:entry align="right"><db:para>Variante</db:para></db:entry>
						  <db:entry valign="middle">OK/Fail</db:entry>
						</db:row>
					<xsl:apply-templates select="./Variants"/>
					 </db:tbody>
					</db:tgroup>
				  </db:informaltable>
				</db:para>
			</db:simplesect>
		</xsl:if>
		<db:para>
		<db:variablelist>
		<db:varlistentry>
		<db:term>Tester</db:term><db:listitem><db:para/></db:listitem>
		</db:varlistentry>
		<db:varlistentry>
		<db:term>Datum</db:term><db:listitem><db:para/></db:listitem>
		</db:varlistentry>
		<db:varlistentry>
		<db:term>Bemerkungen</db:term><db:listitem><db:para/></db:listitem>
		</db:varlistentry>
		</db:variablelist>
		</db:para>
		<!--fo:table table-layout="fixed" height="2cm">
			<fo:table-column column-width="3cm"/>
			<fo:table-column column-width="3cm"/>
			<fo:table-column column-width="4cm"/>
				<fo:table-header background-color="lightgray">
					<fo:table-row>
						<fo:table-cell>	<fo:block font-weight="bold">Tester</fo:block></fo:table-cell>
						<fo:table-cell>	<fo:block font-weight="bold">Datum</fo:block></fo:table-cell>
						<fo:table-cell>	<fo:block font-weight="bold">Bemerkungen</fo:block></fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
						<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
						<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
					</fo:table-row>
				</fo:table-body>
		</fo:table-->
		</db:section>
	</xsl:template>
	<!--xsl:template match="TestCombination">
		<fo:block font-size="medium" padding-bottom="2mm" space-after="10mm">
		<xsl:attribute name="id">
			<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
		</xsl:attribute>
		<fo:block padding-bottom="1mm" font-weight="bold"><xsl:value-of select="./@ID"/> <xsl:value-of select="./Description"/></fo:block>
		<fo:block font-style="italic">Kombination aus:</fo:block>
		<fo:block padding-bottom="1mm"><fo:list-block provisional-distance-between-starts="15mm" provisional-label-separation="5mm">
			<xsl:apply-templates select="./ReferencedTests"/>
		</fo:list-block></fo:block>
		<fo:table table-layout="fixed" height="2cm">
			<fo:table-column column-width="3cm"/>
			<fo:table-column column-width="3cm"/>
			<fo:table-column column-width="4cm"/>
				<fo:table-header background-color="lightgray">
					<fo:table-row>
						<fo:table-cell>	<fo:block font-weight="bold">Tester</fo:block></fo:table-cell>
						<fo:table-cell>	<fo:block font-weight="bold">Datum</fo:block></fo:table-cell>
						<fo:table-cell>	<fo:block font-weight="bold">Bemerkungen</fo:block></fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
						<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
						<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
					</fo:table-row>
				</fo:table-body>
		</fo:table>
		</fo:block>
	</xsl:template-->
	<xsl:template match="ExpectedResults">
		<xsl:apply-templates select="./ExpectedResult"/>
	</xsl:template>
	<xsl:template match="Actions">
		<xsl:apply-templates select="./Action"/>
	</xsl:template>
	<xsl:template match="Variants">
		<xsl:apply-templates select="./Variant"/>
	</xsl:template>
	<xsl:template match="ReferencedTests">
		<xsl:apply-templates select="./ReferencedTest"/>
	</xsl:template>
	<xsl:template match="ExpectedResult">
		<db:step><db:para>
			<xsl:if test="(./@fromMacro='true')">
				<xsl:choose>
					<xsl:when test="(contains(., '§'))">
						<db:emphasis role="error_macro_not_found"><xsl:value-of select="."/></db:emphasis>
					</xsl:when>
					<xsl:otherwise>
						<db:emphasis><xsl:value-of select="."/></db:emphasis>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<xsl:if test="not(./@fromMacro='true')">
				<xsl:value-of select="."/>
			</xsl:if>
		</db:para></db:step>
	</xsl:template>
	<xsl:template match="Action">
		<db:step><db:para>
			<xsl:if test="(./@fromMacro='true')">
				<xsl:choose>
					<xsl:when test="(contains(., '§'))">
						<db:emphasis role="error_macro_not_found"><xsl:value-of select="."/></db:emphasis>
					</xsl:when>
					<xsl:otherwise>
						<db:emphasis><xsl:value-of select="."/></db:emphasis>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<xsl:if test="not(./@fromMacro='true')">
				<xsl:value-of select="."/>
			</xsl:if>
		</db:para></db:step>
	</xsl:template>
	<xsl:template match="Variant">
            <db:row>
              <db:entry align="right"><db:para><xsl:value-of select="."/></db:para></db:entry>
              <db:entry valign="middle"><db:para/></db:entry>
            </db:row>
	</xsl:template>
	<xsl:template match="ReferencedTest">
		<!--fo:list-item>
			<fo:list-item-label start-indent="0mm" end-indent="label-end()"><fo:block><xsl:number format="1."/></fo:block></fo:list-item-label>
			<fo:list-item-body start-indent="body-start()"><fo:block>Test <xsl:value-of select="./@TestID"/> aus Kategorie <xsl:value-of select="./@CategoryName"/></fo:block></fo:list-item-body>
		</fo:list-item-->
	</xsl:template>
	<xsl:template match="Tags">
		<db:simplelist columns="1">
			<xsl:apply-templates select="./Tag"/>
		</db:simplelist>
	</xsl:template>
	<xsl:template match="Tag">
				<xsl:if test="not(@hidden='true')">
				<db:member>
					<xsl:value-of select="."/>
				</db:member>
				</xsl:if>		
				<!--xsl:choose>
  <xsl:when test="@hidden='true'">
  </xsl:when>
  <xsl:otherwise>
				<db:keyword>
					<xsl:value-of select="."/>
				</db:keyword>
  </xsl:otherwise>
</xsl:choose -->
	</xsl:template>
</xsl:stylesheet>

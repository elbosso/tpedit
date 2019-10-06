<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:param name="tag">!!</xsl:param>
		 <xsl:attribute-set name="sidebar.properties">
    <xsl:attribute name="width">2.5in</xsl:attribute>
    <xsl:attribute name="padding-left">1em</xsl:attribute>
    <xsl:attribute name="padding-right">1em</xsl:attribute>
    <xsl:attribute name="start-indent">2em</xsl:attribute>
  </xsl:attribute-set>
	<xsl:template match="FunctionalTest">
		<fo:root>
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
			<fo:layout-master-set>
				<fo:simple-page-master master-name="title">
					<fo:region-body margin="1in"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="my-page">
					<fo:region-body margin="1in"/>
					<fo:region-before extent="25mm"/>
					<fo:region-after extent="1.0in"/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence font-size="medium" master-reference="my-page">
				<fo:flow font-size="medium" flow-name="xsl-region-body">
					<fo:block text-align="center" font-size="x-large" font-stretch="condensed">
						<xsl:value-of select="@application"/>
					</fo:block>
					<fo:block>
						<xsl:value-of select="@date"/>
					</fo:block>
					<fo:block>Revision:</fo:block>
					<fo:block padding-bottom="3mm" padding-top="3mm">
						<!--Table of contents for categories-->
						<fo:table>
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
												<fo:page-number-citation>
													<xsl:attribute name="ref-id">newtests</xsl:attribute>
												</fo:page-number-citation>
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
												<fo:page-number-citation>
													<xsl:attribute name="ref-id">editedtests</xsl:attribute>
												</fo:page-number-citation>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:if>
							
								<xsl:for-each select="./Category">
									<fo:table-row font-style="italic">
										<fo:table-cell>
											<fo:block>
												<xsl:value-of select="./@ID +  $extraCatCount"/>
											</fo:block>
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
														<xsl:for-each select="./Test/Tags/Tag[text()=$tag]/../..">
															<xsl:if test="@new='true'">1</xsl:if>
															<xsl:if test="@edited='true'">1</xsl:if>
														</xsl:for-each>
													</xsl:variable>
													<xsl:variable name="vhitCount" select="string-length($vHits)"/>
													<xsl:if test="$vhitCount &gt; 0">
														<fo:block font-weight="bold">(aktualisiert)</fo:block>
													</xsl:if>
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
												<fo:page-number-citation>
													<xsl:attribute name="ref-id">
														<xsl:value-of select="./@ID"/>
													</xsl:attribute>
												</fo:page-number-citation>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
								<xsl:if test="$tag='!!'">
								<xsl:for-each select="./TagsDescription/TagDescription">
									<fo:table-row font-style="italic">
										<fo:table-cell>
											<fo:block>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<fo:basic-link>
													<xsl:attribute name="internal-destination">
														<xsl:value-of select="./TagName"/>
													</xsl:attribute>
													<xsl:value-of select="./TagName" />
												</fo:basic-link>
												<fo:leader leader-pattern-width="6pt" leader-alignment="reference-area" />
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block text-align="end">
												<fo:page-number-citation>
													<xsl:attribute name="ref-id">
														<xsl:value-of select="./TagName"/>
													</xsl:attribute>
												</fo:page-number-citation>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</xsl:for-each>
								</xsl:if>
							</fo:table-body>
						</fo:table>
						<!--END Table of contents for categories-->
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
			<fo:page-sequence font-size="medium" master-reference="my-page">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block padding-top="10mm" text-align="center" font-size="small">
						<xsl:value-of select="@application"/>
					</fo:block>
				</fo:static-content>
				<fo:static-content flow-name="xsl-region-after">
					<fo:block text-align="center" font-size="small" font-stretch="condensed">Seite <fo:page-number/> von
						<fo:page-number-citation ref-id="endOfDocument"/>
					</fo:block>
				</fo:static-content>
				<fo:flow font-size="medium" flow-name="xsl-region-body">
					<xsl:if test="$nhitCount &gt; 0">
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
											<xsl:for-each select="./Test/Tags/Tag[text()=$tag]/../..">
												<xsl:if test="@new='true'">
													<fo:table-row font-style="italic">
														<fo:table-cell>
															<fo:block>
																<xsl:value-of select="../@ID+  $extraCatCount"/>.<xsl:value-of select="./@ID"/>
															</fo:block>
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
																<fo:page-number-citation>
																	<xsl:attribute name="ref-id">
																		<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
																	</xsl:attribute>
																</fo:page-number-citation>
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
					</xsl:if>
					<xsl:if test="$uhitCount &gt; 0">
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
											<xsl:for-each select="./Test/Tags/Tag[text()=$tag]/../..">
												<xsl:if test="@edited='true'">
													<fo:table-row font-style="italic">
														<fo:table-cell>
															<fo:block>
																<xsl:value-of select="../@ID+  $extraCatCount"/>.<xsl:value-of select="./@ID"/>
															</fo:block>
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
																<fo:page-number-citation>
																	<xsl:attribute name="ref-id">
																		<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
																	</xsl:attribute>
																</fo:page-number-citation>
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
					</xsl:if>
					<xsl:if test="$tag='!!'">
					<fo:block font-size="medium">
						<xsl:apply-templates select="./TagsDescription/TagDescription"/>
					</fo:block>
					</xsl:if>
					<fo:block font-size="medium">
						<xsl:apply-templates select="./Category"/>
					</fo:block>
					<fo:block id="endOfDocument"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="TagDescription">
		<fo:block font-size="medium" padding-bottom="2mm" page-break-after="right">
			<xsl:attribute name="id">
				<xsl:value-of select="./TagName"/>
			</xsl:attribute>
			<fo:block padding-bottom="2mm" padding-top="2mm" background-color="./Color" text-align="center" font-variant="small-caps" font-weight="bold">
				<xsl:value-of select="./TagName"/>
			</fo:block>
			<fo:block padding-bottom="2mm" padding-top="1mm">
				<xsl:value-of select="./Description"/>
				<!--xsl:value-of select="count(./Test/Tags/Tag[contains(text(),'tag')])"/-->
			</fo:block>
			<fo:block padding-bottom="3mm" padding-top="3mm">
				<xsl:variable name="comp">
					<xsl:value-of select="./TagName"/>
				</xsl:variable>
				<xsl:variable name="nTests">
					<xsl:value-of select="count(//*/Test/Tags/Tag[text()=$comp])"/>
				</xsl:variable>
				<!--xsl:value-of select="$nTests"/-->
				<xsl:if test="$nTests &gt; 0">
					<fo:table>
						<fo:table-column column-width="7mm" />
						<fo:table-column column-width="200mm - 2 * 25mm - 7mm - 3mm" />
						<fo:table-column column-width="3mm" />
						<fo:table-body>
							<xsl:for-each select="//Test/Tags/Tag[text()=$comp]/../..">
								<fo:table-row font-style="italic">
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
										</fo:block>
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
											<fo:page-number-citation>
												<xsl:attribute name="ref-id">
													<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
												</xsl:attribute>
											</fo:page-number-citation>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
							<xsl:for-each select="./TestCombination">
								<fo:table-row font-style="italic">
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="./@ID"/>
										</fo:block>
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
											<fo:page-number-citation>
												<xsl:attribute name="ref-id">
													<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
												</xsl:attribute>
											</fo:page-number-citation>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>
				</xsl:if>
			</fo:block>
		</fo:block>
	</xsl:template>
	<xsl:template match="Category">

    <fo:float float="outside">
      <fo:block xsl:use-attribute-sets="sidebar.properties">
          <fo:block font-weight="bold"
            keep-with-next.within-column="always"
            hyphenate="false">
            sidebar
          </fo:block>
      </fo:block>
    </fo:float>

		<fo:block font-size="medium" padding-bottom="2mm" page-break-after="right">
			<xsl:attribute name="id">
				<xsl:value-of select="./@ID"/>
			</xsl:attribute>
			<fo:block padding-bottom="2mm" padding-top="2mm" background-color="lightgray" text-align="center" font-variant="small-caps" font-weight="bold">
				<xsl:value-of select="@name"/>
			</fo:block>
			<fo:block padding-bottom="2mm" padding-top="1mm">
				<xsl:value-of select="./Description"/>
				<!--xsl:value-of select="count(./Test/Tags/Tag[contains(text(),'tag')])"/-->
			</fo:block>
			<fo:block padding-bottom="3mm" padding-top="3mm">
				<xsl:variable name="nTests">
					<xsl:value-of select="count(./Test/Tags/Tag[text()=$tag])"/>
				</xsl:variable>
				<xsl:if test="$nTests &gt; 0">
					<fo:table>
						<fo:table-column column-width="7mm" />
						<fo:table-column column-width="200mm - 2 * 25mm - 7mm - 3mm" />
						<fo:table-column column-width="3mm" />
						<fo:table-body>
							<xsl:for-each select="./Test/Tags/Tag[text()=$tag]/../..">
								<fo:table-row font-style="italic">
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="./@ID"/>
										</fo:block>
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
											<fo:page-number-citation>
												<xsl:attribute name="ref-id">
													<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
												</xsl:attribute>
											</fo:page-number-citation>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
							<xsl:for-each select="./TestCombination">
								<fo:table-row font-style="italic">
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="./@ID"/>
										</fo:block>
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
											<fo:page-number-citation>
												<xsl:attribute name="ref-id">
													<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
												</xsl:attribute>
											</fo:page-number-citation>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>
				</xsl:if>
			</fo:block>
			<fo:block font-size="medium" padding-bottom="2mm">
				<xsl:apply-templates select="./Test/Tags/Tag[text()=$tag]/../.."/>
			</fo:block>
			<fo:block font-size="medium" padding-bottom="2mm">
				<xsl:apply-templates select="./TestCombination"/>
			</fo:block>
		</fo:block>
	</xsl:template>
	<xsl:template match="Test">
		<fo:block font-size="medium" padding-bottom="2mm" space-after="10mm">
			<fo:block keep-together.within-page="always">
				<xsl:attribute name="id">
					<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
				</xsl:attribute>
				<fo:block padding-bottom="1mm" font-weight="bold">
					<fo:inline padding-right="3mm">
						<xsl:value-of select="./@ID"/>
					</fo:inline>
					<fo:inline>
						<xsl:value-of select="./Description"/>
					</fo:inline>
				</fo:block>
				<xsl:if test="./@RequirementID">
					<fo:block font-style="italic">Anforderung: 
						<fo:inline color="blue" font-weight="bold" space-before="0.5mm" space-after="0.5mm">
							<xsl:value-of select="@RequirementID"/>
						</fo:inline>
					</fo:block>
				</xsl:if>
				<xsl:variable name="nTags">
					<xsl:value-of select="count(./Tags/Tag)"/>
				</xsl:variable>
				<xsl:variable name="nTagsh">
					<xsl:value-of select="count(./Tags/Tag[@hidden='true'])"/>
				</xsl:variable>
				<xsl:if test="$nTags &gt; $nTagsh">
					<fo:block><fo:inline font-style="italic">Schlüsselworte: </fo:inline>
			<xsl:apply-templates select="./Tags"/>
		</fo:block>
				</xsl:if>		
				<xsl:variable name="nChildsa">
					<xsl:value-of select="count(./Actions/Action)"/>
				</xsl:variable>
				<xsl:if test="$nChildsa &gt; 0">
					<fo:block font-style="italic">Vorgehen</fo:block>
					<fo:block padding-bottom="1mm">
						<fo:list-block provisional-distance-between-starts="15mm" provisional-label-separation="5mm">
							<xsl:apply-templates select="./Actions"/>
						</fo:list-block>
					</fo:block>
				</xsl:if>
			</fo:block>
			<fo:block keep-together.within-page="always">
				<xsl:variable name="nChildsr">
					<xsl:value-of select="count(./ExpectedResults/ExpectedResult)"/>
				</xsl:variable>
				<xsl:if test="$nChildsr &gt; 0">
					<fo:block font-style="italic">Erwartete Resultate</fo:block>
					<fo:block padding-bottom="1mm">
						<fo:list-block provisional-distance-between-starts="15mm" provisional-label-separation="5mm">
							<xsl:apply-templates select="./ExpectedResults"/>
						</fo:list-block>
					</fo:block>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="./@Variants">
						<xsl:if test="not(./@Variants = '')">
							<fo:block
								border-top-color="black" border-top-style="dotted" border-top-width="thin"
								border-bottom-color="black" border-bottom-style="dotted" border-bottom-width="thin"
								border-left-color="black" border-left-style="dotted" border-left-width="thin"
								border-right-color="black" border-right-style="dotted" border-right-width="thin"
								space-after="1mm">
								<fo:block color="red" font-weight="bold" space-before="0.5mm" space-after="0.5mm">
									<xsl:value-of select="./@Variants"/>
								</fo:block>
							</fo:block>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
				<xsl:variable name="nChildsa">
					<xsl:value-of select="count(./Variants/Variant)"/>
				</xsl:variable>
				<xsl:if test="$nChildsa &gt; 0">
					<fo:block font-style="italic">Varianten</fo:block>
					<fo:block padding-bottom="1mm">
						<fo:table table-layout="fixed" height="2cm">
							<fo:table-column column-width="7cm"/>
							<fo:table-column column-width="3cm"/>
							<fo:table-header background-color="lightgray">
								<fo:table-row>
									<fo:table-cell border-top-color="black" border-top-style="dotted" border-top-width="thin"
												   border-bottom-color="black" border-bottom-style="dotted" border-bottom-width="thin"
												   border-left-color="black" border-left-style="dotted" border-left-width="thin"
												   border-right-color="black" border-right-style="dotted" border-right-width="thin">
										<fo:block  text-align="end" margin="1mm">Variante</fo:block>
									</fo:table-cell>
									<fo:table-cell border-top-color="black" border-top-style="dotted" border-top-width="thin"
												   border-bottom-color="black" border-bottom-style="dotted" border-bottom-width="thin"
												   border-left-color="black" border-left-style="dotted" border-left-width="thin"
												   border-right-color="black" border-right-style="dotted" border-right-width="thin">
										<fo:block  text-align="center" margin="1mm">OK</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<xsl:apply-templates select="./Variants"/>
								<!--fo:table-row>
									<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
									<fo:table-cell><fo:block><fo:leader leader-length="2cm">.</fo:leader></fo:block></fo:table-cell>
								</fo:table-row-->
							</fo:table-body>
						</fo:table>
						<!--fo:list-block provisional-distance-between-starts="15mm" provisional-label-separation="5mm">
						<xsl:apply-templates select="./Actions"/>
						</fo:list-block-->
					</fo:block>
				</xsl:if>		
				<fo:table  inline-progression-dimension="auto" table-layout="auto" height="2cm">
					<fo:table-column column-width="3cm"/>
					<fo:table-column column-width="3cm"/>
					<fo:table-column column-width="proportional-column-width(1)"/>
					<fo:table-header background-color="lightgray">
						<fo:table-row>
							<fo:table-cell>	
								<fo:block font-weight="bold">Tester</fo:block>
							</fo:table-cell>
							<fo:table-cell>	
								<fo:block font-weight="bold">Datum</fo:block>
							</fo:table-cell>
							<fo:table-cell>	
								<fo:block font-weight="bold">Bemerkungen</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>
					<fo:table-body>
						<fo:table-row>
							<fo:table-cell>
								<fo:block keep-together.within-line="always">
									%QF<fo:leader leader-length="1.1cm" leader-alignment="reference-area" leader-pattern="dots"></fo:leader>QF%
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block keep-together.within-line="always">
									%DF<fo:leader leader-length="1.1cm" leader-alignment="reference-area" leader-pattern="dots"></fo:leader>DF%
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block keep-together.within-line="always">
									%TF<fo:leader leader-length="8.5cm" leader-alignment="reference-area" leader-pattern="dots"></fo:leader>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>	<fo:block></fo:block> </fo:table-cell>
							<fo:table-cell>	<fo:block></fo:block> </fo:table-cell>
							<fo:table-cell>	<fo:block>M</fo:block> </fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>	<fo:block></fo:block> </fo:table-cell>
							<fo:table-cell>	<fo:block></fo:block> </fo:table-cell>
							<fo:table-cell>	<fo:block>M</fo:block> </fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>	<fo:block></fo:block> </fo:table-cell>
							<fo:table-cell>	<fo:block></fo:block> </fo:table-cell>
							<fo:table-cell>	<fo:block>M</fo:block> </fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell>
								<fo:block>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block>
								</fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block keep-together.within-line="always">
									<fo:leader leader-length="8.5cm" leader-alignment="reference-area" leader-pattern="dots"></fo:leader>TF%
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</fo:block>
	</xsl:template>
	<xsl:template match="TestCombination">
		<!--fo:block><xsl:value-of select="@ID"/></fo:block-->
		<fo:block font-size="medium" padding-bottom="2mm" space-after="10mm">
			<xsl:attribute name="id">
				<xsl:value-of select="../@ID"/>.<xsl:value-of select="./@ID"/>
			</xsl:attribute>
			<fo:block padding-bottom="1mm" font-weight="bold">
				<xsl:value-of select="./@ID"/> 
				<xsl:value-of select="./Description"/>
			</fo:block>
			<fo:block font-style="italic">Kombination aus:</fo:block>
			<fo:block padding-bottom="1mm">
				<fo:list-block provisional-distance-between-starts="15mm" provisional-label-separation="5mm">
					<xsl:apply-templates select="./ReferencedTests"/>
				</fo:list-block>
			</fo:block>
			<fo:table table-layout="fixed" height="2cm">
				<fo:table-column column-width="3cm"/>
				<fo:table-column column-width="3cm"/>
				<fo:table-column column-width="4cm"/>
				<fo:table-header background-color="lightgray">
					<fo:table-row>
						<fo:table-cell>	
							<fo:block font-weight="bold">Tester</fo:block>
						</fo:table-cell>
						<fo:table-cell>	
							<fo:block font-weight="bold">Datum</fo:block>
						</fo:table-cell>
						<fo:table-cell>	
							<fo:block font-weight="bold">Bemerkungen</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block text-align-last="justify">
								<fo:leader  leader-alignment="reference-area" leader-pattern="dots"></fo:leader>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block text-align-last="justify">
								<fo:leader  leader-alignment="reference-area" leader-pattern="dots"></fo:leader>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block text-align-last="justify">
								<fo:leader  leader-alignment="reference-area" leader-pattern="dots"></fo:leader>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
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
		<fo:list-item>
			<fo:list-item-label start-indent="0mm" end-indent="label-end()">
				<fo:block>
					<xsl:number format="1."/>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block text-align-last="justify">
					<xsl:if test="(./@fromMacro='true')">
						<xsl:choose>
							<xsl:when test="(contains(., '§'))">
								<fo:inline color="#FF0000" font-style="italic"><xsl:value-of select="."/></fo:inline>
							</xsl:when>
							<xsl:otherwise>
								<fo:inline font-style="italic"><xsl:value-of select="."/></fo:inline>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="not(./@fromMacro='true')">
						<fo:inline><xsl:value-of select="."/></fo:inline>
					</xsl:if>
					<fo:leader leader-alignment="reference-area" leader-pattern="dots"></fo:leader>
					<xsl:text disable-output-escaping="yes">%x%</xsl:text>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<xsl:template match="Action">
		<fo:list-item>
			<fo:list-item-label start-indent="0mm" end-indent="label-end()">
				<fo:block>
					<xsl:number format="1."/>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block text-align-last="justify">
					<xsl:if test="(./@fromMacro='true')">
						<xsl:choose>
							<xsl:when test="(contains(., '§'))">
								<fo:inline color="#FF0000" font-style="italic"><xsl:value-of select="."/></fo:inline>
							</xsl:when>
							<xsl:otherwise>
								<fo:inline font-style="italic"><xsl:value-of select="."/></fo:inline>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="not(./@fromMacro='true')">
						<fo:inline><xsl:value-of select="."/></fo:inline>
					</xsl:if>
					<fo:leader leader-alignment="reference-area" leader-pattern="dots"></fo:leader>
					<xsl:text disable-output-escaping="yes">%x%</xsl:text>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<xsl:template match="Variant">
		<fo:table-row>
			<fo:table-cell border-top-color="black" border-top-style="dotted" border-top-width="thin"
						   border-bottom-color="black" border-bottom-style="dotted" border-bottom-width="thin"
						   border-left-color="black" border-left-style="dotted" border-left-width="thin"
						   border-right-color="black" border-right-style="dotted" border-right-width="thin">
				<fo:block text-align="end" margin="1mm">
					<xsl:value-of select="."/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-top-color="black" border-top-style="dotted" border-top-width="thin"
						   border-bottom-color="black" border-bottom-style="dotted" border-bottom-width="thin"
						   border-left-color="black" border-left-style="dotted" border-left-width="thin"
						   border-right-color="black" border-right-style="dotted" border-right-width="thin"
			display-align="center">
				<fo:block text-align="center"><xsl:text disable-output-escaping="yes">%x%</xsl:text>
					<!--&#9744;--><!--fo:leader leader-length="2cm">.</fo:leader-->
					<!--fo:instream-foreign-object xmlns:svg="http://www.w3.org/2000/svg">
						<svg:svg width="20" height="20">
							<svg:g style="fill:none; stroke:#000000">
								<svg:rect x="2" y="2" width="16" height="16"/-->
								<!--svg:rect x="5" y="5" width="15" height="15"/-->
							<!--/svg:g>
						</svg:svg>
					</fo:instream-foreign-object-->				
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<xsl:template match="ReferencedTest">
		<fo:list-item>
			<fo:list-item-label start-indent="0mm" end-indent="label-end()">
				<fo:block>
					<xsl:number format="1."/>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block>Test <xsl:value-of select="./@TestID"/> aus Kategorie <xsl:value-of select="./@CategoryName"/></fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	<xsl:template match="Tags">
			<xsl:apply-templates select="./Tag"/>
	</xsl:template>
	<xsl:template match="Tag">
				<xsl:if test="not(@hidden='true')">
				<fo:inline padding-end="1em">
					<xsl:choose>
						<xsl:when test="$tag='!!'">
					<fo:basic-link>
					<xsl:attribute name="internal-destination">
					<xsl:value-of select="."/>
					</xsl:attribute>
					<xsl:value-of select="."/>
					</fo:basic-link>
						</xsl:when>
						<xsl:otherwise>
						<xsl:value-of select="."/>							
						</xsl:otherwise>
					</xsl:choose>
				</fo:inline>
				</xsl:if>		
	</xsl:template>
</xsl:stylesheet>

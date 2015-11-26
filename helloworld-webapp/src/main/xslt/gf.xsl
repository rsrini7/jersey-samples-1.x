<!--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    http://glassfish.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:pom="http://maven.apache.org/POM/4.0.0" version="1.0" >
<!--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    http://glassfish.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

    Contributor(s):
    If you wish your version of this file to be governed by only the CDDL or
    only the GPL Version 2, indicate your decision by adding "[Contributor]
    elects to include this software in this distribution under the [CDDL or GPL
    Version 2] license."  If you don't indicate a single choice of license, a
    recipient has the option to distribute your version of this file under
    either the CDDL, the GPL Version 2 or to extend the choice of license to
    its licensees as provided above.  However, if you add GPL Version 2 code
    and therefore, elected the GPL Version 2 license, then the option applies
    only if the new code is made subject to such option by the copyright
    holder.

-->
          <xsl:output method="xml" indent="yes" />

			<xsl:template match="/">
<xsl:apply-templates/>
</xsl:template>			
			
          <xsl:template match="pom:profile[pom:id='default']/pom:dependencies/pom:dependency[pom:groupId='com.sun.jersey.contribs' or pom:groupId='com.sun.jersey' or pom:groupId='com.sun.xml.bind' or pom:groupId='javax.servlet']/pom:scope[text()!=test]">
          		<scope>provided</scope>
          </xsl:template>

          <xsl:template match="pom:profile[pom:id='default']/pom:dependencies/pom:dependency[pom:groupId='com.sun.jersey.contribs' or pom:groupId='com.sun.jersey'  or pom:groupId='com.sun.xml.bind' or pom:groupId='javax.servlet']">
            	<xsl:copy>
              	<xsl:apply-templates/>
              	<xsl:if test="count(pom:scope)=0">
              		<scope>provided</scope>
              	</xsl:if>
            	</xsl:copy>
          </xsl:template>

          <!-- remove <packagingExcludes>WEB-INF/glassfish-web.xml</packagingExcludes>
               as this file is required in Glassfish bundle since <class-loader> 
               is defined in it -->
          <xsl:template match="pom:plugin[pom:artifactId='maven-war-plugin']/pom:configuration[pom:packagingExcludes]">
          </xsl:template>
          
          <!--build war even if web.xml is missing as it's not required, 
              <packagingIncludes> defaults to 'all' so it includes 
              <packagingIncludes>WEB-INF/glassfish-web.xml</packagingIncludes> 
              to pick up <class-loader> -->
          <xsl:template match="pom:plugin[pom:artifactId='maven-war-plugin']">
            	<xsl:copy>
              	<xsl:apply-templates/>              	
                <xsl:if test="count(pom:configuration)=1">
                    <configuration>
                      <failOnMissingWebXml>false</failOnMissingWebXml>                                            
                    </configuration>                   
              	</xsl:if>
            	</xsl:copy>
          </xsl:template>                    
            
          <xsl:template match="comment()">
            <xsl:comment><xsl:value-of select="."/></xsl:comment>
          </xsl:template>

          <xsl:template match="*">
            	<xsl:copy>
              	<xsl:apply-templates/>
            	</xsl:copy>
          </xsl:template>

     </xsl:stylesheet> 

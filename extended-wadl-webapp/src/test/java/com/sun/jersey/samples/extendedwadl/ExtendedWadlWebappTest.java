/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.samples.extendedwadl;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.MediaTypes;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

/**
 *
 * @author Naresh (Srinivas.Bhimisetty@Sun.com)
 */
public class ExtendedWadlWebappTest extends JerseyTest {

    public ExtendedWadlWebappTest() throws Exception {
        super(new WebAppDescriptor.Builder("com.sun.jersey.samples.extendedwadl.resources")
                .contextPath("extended-wadl-webapp")
                .initParam("com.sun.jersey.config.property.WadlGeneratorConfig",
                        "com.sun.jersey.samples.extendedwadl.SampleWadlGeneratorConfig")
                .build());

    }

    /**
     * Test checks that the WADL generated using the WadlGenerator api doesn't
     * contain the expected text.
     * @throws java.lang.Exception
     */
    @Test
    public void testExtendedWadl() throws Exception {

        WebResource webResource = resource();
        String wadl = webResource.path("application.wadl").accept(MediaTypes.WADL).get(String.class);

        assertTrue("Generated wadl is of null length", wadl.length() > 0);
        assertTrue("Generated wadl doesn't contain the expected text",
                wadl.contains("This is a paragraph"));

        assertFalse(wadl.contains("application.wadl/xsd0.xsd"));
    }

    @Test
    public void testWadlOptionsMethod() throws Exception {

        WebResource webResource = resource();
        String wadl = webResource.path("items").accept(MediaTypes.WADL).options(String.class);

        assertTrue("Generated wadl is of null length", wadl.length() > 0);
        assertTrue("Generated wadl doesn't contain the expected text",
                wadl.contains("This is a paragraph"));
    }
}

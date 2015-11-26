/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.jersey.samples.optimisticconcurrency;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.MediaTypes;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.core.MediaType;

/**
 *
 * @author Naresh (srinivas.bhimisetty@sun.com)
 */
public class MainTest {

    private HttpServer httpServer;

    private WebResource r;    

    @Before
    public void setUp() throws Exception {
        
        //start the Grizzly web container and create the client
        httpServer = Main.startServer();

        Client c = Client.create();
        r = c.resource(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {       
        httpServer.stop();
    }

    /**
     * Test checks that an application.wadl file is present for the resource.
     */
    @Test
    public void testApplicationWadl() {
        String serviceWadl = r.path("application.wadl").
                accept(MediaTypes.WADL).get(String.class);
        assertTrue("Looks like the expected wadl is not generated",
                serviceWadl.length() > 0);
    }

    /**
     * Test checks that an xml content is shown for the client request to
     * resource "item".
     */
    @Test
    public void testItemResource() {
        String serviceXml = r.path("item").
                accept(MediaType.APPLICATION_XML).get(String.class);
        assertTrue("Looks like the given xml response is not the expected one",
                serviceXml.length() > 0);
    }

    /**
     * Test checks that the initial content seen in the response
     * is the same as what is expected.
     */
    @Test
    public void testItemContentResource() {
        String itemContent = r.path("item").path("content").
                accept(MediaType.TEXT_PLAIN).get(String.class);
        String expectedContentPrefix = "Today is";
        assertTrue("The response text doesn't start with the expected value",
                itemContent.startsWith(expectedContentPrefix));
    }

    /**
     * Test checks the PUT on resource item/content works,
     * and is allowed only once per content item.
     */
    @Test
    public void testOnUpdateItemContent() {
        // Create a child WebResource
        WebResource content = r.path("item").path("content");

        String putData = "All play and no REST makes me a dull boy";
        content.path("0").type(MediaType.TEXT_PLAIN)
                .put(putData);
        String contentData = content.get(String.class);
        assertEquals("Data that has been PUT is not the same as what is retrieved",
                putData, contentData);
        // check that the 409 error is seen on a duplicate update
        ClientResponse cr = content.path("0").type(MediaType.TEXT_PLAIN)
                .put(ClientResponse.class , putData);
        int responseStatus = cr.getStatus();
        assertEquals("Expected 409 HTTP error not seen", 409, responseStatus);
    }
    
}

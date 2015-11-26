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

package com.sun.jersey.samples.moxy;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.core.header.MediaTypes;
import com.sun.jersey.moxy.MoxyContextResolver;
import com.sun.jersey.samples.moxy.beans.Customer;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.util.Collection;
import javax.ws.rs.core.MediaType;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Jakub Podlesak
 */
public class MoxyWebAppTest extends JerseyTest {


    public MoxyWebAppTest() throws Exception {
        super(new WebAppDescriptor.Builder()
                .contextPath("moxy")
                .initParam(MoxyContextResolver.PROPERTY_MOXY_OXM_PACKAGE_NAMES,
                                                    Customer.class.getPackage().getName())
                .initParam(ServletContainer.RESOURCE_CONFIG_CLASS, ClassNamesResourceConfig.class.getName())
                .initParam(ClassNamesResourceConfig.PROPERTY_CLASSNAMES, CustomersResource.class.getName())
                .clientConfig(clientConfig).build());
    }

    private static final ClientConfig clientConfig;

    static {
        ClientConfig cc = new DefaultClientConfig();

        cc.getProperties().put(MoxyContextResolver.PROPERTY_MOXY_OXM_PACKAGE_NAMES,
                                                    Customer.class.getPackage().getName());
        clientConfig = cc;
    }

    @Test
    public void testCustomers() throws Exception {
        WebResource webResource = resource().path("customers");
        webResource.addFilter(new LoggingFilter());
        GenericType<Collection<Customer>> genericType = 
                new GenericType<Collection<Customer>>() {};
        

        Collection<Customer> customers = webResource.accept(MediaType.APPLICATION_XML).get(genericType);
        Assert.assertEquals(customers.size(), 2);
    }

    @Test
    public void testCustomer() throws Exception {
        WebResource webResource = resource().path("customers/1");
        webResource.addFilter(new LoggingFilter());

        Customer customer = webResource.accept(MediaType.APPLICATION_XML).get(Customer.class);
        customer.setName("Tom Dooley");
        webResource.type(MediaType.APPLICATION_XML).put(customer);

        Customer updatedCustomer = webResource.accept(MediaType.APPLICATION_XML).get(Customer.class);
        Assert.assertEquals(customer, updatedCustomer);
    }

    //@Test disabled until XML schema generation is fixed for externally bound bean collections
    public void testApplicationWadl() {
        WebResource webResource = resource();
        String serviceWadl = webResource.path("application.wadl").
                accept(MediaTypes.WADL).get(String.class);

        Assert.assertTrue(serviceWadl.length() > 0);
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.chemistry.opencmis.server.impl.webservices;

import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.ws.WebServiceFeature;

import org.apache.chemistry.opencmis.commons.enums.CmisVersion;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.chemistry.opencmis.commons.server.CmisServiceFactory;
import org.apache.chemistry.opencmis.server.impl.CmisRepositoryContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.ws.api.WSFeatureList;
import com.sun.xml.ws.developer.StreamingAttachmentFeature;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.WSServlet;
import com.sun.xml.ws.transport.http.servlet.WSServletDelegate;

public class CmisWebServicesServlet extends WSServlet {

    public static final String PARAM_CMIS_VERSION = "cmisVersion";

    public static final String CMIS_VERSION = "org.apache.chemistry.opencmis.cmisVersion";

    private static final Logger LOG = LoggerFactory.getLogger(CmisWebServicesServlet.class.getName());

    private static final long serialVersionUID = 1L;

    private CmisVersion cmisVersion;

    @Override
    public void init(ServletConfig config) throws ServletException {

        // get CMIS version
        String cmisVersionStr = config.getInitParameter(PARAM_CMIS_VERSION);
        if (cmisVersionStr != null) {
            try {
                cmisVersion = CmisVersion.fromValue(cmisVersionStr);

                // !!! As long as CMIS 1.1 is not implemented, we have to set
                // the CMIS version to 1.0 !!!
                cmisVersion = CmisVersion.CMIS_1_0;
            } catch (IllegalArgumentException e) {
                LOG.warn("CMIS version is invalid! Setting it to CMIS 1.0.");
                cmisVersion = CmisVersion.CMIS_1_0;
            }
        } else {
            LOG.warn("CMIS version is not defined! Setting it to CMIS 1.0.");
            cmisVersion = CmisVersion.CMIS_1_0;
        }

        config.getServletContext().setAttribute(CMIS_VERSION, cmisVersion);

        super.init(config);
    }

    @Override
    protected WSServletDelegate getDelegate(ServletConfig servletConfig) {
        WSServletDelegate delegate = super.getDelegate(servletConfig);

        // set temp directory and the threshold for all services with a
        // StreamingAttachment annotation
        if (delegate.adapters != null) {
            // get the CmisService factory
            CmisServiceFactory factory = (CmisServiceFactory) getServletContext().getAttribute(
                    CmisRepositoryContextListener.SERVICES_FACTORY);

            if (factory == null) {
                throw new CmisRuntimeException("Service factory not available! Configuration problem?");
            }

            // iterate of all adapters
            for (ServletAdapter adapter : delegate.adapters) {
                WSFeatureList wsfl = adapter.getEndpoint().getBinding().getFeatures();
                for (WebServiceFeature ft : wsfl) {
                    if (ft instanceof StreamingAttachmentFeature) {
                        ((StreamingAttachmentFeature) ft).setDir(factory.getTempDirectory().getAbsolutePath());
                        setMemoryThreshold(factory, (StreamingAttachmentFeature) ft);
                    }
                }
            }
        }

        return delegate;
    }

    private void setMemoryThreshold(CmisServiceFactory factory, StreamingAttachmentFeature ft) {
        try {
            // JAX-WS RI 2.1
            ft.setMemoryThreshold(factory.getMemoryThreshold());
        } catch (NoSuchMethodError e) {
            // JAX-WS RI 2.2 
            // see CMIS-626
            try {
                Method m = ft.getClass().getMethod("setMemoryThreshold", long.class);
                m.invoke(ft, (long) factory.getMemoryThreshold());
            } catch (Exception e2) {
                LOG.warn("Could not set memory threshold for streaming");
            }
        }
    }
}

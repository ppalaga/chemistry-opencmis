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
package org.apache.chemistry.opencmis.fit.tck;

import java.util.Map;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.CmisVersion;

public class AtomPub11TckIT extends AbstractTckIT {

    public static final String ATOMPUB_PATH = "/opencmis/atom11";

    @Override
    public Map<String, String> getSessionParameters() {
        Map<String, String> parameters = getBaseSessionParameters();

        String url = "http://" + HOST + ":" + PORT + ATOMPUB_PATH;

        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameters.put(SessionParameter.ATOMPUB_URL, url);

        return parameters;
    }

    @Override
    public BindingType getBindingType() {
        return BindingType.ATOMPUB;
    }

    @Override
    public CmisVersion getCmisVersion() {
        return CmisVersion.CMIS_1_1;
    }

}

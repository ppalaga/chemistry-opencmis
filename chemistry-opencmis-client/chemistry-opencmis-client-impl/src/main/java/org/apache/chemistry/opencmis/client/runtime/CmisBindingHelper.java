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
package org.apache.chemistry.opencmis.client.runtime;

import java.util.Map;

import org.apache.chemistry.opencmis.client.bindings.factory.CmisBindingFactory;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.api.CmisBinding;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;

/**
 * Helper methods for binding handling.
 */
public class CmisBindingHelper {
  /**
   * Creates a {@link CmisProvider} object.
   */
  public static CmisBinding createProvider(Map<String, String> parameters) {
    if (parameters == null || parameters.isEmpty()) {
      throw new CmisRuntimeException("Session parameter not set!");
    }

    if (!parameters.containsKey(SessionParameter.BINDING_TYPE)) {
      parameters.put(SessionParameter.BINDING_TYPE, BindingType.CUSTOM.value());
    }

    BindingType bt = BindingType.fromValue(parameters.get(SessionParameter.BINDING_TYPE));

    switch (bt) {
    case ATOMPUB:
      return createAtomPubBinding(parameters);
    case WEBSERVICES:
      return createWebServiceBinding(parameters);
    case CUSTOM:
      return createCustomBinding(parameters);
    default:
      throw new CmisRuntimeException("Ambiguous session parameter: " + parameters);
    }
  }

  /**
   * Creates a provider with custom parameters.
   */
  private static CmisBinding createCustomBinding(Map<String, String> parameters) {
    CmisBindingFactory factory = CmisBindingFactory.newInstance();
    CmisBinding binding = factory.createCmisBinding(parameters);

    return binding;
  }

  /**
   * Creates a Web Services provider.
   */
  private static CmisBinding createWebServiceBinding(Map<String, String> parameters) {
    CmisBindingFactory factory = CmisBindingFactory.newInstance();
    CmisBinding binding = factory.createCmisWebServicesBinding(parameters);

    return binding;
  }

  /**
   * Creates an AtomPub provider.
   */
  private static CmisBinding createAtomPubBinding(Map<String, String> parameters) {
    CmisBindingFactory factory = CmisBindingFactory.newInstance();
    CmisBinding binding = factory.createCmisAtomPubBinding(parameters);

    return binding;
  }
}

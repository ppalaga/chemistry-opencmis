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
package org.apache.chemistry.opencmis.tck.tests.crud;

import java.util.Map;

import org.apache.chemistry.opencmis.tck.impl.AbstractSessionTestGroup;

/**
 * This test group contains simple Create, Read, Update and Delete tests.
 */
public class CRUDTestGroup extends AbstractSessionTestGroup {
    @Override
    public void init(Map<String, String> parameters) throws Exception {
        super.init(parameters);

        setName("CRUD Test Group");
        setDescription("Create, Read, Update, and Delete tests.");

        addTest(new CreateAndDeleteFolderTest());
        addTest(new CreateAndDeleteDocumentTest());
        addTest(new CreateBigDocument());
        addTest(new CreateDocumentWithoutContent());
        addTest(new NameCharsetTest());
        addTest(new CreateAndDeleteRelationshipTest());
        addTest(new CreateAndDeleteItemTest());
        addTest(new UpdateSmokeTest());
        addTest(new BulkUpdatePropertiesTest());
        addTest(new SetAndDeleteContentTest());
        addTest(new ContentRangesTest());
        addTest(new CopyTest());
        addTest(new MoveTest());
        addTest(new DeleteTreeTest());
    }
}

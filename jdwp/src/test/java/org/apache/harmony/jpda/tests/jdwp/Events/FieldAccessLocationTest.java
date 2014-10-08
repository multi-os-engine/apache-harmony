/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;


/**
 * JDWP Unit test for FIELD_ACCESS event.
 */
public class FieldAccessLocationTest extends FieldLocationEventTestCase {

    /**
     * This testcase is for FIELD_ACCESS event.
     * <BR>It runs FieldDebuggee that accesses to the value of its internal field
     * and verify that requested FIELD_ACCESS event occurs in the
     * expected method.
     */
    public void testFieldAccessLocationEvent() {
        logWriter.println("testFieldAccessLocationEvent started");

        // Check canWatchFieldAccess capability, relevant for this test.
        logWriter.println("=> Check capability: canWatchFieldAccess");
        if (!debuggeeWrapper.vmMirror.canWatchFieldAccess()) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canWatchFieldAccess");
            return;
        }

        testFieldEvent(JDWPConstants.EventKind.FIELD_ACCESS);

        logWriter.println("testFieldAccessLocationEvent done");
    }
}

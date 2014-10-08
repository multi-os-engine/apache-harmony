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
 * JDWP Unit test for FIELD_MODIFICATION event.
 */
public class FieldModificationLocationTest extends FieldLocationEventTestCase {

    /**
     * This testcase is for FIELD_MODIFICATION event.
     * <BR>It runs FieldDebuggee that modifies the value of its internal field
     * and verify that requested FIELD_MODIFICATION event occurs in the
     * expected method.
     */
    public void testFieldModificationLocationEvent() {
        logWriter.println("testFieldModificationLocationEvent started");

        // Check canWatchFieldModification capability, relevant for this test.
        logWriter.println("=> Check capability: canWatchFieldModification");
        if (!debuggeeWrapper.vmMirror.canWatchFieldModification()) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canWatchFieldModification");
            return;
        }

        testFieldEvent(JDWPConstants.EventKind.FIELD_MODIFICATION);

        logWriter.println("testFieldModificationLocationEvent done");
    }
}

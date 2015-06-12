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

/**
 * @author Anton V. Karnachuk
 */

/**
 * Created on 11.04.2005
 */
package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP Unit test for FIELD_ACCESS event.
 */
public class FieldAccessTest extends JDWPFieldModificationEventTestCase {
    /**
     * Tests FIELD_ACCESS event on a boolean instance field.
     */
    public void testFieldAccessEvent_InstanceBooleanField() {
        runFieldAccessEventTest(JDWPConstants.Tag.BOOLEAN_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on a byte instance field.
     */
    public void testFieldAccessEvent_InstanceByteField() {
        runFieldAccessEventTest(JDWPConstants.Tag.BYTE_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on a char instance field.
     */
    public void testFieldAccessEvent_InstanceCharField() {
        runFieldAccessEventTest(JDWPConstants.Tag.CHAR_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on a short instance field.
     */
    public void testFieldAccessEvent_InstanceShortField() {
        runFieldAccessEventTest(JDWPConstants.Tag.SHORT_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on an int instance field.
     */
    public void testFieldAccessEvent_InstanceIntField() {
        runFieldAccessEventTest(JDWPConstants.Tag.INT_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on a float instance field.
     */
    public void testFieldAccessEvent_InstanceFloatField() {
        runFieldAccessEventTest(JDWPConstants.Tag.FLOAT_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on a long instance field.
     */
    public void testFieldAccessEvent_InstanceLongField() {
        runFieldAccessEventTest(JDWPConstants.Tag.LONG_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on a double instance field.
     */
    public void testFieldAccessEvent_InstanceDoubleField() {
        runFieldAccessEventTest(JDWPConstants.Tag.DOUBLE_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on an Object instance field.
     */
    public void testFieldAccessEvent_InstanceObjectField() {
        runFieldAccessEventTest(JDWPConstants.Tag.OBJECT_TAG, false);
    }

    /**
     * Tests FIELD_ACCESS event on a boolean static field.
     */
    public void testFieldAccessEvent_StaticBooleanField() {
        runFieldAccessEventTest(JDWPConstants.Tag.BOOLEAN_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on a byte static field.
     */
    public void testFieldAccessEvent_StaticByteField() {
        runFieldAccessEventTest(JDWPConstants.Tag.BYTE_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on a char static field.
     */
    public void testFieldAccessEvent_StaticCharField() {
        runFieldAccessEventTest(JDWPConstants.Tag.CHAR_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on a short static field.
     */
    public void testFieldAccessEvent_StaticShortField() {
        runFieldAccessEventTest(JDWPConstants.Tag.SHORT_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on an int static field.
     */
    public void testFieldAccessEvent_StaticIntField() {
        runFieldAccessEventTest(JDWPConstants.Tag.INT_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on a float static field.
     */
    public void testFieldAccessEvent_StaticFloatField() {
        runFieldAccessEventTest(JDWPConstants.Tag.FLOAT_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on a long static field.
     */
    public void testFieldAccessEvent_StaticLongField() {
        runFieldAccessEventTest(JDWPConstants.Tag.LONG_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on a double static field.
     */
    public void testFieldAccessEvent_StaticDoubleField() {
        runFieldAccessEventTest(JDWPConstants.Tag.DOUBLE_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event on an Object static field.
     */
    public void testFieldAccessEvent_StaticObjectField() {
        runFieldAccessEventTest(JDWPConstants.Tag.OBJECT_TAG, true);
    }

    /**
     * Tests FIELD_ACCESS event.
     *
     * <p>It runs {@link FieldDebuggee} and set a FIELD_ACCESS event on a tested field
     * (described by the given arguments).</p>
     *
     * <p>When the event occurs, it check:
     * <ul>
     * <li>this is the expected FIELD_ACCESS event</li>
     * <li>the event thread is properly suspended</li>
     * <li>for static field, the receiver is null</li>
     * <li>for instance field, the receiver is non-null and is of the debuggee type.</li>
     * </ul>
     * </p>
     *
     * @param fieldTag
     *          the type of the tested field. This is used to compute the tested field's name.
     * @param isStatic
     *          true if the tested thread is static, false otherwise.
     */
    private void runFieldAccessEventTest(byte fieldTag, boolean isStatic) {
        logWriter.println(getName() + " started");

        //check capability, relevant for this test
        logWriter.println("=> Check capability: canWatchFieldAccess");
        debuggeeWrapper.vmMirror.capabilities();
        boolean isCapability = debuggeeWrapper.vmMirror.targetVMCapabilities.canWatchFieldAccess;
        if (!isCapability) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canWatchFieldAccess");
            return;
        }

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Request FIELD_ACCESS event.
        String classSignature = getDebuggeeClassSignature();
        String fieldName = getTestFieldName(fieldTag, isStatic);
        int requestID = requestFieldAccessEvent(classSignature, fieldName);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        EventPacket event = debuggeeWrapper.vmMirror.receiveEvent();
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);

        // assert that event is the expected one
        assertEquals("Invalid number of events,", 1, parsedEvents.length);
        assertEquals("Invalid event kind,",
                JDWPConstants.EventKind.FIELD_ACCESS,
                parsedEvents[0].getEventKind(),
                JDWPConstants.EventKind.getName(JDWPConstants.EventKind.FIELD_ACCESS),
                JDWPConstants.EventKind.getName(parsedEvents[0].getEventKind()));
        assertEquals("Invalid request id,", requestID, parsedEvents[0].getRequestID());

        ParsedEvent.Event_FIELD_ACCESS fieldAccessEvent =
                (ParsedEvent.Event_FIELD_ACCESS)parsedEvents[0];

        // Check thread's state.
        long eventThreadID = fieldAccessEvent.getThreadID();
        checkThreadState(eventThreadID, JDWPConstants.ThreadStatus.RUNNING,
                JDWPConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED);

        checkObjectId(fieldAccessEvent.getObject(), isStatic);

        logWriter.println(getName() + " done");
    }

    /**
     * Requests FIELD_ACCESS event for the given field.
     *
     * @param debuggeeClassSignature
     *          the signature of the debuggee class
     * @param fieldName
     *          the name of the tested field
     * @return a request ID
     */
    private int requestFieldAccessEvent(String debuggeeClassSignature, String fieldName) {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setFieldAccess(debuggeeClassSignature,
                JDWPConstants.TypeTag.CLASS, fieldName);
        checkReplyPacket(reply, "Set FIELD_ACCESS event");
        return reply.getNextValueAsInt();
    }
}

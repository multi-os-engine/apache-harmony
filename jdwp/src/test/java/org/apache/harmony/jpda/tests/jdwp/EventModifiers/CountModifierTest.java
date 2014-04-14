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

package org.apache.harmony.jpda.tests.jdwp.EventModifiers;

import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.framework.jdwp.exceptions.ReplyErrorCodeException;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for count event modifier.
 */
public class CountModifierTest extends JDWPSyncTestCase {

    private static final String DEBUGGEE_SIGNATURE = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/CountModifierDebuggee;";
    private static final String TEST_CLASS_SIGNATURE = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/CountModifierDebuggee$TestClass;";
    private static final String TEST_CLASS_NAME = "org.apache.harmony.jpda.tests.jdwp.EventModifiers.CountModifierDebuggee$TestClass";

    // The name of the test method where we set our event requests.
    private static final String METHOD_NAME = "eventTestMethod";

    // The name of the test method where we set our event requests.
    private static final String WATCHED_FIELD_NAME = "watchedField";

    // The number of times we call the test method so we only receive event for
    // the last call.
    private static final int EVENT_COUNT = 3;

    @Override
    protected String getDebuggeeClassName() {
        return CountModifierDebuggee.class.getName();
    }

    public void testBreakpoint() {
        logWriter.println("testBreakpoint started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Breakpoint at start of test method.
        Breakpoint breakpoint = createBreakpoint();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableBreakpoint(
                JDWPConstants.TypeTag.CLASS, breakpoint,
                JDWPConstants.SuspendPolicy.ALL, EVENT_COUNT);
        checkReplyPacket(reply, "Failed to install breakpoint with event modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        // Execute the breakpoint.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint to hit.
        waitForEvent(JDWPConstants.EventKind.BREAKPOINT, requestID);

        // Check we executed the test method as many times as expected.
        int count = getLocationEventCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testBreakpoint done");
    }

    public void testMethodEntry() {
        logWriter.println("testMethodEntry started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Breakpoint at start of test method.
        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableMethodEntry(TEST_CLASS_NAME, EVENT_COUNT);
        checkReplyPacket(reply, "Failed to set METHOD_ENTRY with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for method entry to hit.
        waitForEvent(JDWPConstants.EventKind.METHOD_ENTRY, requestID);

        // Check we executed the test method as many times as expected.
        int count = getLocationEventCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_ENTRY, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testMethodEntry done");
    }

    public void testMethodExit() {
        logWriter.println("testMethodExit started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableMethodExit(TEST_CLASS_NAME, EVENT_COUNT);
        checkReplyPacket(reply, "Failed to set METHOD_ENTRY with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for method exit to hit.
        waitForEvent(JDWPConstants.EventKind.METHOD_EXIT, requestID);

        // Check we executed the test method as many times as expected.
        int count = getLocationEventCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_EXIT, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testMethodExit done");
    }

    public void testException() {
        logWriter.println("testException started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        final String exceptionClassSignature = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/CountModifierDebuggee$TestException;";
        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableException(exceptionClassSignature, true, false, EVENT_COUNT);
        checkReplyPacket(reply, "Failed to set EXCEPTION with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for EXCEPTION to hit.
        waitForEvent(JDWPConstants.EventKind.EXCEPTION, requestID);

        // Check we executed the test method as many times as expected.
        int count = getExceptionEventCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.EXCEPTION, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testException done");
    }

    public void testThreadStart() {
        logWriter.println("testThreadStart started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableThreadStart(EVENT_COUNT);
        checkReplyPacket(reply, "Failed to set THREAD_START with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for THREAD_START to hit.
        waitForEvent(JDWPConstants.EventKind.THREAD_START, requestID);

        // Check we executed the test method as many times as expected.
        int count = getThreadRunCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.THREAD_START, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testThreadStart done");
    }

    public void testThreadEnd() {
        logWriter.println("testThreadEnd started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableThreadEnd(EVENT_COUNT);
        checkReplyPacket(reply, "Failed to set THREAD_END with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for THREAD_END to hit.
        waitForEvent(JDWPConstants.EventKind.THREAD_END, requestID);

        // Check we executed the test method as many times as expected.
        int count = getThreadRunCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.THREAD_END, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testThreadEnd done");
    }

    public void testFieldAccess() {
        logWriter.println("testFieldAccess started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Check canWatchFieldAccess capability.
        logWriter.println("=> Check capability: canWatchFieldAccess");
        if (!debuggeeWrapper.vmMirror.canWatchFieldAccess()) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canWatchFieldAccess");
            synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
            return;
        }

        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableFieldAccess(
                DEBUGGEE_SIGNATURE, JDWPConstants.TypeTag.CLASS,
                WATCHED_FIELD_NAME, EVENT_COUNT);
        checkReplyPacket(reply, "Failed to set FIELD_ACCESS with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for FIELD_ACCESS to hit.
        waitForEvent(JDWPConstants.EventKind.FIELD_ACCESS, requestID);

        // Check we executed the test method as many times as expected.
        int count = getFieldReadWriteCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.FIELD_ACCESS, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testFieldAccess done");
    }

    public void testFieldModification() {
        logWriter.println("testFieldModification started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Check canWatchFieldModification capability.
        logWriter.println("=> Check capability: canWatchFieldModification");
        if (!debuggeeWrapper.vmMirror.canWatchFieldModification()) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canWatchFieldModification");
            synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
            return;
        }

        ReplyPacket reply = debuggeeWrapper.vmMirror.setCountableFieldModification(
                DEBUGGEE_SIGNATURE, JDWPConstants.TypeTag.CLASS,
                WATCHED_FIELD_NAME, EVENT_COUNT);
        checkReplyPacket(reply, "Failed to set FIELD_MODIFICATION with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for FIELD_MODIFICATION to hit.
        waitForEvent(JDWPConstants.EventKind.FIELD_MODIFICATION, requestID);

        // Check we executed the test method as many times as expected.
        int count = getFieldReadWriteCount();
        assertEquals("Invalid event count", EVENT_COUNT, count);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.FIELD_MODIFICATION, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testFieldModification done");
    }

    private void waitForEvent(byte eventKind, int requestID) {
        String eventString = JDWPConstants.EventKind.getName(eventKind);
        logWriter.println("Waiting for " + eventString + " event " + requestID + " ...");
        EventPacket eventPacket = debuggeeWrapper.vmMirror.receiveCertainEvent(eventKind);
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(eventPacket);
        assertNotNull(parsedEvents);
        assertTrue(parsedEvents.length > 0);
        assertEquals(eventKind, parsedEvents[0].getEventKind());
        assertEquals(requestID, parsedEvents[0].getRequestID());
        logWriter.println("Received " + eventString + " event");
    }

    private static Breakpoint createBreakpoint() {
        return new Breakpoint(TEST_CLASS_SIGNATURE, METHOD_NAME, 0);
    }

    private int getLocationEventCount() {
        return getStaticIntField(DEBUGGEE_SIGNATURE, "locationEventCount");
    }

    private int getExceptionEventCount() {
        return getStaticIntField(DEBUGGEE_SIGNATURE, "exceptionEventCount");
    }

    private int getThreadRunCount() {
        return getStaticIntField(DEBUGGEE_SIGNATURE, "threadRunCount");
    }

    private int getFieldReadWriteCount() {
        return getStaticIntField(DEBUGGEE_SIGNATURE, "fieldReadWriteCount");
    }

    private int getStaticIntField(String classSignature, String fieldName) {

        long classID = debuggeeWrapper.vmMirror.getClassID(classSignature);
        assertTrue("Failed to find debuggee class " + classSignature,
                classID != 0);
        long fieldID = debuggeeWrapper.vmMirror.getFieldID(classID, fieldName);
        assertTrue("Failed to find field " + classSignature + "." + fieldName,
                fieldID != 0);
        long[] fieldIDs = new long[] { fieldID };
        Value[] fieldValues = debuggeeWrapper.vmMirror.getReferenceTypeValues(classID, fieldIDs);
        assertNotNull("Failed to get field values for class " + classSignature, fieldValues);
        assertEquals("Invalid number of field values", fieldIDs.length, fieldValues.length);
        assertEquals("Invalid field value tag", JDWPConstants.Tag.INT_TAG, fieldValues[0].getTag());
        return fieldValues[0].getIntValue();
    }

}

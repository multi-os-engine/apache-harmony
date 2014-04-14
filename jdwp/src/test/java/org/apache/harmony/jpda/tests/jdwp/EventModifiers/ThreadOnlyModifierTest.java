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
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.EventThread;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.framework.jdwp.exceptions.ReplyErrorCodeException;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPTestConstants;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for thread event modifier.
 */
public class ThreadOnlyModifierTest extends JDWPSyncTestCase {

    private static final String DEBUGGEE_SIGNATURE = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/ThreadOnlyModifierDebuggee;";
    private static final String TEST_CLASS_SIGNATURE = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/ThreadOnlyModifierDebuggee$TestClass;";
    private static final String TEST_CLASS_NAME = "org.apache.harmony.jpda.tests.jdwp.EventModifiers.ThreadOnlyModifierDebuggee$TestClass";

    // The name of the test method where we set our event requests.
    private static final String METHOD_NAME = "eventTestMethod";

    // The name of the test method where we set our event requests.
    private static final String WATCHED_FIELD_NAME = "watchedField";

    private static final String THREAD_FIELD_NAME = "THREAD_TWO";

    @Override
    protected String getDebuggeeClassName() {
        return ThreadOnlyModifierDebuggee.class.getName();
    }

    private long getObjectField(String className, String fieldName, byte typeTag) {
        long classID = debuggeeWrapper.vmMirror.getClassID(className);
        assertTrue("Failed to find debuggee class " + className,
                classID != 0);
        long fieldID = debuggeeWrapper.vmMirror.getFieldID(classID, fieldName);
        assertTrue("Failed to find field " + className + "." + fieldName,
                fieldID != 0);

        long[] fieldIDs = new long[] { fieldID };
        Value[] fieldValues = debuggeeWrapper.vmMirror.getReferenceTypeValues(classID, fieldIDs);
        assertNotNull("Failed to get field values for class " + className, fieldValues);
        assertEquals("Invalid number of field values", fieldIDs.length, fieldValues.length);
        assertEquals("Invalid field value tag", typeTag, fieldValues[0].getTag());
        return fieldValues[0].getLongValue();
    }

    private long getFilteredThreadId() {
        return getObjectField(DEBUGGEE_SIGNATURE, THREAD_FIELD_NAME,
                JDWPConstants.Tag.THREAD_TAG);
    }

    public void testBreakpoint() {
        logWriter.println("testBreakpoint started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Breakpoint at start of test method.
        long threadID = getFilteredThreadId();
        Breakpoint breakpoint = createBreakpoint();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyBreakpoint(
                JDWPConstants.TypeTag.CLASS, breakpoint,
                JDWPConstants.SuspendPolicy.ALL, threadID);
        checkReplyPacket(reply, "Failed to install breakpoint with thread only modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        // Execute the breakpoint.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint to hit.
        waitForThreadEvent(JDWPConstants.EventKind.BREAKPOINT, requestID, threadID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testBreakpoint done");
    }

    public void testMethodEntry() {
        logWriter.println("testMethodEntry started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Breakpoint at start of test method.
        long threadID = getFilteredThreadId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyMethodEntry(TEST_CLASS_NAME, threadID);
        checkReplyPacket(reply, "Failed to set METHOD_ENTRY with thread only modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for method entry to hit.
        waitForThreadEvent(JDWPConstants.EventKind.METHOD_ENTRY, requestID, threadID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_ENTRY, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testMethodEntry done");
    }

    public void testMethodExit() {
        logWriter.println("testMethodExit started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long threadID = getFilteredThreadId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyMethodExit(TEST_CLASS_NAME, threadID);
        checkReplyPacket(reply, "Failed to set METHOD_ENTRY with thread only modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for method exit to hit.
        waitForThreadEvent(JDWPConstants.EventKind.METHOD_EXIT, requestID, threadID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_EXIT, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testMethodExit done");
    }

    public void testException() {
        logWriter.println("testException started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long threadID = getFilteredThreadId();
        final String exceptionClassSignature = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/ThreadOnlyModifierDebuggee$TestException;";
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyException(exceptionClassSignature, true, false, threadID);
        checkReplyPacket(reply, "Failed to set EXCEPTION with thread only modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for EXCEPTION to hit.
        waitForThreadEvent(JDWPConstants.EventKind.EXCEPTION, requestID, threadID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.EXCEPTION, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testException done");
    }

    public void testThreadStart() {
        logWriter.println("testThreadStart started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long threadID = getFilteredThreadId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyThreadStart(threadID);
        checkReplyPacket(reply, "Failed to set THREAD_START with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for THREAD_START to hit.
        waitForThreadEvent(JDWPConstants.EventKind.THREAD_START, requestID, threadID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.THREAD_START, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testThreadStart done");
    }

    public void testThreadEnd() {
        logWriter.println("testThreadEnd started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long threadID = getFilteredThreadId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyThreadEnd(threadID);
        checkReplyPacket(reply, "Failed to set THREAD_END with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for THREAD_END to hit.
        waitForThreadEvent(JDWPConstants.EventKind.THREAD_END, requestID, threadID);

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

        long threadID = getFilteredThreadId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyFieldAccess(
                DEBUGGEE_SIGNATURE, JDWPConstants.TypeTag.CLASS,
                WATCHED_FIELD_NAME, threadID);
        checkReplyPacket(reply, "Failed to set FIELD_ACCESS with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for FIELD_ACCESS to hit.
        waitForThreadEvent(JDWPConstants.EventKind.FIELD_ACCESS, requestID, threadID);

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

        long threadID = getFilteredThreadId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setThreadOnlyFieldModification(
                DEBUGGEE_SIGNATURE, JDWPConstants.TypeTag.CLASS,
                WATCHED_FIELD_NAME, threadID);
        checkReplyPacket(reply, "Failed to set FIELD_MODIFICATION with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for FIELD_MODIFICATION to hit.
        waitForThreadEvent(JDWPConstants.EventKind.FIELD_MODIFICATION, requestID, threadID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.FIELD_MODIFICATION, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testFieldModification done");
    }

    private void waitForThreadEvent(byte eventKind, int requestID, long threadID) {
        String eventString = JDWPConstants.EventKind.getName(eventKind);
        logWriter.println("Waiting for " + eventString + " event " + requestID + " in thread " + threadID + " ...");
        EventPacket eventPacket = debuggeeWrapper.vmMirror.receiveCertainEvent(eventKind);
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(eventPacket);
        assertNotNull(parsedEvents);
        assertTrue(parsedEvents.length > 0);
        EventThread eventThread = (EventThread) parsedEvents[0];
        assertEquals(eventKind, eventThread.getEventKind());
        assertEquals(requestID, eventThread.getRequestID());
        assertEquals(threadID, eventThread.getThreadID());
        logWriter.println("Received " + eventString + " event");
    }

    private static Breakpoint createBreakpoint() {
        return new Breakpoint(TEST_CLASS_SIGNATURE, METHOD_NAME, 0);
    }
}

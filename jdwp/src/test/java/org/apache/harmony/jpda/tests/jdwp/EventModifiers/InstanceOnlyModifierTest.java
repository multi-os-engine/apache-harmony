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
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.EventThreadLocation;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.framework.jdwp.exceptions.ReplyErrorCodeException;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPTestConstants;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for instance event modifier.
 */
public class InstanceOnlyModifierTest extends JDWPSyncTestCase {

    private static final String DEBUGGEE_SIGNATURE = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/InstanceOnlyModifierDebuggee;";
    private static final String TEST_CLASS_SIGNATURE = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/InstanceOnlyModifierDebuggee$TestClass;";
    private static final String TEST_CLASS_NAME = "org.apache.harmony.jpda.tests.jdwp.EventModifiers.InstanceOnlyModifierDebuggee$TestClass";

    // The name of the test method where we set our event requests.
    private static final String METHOD_NAME = "eventTestMethod";

    // The name of the test method where we set our event requests.
    private static final String WATCHED_FIELD_NAME = "watchedField";

    private static final String INSTANCE_FIELD_NAME = "INSTANCE_ONLY";

    @Override
    protected String getDebuggeeClassName() {
        return InstanceOnlyModifierDebuggee.class.getName();
    }

    private long getFilteredInstanceObjectId() {
        long classID = debuggeeWrapper.vmMirror.getClassID(DEBUGGEE_SIGNATURE);
        assertTrue("Failed to find debuggee class " + DEBUGGEE_SIGNATURE,
                classID != 0);
        long fieldID = debuggeeWrapper.vmMirror.getFieldID(classID, INSTANCE_FIELD_NAME);
        assertTrue("Failed to find field " + DEBUGGEE_SIGNATURE + "." + INSTANCE_FIELD_NAME,
                fieldID != 0);

        long[] fieldIDs = new long[] { fieldID };
        Value[] fieldValues = debuggeeWrapper.vmMirror.getReferenceTypeValues(classID, fieldIDs);
        assertNotNull("Failed to get field values for class " + DEBUGGEE_SIGNATURE, fieldValues);
        assertEquals("Invalid number of field values", fieldIDs.length, fieldValues.length);
        assertEquals("Invalid field value tag", JDWPConstants.Tag.OBJECT_TAG, fieldValues[0].getTag());
        return fieldValues[0].getLongValue();
    }

    public void testBreakpoint() {
        logWriter.println("testBreakpoint started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        if (!debuggeeWrapper.vmMirror.canUseInstanceFilters()) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canUseInstanceFilters");
            synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
            return;
        }

        // Breakpoint at start of test method.
        long objectID = getFilteredInstanceObjectId();
        Breakpoint breakpoint = createBreakpoint();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setInstanceOnlyBreakpoint(
                JDWPConstants.TypeTag.CLASS, breakpoint,
                JDWPConstants.SuspendPolicy.ALL, objectID);
        checkReplyPacket(reply, "Failed to install breakpoint with thread only modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        // Execute the breakpoint.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint to hit.
        waitForEvent(JDWPConstants.EventKind.BREAKPOINT, requestID, objectID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testBreakpoint done");
    }

    public void testMethodEntry() {
        logWriter.println("testMethodEntry started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        if (!debuggeeWrapper.vmMirror.canUseInstanceFilters()) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canUseInstanceFilters");
            synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
            return;
        }

        // Breakpoint at start of test method.
        long objectID = getFilteredInstanceObjectId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setInstanceOnlyMethodEntry(TEST_CLASS_NAME, objectID);
        checkReplyPacket(reply, "Failed to set METHOD_ENTRY with thread only modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for method entry to hit.
        waitForEvent(JDWPConstants.EventKind.METHOD_ENTRY, requestID, objectID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_ENTRY, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testMethodEntry done");
    }

    public void testMethodExit() {
        logWriter.println("testMethodExit started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        if (!debuggeeWrapper.vmMirror.canUseInstanceFilters()) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canUseInstanceFilters");
            synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
            return;
        }

        long objectID = getFilteredInstanceObjectId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setInstanceOnlyMethodExit(TEST_CLASS_NAME, objectID);
        checkReplyPacket(reply, "Failed to set METHOD_ENTRY with thread only modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for method exit to hit.
        waitForEvent(JDWPConstants.EventKind.METHOD_EXIT, requestID, objectID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_EXIT, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testMethodExit done");
    }
    /*
    public void testException() {
        logWriter.println("testException started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long threadID = getFilteredInstanceObjectId();
        final String exceptionClassSignature = "Lorg/apache/harmony/jpda/tests/jdwp/EventModifiers/InstanceOnlyModifierDebuggee$TestException;";
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

        long threadID = getFilteredInstanceObjectId();
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

        long threadID = getFilteredInstanceObjectId();
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
*/
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

        long objectID = getFilteredInstanceObjectId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setInstanceOnlyFieldAccess(
                TEST_CLASS_SIGNATURE, JDWPConstants.TypeTag.CLASS,
                WATCHED_FIELD_NAME, objectID);
        checkReplyPacket(reply, "Failed to set FIELD_ACCESS with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for FIELD_ACCESS to hit.
        waitForEvent(JDWPConstants.EventKind.FIELD_ACCESS, requestID, objectID);

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

        long objectID = getFilteredInstanceObjectId();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setInstanceOnlyFieldModification(
                TEST_CLASS_SIGNATURE, JDWPConstants.TypeTag.CLASS,
                WATCHED_FIELD_NAME, objectID);
        checkReplyPacket(reply, "Failed to set FIELD_MODIFICATION with count modifier");
        int requestID = reply.getNextValueAsInt();
        assertAllDataRead(reply);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for FIELD_MODIFICATION to hit.
        waitForEvent(JDWPConstants.EventKind.FIELD_MODIFICATION, requestID, objectID);

        reply = debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.FIELD_MODIFICATION, requestID);
        checkReplyPacket(reply, "Failed to clear event " + requestID);

        logWriter.println("testFieldModification done");
    }

    private void waitForEvent(byte eventKind, int requestID, long objectID) {
        String eventString = JDWPConstants.EventKind.getName(eventKind);
        logWriter.println("Waiting for " + eventString + " event " + requestID + " with object " + objectID + " ...");
        EventPacket eventPacket = debuggeeWrapper.vmMirror.receiveCertainEvent(eventKind);
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(eventPacket);
        assertNotNull(parsedEvents);
        assertTrue(parsedEvents.length > 0);
        EventThreadLocation eventThread = (EventThreadLocation) parsedEvents[0];
        assertEquals(eventKind, eventThread.getEventKind());
        assertEquals(requestID, eventThread.getRequestID());

        long threadID = eventThread.getThreadID();
        assertTrue(threadID != 0);

        Location location = eventThread.getLocation();

        long frameID = -1;
        int framesCount = debuggeeWrapper.vmMirror.getFrameCount(threadID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.getThreadFrames(threadID, 0, framesCount);
        checkReplyPacket(reply, "Failed to get frames for thread " + threadID);
        int frames = reply.getNextValueAsInt();
        for (int i = 0; i < frames; ++i) {
            long currentFrameID = reply.getNextValueAsLong();
            Location currentFrameLocation = reply.getNextValueAsLocation();
            if (currentFrameLocation.equals(location)) {
                frameID = currentFrameID;
                break;
            }
        }
        assertTrue("Failed to find frame for event location", frameID != -1);

        long thisObjectID = debuggeeWrapper.vmMirror.getThisObject(threadID, frameID);
        assertEquals("Event is not related to the object we're looking for",
                objectID, thisObjectID);

        logWriter.println("Received " + eventString + " event");
    }

    private static Breakpoint createBreakpoint() {
        return new Breakpoint(TEST_CLASS_SIGNATURE, METHOD_NAME, 0);
    }
}

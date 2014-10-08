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

import org.apache.harmony.jpda.tests.framework.jdwp.Event;
import org.apache.harmony.jpda.tests.framework.jdwp.EventBuilder;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Base class for FieldAccessLocationTest and FieldModificationLocationTest
 * unit tests.
 */
abstract class FieldLocationEventTestCase extends JDWPEventTestCase {

    private static final String DEBUGGEE_SIGNATURE =
            "Lorg/apache/harmony/jpda/tests/jdwp/Events/FieldLocationDebuggee;";
    private static final String METHOD_TWO = "expectedMethodForFieldEvent";
    private static final String FIELD_NAME = "testIntField";

    private Set<Integer> requestIds = new HashSet<Integer>();

    protected String getDebuggeeClassName() {
        return FieldLocationDebuggee.class.getName();
    }

    protected void testFieldEvent(byte fieldEventKind) {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long debuggeeClassId = getClassIDBySignature(DEBUGGEE_SIGNATURE);
        long fieldId = debuggeeWrapper.vmMirror.getFieldID(debuggeeClassId, FIELD_NAME);
        long methodTwoId = getMethodID(debuggeeClassId, METHOD_TWO);

        // Request field access/modification events for all possible locations
        // in the expected method.
        requestFieldEventWithLocation(fieldEventKind,
                debuggeeClassId, fieldId, methodTwoId);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for the field event.
        EventPacket event = debuggeeWrapper.vmMirror.receiveEvent();
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);

        // We expect only one event.
        assertEquals("Invalid number of events,", 1, parsedEvents.length);
        ParsedEvent parsedEvent = parsedEvents[0];

        // Check this is the event we expect.
        assertEquals("Invalid event kind,",
                fieldEventKind,
                parsedEvent.getEventKind(),
                JDWPConstants.EventKind.getName(fieldEventKind),
                JDWPConstants.EventKind.getName(parsedEvent.getEventKind()));

        // Check this is one event we requested.
        int eventRequestId = parsedEvent.getRequestID();
        assertTrue("Unexpected event request " + eventRequestId,
                requestIds.contains(Integer.valueOf(eventRequestId)));

        TaggedObject accessedField = null;
        if (fieldEventKind == JDWPConstants.EventKind.FIELD_ACCESS) {
            accessedField = ((ParsedEvent.Event_FIELD_ACCESS)parsedEvents[0]).getObject();
        } else if (fieldEventKind == JDWPConstants.EventKind.FIELD_MODIFICATION) {
            accessedField = ((ParsedEvent.Event_FIELD_MODIFICATION)parsedEvents[0]).getObject();
        }

        // TODO check we're in the expected method

        // Check the field receiver is an instance of our debuggee class.
        long typeID = getObjectReferenceType(accessedField.objectID);
        String returnedExceptionSignature = getClassSignature(typeID);
        assertString("Invalid class signature,",
                DEBUGGEE_SIGNATURE, returnedExceptionSignature);

        // Clear all event requests before leaving.
        clearAllEvents(fieldEventKind);

        // Resume debuggee before leaving.
        resumeDebuggee();
    }

    /**
     * Requests field access/modification events.
     *
     * Since we don't know the location where the field event can be reported,
     * we send a request for all possible locations inside the method.
     */
    private void requestFieldEventWithLocation(byte fieldEventKind,
            long typeId, long fieldId, long methodId) {
        ReplyPacket replyPacket = getLineTable(typeId, methodId);
        long startIndex = replyPacket.getNextValueAsLong();
        long endIndex = replyPacket.getNextValueAsLong();
        logWriter.println("Method code index starts at " + startIndex + " and ends at " + endIndex);

        logWriter.println("Creating request for each possible index");
        for (long idx = startIndex; idx <= endIndex; ++idx) {
            Location location = new Location(JDWPConstants.TypeTag.CLASS,
                    typeId, methodId, idx);
            ReplyPacket eventPacket = requestFieldEventWithLocation(fieldEventKind, typeId,
                    fieldId, location);
            int requestId = eventPacket.getNextValueAsInt();
            logWriter.println("=> New request " + requestId);
            requestIds.add(Integer.valueOf(requestId));
        }
        logWriter.println("Created " + requestIds.size() + " requests");
    }

    private ReplyPacket requestFieldEventWithLocation(byte fieldEventKind,
            long typeId, long fieldId, Location location) {
        EventBuilder builder = new EventBuilder(fieldEventKind,
                JDWPConstants.SuspendPolicy.ALL);
        builder.setFieldOnly(typeId, fieldId);  // FieldOnly
        builder.setLocationOnly(location);  // LocationOnly
        Event event = builder.build();
        ReplyPacket reply = debuggeeWrapper.vmMirror.setEvent(event);
        String eventName = JDWPConstants.EventKind.getName(fieldEventKind);
        checkReplyPacket(reply, "EventRequest.Set " + eventName);
        return reply;
    }

    private void clearAllEvents(byte fieldEventKind) {
        logWriter.println("Clear all field requests");
        for (Integer requestId : requestIds) {
            clearEvent(fieldEventKind, requestId.intValue());
        }
        requestIds.clear();
    }

    private void clearEvent(byte fieldEventKind, int requestId) {
        logWriter.println("=> Clear request " + requestId);
        debuggeeWrapper.vmMirror.clearEvent(fieldEventKind, requestId);
    }
}

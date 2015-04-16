/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * @author Anatoly F. Bondarenko
 */

/**
 * Created on 26.10.2006
 */
package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.EventMod;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.EventThreadLocation;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.util.ArrayList;
import java.util.List;

/**
 * JDWP Unit test for possible combined (co-located) events: METHOD_ENTRY,
 * SINGLE_STEP, BREAKPOINT, METHOD_EXIT.
 */
public class CombinedEvents004Test extends CombinedEventsTestCase {

    private String methodForEvents = "emptyMethod";

    private boolean eventVmDeathReceived = false;

    private long threadID;

    private long testedMethodID;

    private long debuggeeClassID;

    private Location combinedEventsLocation;

    @Override
    protected String getDebuggeeClassName() {
        return CombinedEvents004Debuggee.class.getName();
    }

    /**
     * This test case checks events: METHOD_ENTRY, SINGLE_STEP, BREAKPOINT,
     * METHOD_EXIT for empty method.
     */
    public void testCombinedEvents004_01() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.EventKind.BREAKPOINT,
                JDWPConstants.EventKind.SINGLE_STEP,
                JDWPConstants.EventKind.METHOD_EXIT 
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_02() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.EventKind.BREAKPOINT
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_03() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.EventKind.SINGLE_STEP
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_04() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.EventKind.SINGLE_STEP,
                JDWPConstants.EventKind.BREAKPOINT
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_05() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.EventKind.METHOD_EXIT
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_06() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.EventKind.METHOD_EXIT,
                JDWPConstants.EventKind.BREAKPOINT
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_07() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_ENTRY,
                JDWPConstants.EventKind.METHOD_EXIT,
                JDWPConstants.EventKind.SINGLE_STEP
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_08() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_EXIT, 
                JDWPConstants.EventKind.BREAKPOINT
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    
    public void testCombinedEvents004_09() {
        byte[] EXPECTED_EVENTS_ARRAY = { 
                JDWPConstants.EventKind.METHOD_EXIT, 
                JDWPConstants.EventKind.SINGLE_STEP
        };

        runTest(EXPECTED_EVENTS_ARRAY);
    }
    

    static class RequestedEvent {
        public RequestedEvent(byte eventKind, int requestID) {
            this.eventKind = eventKind;
            this.requestID = requestID;
        }
        
        @Override
        public String toString() {
            return JDWPConstants.EventKind.getName(eventKind) + " (request " + requestID + ")";
        }

        final byte eventKind;
        final int requestID;
    }

    private void initJdwpIds(String debuggeeMainThreadName) {
        debuggeeClassID = debuggeeWrapper.vmMirror.getClassID(
                getDebuggeeClassSignature());
        logWriter.println("=> debuggeeClassID = " + debuggeeClassID);

        threadID = debuggeeWrapper.vmMirror.getThreadID(debuggeeMainThreadName);
        logWriter.println("=> threadID = " + threadID);

        logWriter.println("");
        logWriter.println(
                "=> Info for tested method '" + methodForEvents + "':");
        testedMethodID = debuggeeWrapper.vmMirror.getMethodID(debuggeeClassID,
                methodForEvents);
        if (testedMethodID == -1) {
            String failureMessage =
                    "## FAILURE: Can NOT get MethodID for class '"
                    + getDebuggeeClassName() + "'; Method name = "
                    + methodForEvents;
            printErrorAndFail(failureMessage);
        }
        logWriter.println("=> testedMethodID = " + testedMethodID);
        printMethodLineTable(debuggeeClassID, null, methodForEvents);

        combinedEventsLocation = getMethodEntryLocation(debuggeeClassID,
                methodForEvents);
        if (combinedEventsLocation == null) {
            String failureMessage =
                    "## FAILURE: Can NOT get MethodEntryLocation for method '"
                    + methodForEvents + "'";
            printErrorAndFail(failureMessage);
        }

    }

    private RequestedEvent requestEvent(byte eventKind) {
        int requestID = -1;
        // TODO support METHOD_EXIT_WITH_RETURN_VALUE
        switch (eventKind) {
        case JDWPConstants.EventKind.METHOD_ENTRY: {
            ReplyPacket reply = debuggeeWrapper.vmMirror.setMethodEntry(
                    getDebuggeeClassName());
            checkReplyPacket(reply, "Set METHOD_ENTRY event");
            requestID = reply.getNextValueAsInt();
            assertAllDataRead(reply);
            break;
        }
        case JDWPConstants.EventKind.METHOD_EXIT: {
            ReplyPacket reply = debuggeeWrapper.vmMirror.setMethodExit(
                    getDebuggeeClassName());
            checkReplyPacket(reply, "Set METHOD_EXIT event");
            requestID = reply.getNextValueAsInt();
            break;
        }
        case JDWPConstants.EventKind.BREAKPOINT: {
            ReplyPacket reply = debuggeeWrapper.vmMirror.setBreakpoint(
                    combinedEventsLocation);
            requestID = reply.getNextValueAsInt();
            assertAllDataRead(reply);
            break;
        }
        case JDWPConstants.EventKind.SINGLE_STEP: {
            CommandPacket setRequestCommand = new CommandPacket(
                    JDWPCommands.EventRequestCommandSet.CommandSetID,
                    JDWPCommands.EventRequestCommandSet.SetCommand);
            setRequestCommand.setNextValueAsByte(
                    JDWPConstants.EventKind.SINGLE_STEP);
            setRequestCommand.setNextValueAsByte(
                    JDWPConstants.SuspendPolicy.ALL);
            setRequestCommand.setNextValueAsInt(2);
            setRequestCommand.setNextValueAsByte(EventMod.ModKind.Step);
            setRequestCommand.setNextValueAsThreadID(threadID);
            setRequestCommand.setNextValueAsInt(JDWPConstants.StepSize.MIN);
            setRequestCommand.setNextValueAsInt(JDWPConstants.StepDepth.INTO);
            setRequestCommand.setNextValueAsByte(EventMod.ModKind.ClassOnly);
            setRequestCommand.setNextValueAsReferenceTypeID(debuggeeClassID);

            ReplyPacket setRequestReply = debuggeeWrapper.vmMirror
                    .performCommand(setRequestCommand);
            checkReplyPacket(setRequestReply, "EventRequest::Set command");
            requestID = setRequestReply.getNextValueAsInt();
            break;
        }
        default:
            throw new IllegalArgumentException("Unsupport event "
                    + JDWPConstants.EventKind.getName(eventKind));
        }
        return new RequestedEvent(eventKind, requestID);
    }

    private void runTest(byte[] EXPECTED_EVENTS_ARRAY) {
        logWriter.println("==> " + getName() + " started");
        
        {
            StringBuilder builder = new StringBuilder("Testing ");
            builder.append(JDWPConstants.EventKind.getName(EXPECTED_EVENTS_ARRAY[0]));
            for (int i = 1; i < EXPECTED_EVENTS_ARRAY.length; ++i) {
                builder.append(", ");
                builder.append(JDWPConstants.EventKind.getName(EXPECTED_EVENTS_ARRAY[i]));
            }
            logWriter.println(builder.toString());
        }
        
        // Wait for the debuggee to be ready.
        String debuggeeMainThreadName = synchronizer.receiveMessage();

        // Compute JDWP ids
        initJdwpIds(debuggeeMainThreadName);

        // Request events.
        List<RequestedEvent> requestedEvents = new ArrayList<
                CombinedEvents004Test.RequestedEvent>();
        for (byte eventKind : EXPECTED_EVENTS_ARRAY) {
            RequestedEvent requestedEvent = requestEvent(eventKind);
            requestedEvents.add(requestedEvent);
            logWriter.println("Created request " + requestedEvent);
        }
        logWriter.println("");

        // Continue debuggee
        logWriter.println("=> Send SGNL_CONTINUE signal to debuggee...");
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        logWriter.println("=> Try to receive and check combined events: "
                + " METHOD_ENTRY, SINGLE_STEP, BREAKPOINT, METHOD_EXIT events; ignore single SINGLE_STEP event");
        receiveAndCheckEvents2(requestedEvents);
//        receiveAndCheckEvents(EXPECTED_EVENTS_ARRAY, combinedEventsLocation);
        if (eventVmDeathReceived) {
            logWriter.println(
                    "==> testCombinedEvents001 is FINISHing as VM_DEATH is received!");
            return;
        }

        // Clear event requests
        for (RequestedEvent event : requestedEvents) {
            logWriter.println("Clear event "
                    + JDWPConstants.EventKind.getName(event.eventKind) + " "
                    + event.requestID);
            debuggeeWrapper.vmMirror.clearEvent(event.eventKind,
                    event.requestID);
        }

        logWriter.println("=> Resume debuggee");
        debuggeeWrapper.vmMirror.resume();

        // check that no other events, except VM_DEATH, will be received
        checkVMDeathEvent();

        logWriter.println("");
        logWriter.println("==> " + getName() + " PASSED");
    }
    
    void receiveAndCheckEvents2(List<RequestedEvent> requestedEvents) {
        List<ParsedEvent> receivedEvents = new ArrayList<ParsedEvent>();
        for (;;) {
            // Receive events
            logWriter.println("");
            logWriter.println("... receiving events ...");
            logWriter.println("");
            EventPacket event = debuggeeWrapper.vmMirror.receiveEvent();
            ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);

            // print all received events
            logWriter.println("=> Received event packet with " + parsedEvents.length + " event(s):");
            for (int i = 0; i < parsedEvents.length; i++) {
                ParsedEvent parsedEvent = parsedEvents[i];
                logWriter.println("   #" + (i + 1) + ": " +  
                        JDWPConstants.EventKind.getName(parsedEvent.getEventKind()) + 
                        " (request " + parsedEvent.getRequestID() + ")");
                if (parsedEvents[i].getEventKind() == JDWPConstants.EventKind.VM_DEATH) {
                    eventVmDeathReceived = true;
                }
            }
            logWriter.println("");
            if (eventVmDeathReceived) {
                return;
            }

            // the following code checks received events
            if (receivedEvents.isEmpty() && 
                    parsedEvents.length == 1 && 
                    parsedEvents[0].getEventKind() == JDWPConstants.EventKind.SINGLE_STEP) {
                logWriter.println("=> Ignoring single SINGLE_STEP: continue.");
                debuggeeWrapper.vmMirror.resume();
                continue;
            } else {
                for (ParsedEvent parsedEvent : parsedEvents) {
                    receivedEvents.add(parsedEvent);
                }
                if (receivedEvents.size() < requestedEvents.size()) {
                    // We did not receive all events yet
                    logWriter.println("=> Expecting " + requestedEvents.size() + 
                            " event(s) but only received " + receivedEvents.size() + ": continue");
                    debuggeeWrapper.vmMirror.resume();
                    continue;
                }
                logWriter.println("Received enough events: check them now.");
                boolean success = true;
                for (RequestedEvent requestedEvent : requestedEvents) {
                    int i = 0;
                    for (; i < receivedEvents.size(); ++i) {
                        ParsedEvent parsedEvent = receivedEvents.get(i);
                        if (parsedEvent.getEventKind() == requestedEvent.eventKind &&
                            parsedEvent.getRequestID() == requestedEvent.requestID) {
                            // This is an expected event: check location.
                            EventThreadLocation locationEvent = (EventThreadLocation) parsedEvent;
                            Location location = locationEvent.getLocation();
                            if (!combinedEventsLocation.equals(location)) {
                                logWriter.println("#FAILURE: bad location for " + 
                                        JDWPConstants.EventKind.getName(parsedEvent.getEventKind()) + 
                                        " (request " + parsedEvent.getRequestID() + ")");
                                success = false;
                            }
                            break;
                        }
                    }
                    if (i < receivedEvents.size()) {
                        // Remove the expected event from the list.
                        receivedEvents.remove(i);
                    } else {
                        logWriter.printError("## FAILURE: missed event " + requestedEvent);
                        success = false;
                    }
                }
                if (!receivedEvents.isEmpty()) {
                    // Events we did not expect to receive.
                    for (ParsedEvent parsedEvent : receivedEvents) {
                        logWriter.println("## FAILURE: received unexpected event " + 
                                JDWPConstants.EventKind.getName(parsedEvent.getEventKind()) + 
                                " " + parsedEvent.getRequestID());
                    }
                    success = false;
                }
                if (!success) {
                    // TODO better error message.
                    fail();
                } else {
                    logWriter.println("## SUCCESS: received all request events");
                }
                return;
            }
        }
    }

    void receiveAndCheckEvents(byte[] EXPECTED_EVENTS_ARRAY,
            Location expectedLocation) {
        int EXPECTED_EVENTS_COUNT = EXPECTED_EVENTS_ARRAY.length;
        for (;;) {
            logWriter.println("=>");
            logWriter.println("=> Receiving events...");
            EventPacket event = debuggeeWrapper.vmMirror.receiveEvent();
            ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);

            // print all received events
            logWriter.println("=> Received event packet with events number = "
                    + parsedEvents.length + " :");

            for (int i = 0; i < parsedEvents.length; i++) {
                logWriter.println("");
                logWriter.println("=> Event #" + (i + 1) + " in packet -");
                logWriter.println("=> EventKind: "
                        + parsedEvents[i].getEventKind()
                        + "[" + JDWPConstants.EventKind.getName(
                                parsedEvents[i].getEventKind()) + "]");
                logWriter.println(
                        "=> RequestID: " + parsedEvents[i].getRequestID());
                if (parsedEvents[i].getEventKind()
                        == JDWPConstants.EventKind.VM_DEATH) {
                    eventVmDeathReceived = true;
                }
            }
            if (eventVmDeathReceived) {
                return;
            }
            checkEventsLocation(parsedEvents, null); // DBG__

            // the following code checks received events
            if (parsedEvents.length == 1) {
                debuggeeWrapper.vmMirror.resume();
                continue;
                /*
                 * DBG__ if (parsedEvents[0].getEventKind() ==
                 * JDWPConstants.EventKind.SINGLE_STEP) { logWriter.println("");
                 * logWriter.println("=> Resume debuggee");
                 * logWriter.println(""); debuggeeWrapper.vmMirror.resume();
                 * continue; } else {
                 * logWriter.println("##FAILURE: received unexpected event: " +
                 * parsedEvents[0].getEventKind() + "[" +
                 * JDWPConstants.EventKind.getName(parsedEvents[0]
                 * .getEventKind()) + "] instead of SINGLE_STEP");
                 * fail("received event is not SINGLE_STEP event: " +
                 * parsedEvents[0].getEventKind() + "[" +
                 * JDWPConstants.EventKind.getName(parsedEvents[0]
                 * .getEventKind()) + "]"); } // DBG__
                 */
                // DBG__} else if (parsedEvents.length == EXPECTED_EVENTS_COUNT)
                // {
            } else if (parsedEvents.length > 1) {
                logWriter.println("");
                logWriter.println(
                        "=> Combined events are received. Check events..");
                boolean success = true;
                for (int i = 0; i < parsedEvents.length; i++) {
                    boolean isFound = false;
                    for (int j = 0; j < EXPECTED_EVENTS_COUNT; j++) {
                        if (parsedEvents[i].getEventKind()
                                == EXPECTED_EVENTS_ARRAY[j]) {
                            EXPECTED_EVENTS_ARRAY[j] = 0;
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound) {
                        logWriter.println(
                                "##FAILURE: received unexpected event: "
                                + parsedEvents[i].getEventKind()
                                + "[" + JDWPConstants.EventKind.getName(
                                        parsedEvents[0].getEventKind()) + "]");
                        success = false;
                    }
                }
                if (!success) {
                    logWriter.println("");
                    logWriter.println(
                            "##FAILURE: the following expected events were not received: ");
                    for (int k = 0; k < EXPECTED_EVENTS_COUNT; k++) {
                        if (EXPECTED_EVENTS_ARRAY[k] != 0)
                            logWriter.println("  #" + k + ": "
                                    + EXPECTED_EVENTS_ARRAY[k]
                                    + "[" + JDWPConstants.EventKind.getName(
                                            EXPECTED_EVENTS_ARRAY[k]) + "]");
                    }
                    fail("not all expected events were received");
                }
                for (int i = 0; i < parsedEvents.length; i++) {
                    byte eventKind = parsedEvents[i].getEventKind();
                    long eventThreadID = ((ParsedEvent.EventThread) parsedEvents[i])
                            .getThreadID();
                    logWriter.println("");
                    logWriter.println("=> Chcek location for combined event N "
                            + (i + 1) + ": Event kind = " + eventKind + "("
                            + JDWPConstants.EventKind.getName(eventKind)
                            + "); eventThreadID = " + eventThreadID);
                    Location eventLocation = null;
                    switch (eventKind) {
                    case JDWPConstants.EventKind.METHOD_ENTRY:
                        eventLocation = ((ParsedEvent.Event_METHOD_ENTRY) parsedEvents[i])
                                .getLocation();
                        break;
                    case JDWPConstants.EventKind.SINGLE_STEP:
                        eventLocation = ((ParsedEvent.Event_SINGLE_STEP) parsedEvents[i])
                                .getLocation();
                        break;
                    case JDWPConstants.EventKind.BREAKPOINT:
                        eventLocation = ((ParsedEvent.Event_BREAKPOINT) parsedEvents[i])
                                .getLocation();
                        break;
                    case JDWPConstants.EventKind.METHOD_EXIT:
                        eventLocation = ((ParsedEvent.Event_METHOD_EXIT) parsedEvents[i])
                                .getLocation();
                        break;
                    }
                    long eventClassID = eventLocation.classID;
                    logWriter.println("=> ClassID in event = " + eventClassID);
                    if (expectedLocation.classID != eventClassID) {
                        logWriter.println(
                                "## FAILURE: Unexpected ClassID in event!");
                        logWriter.println("##          Expected ClassID  = "
                                + expectedLocation.classID);
                        success = false;
                    } else {
                        logWriter.println("=> OK - it is expected ClassID");
                    }
                    long eventMethodID = eventLocation.methodID;
                    logWriter.println(
                            "=> MethodID in event = " + eventMethodID);
                    if (expectedLocation.methodID != eventMethodID) {
                        logWriter.println(
                                "## FAILURE: Unexpected MethodID in event!");
                        logWriter.println("##          Expected MethodID = "
                                + expectedLocation.methodID);
                        success = false;
                    } else {
                        logWriter.println("=> OK - it is expected MethodID");
                    }
                    long eventCodeIndex = eventLocation.index;
                    logWriter.println(
                            "=> CodeIndex in event = " + eventCodeIndex);
                    if (expectedLocation.index != eventCodeIndex) {
                        logWriter.println(
                                "## FAILURE: Unexpected CodeIndex in event!");
                        logWriter.println("##          Expected CodeIndex = "
                                + expectedLocation.index);
                        success = false;
                    } else {
                        logWriter.println("=> OK - it is expected CodeIndex)");
                    }
                }
                logWriter.println("");
                if (!success) {
                    String failureMessage = "## FAILURE: Unexpected events' locations are found out!";
                    logWriter.println(failureMessage);
                    // DBG__printErrorAndFail(failureMessage);
                } else {
                    logWriter.println(
                            "=> OK - all combined events have expected location!");
                }
                break;
            } else {
                logWriter.println(
                        "##FAILURE: received unexpected number of events: "
                        + parsedEvents.length + " instead of 1 or "
                        + EXPECTED_EVENTS_COUNT);
                fail("received unexpected number of events: "
                        + parsedEvents.length);
            }
        }
    }

    void checkVMDeathEvent() {
        if (eventVmDeathReceived) {
            return;
        }
        logWriter.println("=> Wait for VM_DEATH event...");
        while (true) { // DBG_
            logWriter.println("=> Receiving events...");
            EventPacket event = debuggeeWrapper.vmMirror.receiveEvent();
            ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);
            if (parsedEvents.length != 1 || parsedEvents[0].getEventKind()
                    != JDWPConstants.EventKind.VM_DEATH) {
                // print all received events
                logWriter.println("##FAILURE: Received unexpected events");
                logWriter.println("=> Events received: " + parsedEvents.length);
                for (int i = 0; i < parsedEvents.length; i++) {
                    logWriter.println("");
                    logWriter.println("=> Event #" + i + ";");
                    logWriter.println("=> EventKind: "
                            + parsedEvents[i].getEventKind()
                            + "[" + JDWPConstants.EventKind.getName(
                                    parsedEvents[i].getEventKind()) + "]");
                    logWriter.println(
                            "=> RequestID: " + parsedEvents[i].getRequestID());
                }
                checkEventsLocation(parsedEvents, null); // DBG__
                logWriter.println("=> Resume debuggee"); // DBG__
                debuggeeWrapper.vmMirror.resume(); // DBG__
                continue; // DBG__
                // DBG__fail("unexpected events received");
            }
            logWriter.println("=> OK - VM_DEATH event was received. ");
            break; // DBG__
        } // DBG__
    }
}

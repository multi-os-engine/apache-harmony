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

import java.io.IOException;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.EventMod;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP unit test for measuring single step time.
 */

public class AnnotationTest extends JDWPEventTestCase {

    private final long STEP_TIME_THRESHOLD = 2 * 60 * 1000;

    private String debuggeeSignature = "Lorg/apache/harmony/jpda/tests/jdwp/Events/AnnotationDebuggee;";

    private String DEBUGGEE_CLASS_NAME = "org.apache.harmony.jpda.tests.jdwp.Events.AnnotationDebuggee";

    protected String getDebuggeeClassName() {
        return DEBUGGEE_CLASS_NAME;
    }

    /**
     * This testcase measure how long a step-over takes on a certain heavy method call.
     */
    public void testStepOverTime() {
        logWriter.println("=> testStepOverTime started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        //find checked method
        long refTypeID = getClassIDBySignature(debuggeeSignature);

        logWriter.println("=> Debuggee class = " + getDebuggeeClassName());
        logWriter.println("=> referenceTypeID for Debuggee class = " + refTypeID);
        logWriter.println("=> Send ReferenceType::Methods command and get methodIDs ");

        long requestID = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(
                refTypeID, "doCall");
        logWriter.println("=> breakpointID = " + requestID);
        logWriter.println("=> starting thread");

        //execute the breakpoint
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        long breakpointThreadID = debuggeeWrapper.vmMirror
                .waitForBreakpoint(requestID);

        logWriter.println("=> breakpointThreadID = " + breakpointThreadID);

        // Sending a SINGLE_STEP request

        CommandPacket setRequestCommand = new CommandPacket(
                JDWPCommands.EventRequestCommandSet.CommandSetID,
                JDWPCommands.EventRequestCommandSet.SetCommand);

        setRequestCommand
                .setNextValueAsByte(JDWPConstants.EventKind.SINGLE_STEP);
        setRequestCommand.setNextValueAsByte(JDWPConstants.SuspendPolicy.ALL);
        setRequestCommand.setNextValueAsInt(1);
        setRequestCommand.setNextValueAsByte(EventMod.ModKind.Step);
        setRequestCommand.setNextValueAsThreadID(breakpointThreadID);
        setRequestCommand.setNextValueAsInt(JDWPConstants.StepSize.LINE);
        setRequestCommand.setNextValueAsInt(JDWPConstants.StepDepth.OVER);

        ReplyPacket setRequestReply = debuggeeWrapper.vmMirror
                .performCommand(setRequestCommand);

        checkReplyPacket(setRequestReply, "Set SINGLE_STEP event");

        requestID = setRequestReply.getNextValueAsInt();

        logWriter.println("=> RequestID = " + requestID);
        assertAllDataRead(setRequestReply);

        long startTime = System.currentTimeMillis();

        //resume debuggee
        resumeDebuggee();

        //receive event
        logWriter.println("==> Wait for SINGLE_STEP event");
        CommandPacket event = null;
        try {
            event = debuggeeWrapper.vmMirror.receiveEvent(STEP_TIME_THRESHOLD);
        } catch (IOException e) {
            throw new TestErrorException(e);
        } catch (InterruptedException e) {
            throw new TestErrorException(e);
        }
        long stopTime = System.currentTimeMillis();

        //check if received event is expected
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);
        logWriter.println("==> Received " + parsedEvents.length + " events");

        // trace events
        for (int i = 0; i < parsedEvents.length; i++) {
            logWriter.println("");
            logWriter.println("==> Event #" + i + ";");
            logWriter.println("==> EventKind: " + parsedEvents[i].getEventKind() + "("
                    + JDWPConstants.EventKind.getName(parsedEvents[i].getEventKind()) + ")");
            logWriter.println("==> RequestID: " + parsedEvents[i].getRequestID());
        }

        // check all
        assertEquals("Received wrong number of events,", 1, parsedEvents.length);
        assertEquals("Received wrong event request ID,", requestID, parsedEvents[0].getRequestID());
        assertEquals("Invalid event kind,", JDWPConstants.EventKind.SINGLE_STEP,
                parsedEvents[0].getEventKind(),
                JDWPConstants.EventKind.getName(JDWPConstants.EventKind.SINGLE_STEP),
                JDWPConstants.EventKind.getName(parsedEvents[0].getEventKind()));

        // log elapsed time
        long elapsed = stopTime - startTime;
        logWriter.println("==> Elapsed time is " + elapsed + " millis");

        // clear SINGLE_STEP event
        logWriter.println("==> Clearing SINGLE_STEP event..");
        ReplyPacket clearRequestReply =
            debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, (int) requestID);
        checkReplyPacket(clearRequestReply, "Clear SINGLE_STEP event");
        logWriter.println("==> SINGLE_STEP event has been cleared");

        // resuming debuggee
        logWriter.println("==> Resuming debuggee");
        resumeDebuggee();

        logWriter.println("==> Test PASSED!");
    }
}

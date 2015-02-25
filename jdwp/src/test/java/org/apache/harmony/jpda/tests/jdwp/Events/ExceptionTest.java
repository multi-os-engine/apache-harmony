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

import org.apache.harmony.jpda.tests.framework.Breakpoint;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.Event_EXCEPTION;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;



/**
 * JDWP Unit test for caught EXCEPTION event.
 */
public class ExceptionTest extends JDWPEventTestCase {
    private static final String EXCEPTION_SIGNATURE =
            "Lorg/apache/harmony/jpda/tests/jdwp/Events/DebuggeeException;";

    private static final String THROW_EXCEPTION_METHOD = "throwDebuggeeException";
    private static final String CATCH_EXCEPTION_METHOD = "throwAndCatchDebuggeeException";

    protected String getDebuggeeClassName() {
        return ExceptionDebuggee.class.getName();
    }

    /**
     * This testcase is for caught EXCEPTION event and exception object.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException with
     * no interpreter transition in between. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported exception object is not null</li>
     * <li>the reported exception object is instance of expected class with expected tag</li>
     * </ul>
     */
    public void testExceptionEvent_ExceptionObject_NoTransition() {
        runExceptionObjectTest(false);
    }

    /**
     * This testcase is for caught EXCEPTION event and exception object.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException with
     * an interpreter transition in between. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported exception object is not null</li>
     * <li>the reported exception object is instance of expected class with expected tag</li>
     * </ul>
     */
    public void testExceptionEvent_ExceptionObject_WithTransition() {
        runExceptionObjectTest(true);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported throw location.
     * <BR>It runs ExceptionDebuggee that throws caught DebuggeeException.
     * The test verifies that requested EXCEPTION event occurs, thread is not null,
     * reported location is not null and is equal to location of the top stack frame.
     */
    public void testExceptionEvent_ThrowLocation_NoTransition() {
        runThrowLocationTest(false);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported location.
     * <BR>It runs ExceptionDebuggee that throws caught DebuggeeException.
     * The test verifies that requested EXCEPTION event occurs, thread is not null,
     * reported location is not null and is equal to location of the top stack frame.
     */
    public void testExceptionEvent_ThrowLocation_WithTransition() {
        runThrowLocationTest(true);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported catch location.
     * <BR>It runs ExceptionDebuggee that throws caught DebuggeeException.
     * The test verifies that requested EXCEPTION event occurs, thread is not null,
     * reported catch location is not null and is different than location of the top stack frame.
     */
    public void testExceptionEvent_CatchLocation_NoTransition() {
        runCatchLocationTest(false);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported location.
     * <BR>It runs ExceptionDebuggee that throws caught DebuggeeException.
     * The test verifies that requested EXCEPTION event occurs, thread is not null,
     * reported catch location is not null and is different than location of the top stack frame.
     */
    public void testExceptionEvent_CatchLocation_WithTransition() {
        runCatchLocationTest(true);
    }

    private void runExceptionObjectTest(boolean withTransition) {
        log("STARTED...");

        ParsedEvent.Event_EXCEPTION exceptionEvent =
                requestAndReceiveExceptionEvent(withTransition);
        TaggedObject returnedException = exceptionEvent.getException();

        // assert that exception ObjectID is not null
        log("returnedException.objectID = " + returnedException.objectID);
        assertTrue("Returned exception object is null.", returnedException.objectID != 0);

        // assert that exception tag is OBJECT
        log("returnedException.tag = " + returnedException.objectID);
        assertEquals("Returned exception tag is not OBJECT.",
                JDWPConstants.Tag.OBJECT_TAG, returnedException.tag);

        // assert that exception class is the expected one
        long typeID = getObjectReferenceType(returnedException.objectID);
        String returnedExceptionSignature = getClassSignature(typeID);
        log("returnedExceptionSignature = |" + returnedExceptionSignature+"|");
        assertString("Invalid signature of returned exception,",
                EXCEPTION_SIGNATURE, returnedExceptionSignature);

        // resume debuggee
        log("resume debuggee...");
        debuggeeWrapper.vmMirror.resume();
    }

    private void runThrowLocationTest(boolean withTransition) {
        log("STARTED...");

        ParsedEvent.Event_EXCEPTION exceptionEvent =
                requestAndReceiveExceptionEvent(withTransition);
        long returnedThread = exceptionEvent.getThreadID();
        Location throwLocation = exceptionEvent.getLocation();

        // assert that exception thread is not null
        log("returnedThread = " + returnedThread);
        assertTrue("Returned exception ThreadID is null,", returnedThread != 0);

        // assert that exception location is not null
        log("returnedExceptionLoc = " + throwLocation);
        assertNotNull("Returned exception location is null,", throwLocation);

        // assert that top stack frame location is not null
        Location topFrameLoc = getTopFrameLocation(returnedThread);
        log("topFrameLoc = " + topFrameLoc);
        assertNotNull("Returned top stack frame location is null,", topFrameLoc);

        // assert that locations of exception and top frame are equal
        assertTrue("Different exception and top frame location tag,",
                throwLocation.equals(topFrameLoc));

        // check throw location's method
        long debuggeeClassID = getClassIDBySignature(getDebuggeeClassSignature());
        long debuggeeThrowMethodID = getMethodID(debuggeeClassID, THROW_EXCEPTION_METHOD);
        if (debuggeeClassID != throwLocation.classID || debuggeeThrowMethodID != throwLocation.methodID) {
            StringBuilder builder = new StringBuilder("Invalid method for throw location: expected ");
            builder.append(getDebuggeeClassSignature());
            builder.append('.');
            builder.append(THROW_EXCEPTION_METHOD);
            builder.append(" but got ");
            builder.append(getClassSignature(throwLocation.classID));
            builder.append('.');
            builder.append(getMethodName(throwLocation.classID, throwLocation.methodID));
            fail(builder.toString());
        }

        // resume debuggee
        log("resume debuggee...");
        debuggeeWrapper.vmMirror.resume();
    }

    private void runCatchLocationTest(boolean withTransition) {
        log("STARTED...");

        ParsedEvent.Event_EXCEPTION exceptionEvent =
                requestAndReceiveExceptionEvent(withTransition);
        long returnedThread = exceptionEvent.getThreadID();

        // assert that exception thread is not null
        log("returnedThread = " + returnedThread);
        assertTrue("Returned exception ThreadID is null,", returnedThread != 0);

        // assert that exception catch location is not null
        Location catchLocation = exceptionEvent.getCatchLocation();
        log("returnedExceptionLoc = " + catchLocation);
        assertNotNull("Returned exception catch location is null,", catchLocation);

        // assert that top stack frame location is not null
        Location topFrameLoc = getTopFrameLocation(returnedThread);
        log("topFrameLoc = " + topFrameLoc);
        assertNotNull("Returned top stack frame location is null,", topFrameLoc);
        assertFalse("Same throw and catch locations", catchLocation.equals(topFrameLoc));

        // check catch location's method
        long debuggeeClassID = getClassIDBySignature(getDebuggeeClassSignature());
        long debuggeeThrowMethodID = getMethodID(debuggeeClassID, CATCH_EXCEPTION_METHOD);
        if (debuggeeClassID != catchLocation.classID || debuggeeThrowMethodID != catchLocation.methodID) {
            StringBuilder builder = new StringBuilder("Invalid method for catch location: expected ");
            builder.append(getDebuggeeClassSignature());
            builder.append('.');
            builder.append(CATCH_EXCEPTION_METHOD);
            builder.append(" but got ");
            builder.append(getClassSignature(catchLocation.classID));
            builder.append('.');
            builder.append(getMethodName(catchLocation.classID, catchLocation.methodID));
            fail(builder.toString());
        }

        // resume debuggee
        log("resume debuggee...");
        debuggeeWrapper.vmMirror.resume();
    }

    private Location getTopFrameLocation(long threadID) {

        // getting frames of the thread
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.FramesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsInt(0);
        packet.setNextValueAsInt(1);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        debuggeeWrapper.vmMirror.checkReply(reply);

        // assert that only one top frame is returned
        int framesCount = reply.getNextValueAsInt();
        assertEquals("Invalid number of top stack frames,", 1, framesCount);

        reply.getNextValueAsFrameID(); // frameID
        Location loc = reply.getNextValueAsLocation();

        return loc;
    }

    private ParsedEvent.Event_EXCEPTION requestAndReceiveExceptionEvent(boolean withTransition) {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        boolean isCatch = true;
        boolean isUncatch = true;
        log("=> setException(...)...");
        debuggeeWrapper.vmMirror.setException(EXCEPTION_SIGNATURE, isCatch, isUncatch);
        log("setException(...) DONE");

        if (withTransition) {
            // Sets a breakpoint between the "throw" method and the "catch" method to
            // force a transition between compiled code and interpreted code.
            installBreakpoint();
        }

        log("send to Debuggee SGNL_CONTINUE...");
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        log("=> receiveEvent()...");
        EventPacket event = debuggeeWrapper.vmMirror.receiveEvent();
        log("Event is received! Check it ...");
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);

        // assert that event is the expected one
        log("parsedEvents.length = " + parsedEvents.length);
        log("parsedEvents[0].getEventKind() = " + parsedEvents[0].getEventKind());
        assertEquals("Invalid number of events,", 1, parsedEvents.length);

        assertEquals("Invalid event kind,",
                JDWPConstants.EventKind.EXCEPTION,
                parsedEvents[0].getEventKind(),
                JDWPConstants.EventKind.getName(JDWPConstants.EventKind.EXCEPTION),
                JDWPConstants.EventKind.getName(parsedEvents[0].getEventKind()));

        return (Event_EXCEPTION) parsedEvents[0];
    }

    private void installBreakpoint() {
        Breakpoint breakpoint = new Breakpoint(getDebuggeeClassSignature(), "throwDebuggeeExceptionWithBreakpoint", 0);
        debuggeeWrapper.vmMirror.setCountableBreakpoint(JDWPConstants.TypeTag.CLASS,
                breakpoint, JDWPConstants.SuspendPolicy.ALL, 10);

    }

    private void log(String msg) {
        String testName = getName();
        logWriter.println(">> " + testName + ": " + msg);
    }

}

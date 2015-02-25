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
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;



/**
 * JDWP Unit test for caught EXCEPTION event.
 */
public class ExceptionCaughtTest extends ExceptionBaseTest {

    // TODO describe enum values
    private static enum ExceptionTestKind {
        TEST_WITHOUT_FRAME_TRANSITION,
        TEST_WITH_FRAME_TRANSITION,
        TEST_FROM_NATIVE_METHOD;
    }

    protected String getDebuggeeClassName() {
        return ExceptionCaughtDebuggee.class.getName();
    }

    /**
     * This testcase is for caught EXCEPTION event and reported exception object.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException.
     * It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported exception object is not null</li>
     * <li>the reported exception object is instance of expected class with expected tag</li>
     * </ul>
     */
    public void testExceptionEvent_ExceptionObject_NoTransition() {
        runExceptionObjectTest(ExceptionTestKind.TEST_WITHOUT_FRAME_TRANSITION);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported exception object.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException with
     * a native transition in between. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported exception object is not null</li>
     * <li>the reported exception object is instance of expected class with expected tag</li>
     * </ul>
     */
    public void testExceptionEvent_ExceptionObject_WithTransition() {
        runExceptionObjectTest(ExceptionTestKind.TEST_WITH_FRAME_TRANSITION);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported exception object.
     * <BR>It runs ExceptionDebuggee that throws an exception from a native method
     * and catches it. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported exception object is not null</li>
     * <li>the reported exception object is instance of expected class with expected tag</li>
     * </ul>
     */
    public void testExceptionEvent_ExceptionObject_FromNative() {
        runExceptionObjectTest(ExceptionTestKind.TEST_FROM_NATIVE_METHOD);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported throw location.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException.
     * It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported thread is not null</li>
     * <li>the reported throw location is not null</li>
     * <li>the reported throw location is equal to location of the top stack frame</li>
     * </ul>
     */
    public void testExceptionEvent_ThrowLocation_NoTransition() {
        runThrowLocationTest(ExceptionTestKind.TEST_WITHOUT_FRAME_TRANSITION);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported throw location.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException with
     * a native transition in between. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported thread is not null</li>
     * <li>the reported throw location is not null</li>
     * <li>the reported throw location is equal to location of the top stack frame</li>
     * </ul>
     */
    public void testExceptionEvent_ThrowLocation_WithTransition() {
        runThrowLocationTest(ExceptionTestKind.TEST_WITH_FRAME_TRANSITION);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported throw location.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException with
     * a native transition in between. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported thread is not null</li>
     * <li>the reported throw location is not null</li>
     * <li>the reported throw location is equal to location of the top stack frame</li>
     * </ul>
     */
    public void testExceptionEvent_ThrowLocation_FromNative() {
        runThrowLocationTest(ExceptionTestKind.TEST_FROM_NATIVE_METHOD);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported catch location.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException.
     * It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported thread is not null</li>
     * <li>the reported catch location is not null</li>
     * <li>the reported catch location is different than the top stack frame</li>
     * </ul>
     */
    public void disable_testExceptionEvent_CatchLocation_NoTransition() {
        runCatchLocationTest(ExceptionTestKind.TEST_WITHOUT_FRAME_TRANSITION);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported catch location.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException with
     * a native transition in between. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported thread is not null</li>
     * <li>the reported catch location is not null</li>
     * <li>the reported catch location is different than the top stack frame</li>
     * </ul>
     */
    public void disable_testExceptionEvent_CatchLocation_WithTransition() {
        runCatchLocationTest(ExceptionTestKind.TEST_WITH_FRAME_TRANSITION);
    }

    /**
     * This testcase is for caught EXCEPTION event and reported catch location.
     * <BR>It runs ExceptionDebuggee that throws and catches a DebuggeeException with
     * a native transition in between. It verifies the following:
     * <ul>
     * <li>the requested EXCEPTION event occurs</li>
     * <li>the reported thread is not null</li>
     * <li>the reported catch location is not null</li>
     * <li>the reported catch location is different than the top stack frame</li>
     * </ul>
     */
    public void disable_testExceptionEvent_CatchLocation_FromNative() {
        runCatchLocationTest(ExceptionTestKind.TEST_FROM_NATIVE_METHOD);
    }

    /**
     * Requests and receives EXCEPTION event then checks reported exception object.
     */
    private void runExceptionObjectTest(ExceptionTestKind kind) {
        printTestLog("STARTED...");

        ParsedEvent.Event_EXCEPTION exceptionEvent =
                requestAndReceiveExceptionEvent(kind);
        TaggedObject returnedException = exceptionEvent.getException();

        // assert that exception ObjectID is not null
        printTestLog("returnedException.objectID = " + returnedException.objectID);
        assertTrue("Returned exception object is null.", returnedException.objectID != 0);

        // assert that exception tag is OBJECT
        printTestLog("returnedException.tag = " + returnedException.objectID);
        assertEquals("Returned exception tag is not OBJECT.",
                JDWPConstants.Tag.OBJECT_TAG, returnedException.tag);

        // assert that exception class is the expected one
        long typeID = getObjectReferenceType(returnedException.objectID);
        String returnedExceptionSignature = getClassSignature(typeID);
        printTestLog("returnedExceptionSignature = |" + returnedExceptionSignature+"|");
        assertString("Invalid signature of returned exception,",
                getExpectedExceptionSignature(kind), returnedExceptionSignature);

        // resume debuggee
        printTestLog("resume debuggee...");
        debuggeeWrapper.vmMirror.resume();
    }

    /**
     * Requests and receives EXCEPTION event then checks reported throw location.
     */
    private void runThrowLocationTest(ExceptionTestKind kind) {
        printTestLog("STARTED...");

        ParsedEvent.Event_EXCEPTION exceptionEvent =
                requestAndReceiveExceptionEvent(kind);
        long returnedThread = exceptionEvent.getThreadID();
        Location throwLocation = exceptionEvent.getLocation();

        // assert that exception thread is not null
        printTestLog("returnedThread = " + returnedThread);
        assertTrue("Returned exception ThreadID is null,", returnedThread != 0);

        // assert that exception location is not null
        printTestLog("returnedExceptionLoc = " + throwLocation);
        assertNotNull("Returned exception location is null,", throwLocation);

        // assert that top stack frame location is not null
        Location topFrameLoc = getTopFrameLocation(returnedThread);
        printTestLog("topFrameLoc = " + topFrameLoc);
        assertNotNull("Returned top stack frame location is null,", topFrameLoc);

        // assert that locations of exception and top frame are equal
        assertEquals("Different exception and top frame location tag,", topFrameLoc, throwLocation);

        // check throw location's method
        String expectedThrowLocationClassSignature = getThrowLocationMethodClassSignature(kind);
        String expectedThrowLocationMethodName = getThrowLocationMethodName(kind);
        long debuggeeClassID = getClassIDBySignature(expectedThrowLocationClassSignature);
        long debuggeeThrowMethodID = getMethodID(debuggeeClassID, expectedThrowLocationMethodName);
        if (debuggeeClassID != throwLocation.classID || debuggeeThrowMethodID != throwLocation.methodID) {
            StringBuilder builder = new StringBuilder("Invalid method for throw location: expected ");
            builder.append(expectedThrowLocationClassSignature);
            builder.append('.');
            builder.append(expectedThrowLocationMethodName);
            builder.append(" but got ");
            builder.append(dumpLocation(throwLocation));
            fail(builder.toString());
        }

        // resume debuggee
        printTestLog("resume debuggee...");
        debuggeeWrapper.vmMirror.resume();
    }

    /**
     * Requests and receives EXCEPTION event then checks reported catch location.
     */
    private void runCatchLocationTest(ExceptionTestKind kind) {
        printTestLog("STARTED...");

        ParsedEvent.Event_EXCEPTION exceptionEvent =
                requestAndReceiveExceptionEvent(kind);
        long returnedThread = exceptionEvent.getThreadID();

        // assert that exception thread is not null
        printTestLog("returnedThread = " + returnedThread);
        assertTrue("Returned exception ThreadID is null,", returnedThread != 0);

        // assert that exception catch location is not null
        Location catchLocation = exceptionEvent.getCatchLocation();
        printTestLog("returnedExceptionLoc = " + catchLocation);
        assertNotNull("Returned exception catch location is null,", catchLocation);

        // assert that top stack frame location is not null
        Location topFrameLoc = getTopFrameLocation(returnedThread);
        printTestLog("topFrameLoc = " + topFrameLoc);
        assertNotNull("Returned top stack frame location is null,", topFrameLoc);
        assertFalse("Same throw and catch locations", catchLocation.equals(topFrameLoc));

        // check catch location's method
        String expectedCatchLocationClassSignature = getDebuggeeClassSignature();
        String expectedCatchLocationMethodName = getCatchLocationMethodName(kind);
        long debuggeeClassID = getClassIDBySignature(expectedCatchLocationClassSignature);
        long debuggeeThrowMethodID = getMethodID(debuggeeClassID, expectedCatchLocationMethodName);
        if (debuggeeClassID != catchLocation.classID || debuggeeThrowMethodID != catchLocation.methodID) {
            StringBuilder builder = new StringBuilder("Invalid method for catch location: expected ");
            builder.append(expectedCatchLocationClassSignature);
            builder.append('.');
            builder.append(expectedCatchLocationMethodName);
            builder.append(" but got ");
            builder.append(dumpLocation(catchLocation));
            fail(builder.toString());
        }

        // resume debuggee
        printTestLog("resume debuggee...");
        debuggeeWrapper.vmMirror.resume();
    }

    /**
     * Requests and receives EXCEPTION event then checks the received event kind,
     * the received event request ID and the thread state.
     * @return the exception event
     */
    private ParsedEvent.Event_EXCEPTION requestAndReceiveExceptionEvent(ExceptionTestKind kind) {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        boolean isCatch = true;
        boolean isUncatch = true;
        printTestLog("=> setException(...)...");
        String exceptionSignature = getExpectedExceptionSignature(kind);
        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.setException(exceptionSignature,
                isCatch, isUncatch);
        int requestID = replyPacket.getNextValueAsInt();
        assertAllDataRead(replyPacket);

        printTestLog("setException(...) DONE");

        if (kind == ExceptionTestKind.TEST_WITH_FRAME_TRANSITION) {
            // Sets the breakpoint at the start of the method.
            Breakpoint breakpoint = new Breakpoint(getDebuggeeClassSignature(),
                    "throwDebuggeeExceptionWithTransition", 0);
            debuggeeWrapper.vmMirror.setCountableBreakpoint(JDWPConstants.TypeTag.CLASS,
                    breakpoint, JDWPConstants.SuspendPolicy.ALL, 10);
        }

        printTestLog("send to Debuggee SGNL_CONTINUE...");

        String signalToSend = getSignalMessage(kind);
        synchronizer.sendMessage(signalToSend);

        return receiveAndCheckExceptionEvent(requestID);
    }

    static String getExpectedExceptionSignature(ExceptionTestKind kind) {
        if (kind == ExceptionTestKind.TEST_FROM_NATIVE_METHOD) {
            return getClassSignature(NullPointerException.class);
        } else {
            return getClassSignature(DebuggeeException.class);
        }
    }

    static String getThrowLocationMethodName(ExceptionTestKind kind) {
        if (kind == ExceptionTestKind.TEST_FROM_NATIVE_METHOD) {
            return "arraycopy";
        } else {
            return "throwDebuggeeException";
        }
    }

    String getThrowLocationMethodClassSignature(ExceptionTestKind kind) {
        if (kind == ExceptionTestKind.TEST_FROM_NATIVE_METHOD) {
            return getClassSignature(System.class);
        } else {
            return getDebuggeeClassSignature();
        }
    }

    static String getCatchLocationMethodName(ExceptionTestKind kind) {
        if (kind == ExceptionTestKind.TEST_FROM_NATIVE_METHOD) {
            return "testThrowAndCatchExceptionFromNativeMethod";
        } else {
            return "throwAndCatchDebuggeeException";
        }
    }

    static String getSignalMessage(ExceptionTestKind kind) {
        switch (kind) {
            case TEST_WITH_FRAME_TRANSITION:
                return ExceptionCaughtDebuggee.TEST_EXCEPTION_WITH_NATIVE_TRANSITION;
            case TEST_FROM_NATIVE_METHOD:
                return ExceptionCaughtDebuggee.TEST_EXCEPTION_FROM_NATIVE_METHOD;
            case TEST_WITHOUT_FRAME_TRANSITION:
                return JPDADebuggeeSynchronizer.SGNL_CONTINUE;
            default:
                throw new IllegalArgumentException("Not supported " + kind.name());
        }
    }

    static boolean needsNativeTransition(ExceptionTestKind kind) {
        return kind == ExceptionTestKind.TEST_WITH_FRAME_TRANSITION;
    }

}

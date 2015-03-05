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
 * @author Viacheslav G. Rybalov
 */

/**
 * Created on 10.03.2005
 */
package org.apache.harmony.jpda.tests.jdwp.ClassType;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.debuggee.InvokeMethodDebuggee;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP unit test for ClassType.InvokeMethod command.
 */
public class InvokeMethodTest extends JDWPSyncTestCase {

    private static final String BREAKPOINT_METHOD_NAME = "execMethod";

    protected String getDebuggeeClassName() {
        return InvokeMethodDebuggee.class.getName();
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning boolean value.
     */
    public void testInvokeMethod001_returnBoolean() {
        runTestInvokeMethod("testBooleanReturn", new Value(InvokeMethodDebuggee.EXPECTED_BOOLEAN_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning byte value.
     */
    public void testInvokeMethod001_returnByte() {
        runTestInvokeMethod("testByteReturn", new Value(InvokeMethodDebuggee.EXPECTED_BYTE_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning char value.
     */
    public void testInvokeMethod001_returnChar() {
        runTestInvokeMethod("testCharReturn", new Value(InvokeMethodDebuggee.EXPECTED_CHAR_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning short value.
     */
    public void testInvokeMethod001_returnShort() {
        runTestInvokeMethod("testShortReturn", new Value(InvokeMethodDebuggee.EXPECTED_SHORT_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning int value.
     */
    public void testInvokeMethod001_returnInt() {
        runTestInvokeMethod("testIntReturn", new Value(InvokeMethodDebuggee.EXPECTED_INT_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning float value.
     */
    public void testInvokeMethod001_returnFloat() {
        runTestInvokeMethod("testFloatReturn", new Value(InvokeMethodDebuggee.EXPECTED_FLOAT_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning long value.
     */
    public void testInvokeMethod001_returnLong() {
        runTestInvokeMethod("testLongReturn", new Value(InvokeMethodDebuggee.EXPECTED_LONG_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning double value.
     */
    public void testInvokeMethod001_returnDouble() {
        runTestInvokeMethod("testDoubleReturn", new Value(InvokeMethodDebuggee.EXPECTED_DOUBLE_RESULT));
    }

    /**
     * Common method to test ClassType.InvokeMethod.
     * <BR>It first starts the InvokeMethodDebuggee and suspends it on a BREAKPOINT event.
     * Then does the following checks:
     * <BR>&nbsp;&nbsp; - send ClassType.InvokeMethod command for given method,
     * which should not throw any Exception, and checks that returned value is
     * expected value and returned exception object is null;
     * <BR>&nbsp;&nbsp; - send ClassType.InvokeMethod command for given method,
     * which should throw some Exception, and checks that returned exception
     * object is not null and has expected attributes;
     *
     * @param methodName the method to execute
     * @param expectedValue the expected return value
     */
    private void runTestInvokeMethod(String methodName, Value expectedValue) {
        printTestLog("START");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long typeID = getClassIDBySignature(getDebuggeeClassSignature());
        long targetMethodID = getMethodID(typeID, methodName);

        // Suspend debuggee on an event so we can invoke method.
        long targetThreadID = suspendDebuggeeOnBreakpoint(typeID);

        // Make InvokeMethod without Exception
        Value returnValue = invokeMethodWithoutException(typeID, targetThreadID, targetMethodID);
        assertEquals("Invalid return value", expectedValue, returnValue);

        // Make InvokeMethod with Exception
        TaggedObject exception = invokeMethodWithException(typeID, targetThreadID, targetMethodID);
        assertTrue("Invalid exception object ID:<" + exception.objectID + ">", exception.objectID != 0);
        assertEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag
                , JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG)
                , JDWPConstants.Tag.getName(exception.tag));
        logWriter.println(" ClassType.InvokeMethod: exception.tag="
                + exception.tag + " exception.objectID=" + exception.objectID);

        //  Let's resume application
        finishDebuggee();
        printTestLog("END");
    }

    /**
     * Suspends all threads of debuggee on a breakpoint.
     * @param debuggeeTypeID the debuggee class ID
     * @return the event thread ID
     */
    private long suspendDebuggeeOnBreakpoint(long debuggeeTypeID) {
        int breakpointRequestID = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(debuggeeTypeID, BREAKPOINT_METHOD_NAME);

        // Signal debuggee to continue.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait event
        long targetThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointRequestID);
        assertTrue("Invalid targetThreadID, must be != 0", targetThreadID != 0);

        //  Let's clear event request
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_ENTRY, breakpointRequestID);

        return targetThreadID;
    }

    private Value invokeMethodWithoutException(long typeID, long targetThreadID,
            long targetMethodID) {
        ReplyPacket reply = checkInvokeMethod(typeID, targetThreadID, targetMethodID, false);

        Value returnValue = reply.getNextValueAsValue();
        assertNotNull("Returned value is null", returnValue);
        logWriter.println(" ClassType.InvokeMethod: returnValue=" + returnValue.toString());

        TaggedObject exception = reply.getNextValueAsTaggedObject();
        assertNotNull("Returned exception is null", exception);
        assertTrue("Invalid exception object ID:<" + exception.objectID + ">", exception.objectID == 0);
        assertEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag
                , JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG)
                , JDWPConstants.Tag.getName(exception.tag));
        logWriter.println(" ClassType.InvokeMethod: exception.tag="
                + exception.tag + " exception.objectID=" + exception.objectID);
        assertAllDataRead(reply);

        return returnValue;
    }

    private TaggedObject invokeMethodWithException(long typeID, long targetThreadID,
            long targetMethodID) {
        ReplyPacket reply = checkInvokeMethod(typeID, targetThreadID, targetMethodID, true);

        Value returnValue = reply.getNextValueAsValue();
        // TODO value is null
        logWriter.println(" ClassType.InvokeMethod: returnValue=" + returnValue.toString());

        TaggedObject exception = reply.getNextValueAsTaggedObject();
        assertNotNull("Returned exception is null", exception);
        assertAllDataRead(reply);

        return exception;
    }

    private ReplyPacket invokeMethod(long typeID, long targetThreadID,
            long targetMethodID, boolean needThrow) {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
        packet.setNextValueAsClassID(typeID);
        packet.setNextValueAsThreadID(targetThreadID);
        packet.setNextValueAsMethodID(targetMethodID);
        packet.setNextValueAsInt(1);
            packet.setNextValueAsValue(new Value(needThrow));
        packet.setNextValueAsInt(0);  // invoke options: resume all threads.
        String message = " Send ClassType.InvokeMethod ";
        message += (needThrow) ? "with" : "without";
        message += " exception";
        logWriter.println(message);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        return reply;
    }

    private ReplyPacket checkInvokeMethod(long typeID, long targetThreadID,
            long targetMethodID, boolean needThrow) {
        ReplyPacket reply = invokeMethod(typeID, targetThreadID, targetMethodID, needThrow);
        checkReplyPacket(reply, "ClassType::InvokeMethod command");
        return reply;
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command.
     * <BR>At first the test starts debuggee.
     * <BR>Then does the following checks:
     * <BR>&nbsp;&nbsp; - send ClassType.InvokeMethod command for method,
     * which actually does not belong to passed class (taking into account
     * inheritance).
     * <BR>Test expects that INVALID_METHODID error is returned by command.
     */
    public void testInvokeMethod002() {
        logWriter.println("==> testInvokeMethod002: START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("\n==> Getting debuggeeRefTypeID... ");
        String debuggeeSignature = getDebuggeeClassSignature();
        logWriter.println("==> debuggeeSignature = |" + debuggeeSignature + "|+");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(debuggeeSignature);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can not get debuggeeRefTypeID!");
            fail("Can not get debuggeeRefTypeID!");
        }
        logWriter.println("==> debuggeeRefTypeID = " + debuggeeRefTypeID);

        logWriter.println("\n==> Getting testMethodID for debuggee method 'testMethod2'... ");
        String testMethodName = "testMethod2";
        long testMethodID =
            debuggeeWrapper.vmMirror.getMethodID(debuggeeRefTypeID, testMethodName);
        if ( testMethodID == -1 ) {
            logWriter.println("## FAILURE: Can not get methodID!");
            fail("Can not get methodID!");
        }
        logWriter.println("==> testMethodID = " + testMethodID);

        // Suspend debuggee on breakpoint so we can invoke method.
        long targetThreadID = suspendDebuggeeOnBreakpoint(debuggeeRefTypeID);

        logWriter.println("\n==> Getting invalidClassRefTypeID... ");
        String invalidClassSignature = "Lorg/apache/harmony/jpda/tests/jdwp/share/debuggee/testClass2;";
        logWriter.println("==> invalidClassSignature = |" + invalidClassSignature + "|+");
        long invalidClassRefTypeID = debuggeeWrapper.vmMirror.getClassID(invalidClassSignature);
        if ( invalidClassRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can not get invalidClassRefTypeID!");
            fail("Can not get invalidClassRefTypeID!");
        }
        logWriter.println("==> invalidClassRefTypeID = " + invalidClassRefTypeID);

        logWriter.println
        ("\n==> Send ClassType::InvokeMethod for invalidClassRefTypeID, testMethodID...");
        ReplyPacket reply = invokeMethod(invalidClassRefTypeID, targetThreadID, testMethodID, false);
        checkInvokeMethodFailure(reply, JDWPConstants.Error.INVALID_METHODID);

        finishDebuggee();
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command.
     * <BR>At first the test starts debuggee.
     * <BR>Then does the following checks:
     * <BR>&nbsp;&nbsp; - send ClassType.InvokeMethod command for method,
     * which actually is not static method.
     * <BR>Test expects that INVALID_METHODID error is returned by command.
     */
    public void testInvokeMethod003() {
        logWriter.println("==> testInvokeMethod003: START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("\n==> Getting debuggeeRefTypeID... ");
        String debuggeeSignature = getDebuggeeClassSignature();
        logWriter.println("==> debuggeeSignature = |" + debuggeeSignature + "|+");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(debuggeeSignature);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can not get debuggeeRefTypeID!");
            fail("Can not get debuggeeRefTypeID!");
        }
        logWriter.println("==> debuggeeRefTypeID = " + debuggeeRefTypeID);

        logWriter.println("\n==> Getting nonStaticMethodID for debuggee method 'testMethod1'... ");
        String nonStaticMethodName = "testMethod1";
        long nonStaticMethodID =
            debuggeeWrapper.vmMirror.getMethodID(debuggeeRefTypeID, nonStaticMethodName);
        if ( nonStaticMethodID == -1 ) {
            logWriter.println("## FAILURE: Can not get methodID!");
            fail("Can not get methodID!");
        }
        logWriter.println("==> nonStaticMethodID = " + nonStaticMethodID);

        // Suspend debuggee on breakpoint so we can invoke method.
        long targetThreadID = suspendDebuggeeOnBreakpoint(debuggeeRefTypeID);

        logWriter.println
        ("\n==> Send ClassType::InvokeMethod for debuggeeRefTypeID, nonStaticMethodID...");
        ReplyPacket reply = invokeMethod(debuggeeRefTypeID, targetThreadID, nonStaticMethodID, false);
        checkInvokeMethodFailure(reply, JDWPConstants.Error.INVALID_METHODID);

        finishDebuggee();
    }

    public void testInvokeMethod004_InvalidArgumentCount() {
        printTestLog("START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("\n==> Getting debuggeeRefTypeID... ");
        String debuggeeSignature = getDebuggeeClassSignature();
        logWriter.println("==> debuggeeSignature = |" + debuggeeSignature + "|+");
        long debuggeeRefTypeID = debuggeeWrapper.vmMirror.getClassID(debuggeeSignature);
        if ( debuggeeRefTypeID == -1 ) {
            logWriter.println("## FAILURE: Can not get debuggeeRefTypeID!");
            fail("Can not get debuggeeRefTypeID!");
        }
        logWriter.println("==> debuggeeRefTypeID = " + debuggeeRefTypeID);

        String staticMethodName = "testMethod2";
        logWriter.println("\n==> Getting method ID for debuggee method '"+staticMethodName+"'... ");
        long targetMethodID =
            debuggeeWrapper.vmMirror.getMethodID(debuggeeRefTypeID, staticMethodName);
        if ( targetMethodID == -1 ) {
            logWriter.println("## FAILURE: Can not get methodID!");
            fail("Can not get methodID!");
        }
        logWriter.println("==> nonStaticMethodID = " + targetMethodID);

        // Suspend debuggee on breakpoint so we can invoke method.
        long targetThreadID = suspendDebuggeeOnBreakpoint(debuggeeRefTypeID);

        logWriter.println
        ("\n==> Send ClassType::InvokeMethod for debuggeeRefTypeID, nonStaticMethodID...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
        packet.setNextValueAsClassID(debuggeeRefTypeID);
        packet.setNextValueAsThreadID(targetThreadID);
        packet.setNextValueAsMethodID(targetMethodID);
        packet.setNextValueAsInt(0);  // delibarately pass no arguments.
        packet.setNextValueAsInt(0);  // invoke options: resume all threads.
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkInvokeMethodFailure(reply, JDWPConstants.Error.ILLEGAL_ARGUMENT);

        finishDebuggee();
    }

    private void checkInvokeMethodFailure(ReplyPacket reply, int expectedError) {
        String expectedErrorName = JDWPConstants.Error.getName(expectedError);
        short errorCode = reply.getErrorCode();
        if (errorCode == JDWPConstants.Error.NONE) {
            logWriter.println
            ("## FAILURE: ClassType::InvokeMethod command does NOT return expected error - " + expectedErrorName);

            // next is only for extra info
            logWriter.println("\n==> Result if invoke method:");
            Value returnValue = reply.getNextValueAsValue();
            if (returnValue != null) {
                if (returnValue.getTag() != JDWPConstants.Tag.NO_TAG) {
                    logWriter.println(" ClassType.InvokeMethod: returnValue=" + returnValue.toString());
                } else {
                    logWriter.println(" ClassType.InvokeMethod: returnValue=<null>");
                }
            }

            TaggedObject exception = reply.getNextValueAsTaggedObject();
            if (exception != null) {
                logWriter.println(" ClassType.InvokeMethod: exception.tag="
                        + exception.tag + " exception.objectID=" + exception.objectID);
                if ( exception.objectID != 0 ) {
                    String exceptionSignature = getObjectSignature(exception.objectID);
                    logWriter.println(" exceptionSignature = " + exceptionSignature);
                }
            }
        }
        checkReplyPacket(reply, "ClassType::InvokeMethod command", expectedError);
    }

    private void finishDebuggee() {
        // Resume debuggee from BREAKPOINT event.
        resumeDebuggee();

        // Continue debuggee so it terminates.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }
}

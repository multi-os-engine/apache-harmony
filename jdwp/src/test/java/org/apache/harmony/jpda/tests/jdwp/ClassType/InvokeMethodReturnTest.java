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

package org.apache.harmony.jpda.tests.jdwp.ClassType;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPInvokeMethodTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.debuggee.InvokeMethodReturnDebuggee;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.util.ArrayList;
import java.util.List;

/**
 * JDWP unit test for ClassType.InvokeMethod command.
 */
public class InvokeMethodReturnTest extends JDWPInvokeMethodTestCase {

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning boolean value.
     */
    public void testInvokeStaticMethodBoolean() {
        runTestInvokeMethod("testStaticBoolean",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_BOOLEAN_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning byte value.
     */
    public void testInvokeStaticMethodByte() {
        runTestInvokeMethod("testStaticByte",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_BYTE_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning char value.
     */
    public void testInvokeStaticMethodChar() {
        runTestInvokeMethod("testStaticChar",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_CHAR_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning short value.
     */
    public void testInvokeStaticMethodShort() {
        runTestInvokeMethod("testStaticShort",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_SHORT_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning int value.
     */
    public void testInvokeStaticMethodInt() {
        runTestInvokeMethod("testStaticInt",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_INT_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning float value.
     */
    public void testInvokeStaticMethodFloat() {
        runTestInvokeMethod("testStaticFloat",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_FLOAT_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning long value.
     */
    public void testInvokeStaticMethodLong() {
        runTestInvokeMethod("testStaticLong",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_LONG_RESULT));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning double value.
     */
    public void testInvokeStaticMethodDouble() {
        runTestInvokeMethod("testStaticDouble",
                new Value(InvokeMethodReturnDebuggee.EXPECTED_DOUBLE_RESULT));
    }

    /**
     * Common method to test ClassType.InvokeMethod.
     * <BR>It first starts the InvokeMethodReturnDebuggee and suspends it on a BREAKPOINT event.
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
        Value returnValue = invokeStaticMethodWithoutException(typeID, targetThreadID,
                targetMethodID);
        assertEquals("Invalid return value", expectedValue, returnValue);

        // Make InvokeMethod with Exception
        TaggedObject exception = invokeStaticMethodWithException(typeID, targetThreadID,
                targetMethodID);
        assertTrue("Invalid exception object ID:<" + exception.objectID + ">",
                exception.objectID != 0);
        assertEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag
                , JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG)
                , JDWPConstants.Tag.getName(exception.tag));
        logWriter.println(" ClassType.InvokeMethod: exception.tag="
                + exception.tag + " exception.objectID=" + exception.objectID);

        //  Let's resume application
        finishDebuggee();
        printTestLog("END");
    }

    private Value invokeStaticMethodWithoutException(long typeID, long targetThreadID,
            long targetMethodID) {
        ReplyPacket reply = invokeStaticMethod(typeID, targetThreadID, targetMethodID, false);

        Value returnValue = reply.getNextValueAsValue();
        assertNotNull("Returned value is null", returnValue);
        logWriter.println(" ClassType.InvokeMethod: returnValue=" + returnValue.toString());

        TaggedObject exception = reply.getNextValueAsTaggedObject();
        assertNotNull("Returned exception is null", exception);
        assertTrue("Invalid exception object ID:<" + exception.objectID + ">",
                exception.objectID == 0);
        assertEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag
                , JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG)
                , JDWPConstants.Tag.getName(exception.tag));
        logWriter.println(" ClassType.InvokeMethod: exception.tag="
                + exception.tag + " exception.objectID=" + exception.objectID);
        assertAllDataRead(reply);

        return returnValue;
    }

    private TaggedObject invokeStaticMethodWithException(long typeID, long targetThreadID,
            long targetMethodID) {
        ReplyPacket reply = invokeStaticMethod(typeID, targetThreadID, targetMethodID, true);

        Value returnValue = reply.getNextValueAsValue();
        // TODO value is null
        logWriter.println(" ClassType.InvokeMethod: returnValue=" + returnValue.toString());

        TaggedObject exception = reply.getNextValueAsTaggedObject();
        assertNotNull("Returned exception is null", exception);
        assertAllDataRead(reply);

        return exception;
    }

    private ReplyPacket invokeStaticMethod(long typeID, long targetThreadID,
            long targetMethodID, boolean needThrow) {
        String message = " Send ClassType.InvokeMethod ";
        message += (needThrow) ? "with" : "without";
        message += " exception";
        logWriter.println(message);

        List<Value> args = new ArrayList<Value>();
        args.add(new Value(needThrow));
        int options = 0;  // invoke options: resume all threads.
        ReplyPacket reply = invokeMethod(0, typeID, targetThreadID, targetMethodID, args, options);
        checkReplyPacket(reply, "ClassType::InvokeMethod command");
        return reply;
    }

    @Override
    protected ReplyPacket invokeMethod(long receiverID, long typeID, long targetThreadID,
            long targetMethodID, List<? extends Value> args, int options) {
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
        packet.setNextValueAsClassID(typeID);
        packet.setNextValueAsThreadID(targetThreadID);
        packet.setNextValueAsMethodID(targetMethodID);
        packet.setNextValueAsInt(args.size());
        for (Value arg : args) {
            packet.setNextValueAsValue(arg);
        }
        packet.setNextValueAsInt(options);
        return debuggeeWrapper.vmMirror.performCommand(packet);
    }
}

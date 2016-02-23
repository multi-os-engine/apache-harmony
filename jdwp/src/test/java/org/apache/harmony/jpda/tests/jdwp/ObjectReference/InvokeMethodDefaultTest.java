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

package org.apache.harmony.jpda.tests.jdwp.ObjectReference;

import org.apache.harmony.jpda.tests.framework.jdwp.*;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP unit test for ObjectReference.InvokeMethod command.
 */
public class InvokeMethodDefaultTest extends JDWPSyncTestCase {
    protected String getDebuggeeClassName() {
        return InvokeMethodDefaultDebuggee.class.getName();
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command.
     * <BR>The test first starts the debuggee, requests METHOD_ENTRY event so
     * the application suspends on first invoke.
     * <BR>Then sends ObjectReference.InvokeMethod command to invoke method
     * InvokeMethodDefaultTest$TestInterface.testDefaultMethod on an instance
     * of a subclass of Object overriding the method toString.
     * <BR>Checks that returned value is expected string value and returned
     * exception object is null.
     * <BR>Finally resumes the application.
     * @param shouldThrow If true test that we can make the function throw.
     */
    private void testInvokeMethod(boolean shouldThrow) {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Get debuggee class ID.
        String debuggeeClassSig = getClassSignature(InvokeMethodDefaultDebuggee.class);
        long debuggeeTypeID = debuggeeWrapper.vmMirror.getClassID(debuggeeClassSig);
        assertTrue("Failed to find debuggee class", debuggeeTypeID != 0);

        // Set METHOD_ENTRY event request so application is suspended.
        CommandPacket packet = new CommandPacket(
                JDWPCommands.EventRequestCommandSet.CommandSetID,
                JDWPCommands.EventRequestCommandSet.SetCommand);
        packet.setNextValueAsByte(JDWPConstants.EventKind.METHOD_ENTRY);
        packet.setNextValueAsByte(JDWPConstants.SuspendPolicy.ALL);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsByte(EventMod.ModKind.ClassOnly);  // class-only modifier.
        packet.setNextValueAsReferenceTypeID(debuggeeTypeID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "EventRequest::Set command");

        int requestID = reply.getNextValueAsInt();
        logWriter.println(" EventRequest.Set: requestID=" + requestID);
        assertAllDataRead(reply);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        long targetThreadID = 0;
        // Wait for METHOD_ENTRY event and collect event thread.
        CommandPacket event = debuggeeWrapper.vmMirror.receiveEvent();
        byte suspendPolicy = event.getNextValueAsByte();
        int events = event.getNextValueAsInt();
        logWriter.println(" EVENT_THREAD event: suspendPolicy=" + suspendPolicy + " events=" + events);
        for (int i = 0; i < events; i++) {
            byte eventKind = event.getNextValueAsByte();
            int newRequestID = event.getNextValueAsInt();
            long threadID = event.getNextValueAsThreadID();
            //Location location =
            event.getNextValueAsLocation();
            logWriter.println("  EVENT_THREAD event " + i + ": eventKind="
                    + eventKind + " requestID=" + newRequestID + " threadID="
                    + threadID);
            if (newRequestID == requestID) {
                targetThreadID = threadID;
            }
        }
        assertAllDataRead(event);
        assertTrue("Invalid targetThreadID, must be != 0", targetThreadID != 0);

        //  Now we're suspended, clear event request.
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.METHOD_ENTRY, requestID);

        // Load value of the invoke's receiver.
        long receiverFieldID = debuggeeWrapper.vmMirror.getFieldID(debuggeeTypeID, "invokeReceiver");
        assertTrue("Failed to find receiver field", receiverFieldID != 0);

        logWriter.println(" Send ReferenceType.GetValues");
        packet = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.GetValuesCommand);
        packet.setNextValueAsReferenceTypeID(debuggeeTypeID);
        packet.setNextValueAsInt(1);  // looking for one field.
        packet.setNextValueAsFieldID(receiverFieldID);  // the field we're looking for.
        reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ReferenceType::GetValues command");
        int valuesCount = reply.getNextValueAsInt();  // number of field values.
        Value fieldValue = reply.getNextValueAsValue();
        assertAllDataRead(reply);
        assertEquals("Not the right number of values", 1, valuesCount);
        assertNotNull("Received null value", fieldValue);
        assertEquals("Expected an object value", JDWPConstants.Tag.OBJECT_TAG, fieldValue.getTag());
        long receiverObjectID = fieldValue.getLongValue();
        assertTrue("Field is null", receiverObjectID != 0);

        // Get test class ID.
        String testClassSig = getClassSignature(InvokeMethodDefaultDebuggee.TestClass.class);
        long testTypeID = debuggeeWrapper.vmMirror.getClassID(testClassSig);
        assertTrue("Failed to find test class", testTypeID != 0);

        // Get InvokeMethodDefualtDebuggee.TestInterface class ID.
        long interfaceTypeID = debuggeeWrapper.vmMirror.getInterfaceID(
                getClassSignature(InvokeMethodDefaultDebuggee.TestInterface.class));
        assertTrue("Failed to find InvokeMethodDefaultDebuggee.TestInterface class",
                interfaceTypeID != 0);

        // Get InvokeMethodDefualtDebuggee.TestInterface.testDefaultMethod method ID.
        long targetMethodID = debuggeeWrapper.vmMirror.getMethodID(interfaceTypeID, "testDefaultMethod");
        assertTrue("Failed to find method", targetMethodID != 0);
        logWriter.println(" Method ID=" + targetMethodID);

        // The method argument.
        Value throwValue = new Value(shouldThrow);
        // Invoke method.
        packet = new CommandPacket(
                JDWPCommands.ObjectReferenceCommandSet.CommandSetID,
                JDWPCommands.ObjectReferenceCommandSet.InvokeMethodCommand);
        packet.setNextValueAsObjectID(receiverObjectID);
        packet.setNextValueAsThreadID(targetThreadID);
        packet.setNextValueAsClassID(testTypeID);  // TestClass type ID.
        packet.setNextValueAsMethodID(targetMethodID);  // testDefaultMethod method ID.
        packet.setNextValueAsInt(1);  // 1 argument.
        packet.setNextValueAsValue(throwValue);
        packet.setNextValueAsInt(0);  // invoke options.
        logWriter.println(" Send ObjectReference.InvokeMethod " +
                ((shouldThrow) ? "with" : "without") + " exception");
        reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ObjectReference::InvokeMethod command");

        Value returnValue = reply.getNextValueAsValue();
        assertNotNull("Returned value is null", returnValue);
        if (shouldThrow) {
            assertEquals("Invalid returned value,", 0, returnValue.getIntValue());
            logWriter.println(" ObjectReference.InvokeMethod: returnValue.getIntValue()="
                    + returnValue.getIntValue());

            // Check that some exception was thrown.
            TaggedObject exception = reply.getNextValueAsTaggedObject();
            assertNotNull("Returned exception is null", exception);
            assertTrue("Invalid exception object ID:<" + exception.objectID + ">",
                    exception.objectID != 0);
            assertEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag
                    , JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG)
                    , JDWPConstants.Tag.getName(exception.tag));
            logWriter.println(" InterfaceType.InvokeMethod: exception.tag="
                    + exception.tag + " exception.objectID=" + exception.objectID);
        } else {
            assertEquals("Invalid returned value,", 123, returnValue.getIntValue());
            logWriter.println(" ObjectReference.InvokeMethod: returnValue.getIntValue()="
                    + returnValue.getIntValue());

            TaggedObject exception = reply.getNextValueAsTaggedObject();
            assertNotNull("Returned exception is null", exception);
            assertTrue("Invalid exception object ID:<" + exception.objectID + ">",
                    exception.objectID == 0);
            assertEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag
                    , JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG)
                    , JDWPConstants.Tag.getName(exception.tag));
            logWriter.println(" ObjectReference.InvokeMethod: exception.tag="
                    + exception.tag + " exception.objectID=" + exception.objectID);
        }

        //  Let's resume application
        debuggeeWrapper.vmMirror.resume();

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }

    public void testInvokeMethodFromInterfaceThrow() {
        testInvokeMethod(true);
    }

    public void testInvokeMethodFromInterfaceNoThrow() {
        testInvokeMethod(false);
    }
}

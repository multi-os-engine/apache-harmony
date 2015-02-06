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

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP unit test for ObjectReference.InvokeMethod command.
 */
public class InvokeMethod004Test extends JDWPSyncTestCase {
    protected String getDebuggeeClassName() {
        return InvokeMethod004Debuggee.class.getName();
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command.
     * <BR>The test first starts the debuggee, requests METHOD_ENTRY event so
     * the application suspends on first invoke.
     * <BR>Then sends ObjectReference.InvokeMethod command to invoke method
     * Object.toString on an instance of a subclass of Object overriding the
     * method toString.
     * <BR>Checks that returned value is expected string value and returned
     * exception object is null.
     * <BR>Finally resumes the application.
     */
    public void testInvokeMethod_toString() {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Get debuggee class ID.
        String debuggeeClassSig = getDebuggeeClassSignature();
        long debuggeeTypeID = debuggeeWrapper.vmMirror.getClassID(debuggeeClassSig);
        assertTrue("Failed to find debuggee class", debuggeeTypeID != 0);
        
        int breakpointID = 
                debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(debuggeeTypeID, "execMethod");

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        
        long targetThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointID);

        //  Now we're suspended, clear event request.
        debuggeeWrapper.vmMirror.clearBreakpoint(breakpointID);
        
        // Suspend target thread another time so its supsend count > 1
        debuggeeWrapper.vmMirror.suspendThread(targetThreadID);
        int suspend_count = getThreadSuspendCount(targetThreadID);
        assertEquals("Invalid suspend count", 2, suspend_count);

        // Load value of the invoke's receiver.
        long receiverObjectID = getReceiverObjectID(debuggeeTypeID);
        assertTrue("Field is null", receiverObjectID != 0);

        // Get test class ID.
        String testClassSig = getClassSignature(InvokeMethod004Debuggee.TestClass.class);
        long testTypeID = debuggeeWrapper.vmMirror.getClassID(testClassSig);
        assertTrue("Failed to find test class", testTypeID != 0);

        // Get java.lang.Object class ID.
        long objectTypeID = debuggeeWrapper.vmMirror.getClassID("Ljava/lang/Object;");
        assertTrue("Failed to find java.lang.Object class", objectTypeID != 0);

        // Get java.lang.Object.toString method ID.
        long targetMethodID = debuggeeWrapper.vmMirror.getMethodID(objectTypeID, "toString");
        assertTrue("Failed to find method", targetMethodID != 0);

        // Invoke method.
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ObjectReferenceCommandSet.CommandSetID,
                JDWPCommands.ObjectReferenceCommandSet.InvokeMethodCommand);
        packet.setNextValueAsObjectID(receiverObjectID);
        packet.setNextValueAsThreadID(targetThreadID);
        packet.setNextValueAsClassID(testTypeID);  // TestClass type ID.
        packet.setNextValueAsMethodID(targetMethodID);  // Object.toString method ID.
        packet.setNextValueAsInt(0);  // no arguments.
        packet.setNextValueAsInt(0);  // invoke options.
        logWriter.println(" Send ObjectReference.InvokeMethod without Exception");
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ObjectReference::InvokeMethod command");

        Value returnValue = reply.getNextValueAsValue();
        assertNotNull("Returned value is null", returnValue);
        assertEquals("Returned value tag is incorrect", JDWPConstants.Tag.STRING_TAG, 
                returnValue.getTag());
        long stringID = returnValue.getLongValue();
        assertTrue("Returned string is null", stringID != 0);

        String returnedString = debuggeeWrapper.vmMirror.getStringValue(stringID);
        assertEquals("Invalid returned value,", "TestClass.toString()", returnedString);
        logWriter.println(" ObjectReference.InvokeMethod: returnedString=\""
                + returnedString + "\"");

        TaggedObject exception = reply.getNextValueAsTaggedObject();
        assertNotNull("Returned exception is null", exception);
        assertTrue("Invalid exception object ID:<" + exception.objectID + ">", exception.objectID == 0);
        assertEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag
                , JDWPConstants.Tag.getName(JDWPConstants.Tag.OBJECT_TAG)
                , JDWPConstants.Tag.getName(exception.tag));
        logWriter.println(" ClassType.InvokeMethod: exception.tag="
                + exception.tag + " exception.objectID=" + exception.objectID);
        assertAllDataRead(reply);
        
        suspend_count = getThreadSuspendCount(targetThreadID);
        assertEquals("Invalid suspend count", 1, suspend_count);

        //  Let's resume application
        debuggeeWrapper.vmMirror.resume();

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }
    
    private long getReceiverObjectID(long debuggeeTypeID) {
        long receiverFieldID = debuggeeWrapper.vmMirror.getFieldID(debuggeeTypeID, "invokeReceiver");
        assertTrue("Failed to find receiver field", receiverFieldID != 0);

        logWriter.println(" Send ReferenceType.GetValues");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.GetValuesCommand);
        packet.setNextValueAsReferenceTypeID(debuggeeTypeID);
        packet.setNextValueAsInt(1);  // looking for one field.
        packet.setNextValueAsFieldID(receiverFieldID);  // the field we're looking for.
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ReferenceType::GetValues command");
        int valuesCount = reply.getNextValueAsInt();  // number of field values.
        Value fieldValue = reply.getNextValueAsValue();
        assertAllDataRead(reply);
        assertEquals("Not the right number of values", 1, valuesCount);
        assertNotNull("Received null value", fieldValue);
        assertEquals("Expected an object value", JDWPConstants.Tag.OBJECT_TAG, fieldValue.getTag());
        long receiverObjectID = fieldValue.getLongValue();
        assertTrue("Field is null", receiverObjectID != 0);
        
        return receiverObjectID;
    }

    private int getThreadSuspendCount(long targetThreadID) {
        logWriter.println(" Send ThreadReference.SuspendCount");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.SuspendCountCommand);
        packet.setNextValueAsThreadID(targetThreadID);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ThreadReference::SuspendCount command");
        int suspendCount = reply.getNextValueAsInt();
        assertAllDataRead(reply);
        return suspendCount;
    }
}

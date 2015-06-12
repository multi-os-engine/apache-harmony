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
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPInvokeMethodTestCase;

/**
 * JDWP unit test for ObjectReference.InvokeMethod command.
 */
public class InvokeMethodReturnTest extends JDWPInvokeMethodTestCase {

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning boolean value.
     */
    public void testInvokeInstanceMethodBoolean() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.BOOLEAN_TAG, new Value(false));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.BOOLEAN_TAG, new Value(true));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning byte value.
     */
    public void testInvokeInstanceMethodByte() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.BYTE_TAG, new Value((byte) 0));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.BYTE_TAG, new Value(Byte.MAX_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.BYTE_TAG, new Value(Byte.MIN_VALUE));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning char value.
     */
    public void testInvokeInstanceMethodChar() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.CHAR_TAG, new Value((char) 0));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.CHAR_TAG, new Value(Character.MAX_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.CHAR_TAG, new Value(Character.MIN_VALUE));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning short value.
     */
    public void testInvokeInstanceMethodShort() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.SHORT_TAG, new Value((short) 0));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.SHORT_TAG, new Value(Short.MAX_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.SHORT_TAG, new Value(Short.MIN_VALUE));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning int value.
     */
    public void testInvokeInstanceMethodInt() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.INT_TAG, new Value(0));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.INT_TAG, new Value(Integer.MAX_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.INT_TAG, new Value(Integer.MIN_VALUE));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning float value.
     */
    public void testInvokeInstanceMethodFloat() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(0.0f));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(Float.MAX_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(Float.MIN_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG,
                                    new Value(Float.POSITIVE_INFINITY));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG,
                                    new Value(Float.NEGATIVE_INFINITY));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning long value.
     */
    public void testInvokeInstanceMethodLong() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.LONG_TAG, new Value(0L));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.LONG_TAG, new Value(Long.MAX_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.LONG_TAG, new Value(Long.MIN_VALUE));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning double value.
     */
    public void testInvokeInstanceMethodDouble() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG, new Value(0.00));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG, new Value(Double.MAX_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG, new Value(Double.MIN_VALUE));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG,
                                    new Value(Double.POSITIVE_INFINITY));
        runInstanceInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG,
                                    new Value(Double.NEGATIVE_INFINITY));
    }

    /**
     * This testcase exercises ObjectReference.InvokeMethod command for method
     * returning Object value.
     */
    public void testInvokeStaticMethodObject() {
        runInstanceInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                    new Value(JDWPConstants.Tag.OBJECT_TAG, 0));

        // Array
        long arrayObjectID = getStaticFieldObjectId(ARRAY_OBJECT_FIELD_NAME);
        runInstanceInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                    new Value(JDWPConstants.Tag.ARRAY_TAG, arrayObjectID));

        // java.lang.String
        long stringObjectID = getStaticFieldObjectId(STRING_OBJECT_FIELD_NAME);
        runInstanceInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                    new Value(JDWPConstants.Tag.STRING_TAG, stringObjectID));

        // java.lang.Thread
        long threadObjectID = getStaticFieldObjectId(THREAD_OBJECT_FIELD_NAME);
        runInstanceInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                    new Value(JDWPConstants.Tag.THREAD_TAG, threadObjectID));

        // java.lang.ThreadGroup
        long threadGroupObjectID = getStaticFieldObjectId(THREAD_GROUP_OBJECT_FIELD_NAME);
        runInstanceInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                    new Value(JDWPConstants.Tag.THREAD_GROUP_TAG,
                                              threadGroupObjectID));

        // java.lang.Class
        long classObjectID = getStaticFieldObjectId(CLASS_OBJECT_FIELD_NAME);
        runInstanceInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                    new Value(JDWPConstants.Tag.CLASS_OBJECT_TAG, classObjectID));

        // java.lang.ClassLoader
        long classLoaderObjectID = getStaticFieldObjectId(CLASS_LOADER_OBJECT_FIELD_NAME);
        runInstanceInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                    new Value(JDWPConstants.Tag.CLASS_LOADER_TAG,
                                              classLoaderObjectID));
    }

    private void runInstanceInvokeMethodTest(byte tag, Value expectedValue) {
        runInvokeMethodTest(tag, false, expectedValue);
    }

    @Override
    protected ReplyPacket invokeMethod(long typeID, long targetThreadID, long targetMethodID,
            boolean withException) {

        // Get debuggee instance.
        long topFrameID = getTopFrameID(targetThreadID);
        long receiverID = debuggeeWrapper.vmMirror.getThisObject(targetThreadID, topFrameID);
        assertTrue("Invalid receiver", receiverID != 0);

        final int invoke_options = 0;  // invoke options: resume all threads.

        CommandPacket packet = new CommandPacket(
                JDWPCommands.ObjectReferenceCommandSet.CommandSetID,
                JDWPCommands.ObjectReferenceCommandSet.InvokeMethodCommand);
        packet.setNextValueAsObjectID(receiverID);
        packet.setNextValueAsThreadID(targetThreadID);
        packet.setNextValueAsClassID(typeID);
        packet.setNextValueAsMethodID(targetMethodID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsValue(new Value(withException));
        packet.setNextValueAsInt(invoke_options);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ObjectReference.InvokeMethod");
        return reply;
    }

    private long getTopFrameID(long targetThreadID) {
        ReplyPacket reply = debuggeeWrapper.vmMirror.getThreadFrames(targetThreadID, 0, 1);
        int framesCount = reply.getNextValueAsInt();
        assertEquals("Invalid frame count", 1, framesCount);
        long frameID = reply.getNextValueAsFrameID();
        reply.getNextValueAsLocation(); // unused
        assertAllDataRead(reply);
        return frameID;
    }
}

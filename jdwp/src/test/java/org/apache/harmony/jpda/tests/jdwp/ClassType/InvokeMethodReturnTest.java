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
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPInvokeMethodTestCase;

/**
 * JDWP unit test for ClassType.InvokeMethod command.
 */
public class InvokeMethodReturnTest extends JDWPInvokeMethodTestCase {

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning boolean value.
     */
    public void testInvokeStaticMethodBoolean() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.BOOLEAN_TAG, new Value(false));
        runStaticInvokeMethodTest(JDWPConstants.Tag.BOOLEAN_TAG, new Value(true));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning byte value.
     */
    public void testInvokeStaticMethodByte() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.BYTE_TAG, new Value((byte) 0));
        runStaticInvokeMethodTest(JDWPConstants.Tag.BYTE_TAG, new Value(Byte.MAX_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.BYTE_TAG, new Value(Byte.MIN_VALUE));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning char value.
     */
    public void testInvokeStaticMethodChar() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.CHAR_TAG, new Value((char) 0));
        runStaticInvokeMethodTest(JDWPConstants.Tag.CHAR_TAG, new Value(Character.MAX_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.CHAR_TAG, new Value(Character.MIN_VALUE));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning short value.
     */
    public void testInvokeStaticMethodShort() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.SHORT_TAG, new Value((short) 0));
        runStaticInvokeMethodTest(JDWPConstants.Tag.SHORT_TAG, new Value(Short.MAX_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.SHORT_TAG, new Value(Short.MIN_VALUE));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning int value.
     */
    public void testInvokeStaticMethodInt() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.INT_TAG, new Value(0));
        runStaticInvokeMethodTest(JDWPConstants.Tag.INT_TAG, new Value(Integer.MAX_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.INT_TAG, new Value(Integer.MIN_VALUE));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning float value.
     */
    public void testInvokeStaticMethodFloat() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(0.0f));
        runStaticInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(Float.MAX_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(Float.MIN_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(Float.POSITIVE_INFINITY));
        runStaticInvokeMethodTest(JDWPConstants.Tag.FLOAT_TAG, new Value(Float.NEGATIVE_INFINITY));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning long value.
     */
    public void testInvokeStaticMethodLong() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.LONG_TAG, new Value(0L));
        runStaticInvokeMethodTest(JDWPConstants.Tag.LONG_TAG, new Value(Long.MAX_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.LONG_TAG, new Value(Long.MIN_VALUE));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning double value.
     */
    public void testInvokeStaticMethodDouble() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG, new Value(0.00));
        runStaticInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG, new Value(Double.MAX_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG, new Value(Double.MIN_VALUE));
        runStaticInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG,
                                  new Value(Double.POSITIVE_INFINITY));
        runStaticInvokeMethodTest(JDWPConstants.Tag.DOUBLE_TAG,
                                  new Value(Double.NEGATIVE_INFINITY));
    }

    /**
     * This testcase exercises ClassType.InvokeMethod command for method
     * returning Object value.
     */
    public void testInvokeStaticMethodObject() {
        runStaticInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                  new Value(JDWPConstants.Tag.OBJECT_TAG, 0));

        // Array
        long arrayObjectID = getStaticFieldObjectId(ARRAY_OBJECT_FIELD_NAME);
        runStaticInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                  new Value(JDWPConstants.Tag.ARRAY_TAG, arrayObjectID));

        // java.lang.String
        long stringObjectID = getStaticFieldObjectId(STRING_OBJECT_FIELD_NAME);
        runStaticInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                  new Value(JDWPConstants.Tag.STRING_TAG, stringObjectID));

        // java.lang.Thread
        long threadObjectID = getStaticFieldObjectId(THREAD_OBJECT_FIELD_NAME);
        runStaticInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                  new Value(JDWPConstants.Tag.THREAD_TAG, threadObjectID));

        // java.lang.ThreadGroup
        long threadGroupObjectID = getStaticFieldObjectId(THREAD_GROUP_OBJECT_FIELD_NAME);
        runStaticInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                  new Value(JDWPConstants.Tag.THREAD_GROUP_TAG,
                                            threadGroupObjectID));

        // java.lang.Class
        long classObjectID = getStaticFieldObjectId(CLASS_OBJECT_FIELD_NAME);
        runStaticInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                  new Value(JDWPConstants.Tag.CLASS_OBJECT_TAG, classObjectID));

        // java.lang.ClassLoader
        long classLoaderObjectID = getStaticFieldObjectId(CLASS_LOADER_OBJECT_FIELD_NAME);
        runStaticInvokeMethodTest(JDWPConstants.Tag.OBJECT_TAG,
                                  new Value(JDWPConstants.Tag.CLASS_LOADER_TAG,
                                            classLoaderObjectID));
    }

    private void runStaticInvokeMethodTest(byte tag, Value expectedValue) {
        runInvokeMethodTest(tag, true, expectedValue);
    }

    @Override
    protected ReplyPacket invokeMethod(long typeID, long targetThreadID, long targetMethodID,
            boolean withException) {
        final int invoke_options = 0;  // invoke options: resume all threads.

        CommandPacket packet = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
        packet.setNextValueAsClassID(typeID);
        packet.setNextValueAsThreadID(targetThreadID);
        packet.setNextValueAsMethodID(targetMethodID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsValue(new Value(withException));
        packet.setNextValueAsInt(invoke_options);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ClassType.InvokeMethod");
        return reply;
    }
}

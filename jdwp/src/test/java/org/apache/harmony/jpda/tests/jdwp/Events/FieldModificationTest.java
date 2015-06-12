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

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP Unit test for FIELD_MODIFICATION event.
 */
public class FieldModificationTest extends JDWPFieldEventTestCase {
    /**
     * Tests FIELD_MODIFICATION event on a boolean instance field set to false.
     */
    public void testFieldModificationEvent_InstanceBooleanField_False() {
        runFieldModificationEventTest(JDWPConstants.Tag.BOOLEAN_TAG, false, new Value(false));
    }

    /**
     * Tests FIELD_MODIFICATION event on a boolean instance field set to true.
     */
    public void testFieldModificationEvent_InstanceBooleanField_True() {
        runFieldModificationEventTest(JDWPConstants.Tag.BOOLEAN_TAG, false, new Value(true));
    }

    /**
     * Tests FIELD_MODIFICATION event on a byte instance field set to 0.
     */
    public void testFieldModificationEvent_InstanceByteField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.BYTE_TAG, false, new Value((byte) 0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a byte instance field set to Byte.MAX_VALUE.
     */
    public void testFieldModificationEvent_InstanceByteField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.BYTE_TAG, false, new Value(Byte.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a byte instance field set to Byte.MIN_VALUE.
     */
    public void testFieldModificationEvent_InstanceByteField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.BYTE_TAG, false, new Value(Byte.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a char instance field set to 0.
     */
    public void testFieldModificationEvent_InstanceCharField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.CHAR_TAG, false, new Value((char) 0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a char instance field set to Character.MAX_VALUE.
     */
    public void testFieldModificationEvent_InstanceCharField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.CHAR_TAG, false,
                new Value(Character.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a char instance field set to Character.MIN_VALUE.
     */
    public void testFieldModificationEvent_InstanceCharField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.CHAR_TAG, false,
                new Value(Character.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a short instance field set to 0.
     */
    public void testFieldModificationEvent_InstanceShortField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.SHORT_TAG, false, new Value((short) 0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a short instance field set to Short.MAX_VALUE.
     */
    public void testFieldModificationEvent_InstanceShortField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.SHORT_TAG, false,
                new Value(Short.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a short instance field set to Short.MIN_VALUE.
     */
    public void testFieldModificationEvent_InstanceShortField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.SHORT_TAG, false,
                new Value(Short.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on an int instance field set to 0.
     */
    public void testFieldModificationEvent_InstanceIntField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.INT_TAG, false, new Value(0));
    }

    /**
     * Tests FIELD_MODIFICATION event on an int instance field set to Integer.MAX_VALUE.
     */
    public void testFieldModificationEvent_InstanceIntField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.INT_TAG, false,
                new Value(Integer.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on an int instance field set to Integer.MIN_VALUE.
     */
    public void testFieldModificationEvent_InstanceIntField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.INT_TAG, false,
                new Value(Integer.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float instance field set to 0.
     */
    public void testFieldModificationEvent_InstanceFloatField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, false, new Value(0.0f));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float instance field set to Float.MAX_VALUE.
     */
    public void testFieldModificationEvent_InstanceFloatField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, false,
                new Value(Float.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float instance field set to Float.MIN_VALUE.
     */
    public void testFieldModificationEvent_InstanceFloatField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, false,
                new Value(Float.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float instance field set to Float.POSITIVE_INFINITY.
     */
    public void testFieldModificationEvent_InstanceFloatField_PositiveInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, false,
                new Value(Float.POSITIVE_INFINITY));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float instance field set to Float.NEGATIVE_INFINITY.
     */
    public void testFieldModificationEvent_InstanceFloatField_NegativeInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, false,
                new Value(Float.NEGATIVE_INFINITY));
    }

    /**
     * Tests FIELD_MODIFICATION event on a long instance field set to 0.
     */
    public void testFieldModificationEvent_InstanceLongField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.LONG_TAG, false, new Value(0L));
    }

    /**
     * Tests FIELD_MODIFICATION event on a long instance field set to Long.MAX_VALUE.
     */
    public void testFieldModificationEvent_InstanceLongField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.LONG_TAG, false,
                new Value(Long.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a long instance field set to Long.MIN_VALUE.
     */
    public void testFieldModificationEvent_InstanceLongField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.LONG_TAG, false,
                new Value(Long.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double instance field set to 0.
     */
    public void testFieldModificationEvent_InstanceDoubleField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, false, new Value(0.0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double instance field set to Double.MAX_VALUE.
     */
    public void testFieldModificationEvent_InstanceDoubleField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, false,
                new Value(Double.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double instance field set to Double.MIN_VALUE.
     */
    public void testFieldModificationEvent_InstanceDoubleField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, false,
                new Value(Double.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double instance field set to Double.POSITIVE_INFINITY.
     */
    public void testFieldModificationEvent_InstanceDoubleField_PositiveInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, false,
                new Value(Double.POSITIVE_INFINITY));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double instance field set to Double.NEGATIVE_INFINITY.
     */
    public void testFieldModificationEvent_InstanceDoubleField_NegativeInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, false,
                new Value(Double.NEGATIVE_INFINITY));
    }

    // TODO check each object kind
    /**
     * Tests FIELD_MODIFICATION event on an Object instance field set to null.
     */
    public void testFieldModificationEvent_InstanceObjectField_Null() {
        runFieldModificationEventTest(JDWPConstants.Tag.OBJECT_TAG, false,
                new Value(JDWPConstants.Tag.OBJECT_TAG, 0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a boolean static field set to false.
     */
    public void testFieldModificationEvent_StaticBooleanField_False() {
        runFieldModificationEventTest(JDWPConstants.Tag.BOOLEAN_TAG, true, new Value(false));
    }

    /**
     * Tests FIELD_MODIFICATION event on a boolean static field set to true.
     */
    public void testFieldModificationEvent_StaticBooleanField_True() {
        runFieldModificationEventTest(JDWPConstants.Tag.BOOLEAN_TAG, true, new Value(true));
    }

    /**
     * Tests FIELD_MODIFICATION event on a byte static field set to 0.
     */
    public void testFieldModificationEvent_StaticByteField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.BYTE_TAG, true, new Value((byte) 0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a byte static field set to Byte.MAX_VALUE.
     */
    public void testFieldModificationEvent_StaticByteField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.BYTE_TAG, true, new Value(Byte.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a byte static field set to Byte.MIN_VALUE.
     */
    public void testFieldModificationEvent_StaticByteField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.BYTE_TAG, true, new Value(Byte.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a char static field set to 0.
     */
    public void testFieldModificationEvent_StaticCharField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.CHAR_TAG, true, new Value((char) 0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a char static field set to Character.MAX_VALUE.
     */
    public void testFieldModificationEvent_StaticCharField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.CHAR_TAG, true,
                new Value(Character.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a char static field set to Character.MIN_VALUE.
     */
    public void testFieldModificationEvent_StaticCharField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.CHAR_TAG, true,
                new Value(Character.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a short static field set to 0.
     */
    public void testFieldModificationEvent_StaticShortField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.SHORT_TAG, true, new Value((short) 0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a short static field set to Short.MAX_VALUE.
     */
    public void testFieldModificationEvent_StaticShortField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.SHORT_TAG, true,
                new Value(Short.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a short static field set to Short.MIN_VALUE.
     */
    public void testFieldModificationEvent_StaticShortField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.SHORT_TAG, true,
                new Value(Short.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on an int static field set to 0.
     */
    public void testFieldModificationEvent_StaticIntField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.INT_TAG, true, new Value(0));
    }

    /**
     * Tests FIELD_MODIFICATION event on an int static field set to Integer.MAX_VALUE.
     */
    public void testFieldModificationEvent_StaticIntField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.INT_TAG, true,
                new Value(Integer.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on an int static field set to Integer.MIN_VALUE.
     */
    public void testFieldModificationEvent_StaticIntField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.INT_TAG, true,
                new Value(Integer.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float static field set to 0.
     */
    public void testFieldModificationEvent_StaticFloatField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, true, new Value(0.0f));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float static field set to Float.MAX_VALUE.
     */
    public void testFieldModificationEvent_StaticFloatField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, true,
                new Value(Float.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float static field set to Float.MIN_VALUE.
     */
    public void testFieldModificationEvent_StaticFloatField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, true,
                new Value(Float.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float static field set to Float.POSITIVE_INFINITY.
     */
    public void testFieldModificationEvent_StaticFloatField_PositiveInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, true,
                new Value(Float.POSITIVE_INFINITY));
    }

    /**
     * Tests FIELD_MODIFICATION event on a float static field set to Float.NEGATIVE_INFINITY.
     */
    public void testFieldModificationEvent_StaticFloatField_NegativeInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.FLOAT_TAG, true,
                new Value(Float.NEGATIVE_INFINITY));
    }

    /**
     * Tests FIELD_MODIFICATION event on a long static field set to 0.
     */
    public void testFieldModificationEvent_StaticLongField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.LONG_TAG, true, new Value(0L));
    }

    /**
     * Tests FIELD_MODIFICATION event on a long static field set to Long.MAX_VALUE.
     */
    public void testFieldModificationEvent_StaticLongField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.LONG_TAG, true,
                new Value(Long.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a long static field set to Long.MIN_VALUE.
     */
    public void testFieldModificationEvent_StaticLongField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.LONG_TAG, true,
                new Value(Long.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double static field set to 0.
     */
    public void testFieldModificationEvent_StaticDoubleField_Zero() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, true, new Value(0.0));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double static field set to Double.MAX_VALUE.
     */
    public void testFieldModificationEvent_StaticDoubleField_MaxValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, true,
                new Value(Double.MAX_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double static field set to Double.MIN_VALUE.
     */
    public void testFieldModificationEvent_StaticDoubleField_MinValue() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, true,
                new Value(Double.MIN_VALUE));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double static field set to Double.POSITIVE_INFINITY.
     */
    public void testFieldModificationEvent_StaticDoubleField_PositiveInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, true,
                new Value(Double.POSITIVE_INFINITY));
    }

    /**
     * Tests FIELD_MODIFICATION event on a double static field set to Double.NEGATIVE_INFINITY.
     */
    public void testFieldModificationEvent_StaticDoubleField_NegativeInfinity() {
        runFieldModificationEventTest(JDWPConstants.Tag.DOUBLE_TAG, true,
                new Value(Double.NEGATIVE_INFINITY));
    }

    // TODO check each object kind
    /**
     * Tests FIELD_MODIFICATION event on an Object static field set to null.
     */
    public void testFieldModificationEvent_StaticObjectField_Null() {
        runFieldModificationEventTest(JDWPConstants.Tag.OBJECT_TAG, true,
                new Value(JDWPConstants.Tag.OBJECT_TAG, 0));
    }

    /**
     * Tests FIELD_MODIFICATION event.
     *
     * <p>It runs {@link FieldDebuggee} and set a FIELD_MODIFICATION event on a tested field
     * (described by the given arguments).</p>
     *
     * <p>When the event occurs, it check:
     * <ul>
     * <li>this is the expected FIELD_MODIFICATION event</li>
     * <li>the event thread is properly suspended</li>
     * <li>for static field, the receiver is null</li>
     * <li>for instance field, the receiver is non-null and is of the debuggee type.</li>
     * <li>the value set into the field is expected.</li>
     * </ul>
     * </p>
     *
     * @param fieldTag
     *          the type of the tested field. This is used to compute the tested field's name.
     * @param isStatic
     *          true if the tested thread is static, false otherwise.
     * @param expectedValue
     *          the value set into the field at the time of the event.
     */
    private void runFieldModificationEventTest(byte fieldTag, boolean isStatic,
            Value expectedValue) {
        logWriter.println(getName() + " started");

        //check capability, relevant for this test
        logWriter.println("=> Check capability: canWatchFieldModification");
        debuggeeWrapper.vmMirror.capabilities();
        boolean isCapability = debuggeeWrapper.vmMirror.targetVMCapabilities.canWatchFieldModification;
        if (!isCapability) {
            logWriter.println("##WARNING: this VM doesn't possess capability: canWatchFieldModification");
            return;
        }

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        String classSignature = getDebuggeeClassSignature();
        String fieldName = getTestFieldName(fieldTag, isStatic);
        int requestID = setFieldModification(classSignature, fieldName);

        // We set the EXPECTED_<TYPE>_FIELD with the expected value. Then, when the tested field is
        // set with this value in the debugger, the FIELD_MODIFICATION event must contain it.
        String expectedValueFieldName = getExpectedValueFieldName(fieldTag);
        setExpectedFieldValue(expectedValueFieldName, expectedValue);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // TODO receiveCertainEvent(JDWPConstants.EventKind.FIELD_MODIFICATION) to ignore other one
        // (like THREAD_START and THREAD_END).
        EventPacket event = debuggeeWrapper.vmMirror.receiveEvent();
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);

        // assert that event is the expected one
        assertEquals("Invalid number of events,", 1, parsedEvents.length);
        assertEquals("Invalid event kind,",
                JDWPConstants.EventKind.FIELD_MODIFICATION,
                parsedEvents[0].getEventKind(),
                JDWPConstants.EventKind.getName(JDWPConstants.EventKind.FIELD_MODIFICATION),
                JDWPConstants.EventKind.getName(parsedEvents[0].getEventKind()));
        assertEquals("Invalid request id,", requestID, parsedEvents[0].getRequestID());

        ParsedEvent.Event_FIELD_MODIFICATION fieldModificationEvent =
                (ParsedEvent.Event_FIELD_MODIFICATION)parsedEvents[0];

        // Check thread's state.
        long eventThreadID = fieldModificationEvent.getThreadID();
        checkThreadState(eventThreadID, JDWPConstants.ThreadStatus.RUNNING,
                JDWPConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED);

        checkObjectId(fieldModificationEvent.getObject(), isStatic);
        checkFieldValue(expectedValue, fieldModificationEvent.getValueToBe());

        logWriter.println(getName() + " done");
    }

    /**
     * Requests FIELD_MODIFICATION event for the given field.
     *
     * @param debuggeeClassSignature
     *          the signature of the debuggee class
     * @param fieldName
     *          the name of the tested field
     * @return a request ID
     */
    private int setFieldModification(String debuggeeClassSignature, String fieldName) {
        ReplyPacket reply = debuggeeWrapper.vmMirror.setFieldModification(debuggeeClassSignature,
                JDWPConstants.TypeTag.CLASS, fieldName);
        checkReplyPacket(reply, "Set FIELD_MODIFICATION event");
        return reply.getNextValueAsInt();
    }

    /**
     * Sets the debuggee's static field identified by the given name with the given value.
     *
     * @param value
     *          the new static field value
     */
    private void setExpectedFieldValue(String expectedValueFieldName, Value value) {
        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        long fieldID = checkField(classID, expectedValueFieldName);
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.SetValuesCommand);
        packet.setNextValueAsReferenceTypeID(classID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsFieldID(fieldID);
        packet.setNextValueAsUntaggedValue(value);

        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(replyPacket, "ClassType.SetValues");
    }

    /**
     * Checks the value of the FIELD_MODIFICATION event is expected.
     * @param expected
     *          the expected field value
     * @param actual
     *          the field value of the FIELD_MODIFICATION event
     */
    private void checkFieldValue(Value expected, Value actual) {
        assertNotNull("Invalid expected value", expected);
        assertNotNull("Invalid field modification value", actual);
        assertEquals("Unexpected field modification value", expected, actual);
    }
}

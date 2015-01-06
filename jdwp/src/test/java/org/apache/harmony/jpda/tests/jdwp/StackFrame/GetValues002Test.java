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

package org.apache.harmony.jpda.tests.jdwp.StackFrame;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for StackFrame.GetValues command.
 */
public class GetValues002Test extends JDWPStackFrameTestCase {
    @Override
    protected String getDebuggeeClassName() {
        return GetValues002Debuggee.class.getName();
    }

    /**
     * Tests we correctly read value of boolean variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Boolean() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Boolean", "breakpointBoolean",
                                                     "runBreakpointBoolean", "param",
                                                     JDWPConstants.Tag.BOOLEAN_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.BOOLEAN_PARAM_VALUE,
                             value.getBooleanValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of byte variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Byte() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Byte", "breakpointByte",
                                                     "runBreakpointByte", "param",
                                                     JDWPConstants.Tag.BYTE_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.BYTE_PARAM_VALUE,
                             value.getByteValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of char variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Char() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Char", "breakpointChar",
                                                     "runBreakpointChar", "param",
                                                     JDWPConstants.Tag.CHAR_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.CHAR_PARAM_VALUE,
                             value.getCharValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of short variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Short() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Short", "breakpointShort",
                                                     "runBreakpointShort", "param",
                                                     JDWPConstants.Tag.SHORT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.SHORT_PARAM_VALUE,
                             value.getShortValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of int variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Int() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Int", "breakpointInt",
                                                     "runBreakpointInt", "param",
                                                     JDWPConstants.Tag.INT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.INT_PARAM_VALUE,
                             value.getIntValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of long variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Long() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Long", "breakpointLong",
                                                     "runBreakpointLong", "param",
                                                     JDWPConstants.Tag.LONG_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.LONG_PARAM_VALUE,
                             value.getLongValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of float variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Float() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Float", "breakpointFloat",
                                                     "runBreakpointFloat", "param",
                                                     JDWPConstants.Tag.FLOAT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.FLOAT_PARAM_VALUE,
                             value.getFloatValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of double variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_Double() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Double", "breakpointDouble",
                                                     "runBreakpointDouble", "param",
                                                     JDWPConstants.Tag.DOUBLE_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.DOUBLE_PARAM_VALUE,
                             value.getDoubleValue());
            }
        };
        runTest(tester);
    }

    /**
     * Tests we correctly read value of java.lang.String variable in the stack.
     *
     * Refer to {@link #runTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues_String() {
        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        Value stringFieldValue = getStaticFieldValue(classID, "STRING_PARAM_VALUE");
        final long stringObjectID = stringFieldValue.getLongValue();

        GetValuesTester tester = new GetValuesTester("testGetValues_String", "breakpointString",
                                                     "runBreakpointString", "param",
                                                     JDWPConstants.Tag.STRING_TAG) {
            @Override
            public void checkValue(Value value) {
                long stringID = value.getLongValue();
                assertEquals("Invalid string object id", stringObjectID, stringID);

                String stringValue = getStringValue(stringID);
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             GetValues002Debuggee.STRING_PARAM_VALUE,
                             stringValue);
            }
        };
        runTest(tester);
    }

    /**
     * Returns the value of the static field identified by the given name in
     * the given class.
     *
     * @param classID
     *          the ID of the declaring class of the static field
     * @param fieldName
     *          the name of the static field in the class.
     */
    private Value getStaticFieldValue(long classID, String fieldName) {
        long fieldID = debuggeeWrapper.vmMirror.getFieldID(classID, fieldName);
        long[] fieldIDs = new long[]{ fieldID };
        Value[] fieldValues = debuggeeWrapper.vmMirror.getReferenceTypeValues(classID, fieldIDs);
        assertNotNull("No field values", fieldValues);
        assertEquals("Invalid field values count", fieldValues.length, 1);
        return fieldValues[0];
    }

    /**
     * An abstract class giving the configuration of the test and checking the
     * variable value retrieved from the stack during the test. It must be
     * subclassed to implement the way of checking the variable value.
     */
    static abstract class GetValuesTester {
        protected GetValuesTester(String testName, String breakpointMethodName,
                                  String testMethodName, String testVariableName,
                                  byte testVariableJdwpTag) {
            this.testName = testName;
            this.breakpointMethodName = breakpointMethodName;
            this.testMethodName = testMethodName;
            this.testVariableName = testVariableName;
            this.testVariableJdwpTag = testVariableJdwpTag;
        }

        /**
         * Checks if the given value is expected.
         * @param value
         *          the value of the tested variable retrieved from the stack.
         */
        public abstract void checkValue(Value value);

        /**
         * Returns the name of the test being run.
         */
        public String getTestName() {
            return testName;
        }

        /**
         * Returns the name of the method where a breakpoint is installed.
         * This method must be called by the method identified by the method
         * {@link getTestMethodName}.
         */
        public String getBreakpointMethodName() {
            return breakpointMethodName;
        }

        /**
         * Returns the name of the method calling the "breakpoint" method.
         */
        public String getTestMethodName() {
            return testMethodName;
        }

        /**
         * Returns the name of the variable (in the tested method) for which
         * we want to check the value.
         */
        public String getTestVariableName() {
            return testVariableName;
        }

        /**
         * Returns the JDWP tag of the tested variable. This matches the
         * declared type of the variable in the tested method.
         *
         * Note: it can be different from the value's tag we retrieve from
         * the stack in the case of Object variable (like String).
         */
        public byte getTestVariableJdwpTag() {
            return testVariableJdwpTag;
        }

        private final String testName;
        private final String breakpointMethodName;
        private final String testMethodName;
        private final String testVariableName;
        private final byte testVariableJdwpTag;
    }

    /**
     * Runs the test using the configuration from the given {@code tester}.
     *
     * Here is how the test works:
     * 1. Finds the method ID of the tested method.
     * 2. Retrieves the variable information (from the tested method's variable table)
     *    for the tested variable.
     * 3. Installs a breakpoint at the beginning of the "breakpoint" method called by the
     *    tested method. Then resumes the debuggee and waits for the breakpoint event.
     * 4. Finds the frame ID of the tested method and reads the value of the tested variable.
     * 5. Checks the variable value in the stack is the expected value.
     * 6. Removes breakpoint and resumes debuggee.
     *
     * @param tester
     *      an instance of {@link GetValuesTester} with the configuration of the test.
     */
    private void runTest(GetValuesTester tester) {
        final String testName = tester.getTestName();
        final String breakpointMethodName = tester.getBreakpointMethodName();
        final String testMethodName = tester.getTestMethodName();
        final String testVariableName = tester.getTestVariableName();
        final byte testVariableJdwpTag = tester.getTestVariableJdwpTag();

        logWriter.println("==> " + testName + " started...");

        // Wait for debuggee to start
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Get variable information.
        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        long testMethodID = getMethodID(classID, testMethodName);
        VarInfo testVarInfo = getVariableInfo(classID, testMethodID, testVariableName);
        assertNotNull(testVarInfo);

        // Install breakpoint.
        long breakpointRequestID = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(classID,
                breakpointMethodName);

        // Resume debuggee.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint.
        long breakpointThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointRequestID);

        // Find the frame for the tested method.
        FrameInfo testMethodFrame = getFrameInfo(breakpointThreadID, classID, testMethodID);
        assertNotNull("Can't find frame for method " + testMethodName, testMethodFrame);

        // Check the value is the expected one.
        Value value = getVariableValue(breakpointThreadID, testMethodFrame.frameID,
                                       testVarInfo.getSlot(), testVariableJdwpTag);
        tester.checkValue(value);

        // Remove the breakpoint
        debuggeeWrapper.vmMirror.clearBreakpoint((int) breakpointRequestID);

        // Resume debuggee.
        debuggeeWrapper.vmMirror.resume();

        logWriter.println("==> " + testName + " OK.");
    }

    /**
     * Return the {@link VarInfo} of the given variable in the given method.
     *
     * @param classID
     *          the ID of the declaring class of the method
     * @param methodID
     *          the ID of the method
     * @param variableName
     *          the name of the variable we look for
     */
    private VarInfo getVariableInfo(long classID, long methodID, String variableName) {
        VarInfo[] variables = jdwpGetVariableTable(classID, methodID);
        for (VarInfo variable : variables) {
            if (variable.name.equals(variableName)) {
                return variable;
            }
        }
        return null;
    }

    /**
     * Returns the {@link FrameInfo} of the most recent frame matching the given method.
     *
     * @param threadID
     *          the ID of the thread where to look for the frame
     * @param classID
     *          the ID of the declaring class of the method
     * @param methodID
     *          the ID of the method
     */
    private FrameInfo getFrameInfo(long threadID, long classID, long methodID) {
        int frameCount = jdwpGetFrameCount(threadID);

        // There should be at least 2 frames: the breakpoint method and its caller.
        assertTrue("Not enough frames", frameCount > 2);

        FrameInfo[] frames = jdwpGetFrames(threadID, 0, frameCount);
        for (FrameInfo frameInfo : frames) {
            if (frameInfo.location.classID == classID &&
                    frameInfo.location.methodID == methodID) {
                return frameInfo;
            }
        }
        return null;
    }

    /**
     * Returns the value of a local variable in the stack.
     *
     * @param threadID
     *          the ID of the thread of the stack
     * @param frameID
     *          the ID of the frame of the stack
     * @param slot
     *          the slot of the variable in the stack
     * @param tag
     *          the type of the value
     */
    private Value getVariableValue(long threadID, long frameID, int slot, byte tag) {
        logWriter.println("Request value for slot " + slot +
                          " (tag " + JDWPConstants.Tag.getName(tag) +
                          ") in frame " + frameID + " of thread " + threadID +
                          " with a StackFrame::GetValues command...");

        // Send StackFrame::GetValues command.
        CommandPacket packet = new CommandPacket(
                JDWPCommands.StackFrameCommandSet.CommandSetID,
                JDWPCommands.StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsFrameID(frameID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsInt(slot);
        packet.setNextValueAsByte(tag);

        // Check reply has no error.
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "StackFrame::GetValues command");

        // Check we have 1 value.
        int numberOfValues = reply.getNextValueAsInt();
        assertEquals("Incorrect number of values", 1, numberOfValues);

        // Check the value tag is correct.
        Value value = reply.getNextValueAsValue();
        assertEquals("Invalid value tag", tag, value.getTag());

        assertAllDataRead(reply);
        return value;
    }
}

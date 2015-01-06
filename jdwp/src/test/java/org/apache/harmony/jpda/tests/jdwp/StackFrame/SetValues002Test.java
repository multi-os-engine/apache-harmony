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
 * JDWP Unit test for StackFrame.SetValues command.
 */
public class SetValues002Test extends JDWPStackFrameAccessTest {
    /**
     * Tests we correctly read value of boolean variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues001_Boolean() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Boolean", "breakpointBoolean",
                                                     "runBreakpointBoolean", "param",
                                                     JDWPConstants.Tag.BOOLEAN_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.BOOLEAN_PARAM_VALUE_TO_SET,
                             value.getBooleanValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.BOOLEAN_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of byte variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues002_Byte() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Byte", "breakpointByte",
                                                     "runBreakpointByte", "param",
                                                     JDWPConstants.Tag.BYTE_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.BYTE_PARAM_VALUE_TO_SET,
                             value.getByteValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.BYTE_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of char variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues003_Char() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Char", "breakpointChar",
                                                     "runBreakpointChar", "param",
                                                     JDWPConstants.Tag.CHAR_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.CHAR_PARAM_VALUE_TO_SET,
                             value.getCharValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.CHAR_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of short variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues004_Short() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Short", "breakpointShort",
                                                     "runBreakpointShort", "param",
                                                     JDWPConstants.Tag.SHORT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.SHORT_PARAM_VALUE_TO_SET,
                             value.getShortValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.SHORT_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of int variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues005_Int() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Int", "breakpointInt",
                                                     "runBreakpointInt", "param",
                                                     JDWPConstants.Tag.INT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.INT_PARAM_VALUE_TO_SET,
                             value.getIntValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.INT_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of long variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues006_Long() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Long", "breakpointLong",
                                                     "runBreakpointLong", "param",
                                                     JDWPConstants.Tag.LONG_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.LONG_PARAM_VALUE_TO_SET,
                             value.getLongValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.LONG_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of float variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues007_Float() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Float", "breakpointFloat",
                                                     "runBreakpointFloat", "param",
                                                     JDWPConstants.Tag.FLOAT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.FLOAT_PARAM_VALUE_TO_SET,
                             value.getFloatValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.FLOAT_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of double variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues008_Double() {
        SetValuesTester tester = new SetValuesTester("testSetValues_Double", "breakpointDouble",
                                                     "runBreakpointDouble", "param",
                                                     JDWPConstants.Tag.DOUBLE_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.DOUBLE_PARAM_VALUE_TO_SET,
                             value.getDoubleValue());
            }

            @Override
            public Value setValue() {
                return new Value(StackTrace002Debuggee.DOUBLE_PARAM_VALUE_TO_SET);
            }
        };
        runSetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of java.lang.String variable in the stack.
     *
     * Refer to {@link #runSetValuesTest(SetValuesTester)} method for the sequence of
     * the test.
     */
    public void testSetValues009_String() {
        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        Value stringFieldValue = getStaticFieldValue(classID, "STRING_PARAM_VALUE_TO_SET");
        final long stringObjectID = stringFieldValue.getLongValue();

        SetValuesTester tester = new SetValuesTester("testSetValues_String", "breakpointString",
                                                     "runBreakpointString", "param",
                                                     JDWPConstants.Tag.STRING_TAG) {
            @Override
            public void checkValue(Value value) {
                long stringID = value.getLongValue();
                assertEquals("Invalid string object id", stringObjectID, stringID);

                String stringValue = getStringValue(stringID);
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.STRING_PARAM_VALUE_TO_SET,
                             stringValue);
            }

            @Override
            public Value setValue() {
                return new Value(JDWPConstants.Tag.STRING_TAG, stringObjectID);
            }
        };
        runSetValuesTest(tester);
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
    static abstract class SetValuesTester {
        protected SetValuesTester(String testName, String breakpointMethodName,
                                  String testMethodName, String testVariableName,
                                  byte testVariableJdwpTag) {
            this.testName = testName;
            this.breakpointMethodName = breakpointMethodName;
            this.testMethodName = testMethodName;
            this.testVariableName = testVariableName;
            this.testVariableJdwpTag = testVariableJdwpTag;
        }

        /**
         * Returns the new value of the tested variable.
         */
        public abstract Value setValue();

        /**
         * Checks if the given value is expected after we set it.
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
         * {@link #getTestMethodName}.
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
     * 5. Sets a new value in the tested variable using the StackFrame.SetValues command.
     * 6. Removes breakpoint and resumes debuggee.
     *
     * @param tester
     *      an instance of {@link SetValuesTester} with the configuration of the test.
     */
    private void runSetValuesTest(SetValuesTester tester) {
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
        int breakpointRequestID = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(classID,
                breakpointMethodName);

        // Resume debuggee.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for the 1st breakpoint.
        long breakpointThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointRequestID);

        // Find the frame for the tested method.
        FrameInfo testMethodFrame = getFrameInfo(breakpointThreadID, classID, testMethodID);
        assertNotNull("Can't find frame for method " + testMethodName, testMethodFrame);

        // Sets the new value in the tested variable.
        Value newValue = tester.setValue();
        assertNotNull("Incorrect value", newValue);
        assertEquals("Incorrect JDWP type tag for new value",
                     testVariableJdwpTag, newValue.getTag());
        setVariableValue(breakpointThreadID, testMethodFrame.frameID,
                         testVarInfo.getSlot(), testVariableJdwpTag, newValue);

        // Resume to hit the breakpoint again.
        debuggeeWrapper.vmMirror.resume();

        // Wait for the 2nd breakpoint.
        breakpointThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointRequestID);

        // Find the frame for the tested method.
        testMethodFrame = getFrameInfo(breakpointThreadID, classID, testMethodID);
        assertNotNull("Can't find frame for method " + testMethodName, testMethodFrame);

        // Check the value is the expected one.
        Value value = getVariableValue(breakpointThreadID, testMethodFrame.frameID,
                                       testVarInfo.getSlot(), testVariableJdwpTag);
        tester.checkValue(value);

        // Remove the breakpoint
        debuggeeWrapper.vmMirror.clearBreakpoint(breakpointRequestID);

        // Resume debuggee.
        debuggeeWrapper.vmMirror.resume();

        logWriter.println("==> " + testName + " OK.");
    }

    /**
     * Sets the value of a local variable in the stack.
     *
     * @param threadID
     *          the ID of the thread of the stack
     * @param frameID
     *          the ID of the frame of the stack
     * @param slot
     *          the slot of the variable in the stack
     * @param tag
     *          the type of the value
     * @param newValue
     *          the new value to set
     */
    private void setVariableValue(long threadID, long frameID, int slot, byte tag,
                                  Value newValue) {
        logWriter.println("Sets new value for slot " + slot +
                          " (tag " + JDWPConstants.Tag.getName(tag) +
                          ") in frame " + frameID + " of thread " + threadID +
                          " with a StackFrame::GetValues command...");

        // Send StackFrame::SetValues command.
        CommandPacket packet = new CommandPacket(JDWPCommands.StackFrameCommandSet.CommandSetID,
                                                 JDWPCommands.StackFrameCommandSet.SetValuesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsFrameID(frameID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsInt(slot);
        packet.setNextValueAsValue(newValue);

        // Check reply has no error.
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "StackFrame::SetValues command");
    }
}

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
public class GetValues002Test extends JDWPStackFrameAccessTest {
    /**
     * Tests we correctly read value of boolean variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues001_Boolean() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Boolean", "breakpointBoolean",
                                                     "runBreakpointBoolean", "param",
                                                     JDWPConstants.Tag.BOOLEAN_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.BOOLEAN_PARAM_VALUE,
                             value.getBooleanValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of byte variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues002_Byte() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Byte", "breakpointByte",
                                                     "runBreakpointByte", "param",
                                                     JDWPConstants.Tag.BYTE_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.BYTE_PARAM_VALUE,
                             value.getByteValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of char variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues003_Char() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Char", "breakpointChar",
                                                     "runBreakpointChar", "param",
                                                     JDWPConstants.Tag.CHAR_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.CHAR_PARAM_VALUE,
                             value.getCharValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of short variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues004_Short() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Short", "breakpointShort",
                                                     "runBreakpointShort", "param",
                                                     JDWPConstants.Tag.SHORT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.SHORT_PARAM_VALUE,
                             value.getShortValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of int variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues005_Int() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Int", "breakpointInt",
                                                     "runBreakpointInt", "param",
                                                     JDWPConstants.Tag.INT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.INT_PARAM_VALUE,
                             value.getIntValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of long variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues006_Long() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Long", "breakpointLong",
                                                     "runBreakpointLong", "param",
                                                     JDWPConstants.Tag.LONG_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.LONG_PARAM_VALUE,
                             value.getLongValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of float variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues007_Float() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Float", "breakpointFloat",
                                                     "runBreakpointFloat", "param",
                                                     JDWPConstants.Tag.FLOAT_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.FLOAT_PARAM_VALUE,
                             value.getFloatValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of double variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues008_Double() {
        GetValuesTester tester = new GetValuesTester("testGetValues_Double", "breakpointDouble",
                                                     "runBreakpointDouble", "param",
                                                     JDWPConstants.Tag.DOUBLE_TAG) {
            @Override
            public void checkValue(Value value) {
                assertEquals("Incorrect value in variable " + getTestVariableName(),
                             StackTrace002Debuggee.DOUBLE_PARAM_VALUE,
                             value.getDoubleValue());
            }
        };
        runGetValuesTest(tester);
    }

    /**
     * Tests we correctly read value of java.lang.String variable in the stack.
     *
     * Refer to {@link #runGetValuesTest(GetValuesTester)} method for the sequence of
     * the test.
     */
    public void testGetValues009_String() {
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
                             StackTrace002Debuggee.STRING_PARAM_VALUE,
                             stringValue);
            }
        };
        runGetValuesTest(tester);
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
     * 5. Checks the variable value in the stack is the expected value using the
     *    StackFrame.GetValues command.
     * 6. Removes breakpoint and resumes debuggee.
     *
     * @param tester
     *      an instance of {@link GetValuesTester} with the configuration of the test.
     */
    private void runGetValuesTest(GetValuesTester tester) {
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
        debuggeeWrapper.vmMirror.clearBreakpoint(breakpointRequestID);

        // Resume debuggee.
        debuggeeWrapper.vmMirror.resume();

        logWriter.println("==> " + testName + " OK.");
    }
}

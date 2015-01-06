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

import org.apache.harmony.jpda.tests.framework.jdwp.Value;

/**
 * JDWP Unit test for StackFrame.SetValues command.
 */
public class SetValues002Test extends JDWPStackFrameAccessTest {
    /**
     * Tests we correctly read value of boolean variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues001_Boolean() {
        StackFrameTester tester = new StackFrameTester("breakpointBoolean",
                StackTrace002Debuggee.BOOLEAN_SIGNAL);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointBoolean");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.BOOLEAN_PARAM_VALUE),
                new Value(StackTrace002Debuggee.BOOLEAN_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of byte variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues002_Byte() {
        StackFrameTester tester = new StackFrameTester("breakpointByte",
                StackTrace002Debuggee.BYTE_SIGNAL);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointByte");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.BYTE_PARAM_VALUE),
                new Value(StackTrace002Debuggee.BYTE_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of char variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues003_Char() {
        StackFrameTester tester = new StackFrameTester("breakpointChar",
                StackTrace002Debuggee.CHAR_SIGNAL);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointChar");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.CHAR_PARAM_VALUE),
                new Value(StackTrace002Debuggee.CHAR_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of short variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues004_Short() {
        StackFrameTester tester = new StackFrameTester("breakpointShort",
                StackTrace002Debuggee.SHORT_SIGNAL);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointShort");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.SHORT_PARAM_VALUE),
                new Value(StackTrace002Debuggee.SHORT_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of int variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues005_Int() {
        StackFrameTester tester = new StackFrameTester("breakpointInt",
                StackTrace002Debuggee.INT_SIGNAL);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointInt");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.INT_PARAM_VALUE),
                new Value(StackTrace002Debuggee.INT_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of long variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues006_Long() {
        StackFrameTester tester = new StackFrameTester("breakpointLong",
                StackTrace002Debuggee.LONG_METHOD_SIGNAL);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointLong");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.LONG_PARAM_VALUE),
                new Value(StackTrace002Debuggee.LONG_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of float variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues007_Float() {
        StackFrameTester tester = new StackFrameTester("breakpointFloat",
                StackTrace002Debuggee.FLOAT_METHOD);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointFloat");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.FLOAT_PARAM_VALUE),
                new Value(StackTrace002Debuggee.FLOAT_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of double variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues008_Double() {
        StackFrameTester tester = new StackFrameTester("breakpointDouble",
                StackTrace002Debuggee.DOUBLE_METHOD);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointDouble");
        methodInfo.addVariable("param", new Value(StackTrace002Debuggee.DOUBLE_PARAM_VALUE),
                new Value(StackTrace002Debuggee.DOUBLE_PARAM_VALUE_TO_SET));
        runStackFrameTest(tester);
    }

    /**
     * Tests we correctly read value of java.lang.String variable in the stack.
     *
     * Refer to {@link JDWPStackFrameAccessTest#runStackFrameTest(StackFrameTester)}
     * method for the sequence of the test.
     */
    public void testSetValues009_String() {
        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        Value stringFieldValue = getStaticFieldValue(classID,
                "STRING_PARAM_VALUE");
        Value stringFieldValueToSet = getStaticFieldValue(classID,
                "STRING_PARAM_VALUE_TO_SET");

        StackFrameTester tester = new StackFrameTester(
                "breakpointString", StackTrace002Debuggee.STRING_SIGNAL);
        MethodInfo methodInfo = tester.addTestMethod("runBreakpointString");
        methodInfo.addVariable("param", stringFieldValue, stringFieldValueToSet);
        runStackFrameTest(tester);
    }
}

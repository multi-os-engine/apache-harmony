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

import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * Debuggee for GetValues002Test and SetValues002Test.
 */
public class StackTrace002Debuggee extends SyncDebuggee {
    // Strings to select which method the debuggee needs to call.
    static final String BOOLEAN_SIGNAL = "runBreakpointBoolean";
    static final String BOOLEAN_METHOD_2 = "Boolean2";
    static final String BYTE_SIGNAL = "runBreakpointByte";
    static final String CHAR_SIGNAL = "runBreakpointChar";
    static final String SHORT_SIGNAL = "runBreakpointShort";
    static final String INT_SIGNAL = "runBreakpointInt";
    static final String INT_METHOD2_SIGNAL = "runBreakpointInt2";
    static final String LONG_METHOD_SIGNAL = "runBreakpointLong";
    static final String FLOAT_METHOD = "runBreakpointFloat";
    static final String DOUBLE_METHOD = "runBreakpointDouble";
    static final String STRING_SIGNAL = "runBreakpointString";

    // Values used to check StackFrame.GetValues.
    static final boolean BOOLEAN_PARAM_VALUE = true;
    static final byte BYTE_PARAM_VALUE = 123;
    static final char CHAR_PARAM_VALUE = '@';
    static final short SHORT_PARAM_VALUE = 12345;
    static final int INT_PARAM_VALUE = 123456789;
    static final long LONG_PARAM_VALUE = 102030405060708090L;
    static final float FLOAT_PARAM_VALUE = 123.456f;
    static final double DOUBLE_PARAM_VALUE = 0.123456789;
    static final String STRING_PARAM_VALUE = "this is a string object";

    // Values used to check StackFrame.SetValues.
    static final boolean BOOLEAN_PARAM_VALUE_TO_SET = !BOOLEAN_PARAM_VALUE;
    static final byte BYTE_PARAM_VALUE_TO_SET = -BYTE_PARAM_VALUE;
    static final char CHAR_PARAM_VALUE_TO_SET = '%';
    static final short SHORT_PARAM_VALUE_TO_SET = -SHORT_PARAM_VALUE;
    static final int INT_PARAM_VALUE_TO_SET = -INT_PARAM_VALUE;
    static final long LONG_PARAM_VALUE_TO_SET = -LONG_PARAM_VALUE;
    static final float FLOAT_PARAM_VALUE_TO_SET = -FLOAT_PARAM_VALUE;
    static final double DOUBLE_PARAM_VALUE_TO_SET = -DOUBLE_PARAM_VALUE;
    static final String
            STRING_PARAM_VALUE_TO_SET = "this is another string object";

    public static void main(String[] args) {
        runDebuggee(StackTrace002Debuggee.class);
    }

    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Wait for test setup.
        String signal = synchronizer.receiveMessage();
        if (signal.equals(BOOLEAN_SIGNAL)) {
            runBreakpointBoolean(BOOLEAN_PARAM_VALUE);
        } else if (signal.equals(BOOLEAN_METHOD_2)) {
            testBoolean();
        } else if (signal.equals(BYTE_SIGNAL)) {
            runBreakpointByte(BYTE_PARAM_VALUE);
        } else if (signal.equals(CHAR_SIGNAL)) {
            runBreakpointChar(CHAR_PARAM_VALUE);
        } else if (signal.equals(SHORT_SIGNAL)) {
            runBreakpointShort(SHORT_PARAM_VALUE);
        } else if (signal.equals(INT_SIGNAL)) {
            runBreakpointInt(INT_PARAM_VALUE);
        } else if (signal.equals(INT_METHOD2_SIGNAL)) {
            runBreakpointInt2(INT_PARAM_VALUE);
        } else if (signal.equals(LONG_METHOD_SIGNAL)) {
            runBreakpointLong(LONG_PARAM_VALUE);
        } else if (signal.equals(FLOAT_METHOD)) {
            runBreakpointFloat(FLOAT_PARAM_VALUE);
        } else if (signal.equals(DOUBLE_METHOD)) {
            runBreakpointDouble(DOUBLE_PARAM_VALUE);
        } else if (signal.equals(STRING_SIGNAL)) {
            runBreakpointString(STRING_PARAM_VALUE);
        } else {
            throw new TestErrorException("Unknown method " + signal);
        }
    }

    // Test boolean type.
    public void runBreakpointBoolean(boolean param) {
        breakpointBoolean(param);
        breakpointBoolean(param);
    }

    public void breakpointBoolean(boolean param) {
        logWriter.println("breakpointBoolean(param=" + param + ")");
    }

    public void testBoolean() {
        runBreakpointBoolean2(BOOLEAN_PARAM_VALUE);
    }

    public void runBreakpointBoolean2(boolean param) {
        breakpointBoolean2(param);
        breakpointBoolean2(param);
    }

    public void breakpointBoolean2(boolean param) {
        logWriter.println("breakpointBoolean(param=" + param + ")");
    }

    // Test byte type.
    public void runBreakpointByte(byte param) {
        breakpointByte(param);
        breakpointByte(param);
    }

    public void breakpointByte(byte param) {
        logWriter.println("breakpointByte(param=" + param + ")");
    }

    // Test char type.
    public void runBreakpointChar(char param) {
        breakpointChar(param);
        breakpointChar(param);
    }

    public void breakpointChar(char param) {
        logWriter.println("breakpointChar(param=" + param + ")");
    }

    // Test short type.
    public void runBreakpointShort(short param) {
        breakpointShort(param);
        breakpointShort(param);
    }

    public void breakpointShort(short param) {
        logWriter.println("breakpointShort(param=" + param + ")");
    }

    // Test int type.
    public void runBreakpointInt(int param) {
        breakpointInt(param);
        breakpointInt(param);
    }

    public void breakpointInt(int param) {
        logWriter.println("breakpointInt(param=" + param + ")");
    }

    public void runBreakpointInt2(int param) {
        int local = param;
        breakpointInt2(local);
        local = local + param;
        breakpointInt2(local);
    }

    public void breakpointInt2(int param) {
        logWriter.println("breakpointInt2(param=" + param + ")");
    }

    // Test long type.
    public void runBreakpointLong(long param) {
        breakpointLong(param);
        breakpointLong(param);
    }

    public void breakpointLong(long param) {
        logWriter.println("breakpointLong(param=" + param + ")");
    }

    // Test float type.
    public void runBreakpointFloat(float param) {
        breakpointFloat(param);
        breakpointFloat(param);
    }

    public void breakpointFloat(float param) {
        logWriter.println("breakpointFloat(param=" + param + ")");
    }

    // Test double type.
    public void runBreakpointDouble(double param) {
        breakpointDouble(param);
        breakpointDouble(param);
    }

    public void breakpointDouble(double param) {
        logWriter.println("breakpointDouble(param=" + param + ")");
    }

    // Test String type.
    public void runBreakpointString(String param) {
        breakpointString(param);
        breakpointString(param);
    }

    public void breakpointString(String param) {
        logWriter.println("breakpointString(param=\"" + param + "\")");
    }
}
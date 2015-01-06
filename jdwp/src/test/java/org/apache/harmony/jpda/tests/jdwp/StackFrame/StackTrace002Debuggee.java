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

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * Debuggee for GetValues002Test and SetValues002Test.
 */
public class StackTrace002Debuggee extends SyncDebuggee {
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
    static final String STRING_PARAM_VALUE_TO_SET = "this is another string object";

    public static void main(String [] args) {
        runDebuggee(StackTrace002Debuggee.class);
    }

    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Wait for test setup.
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        runBreakpointBoolean(BOOLEAN_PARAM_VALUE);
        runBreakpointByte(BYTE_PARAM_VALUE);
        runBreakpointChar(CHAR_PARAM_VALUE);
        runBreakpointShort(SHORT_PARAM_VALUE);
        runBreakpointInt(INT_PARAM_VALUE);
        runBreakpointLong(LONG_PARAM_VALUE);
        runBreakpointFloat(FLOAT_PARAM_VALUE);
        runBreakpointDouble(DOUBLE_PARAM_VALUE);
        runBreakpointString(STRING_PARAM_VALUE);
    }

    // Test boolean type.
    public static void runBreakpointBoolean(boolean param) {
        breakpointBoolean(param);
        breakpointBoolean(param);
    }

    public static void breakpointBoolean(boolean param) {
        System.out.println("breakpointBoolean(param=" + param + ")");
    }

    // Test byte type.
    public static void runBreakpointByte(byte param) {
        breakpointByte(param);
        breakpointByte(param);
    }

    public static void breakpointByte(byte param) {
        System.out.println("breakpointByte(param=" + param + ")");
    }

    // Test char type.
    public static void runBreakpointChar(char param) {
        breakpointChar(param);
        breakpointChar(param);
    }

    public static void breakpointChar(char param) {
        System.out.println("breakpointChar(param=" + param + ")");
    }

    // Test short type.
    public static void runBreakpointShort(short param) {
        breakpointShort(param);
        breakpointShort(param);
    }

    public static void breakpointShort(short param) {
        System.out.println("breakpointShort(param=" + param + ")");
    }

    // Test int type.
    public static void runBreakpointInt(int param) {
        breakpointInt(param);
        breakpointInt(param);
    }

    public static void breakpointInt(int param) {
        System.out.println("breakpointInt(param=" + param + ")");
    }

    // Test long type.
    public static void runBreakpointLong(long param) {
        breakpointLong(param);
        breakpointLong(param);
    }

    public static void breakpointLong(long param) {
        System.out.println("breakpointLong(param=" + param + ")");
    }

    // Test float type.
    public static void runBreakpointFloat(float param) {
        breakpointFloat(param);
        breakpointFloat(param);
    }

    public static void breakpointFloat(float param) {
        System.out.println("breakpointFloat(param=" + param + ")");
    }

    // Test double type.
    public static void runBreakpointDouble(double param) {
        breakpointDouble(param);
        breakpointDouble(param);
    }

    public static void breakpointDouble(double param) {
        System.out.println("breakpointDouble(param=" + param + ")");
    }

    // Test String type.
    public static void runBreakpointString(String param) {
        breakpointString(param);
        breakpointString(param);
    }

    public static void breakpointString(String param) {
        System.out.println("breakpointString(param=\"" + param + "\")");
    }
}
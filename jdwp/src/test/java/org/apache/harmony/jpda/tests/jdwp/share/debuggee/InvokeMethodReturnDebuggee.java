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

package org.apache.harmony.jpda.tests.jdwp.share.debuggee;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * This class provides common debuggee class for InvokeMethod tests. 
 *  
 */
public class InvokeMethodReturnDebuggee extends SyncDebuggee {

    public static final boolean EXPECTED_BOOLEAN_RESULT = true;
    public static final byte EXPECTED_BYTE_RESULT = 123;
    public static final char EXPECTED_CHAR_RESULT = '@';
    public static final short EXPECTED_SHORT_RESULT = 12345;
    public static final int EXPECTED_INT_RESULT = 123456789;
    public static final float EXPECTED_FLOAT_RESULT = 1.23456f;
    public static final long EXPECTED_LONG_RESULT = 123456789133456789L;
    public static final double EXPECTED_DOUBLE_RESULT = 0.123456789d;

    // Static methods
    public static boolean testStaticBoolean(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_BOOLEAN_RESULT;
    }

    public static byte testStaticByte(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_BYTE_RESULT;
    }

    public static char testStaticChar(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_CHAR_RESULT;
    }

    public static short testStaticShort(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_SHORT_RESULT;
    }

    public static int testStaticInt(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_INT_RESULT;
    }

    public static float testStaticFloat(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_FLOAT_RESULT;
    }

    public static long testStaticLong(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_LONG_RESULT;
    }

    public static double testStaticDouble(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_DOUBLE_RESULT;
    }

    // Instance methods
    public boolean testInstanceBoolean(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_BOOLEAN_RESULT;
    }

    public byte testInstanceByte(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_BYTE_RESULT;
    }

    public char testInstanceChar(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_CHAR_RESULT;
    }

    public short testInstanceShort(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_SHORT_RESULT;
    }

    public int testInstanceInt(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_INT_RESULT;
    }

    public float testInstanceFloat(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_FLOAT_RESULT;
    }

    public long testInstanceLong(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_LONG_RESULT;
    }

    public double testInstanceDouble(boolean needThrow) throws Throwable {
        if (needThrow) {
            throwException();
        }
        return EXPECTED_DOUBLE_RESULT;
    }

    private static void throwException() throws Throwable {
        throw new Throwable("test exception");
    }

    void execMethod() {
        logWriter.println("InvokeMethodDebuggee.execMethod()");
    }

    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        logWriter.println("InvokeMethodDebuggee");
//        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        synchronizer.receiveMessageWithoutException("org.apache.harmony.jpda.tests.jdwp.share.debuggee.InvokeMethodDebuggee(#1)");
        execMethod();
//        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        synchronizer.receiveMessageWithoutException("org.apache.harmony.jpda.tests.jdwp.share.debuggee.InvokeMethodDebuggee(#2)");
    }

    public static void main(String[] args) {
        runDebuggee(InvokeMethodReturnDebuggee.class);
    }
}


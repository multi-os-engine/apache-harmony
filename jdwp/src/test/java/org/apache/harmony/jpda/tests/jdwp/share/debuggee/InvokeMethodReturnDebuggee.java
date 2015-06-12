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
    // These fields are updated by the test before the corresponding method is invoked
    // in the suspended thread.
    private static boolean booleanResult;
    private static byte byteResult;
    private static char charResult;
    private static short shortResult;
    private static int intResult;
    private static float floatResult;
    private static long longResult;
    private static double doubleResult;
    private static Object objectResult;

    // These fields are used by the test to check all object tags.
    private static final String STRING_OBJECT = "a string";
    private static final int[] ARRAY_OBJECT = new int[] {1, 2, 3};
    private static final Thread THREAD_OBJECT = Thread.currentThread();
    private static final ThreadGroup THREAD_GROUP_OBJECT = THREAD_OBJECT.getThreadGroup();
    private static final Class<?> CLASS_OBJECT = InvokeMethodDebuggee.class;
    private static final ClassLoader CLASS_LOADER_OBJECT = CLASS_OBJECT.getClassLoader();

    // Static methods
    public static boolean testStaticBoolean(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return booleanResult;
    }

    public static byte testStaticByte(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return byteResult;
    }

    public static char testStaticChar(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return charResult;
    }

    public static short testStaticShort(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return shortResult;
    }

    public static int testStaticInt(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return intResult;
    }

    public static float testStaticFloat(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return floatResult;
    }

    public static long testStaticLong(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return longResult;
    }

    public static double testStaticDouble(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return doubleResult;
    }

    public static Object testStaticObject(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return objectResult;
    }

    // Instance methods
    public boolean testInstanceBoolean(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return booleanResult;
    }

    public byte testInstanceByte(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return byteResult;
    }

    public char testInstanceChar(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return charResult;
    }

    public short testInstanceShort(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return shortResult;
    }

    public int testInstanceInt(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return intResult;
    }

    public float testInstanceFloat(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return floatResult;
    }

    public long testInstanceLong(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return longResult;
    }

    public double testInstanceDouble(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return doubleResult;
    }

    public Object testInstanceObject(boolean needThrow) throws Throwable {
        throwExceptionIfNeeded(needThrow);
        return objectResult;
    }

    private static void throwExceptionIfNeeded(boolean needThrow) throws Throwable {
        if (needThrow) {
            throw new Throwable("test exception");
        }
    }

    void breakpointMethod() {
        logWriter.println("InvokeMethodDebuggee.breakpointMethod()");
    }

    @Override
    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("InvokeMethodReturnDebuggee");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        breakpointMethod();

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }

    public static void main(String[] args) {
        runDebuggee(InvokeMethodReturnDebuggee.class);
    }
}


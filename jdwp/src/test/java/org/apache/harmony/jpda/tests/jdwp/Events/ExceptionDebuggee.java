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

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Debuggee for ExceptionTest unit test.
 * Generates caught DebuggeeException exception.
 */
public class ExceptionDebuggee extends SyncDebuggee {
    public static final String TEST_EXCEPTION_WITH_NATIVE_TRANSITION = "THROUGH_NATIVE";
    public static final String TEST_EXCEPTION_FROM_NATIVE_METHOD = "FROM_NATIVE";

    public static void main(String[] args) {
        runDebuggee(ExceptionDebuggee.class);
    }

    public void run() {
        logWriter.println("-- ExceptionDebuggee: STARTED");

        // Cause loading of DebuggeeException so it is visible from the test.
        new DebuggeeException("dummy exception");

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("-- ExceptionDebuggee: Wait for SGNL_CONTINUE...");
        String message = synchronizer.receiveMessage();
        logWriter.println("-- ExceptionDebuggee: SGNL_CONTINUE has been received!");

        if (message.equals(TEST_EXCEPTION_FROM_NATIVE_METHOD)) {
            testThrowAndCatchExceptionFromNativeMethod();
        } else if (message.equals(TEST_EXCEPTION_WITH_NATIVE_TRANSITION)) {
            testThrowAndCatchDebuggeeException(true);
        } else {
            testThrowAndCatchDebuggeeException(false);
        }

        logWriter.println("-- ExceptionDebuggee: FINISHing...");
    }

    void testThrowAndCatchDebuggeeException(boolean nativeTransition) {
        try {
            throwAndCatchDebuggeeException(nativeTransition);
        } catch (Throwable e) {
            logWriter.printError("Unexpected exception", e);
        }
    }

    void throwAndCatchDebuggeeException(boolean nativeTransition) throws Throwable {
        try {
            if (nativeTransition) {
                throwDebuggeeExceptionWithTransition();
            } else {
                throwDebuggeeException();
            }
        } catch (DebuggeeException e) {
            logWriter.println("We caught our exception as expected");
        }
    }

    private void throwDebuggeeExceptionWithTransition() throws Throwable {
        // We use java reflection to invoke the method throwing the exception through native.
        logWriter.println("throwDebuggeeExceptionWithTransition");
        Method method = getClass().getDeclaredMethod("throwDebuggeeException");
        try {
            method.invoke(this);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof DebuggeeException) {
                // Rethrow our exception.
                throw e.getCause();
            } else {
                throw e;
            }
        }
    }

    private void throwDebuggeeException() {
        logWriter.println("throwDebuggeeException");
        throw new DebuggeeException("Caught debuggee exception");
    }

    /**
     * Catches a {@link NullPointerException} thrown from a native method.
     */
    private void testThrowAndCatchExceptionFromNativeMethod() {
        try {
            throwNullPointerExceptionFromNative();
        } catch (NullPointerException e) {
            System.out.println("Expected exeception");
        }
    }


    /**
     * Causes a {@link NullPointerException} to be thrown from native method
     * {@link System#arraycopy(Object, int, Object, int, int)}.
     */
    private void throwNullPointerExceptionFromNative() {
        System.arraycopy((Object) null, 0, (Object) null, 0, 0);
    }
}

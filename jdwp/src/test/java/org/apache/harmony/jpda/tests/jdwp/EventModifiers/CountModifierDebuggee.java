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

package org.apache.harmony.jpda.tests.jdwp.EventModifiers;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * Debuggee for CountModifierTest.
 */
public class CountModifierDebuggee extends SyncDebuggee {
    private static int locationEventCount = 0;
    private static int exceptionEventCount = 0;
    private static int threadRunCount = 0;
    private static int fieldReadWriteCount = 0;

    private static int watchedField = 0;

    static class TestException extends Exception {
    }

    static class TestClass {
        public void eventTestMethod() {
            System.out.println("TestClass.eventTestMethod: count=" + locationEventCount);
        }

        public void throwException() throws TestException {
            System.out.println("TestClass.throwException: exceptionCount=" + exceptionEventCount);
            throw new TestException();
        }
    }

    private void countAndCall(TestClass obj) {
        ++locationEventCount;
        obj.eventTestMethod();
    }

    private static void catchException(TestClass obj) {
        ++exceptionEventCount;
        try {
            obj.throwException();
        } catch (TestException e) {
            // ignore.
        }
    }

    private void startThread() {
        ++threadRunCount;
        System.out.println("CountModifierDebuggee.startThread: threadRunCount=" + threadRunCount);
        Thread t = new Thread("TestThread-" + threadRunCount);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readAndWriteField() {
        ++fieldReadWriteCount;
        System.out.println("CountModifierDebuggee.readAndWriteField: fieldReadWriteCount=" + fieldReadWriteCount);
        watchedField = watchedField + 1;
    }

    @Override
    public void run() {
        TestClass obj = new TestClass();
        new TestException();  // force class loading.

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("CountModifierDebuggee started");

        // Wait for test setup.
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        locationEventCount = 0;
        countAndCall(obj);
        countAndCall(obj);
        countAndCall(obj);

        exceptionEventCount = 0;
        catchException(obj);
        catchException(obj);
        catchException(obj);

        threadRunCount = 0;
        startThread();
        startThread();
        startThread();

        fieldReadWriteCount = 0;
        readAndWriteField();
        readAndWriteField();
        readAndWriteField();

        logWriter.println("CountModifierDebuggee finished");
    }

    public static void main(String[] args) {
        runDebuggee(CountModifierDebuggee.class);
    }

}

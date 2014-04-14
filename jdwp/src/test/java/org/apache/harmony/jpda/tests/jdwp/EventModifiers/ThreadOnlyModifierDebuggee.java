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
 * Debuggee for ThreadOnlyModifierTest.
 */
public class ThreadOnlyModifierDebuggee extends SyncDebuggee {
    static class TestException extends Exception {
    }

    static class TestClass {
        public void eventTestMethod() {
            System.out.println(
                    "ThreadOnlyModifierDebuggee.TestClass.eventTestMethod()");
        }
    }

    static class TestThread implements Runnable {
        private final TestClass obj;

        public TestThread(TestClass obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            obj.eventTestMethod();
            throwAndCatchException();
            readAndWriteField();
        }

        private void readAndWriteField() {
            System.out
            .println(
                    "ThreadOnlyModifierDebuggee.TestThread.readAndWriteField()");
            watchedField = watchedField + 1;
        }

        private void throwAndCatchException() {
            try {
                throwException();
            } catch (TestException e) {
                System.out.println("Catch TestException");
            }
        }

        private void throwException() throws TestException {
            System.out.println(
                    "ThreadOnlyModifierDebuggee.TestThread.throwException()");
            throw new TestException();
        }
    }

    private static Thread THREAD_ONE;
    private static Thread THREAD_TWO;
    private static Thread THREAD_THREE;

    private static int watchedField = 0;

    @Override
    public void run() {
        TestClass obj = new TestClass();
        new TestException();  // force class loading.

        logWriter.println("Create threads");
        THREAD_ONE = new Thread(new TestThread(obj));
        THREAD_TWO = new Thread(new TestThread(obj));
        THREAD_THREE = new Thread(new TestThread(obj));

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("ThreadOnlyModifierDebuggee started");

        // Wait for test setup.
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        runThread(THREAD_ONE, "THREAD_ONE");
        runThread(THREAD_TWO, "THREAD_TWO");
        runThread(THREAD_THREE, "THREAD_THREE");

        logWriter.println("ThreadOnlyModifierDebuggee finished");
    }

    private void runThread(Thread t, String threadName) {
        logWriter.println("Start " + threadName);
        t.start();
        try {
            logWriter.println("Wait for " + threadName);
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runDebuggee(ThreadOnlyModifierDebuggee.class);
    }

}

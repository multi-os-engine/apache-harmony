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

package org.apache.harmony.jpda.tests.jdwp.ClassType;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

public class InvokeWithMultipleEvents001Debuggee extends SyncDebuggee {
    // Information for the test.
    public static final String INVOKE_METHOD_NAME = "invokedMethod";
    public static final String BREAKPOINT_EVENT_THREAD_METHOD_NAME = "breakpointEventThread";
    public static final String BREAKPOINT_ALL_THREADS_METHOD_NAME = "breakpointAllThreads";

    private static boolean testThreadFinished = false;
    private static Thread testThread = null;

    private class TestThread extends Thread {
        @Override
        public void run() {
            // We're going to suspend all threads in the method below.
            logWriter.println("Breakpoint for event thread #2");
            breakpointAllThreads();

            // The test needs to resume us so the invoke in progress in event thread #1 can
            // complete.
            testThreadFinished = true;
        }
    }

    // Invoked to suspend main thread (event thread #1) on a breakpoint.
    public void breakpointEventThread() {
    }

    // Invoked to suspend test thread (event thread #2) and all others threads on a breakpoint.
    public void breakpointAllThreads() {
    }

    // Invoked in event thread #1. This will unblock event thread #2 that will suspend all threads
    // including event thread #1. This allows to verify the debugger is not blocked waiting for
    // this invoke.
    public static void invokedMethod() {
        // Start event thread #2. It's going to hit a breakpoint and suspend us.
        testThread.start();

        // We don't use wait/notify pattern to be sure our loop is active.
        while (!testThreadFinished) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        logWriter.println("InvokeWithMultipleEvents001Debuggee starts");

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Create test thread but does not start it now. It will be started by the invoke from
        // the test through JDWP.
        testThread = new TestThread();

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // We want to suspend the main thread on a breakpoint.
        logWriter.println("Breakpoint for event thread #1");
        breakpointEventThread();

        // Ensure tested thread is finished.
        try {
            testThread.join();
        } catch (InterruptedException e) {
            logWriter.printError("Failed to join tested thread", e);
        }
        testThread = null;

        logWriter.println("InvokeWithMultipleEvents001Debuggee ends");
    }

    public static void main(String[] args) {
        runDebuggee(InvokeWithMultipleEvents001Debuggee.class);
    }

}

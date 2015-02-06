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

package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * Debuggee for Breakpoint003Test unit test.
 */
public class Breakpoint003Debuggee extends SyncDebuggee {

    public static void main(String[] args) {
        runDebuggee(Breakpoint003Debuggee.class);
    }

    public void run() {
        logWriter.println("Breakpoint002Debuggee started");

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Cause breakpoint to hit twice.
        breakpointMethod();
        breakpointMethod();

        logWriter.println("Breakpoint002Debuggee finished");
    }

    private static void breakpointMethod() {
        System.out.println("breakpointMethod");
    }

}

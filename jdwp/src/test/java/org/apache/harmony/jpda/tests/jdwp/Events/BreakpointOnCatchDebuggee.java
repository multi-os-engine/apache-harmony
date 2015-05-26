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
 * Debuggee for BreakpointOnCatchTest unit test.
 */
public class BreakpointOnCatchDebuggee extends SyncDebuggee {
  // This variable must be set to the name of the method where we set a breakpoint.
  public static String BREAKPOINT_METHOD_NAME = "breakpointOnCatch";

  // This variable must be set to the line number of the tested "catch" statement.
  public static int BREAKPOINT_CATCH_LINE = 50;

    @Override
    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("BreakpointOnCatchDebuggee started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        breakpointOnCatch();

        logWriter.println("BreakpointOnCatchDebuggee finished");
    }

    private void breakpointOnCatch() {
      try {
        throw new Error("ERROR");
      } catch (Throwable e) {  // BREAKPOINT_CATCH_LINE must keep synced to this line number.
        logWriter.println("Caught the expected exception");
      }
    }

    public static void main(String[] args) {
        runDebuggee(BreakpointOnCatchDebuggee.class);
    }

}

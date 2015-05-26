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

import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.EventThread;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;



/**
 * JDWP Unit test for BREAKPOINT event on "catch (...)" line.
 */
public class BreakpointOnCatchTest extends JDWPEventTestCase {
    @Override
    protected String getDebuggeeClassName() {
        return BreakpointOnCatchDebuggee.class.getName();
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs BreakpointOnCatchDebuggee and set breakpoint to its breakpointOnCatch
     * method, then verifies that requested BREAKPOINT event occurs on a catch statement
     * (with a pending exception).
     */
    public void testBreakpointOnCatch() {
        logWriter.println("testBreakpointOnCatch started");

        // Wait for debuggee to start.
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Get line table and look for the PC of the tested line.
        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        long methodID = getMethodID(classID, BreakpointOnCatchDebuggee.BREAKPOINT_METHOD_NAME);

        // Install breakpoint on the expected line
        logWriter.println("Install breakpoint in " +
            BreakpointOnCatchDebuggee.BREAKPOINT_METHOD_NAME);
        int requestID = installBreakpointOnCatch(classID, methodID);

        // execute the breakpoint
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        CommandPacket event =
            debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.BREAKPOINT);
        ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(event);

        assertEquals("Invalid number of events,", 1, parsedEvents.length);
        assertEquals("Invalid request ID", requestID, parsedEvents[0].getRequestID());

        long eventThreadID = ((EventThread) parsedEvents[0]).getThreadID();
        checkThreadState(eventThreadID, JDWPConstants.ThreadStatus.RUNNING,
                JDWPConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED);

        logWriter.println("Successfully suspended on a catch statement");
        logWriter.println("testBreakpointOnCatch done");
    }

    private int installBreakpointOnCatch(long classID, long methodID) {
      long lineCodeIndex = getCodeIndexForLineNumber(classID, methodID,
          BreakpointOnCatchDebuggee.BREAKPOINT_CATCH_LINE);

      // Install breakpoint on the expected line
      Location location = new Location(JDWPConstants.TypeTag.CLASS, classID, methodID,
          lineCodeIndex);
      ReplyPacket replyPacket = debuggeeWrapper.vmMirror.setBreakpoint(location);
      return replyPacket.getNextValueAsInt();
    }

    private long getCodeIndexForLineNumber(long classID, long methodID, int line) {
      ReplyPacket replyPacket = getLineTable(classID, methodID);
      replyPacket.getNextValueAsLong();  // startIndex
      replyPacket.getNextValueAsLong();  // endIndex
      int linesCount = replyPacket.getNextValueAsInt();
      for (int i = 0; i < linesCount; ++i) {
        long lineCodeIndex = replyPacket.getNextValueAsLong();
        int lineNumber = replyPacket.getNextValueAsInt();
        if (lineNumber == line) {
          return lineCodeIndex;
        }
      }
      throw new TestErrorException("Cannot find code index for line " + line);
    }
}

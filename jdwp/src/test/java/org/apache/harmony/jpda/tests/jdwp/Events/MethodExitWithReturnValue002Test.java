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

import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


public class MethodExitWithReturnValue002Test extends JDWPEventTestCase {

    protected String getDebuggeeClassName() {
        return MethodExitWithReturnValue002Debuggee.class.getName();
    }

    public void testExceptionHandlingAfterDeoptimization() {
        logWriter.println("testExceptionHandlingAfterDeoptimization starts");

        // Suspend debuggee on a breakpoint.
        stopOnBreakpoint();

        // Request MethodEntry event to cause full deoptimization of the debuggee.
        installMethodEntry();

        // Resume the debuggee from the breakpoint.
        debuggeeWrapper.vmMirror.resume();

        // Wait for result from debuggee
        String resultAsString = synchronizer.receiveMessage();
        int result = Integer.parseInt(resultAsString);

        assertEquals("Incorrect result",
                     MethodExitWithReturnValue002Debuggee.SUCCESS_RESULT, result);

        // TODO remove method entry & continue debuggee ???

        logWriter.println("testExceptionHandlingAfterDeoptimization ends");

    }

    private void stopOnBreakpoint() {
        // Wait for debuggee to start.
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long debuggeeClassID = getClassIDBySignature(getDebuggeeClassSignature());
        long requestID = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(debuggeeClassID,
                                                                             "breakpointMethod");

        // Continue debuggee.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint.
        debuggeeWrapper.vmMirror.waitForBreakpoint(requestID);

        // Remove breakpoint.
        debuggeeWrapper.vmMirror.clearBreakpoint((int) requestID);
    }

    private void installMethodEntry() {
        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.setMethodEntry(getDebuggeeClassName());
        replyPacket.getNextValueAsInt();  // unused 'requestID'
        assertAllDataRead(replyPacket);
    }

}

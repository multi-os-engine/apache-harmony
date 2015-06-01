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

import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.framework.jdwp.exceptions.TimeoutException;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.io.IOException;

public class InvokeWithMultipleEvents001Test extends JDWPSyncTestCase {

    @Override
    protected String getDebuggeeClassName() {
        return InvokeWithMultipleEvents001Debuggee.class.getName();
    }

    public void testInvokeWithMultipleEvents001() {
        // Wait for debuggee to start.
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        long invokeMethodID = getMethodID(classID,
                InvokeWithMultipleEvents001Debuggee.INVOKE_METHOD_NAME);

        // Set breakpoint with EVENT_THREAD suspend policy. We will invoke the method in the thread
        // suspended on this breakpoint.
        int breakpointEventThread = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(classID,
                InvokeWithMultipleEvents001Debuggee.BREAKPOINT_EVENT_THREAD_METHOD_NAME,
                JDWPConstants.SuspendPolicy.EVENT_THREAD);

        // Set breakpoint with EVENT_THREAD suspend policy. We will invoke the method in the thread
        // suspended on this breakpoint.
        int breakpointAllThreads = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(classID,
                InvokeWithMultipleEvents001Debuggee.BREAKPOINT_ALL_THREADS_METHOD_NAME,
                JDWPConstants.SuspendPolicy.ALL);

        // Tell the debuggee to continue.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint and get id of suspended thread.
        long eventThreadOne = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointEventThread);
        int suspendCount = debuggeeWrapper.vmMirror.getThreadSuspendCount(eventThreadOne);
        assertEquals("Invalid suspend count:", 1, suspendCount);

        // Send ClassType.InvokeMethod but does not read reply now.
        // That invoked method starts another thread that is going to hit a breakpoint and suspend
        // all threads. The invoke can only complete when that new thread terminates.
        final int invoke_options = 0;  // resume/suspend all threads before/after the invoke.
        CommandPacket invokeMethodCommand = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.InvokeMethodCommand);
        invokeMethodCommand.setNextValueAsClassID(classID);
        invokeMethodCommand.setNextValueAsThreadID(eventThreadOne);
        invokeMethodCommand.setNextValueAsMethodID(invokeMethodID);
        invokeMethodCommand.setNextValueAsInt(0);
        invokeMethodCommand.setNextValueAsInt(invoke_options);
        logWriter.println(" Send ClassType.InvokeMethod with Exception");
        int invokeMethodCommandID = -1;
        try {
            invokeMethodCommandID = debuggeeWrapper.vmMirror.sendCommand(invokeMethodCommand);
        } catch (IOException e) {
            logWriter.printError("Failed to send ClassType.InvokeMethod", e);
            fail();
        }

        // Wait for 2nd breakpoint to hit.
        long eventThreadTwo = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointAllThreads);
        suspendCount = debuggeeWrapper.vmMirror.getThreadSuspendCount(eventThreadTwo);
        assertEquals("Invalid suspend count:", 1, suspendCount);

        // At this point, the event thread #1 must have been suspended by event thread #2. Since
        // the invoke has resumed it too, its suspend count must remain 1.
        suspendCount = debuggeeWrapper.vmMirror.getThreadSuspendCount(eventThreadOne);
        assertEquals("Invalid suspend count:", 1, suspendCount);

        // TODO check we cannot read the result of the invoke because the thread is suspended.
        // TODO catch TimeoutException?

        // Send a VirtualMachine.Resume to resume all threads. This will unblock the event thread
        // with the invoke in-progress.
        logWriter.println("Resume all threads");
        resumeDebuggee();

        // Now we can read the invoke reply.
        ReplyPacket invokeMethodReply = null;
        try {
            logWriter.println("Receiving reply for command " + invokeMethodCommandID + " ...");
            invokeMethodReply = debuggeeWrapper.vmMirror.receiveReply(invokeMethodCommandID);
        } catch (Exception e) {
            throw new TestErrorException("Did not receive invoke reply", e);
        }
        checkReplyPacket(invokeMethodReply, "ClassType::InvokeMethod command");
        logWriter.println("Received reply for command " + invokeMethodCommandID + " OK");

        // Check result is 'void'
        Value invokeResult = invokeMethodReply.getNextValueAsValue();
        assertNull("Expect null result value for 'void'", invokeResult);

        // Check exception is null.
        TaggedObject invokeException = invokeMethodReply.getNextValueAsTaggedObject();
        assertEquals("Invalid exception object id", 0, invokeException.objectID);
        assertAllDataRead(invokeMethodReply);

        // The invoke is complete but the thread is still suspended: let's resume it now.
        suspendCount = debuggeeWrapper.vmMirror.getThreadSuspendCount(eventThreadOne);
        assertEquals("Invalid suspend count:", 1, suspendCount);

        logWriter.println("Resume event thread #1");
        debuggeeWrapper.vmMirror.resumeThread(eventThreadOne);
    }

}

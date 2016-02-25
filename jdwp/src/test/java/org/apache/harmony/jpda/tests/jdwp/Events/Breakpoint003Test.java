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

import java.util.*;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.EventThread;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for BREAKPOINT event in framework code.
 */
public class Breakpoint003Test extends JDWPEventTestCase {
    protected String getDebuggeeClassName() {
        return Breakpoint003Debuggee.class.getName();
    }

    public void testBreakPointInFrameworkCode() {
        test("testBreakPointInIntegerParseInt");
    }

    private void test(String testName) {
        logWriter.println(testName + " started");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        long classID = debuggeeWrapper.vmMirror.getClassID("Ljava/lang/Integer;");
        assertTrue("Failed to find String class", classID != -1);
        int breakpointReqID = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(classID, "parseInt");
        assertTrue("Failed to install breakpoint in Integer.parseInt ", breakpointReqID != -1);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        long eventThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointReqID);
        checkThreadState(eventThreadID, JDWPConstants.ThreadStatus.RUNNING,
                JDWPConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED);

        // Get and check top frame's method name.
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.FramesCommand);
        packet.setNextValueAsThreadID(eventThreadID);
        packet.setNextValueAsInt(0);  // start from frame 0
        packet.setNextValueAsInt(1);  // length of frames
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ThreadReference::FramesCommand command");
        int frames = reply.getNextValueAsInt();
        assertEquals("Invalid number of frames", frames, 1);
        long frameID = reply.getNextValueAsLong();
        Location location = reply.getNextValueAsLocation();
        String methodName = getMethodName(location.classID, location.methodID);
        assertEquals("Invalid method name", methodName, "parseInt");

        // Get and check top frame's incoming String argument.
        packet = new CommandPacket(
                JDWPCommands.StackFrameCommandSet.CommandSetID,
                JDWPCommands.StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(eventThreadID);
        packet.setNextValueAsFrameID(frameID);
        packet.setNextValueAsInt(1);  // Get 1 value.
        packet.setNextValueAsInt(0);  // Slot 0.
        packet.setNextValueAsByte(JDWPConstants.Tag.STRING_TAG);
        //check reply for errors
        reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "StackFrame::GetValues command");
        //check number of retrieved values
        int numberOfValues = reply.getNextValueAsInt();
        assertEquals("Invalid number of values", numberOfValues, 1);
        Value val = reply.getNextValueAsValue();
        assertEquals("Invalid valud tag", val.getTag(), JDWPConstants.Tag.STRING_TAG);
        long strLocalVariableID = val.getLongValue();
        String strLocalVariable = getStringValue(strLocalVariableID);
        assertEquals("Invalid valud tag", strLocalVariable, Breakpoint003Debuggee.VALUE_STRING);

        logWriter.println(testName + " done");
    }
}

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

package org.apache.harmony.jpda.tests.jdwp.StackFrame;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.StackFrame.JDWPStackFrameTestCase.FrameInfo;
import org.apache.harmony.jpda.tests.jdwp.StackFrame.JDWPStackFrameTestCase.VarInfo;

/**
 * Base class for testing StackFrame.GetValues and StackFrame.SetValues commands.
 */
public class JDWPStackFrameAccessTest extends JDWPStackFrameTestCase {
    @Override
    protected final String getDebuggeeClassName() {
        return StackTrace002Debuggee.class.getName();
    }

    /**
     * Returns the {@link VarInfo} of the given variable in the given method.
     *
     * @param classID
     *          the ID of the declaring class of the method
     * @param methodID
     *          the ID of the method
     * @param variableName
     *          the name of the variable we look for
     */
    protected VarInfo getVariableInfo(long classID, long methodID, String variableName) {
        VarInfo[] variables = jdwpGetVariableTable(classID, methodID);
        for (VarInfo variable : variables) {
            if (variable.name.equals(variableName)) {
                return variable;
            }
        }
        return null;
    }

    /**
     * Returns the {@link FrameInfo} of the most recent frame matching the given method.
     *
     * @param threadID
     *          the ID of the thread where to look for the frame
     * @param classID
     *          the ID of the declaring class of the method
     * @param methodID
     *          the ID of the method
     */
    protected FrameInfo getFrameInfo(long threadID, long classID, long methodID) {
        int frameCount = jdwpGetFrameCount(threadID);

        // There should be at least 2 frames: the breakpoint method and its caller.
        assertTrue("Not enough frames", frameCount > 2);

        FrameInfo[] frames = jdwpGetFrames(threadID, 0, frameCount);
        for (FrameInfo frameInfo : frames) {
            if (frameInfo.location.classID == classID &&
                    frameInfo.location.methodID == methodID) {
                return frameInfo;
            }
        }
        return null;
    }

    /**
     * Returns the value of a local variable in the stack.
     *
     * @param threadID
     *          the ID of the thread of the stack
     * @param frameID
     *          the ID of the frame of the stack
     * @param slot
     *          the slot of the variable in the stack
     * @param tag
     *          the type of the value
     */
    protected Value getVariableValue(long threadID, long frameID, int slot, byte tag) {
        logWriter.println("Request value for slot " + slot +
                          " (tag " + JDWPConstants.Tag.getName(tag) +
                          ") in frame " + frameID + " of thread " + threadID +
                          " with a StackFrame::GetValues command...");

        // Send StackFrame::GetValues command.
        CommandPacket packet = new CommandPacket(
                JDWPCommands.StackFrameCommandSet.CommandSetID,
                JDWPCommands.StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsFrameID(frameID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsInt(slot);
        packet.setNextValueAsByte(tag);

        // Check reply has no error.
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "StackFrame::GetValues command");

        // Check we have 1 value.
        int numberOfValues = reply.getNextValueAsInt();
        assertEquals("Incorrect number of values", 1, numberOfValues);

        // Check the value tag is correct.
        Value value = reply.getNextValueAsValue();
        assertEquals("Invalid value tag", tag, value.getTag());

        assertAllDataRead(reply);
        return value;
    }
}

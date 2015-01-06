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

/**
 * @author Aleksander V. Budniy

 /**
 * Created on 15.08.2005
 */
package org.apache.harmony.jpda.tests.jdwp.StackFrame;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands.StackFrameCommandSet;
import org.apache.harmony.jpda.tests.jdwp.StackFrame.JDWPStackFrameTestCase.FrameInfo;

import java.util.Arrays;

/**
 * JDWP Unit test for StackFrame.GetValues command.
 */
public class GetValuesTest extends JDWPStackTraceBaseTest {
    /**
     * This testcase exercises StackFrame.GetValues command.
     * <BR>The test starts StackTraceDebuggee, sets breakpoint at the beginning of
     * the tested method - 'nestledMethod3' and stops at the breakpoint.
     * <BR> Then the test performs Method.VariableTable command and checks
     * returned VariableTable.
     * <BR> At last the test performs StackFrame.GetValues command and checks
     * returned values of variables.
     *
     */
    public void testGetValues001() {
        //gets and checks local variables of tested method
        examineGetValues();
    }

    public void testGetValues002_InvalidObjectError() {
        long invalidThreadID = -1;
        logWriter.println("Send StackFrame.GetValues with invalid thread " + invalidThreadID);
        CommandPacket packet = new CommandPacket(StackFrameCommandSet.CommandSetID,
                StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(invalidThreadID);
        packet.setNextValueAsFrameID(0);
        packet.setNextValueAsInt(0);
        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(replyPacket, "StackFrame.GetValues",
                JDWPConstants.Error.INVALID_OBJECT);
    }

    public void testGetValues003_ThreadNotSuspendedError() {
        long threadID = getThreadID();

        CommandPacket packet = new CommandPacket(StackFrameCommandSet.CommandSetID,
                StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsFrameID(0);
        packet.setNextValueAsInt(0);
        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(replyPacket, "StackFrame.GetValues",
                JDWPConstants.Error.THREAD_NOT_SUSPENDED);
    }

    public void testGetValues004_InvalidFrameIDError() {
        long threadID = getThreadID();

        // suspend thread
        jdwpSuspendThread(threadID);

        long invalidFrameID = -1;
        logWriter.println("Send StackFrame.GetValues with invalid frameID " + invalidFrameID);
        CommandPacket packet = new CommandPacket(StackFrameCommandSet.CommandSetID,
                StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsFrameID(invalidFrameID);
        packet.setNextValueAsInt(0);
        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(replyPacket, "StackFrame.GetValues",
                JDWPConstants.Error.INVALID_FRAMEID);
    }

    public void testGetValues005_InvalidSlotError() {
        long threadID = getThreadID();

        // suspend thread
        jdwpSuspendThread(threadID);

        FrameInfo frameInfo = findFrameInfo(threadID);

        //getting Variable Table
        logWriter.println("");
        logWriter.println("=> Getting Variable Table...");
        long refTypeID = getClassIDBySignature(getDebuggeeClassSignature());
        logWriter.println("=> Debuggee class = " + getDebuggeeClassName());
        logWriter.println("=> referenceTypeID for Debuggee class = "
                + refTypeID);
        varInfos = jdwpGetVariableTable(refTypeID, frameInfo.location.methodID);
        if (GetValuesTest.checkVarTable(logWriter, varInfos, varTags, varSignatures, varNames)) {
            logWriter.println("=> Variable table check passed.");
        } else {
            printErrorAndFail("Variable table check failed.");
        }

        int invalidSlotId = -1;
        logWriter.println("Send StackFrame.GetValues with invalid slot " + invalidSlotId);
        CommandPacket packet = new CommandPacket(StackFrameCommandSet.CommandSetID,
                StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsFrameID(frameInfo.frameID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsInt(invalidSlotId);
        packet.setNextValueAsByte(JDWPConstants.Tag.OBJECT_TAG);
        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(replyPacket, "StackFrame.GetValues",
                JDWPConstants.Error.INVALID_SLOT);
    }

    private void examineGetValues() {
        long refTypeID = getClassIDBySignature(getDebuggeeClassSignature());
        logWriter.println("=> Debuggee class = " + getDebuggeeClassName());
        logWriter.println("=> referenceTypeID for Debuggee class = "
                + refTypeID);
        long threadID = debuggeeWrapper.vmMirror.getThreadID(testedThreadName);

        logWriter.println("=> testedThreadID = " + threadID);
        if (threadID == -1) {
            printErrorAndFail("testedThread is not found!");
        }

        // suspend thread
        jdwpSuspendThread(threadID);

        // find frame for the tested method
        FrameInfo frameInfo = findFrameInfo(threadID);

        //getting Variable Table
        logWriter.println("");
        logWriter.println("=> Getting Variable Table...");
        varInfos = jdwpGetVariableTable(refTypeID, frameInfo.location.methodID);
        if (checkVarTable(logWriter, varInfos, varTags, varSignatures, varNames)) {
            logWriter.println("=> Variable table check passed.");
        } else {
            printErrorAndFail("Variable table check failed.");
        }

        //prepare and perform GetValues command
        logWriter.println("");
        logWriter.println("=> Send StackFrame::GetValues command...");
        CommandPacket packet = new CommandPacket(
                JDWPCommands.StackFrameCommandSet.CommandSetID,
                JDWPCommands.StackFrameCommandSet.GetValuesCommand);
        packet.setNextValueAsThreadID(threadID);
        packet.setNextValueAsFrameID(frameInfo.frameID);

        logWriter.println("=> Thread: " + threadID);
        logWriter.println("=> Frame: " + frameInfo.frameID);
        packet.setNextValueAsInt(varTags.length);
        for (int i = 0; i < varTags.length; i++) {
            logWriter.println("");
            logWriter.println("=> For variable #"+i+":");
            packet.setNextValueAsInt(varInfos[i].getSlot());
            logWriter.println("=> Slot = "+varInfos[i].getSlot());
            byte tag = varTags[Arrays.asList(varNames).indexOf(varInfos[i].getName())];
            packet.setNextValueAsByte(tag);
            logWriter.println("=> Tag = "+JDWPConstants.Tag.getName(tag));
            logWriter.println("");
        }

        //check reply for errors
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "StackFrame::GetValues command");

        //check number of retrieved values
        int numberOfValues = reply.getNextValueAsInt();
        logWriter.println("=> Number of values = " + numberOfValues);
        if (numberOfValues != varTags.length) {
            printErrorAndFail("Unexpected number of values: "
                    + numberOfValues + " instead of "+varTags.length);
        }

        boolean success = true;
        //print and check values of variables
        logWriter.println("=> Values of variables: ");

        // TODO check we find every variable
        for (int i = 0; i < numberOfValues; ++i) {
            Value val = reply.getNextValueAsValue();
            switch (val.getTag()) {
                case JDWPConstants.Tag.BOOLEAN_TAG: {
                    boolean boolValue = val.getBooleanValue();
                    if (boolValue) {
                        logWriter.println("=> "+varInfos[1].getName() + " = " + boolValue);
                        logWriter.println("");
                    } else {
                        logWriter.printError("Unexpected value of boolean variable: "
                                + boolValue + " instead of: true");
                        logWriter.printError("");
                        success = false;
                    }
                    break;
                }

                case JDWPConstants.Tag.INT_TAG: {
                    logWriter.println("=>Tag is correct");
                    int intValue = val.getIntValue();
                    if (intValue == -512) {
                        logWriter.println("=> "+varInfos[2].getName() + " = " + intValue);
                        logWriter.println("");
                    } else {
                        logWriter
                        .printError("unexpected value of int variable: "
                                + intValue + " instead of: -512");
                        logWriter.printError("");
                        success = false;
                    }
                    break;
                }

                case JDWPConstants.Tag.STRING_TAG: {
                    logWriter.println("=>Tag is correct");
                    long strLocalVariableID = val.getLongValue();
                    String strLocalVariable = getStringValue(strLocalVariableID);
                    if (strLocalVariable.equals("test string")) {
                        logWriter.println("=> "+varInfos[2].getName() + " = "
                                + strLocalVariable);
                        logWriter.println("");
                    } else {
                        logWriter
                        .printError("Unexpected value of string variable: "
                                + strLocalVariable
                                + " instead of: "
                                + "test string");
                        logWriter.printError("");
                        success = false;
                    }
                    break;
                }

                case JDWPConstants.Tag.OBJECT_TAG: {
                    logWriter.println("=> Tag is correct");
                    logWriter.println("");
                    break;
                }
                default:
                    logWriter.printError("Unexpected tag of variable: "
                            + JDWPConstants.Tag.getName(val.getTag()));
                    logWriter.printError("");
                    success = false;
                    break;
            }  // switch
        }  // for

        assertTrue(logWriter.getErrorMessage(), success);
    }
}

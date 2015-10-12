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

package org.apache.harmony.jpda.tests.jdwp.ReferenceType;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.debuggee.SyntheticMembersDebuggee;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP Unit test for ReferenceType.FieldsWithGeneric command.
 */
public class SyntheticFieldsWithGenericTest extends JDWPSyncTestCase {

    private static final String COMMAND_NAME = "ReferenceType.FieldsWithGeneric command";
    private static final int FIELD_SYNTHETIC_FLAG = 0xf0000000;

    @Override
    protected String getDebuggeeClassName() {
        return SyntheticMembersDebuggee.class.getName();
    }

    /**
     * This testcase exercises ReferenceType.FieldsWithGeneric command.
     * <BR>The test starts FieldsDebuggee class, requests referenceTypeId
     * for this class by VirtualMachine.ClassesBySignature command, then
     * performs ReferenceType.Fields command and checks that returned
     * list of fields corresponds to expected list with expected attributes.
     */
    public void testSyntheticFieldsWithGeneric001() {
        String thisTestName = "testFields001";
        logWriter.println("==> " + thisTestName + " for " + COMMAND_NAME + ": START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        String signature = getClassSignature(SyntheticMembersDebuggee.InnerClass.class);
        long refTypeID = getClassIDBySignature(signature);

        logWriter.println("=> Debuggee class = " + getDebuggeeClassName());
        logWriter.println("=> referenceTypeID for inner class = " + refTypeID);
        logWriter.println("=> CHECK: send " + COMMAND_NAME + " and check reply...");

        CommandPacket fieldsCommand = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.FieldsWithGenericCommand);
        fieldsCommand.setNextValueAsReferenceTypeID(refTypeID);

        ReplyPacket fieldsReply = debuggeeWrapper.vmMirror.performCommand(fieldsCommand);
        fieldsCommand = null;
        checkReplyPacket(fieldsReply, COMMAND_NAME);

        int returnedFieldsNumber = fieldsReply.getNextValueAsInt();
        logWriter.println("=> Returned fields number = " + returnedFieldsNumber);

        boolean foundSyntheticField = false;
        for (int i = 0; i < returnedFieldsNumber; ++i) {
            long fieldID = fieldsReply.getNextValueAsFieldID();
            String fieldName = fieldsReply.getNextValueAsString();
            String fieldSignature = fieldsReply.getNextValueAsString();
            String fieldGenericSignature = fieldsReply.getNextValueAsString();
            int fieldModifiers = fieldsReply.getNextValueAsInt();
            logWriter.println("\n=> Field ID = " + fieldID);
            logWriter.println("=> Field name = " + fieldName);
            logWriter.println("=> Field signature = " + fieldSignature);
            logWriter.println("=> Field generic signature = " + fieldGenericSignature);
            logWriter.println("=> Field modifiers = 0x" + Integer.toHexString(fieldModifiers));
            if ((fieldModifiers & FIELD_SYNTHETIC_FLAG) == FIELD_SYNTHETIC_FLAG) {
                // We found a synthetic field.
                foundSyntheticField = true;
                break;
            }
        }

        assertTrue("Did not found any synthetic field", foundSyntheticField);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("==> " + thisTestName + " for " + COMMAND_NAME + ": FINISH");
    }
}

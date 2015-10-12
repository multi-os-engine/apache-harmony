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
 * JDWP Unit test for ReferenceType.Methods command.
 */
public class SyntheticMethodsTest extends JDWPSyncTestCase {

    private static final String COMMAND_NAME = "ReferenceType.Methods command";
    private static final int METHOD_SYNTHETIC_FLAG = 0xf0000000;

    @Override
    protected String getDebuggeeClassName() {
        return SyntheticMembersDebuggee.class.getName();
    }

    /**
     * This testcase exercises ReferenceType.Methods command.
     * <BR>The test starts MethodsDebuggee class, requests referenceTypeId
     * for this class by VirtualMachine.ClassesBySignature command, then
     * performs ReferenceType.Methods command and checks that returned
     * list of methods corresponds to expected list of methods with expected attributes.
     */
    public void testSyntheticMethods001() {
        String thisTestName = "testMethods001";
        logWriter.println("==> " + thisTestName + " for " + COMMAND_NAME + ": START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        String signature = getDebuggeeClassSignature();
        long refTypeID = getClassIDBySignature(signature);

        logWriter.println("=> Debuggee class = " + getDebuggeeClassName());
        logWriter.println("=> referenceTypeID for debuggee class = " + refTypeID);
        logWriter.println("=> CHECK: send " + COMMAND_NAME + " and check reply...");

        CommandPacket methodsCommand = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.MethodsCommand);
        methodsCommand.setNextValueAsReferenceTypeID(refTypeID);

        ReplyPacket methodsReply = debuggeeWrapper.vmMirror.performCommand(methodsCommand);
        methodsCommand = null;
        checkReplyPacket(methodsReply, COMMAND_NAME);

        int returnedMethodsNumber = methodsReply.getNextValueAsInt();
        logWriter.println("=> Returned methods number = " + returnedMethodsNumber);

        logWriter.println("=> CHECK for all expected methods...");
        boolean foundSyntheticMethod = false;
        for (int i = 0; i < returnedMethodsNumber; i++) {
            long returnedMethodID = methodsReply.getNextValueAsMethodID();
            String returnedMethodName = methodsReply.getNextValueAsString();
            String returnedMethodSignature = methodsReply.getNextValueAsString();
            int returnedMethodModifiers = methodsReply.getNextValueAsInt();
            logWriter.println("\n=> Method ID = " + returnedMethodID);
            logWriter.println("=> Method name = " + returnedMethodName);
            logWriter.println("=> Method signature = " + returnedMethodSignature);
            logWriter.println("=> Method modifiers = 0x" + Integer.toHexString(returnedMethodModifiers));
            if ((returnedMethodModifiers & METHOD_SYNTHETIC_FLAG) == METHOD_SYNTHETIC_FLAG) {
                // We found a synthetic method.
                foundSyntheticMethod = true;
                break;
            }
        }

        assertTrue("Did not found any synthetic method", foundSyntheticMethod);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("==> " + thisTestName + " for " + COMMAND_NAME + ": FINISH");
    }
}

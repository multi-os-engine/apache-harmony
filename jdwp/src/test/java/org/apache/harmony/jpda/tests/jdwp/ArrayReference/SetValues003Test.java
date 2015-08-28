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

package org.apache.harmony.jpda.tests.jdwp.ArrayReference;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPTestConstants;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;


/**
 * JDWP unit test for ArrayReference.SetValues command with errors.
 */
public class SetValues003Test extends JDWPSyncTestCase {

    private static final String THIS_COMMAND_NAME = "ArrayReference::SetValues command";

    @Override
    protected String getDebuggeeClassName() {
        return SetValues003Debuggee.class.getName();
    }

    /**
     * Tests that ArrayReference.SetValues command returns INVALID_OBJECT error when
     * given an unknown object ID.
     */
    public void testSetValues003_InvalidObject() {
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Pass an invalid object ID as array ID.
        ReplyPacket checkedReply = setArrayValue(JDWPTestConstants.INVALID_OBJECT_ID, 0,
                JDWPTestConstants.NULL_OBJECT_ID);

        checkReplyPacket(checkedReply, "ArrayReference::SetValues command",
                JDWPConstants.Error.INVALID_OBJECT);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": OK.");
    }

    /**
     * Tests that ArrayReference.SetValues command returns INVALID_ARRAY error when
     * given a valid object ID that is not an array.
     */
    public void testSetValues003_InvalidArray() {
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Find reference type ID.
        long refTypeID = getClassIDBySignature(getDebuggeeClassSignature());
        logWriter.println("=> Debuggee class = " + getDebuggeeClassName());
        logWriter.println("=> referenceTypeID for Debuggee class = " + refTypeID);

        // Pass the debuggee type ID as array ID.
        ReplyPacket checkedReply = setArrayValue(refTypeID, 0, JDWPTestConstants.NULL_OBJECT_ID);

        checkReplyPacket(checkedReply, "ArrayReference::SetValues command",
                JDWPConstants.Error.INVALID_ARRAY);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": OK.");
    }

    /**
     * Tests that ArrayReference.SetValues command returns INVALID_LENGTH error when
     * given an invalid element index.
     */
    public void testSetValues003_InvalidIndex() {
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Find array object ID.
        long checkedObjectID = getTestArrayObjectID();
        logWriter.println("=> Returned checked ArrayID = " + checkedObjectID);
        logWriter.println("=> CHECK: send " + THIS_COMMAND_NAME
            + " for this ArrayID for element of referenceType with null values...");

        // Set array's element 0 to the array reference itself.
        ReplyPacket checkedReply = setArrayValue(checkedObjectID, 1,
                JDWPTestConstants.NULL_OBJECT_ID);

        checkReplyPacket(checkedReply, "ArrayReference::SetValues command",
                JDWPConstants.Error.INVALID_LENGTH);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": OK.");
    }

    /**
     * Tests that ArrayReference.SetValues command returns TYPE_MISMATCH error when
     * given a valid object ID whose type is not compatible with the array.
     */
    public void testSetValues003_TypeMismatch() {
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": START...");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Find array object ID.
        long checkedObjectID = getTestArrayObjectID();
        logWriter.println("=> Returned checked ArrayID = " + checkedObjectID);
        logWriter.println("=> CHECK: send " + THIS_COMMAND_NAME
            + " for this ArrayID for element of referenceType with null values...");

        // Set array's element 0 to the array reference itself.
        ReplyPacket checkedReply = setArrayValue(checkedObjectID, 0, checkedObjectID);

        checkReplyPacket(checkedReply, "ArrayReference::SetValues command",
                JDWPConstants.Error.TYPE_MISMATCH);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("==> " + getName() + " for " + THIS_COMMAND_NAME + ": OK.");
    }

    /**
     * Returns the ID of the tested array object.
     */
    private long getTestArrayObjectID() {
        String checkedFieldNames[] = {
                "objectArrayField",
        };

        long refTypeID = getClassIDBySignature(getDebuggeeClassSignature());
        logWriter.println("=> Debuggee class = " + getDebuggeeClassName());
        logWriter.println("=> referenceTypeID for Debuggee class = " + refTypeID);

        long checkedFieldIDs[] = checkFields(refTypeID, checkedFieldNames);

        logWriter.println("=> Send ReferenceType::GetValues command and get ArrayID to check...");

        CommandPacket getValuesCommand = new CommandPacket(
                JDWPCommands.ReferenceTypeCommandSet.CommandSetID,
                JDWPCommands.ReferenceTypeCommandSet.GetValuesCommand);
        getValuesCommand.setNextValueAsReferenceTypeID(refTypeID);
        getValuesCommand.setNextValueAsInt(1);
        getValuesCommand.setNextValueAsFieldID(checkedFieldIDs[0]);
        ReplyPacket getValuesReply =
            debuggeeWrapper.vmMirror.performCommand(getValuesCommand);
        checkReplyPacket(getValuesReply, "ReferenceType::GetValues command");

        int returnedValuesNumber = getValuesReply.getNextValueAsInt();
        assertEquals("ReferenceType::GetValues returned invalid number of values,",
                1, returnedValuesNumber);
        logWriter.println("=> Returned values number = " + returnedValuesNumber);

        Value checkedObjectFieldValue = getValuesReply.getNextValueAsValue();
        byte checkedObjectFieldTag = checkedObjectFieldValue.getTag();
        logWriter.println("=> Returned field value tag for checked object= " + checkedObjectFieldTag
            + "(" + JDWPConstants.Tag.getName(checkedObjectFieldTag) + ")");

        assertEquals("ReferenceType::GetValues returned invalid object field tag,",
                JDWPConstants.Tag.ARRAY_TAG, checkedObjectFieldTag,
                JDWPConstants.Tag.getName(JDWPConstants.Tag.ARRAY_TAG),
                JDWPConstants.Tag.getName(checkedObjectFieldTag));

        return checkedObjectFieldValue.getLongValue();
    }

    /**
     * Sends an ArrayReference.SetValues command to the debuggee.
     *
     * @param arrayID
     *          the ID of the array
     * @param elementIndex
     *          the index of the element to set
     * @param elementObjectID
     *          the ID of the element object to set
     * @return
     *          the reply packet
     */
    private ReplyPacket setArrayValue(long arrayID, int elementIndex, long elementObjectID) {
        CommandPacket checkedCommand = new CommandPacket(
                JDWPCommands.ArrayReferenceCommandSet.CommandSetID,
                JDWPCommands.ArrayReferenceCommandSet.SetValuesCommand);
        checkedCommand.setNextValueAsObjectID(arrayID);
        checkedCommand.setNextValueAsInt(elementIndex); // first index
        checkedCommand.setNextValueAsInt(1); // elements' number
        checkedCommand.setNextValueAsObjectID(elementObjectID); // null value
        return debuggeeWrapper.vmMirror.performCommand(checkedCommand);
    }
}

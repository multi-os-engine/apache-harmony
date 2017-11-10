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

package org.apache.harmony.jpda.tests.jdwp.DDM;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.util.Arrays;

/**
 * JDWP unit test for DDM.Chunk command.
 */
public class DDMTest extends JDWPSyncTestCase {

    /**
     * JDWP DDM Command Set constants
     */
    public static class DDMCommandSet {
        public static final byte CommandSetID = -57; // uint8_t value is 199

        public static final byte ChunkCommand = 1;
    }

    /**
     * Returns full name of debuggee class which is used by this test.
     * @return full name of debuggee class.
     */
    @Override
    protected String getDebuggeeClassName() {
        return "org.apache.harmony.jpda.tests.jdwp.DDM.DDMDebuggee";
    }

    private CommandPacket makeCommand(byte[] test_values) {
        CommandPacket packet = new CommandPacket(
                DDMCommandSet.CommandSetID,
                DDMCommandSet.ChunkCommand);
        packet.setNextValueAsInt(DDMDebuggee.DDM_TEST_TYPE);
        packet.setNextValueAsInt(test_values.length);
        for (byte b : test_values) {
          packet.setNextValueAsByte(b);
        }
        return packet;
    }

    /**
     * This testcase exercises DDM.Chunk command.
     * <BR>Starts <A HREF="./DDMDebuggee.html">DDMDebuggee</A> debuggee.
     * <BR>Creates new instance of array by DDM.Chunk command,
     * check it length by ArrayReference.Length command.
     */
    public void testChunk001() {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        byte[] test_values = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        CommandPacket packet = makeCommand(test_values);
        logWriter.println("Send Chunk message with handler");

        byte[] expected = DDMDebuggee.calculateExpectedResult(test_values);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "DDM::Chunk command");
        int type = reply.getNextValueAsInt();
        assertEquals("DDM::Chunk returned unexpected type", DDMDebuggee.DDM_RESULT_TYPE, type);
        int len = reply.getNextValueAsInt();
        assertEquals("DDM::Chunk returned unexpected amount of data", expected.length, len);
        byte[] res = new byte[len];
        for (int i = 0; i < len; i++) {
          res[i] = reply.getNextValueAsByte();
        }
        if (!Arrays.equals(expected, res)) {
          fail("Unexpected different value: expected " + Arrays.toString(expected) + " got " +
              Arrays.toString(res));
        }
        assertAllDataRead(reply);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        logWriter.println("Send same message without handler");
        packet = makeCommand(test_values);
        reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "DDM::Chunk command");
        assertAllDataRead(reply);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }
}

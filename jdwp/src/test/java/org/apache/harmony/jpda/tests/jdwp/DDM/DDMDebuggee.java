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

import java.nio.*;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

import java.util.Arrays;
import java.util.zip.Adler32;

/**
 * Debuggee for JDWP DDMTest unit test which
 * exercises DDM.Chunk command.
 */
public class DDMDebuggee extends SyncDebuggee {

    public static final int DDM_TEST_TYPE = 0xADD0A110;
    public static final int DDM_RESULT_TYPE = 0xADD04350;

    public static byte[] calculateExpectedResult(byte[] in) {
        return calculateExpectedResult(in, 0, in.length);
    }
    public static byte[] calculateExpectedResult(byte[] in, int off, int len) {
        ByteBuffer b = ByteBuffer.wrap(new byte[8]);
        b.order(ByteOrder.BIG_ENDIAN);
        Adler32 a = new Adler32();
        a.update(in, off, len);
        b.putLong(a.getValue());
        return b.array();
    }

    public class TestChunkHandler extends ChunkHandler {
        @Override
        public void connected() {}

        @Override
        public void disconnected() {}

        @Override
        public Chunk handleChunk(Chunk request) {
            if (request.type != DDM_TEST_TYPE) {
                throw new Error("Bad type!");
            }

            byte[] res = calculateExpectedResult(request.data, request.offset, request.length);
            return new Chunk(DDM_RESULT_TYPE, res, 0, res.length);
        }
    }

    @Override
    public void run() {
        ChunkHandler h = new TestChunkHandler();
        DdmServer.registerHandler(DDM_TEST_TYPE, h);
        DdmServer.registrationComplete();
        logWriter.println("-> Added chunk handler type: " + DDM_TEST_TYPE + " handler: " + h);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("Removing handler type: " + DDM_TEST_TYPE);
        DdmServer.unregisterHandler(DDM_TEST_TYPE);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("Test complete");
    }

    /**
     * Starts DDMDebuggee with help of runDebuggee() method
     * from <A HREF="../../share/Debuggee.html">Debuggee</A> super class.
     */
    public static void main(String [] args) {
        runDebuggee(DDMDebuggee.class);
    }
}

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

import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.framework.jdwp.EventBuilder;
import org.apache.harmony.jpda.tests.framework.jdwp.EventPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.EventThread;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPUnitDebuggeeWrapper;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.util.HashSet;
import java.util.Set;

/**
 * JDWP Unit test for BREAKPOINT event in methods possibly inlined.
 */
public class Breakpoint003Test extends JDWPTestCase {
    protected JPDADebuggeeSynchronizer synchronizer;

    private volatile boolean eventThreadRun;
    private Set<Long> threadIds;
    private int threadStartRequestID;
    private int threadEndRequestID;
    private volatile int breakpointRequestID;
    private volatile long breakpointEventThreadID;
    private Object synchronizationObject;
    private Thread eventReceiverThread;

    private void onThreadStart(long threadId) {
        synchronized (synchronizationObject) {
            logWriter.println("#THREAD_START: thread 0x" + Long.toHexString(threadId));
            threadIds.add(Long.valueOf(threadId));
        }
    }

    private void onThreadEnd(long threadId) {
        synchronized (synchronizationObject) {
            logWriter.println("#THREAD_END: thread 0x" + Long.toHexString(threadId));
            threadIds.remove(Long.valueOf(threadId));
            synchronizationObject.notify();
        }
    }

    /**
     * This method is invoked right before starting debuggee VM.
     */
    @Override
    protected void beforeDebuggeeStart(JDWPUnitDebuggeeWrapper debuggeeWrapper) {
        synchronizer = createSynchronizer();
        int port = synchronizer.bindServer();
        debuggeeWrapper.savedVMOptions = "-Djpda.settings.syncPort=" + port;
        super.beforeDebuggeeStart(debuggeeWrapper);
    }

    /**
     * Creates wrapper for synchronization channel.
     */
    protected JPDADebuggeeSynchronizer createSynchronizer() {
        return new JPDADebuggeeSynchronizer(logWriter, settings);
    }

    private void requestThreadStart() {
        threadStartRequestID = requestThreadEvent(
                JDWPConstants.EventKind.THREAD_START);
    }

    private void requestThreadEnd() {
        threadEndRequestID = requestThreadEvent(
                JDWPConstants.EventKind.THREAD_END);
    }

    private int requestThreadEvent(byte eventKind) {
        EventBuilder builder = new EventBuilder(eventKind,
                JDWPConstants.SuspendPolicy.NONE);
        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.setEvent(
                builder.build());
        int requestID = replyPacket.getNextValueAsInt();
        assertAllDataRead(replyPacket);
        return requestID;
    }

    public class EventReceiver implements Runnable {
        @Override
        public void run() {
            while (eventThreadRun) {
                receiveEvent();
            }
        }

        private void receiveEvent() {
            EventPacket eventPacket;
            try {
                eventPacket = debuggeeWrapper.vmMirror.receiveEvent();
            } catch (TestErrorException e) {
                if (!eventThreadRun) {
                    // We're not running anymore so stop thread
                    return;
                } else {
                    throw e;
                }
            }
            ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(
                    eventPacket);
            // Get suspend policy (all events share the same so take the
            // first one).
            byte suspendPolicy = parsedEvents[0].getSuspendPolicy();
            logWriter.println(
                    "Received " + parsedEvents.length + " event(s)");
            for (int i = 0; i < parsedEvents.length; ++i) {
                
            }
            long eventThreadID = -1;
            for (ParsedEvent event : parsedEvents) {
                if (event instanceof EventThread) {
                    long threadID = ((EventThread) event).getThreadID();
                    if (eventThreadID != -1) {
                        // Check the event is for the same thread.
                        assertEquals(eventThreadID, threadID);
                    }
                    eventThreadID = threadID;
                }
                int requestID = event.getRequestID();
                logWriter.println("Event for request 0x" + Integer.toHexString(requestID));
                if (requestID == threadStartRequestID ||
                    requestID == threadEndRequestID ||
                    requestID == breakpointRequestID) {
                    switch (event.getEventKind()) {
                        case JDWPConstants.EventKind.THREAD_START:
                            assertTrue(eventThreadID != -1);
                            if (event.getRequestID() == threadStartRequestID) {
                                // Our requested THREAD_START event.
                                onThreadStart(eventThreadID);
                            }
                            break;
                        case JDWPConstants.EventKind.THREAD_END:
                            assertTrue(eventThreadID != -1);
                            if (event.getRequestID() == threadEndRequestID) {
                                // Our requested THREAD_END event.
                                onThreadStart(eventThreadID);
                            }
                            onThreadEnd(eventThreadID);
                            break;
                        case JDWPConstants.EventKind.BREAKPOINT:
                            assertTrue(eventThreadID != -1);
                            assertTrue(breakpointRequestID != 0);
                            if (event.getRequestID() == breakpointRequestID) {
                                // Our requested BREAKPOINT event.
                                onBreakpoint(eventThreadID);
                                // We want to resume only the event thread.
                                suspendPolicy = JDWPConstants.SuspendPolicy.EVENT_THREAD;
                            }
                            break;
                        case JDWPConstants.EventKind.VM_DEATH:
                            // Last event so stop thread.
                            eventThreadRun = false;
                            break;
                        default:
                            // Ignore
                            break;
                    }
                }
            }
            // Resume according on suspend policy
            if (suspendPolicy == JDWPConstants.SuspendPolicy.ALL) {
                logWriter.println("Resume VM");
                debuggeeWrapper.vmMirror.resume();
            } else if (suspendPolicy == JDWPConstants.SuspendPolicy.EVENT_THREAD) {
                assertTrue(eventThreadID != -1);
                logWriter.println("Resume thread 0x" + Long.toHexString(eventThreadID));
                debuggeeWrapper.vmMirror.resumeThread(eventThreadID);
            } else {
                // Nothing to do.
            }
        }
    }

    /**
     * Overrides inherited method to resume debuggee VM and then to establish
     * sync connection.
     */
    @Override
    protected void internalSetUp() throws Exception {
        super.internalSetUp();

        // Init state.
        eventThreadRun = true;
        threadIds = new HashSet<Long>();
        threadStartRequestID = 0;
        threadEndRequestID = 0;
        breakpointRequestID = 0;
        breakpointEventThreadID = 0;
        synchronizationObject = new Object();

        // Request THREAD_START and THREAD_DEATH events.
        requestThreadStart();
        requestThreadEnd();

        eventReceiverThread = new Thread(new EventReceiver());
        eventReceiverThread.start();

        // Initialize list of thread IDs with the threads currently running.
        ReplyPacket allThreadsReply = debuggeeWrapper.vmMirror.getAllThreadID();
        int threadsCount = allThreadsReply.getNextValueAsInt();
        for (int i = 0; i < threadsCount; ++i) {
            long threadID = allThreadsReply.getNextValueAsThreadID();
            onThreadStart(threadID);
        }
        assertAllDataRead(allThreadsReply);

        debuggeeWrapper.resume();
        logWriter.println("Resumed debuggee VM");

        synchronizer.startServer();
        logWriter.println("Established sync connection");
    }

    /**
     * Overrides inherited method to close sync connection upon exit.
     */
    @Override
    protected void internalTearDown() {

        if (synchronizer != null) {
            synchronizer.stop();
            logWriter.println("Completed sync connection");
        }
        super.internalTearDown();
    }

    @Override
    protected String getDebuggeeClassName() {
        return Breakpoint003Debuggee.class.getName();
    }

    /**
     * 1) Hit breakpoint twice so it suspends all threads (except the event thread) twice using
     *    a ALL suspend policy.
     */
    public void testBreakpoint_ResumeAll() {
        logWriter.println("testBreakpoint_ResumeAll started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Install breakpoint with ALL suspend policy.
        long classID = debuggeeWrapper.vmMirror.getClassID(
                getDebuggeeClassSignature());
        assertTrue("Failed to find debuggee class", classID != -1);

        breakpointRequestID = debuggeeWrapper.vmMirror
                .setBreakpointAtMethodBegin(classID, "breakpointMethod");
        assertTrue("Failed to install breakpoint", breakpointRequestID != -1);

        // Continue debuggee.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for 1st breakpoint hit.
        long eventThreadID = waitForBreakpointAndResumeThreadOnly();
        logWriter.println("Hit 1st breakpoint in thread 0x"
                + Long.toHexString(eventThreadID));

        // Wait for 2nd breakpoint hit.
        long newEventThreadID = waitForBreakpointAndResumeThreadOnly();
        assertEquals("Breakpoint in a different thread", eventThreadID,
                newEventThreadID);
        logWriter.println("Hit 2nd breakpoint in thread 0x"
                + Long.toHexString(eventThreadID));

        // Wait for end of event thread to avoid ERR_THREAD_NOT_ALIVE error.
        waitForThreadEnd(eventThreadID);

        // Breakpoint hit twice so it cause two global suspensions. Therefore
        // all threads have
        // a suspend count == 2. Resume them all individually until their
        // suspend count == 1.
        synchronized (synchronizationObject) {
            for (long threadID : threadIds) {
                int suspendCount = debuggeeWrapper.vmMirror
                        .getThreadSuspendCount(threadID);
                while (suspendCount > 1) {
                    debuggeeWrapper.vmMirror.resumeThread(threadID);
                    suspendCount = debuggeeWrapper.vmMirror
                            .getThreadSuspendCount(threadID);
                }
            }
        }

        // Stop event thread.
        logWriter.println("Stopping event receiver thread...");
        eventThreadRun = false;
        try {
            eventReceiverThread.join();
        } catch (InterruptedException e) {
            // ignore.
        }
        logWriter.println("Event receiver thread is stopped");

        // Now the running threads have a suspend count <= 1, we can send a
        // VirtualMachine.Resume to finish the debuggee.
        debuggeeWrapper.vmMirror.resume();

        // TODO clean this up.
        logWriter.println("Waiting for VM_DEATH event ...");
        debuggeeWrapper.vmMirror.receiveCertainEvent(JDWPConstants.EventKind.VM_DEATH);
        logWriter.println("Received VM_DEATH event.");

        logWriter.println("testBreakpoint_ResumeAll done");
    }

    private void waitForThreadEnd(long eventThreadID) {
        synchronized (synchronizationObject) {
            while (threadIds.contains(Long.valueOf(eventThreadID))) {
                try {
                    synchronizationObject.wait();
                } catch (InterruptedException e) {
                    // TODO(shertz): Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private long waitForBreakpointAndResumeThreadOnly() {
        long threadID = 0;
        synchronized (synchronizationObject) {
            while (breakpointEventThreadID == 0) {
                // TODO apply test timeout
                try {
                    logWriter.println("Wait for breakpoint 0x"
                            + Integer.toHexString(breakpointRequestID));
                    synchronizationObject.wait();
                } catch (InterruptedException e) {
                    // TODO should we stop?
                    e.printStackTrace();
                }
            }
            threadID = breakpointEventThreadID;
            breakpointEventThreadID = 0;
        }
        return threadID;
    }

    private void onBreakpoint(long eventThreadID) {
        synchronized (synchronizationObject) {
            assertEquals("Previous breakpoint thread id bnot cleared", 0,
                    breakpointEventThreadID);
            logWriter.println("Breakpoint hit in thread 0x"
                    + Long.toHexString(eventThreadID));
            breakpointEventThreadID = eventThreadID;
            synchronizationObject.notify();
        }
    }
}

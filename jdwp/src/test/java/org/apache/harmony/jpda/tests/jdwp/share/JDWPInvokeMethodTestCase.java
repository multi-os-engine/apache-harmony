package org.apache.harmony.jpda.tests.jdwp.share;

import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.debuggee.InvokeMethodReturnDebuggee;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.util.List;

/**
 * Base class for tests of ClassType.InvokeMethod and ObjectReference.InvokeMethod.
 */
public abstract class JDWPInvokeMethodTestCase extends JDWPSyncTestCase {

    private static final String BREAKPOINT_METHOD_NAME = "execMethod";

    @Override
    protected final String getDebuggeeClassName() {
        return InvokeMethodReturnDebuggee.class.getName();
    }

    protected abstract ReplyPacket invokeMethod(long receiverID, long typeID, long targetThreadID,
            long targetMethodID, List<? extends Value> args, int options);


    /**
     * Suspends all threads of debuggee on a breakpoint.
     * @param debuggeeTypeID the debuggee class ID
     * @return the event thread ID
     */
    protected long suspendDebuggeeOnBreakpoint(long debuggeeTypeID) {
        int breakpointRequestID =
                debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(debuggeeTypeID,
                        BREAKPOINT_METHOD_NAME);

        // Signal debuggee to continue.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait event
        long targetThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointRequestID);
        assertTrue("Invalid targetThreadID, must be != 0", targetThreadID != 0);

        //  Let's clear event request
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT, 
                breakpointRequestID);

        return targetThreadID;
    }

    protected void finishDebuggee() {
        // Resume debuggee from BREAKPOINT event.
        resumeDebuggee();

        // Continue debuggee so it terminates.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }

}

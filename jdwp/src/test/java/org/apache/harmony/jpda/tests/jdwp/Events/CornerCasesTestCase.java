package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.EventMod;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.Event_BREAKPOINT;
import org.apache.harmony.jpda.tests.framework.jdwp.ParsedEvent.Event_SINGLE_STEP;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

public abstract class CornerCasesTestCase extends JDWPSyncTestCase {

    protected long startupFunction(String startClass, String startMethod) {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        int br = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(getClassIDBySignature(startClass), startMethod);
        logWriter.println("=> breakpointID = " + br);

        logWriter.println("=> starting thread");
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        long tr = debuggeeWrapper.vmMirror.waitForBreakpoint(br);
        logWriter.println("=> caught breakpoint, breakpointID = " + br);
        return tr;
    }

    protected void continueFunction(String startClass, String startMethod, String cls, String method) {
        logWriter.println("=> Test started");

        startupFunction(startClass, startMethod);

        int br = debuggeeWrapper.vmMirror.setBreakpointAtMethodEnd(getClassIDBySignature(cls), method);
        logWriter.println("=> breakpointID = " + br);

        logWriter.println("=> resuming thread");
        resumeDebuggee();

        debuggeeWrapper.vmMirror.waitForBreakpoint(br);
        logWriter.println("=> caught breakpoint, breakpointID = " + br);

        logWriter.println("==> resuming thread");
        resumeDebuggee();

        logWriter.println("==> Test PASSED!");
    }

    protected void stepFunction(String startClass, String startMethod, int[] steps, String cls, String method, boolean withBr) {
        stepWithTasksFunction(startClass, startMethod, steps, cls, method, withBr, null);
    }

    protected void stepWithTasksFunction(String startClass, String startMethod, int[] steps, String cls, String method, boolean withBr, Runnable[] after) {
        logWriter.println("=> Test started");

        long tr = startupFunction(startClass, startMethod);

        long typeRef = getClassIDBySignature(cls);

        int brID = 0;
        if (withBr) {
            brID = debuggeeWrapper.vmMirror.setBreakpointAtMethodEnd(typeRef, method);
            logWriter.println("=> breakpointID = " + brID);
        }

        for (int i = 0; i < steps.length; i++) {
            CommandPacket setRequestCommand = new CommandPacket(
                    JDWPCommands.EventRequestCommandSet.CommandSetID,
                    JDWPCommands.EventRequestCommandSet.SetCommand);

            setRequestCommand
                    .setNextValueAsByte(JDWPConstants.EventKind.SINGLE_STEP);
            setRequestCommand.setNextValueAsByte(JDWPConstants.SuspendPolicy.ALL);
            setRequestCommand.setNextValueAsInt(1);
            setRequestCommand.setNextValueAsByte(EventMod.ModKind.Step);
            setRequestCommand.setNextValueAsThreadID(tr);
            setRequestCommand.setNextValueAsInt(JDWPConstants.StepSize.LINE);
            setRequestCommand.setNextValueAsInt(steps[i]);

            logWriter.println("==> Requesting SINGLE_STEP event");
            ReplyPacket setRequestReply = debuggeeWrapper.vmMirror
                    .performCommand(setRequestCommand);

            checkReplyPacket(setRequestReply, "Set SINGLE_STEP event");

            int stepID = setRequestReply.getNextValueAsInt();
            logWriter.println("=> stepID = " + stepID);

            resumeDebuggee();

            logWriter.println("==> Wait for SINGLE_STEP event");
            CommandPacket event = debuggeeWrapper.vmMirror.receiveEvent();
            ParsedEvent[] eventParsed = ParsedEvent.parseEventPacket(event);

            assertEquals("Received wrong number of events", 1, eventParsed.length);
            if (i == steps.length - 1 && withBr) {
                assertEquals("Received wrong kind of event", JDWPConstants.EventKind.BREAKPOINT, eventParsed[0].getEventKind());
                assertEquals("Received event for the wrong request", brID, eventParsed[0].getRequestID());
            } else {
                assertEquals("Received wrong kind of event", JDWPConstants.EventKind.SINGLE_STEP, eventParsed[0].getEventKind());
                assertEquals("Received event for the wrong request", stepID, eventParsed[0].getRequestID());
            }

            logWriter.println("==> Clearing SINGLE_STEP event..");
            ReplyPacket clearRequestReply =
                debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.SINGLE_STEP, stepID);
            checkReplyPacket(clearRequestReply, "Clear SINGLE_STEP event");

            if (i == steps.length - 1) {
                Location location;
                if (withBr) {
                    Event_BREAKPOINT brEvent = (Event_BREAKPOINT)eventParsed[0];
                    location = brEvent.getLocation();
                } else {
                    Event_SINGLE_STEP stepEvent = (Event_SINGLE_STEP)eventParsed[0];
                    location = stepEvent.getLocation();
                }
                assertEquals("Invalid class location after step", typeRef, location.classID);
                assertEquals("Invalid method location after step",
                    debuggeeWrapper.vmMirror.getMethodID(typeRef, method), location.methodID);
            }
        }

        if (after != null) {
            for (Runnable task : after) {
                task.run();
            }
        }

        logWriter.println("==> resuming thread");
        resumeDebuggee();

        logWriter.println("==> Test PASSED!");
    }

}

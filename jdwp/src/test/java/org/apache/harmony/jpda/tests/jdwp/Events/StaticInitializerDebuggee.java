package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.LogWriter;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

public class StaticInitializerDebuggee extends SyncDebuggee {

    public static LogWriter sLogWriter;

    public static void main(String[] args) {
        runDebuggee(StaticInitializerDebuggee.class);
    }

    private static void calledFromRun1() {
        sLogWriter.println("StaticInitializerDebuggee.calledFromRun1 enter");
        calledFromRun2();
        sLogWriter.println("StaticInitializerDebuggee.calledFromRun1 exit");
    }

    private static void calledFromRun2() {
        sLogWriter.println("StaticInitializerDebuggee.calledFromRun2 enter");
        StaticInitializerDebuggeePeer.calledFromOutside1();
        sLogWriter.println("StaticInitializerDebuggee.calledFromRun2 exit");
    }

    @Override
    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        sLogWriter = logWriter;

        logWriter.println("StaticInitializerDebuggee started");

        logWriter.println("Will call " + StaticInitializerDebuggeePeer.class.getSimpleName() + ".calledFromRun1()");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        calledFromRun1();

        logWriter.println("StaticInitializerDebuggee finished");
    }

}

package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

public class NativeMethodDebuggee extends SyncDebuggee {

    public static void main(String[] args) {
        runDebuggee(NativeMethodDebuggee.class);
    }

    public void calledFromRun1() {
        logWriter.println("StaticInitializerDebuggee.calledFromRun1 enter");
        calledFromRun2();
        logWriter.println("StaticInitializerDebuggee.calledFromRun1 exit");
    }

    public void calledFromRun2() {
        logWriter.println("StaticInitializerDebuggee.calledFromRun2 enter");
        caller();
        logWriter.println("StaticInitializerDebuggee.calledFromRun2 exit");
    }

    public native void caller();

    public void calledFromCaller1() {
        logWriter.println("StaticInitializerDebuggee.calledFromCaller1 enter");
        calledFromCaller2();
        logWriter.println("StaticInitializerDebuggee.calledFromCaller1 exit");
    }

    public void calledFromCaller2() {
        logWriter.println("StaticInitializerDebuggee.calledFromCaller2 enter");
        logWriter.println("StaticInitializerDebuggee.calledFromCaller2 exit");
    }

    public void calledFromCaller3() {
        logWriter.println("StaticInitializerDebuggee.calledFromCaller3 enter");
        calledFromCaller4();
        logWriter.println("StaticInitializerDebuggee.calledFromCaller3 exit");
    }

    public void calledFromCaller4() {
        logWriter.println("StaticInitializerDebuggee.calledFromCaller4 enter");
        logWriter.println("StaticInitializerDebuggee.calledFromCaller4 exit");
    }

    @Override
    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("NativeMethodDebuggee started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        calledFromRun1();

        logWriter.println("NativeMethodDebuggee finished");
    }

}

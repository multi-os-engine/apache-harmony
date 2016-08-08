package org.apache.harmony.jpda.tests.jdwp.Events;


public class StaticInitializerDebuggeePeer {

    private static void calledFromStaticInitializer1() {
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromStaticInitializer1 enter");
        calledFromStaticInitializer2();
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromStaticInitializer1 exit");
    }

    private static void calledFromStaticInitializer2() {
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromStaticInitializer2 enter");
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromStaticInitializer2 exit");
    }

    static {
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.<clinit> enter");
        calledFromStaticInitializer1();
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.<clinit> exit");
    }

    public static void calledFromOutside1() {
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromOutside1 enter");
        calledFromOutside2();
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromOutside1 exit");
    }

    private static void calledFromOutside2() {
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromOutside2 enter");
        StaticInitializerDebuggee.sLogWriter.println("StaticInitializerDebuggeePeer.calledFromOutside2 exit");
    }

}

package org.apache.harmony.jpda.tests.jdwp.InterfaceType;

import org.apache.harmony.jpda.tests.framework.LogWriter;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.JPDATestOptions;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * Created by allight on 2/19/16.
 */
public class InvokeMethodDebuggee extends SyncDebuggee implements InvokeMethodTestInterface {

    void execMethod() {
        logWriter.println("InvokeMethodDebuggee.execMethod()");
    }

    @Override
    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        logWriter.println("InvokeMethodDebuggee");
        synchronizer.receiveMessageWithoutException("org.apache.harmony.jpda.tests.jdwp.InterfaceType.InvokeMethodDebuggee(#1)");
        execMethod();
        synchronizer.receiveMessageWithoutException("org.apache.harmony.jpda.tests.jdwp.InterfaceType.InvokeMethodDebuggee(#2)");
    }
    public static void main(String[] args) {
        runDebuggee(InvokeMethodDebuggee.class);
    }
}

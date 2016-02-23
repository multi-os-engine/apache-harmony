package org.apache.harmony.jpda.tests.jdwp.InterfaceType;

import org.apache.harmony.jpda.tests.framework.LogWriter;
import org.apache.harmony.jpda.tests.share.JPDATestOptions;

/**
 * Created by allight on 2/19/16.
 */
public interface InvokeMethodTestInterface {
    default void foo() {}
    public static int testInvokeMethodStatic1(boolean needsThrow) throws Throwable {
        if (needsThrow) {
            throw new Throwable("test exception");
        }
        return 567;
    }
}

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

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for BREAKPOINT event in methods possibly inlined.
 */
public class Breakpoint002Test extends JDWPEventTestCase {
    private static final String debuggeeSignature = "Lorg/apache/harmony/jpda/tests/jdwp/Events/Breakpoint002Debuggee;";

    protected String getDebuggeeClassName() {
        return Breakpoint002Debuggee.class.getName();
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointReturnVoid
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedReturnVoid() {
        logWriter.println("testInlinedReturnVoid started");
        testBreakpointIn("breakpointReturnVoid");
        logWriter.println("testInlinedReturnVoid done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointReturnIntConst
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedReturnIntConstant() {
        logWriter.println("testInlinedReturnIntConstant started");
        testBreakpointIn("breakpointReturnIntConst");
        logWriter.println("testInlinedReturnIntConstant done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointReturnLongConst
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedReturnLongConstant() {
        logWriter.println("testInlinedReturnLongConstant started");
        testBreakpointIn("breakpointReturnLongConst");
        logWriter.println("testInlinedReturnLongConstant done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointReturnIntArg
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedReturnIntArgument() {
        logWriter.println("testInlinedReturnIntArgument started");
        testBreakpointIn("breakpointReturnIntArg");
        logWriter.println("testInlinedReturnIntArgument done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointReturnLongArg
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedReturnLongArgument() {
        logWriter.println("testInlinedReturnLongArgument started");
        testBreakpointIn("breakpointReturnLongArg");
        logWriter.println("testInlinedReturnLongArgument done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointReturnObjectArg
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedReturnObjectArgument() {
        logWriter.println("testInlinedReturnObjectArgument started");
        testBreakpointIn("breakpointReturnObjectArg");
        logWriter.println("testInlinedReturnObjectArgument done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointIntGetter
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedIntGetter() {
        logWriter.println("testInlinedIntGetter started");
        testBreakpointIn("breakpointIntGetter");
        logWriter.println("testInlinedIntGetter done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointLongGetter
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedLongGetter() {
        logWriter.println("testInlinedLongGetter started");
        testBreakpointIn("breakpointLongGetter");
        logWriter.println("testInlinedLongGetter done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointObjectGetter
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedObjectGetter() {
        logWriter.println("testInlinedObjectGetter started");
        testBreakpointIn("breakpointObjectGetter");
        logWriter.println("testInlinedObjectGetter done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointIntSetter
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedIntSetter() {
        logWriter.println("testInlinedIntSetter started");
        testBreakpointIn("breakpointIntSetter");
        logWriter.println("testInlinedIntSetter done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointLongSetter
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedLongSetter() {
        logWriter.println("testInlinedLongSetter started");
        testBreakpointIn("breakpointLongSetter");
        logWriter.println("testInlinedLongSetter done");
    }

    /**
     * This testcase is for BREAKPOINT event.
     * <BR>It runs Breakpoint002Debuggee and set breakpoint to its breakpointObjectSetter
     * method, then verifies that requested BREAKPOINT event occurs.
     */
    public void testInlinedObjectSetter() {
        logWriter.println("testInlinedObjectSetter started");
        testBreakpointIn("breakpointObjectSetter");
        logWriter.println("testInlinedObjectSetter done");
    }

    private void testBreakpointIn(String methodName) {
      long classID = debuggeeWrapper.vmMirror.getClassID(debuggeeSignature);
      assertTrue("Failed to find debuggee class", classID != -1);

      synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);
      long breakpointReqID = debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(classID, methodName);
      assertTrue("Failed to install breakpoint in method " + methodName, breakpointReqID != -1);
      synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

      debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointReqID);
    }
}

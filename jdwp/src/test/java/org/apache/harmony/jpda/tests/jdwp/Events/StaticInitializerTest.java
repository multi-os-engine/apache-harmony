package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;

public class StaticInitializerTest extends CornerCasesTestCase {

    @Override
    protected String getDebuggeeClassName() {
        return StaticInitializerDebuggee.class.getName();
    }

    private final String debugeeSignature = "L" + getDebuggeeClassName().replaceAll("\\.", "/") + ";";
    private final String debugeePeerSignature = "L" + StaticInitializerDebuggeePeer.class.getName().replaceAll("\\.", "/") + ";";

    private final String clInitSignature = "<clinit>";
    private final String originalCalledSignature = "calledFromOutside1";
    private final String downcallOfOriginalCalledSignature = "calledFromOutside2";
    private final String originalCallerSignature = "calledFromRun2";
    private final String upcallOfOriginalCallerSignature = "calledFromRun1";
    private final String downcallOfClInitSignature = "calledFromStaticInitializer1";
    private final String downcallOfDowncallOfClInitSignature = "calledFromStaticInitializer2";

    private void continueFromClInitFunction(String cls, String method) {
        continueFunction(debugeePeerSignature, clInitSignature, cls, method);
    }

    private void stepFromClInitFunction(int[] steps, String cls, String method, boolean withBr) {
        stepFunction(debugeePeerSignature, clInitSignature, steps, cls, method, withBr);
    }

    private void stepFromOriginalCallerFunction(int[] steps, String cls, String method, boolean withBr) {
        stepFunction(debugeeSignature, originalCallerSignature, steps, cls, method, withBr);
    }

    private void continueFromOriginalCallerFunction(String cls, String method) {
        continueFunction(debugeeSignature, originalCallerSignature, cls, method);
    }


    /*
     * Tests for continue command from <clinit>.
     */

    public void testContinueToOriginalCallerFromClInit() {
        continueFromClInitFunction(debugeeSignature, originalCallerSignature);
    }

    public void testContinueToUpcallOfOriginalCallerFromClInit() {
        continueFromClInitFunction(debugeeSignature, upcallOfOriginalCallerSignature);
    }

    public void testContinueToOriginalCalledFromClInit() {
        continueFromClInitFunction(debugeePeerSignature, originalCalledSignature);
    }

    public void testContinueToDowncallOfOriginalCalledFromCLInit() {
        continueFromClInitFunction(debugeePeerSignature, downcallOfOriginalCalledSignature);
    }

    public void testContinueToDowncallOfClInitFromCLInit() {
        continueFromClInitFunction(debugeePeerSignature, downcallOfClInitSignature);
    }

    public void testContinueToDowncallOfDowncallOfClInitFromCLInit() {
        continueFromClInitFunction(debugeePeerSignature, downcallOfDowncallOfClInitSignature);
    }


    /*
     * Tests for step over command from <clinit>.
     */

    public void testStepOverToDowncallOfClInitFromCLInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, downcallOfClInitSignature, true);
    }

    public void testStepOverToDowncallOfDowncallOfClInitFromCLInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, downcallOfDowncallOfClInitSignature, true);
    }

    public void testStepOverToOriginalCalledFromClInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, originalCalledSignature, false);
    }


    /*
     * Tests for step out command from <clinit>.
     */

    public void testStepOutToDowncallOfClInitFromCLInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, downcallOfClInitSignature, true);
    }

    public void testStepOutToDowncallOfDowncallOfClInitFromCLInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, downcallOfDowncallOfClInitSignature, true);
    }

    public void testStepOutToOriginalCalledFromClInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, originalCalledSignature, true);
    }

    public void testStepOutToDowncallOriginalCalledFromClInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, downcallOfOriginalCalledSignature, true);
    }

    public void testStepOutToOriginalCallerFromClInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeeSignature, originalCallerSignature, false);
    }


    /*
     * Tests for step in command from <clinit>.
     */

    public void testStepInToDowncallOfClInitFromCLInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, debugeePeerSignature, downcallOfClInitSignature, false);
    }

    public void testStepInToOriginalCalledFromCLInit() {
        stepFromClInitFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, debugeePeerSignature, originalCalledSignature, false);
    }


    /*
     * Tests for step in command from original caller.
     */

    public void testStepInToClInitFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, debugeePeerSignature, clInitSignature, false);
    }


    /*
     * Tests for continue command from original caller.
     */

    public void testContinueToClInitFromOriginalCaller() {
        continueFromOriginalCallerFunction(debugeePeerSignature, clInitSignature);
    }

    public void testContinueToOriginalCalledFromOriginalCaller() {
        continueFromOriginalCallerFunction(debugeePeerSignature, originalCalledSignature);
    }

    public void testContinueToDowncallOfOriginalCalledFromOriginalCaller() {
        continueFromOriginalCallerFunction(debugeePeerSignature, downcallOfOriginalCalledSignature);
    }

    public void testContinueToDowncallOfClInitFromOriginalCaller() {
        continueFromOriginalCallerFunction(debugeePeerSignature, downcallOfClInitSignature);
    }

    public void testContinueToDowncallOfDowncallOfClInitFromOriginalCaller() {
        continueFromOriginalCallerFunction(debugeePeerSignature, downcallOfDowncallOfClInitSignature);
    }


    /*
     * Tests for step over command from original caller.
     */

    public void testStepOverToClInitFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, clInitSignature, true);
    }

    public void testStepOverToOriginalCalledFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, originalCalledSignature, true);
    }

    public void testStepOverToDowncallOfOriginalCalledFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, downcallOfOriginalCalledSignature, true);
    }

    public void testStepOverToDowncallOfClInitFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, downcallOfClInitSignature, true);
    }

    public void testStepOverToDowncallOfDowncallOfClInitFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, debugeePeerSignature, downcallOfDowncallOfClInitSignature, true);
    }


    /*
     * Tests for step out command from original caller.
     */

    public void testStepOutToClInitFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, clInitSignature, true);
    }

    public void testStepOutToOriginalCalledFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, originalCalledSignature, true);
    }

    public void testStepOutToDowncallOfOriginalCalledFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, downcallOfOriginalCalledSignature, true);
    }

    public void testStepOutToDowncallOfClInitFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, downcallOfClInitSignature, true);
    }

    public void testStepOutToDowncallOfDowncallOfClInitFromOriginalCaller() {
        stepFromOriginalCallerFunction(new int[]{JDWPConstants.StepDepth.OUT}, debugeePeerSignature, downcallOfDowncallOfClInitSignature, true);
    }

}

package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;

public class NativeMethodTest extends CornerCasesTestCase {

    @Override
    protected String getDebuggeeClassName() {
        return NativeMethodDebuggee.class.getName();
    }

    private final String debugeeSignature = "L" + getDebuggeeClassName().replaceAll("\\.", "/") + ";";

    private final String calledFromRun1Signature = "calledFromRun1";
    private final String calledFromRun2Signature = "calledFromRun2";
    private final String calledFromCaller1Signature = "calledFromCaller1";
    private final String calledFromCaller2Signature = "calledFromCaller2";
    private final String calledFromCaller3Signature = "calledFromCaller3";
    private final String calledFromCaller4Signature = "calledFromCaller4";

    private void stepAndContinueFunction(String start, int[] steps, String method, boolean withBr, final String method2) {
        stepWithTasksFunction(debugeeSignature, start, steps, debugeeSignature, method, withBr, new Runnable[]{
                new Runnable() {
                    @Override
                    public void run() {
                        int br = debuggeeWrapper.vmMirror.setBreakpointAtMethodEnd(getClassIDBySignature(debugeeSignature), method2);
                        logWriter.println("=> breakpointID = " + br);

                        logWriter.println("=> resuming thread");
                        resumeDebuggee();

                        debuggeeWrapper.vmMirror.waitForBreakpoint(br);
                        logWriter.println("=> caught breakpoint, breakpointID = " + br);
                    }
                }
        });
    }

    private void continueFromCalledFromRun2Function(String method) {
        continueFunction(debugeeSignature, calledFromRun2Signature, debugeeSignature, method);
    }

    private void stepFromCalledFromRun2Function(int[] steps, String method, boolean withBr) {
        stepFunction(debugeeSignature, calledFromRun2Signature, steps, debugeeSignature, method, withBr);
    }

    private void continueFromCalledFromCaller1Function(String method) {
        continueFunction(debugeeSignature, calledFromCaller1Signature, debugeeSignature, method);
    }

    private void stepFromCalledFromCaller1Function(int[] steps, String method, boolean withBr) {
        stepFunction(debugeeSignature, calledFromCaller1Signature, steps, debugeeSignature, method, withBr);
    }

    private void continueFromCalledFromCaller2Function(String method) {
        continueFunction(debugeeSignature, calledFromCaller2Signature, debugeeSignature, method);
    }

    private void stepFromCalledFromCaller2Function(int[] steps, String method, boolean withBr) {
        stepFunction(debugeeSignature, calledFromCaller2Signature, steps, debugeeSignature, method, withBr);
    }

    private void stepAndContinueFromCalledFromCaller2Function(int[] steps, String method, boolean withBr, final String method2) {
        stepAndContinueFunction(calledFromCaller2Signature, steps, method, withBr, method2);
    }

    private void continueFromCalledFromCaller3Function(String method) {
        continueFunction(debugeeSignature, calledFromCaller3Signature, debugeeSignature, method);
    }

    private void stepFromCalledFromCaller3Function(int[] steps, String method, boolean withBr) {
        stepFunction(debugeeSignature, calledFromCaller3Signature, steps, debugeeSignature, method, withBr);
    }

    private void continueFromCalledFromCaller4Function(String method) {
        continueFunction(debugeeSignature, calledFromCaller4Signature, debugeeSignature, method);
    }

    private void stepFromCalledFromCaller4Function(int[] steps, String method, boolean withBr) {
        stepFunction(debugeeSignature, calledFromCaller4Signature, steps, debugeeSignature, method, withBr);
    }

    private void stepAndContinueFromCalledFromCaller4Function(int[] steps, String method, boolean withBr, final String method2) {
        stepAndContinueFunction(calledFromCaller4Signature, steps, method, withBr, method2);
    }


    /*
     * Tests for continue command from calledFromRun2.
     */

    public void testContinueToCalledFromRun1FromCalledFromRun2() {
        continueFromCalledFromRun2Function(calledFromRun2Signature);
    }

    public void testContinueToCalledFromCaller1FromCalledFromRun2() {
        continueFromCalledFromRun2Function(calledFromCaller1Signature);
    }

    public void testContinueToCalledFromCaller2FromCalledFromRun2() {
        continueFromCalledFromRun2Function(calledFromCaller2Signature);
    }

    public void testContinueToCalledFromCaller3FromCalledFromRun2() {
        continueFromCalledFromRun2Function(calledFromCaller3Signature);
    }

    public void testContinueToCalledFromCaller4FromCalledFromRun2() {
        continueFromCalledFromRun2Function(calledFromCaller4Signature);
    }


    /*
     * Tests for step in command from calledFromRun2.
     */

    public void testStepInToCalledFromCaller1FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromCaller1Signature, false);
    }


    /*
     * Tests for step out command from calledFromRun2.
     */

    public void testStepOutToCalledFromCaller1FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller1Signature, true);
    }

    public void testStepOutToCalledFromCaller2FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller2Signature, true);
    }

    public void testStepOutToCalledFromCaller3FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller3Signature, true);
    }

    public void testStepOutToCalledFromCaller4FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller4Signature, true);
    }


    /*
     * Tests for step over command from calledFromRun2.
     */

    public void testStepOverToCalledFromCaller1FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller1Signature, true);
    }

    public void testStepOverToCalledFromCaller2FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller2Signature, true);
    }

    public void testStepOverToCalledFromCaller3FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller3Signature, true);
    }

    public void testStepOverToCalledFromCaller4FromCalledFromRun2() {
        stepFromCalledFromRun2Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller4Signature, true);
    }


    /*
     * Tests for continue command from calledFromCaller1.
     */

    public void testContinueToCalledFromRun1FromCalledFromCaller1() {
        continueFromCalledFromCaller1Function(calledFromRun1Signature);
    }

    public void testContinueToCalledFromRun2FromCalledFromCaller1() {
        continueFromCalledFromCaller1Function(calledFromRun2Signature);
    }

    public void testContinueToCalledFromCaller2FromCalledFromCaller1() {
        continueFromCalledFromCaller1Function(calledFromCaller2Signature);
    }

    public void testContinueToCalledFromCaller3FromCalledFromCaller1() {
        continueFromCalledFromCaller1Function(calledFromCaller3Signature);
    }

    public void testContinueToCalledFromCaller4FromCalledFromCaller1() {
        continueFromCalledFromCaller1Function(calledFromCaller4Signature);
    }


    /*
     * Tests for step out command from calledFromCaller1.
     */

    public void testStepOutToCalledFromRun2FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromRun2Signature, false);
    }

    public void testStepOutToCalledFromCaller2FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller2Signature, true);
    }

    public void testStepOutToCalledFromCaller3FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller3Signature, true);
    }

    public void testStepOutToCalledFromCaller4FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller4Signature, true);
    }


    /*
     * Tests for step over command from calledFromCaller1.
     */

    public void testStepOverToCalledFromCaller2FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller2Signature, true);
    }

    public void testStepOverToCalledFromCaller3FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller3Signature, false);
    }


    /*
     * Tests for step in command from calledFromCaller1.
     */

    public void testStepInToCalledFromCaller2FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromCaller2Signature, false);
    }

    public void testStepInToCalledFromCaller3FromCalledFromCaller1() {
        stepFromCalledFromCaller1Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromCaller3Signature, false);
    }


    /*
     * Tests for continue command from calledFromCaller2.
     */

    public void testContinueToCalledFromRun1FromCalledFromCaller2() {
        continueFromCalledFromCaller2Function(calledFromRun1Signature);
    }

    public void testContinueToCalledFromRun2FromCalledFromCaller2() {
        continueFromCalledFromCaller2Function(calledFromRun2Signature);
    }

    public void testContinueToCalledFromCaller1FromCalledFromCaller2() {
        continueFromCalledFromCaller2Function(calledFromCaller1Signature);
    }

    public void testContinueToCalledFromCaller3FromCalledFromCaller2() {
        continueFromCalledFromCaller2Function(calledFromCaller3Signature);
    }

    public void testContinueToCalledFromCaller4FromCalledFromCaller2() {
        continueFromCalledFromCaller2Function(calledFromCaller4Signature);
    }


    /*
     * Tests for step out command from calledFromCaller2.
     */

    public void testStepOutToCalledFromCaller1FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller1Signature, false);
    }


    /*
     * Tests for step over command from calledFromCaller2.
     */

    public void testStepOverToCalledFromCaller1FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller1Signature, false);
    }


    /*
     * Tests for step in command from calledFromCaller2.
     */

    public void testStepInToCalledFromCaller1FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromCaller1Signature, false);
    }


    /*
     * Tests for combined commands from calledFromCaller2.
     */

    public void testIndirectStepOutToCalledFromCaller3FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OUT}, calledFromCaller3Signature, true);
    }

    public void testIndirectStepOutToCalledFromCaller4FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OUT}, calledFromCaller4Signature, true);
    }

    public void testIndirectStepOutToCalledFromRun2FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OUT}, calledFromRun2Signature, false);
    }

    public void testIndirectStepInToCalledFromCaller3FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromCaller3Signature, false);
    }

    public void testIndirectStepOverToCalledFromCaller3FromCalledFromCaller2() {
        stepFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller3Signature, false);
    }

    public void testIndirectContinueToCalledFromCaller3FromCalledFromCaller2() {
        stepAndContinueFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller1Signature, false, calledFromCaller3Signature);
    }

    public void testIndirectContinueToCalledFromCaller4FromCalledFromCaller2() {
        stepAndContinueFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller1Signature, false, calledFromCaller4Signature);
    }

    public void testIndirectContinueToCalledFromRun2FromCalledFromCaller2() {
        stepAndContinueFromCalledFromCaller2Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller1Signature, false, calledFromRun2Signature);
    }


    /*
     * Tests for continue command from calledFromCaller3.
     */

    public void testContinueToCalledFromRun1FromCalledFromCaller3() {
        continueFromCalledFromCaller3Function(calledFromRun1Signature);
    }

    public void testContinueToCalledFromRun2FromCalledFromCaller3() {
        continueFromCalledFromCaller3Function(calledFromRun2Signature);
    }

    public void testContinueToCalledFromCaller4FromCalledFromCaller3() {
        continueFromCalledFromCaller3Function(calledFromCaller4Signature);
    }


    /*
     * Tests for step out command from calledFromCaller3.
     */

    public void testStepOutToCalledFromCaller4FromCalledFromCaller3() {
        stepFromCalledFromCaller3Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller4Signature, true);
    }

    public void testStepOutToCalledFromRun2FromCalledFromCaller3() {
        stepFromCalledFromCaller3Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromRun2Signature, false);
    }


    /*
     * Tests for step over command from calledFromCaller3.
     */

    public void testStepOverToCalledFromCaller4FromCalledFromCaller3() {
        stepFromCalledFromCaller3Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller4Signature, true);
    }

    public void testStepOverToCalledFromRun2FromCalledFromCaller3() {
        stepFromCalledFromCaller3Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromRun2Signature, false);
    }


    /*
     * Tests for step in command from calledFromCaller3.
     */

    public void testStepInToCalledFromCaller4FromCalledFromCaller3() {
        stepFromCalledFromCaller3Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromCaller4Signature, false);
    }

    public void testStepInToCalledFromRun2FromCalledFromCaller3() {
        stepFromCalledFromCaller3Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromRun2Signature, false);
    }


    /*
     * Tests for continue command from calledFromCaller4.
     */

    public void testContinueToCalledFromRun1FromCalledFromCaller4() {
        continueFromCalledFromCaller4Function(calledFromRun1Signature);
    }

    public void testContinueToCalledFromRun2FromCalledFromCaller4() {
        continueFromCalledFromCaller4Function(calledFromRun2Signature);
    }

    public void testContinueToCalledFromCaller3FromCalledFromCaller4() {
        continueFromCalledFromCaller4Function(calledFromCaller3Signature);
    }


    /*
     * Tests for step out command from calledFromCaller4.
     */

    public void testStepOutToCalledFromCaller3FromCalledFromCaller4() {
        stepFromCalledFromCaller4Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller3Signature, false);
    }


    /*
     * Tests for step over command from calledFromCaller4.
     */

    public void testStepOverToCalledFromCaller3FromCalledFromCaller4() {
        stepFromCalledFromCaller4Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromCaller3Signature, false);
    }


    /*
     * Tests for step in command from calledFromCaller4.
     */

    public void testStepInToCalledFromCaller3FromCalledFromCaller4() {
        stepFromCalledFromCaller4Function(new int[]{JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromCaller3Signature, false);
    }


    /*
     * Tests for combined commands from calledFromCaller4.
     */

    public void testIndirectStepOutToCalledFromRun2FromCalledFromCaller4() {
        stepFromCalledFromCaller4Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OUT}, calledFromRun2Signature, false);
    }

    public void testIndirectStepInToCalledFromRun2FromCalledFromCaller4() {
        stepFromCalledFromCaller4Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.INTO}, calledFromRun2Signature, false);
    }

    public void testIndirectStepOverToCalledFromRun2FromCalledFromCaller4() {
        stepFromCalledFromCaller4Function(new int[]{JDWPConstants.StepDepth.OUT, JDWPConstants.StepDepth.OVER, JDWPConstants.StepDepth.OVER}, calledFromRun2Signature, false);
    }

    public void testIndirectContinueToCalledFromRun2FromCalledFromCaller4() {
        stepAndContinueFromCalledFromCaller4Function(new int[]{JDWPConstants.StepDepth.OUT}, calledFromCaller3Signature, false, calledFromRun2Signature);
    }

}

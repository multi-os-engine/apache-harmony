package org.apache.harmony.jpda.tests.jdwp.share;

import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.debuggee.InvokeMethodReturnDebuggee;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * Base class for tests of ClassType.InvokeMethod and ObjectReference.InvokeMethod.
 */
public abstract class JDWPInvokeMethodTestCase extends JDWPSyncTestCase {

    private static final String BREAKPOINT_METHOD_NAME = "breakpointMethod";

    protected static final String STRING_OBJECT_FIELD_NAME = "STRING_OBJECT";
    protected static final String ARRAY_OBJECT_FIELD_NAME = "ARRAY_OBJECT";
    protected static final String THREAD_OBJECT_FIELD_NAME = "THREAD_OBJECT";
    protected static final String THREAD_GROUP_OBJECT_FIELD_NAME = "THREAD_GROUP_OBJECT";
    protected static final String CLASS_OBJECT_FIELD_NAME = "CLASS_OBJECT";
    protected static final String CLASS_LOADER_OBJECT_FIELD_NAME = "CLASS_LOADER_OBJECT";

    private long typeID;
    private long targetThreadID;

    @Override
    protected final String getDebuggeeClassName() {
        return InvokeMethodReturnDebuggee.class.getName();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        printTestLog("START");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        // Suspend debuggee on an event so we can invoke method.
        typeID = getClassIDBySignature(getDebuggeeClassSignature());

        // Request breakpoint.
        int breakpointRequestID =
                debuggeeWrapper.vmMirror.setBreakpointAtMethodBegin(typeID, BREAKPOINT_METHOD_NAME);

        // Signal debuggee to continue.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint event.
        targetThreadID = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointRequestID);
        assertTrue("Invalid targetThreadID, must be != 0", targetThreadID != 0);

        // Let's clear event request before testing.
        debuggeeWrapper.vmMirror.clearEvent(JDWPConstants.EventKind.BREAKPOINT,
                                            breakpointRequestID);
    }

    @Override
    protected void tearDown() throws Exception {
        //  Let's resume application
        finishDebuggee();
        printTestLog("END");

        super.tearDown();
    }

    /**
     * Common method to test ClassType.InvokeMethod (for static methods) and
     * ObjectReference.InvokeMethod for instance method.
     *
     * <p>It first starts the InvokeMethodReturnDebuggee and suspends it on a BREAKPOINT event.
     * It sets the expected value into a field that is used by the tested method. Then it does
     * the following checks:
     * <ul>
     * <li>sends either ClassType.InvokeMethod or ObjectReference.InvokeMethod command for the
     * tested method which does not throw any exception, then checks that returned value is the
     * expected value and returned exception object is null</li>
     * <li>sends either ClassType.InvokeMethod or ObjectReference.InvokeMethod command for the
     * tested method which does throw an uncaught exception, then checks that returned value is 0
     * and returned exception object is not null and has expected attributes</li>
     * </ul>
     * </p>
     *
     * @param tag
     *          the JDWP tag of the method's return type
     * @param isStatic
     *          true if the invoked method is static, false otherwise
     * @param expectedValue
     *      the expected return value
     */
    // TODO we could infer the tag from the expected value. Not for objects
    protected void runInvokeMethodTest(byte tag, boolean isStatic, Value expectedValue) {
        // We set the EXPECTED_<TYPE>_FIELD with the expected value. Then, when the tested method
        // executes, it will return the value of this field.
        String expectedValueFieldName = getExpectedValueFieldName(tag);
        setExpectedFieldValue(typeID, expectedValueFieldName, expectedValue);

        String methodName = getTestMethodName(tag, isStatic);
        long targetMethodID = getMethodID(typeID, methodName);

        // Test InvokeMethod without Exception.
        ReplyPacket reply = invokeMethod(typeID, targetThreadID, targetMethodID, false);
        Value returnValue = reply.getNextValueAsValue();
        TaggedObject exception = reply.getNextValueAsTaggedObject();
        assertAllDataRead(reply);

        assertNotNull("Returned value is null", returnValue);
        assertEquals("Invalid return value", expectedValue, returnValue);
        assertNotNull("Returned exception is null", exception);
        assertTrue("Invalid exception object ID:<" + exception.objectID + ">",
                exception.objectID == 0);
        assertTagEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag);

        // Test InvokeMethod with Exception.
        reply = invokeMethod(typeID, targetThreadID, targetMethodID, true);
        returnValue = reply.getNextValueAsValue();
        exception = reply.getNextValueAsTaggedObject();
        assertAllDataRead(reply);

        assertNotNull("Returned value is null", returnValue);
        assertTagEquals("Invalid tag for return value,", tag, returnValue.getTag());
        assertNotNull("Returned exception is null", exception);
        assertTrue("Invalid exception object ID:<" + exception.objectID + ">",
                exception.objectID != 0);
        assertTagEquals("Invalid exception tag,", JDWPConstants.Tag.OBJECT_TAG, exception.tag);
    }

    /**
     * Invokes a method in a suspended thread.
     *
     * @param typeID
     *          the type ID of the debuggee class
     * @param targetThreadID
     *          the ID of the suspended thread that invokes the method
     * @param targetMethodID
     *          the ID of the invoked method
     * @param withException
     *          true if the invoked method needs to throw an exception, false otherwise.
     * @return a reply packet
     */
    protected abstract ReplyPacket invokeMethod(long typeID, long targetThreadID,
            long targetMethodID, boolean withException);

    /**
     * Sets the debuggee's static field identified by the given name with the given value.
     *
     * @param value
     *          the new static field value
     */
    private void setExpectedFieldValue(long classID, String expectedValueFieldName, Value value) {
        long fieldID = checkField(classID, expectedValueFieldName);
        CommandPacket packet = new CommandPacket(
                JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.SetValuesCommand);
        packet.setNextValueAsReferenceTypeID(classID);
        packet.setNextValueAsInt(1);
        packet.setNextValueAsFieldID(fieldID);
        packet.setNextValueAsUntaggedValue(value);

        ReplyPacket replyPacket = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(replyPacket, "ClassType.SetValues");
    }

    private void finishDebuggee() {
        // Resume debuggee from BREAKPOINT event.
        resumeDebuggee();

        // Continue debuggee so it terminates.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }

    protected long getStaticFieldObjectId(String fieldName) {
        long classID = getClassIDBySignature(getDebuggeeClassSignature());
        long fieldID = checkField(classID, fieldName);
        Value fieldValue = debuggeeWrapper.vmMirror.getReferenceTypeValue(classID, fieldID);
        return fieldValue.getLongValue();
    }

    private static String getTestMethodName(byte tag, boolean isStatic) {
        StringBuilder builder = new StringBuilder("test");
        builder.append(isStatic ? "Static" : "Instance");
        switch (tag) {
            case JDWPConstants.Tag.BOOLEAN_TAG:
                builder.append("Boolean");
                break;
            case JDWPConstants.Tag.BYTE_TAG:
                builder.append("Byte");
                break;
            case JDWPConstants.Tag.CHAR_TAG:
                builder.append("Char");
                break;
            case JDWPConstants.Tag.SHORT_TAG:
                builder.append("Short");
                break;
            case JDWPConstants.Tag.INT_TAG:
                builder.append("Int");
                break;
            case JDWPConstants.Tag.FLOAT_TAG:
                builder.append("Float");
                break;
            case JDWPConstants.Tag.LONG_TAG:
                builder.append("Long");
                break;
            case JDWPConstants.Tag.DOUBLE_TAG:
                builder.append("Double");
                break;
            case JDWPConstants.Tag.OBJECT_TAG:
                builder.append("Object");
                break;
            case JDWPConstants.Tag.ARRAY_TAG:
            case JDWPConstants.Tag.CLASS_LOADER_TAG:
            case JDWPConstants.Tag.CLASS_OBJECT_TAG:
            case JDWPConstants.Tag.STRING_TAG:
            case JDWPConstants.Tag.THREAD_TAG:
            case JDWPConstants.Tag.THREAD_GROUP_TAG:
                throw new IllegalArgumentException("Only use OBJECT_TAG instead of "
                        + JDWPConstants.Tag.getName(tag));
            default:
                throw new TestErrorException(
                        "Unsupported field type " + JDWPConstants.Tag.getName(tag));
        }
        return builder.toString();
    }

    protected String getExpectedValueFieldName(byte tag) {
        StringBuilder builder = new StringBuilder();
        switch (tag) {
            case JDWPConstants.Tag.BOOLEAN_TAG:
                builder.append("boolean");
                break;
            case JDWPConstants.Tag.BYTE_TAG:
                builder.append("byte");
                break;
            case JDWPConstants.Tag.CHAR_TAG:
                builder.append("char");
                break;
            case JDWPConstants.Tag.SHORT_TAG:
                builder.append("short");
                break;
            case JDWPConstants.Tag.INT_TAG:
                builder.append("int");
                break;
            case JDWPConstants.Tag.FLOAT_TAG:
                builder.append("float");
                break;
            case JDWPConstants.Tag.LONG_TAG:
                builder.append("long");
                break;
            case JDWPConstants.Tag.DOUBLE_TAG:
                builder.append("double");
                break;
            case JDWPConstants.Tag.OBJECT_TAG:
                builder.append("object");
                break;
            case JDWPConstants.Tag.ARRAY_TAG:
            case JDWPConstants.Tag.CLASS_LOADER_TAG:
            case JDWPConstants.Tag.CLASS_OBJECT_TAG:
            case JDWPConstants.Tag.STRING_TAG:
            case JDWPConstants.Tag.THREAD_TAG:
            case JDWPConstants.Tag.THREAD_GROUP_TAG:
                throw new IllegalArgumentException("Only use OBJECT_TAG instead of "
                        + JDWPConstants.Tag.getName(tag));
            default:
                throw new TestErrorException(
                        "Unsupported field type " + JDWPConstants.Tag.getName(tag));
        }
        builder.append("Result");
        return builder.toString();
    }
}

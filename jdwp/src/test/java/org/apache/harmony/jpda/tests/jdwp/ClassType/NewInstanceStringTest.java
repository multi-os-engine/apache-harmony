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

package org.apache.harmony.jpda.tests.jdwp.ClassType;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPTestConstants;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

import java.util.ArrayList;
import java.util.List;

/**
 * JDWP unit test for ClassType.NewInstance command with java.lang.String class.
 */
public class NewInstanceStringTest extends JDWPSyncTestCase {
    private static final String CONSTRUCTOR_NAME = "<init>";

    /**
     * A provider is responsible for giving the arguments passed to the tested
     * constructor.
     */
    private static interface ConstructorArgumentsProvider {
        /**
         * This method is called to provide the arguments tot he constructor
         * called to create the java.lang.String instance. This is called when
         * the debuggee is suspended on a breakpoint.
         *
         * @param constructorArguments
         *            the list of arguments passed to the constructor
         */
        public void provideConstructorArguments(List<Value> constructorArguments);
    }

    @Override
    protected String getDebuggeeClassName() {
        return NewInstanceStringDebuggee.class.getName();
    }

    /**
     * Test ClassType.NewInstance using the constructor java.lang.String().
     */
    public void testNewInstanceString_NoArgConstructor() {
        runTestNewInstanceString("()V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // No argument.
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(byte[])</code>.
     */
    public void testNewInstanceString_ByteArrayArgConstructor() {
        runTestNewInstanceString("([B)V", new ConstructorArgumentsProvider() {

            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to BYTE_ARRAY static field.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long fieldId = checkField(debuggeeClassId, "BYTE_ARRAY");
                Value fieldValue = debuggeeWrapper.vmMirror.getReferenceTypeValue(debuggeeClassId,
                        fieldId);
                constructorArguments.add(fieldValue);
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(byte[],
     * int, int)</code>.
     */
    public void testNewInstanceString_ByteArrayIntIntConstructor() {
        runTestNewInstanceString("([BII)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to BYTE_ARRAY static field.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long fieldId = checkField(debuggeeClassId, "BYTE_ARRAY");
                Value fieldValue = debuggeeWrapper.vmMirror.getReferenceTypeValue(debuggeeClassId,
                        fieldId);
                constructorArguments.add(fieldValue);
                constructorArguments.add(new Value(0));
                constructorArguments.add(new Value(1));
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(byte[],
     * int, int, java.lang.String)</code>.
     */
    public void testNewInstanceString_ByteArrayIntIntStringConstructor() {
        runTestNewInstanceString("([BIILjava/lang/String;)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to BYTE_ARRAY and STRING_CHARSET static
                // fields.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long byteArrayFieldId = checkField(debuggeeClassId, "BYTE_ARRAY");
                long stringCharsetFieldId = checkField(debuggeeClassId, "STRING_CHARSET");
                Value byteArrayValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, byteArrayFieldId);
                Value stringCharsetValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, stringCharsetFieldId);
                constructorArguments.add(byteArrayValue);
                constructorArguments.add(new Value(0));
                constructorArguments.add(new Value(1));
                constructorArguments.add(stringCharsetValue);
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(byte[],
     * java.lang.String)</code>.
     */
    public void testNewInstanceString_ByteArrayStringConstructor() {
        runTestNewInstanceString("([BLjava/lang/String;)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to BYTE_ARRAY and STRING_CHARSET static
                // fields.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long byteArrayFieldId = checkField(debuggeeClassId, "BYTE_ARRAY");
                long stringCharsetFieldId = checkField(debuggeeClassId, "STRING_CHARSET");
                Value byteArrayValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, byteArrayFieldId);
                Value stringCharsetValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, stringCharsetFieldId);
                constructorArguments.add(byteArrayValue);
                constructorArguments.add(stringCharsetValue);
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(byte[],
     * int, int, java.nio.charset.Charset)</code>.
     */
    public void testNewInstanceString_ByteArrayIntIntCharsetConstructor() {
        runTestNewInstanceString("([BIILjava/nio/charset/Charset;)V",
                new ConstructorArgumentsProvider() {
                    @Override
                    public void provideConstructorArguments(List<Value> constructorArguments) {
                        // Pass a reference to BYTE_ARRAY and CHARSET static
                        // fields.
                        long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                        long byteArrayFieldId = checkField(debuggeeClassId, "BYTE_ARRAY");
                        long charsetFieldId = checkField(debuggeeClassId, "CHARSET");
                        Value byteArrayValue = debuggeeWrapper.vmMirror
                                .getReferenceTypeValue(debuggeeClassId, byteArrayFieldId);
                        Value charsetValue = debuggeeWrapper.vmMirror
                                .getReferenceTypeValue(debuggeeClassId, charsetFieldId);
                        constructorArguments.add(byteArrayValue);
                        constructorArguments.add(new Value(0));
                        constructorArguments.add(new Value(1));
                        constructorArguments.add(charsetValue);
                    }
                });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(byte[],
     * java.nio.charset.Charset)</code>.
     */
    public void testNewInstanceString_ByteArrayCharsetConstructor() {
        runTestNewInstanceString("([BLjava/nio/charset/Charset;)V",
                new ConstructorArgumentsProvider() {
                    @Override
                    public void provideConstructorArguments(List<Value> constructorArguments) {
                        // Pass a reference to BYTE_ARRAY and CHARSET static
                        // fields.
                        long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                        long byteArrayFieldId = checkField(debuggeeClassId, "BYTE_ARRAY");
                        long charsetFieldId = checkField(debuggeeClassId, "CHARSET");
                        Value byteArrayValue = debuggeeWrapper.vmMirror
                                .getReferenceTypeValue(debuggeeClassId, byteArrayFieldId);
                        Value charsetValue = debuggeeWrapper.vmMirror
                                .getReferenceTypeValue(debuggeeClassId, charsetFieldId);
                        constructorArguments.add(byteArrayValue);
                        constructorArguments.add(charsetValue);
                    }
                });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(char[])</code>.
     */
    public void testNewInstanceString_CharArrayConstructor() {
        runTestNewInstanceString("([C)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to CHAR_ARRAY static field.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long charArrayFieldId = checkField(debuggeeClassId, "CHAR_ARRAY");
                Value charArrayFieldValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, charArrayFieldId);
                constructorArguments.add(charArrayFieldValue);
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(char[],
     * int, int)</code>.
     */
    public void testNewInstanceString_CharArrayIntIntConstructor() {
        runTestNewInstanceString("([CII)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to CHAR_ARRAY static field.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long charArrayFieldId = checkField(debuggeeClassId, "CHAR_ARRAY");
                Value charArrayFieldValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, charArrayFieldId);
                constructorArguments.add(charArrayFieldValue);
                constructorArguments.add(new Value(0));
                constructorArguments.add(new Value(1));
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(java.lang.String)</code>.
     */
    public void testNewInstanceString_StringConstructor() {
        runTestNewInstanceString("(Ljava/lang/String;)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to TEST_STRING static field.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long testStringFieldId = checkField(debuggeeClassId, "TEST_STRING");
                Value testStringValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, testStringFieldId);
                constructorArguments.add(testStringValue);
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(java.lang.StringBuffer)</code>.
     */
    public void testNewInstanceString_StringBufferConstructor() {
        runTestNewInstanceString("(Ljava/lang/StringBuffer;)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to STRING_BUFFER static field.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long stringBufferFieldId = checkField(debuggeeClassId, "STRING_BUFFER");
                Value stringBufferValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, stringBufferFieldId);
                constructorArguments.add(stringBufferValue);
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(int[],
     * int, int)</code>.
     */
    public void testNewInstanceString_IntArrayIntIntConstructor() {
        runTestNewInstanceString("([III)V", new ConstructorArgumentsProvider() {
            @Override
            public void provideConstructorArguments(List<Value> constructorArguments) {
                // Pass a reference to INT_ARRAY static field.
                long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                long intArrayFieldId = checkField(debuggeeClassId, "INT_ARRAY");
                Value intArrayFieldValue = debuggeeWrapper.vmMirror
                        .getReferenceTypeValue(debuggeeClassId, intArrayFieldId);
                constructorArguments.add(intArrayFieldValue);
                constructorArguments.add(new Value(0));
                constructorArguments.add(new Value(1));
            }
        });
    }

    /**
     * Test ClassType.NewInstance using the constructor
     * <code>java.lang.String(java.lang.StringBuilder)</code>.
     */
    public void testNewInstanceString_StringBuilderConstructor() {
        runTestNewInstanceString("(Ljava/lang/StringBuilder;)V",
                new ConstructorArgumentsProvider() {
                    @Override
                    public void provideConstructorArguments(List<Value> constructorArguments) {
                        // Pass a reference to STRING_BUILDER static field.
                        long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
                        long stringBuilderFieldId = checkField(debuggeeClassId, "STRING_BUILDER");
                        Value stringBuilderValue = debuggeeWrapper.vmMirror
                                .getReferenceTypeValue(debuggeeClassId, stringBuilderFieldId);
                        constructorArguments.add(stringBuilderValue);
                    }
                });
    }

    /**
     * This testcase exercises ClassType.NewInstance command for
     * java.lang.String. At first, the test starts debuggee. Then request a
     * breakpoint and wait for it. Once the debuggee is suspended on the
     * breakpoint, send ClassType.NewInstance command for the java.lang.String
     * class using the constructor whose signature is given as parameter. A
     * provider is responsible to provide the arguments for the specified
     * constructor as JDWP values. Finally, the test verifies that the returned
     * object is not null and the exception object is null.
     */
    private void runTestNewInstanceString(String constructorSignature,
            ConstructorArgumentsProvider provider) {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long debuggeeClassId = getClassIDBySignature(getDebuggeeClassSignature());
        logWriter.println("Debuggee class: " + getDebuggeeClassSignature());
        logWriter.println("Debuggee class ID: " + debuggeeClassId);

        // Request breakpoint.
        int breakpointRequestId = debuggeeWrapper.vmMirror
                .setBreakpointAtMethodBegin(debuggeeClassId, "breakpointMethod");

        // Continue debuggee.
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        // Wait for breakpoint.
        long threadId = debuggeeWrapper.vmMirror.waitForBreakpoint(breakpointRequestId);

        long javaLangStringId = getClassIDBySignature("Ljava/lang/String;");
        assertTrue("Failed to find java.lang.String class", javaLangStringId != -1);
        logWriter.println("java.lang.String class ID: " + javaLangStringId);

        final String methodName = CONSTRUCTOR_NAME;
        final String methodSignature = constructorSignature;
        final String fullMethodName = methodName + methodSignature;
        long constructorId = getMethodID(javaLangStringId, methodName, methodSignature);
        assertTrue("Failed to find constructor " + fullMethodName, constructorId != -1);
        logWriter.println(fullMethodName + " method ID: " + constructorId);

        // Request provider to fill the arguments list.
        List<Value> argumentsList = new ArrayList<Value>();
        provider.provideConstructorArguments(argumentsList);

        logWriter
                .println("Sending ClassType.NewInstance command for constructor " + fullMethodName);
        CommandPacket packet = new CommandPacket(JDWPCommands.ClassTypeCommandSet.CommandSetID,
                JDWPCommands.ClassTypeCommandSet.NewInstanceCommand);
        packet.setNextValueAsReferenceTypeID(javaLangStringId);
        packet.setNextValueAsThreadID(threadId);
        packet.setNextValueAsMethodID(constructorId);
        packet.setNextValueAsInt(argumentsList.size()); // argCount
        for (Value value : argumentsList) {
            packet.setNextValueAsValue(value);
        }
        packet.setNextValueAsInt(0); // invoke options
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "ClassType.NewInstance command");

        // Check result.
        TaggedObject stringResult = reply.getNextValueAsTaggedObject();
        TaggedObject exceptionResult = reply.getNextValueAsTaggedObject();
        assertAllDataRead(reply);

        assertNotNull("stringResult is null", stringResult);
        assertNotNull("exceptionResult is null", exceptionResult);
        assertTrue(stringResult.objectID != JDWPTestConstants.NULL_OBJECT_ID);
        assertTrue(exceptionResult.tag == JDWPConstants.Tag.OBJECT_TAG);
        assertEquals(exceptionResult.objectID, JDWPTestConstants.NULL_OBJECT_ID);

        // Debuggee is suspended on the breakpoint: resume it now.
        resumeDebuggee();
    }
}

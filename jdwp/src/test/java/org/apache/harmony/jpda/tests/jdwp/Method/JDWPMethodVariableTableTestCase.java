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

package org.apache.harmony.jpda.tests.jdwp.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for Method.VariableTable command.
 */
class JDWPMethodVariableTableTestCase extends JDWPMethodTestCase {

    private final Map<String, VariableTableChecker> checkerMap =
            new HashMap<String, VariableTableChecker>();

    @Override
    protected final String getDebuggeeClassName() {
        return MethodVariableTestDebuggee.class.getName();
    }

    protected JDWPMethodVariableTableTestCase() {
        // Static methods
        {
            VariableTableChecker checker = new VariableTableChecker("static_NoParam");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_BooleanParam");
            checker.addParameter("booleanParam", "Z");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_ByteParam");
            checker.addParameter("byteParam", "B");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_CharParam");
            checker.addParameter("charParam", "C");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_ShortParam");
            checker.addParameter("shortParam", "S");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_IntParam");
            checker.addParameter("intParam", "I");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_FloatParam");
            checker.addParameter("floatParam", "F");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_LongParam");
            checker.addParameter("longParam", "J");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_DoubleParam");
            checker.addParameter("doubleParam", "D");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_ObjectParam");
            checker.addParameter("objectParam", "Ljava/lang/Object;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_StringParam");
            checker.addParameter("stringParam", "Ljava/lang/String;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_ListOfStringsParam");
            checker.addParameter("listOfStringsParam", "Ljava/util/List;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_GenericParam");
            checker.addParameter("genericParam", "Ljava/lang/CharSequence;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_IntArrayParam");
            checker.addParameter("intArrayParam", "[I");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("static_StringMultiArrayParam");
            checker.addParameter("stringMultiArrayParam", "[[Ljava/lang/String;");
            addCheckerToMap(checker);
        }
        // Instance methods
        {
            VariableTableChecker checker = new VariableTableChecker("instance_NoParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_BooleanParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("booleanParam", "Z");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_ByteParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("byteParam", "B");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_CharParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("charParam", "C");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_ShortParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("shortParam", "S");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_IntParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("intParam", "I");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_FloatParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("floatParam", "F");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_LongParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("longParam", "J");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_DoubleParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("doubleParam", "D");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_ObjectParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("objectParam", "Ljava/lang/Object;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_StringParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("stringParam", "Ljava/lang/String;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_ListOfStringsParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("listOfStringsParam", "Ljava/util/List;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_GenericParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("genericParam", "Ljava/lang/CharSequence;");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker("instance_IntArrayParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("intArrayParam", "[I");
            addCheckerToMap(checker);
        }
        {
            VariableTableChecker checker = new VariableTableChecker(
                    "instance_StringMultiArrayParam");
            checker.addParameter("this", getDebuggeeClassSignature());
            checker.addParameter("stringMultiArrayParam", "[[Ljava/lang/String;");
            addCheckerToMap(checker);
        }
    }

    private void addCheckerToMap(VariableTableChecker checker) {
        checkerMap.put(checker.methodName, checker);
    }

    /**
     * This testcase exercises Method.VariableTable[WithGeneric] command. <BR>
     * It runs MethodDebuggee, receives methods of debuggee. For each received
     * method sends Method.VariableTable command and checks the returned
     * VariableTable.
     *
     * @param withGenerics
     *            true to test Method.VariableTableWithGeneric, false to test
     *            Method.VariableTable.
     */
    protected void checkMethodVariableTable(boolean withGenerics) {
        final String testName = getName();
        logWriter.println(testName + " started");
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        long classID = getClassIDBySignature(getDebuggeeClassSignature());

        MethodInfo[] methodsInfo = jdwpGetMethodsInfo(classID);
        assertFalse("Invalid number of methods: 0", methodsInfo.length == 0);

        final byte commandCode;
        final String commandName;
        if (withGenerics) {
            // Testing Method.VariableTableWithGeneric command.
            commandCode = JDWPCommands.MethodCommandSet.VariableTableWithGenericCommand;
            commandName = "Method::VariableTableWithGeneric";
        } else {
            // Testing Method.VariableTable command.
            commandCode = JDWPCommands.MethodCommandSet.VariableTableCommand;
            commandName = "Method::VariableTable";
        }

        int checkedMethodsCount = 0;
        for (MethodInfo methodInfo : methodsInfo) {
            logWriter.println(methodInfo.toString());

            // get variable table for this class
            CommandPacket packet = new CommandPacket(JDWPCommands.MethodCommandSet.CommandSetID,
                    commandCode);
            packet.setNextValueAsClassID(classID);
            packet.setNextValueAsMethodID(methodInfo.getMethodID());
            ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
            checkReplyPacket(reply, commandName + " command");

            final int argCnt = reply.getNextValueAsInt();
            logWriter.println("argCnt = " + argCnt);
            final int slots = reply.getNextValueAsInt();
            logWriter.println("slots = " + slots);

            final VariableTableChecker checker = getCheckerForMethod(methodInfo.getName());
            if (checker != null) {
                ++checkedMethodsCount;
                logWriter.println("# Check method \"" + checker.methodName + "\"");

                // Check argCount.
                int expectedArgsCount = checker.getArgsCount();
                assertEquals("Invalid argCount", expectedArgsCount, argCnt);

                // Check number of slots.
                int expectedSlotCount = checker.getSlotCount();
                assertEquals("Invalid argCount", expectedSlotCount, slots);
            }
            int checkedVariablesCount = 0;
            for (int j = 0; j < slots; j++) {
                final long codeIndex = reply.getNextValueAsLong();
                logWriter.println("codeIndex = " + codeIndex);
                final String name = reply.getNextValueAsString();
                logWriter.println("name = \"" + name + "\"");
                final String signature = reply.getNextValueAsString();
                logWriter.println("signature = \"" + signature + "\"");
                String genericSignature = "";
                if (withGenerics) {
                    reply.getNextValueAsString();
                    logWriter.println("genericSignature = \"" + genericSignature + "\"");
                }
                final int length = reply.getNextValueAsInt();
                logWriter.println("length = " + length);
                final int slot = reply.getNextValueAsInt();
                logWriter.println("slot = " + slot);

                if (checker != null) {
                    final VariableInfo variableInfo = checker.getVariableInfo(name);
                    if (variableInfo != null) {
                        ++checkedVariablesCount;
                        logWriter.println("## Check variable \"" + variableInfo.name + "\"");
                        assertEquals("Invalid variable type signature",
                                variableInfo.variableSignature, signature);
                        if (!genericSignature.isEmpty()
                                && variableInfo.variableGenericSignature != null) {
                            // Check generic signature matches the expected one
                            assertEquals(variableInfo.variableGenericSignature, genericSignature);
                        }
                        if (variableInfo.isParameter) {
                            assertEquals("Parameter should start at code index 0", 0, codeIndex);
                        }
                    }
                }
            }

            if (checker != null) {
                // Check that we found all the variables we want to check for the current method.
                assertEquals("Not all variables have been checked for method " + checker.methodName,
                        checker.variableInfos.size(), checkedVariablesCount);
            }

        }

        // Checks that we found all the methods that we want to check.
        assertEquals("Not all methods have been checked", checkerMap.size(), checkedMethodsCount);

        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println(testName + " ends");
    }

    private VariableTableChecker getCheckerForMethod(String methodName) {
        return checkerMap.get(methodName);
    }

    private static class VariableTableChecker {
        final String methodName;
        private final List<VariableInfo> variableInfos =
                new ArrayList<JDWPMethodVariableTableTestCase.VariableInfo>();

        public VariableTableChecker(String methodName) {
            this.methodName = methodName;
        }

        private void addVariableInfo(VariableInfo variableInfo) {
            variableInfos.add(variableInfo);
        }

        public void addParameter(String name, String signature) {
            addParameter(name, signature, null);
        }

        public void addParameter(String name, String signature, String genericSignature) {
            addVariableInfo(new VariableInfo(true, name, signature, genericSignature));
        }

        public int getArgsCount() {
            int argsCount = 0;
            for (VariableInfo variableInfo : variableInfos) {
                if (variableInfo.isParameter) {
                    if (variableInfo.variableSignature.equals("J")
                            || variableInfo.variableSignature.equals("D")) {
                        // long and double take 2 slots.
                        argsCount += 2;
                    } else {
                        argsCount += 1;
                    }
                }
            }
            return argsCount;
        }

        public int getSlotCount() {
            return variableInfos.size();
        }

        public VariableInfo getVariableInfo(String variableName) {
            for (VariableInfo v : variableInfos) {
                if (v.name.equals(variableName)) {
                    return v;
                }
            }
            return null;
        }
    }

    private static class VariableInfo {
        private final boolean isParameter;
        private final String name;
        private final String variableSignature;
        private final String variableGenericSignature;

        public VariableInfo(boolean isParameter, String variableName, String variableSignature,
                String variableGenericSignature) {
            this.isParameter = isParameter;
            this.name = variableName;
            this.variableSignature = variableSignature;
            this.variableGenericSignature = variableGenericSignature;
        }
    }
}

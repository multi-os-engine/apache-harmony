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

/**
 * @author Anton V. Karnachuk
 */

/**
 * Created on 11.04.2005
 */
package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * Debuggee for FieldAccessTest and FieldModified unit tests.
 * Provides access and modification of testIntField field.
 */
public class FieldDebuggee extends SyncDebuggee {

    public static void main(String[] args) {
        runDebuggee(FieldDebuggee.class);
    }

    // Static fields.
    private static boolean testStaticBooleanField = false;
    private static byte testStaticByteField = 0;
    private static char testStaticCharField = 0;
    private static short testStaticShortField = 0;
    private static int testStaticIntField = 0;
    private static float testStaticFloatField = 0.0f;
    private static long testStaticLongField = 0;
    private static double testStaticDoubleField = 0.0;
    private static Object testStaticObjectField = "a string";

    // Instance fields.
    private boolean testBooleanField = false;
    private byte testByteField = 0;
    private char testCharField = 0;
    private short testShortField = 0;
    private int testIntField = 0;
    private float testFloatField = 0.0f;
    private long testLongField = 0;
    private double testDoubleField = 0.0;
    private Object testObjectField = "a string";

    // Values set into respective fields for FIELD_MODIFICATION event.
    public static boolean EXPECTED_BOOLEAN_VALUE = false;
    public static byte EXPECTED_BYTE_VALUE = 0;
    public static char EXPECTED_CHAR_VALUE = 0;
    public static short EXPECTED_SHORT_VALUE = 0;
    public static int EXPECTED_INT_VALUE = 0;
    public static float EXPECTED_FLOAT_VALUE = 0.0f;
    public static long EXPECTED_LONG_VALUE = 0;
    public static double EXPECTED_DOUBLE_VALUE = 0.0;
    public static Object EXPECTED_OBJECT_VALUE = "another string";
    // TODO consider setting different object to verify tags:
    // CLASS_LOADER, CLASS_OBJECT, STRING, ARRAY, THREAD and THREAD_GROUP.

    @Override
    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("FieldDebuggee started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        modifyFields();
        accessFields();

        logWriter.println("FieldDebuggee finished");
    }

    /**
     * Accesses fields to trigger FIELD_ACCESS event.
     */
    private void accessFields() {
        // Accesses static fields.
        logWriter.println("Access to field testStaticBooleanField: value=" + testStaticBooleanField);
        logWriter.println("Access to field testStaticByteField: value=" + testStaticByteField);
        logWriter.println("Access to field testStaticCharField: value=" + testStaticCharField);
        logWriter.println("Access to field testStaticShortField: value=" + testStaticShortField);
        logWriter.println("Access to field testStaticIntField: value=" + testStaticIntField);
        logWriter.println("Access to field testStaticFloatField: value=" + testStaticFloatField);
        logWriter.println("Access to field testStaticLongField: value=" + testStaticLongField);
        logWriter.println("Access to field testStaticDoubleField: value=" + testStaticDoubleField);
        logWriter.println("Access to field testStaticObjectField: value=" + testStaticObjectField);

        // Accesses instance fields.
        logWriter.println("Access to field testBooleanField: value=" + testBooleanField);
        logWriter.println("Access to field testByteField: value=" + testByteField);
        logWriter.println("Access to field testCharField: value=" + testCharField);
        logWriter.println("Access to field testShortField: value=" + testShortField);
        logWriter.println("Access to field testIntField: value=" + testIntField);
        logWriter.println("Access to field testFloatField: value=" + testFloatField);
        logWriter.println("Access to field testLongField: value=" + testLongField);
        logWriter.println("Access to field testDoubleField: value=" + testDoubleField);
        logWriter.println("Access to field testObjectField: value=" + testObjectField);
    }

    /**
     * Modifies fields to trigger FIELD_MODIFICATION event.
     */
    private void modifyFields() {
        // Modifies static fields.
        testStaticBooleanField = EXPECTED_BOOLEAN_VALUE;
        testStaticByteField = EXPECTED_BYTE_VALUE;
        testStaticCharField = EXPECTED_CHAR_VALUE;
        testStaticShortField = EXPECTED_SHORT_VALUE;
        testStaticIntField = EXPECTED_INT_VALUE;
        testStaticFloatField = EXPECTED_FLOAT_VALUE;
        testStaticLongField = EXPECTED_LONG_VALUE;
        testStaticDoubleField = EXPECTED_DOUBLE_VALUE;
        testStaticObjectField = EXPECTED_OBJECT_VALUE;

        // Modifies instance fields
        testBooleanField = EXPECTED_BOOLEAN_VALUE;
        testByteField = EXPECTED_BYTE_VALUE;
        testCharField = EXPECTED_CHAR_VALUE;
        testShortField = EXPECTED_SHORT_VALUE;
        testIntField = EXPECTED_INT_VALUE;
        testFloatField = EXPECTED_FLOAT_VALUE;
        testLongField = EXPECTED_LONG_VALUE;
        testDoubleField = EXPECTED_DOUBLE_VALUE;
        testObjectField = EXPECTED_OBJECT_VALUE;
    }
}

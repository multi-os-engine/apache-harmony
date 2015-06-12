package org.apache.harmony.jpda.tests.jdwp.Events;

import org.apache.harmony.jpda.tests.framework.TestErrorException;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.TaggedObject;

/**
 * Base class for field watchpoint tests.
 */
public class JDWPFieldModificationEventTestCase extends JDWPEventTestCase {

    @Override
    protected String getDebuggeeClassName() {
        return FieldDebuggee.class.getName();
    }

    protected void checkObjectId(TaggedObject taggedObject,
            boolean isStaticField) {
        if (isStaticField) {
            // Check the returned object is null.
            assertEquals("Invalid object id:", 0, taggedObject.objectID);
        } else {
            // Check that object's class is the expected one.
            // TODO that would fail if we access this fields from a subclass.
            long typeID = getObjectReferenceType(taggedObject.objectID);
            String debuggeeClassSignature = getDebuggeeClassSignature();
            String returnedExceptionSignature = getClassSignature(typeID);
            assertString("Invalid class signature,", debuggeeClassSignature,
                    returnedExceptionSignature);
        }
    }

    protected String getTestFieldName(byte tag, boolean isStatic) {
        StringBuilder builder = new StringBuilder("test");
        // TODO add "Instance"
        builder.append(isStatic ? "Static" : "");
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
        builder.append("Field");
        return builder.toString();
    }

    protected String getExpectedValueFieldName(byte tag) {
        StringBuilder builder = new StringBuilder("EXPECTED_");
        switch (tag) {
            case JDWPConstants.Tag.BOOLEAN_TAG:
                builder.append("BOOLEAN");
                break;
            case JDWPConstants.Tag.BYTE_TAG:
                builder.append("BYTE");
                break;
            case JDWPConstants.Tag.CHAR_TAG:
                builder.append("CHAR");
                break;
            case JDWPConstants.Tag.SHORT_TAG:
                builder.append("SHORT");
                break;
            case JDWPConstants.Tag.INT_TAG:
                builder.append("INT");
                break;
            case JDWPConstants.Tag.FLOAT_TAG:
                builder.append("FLOAT");
                break;
            case JDWPConstants.Tag.LONG_TAG:
                builder.append("LONG");
                break;
            case JDWPConstants.Tag.DOUBLE_TAG:
                builder.append("DOUBLE");
                break;
            case JDWPConstants.Tag.OBJECT_TAG:
                builder.append("OBJECT");
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
        builder.append("_VALUE");
        return builder.toString();
    }

}

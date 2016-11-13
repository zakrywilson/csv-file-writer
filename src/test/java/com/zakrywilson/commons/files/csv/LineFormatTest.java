package com.zakrywilson.commons.files.csv;

import org.junit.Assert;
import org.junit.Test;

public class LineFormatTest {

    @Test
    public void setArgCount() throws Exception {
        LineFormat format = new LineFormat();

        int argCount, target, received;

        // Check default value and expect 1
        target = 1;
        received = format.getArgCount();
        if (received != target) {
            Assert.fail(String.format("Should have returned %d for default value but returned %d",
                        target, received));
        }

        // Pass in -1 but expect 1 to have been set
        argCount = -1;
        format.setArgCount(argCount);
        received = format.getArgCount();
        if (received != target) {
            Assert.fail(String.format("Should have returned %d but returned %d", target, received));
        }

        // Pass in 0 but expect 1 to have been set
        argCount = 0;
        format.setArgCount(argCount);
        received = format.getArgCount();
        if (received != target) {
            Assert.fail(String.format("Should have returned %d but returned %d", target, received));
        }

        // Pass in an int greater than 1 and expect that same value to have been set
        for (int i = 1; i <= 10; i++) {
            argCount = i;
            format.setArgCount(argCount);
            received = format.getArgCount();
            if (received != argCount) {
                Assert.fail(String.format("Should have returned %d but returned %d", argCount, received));
            }
        }
    }

    @Test
    public void setEndOfLine() throws Exception {
        LineFormat format = new LineFormat();

        String newline, target, received;

        // Check default value and expect system's default
        target = System.lineSeparator();
        received = format.getEndOfLine();
        if (!received.equals(target)) {
            Assert.fail("Should have returned system's default line break for default value");
        }

        // Pass in null but expect the system's default
        newline = null;
        format.setEndOfLine(newline);
        received = format.getEndOfLine();
        if (!received.equals(target)) {
            Assert.fail("Should have returned system's default line break for a null value");
        }

        // Pass in an empty string but expect the system's default
        newline = "";
        format.setEndOfLine(newline);
        received = format.getEndOfLine();
        if (!received.equals(target)) {
            Assert.fail("Should have returned system's default line break for an empty string");
        }

        // Pass in \n and expect to it to be set
        newline = "\n";
        target = "\n";
        format.setEndOfLine(newline);
        received = format.getEndOfLine();
        if (!received.equals(target)) {
            Assert.fail("Should have returned \\n");
        }

        // Pass in \r and expect to it to be set
        newline = "\r";
        target = "\r";
        format.setEndOfLine(newline);
        received = format.getEndOfLine();
        if (!received.equals(target)) {
            Assert.fail("Should have returned \\r");
        }

        // Pass in \r\n and expect to it to be set
        newline = "\r\n";
        target = "\r\n";
        format.setEndOfLine(newline);
        received = format.getEndOfLine();
        if (!received.equals(target)) {
            Assert.fail("Should have returned \\r\\n");
        }
    }

    @Test
    public void setUsePadding() throws Exception {
        LineFormat format = new LineFormat();

        // Default value should be "false"
        if (format.isUsingPadding()) {
            Assert.fail("Default usePadding boolean value should be 'false'");
        }

        // Set to true, expect true
        format.setUsePadding(true);
        if (!format.isUsingPadding()) {
            Assert.fail("usePadding boolean value should have been 'true'");
        }

        // Set to false, expect false
        format.setUsePadding(false);
        if (format.isUsingPadding()) {
            Assert.fail("usePadding boolean value should have been 'false'");
        }
    }

    @Test
    public void get() throws Exception {

    }

}
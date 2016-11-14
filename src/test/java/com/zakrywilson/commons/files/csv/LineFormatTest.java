package com.zakrywilson.commons.files.csv;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link LineFormat}.
 *
 * @author Zach Wilson
 */
public class LineFormatTest {

    /**
     * Tests {@link LineFormat#setColumns(int)}.
     */
    @Test
    public void setColumns() {
        LineFormat format = new LineFormat(1);

        int argCount, target, received;

        // Check default value and expect 1
        target = 1;
        received = format.getColumns();
        if (received != target) {
            Assert.fail(String.format("Should have returned %d for default value but returned %d",
                        target, received));
        }

        // Pass in -1 but expect 1 to have been set
        argCount = -1;
        format.setColumns(argCount);
        received = format.getColumns();
        if (received != target) {
            Assert.fail(String.format("Should have returned %d but returned %d", target, received));
        }

        // Pass in 0 but expect 1 to have been set
        argCount = 0;
        format.setColumns(argCount);
        received = format.getColumns();
        if (received != target) {
            Assert.fail(String.format("Should have returned %d but returned %d", target, received));
        }

        // Pass in an int greater than 1 and expect that same value to have been set
        for (int i = 1; i <= 10; i++) {
            argCount = i;
            format.setColumns(argCount);
            received = format.getColumns();
            if (received != argCount) {
                Assert.fail(String.format("Should have returned %d but returned %d", argCount, received));
            }
        }
    }

    /**
     * Tests {@link LineFormat#setEndOfLine(String)}.
     */
    @Test
    public void setEndOfLine() {
        LineFormat format = new LineFormat(1);

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

        // Pass in an empty string and expect it to be set
        newline = "";
        target = "";
        format.setEndOfLine(newline);
        received = format.getEndOfLine();
        if (!received.equals(target)) {
            Assert.fail("Should have returned an empty string");
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

    /**
     * Tests {@link LineFormat#setDelimiter(String)}.
     */
    @Test
    public void setDelimiter() {
        LineFormat format = new LineFormat(1);

        // Default value should be a comma
        if (!format.getDelimiter().equals(",")) {
            Assert.fail("Default delimiter should be a comma: " + format.getDelimiter());
        }

        // Set delimiter to a tab character
        String target = "\t";
        format.setDelimiter(target);
        if (!format.getDelimiter().equals(target)) {
            Assert.fail("Delimiter value should have been a tab character: " + format.getDelimiter());
        }

        // Set delimiter to a comma and space character sequence
        target = ", ";
        format.setDelimiter(target);
        if (!format.getDelimiter().equals(target)) {
            Assert.fail("Delimiter value should have been '" + target + "': " + format.getDelimiter());
        }
    }

    /**
     * Tests {@link LineFormat#get()}.
     */
    @Test
    public void get() {
        Assert.fail("Test not yet implemented");
    }

}
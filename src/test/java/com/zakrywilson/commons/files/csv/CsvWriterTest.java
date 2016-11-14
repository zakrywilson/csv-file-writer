package com.zakrywilson.commons.files.csv;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Tests {@link CsvWriter}.
 *
 * @author Zach Wilson
 */
public class CsvWriterTest {

    /**
     * Tests {@link CsvWriter#write(String...)} by running through several tests where different
     * numbers of rows and columns and different delimiters are used.
     *
     * @throws IOException if an I/O error occurs that does not pertain to the code being tested
     */
    @Test
    public void test() throws IOException {
        runTest(10, 3, ",");
        runTest(11, 4, ", ");
        runTest(8, 6, "\t");
        runTest(8, 6, "  ");
        runTest(8, 6, "; ");
    }

    /** Simple date format used for timestamps on file names. */
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * Gets a new timestamp in the format <tt>yyyyMMddHHmmssSSS</tt>.
     *
     * @return a new timestamp
     */
    private static String getTimestamp() {
        return FORMATTER.format(new Date());
    }

    /**
     * Gets a new <tt>String[]</tt> with the provided number of rows and columns.
     * <p>
     * The format for each element in the matrix is <tt>rowX-columnY</tt> where <tt>X</tt> is the
     * row number and <tt>Y</tt> is the column number, 0-based.
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return the matrix
     */
    private static String[][] getStringMatrix(int rows, int columns) {
        String[][] elements = new String[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                elements[i][j] = String.format("row%d-column%d", i, j);
            }
        }
        return elements;
    }

    /**
     * Creates a list of strings that are expected to exist as each row in the CSV file.
     * <p>
     * Concatenates each row in the matrix with the specified delimiter to represent all expected
     * lines in the CSV file.
     *
     * @param matrix the matrix to have its rows concatenated
     * @param delimiter the delimiter of which glues together each column in a given row
     * @return the list of target lines
     */
    private static List<String> getTargetLines(String[][] matrix, String delimiter) {
        List<String> lines = new ArrayList<>();
        for (String[] row : matrix) {
            String line = "";
            boolean firstElement = true;
            for (String column : row) {
                if (firstElement) {
                    line += column;
                    firstElement = false;
                } else {
                    line += delimiter + column;
                }
            }
            lines.add(line);
        }
        return lines;
    }

    /**
     * Creates a new temporary file with the name <tt>file-X.csv</tt> where <tt>X</tt> is the
     * timestamp. See {@link CsvWriterTest#FORMATTER} for the specific format. This temporary file
     * will be deleted when the JVM terminates.
     *
     * @return a new temp file
     * @throws IOException if the file cannot be created
     */
    private static File createTempFile() throws IOException {
        File file = new File("src/test/file-" + getTimestamp() + ".csv");
        if (!file.createNewFile()) {
            throw new RuntimeException("Failed to create new file for testing: " + file);
        }
        if (!file.exists()) {
            throw new RuntimeException("File does not exist: " + file);
        }
        file.deleteOnExit();
        return file;
    }

    /**
     * Runs a single test that tests the ability to accurately write out a CSV file with a
     * variable number of rows and columns with a specified delimiter.
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @param delimiter the delimiter separating columns
     * @throws IOException if an I/O error occurs
     */
    private static void runTest(int rows, int columns, String delimiter) throws IOException {
        // Create the test elements
        String[][] elements = getStringMatrix(rows, columns);

        // Create the target lines
        List<String> targetLines = getTargetLines(elements, delimiter);

        // Create the CSV file
        File file = createTempFile();

        // Write to the CSV file
        try (CsvWriter writer = new CsvWriter(file, columns)) {
            writer.setDelimiter(delimiter);
            for (String[] row : elements) {
                writer.write(row);
            }
        } catch (IOException e) {
            Assert.fail("Encountered exception: " + e.getMessage());
        }

        // Read in the CSV file
        List<String> resultLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        // Test to ensure that the result lines match the target lines
        if (resultLines.size() != rows) {
            Assert.fail(
                    String.format("Did not produce the correct number of rows: expected %d, received %d",
                            rows, resultLines.size()));
        }

        // Go through each line and verify that the line matches the expected string
        Iterator<String> resultIterator = resultLines.iterator();
        Iterator<String> targetIterator = targetLines.iterator();
        while (resultIterator.hasNext() && targetIterator.hasNext()) {
            String result = resultIterator.next();
            String target = targetIterator.next();
            if (!result.equals(target)) {
                Assert.fail(
                        String.format("Result row '%s' does not match target row '%s'",
                                result, target));
            }
        }
    }

}
package com.zakrywilson.commons.files.csv;

import com.sun.istack.internal.Nullable;

/**
 * Represents the format for a single line in a CSV file.
 * <p>
 * Configurable attributes to a <tt>LineFormat</tt>: the expected column count, the delimiter, and
 * the line break. The column count is the number of columns expected for a given line (i.e., the
 * number of <tt>%s</tt>'s). The default delimiter is a single comma with no surrounding white
 * space. For example, using the default delimiter would result in a string <tt>%s,%s,%s</tt>. The
 * delimiter can be changed to anything such as a tab character. The line break can be one of the
 * following: <tt>\n</tt>, <tt>\r</tt>, <tt>\r\n</tt> or an empty string (i.e., no newline). The
 * newline is defaulted to the system's default newline character or character sequence.
 *
 * @author Zach Wilson
 */
final class LineFormat {

    /**
     * The number of columns in the CSV file. Default is <tt>1</tt> and the value should never be
     * less than <tt>1</tt> because a format must contain at least one <tt>%s</tt>.
     */
    private int columns = 1;

    /**
     * The delimiter. Default is a comma.
     */
    private String delimiter = ",";

    /**
     * The end-of-line character used in the CSV file. Default is the system's line break.
     */
    private String endOfLine = System.lineSeparator();

    /**
     * Determines whether the format used to write to the CSV file is ready. The formatter will
     * need to be updated after a change to the configuration which is done through methods such as
     * {@link LineFormat#setColumns(int)}. If a change like this is made, the formatter will
     * need to update {@link LineFormat#format} before being used again.
     */
    private boolean updateNeeded = true;

    /**
     * The <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">format
     * string</a> used to format a single line in the file.
     */
    private String format;

    /**
     * Constructs a new <tt>LineFormat</tt> with default attributes.
     *
     * @param expectedNumberOfColumns the number of columns to be expected
     */
    LineFormat(int expectedNumberOfColumns) {
        setColumns(expectedNumberOfColumns);
    }

    /**
     * Returns a <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">
     * format string</a> used to format a single line in a file. If any of the configurable fields
     * have been updated since this method was last called, the format will be updated. This is done
     * so the format is only updated when necessary so it is more efficient.
     * <p>
     * The returned format string will be characterized by the column count (the expected number of
     * arguments, corresponding to the number of <tt>%s</tt>'s needed), the delimiter between each
     * separated item (a comma is the default), and the type of line break: <tt>\n</tt>,
     * <tt>\r</tt>, or <tt>\r\n</tt>.
     *
     * @return the format string
     * @throws IllegalArgumentException if the column count is not variable and does not equal
     * {@link LineFormat#columns}
     */
    String get() {
        // Do not re-create the format string if no updates have been made
        if (!updateNeeded) {
            return format;
        }

        // Create the format string
        format = "%s";
        if (columns > 1) {
            for (int i = 1; i < columns; i++) {
                format += delimiter + "%s";
            }
        }
        format += endOfLine;
        updateNeeded = false;
        return format;
    }

    /**
     * Gets the expected column count.
     *
     * @return the expected column count
     */
    int getColumns() {
        return columns;
    }

    /**
     * Sets the number of expected columns in the CSV file.
     * <p>
     * If <tt>n</tt> is less than or equal to <tt>1</tt>, the value is ignored. This is enforced
     * because a format must contain at least one <tt>%s</tt>.
     *
     * @param n the number of <tt>%s</tt>'s
     */
    void setColumns(int n) {
        if (n == columns) {
            return;
        }
        if (n > 1) {
            columns = n;
            updateNeeded = true;
        }
    }

    /**
     * Gets the delimiter.
     *
     * @return the delimiter
     */
    String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the character delimiter. The default is a comma.
     * <p>
     * If <tt>s</tt> is <tt>null</tt>, the string is ignored and the previous (or default if not
     * previously set) delimiter will be used.
     *
     * @param s the delimiter to be set
     */
    void setDelimiter(@Nullable String s) {
        if (s == null || s.length() == 0 || s.equals(delimiter)) {
            return;
        }
        delimiter = s;
        updateNeeded = true;
    }

    /**
     * Gets the newline character or sequence of characters for the line.
     *
     * @return the line break
     */
    String getEndOfLine() {
        return endOfLine;
    }

    /**
     * Sets the newline character or sequence of characters for the line.
     * <p>
     * The system's default is used if the line break is not set, the provided line break is
     * <tt>null</tt>, or if the character or sequence of characters is not one of the following:
     * <tt>\n</tt>, <tt>\r\n</tt>, or <tt>\r</tt>. An empty string will result in <b>no</b> line
     * break being used.
     *
     * @param s the newline character or sequence of characters to be set
     */
    void setEndOfLine(@Nullable String s) {
        if (s == null || s.equals(endOfLine)) {
            return;
        }
        if (s.equals("\n") || s.equals("\r\n") || s.equals("\r") || s.equals("")) {
            endOfLine = s;
            updateNeeded = true;
        }
    }

    @Override
    public String toString() {
        return get();
    }

}

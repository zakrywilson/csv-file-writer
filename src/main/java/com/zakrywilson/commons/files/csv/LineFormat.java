package com.zakrywilson.commons.files.csv;

import com.sun.istack.internal.Nullable;

/**
 * Represents the format for a single line in a CSV file.
 * <p>
 * Configurable attributes to a <tt>LineFormat</tt>: the arg count, the use of padding, and the
 * line break. The arg count is how many <tt>%s</tt>'s will be in a single line. More specifically,
 * an arg count of <tt>3</tt> would result in a line with three <tt>%s</tt>'s, separated by commas
 * (i.e., <tt>%s,%s,%s</tt>). The padding is a single space between a comma and a proceeding
 * <tt>%s</tt>. For example, using padding would result in a string <tt>%s, %s, %s</tt> and without
 * padding would result in <tt>%s,%s,%s</tt>. The line break can be one of the following:
 * <tt>\n</tt>, <tt>\r</tt>, <tt>\r\n</tt> or an empty string.
 *
 * @author Zach Wilson
 */
final class LineFormat {

    /**
     * The number of columns in the CSV file. Default is <tt>1</tt> and the value should never be
     * less than <tt>1</tt> because a format must contain at least one <tt>%s</tt>.
     */
    private int argCount = 1;

    /**
     * The end-of-line character used in the CSV file. Default is the system's line break.
     */
    private String endOfLine = System.lineSeparator();

    /**
     * Determines whether the format used to write to the CSV file is ready. The formatter will
     * need to be updated after a change to the configuration which is done through methods such as
     * {@link LineFormat#setArgCount(int)}. If a change like this is made, the formatter will
     * need to update {@link LineFormat#format} before being used again.
     */
    private boolean needsUpdating;

    /**
     * Determines whether a space should be used between a comma and its proceeding character in the
     * CSV file. For example, no padding would be <tt>%s,%s</tt> and with padding would be
     * <tt>%s, %s</tt>.
     */
    private boolean usePadding;

    /**
     * The <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">format
     * string</a> used to format a single line in the file.
     */
    private String format;

    /**
     * Constructs a new <tt>LineFormat</tt> with default attributes.
     */
    LineFormat() {}

    /**
     * Returns a <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">
     * format string</a> used to format a single line in a file. If any of the configurable fields
     * have been updated since this method was last called, the format will be updated. This is done
     * so the format is only updated when necessary so it is more efficient.
     * <p>
     * The returned format string will be characterized by the argument count (the expected number
     * of arguments, corresponding to the number of <tt>%s</tt>'s), the padding between each
     * separated item (no padding is the default), and the type of line break (i.e., <tt>\n</tt>,
     * <tt>\r</tt>, or <tt>\r\n</tt>).
     * <p>
     * Note that if the argument count is set to the default value <tt>1</tt>, that only one
     * <tt>%s</tt> will be included in the format.
     *
     * @return the format string
     */
    String get() {
        if (!needsUpdating) {
            return format;
        }
        format = "%s";
        if (argCount > 0) {
            String delimiter = ",";
            if (usePadding) {
                delimiter += " ";
            }
            for (int i = 1; i < argCount; i++) {
                format +=  delimiter + "%s";
            }
        }
        format += endOfLine;
        needsUpdating = false;
        return format;
    }

    /**
     * Gets the expected argument count.
     *
     * @return the arg count
     */
    int getArgCount() {
        return argCount;
    }

    /**
     * Sets the number of expected columns in the CSV file.
     * <p>
     * If <tt>n</tt> is less than or equal to <tt>1</tt>, the value is ignored. This is enforced
     * because a format must contain at least one <tt>%s</tt>.
     *
     * @param n the number of <tt>%s</tt>'s
     */
    void setArgCount(int n) {
        if (n == argCount) {
            return;
        }
        if (n > 1) {
            argCount = n;
            needsUpdating = true;
        }
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
            needsUpdating = true;
        }
    }

    /**
     * Gets whether a space will be used between two items in the line (i.e., whether a space will
     * separate a comma and its proceeding <tt>%s</tt>.
     *
     * <p>For example, no padding would be <tt>%s,%s</tt> and with padding would be
     * <tt>%s, %s</tt>.
     *
     * @return <tt>true</tt> if padding is being used
     */
    boolean isUsingPadding() {
        return usePadding;
    }

    /**
     * Sets whether a space will be used between two items in the line (i.e., whether a space will
     * separate a comma and its proceeding <tt>%s</tt>.
     *
     * <p>For example, no padding would be <tt>%s,%s</tt> and with padding would be
     * <tt>%s, %s</tt>.
     *
     * @param b boolean if <tt>true</tt>, then a white space will be used to separate elements
     */
    void setUsePadding(boolean b) {
        if (b == usePadding) {
            return;
        }
        usePadding = b;
        needsUpdating = true;
    }

    @Override
    public String toString() {
        return get();
    }

}

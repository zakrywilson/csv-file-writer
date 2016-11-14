package com.zakrywilson.commons.files.csv;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.Formatter;

/**
 * Convenience class for writing CSV files.
 * <p>
 * Class allows for only {@link CsvWriter#write(String...)} to be called to write out a line to the
 * CSV file by only passing in the objects as variable arguments.
 * <p>
 * Configurable attributes of the line are as follows: the column count, the use of padding, and the
 * line break. The column count is how many columns will be in a single line. More specifically, an
 * column count of <tt>3</tt> would expect three elements passed into {@link
 * CsvWriter#write(String...)}, resulting in a string such as <tt>item1,item2,item3</tt> in the CSV
 * file. The padding is a single space between a comma and a proceeding column element. For example,
 * using padding would result in a string <tt>item1, item2, item3</tt> and without padding would
 * result in <tt>item1,item2,item3</tt>. The line break can be one of the following: <tt>\n</tt>,
 * <tt>\r</tt>, <tt>\r\n</tt> or an empty string.
 *
 * @author Zach Wilson
 */
public final class CsvWriter implements AutoCloseable {

    /**
     * The <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">format
     * string</a> used to format a single line in the file.
     */
    private LineFormat format = new LineFormat();

    /**
     * The writer to the CSV file.
     */
    private BufferedWriter writer;

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>String</tt>.
     *
     * @param path the path of the file to be written to
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt> or blank
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull String path) throws IllegalArgumentException, IOException {
        this(path, false);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>String</tt>.
     *
     * @param path the path of the file to be written to
     * @param append if <tt>true</tt>, then bytes will be written to the end of the file rather than
     * the beginning
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt> or blank
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull String path, boolean append) throws IllegalArgumentException, IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if (path.trim().length() < 1) {
            throw new IllegalArgumentException("Path cannot be blank");
        }
        initialize(new File(path), append);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>Path</tt>.
     *
     * @param path the path of the file to be written to
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull Path path) throws IllegalArgumentException, IOException {
        this(path, false);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>Path</tt>.
     *
     * @param path the path of the file to be written to
     * @param append if <tt>true</tt>, then bytes will be written to the end of the file rather than
     * the beginning
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull Path path, boolean append) throws IllegalArgumentException, IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        initialize(path.toFile(), append);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>File</tt>.
     *
     * @param file the file to be written to
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull File file) throws IllegalArgumentException, IOException {
        initialize(file, false);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>File</tt>.
     *
     * @param file the file to be written to
     * @param append if <tt>true</tt>, then bytes will be written to the end of the file rather than
     * the beginning
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull File file, boolean append) throws IllegalArgumentException, IOException {
        initialize(file, append);
    }

    /**
     * Closes the writer.
     *
     * @throws IOException if the writer cannot be closed
     */
    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    /**
     * Sets the number of expected columns in the CSV file.
     * <p>
     * If the column count is set, an {@link IllegalArgumentException} will be thrown if that exact
     * number of arguments is not provided when {@link CsvWriter#write(String...)} is called.
     * <p>
     * Setting this value is optional. The value is defaulted to <tt>0</tt>. If the value is less
     * than <tt>1</tt>, the column count will be ignored.
     *
     * @param n the number of columns
     */
    public void setColumnCount(int n) {
        format.setArgCount(n);
    }

    /**
     * Sets the newline character or sequence of characters for the CSV file.
     * <p>
     * The system's default is used if the line break is not set, the provided line break is
     * <tt>null</tt>, or if the character or sequence of characters is not one of the following:
     * <tt>\n</tt>, <tt>\r\n</tt>, or <tt>\r</tt>.
     *
     * @param s the newline character or sequence of characters to be set
     */
    public void setNewLine(@Nullable String s) {
        format.setEndOfLine(s);
    }

    /**
     * Sets whether a space should be used between a comma and its proceeding character in the
     * CSV file.
     *
     * <p>For example, no padding would be <tt>item1,item2</tt> and with padding would be
     * <tt>item1, item2</tt>.
     *
     * @param b boolean if <tt>true</tt>, then a white space will be used to separate columns
     */
    public void setUsePadding(boolean b) {
        format.setUsePadding(b);
    }

    /**
     * Writes the strings out to the CSV file.
     *
     * @param strings the strings to be written to the CSV file
     * @throws IllegalArgumentException if the strings are <tt>null</tt> or if the length of
     * <tt>strings</tt> is not equal to the column count (see {@link CsvWriter#setColumnCount(int)}
     * for details)
     * @throws IOException if an I/O error occurs
     */
    public void write(@NotNull String ... strings) throws IOException {
        if (strings == null) {
            throw new IllegalArgumentException("Strings cannot be null");
        }
        if (strings.length != format.getArgCount()) {
            throw new IllegalArgumentException(
                    String.format("Number of columns provided (%d) does not match the amount required (%d)",
                                  strings.length, format.getArgCount()));
        }
        writer.write(new Formatter().format(format.get(), strings).toString());
    }

    /**
     * Creates a new instance of {@link BufferedWriter} with the provided file object. If the
     * second argument is <tt>true</tt>, then bytes will be written to the end of the file rather
     * than the beginning.
     *
     * @param file the file to which a <tt>BufferedWriter</tt> will open a stream
     * @param append if <tt>true</tt>, then bytes will be written to the end of the file rather than
     * the beginning
     * @throws IllegalArgumentException if the file is <tt>null</tt> or is a directory
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    private void initialize(@NotNull File file, boolean append) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file);
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException("File cannot be a directory: " + file);
        }
        writer = new BufferedWriter(new FileWriter(file, append));
    }

}

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
 * Configurable attributes of the line are as follows: the column count, the delimiter, and the line
 * break. The column count is how many columns will be in a single line. More specifically, an
 * column count of <tt>3</tt> would expect three elements passed into {@link
 * CsvWriter#write(String...)}, resulting in a string such as <tt>item1,item2,item3</tt> in the CSV
 * file. The delimiter is the character or sequence of characters separating two columns. The
 * default delimiter is a comma with no surrounding white space. The delimiter can be changed to
 * anything but cannot be <tt>null</tt> or an empty string. The line break can be one of the
 * following: <tt>\n</tt>, <tt>\r</tt>, <tt>\r\n</tt> or an empty string.
 *
 * @author Zach Wilson
 */
public final class CsvWriter implements AutoCloseable {

    /**
     * The <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">format
     * string</a> used to format a single line in the file.
     */
    private LineFormat format;

    /**
     * The writer to the CSV file.
     */
    private BufferedWriter writer;

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>String</tt>.
     *
     * @param path the path of the file to be written to
     * @param columnCount the number of columns in the CSV file
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt> or blank
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull String path, int columnCount) throws IllegalArgumentException, IOException {
        this(path, columnCount, false);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>String</tt>.
     *
     * @param path the path of the file to be written to
     * @param columnCount the number of columns in the CSV file
     * @param append if <tt>true</tt>, then bytes will be written to the end of the file rather than
     * the beginning
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt> or blank
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull String path, int columnCount, boolean append) throws IllegalArgumentException, IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if (path.trim().length() < 1) {
            throw new IllegalArgumentException("Path cannot be blank");
        }
        initialize(new File(path), columnCount, append);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>Path</tt>.
     *
     * @param path the path of the file to be written to
     * @param columnCount the number of columns in the CSV file
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull Path path, int columnCount) throws IllegalArgumentException, IOException {
        this(path, columnCount, false);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>Path</tt>.
     *
     * @param path the path of the file to be written to
     * @param columnCount the number of columns in the CSV file
     * @param append if <tt>true</tt>, then bytes will be written to the end of the file rather than
     * the beginning
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull Path path, int columnCount, boolean append) throws IllegalArgumentException, IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        initialize(path.toFile(), columnCount, append);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>File</tt>.
     *
     * @param file the file to be written to
     * @param columnCount the number of columns in the CSV file
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull File file, int columnCount) throws IllegalArgumentException, IOException {
        initialize(file, columnCount, false);
    }

    /**
     * Constructs a new <tt>CsvWriter</tt> with the provided <tt>File</tt>.
     *
     * @param file the file to be written to
     * @param columnCount the number of columns in the CSV file
     * @param append if <tt>true</tt>, then bytes will be written to the end of the file rather than
     * the beginning
     * @throws IllegalArgumentException if <tt>path</tt> is <tt>null</tt>
     * @throws IOException if the file exists but is a directory rather than a regular file, does
     * not exist but cannot be created, or cannot be opened for any other reason
     */
    public CsvWriter(@NotNull File file, int columnCount, boolean append) throws IllegalArgumentException, IOException {
        initialize(file, columnCount, append);
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
     * Sets the delimiter for the CSV file.
     *
     * @param s the delimiter
     */
    public void setDelimiter(@Nullable String s) {
        format.setDelimiter(s);
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
    public void setNewline(@Nullable String s) {
        format.setEndOfLine(s);
    }

    /**
     * Writes the strings out to the CSV file.
     *
     * @param strings the strings to be written to the CSV file
     * @throws IllegalArgumentException if the strings are <tt>null</tt> or if the length of
     * <tt>strings</tt> is not equal to the column count
     * @throws IOException if an I/O error occurs
     */
    public void write(@NotNull String ... strings) throws IOException {
        if (strings == null) {
            throw new IllegalArgumentException("Strings cannot be null");
        }
        if (format.getColumns() != strings.length) {
            throw new IllegalArgumentException(
                    String.format("Invalid number of args: expected %d, received %d",
                                  format.getColumns(), strings.length));
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
    private void initialize(@NotNull File file, int columnCount, boolean append) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file);
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException("File cannot be a directory: " + file);
        }
        format = new LineFormat(columnCount);
        writer = new BufferedWriter(new FileWriter(file, append));
    }

}

# csv-file-writer

Convenience class for writing CSV files

## How to use

### Writing out a single row in a CSV file

```java
String path = "path/to/file.csv"; // File must exist
int columns = 4;                  // Must provide number of columns

try (CsvWriter writer = new CsvWriter(path, columns) {

    // Optionally change the column delimiter 
    // (Default is "," but can be just about anything)
    writer.setDelimiter("; ");
    
    // Pass it four args (since that is what is specified above)
    writer.write("column1", "column2", "column3", "column4");
    
}

// The CSV file contents:
// column1; column2; column3; column4
```

### Writing out multiple rows in a CSV file

```java
String path = "path/to/file.csv"; // File must exist
int columns = 2;                  // Must provide number of columns
int rows = 3;

try (CsvWriter writer = new CsvWriter(path, columns) {

    // Optionally change the column delimiter
    // (Default is "," but can be just about anything)
    writer.setDelimiter("\t");
    
    // Optionally change the newline character
    // (Default is the operating system's newline)
    writer.setNewline("\r\n");
    
    for (int i = 0; i < rows; i++) {
    
        // Pass it two args
        writer.write("column1", "column2");
        
    }
    
}

// The CSV file contents:
// column1  column2
// column1  column2
// column1  column2
```

### Other 

## Requirements

Java 8  
Maven 3  
JUnit 4 (only for testing)  

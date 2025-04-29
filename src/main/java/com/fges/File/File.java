package com.fges.File;

import com.fges.GroceryItem;

import java.util.Map;
import java.io.IOException;

public class File {
    private FileFormat format;
    private String filePath;

    public void formatAFile(String filePath) {
        /*
        * Permet de formatter un fichier dont on ne connait pas le format de base
        */
        this.filePath = filePath;
        if (filePath.endsWith(".json")) {
            this.format = new JsonFormat();
        } else if (filePath.endsWith(".csv")) {
            this.format = new CsvFormat();
        } else {
            this.format = new JsonFormat();
        }
    }
    public void formatAFileWithSpecifiedFormat(String filePath, String specifiedFormat) {
        this.filePath = filePath;

        // Set format based on command line option
        switch (specifiedFormat.toLowerCase()) {
            case "csv":
                this.format = new CsvFormat();
                break;
            case "json":
                this.format = new JsonFormat();
                break;
            default:
                // Default to JSON if format is unrecognized
                System.err.println("Unrecognized format: " + specifiedFormat + ". Using JSON format.");
                this.format = new JsonFormat();
        }
    }

    public void formatAFile(String filePath, FileFormat format) {
        /*
        * Formatte un fichier dont on connais le format */
        this.filePath = filePath;
        this.format = format;
    }

    public Map<String, GroceryItem> loadFile() throws IOException {
        return format.read(filePath);
    }
    public void saveFile(Map<String, GroceryItem> items) throws IOException {
        format.write(filePath, items);
    }
    public void modification(Map<String, GroceryItem> items) throws IOException {
        saveFile(items);
    }

    public String getFilePath() {
        return filePath;
    }
}

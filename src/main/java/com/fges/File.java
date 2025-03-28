package com.fges;

import java.util.Map;
import java.io.IOException;

public class File {
    private FileFormat format;
    private String filePath;

    public void Fichier(String filePath) {
        this.filePath = filePath;
        if (filePath.endsWith(".json")) {
            this.format = new JsonFormat();
        } else if (filePath.endsWith(".csv")) {
            this.format = new CsvFormat();
        } else {
            // Format par défaut
            this.format = new JsonFormat();
        }
    }

    public void Fichier(String filePath, FileFormat format) {
        this.filePath = filePath;
        this.format = format;
    }

    public Map<String, GroceryItem> entrée() throws IOException {
        return format.read(filePath);
    }
    public void sortie(Map<String, GroceryItem> items) throws IOException {
        format.write(filePath, items);
    }
    public void modification(Map<String, GroceryItem> items) throws IOException {
        sortie(items);
    }

}

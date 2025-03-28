package com.fges;


import java.io.IOException;
import java.util.Map;

interface FileFormat {
    Map<String, GroceryItem> read(String filePath) throws IOException;
    void write(String filePath, Map<String, GroceryItem> items) throws IOException;
}



package com.fges.format;


import com.fges.GroceryItem;

import java.io.IOException;
import java.util.Map;

public interface FileFormat {
    Map<String, GroceryItem> read(String filePath) throws IOException;
    void write(String filePath, Map<String, GroceryItem> items) throws IOException;
}



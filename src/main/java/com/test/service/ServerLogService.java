package com.test.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Scanner;

/**
 * Service to process the log file
 */
public interface ServerLogService {

    /**
     * Process the log file under the given path.
     */
    void processFile(String filePath);

}
package com.test.service;

import com.test.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Scanner;

public class ServerLogServiceImpl implements ServerLogService {

    private Logger logger = LoggerFactory.getLogger(ServerLogServiceImpl.class);

    public static final String LOG_FILE_NAME = "logfile.txt";

    private ServerLogEntryService serverLogEntryService;

    public ServerLogServiceImpl() {
        this.serverLogEntryService = new ServerLogEntryServiceImpl();
    }

    @Override
    public void processFile(String filePath) {
        logger.info("Start reading the file from: {}", filePath);
        // Validate path
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(filePath + FileSystems.getDefault().getSeparator() + LOG_FILE_NAME);
        } catch (FileNotFoundException exception) {
            logger.error("Error reading the log file", exception);
            throw new ServiceException("No server logs file found under the path: " + filePath);
        }
        // Start reading the file
        readFile(inputStream);
        logger.info("Finish reading the file");
    }

    private void readFile(InputStream inputStream) {
        Scanner sc = null;
        try {
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                this.serverLogEntryService.process(sc.nextLine());
            }
            if (sc.ioException() != null) {
                logger.error("Error while reading the file lines", sc.ioException());
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch(IOException ioException) {
                    logger.error("Error while closing the input stream", ioException);
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

}
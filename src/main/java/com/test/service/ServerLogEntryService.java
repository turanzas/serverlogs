package com.test.service;

/**
 * Service to process a log entry
 */
public interface ServerLogEntryService {

    /**
     * Process a log line
     */
    void process(String line);

}

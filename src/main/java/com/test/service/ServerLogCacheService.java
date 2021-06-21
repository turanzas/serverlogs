package com.test.service;

import com.test.model.ServerLogEntry;

/**
 * Service to track temporary log entries
 */
public interface ServerLogCacheService {

    /**
     * Gets an existing entry by its id
     */
    ServerLogEntry get(String id);

    /**
     * Puts an entry into the cache.
     * In case it already exits throws an exception.
     */
    void cache(ServerLogEntry serverLogEntry);

    /**
     * Removes an entry from the cache.
     */
    void evict(String id);

}

package com.test.service;

import com.test.exception.ServiceException;
import com.test.model.ServerLogEntry;

import java.util.HashMap;
import java.util.Map;

public class ServerLogCacheServiceImpl implements ServerLogCacheService {

    private final Map<String, ServerLogEntry> cache = new HashMap<>();

    @Override
    public ServerLogEntry get(String id) {
        return cache.get(id);
    }

    @Override
    public void cache(ServerLogEntry serverLogEntry) {
        if (cache.containsKey(serverLogEntry.getId())) {
            throw new ServiceException("Cannot cache an existing entry");
        }
        cache.put(serverLogEntry.getId(), serverLogEntry);
    }

    @Override
    public void evict(String id) {
        cache.remove(id);
    }

}
package com.test.service;

import com.test.entity.ServerLogEntity;

/**
 * Service for db ops on Server Log Entity
 */
public interface ServerLogEntityService {

    /**
     * Save the entity into the db
     */
    void save(ServerLogEntity serverLogEntity);

}
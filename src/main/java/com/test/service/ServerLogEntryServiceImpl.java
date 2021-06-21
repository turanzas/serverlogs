package com.test.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.entity.ServerLogEntity;
import com.test.exception.ServiceException;
import com.test.model.ServerLogEntry;
import com.test.model.State;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerLogEntryServiceImpl implements ServerLogEntryService {

    public static final Long ALERT_DURATION = 4L;

    private Logger logger = LoggerFactory.getLogger(ServerLogServiceImpl.class);

    private ObjectMapper objectMapper;
    private ServerLogCacheService serverLogCacheService;
    private ServerLogEntityService serverLogEntityService;

    public ServerLogEntryServiceImpl() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
        this.serverLogCacheService = new ServerLogCacheServiceImpl();
        this.serverLogEntityService = new ServerLogEntityServiceImpl();
    }

    @Override
    public void process(String line) {
        logger.debug("Start process line: {}", line);
        try {
            ServerLogEntry currentServerLogEntry = parseLine(line);
            ServerLogEntry previousServerLogEntry = this.serverLogCacheService.get(currentServerLogEntry.getId());
            if (previousServerLogEntry == null) {
                logger.debug("Server log entry cached");
                this.serverLogCacheService.cache(currentServerLogEntry);
            } else {
                this.serverLogCacheService.evict(currentServerLogEntry.getId());
                saveEntity(previousServerLogEntry, currentServerLogEntry);
            }
        } catch (Exception exception) {
            logger.error("Error processing log line", exception);
        }
        logger.debug("Finish process line");
    }

    private ServerLogEntry parseLine(String line) {
        logger.debug("Start parse line");
        if (StringUtils.isBlank(line)) {
            throw new ServiceException("Invalid blank log line");
        }
        ServerLogEntry serverLogEntry;
        try {
            serverLogEntry = this.objectMapper.readValue(line, ServerLogEntry.class);
        } catch (Exception exception) {
            throw new ServiceException("Invalid log line, formatting exception", exception);
        }
        logger.debug("Finish parse line");
        return serverLogEntry;
    }

    private void saveEntity(ServerLogEntry previousServerLogEntry, ServerLogEntry currentServerLogEntry) {
        logger.debug("Start save server log entity");
        ServerLogEntity serverLogEntity = new ServerLogEntity();
        serverLogEntity.setId(currentServerLogEntry.getId());
        serverLogEntity.setType(currentServerLogEntry.getType());
        serverLogEntity.setHost(currentServerLogEntry.getHost());
        long ms = (State.STARTED == previousServerLogEntry.getState())
                ? currentServerLogEntry.getTimestamp() - previousServerLogEntry.getTimestamp()
                : previousServerLogEntry.getTimestamp() - currentServerLogEntry.getTimestamp();
        serverLogEntity.setDuration(ms);
        serverLogEntity.setAlert(ms > ALERT_DURATION);

        this.serverLogEntityService.save(serverLogEntity);
        logger.debug("Finish save server log entity");
    }

}
package com.test.service;

import com.test.entity.ServerLogEntity;
import com.test.repository.ServerLogEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerLogEntityServiceImpl implements ServerLogEntityService {

    private Logger logger = LoggerFactory.getLogger(ServerLogEntityServiceImpl.class);

    private ServerLogEntityRepository serverLogEntityRepository;

    public ServerLogEntityServiceImpl() {
        this.serverLogEntityRepository = new ServerLogEntityRepository();
    }

    @Override
    public void save(ServerLogEntity serverLogEntity) {
        if (!serverLogEntityRepository.exists(serverLogEntity.getId())) {
            this.serverLogEntityRepository.insert(serverLogEntity);
        } else {
            logger.debug("Ignoring existing server log entity (already processed): {}", serverLogEntity.getId());
        }
    }

}
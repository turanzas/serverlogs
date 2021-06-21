package com.test.main;

import com.test.config.DatabaseConfig;
import com.test.exception.DatabaseException;
import com.test.exception.ServiceException;
import com.test.service.ServerLogService;
import com.test.service.ServerLogServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Console application to parse a log file.
 */
public class ServerLogApp {

    private Logger logger = LoggerFactory.getLogger(ServerLogApp.class);

    private ServerLogService serverLogService;

    public ServerLogApp() {
        this.serverLogService = new ServerLogServiceImpl();
    }

    /**
     * Runs the application
     */
    public void run(String[] args) {
        logger.info("Start server log app");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            migrateDatabase();
            String path = parseArguments(args);
            serverLogService.processFile(path);
        } catch (ServiceException | DatabaseException exception) {
            logger.error("Error while running the app", exception);
        }

        stopWatch.stop();
        logger.info("Finish server log app. Total processing time: {}", stopWatch.getTime(TimeUnit.MILLISECONDS));
    }

    /**
     * Migrates the database schema
     */
    private void migrateDatabase() {
        logger.debug("Start migrating the database");
        Flyway flyway = Flyway.configure()
                .dataSource(DatabaseConfig.URL, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD)
                .locations(DatabaseConfig.SCRIPTS_LOCATION)
                .schemas(DatabaseConfig.DEFAULT_SCHEMA)
                .validateOnMigrate(false)
                .load();
        flyway.migrate();
        logger.debug("Finish migrating the database");
    }

    /**
     * Parses application arguments
     */
    private String parseArguments(String[] args) {
        logger.debug("Start parsing command line arguments: {}", args);
        String path = null;
        if (args != null && args.length == 1) {
            path = args[0];
        }
        if (StringUtils.isBlank(path)) {
            logger.error("Illegal path received: {}", path);
            throw new ServiceException("Only a valid file path must be provided as an argument");
        }
        logger.debug("Finish parsing command line arguments");
        return path;
    }

}
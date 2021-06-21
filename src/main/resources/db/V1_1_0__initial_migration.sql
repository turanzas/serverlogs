create table SERVER_LOGS (
    ID VARCHAR(100) not null constraint SERVER_LOGS_PK primary key,
    DURATION BIGINT not null,
    TYPE VARCHAR(100),
    HOST VARCHAR(100),
    ALERT BOOLEAN
);
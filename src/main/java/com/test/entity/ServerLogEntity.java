package com.test.entity;

import lombok.Data;

@Data
public class ServerLogEntity {

    private String id;
    private Long duration;
    private String type;
    private String host;
    private Boolean alert;

}
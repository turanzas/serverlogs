package com.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerLogEntry {

    private String id;
    private State state;
    private Long timestamp;
    private String type;
    private String host;

}

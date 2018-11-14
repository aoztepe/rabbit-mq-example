package com.example.rabbitmq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Log {

    private String content;

    private String routingKey;

    public Log() {
    }

    public Log(String content, String routingKey) {
        this.content = content;
        this.routingKey = routingKey;
    }
}

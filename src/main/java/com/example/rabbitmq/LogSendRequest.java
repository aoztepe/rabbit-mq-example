package com.example.rabbitmq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogSendRequest {

    private String content;

    private String routingKey;
}

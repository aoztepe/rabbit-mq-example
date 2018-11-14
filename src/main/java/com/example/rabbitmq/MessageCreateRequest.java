package com.example.rabbitmq;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MessageCreateRequest {

    @NotEmpty
    private String message;

    @NotNull
    private Type type;
}

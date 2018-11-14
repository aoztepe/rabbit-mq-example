package com.example.rabbitmq;

import java.util.List;

public interface MessageService {

    Message getByMessage(String message);

    List<Message> getAll();

    Message save(Message message);

    void delete(Message message);

    List<Message> getByType(Type type);

    Message getLast();
}

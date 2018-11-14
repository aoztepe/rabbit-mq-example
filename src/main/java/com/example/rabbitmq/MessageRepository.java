package com.example.rabbitmq;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    Message findByMessage(String mesage);

    List<Message> findAll();

    List<Message> findAllByType(Type type);

    Message findTopByOrderByIdDesc();

}

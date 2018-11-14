package com.example.rabbitmq;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message getByMessage(String message) {
        return messageRepository.findByMessage(message);
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public List<Message> getByType(Type type) {
        return messageRepository.findAllByType(type);
    }

    @Override
    public Message getLast() {
        return messageRepository.findTopByOrderByIdDesc();
    }
}

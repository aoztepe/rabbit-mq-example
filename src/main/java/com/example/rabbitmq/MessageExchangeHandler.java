package com.example.rabbitmq;

import org.json.JSONObject;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

@Component
public class MessageExchangeHandler {

    private final QueueService queueService;
    private final MessageService messageService;

    public MessageExchangeHandler(QueueService queueService, MessageService messageService) {
        this.queueService = queueService;
        this.messageService = messageService;
    }

    public String prepare(MessageCreateRequest messageCreateRequest) {

        if (messageCreateRequest.getType().equals(Type.GAME)) {
            try {
                com.example.rabbitmq.Message message = new com.example.rabbitmq.Message();
                message.setId(messageService.getLast().getId() + 1);
                message.setMessage(messageCreateRequest.getMessage());
                message.setType(messageCreateRequest.getType());
                queueService.produceMessage(convertToJson(message), RabbitConfig.ROUTING_GAME, RabbitConfig.EXCHANGE_GAME);
                return "Success";
            } catch (Exception e) {
                return "Failed - > Cause : " + e.getMessage();
            }
        } else {
            try {
                com.example.rabbitmq.Message message = new com.example.rabbitmq.Message();
                message.setId(messageService.getLast().getId() + 1);
                message.setMessage(messageCreateRequest.getMessage());
                message.setType(messageCreateRequest.getType());
                queueService.produceMessage(convertToJson(message), RabbitConfig.ROUTING_SPORT, RabbitConfig.EXCHANGE_SPORT);
                return "Success";
            } catch (Exception e) {
                return "Failed - > Cause : " + e.getMessage();
            }
        }
    }

    private MessageProperties properties() {
        MessageProperties properties = new MessageProperties();
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        properties.setContentType("application/json");
        return properties;
    }

    private String convertToJson(com.example.rabbitmq.Message message) {
        return new JSONObject(message).toString();
    }
}

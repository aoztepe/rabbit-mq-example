package com.example.rabbitmq;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

@Service
public class QueueService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final String ERROR_EXCHANGE = "app.local.error";

    private final MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    public QueueService(RabbitTemplate rabbitTemplate, MessageService messageService) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageService = messageService;
    }

    public void produceMessage(String message, String routing, String exchange) {
        rabbitTemplate.convertAndSend(exchange, routing, message);
        logger.info("Successfully sent to the queue");
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_GAME)
    public void receiveGameMessage(org.springframework.amqp.core.Message message) {
        try {
            byte[] bytes = message.getBody();
            String decodedData = "";
            decodedData = new String(bytes, "UTF-8");
            Gson gson = new Gson();
            Message message1 = gson.fromJson(decodedData, Message.class);
            messageService.save(message1);
            logger.info("Successfully saved to db - > " + message1.getMessage());
        } catch (Exception e) {
            logger.error("Error during saving db  -> " + e.getMessage());
        }
    }


    @Scheduled(fixedRate = 10000L)
    public void receiveSportMessage() {
        byte[] bytes = null;
        try {
            bytes = rabbitTemplate.receive(RabbitConfig.QUEUE_SPORT).getBody();
        } catch (Exception e) {
            //
        }
        if (bytes != null) {
            String decodedData = "";
            try {
                decodedData = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            }
            Gson gson = new Gson();
            Message message1 = gson.fromJson(decodedData, Message.class);
            messageService.save(message1);
            logger.info("Successfully saved to db - > " + message1.getMessage());
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }


    public String produceTopicMessage(Log log) {
        amqpTemplate.convertAndSend(ERROR_EXCHANGE, log.getRoutingKey(), log);
        logger.info("Message successfully sent to Q - >" + log.toString());
        return "Success";
    }


    @RabbitListener(queues = RabbitConfig.QUEUE_DOMAIN_ERROR, containerFactory = "jsaFactory")
    public void receiveDomainErrorMessage(Log log) {
        logger.info("Message Received From Domain Error Q - >" + log.toString());
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_SYSTEM_ERROR, containerFactory = "jsaFactory")
    public void receiveSystemErrorMessage(Log log) {
        logger.info("Message Received From System Error Q - >" + log.toString());
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_TOTAL_ERROR, containerFactory = "jsaFactory")
    public void receiveTotalErrorMessage(Log log) {
        logger.info("Message Received From Total Error Q - >" + log.toString());
    }
}

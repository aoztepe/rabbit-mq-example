package com.example.rabbitmq;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class Controller {

    private final MessageExchangeHandler messageExchangeHandler;
    private final QueueService queueService;

    public Controller(MessageExchangeHandler messageExchangeHandler, QueueService queueService) {
        this.messageExchangeHandler = messageExchangeHandler;
        this.queueService = queueService;
    }

    @RequestMapping("/send")
    public String send(@Valid @RequestBody MessageCreateRequest request) {
        return messageExchangeHandler.prepare(request);
    }

    @RequestMapping("/send-log")
    public String sendLog(@RequestBody LogSendRequest request) {
        Log log = new Log();
        log.setContent(request.getContent());
        log.setRoutingKey(request.getRoutingKey());
        return queueService.produceTopicMessage(log);
    }


}

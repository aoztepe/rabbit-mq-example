package com.example.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableRabbit
@EnableScheduling
public class RabbitConfig {

    public final static String EXCHANGE_SPORT = "sport";

    public final static String EXCHANGE_GAME = "game";

    public final static String QUEUE_GAME = "game-queue";

    public final static String QUEUE_SPORT = "sport-queue";

    public final static String ROUTING_SPORT = "sport-routing";

    public final static String ROUTING_GAME = "game-routing";


    public final static String QUEUE_SYSTEM_ERROR = "system-error";

    public final static String QUEUE_DOMAIN_ERROR = "domain-error";

    public final static String QUEUE_TOTAL_ERROR = "total-error";

    public final static String SYSTEM_ROUTING = "sys.#";

    public final static String DOMAIN_ROUTING = "dmn.#";

    public final static String TOTAL_ROUTING = "#.error";

    public final static String EXCHANGE_ERROR = "app.local.error";

    @Bean
    public TopicExchange errorExchange() {
        return new TopicExchange(EXCHANGE_ERROR);
    }

    @Bean
    public Queue systemErrorQ() {
        return new Queue(QUEUE_SYSTEM_ERROR, true, false, false);
    }

    @Bean
    public Queue domainErrorQ() {
        return new Queue(QUEUE_DOMAIN_ERROR, true, false, false);
    }

    @Bean
    public Queue totalErrorQ() {
        return new Queue(QUEUE_TOTAL_ERROR, true, false, false);
    }

    @Bean
    public Binding systemErrorBinding() {
        return BindingBuilder.bind(systemErrorQ()).to(errorExchange()).with(SYSTEM_ROUTING);
    }

    @Bean
    public Binding domainErrorBinding() {
        return BindingBuilder.bind(domainErrorQ()).to(errorExchange()).with(DOMAIN_ROUTING);
    }

    @Bean
    public Binding totalErrorBinding() {
        return BindingBuilder.bind(totalErrorQ()).to(errorExchange()).with(TOTAL_ROUTING);
    }


    @Bean
    public DirectExchange sportExchange() {
        return new DirectExchange(EXCHANGE_SPORT);
    }

    @Bean
    public Queue gameQueue() {
        return new Queue(QUEUE_GAME);
    }

    @Bean
    public Queue sportQueue() {
        return new Queue(QUEUE_SPORT);
    }

    @Bean
    public DirectExchange gameExchange() {
        return new DirectExchange(EXCHANGE_GAME);
    }

    @Bean
    public Binding gameBinding() {
        return BindingBuilder.bind(gameQueue()).to(gameExchange()).with(ROUTING_GAME);
    }

    @Bean
    public Binding sportBinding() {
        return BindingBuilder.bind(sportQueue()).to(sportExchange()).with(ROUTING_SPORT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory, SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

}

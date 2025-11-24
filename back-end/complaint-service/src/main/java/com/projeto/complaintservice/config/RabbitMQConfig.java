package com.projeto.complaintservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "complaint-exchange";

    public static final String QUEUE_PROTOCOL = "complaint.created";
    public static final String QUEUE_NOTIFICATION = "notification.email";

    public static final String ROUTING_KEY_PROTOCOL = "complaint.created";
    public static final String ROUTING_KEY_NOTIFICATION = "complaint.notification";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queueProtocol() {
        return new Queue(QUEUE_PROTOCOL, true); // durable = true
    }
    
    @Bean
    public Queue queueNotification() {
        return new Queue(QUEUE_NOTIFICATION, true);
    }

    @Bean
    public Binding bindingProtocol() {
        return BindingBuilder
                .bind(queueProtocol())
                .to(exchange())
                .with(ROUTING_KEY_PROTOCOL);
    }
    
    @Bean
    public Binding bindingNotification() {
        return BindingBuilder
                .bind(queueNotification())
                .to(exchange())
                .with(ROUTING_KEY_NOTIFICATION);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
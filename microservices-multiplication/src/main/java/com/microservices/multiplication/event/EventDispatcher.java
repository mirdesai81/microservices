package com.microservices.multiplication.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by mihir.desai on 3/7/2018.
 */
@Component
public class EventDispatcher {
    private RabbitTemplate rabbitTemplate;

    private String multiplicationExchange;

    private String multiplicationSolvedRoutingKey;

    @Autowired
    EventDispatcher(final RabbitTemplate rabbitTemplate,
                    @Value("${multiplication.exchange}") final String multiplicationExchange,
                    @Value("${multiplication.solved.key}") final String multiplicationSolvedRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.multiplicationExchange = multiplicationExchange;
        this.multiplicationSolvedRoutingKey = multiplicationSolvedRoutingKey;
    }

    public void  send(final MultiplicationSolvedEvent multiplicationSolvedEvent) {
        this.rabbitTemplate.convertAndSend(this.multiplicationExchange,
                this.multiplicationSolvedRoutingKey,multiplicationSolvedEvent);
    }
}

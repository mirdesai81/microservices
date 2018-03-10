package com.microservices.gamification.event;

import com.microservices.gamification.service.GameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Created by mihir.desai on 3/8/2018.
 */
@Component
public class EventHandler {
    private GameService gameService;

    private static final Logger logger = LogManager.getLogger(EventHandler.class);

    public EventHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @RabbitListener(containerFactory = "rabbitListenerContainerFactory",queues = "${multiplication.queue}")
    public void handleMultiplicationSolvedEvent(final MultiplicationSolvedEvent event) {
        logger.debug("Multiplication solved event : {}",event.getMultiplicationResultAttemptId());
        try {
            gameService.newAttemptForUser(event.getUserId(),
                    event.getMultiplicationResultAttemptId(),
                    event.isCorrect());
        }catch (final Exception e) {
            logger.error("Exception occured while trying to process MultiplicationSolvedEvent",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}

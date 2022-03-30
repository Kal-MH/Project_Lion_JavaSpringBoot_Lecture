package dev.kalmh.publisher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PublisherService {
    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange fanoutExchange;

    public PublisherService(
            RabbitTemplate rabbitTemplate,
            FanoutExchange fanoutExchange
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
    }

    AtomicInteger dots = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    public void publishMessage() {
        //message build
        StringBuilder builder = new StringBuilder("Hello");
        if (dots.incrementAndGet() == 4) {
            dots.set(1);
        }
        builder.append(".".repeat(Math.max(0, dots.get())));
        builder.append(count.incrementAndGet());
        String message = builder.toString();

        //send message
        rabbitTemplate.convertAndSend(
                fanoutExchange.getName(),
                "",
                message
        );
        logger.info("publisher sent '" + message + "'.");
    }
}

package dev.kalmh.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    private final RabbitTemplate rabbitTemplate;
    private final Queue rabbitQueue;

    public ProducerService(
            RabbitTemplate rabbitTemplate,
            Queue rabbitQueue
    ) {
        this.rabbitQueue = rabbitQueue;
        this.rabbitTemplate = rabbitTemplate;
    }

    //int warpper class
    // - 멀티쓰레드에서 동시성을 보장
    AtomicInteger dots = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);

    public void send() {
        StringBuilder builder = new StringBuilder("Hello");
        if (dots.incrementAndGet() == 4) {
            dots.set(1);
        }

        builder.append(".".repeat(Math.max(0, dots.get())));
        builder.append(count.incrementAndGet());
        String message = builder.toString();
        rabbitTemplate.convertAndSend(rabbitQueue.getName(), message);
        logger.info("producer sent '" + message + "'");
    }
}

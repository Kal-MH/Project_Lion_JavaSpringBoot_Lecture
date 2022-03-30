package dev.kalmh.producer.service;

import com.google.gson.Gson;
import dev.kalmh.producer.model.JobMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    private final RabbitTemplate rabbitTemplate;
    private final Queue rabbitQueue;
    private final Gson gson;

    public ProducerService(
            RabbitTemplate rabbitTemplate,
            Queue rabbitQueue,
            Gson gson
    ) {
        this.rabbitQueue = rabbitQueue;
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;
    }

    public String send() {
        JobMessage jobMessage = new JobMessage(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(rabbitQueue.getName(), gson.toJson(jobMessage));
        logger.info("Producer sent '" + jobMessage.getJobId() + "'.");
        return jobMessage.getJobId();
    }
}

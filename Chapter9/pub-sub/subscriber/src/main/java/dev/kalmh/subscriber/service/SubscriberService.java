package dev.kalmh.subscriber.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
//autoGenQueue로 지정함으로 이미 config에 존재하는 Bean객체를 받아올 수 있게 된다.
@RabbitListener(queues = "#{autoGenQueue.name}")
public class SubscriberService {
    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    @RabbitHandler
    public void receiveMessage(String message) {
        logger.info("Subscriber received: '" + message + "'.");
    }
}

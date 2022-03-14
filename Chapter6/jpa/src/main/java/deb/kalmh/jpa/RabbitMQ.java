package deb.kalmh.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!local")
public class RabbitMQ implements MessageQueueInterface{
    private static final Logger logger = LoggerFactory.getLogger(ProfileComponent.class);
    public RabbitMQ() {
        logger.info("rabitMQ");
    }
    @Override
    public String readMessage() {
        return "Message From RabbitMQ";
    }
}

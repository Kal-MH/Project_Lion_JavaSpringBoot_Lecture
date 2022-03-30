package dev.kalmh.publisher.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublisherConfig {
    //Exchange는 우체국의 역할을 한다.
    // - 메세지가 어떤 큐로 전달될 지를 결정.
    // - fanoutExchange는 구독되어 있는 메세지 큐에 모두 메세지를 전달한다.
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("boot.fanout.exchange");
    }
}

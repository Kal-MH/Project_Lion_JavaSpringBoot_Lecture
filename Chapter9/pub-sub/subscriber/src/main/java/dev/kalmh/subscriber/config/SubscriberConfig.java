package dev.kalmh.subscriber.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriberConfig {
    //pulisher와 같은 exchange를 지정해야 한다.
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("boot.fanout.exchange");
    }
    //메세지를 받는 주체이기 때문에 메세지를 저장할 큐가 필요하다.
    @Bean
    public Queue autoGenQueue() {return new AnonymousQueue();}

    //exchage와 queue를 연결하는 함수
    @Bean
    public Binding binding(
            Queue queue,
            FanoutExchange fanoutExchange
    ) {
        return BindingBuilder
                .bind(queue)
                .to(fanoutExchange);
    }
}

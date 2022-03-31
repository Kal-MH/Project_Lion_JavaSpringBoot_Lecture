package dev.kalmh.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSockerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //WebSocket 통신 시작할 endpoint 설정
        registry.addEndpoint("/ws/chat");
        registry.addEndpoint("/ws/chat").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //WebSocket 통신 연결 후, destination 설정하기
        //서버는 메세지를 클라이언트에게 전달하기 위한 메세지 브로커를 생성하고,
        //클라이언트에서는 정의된 endpoint("/receive-endpoint")를 구독함으로써, 메세지를 받는다.
        registry.enableSimpleBroker("/receive-endpoint");
        //클라이언트에서 서버로 메세지를 보낼 때 사용하는 엔드포인트 설정
        registry.setApplicationDestinationPrefixes("/send-endpoint");
    }
}

package com.chatting.chatserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     *  WebSocket 연결을 수립하는데 사용, 클라이언트가 여기로 메세지를 보냄.
     *  즉, JS차원에서의 URL임. <-> @MessageMapping과 연계
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/ws") // ws라는 엔드포인트가 등록
               .setAllowedOrigins("*")
               .withSockJS(); // SockJS 프로토콜을 지원하지 않는 인터넷 브라우저를 대응하기 위해 withSockJS()를 사용하여 SockJS 지원을 활성화
    }

    /**
     * 클라이언트가 구독(특정 URL에 접속되어있는) 곳을 정의
     * <-> @SendTo와 연계
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // @MessageMapping 애노테이션이 지정된 컨트롤러 메소드가 WebSocket 클라이언트에서 수신하는 메시지의 대상을 구성
        config.setApplicationDestinationPrefixes("/app");
        // 전체메시지는 /topic -> /chatroom, 개인 메세지는 /queue -> /user의
        config.enableSimpleBroker("/chatroom", "/user");

        config.setUserDestinationPrefix("/user"); // 개인 메시지의 수신 대상 프리픽스를 구성

    }
}

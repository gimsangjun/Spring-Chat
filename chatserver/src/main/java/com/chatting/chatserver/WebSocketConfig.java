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
     * WebSocket 접속과정
     * 클라이언트                      서버
     *     TCP/IP 접속 요청  ->
     *     <- TCP/IP 접속 수락
     *     웹소켓 열기 핸드쉐이크 요청 ->
     *     <- 웹소켓 열기 핸드쉐이크 수락
     *     <- 웹소켓 데이터 송수신 ->
     */

    /**
     * client에서 다음과 같은 코드로 WebSocket연결을함.
     *   const connect =()=>{
     *       let Sock = new SockJS('http://localhost:8080/ws');
     *       stompClient = over(Sock);
     *       stompClient.connect({},onConnected, onError);
     *       // onConnected는 구독 하는 코드가 들어가있음.
     *   }
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/ws") // WebSocket 또는 SocjJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로
               .setAllowedOrigins("http://localhost:3000")
               .withSockJS(); // SockJS 프로토콜을 지원하지 않는 인터넷 브라우저를 대응하기 위해 withSockJS()를 사용하여 SockJS 지원을 활성화
    }

    /**
     * 클라이언트가 구독(특정 URL에 접속되어있는) 곳을 정의
     * <-> @SendTo와 연계
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /app 경로로 시작하는 STOMP 메세지의 "destination"헤더는 @Controller객체의 @MessageMapping 메서드로 라우팅 된다.
        // Client에서 온 SEND요청을 처리
        // 다음과 같은 클라이언트 요청을 처리 한다.
        // "/app/message", "/app/private-message"
        config.setApplicationDestinationPrefixes("/app");
        // 내장된 메세지 브로커를 사용해 Client에게 Subscriptions, Broadcasting 기능을 제공한다.
        // 해당 경로로 SimpleBroker를 등록.
        // /chatroom, /user로 시작하는 "destination"헤더를 가진 메세지를 브로커로 라우팅한다. -> ChatController에서 라우팅한다.
        // SimpleBroker는 해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달하는 간단한 작업을 수행
        // 구독할때 사용하는듯 하다. 클라이언트의 코드는 다음과 같다.
        //   const onConnected = () => {
        //     setUserData({...userData,"connected": true});
        //     stompClient.subscribe('/chatroom/public', onMessageReceived);
        //     stompClient.subscribe('/user/'+userData.username+'/private', onPrivateMessage);
        //     userJoin();
        //   }
        config.enableSimpleBroker("/chatroom", "/user");

        // 개인메세지를 클라이언트들에게 전달할때  사용
        // '/user/'+userData.username+'/private'에서의 개인메세지의 prefix를 구성 -> ChatController
        config.setUserDestinationPrefix("/user");

    }
}

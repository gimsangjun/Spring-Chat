package com.chatting.chatserver;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    /**
     *
     * @Payload 애노테이션은 WebSocket메세지를 Message객체로 변환하고 컨트롤러의 메소드 인자로 전달하는 역할
     * @SendTo 애노테이션은 서버가 WebSocket메세지를 처리한 후 다시 클라이언트로 전송할 위치를 정의
     * 따라서, 클라이언트가 "/ws/message"로 메세지를 전송하면, 해당 메세지는 @MessageMapping("/message")메소드에 의해 처리되어
     * /chatroom/public으로 다시 전송됨.
     * 이때 클라이언트는 "/app/chatroom/public"의 위치를 구독(이 URL에 접속되어있는 상태, js차원에서 /ws/message로 메세지전송)하고 있기때문에 해당 메세지를 수신할수 있음.
     */
    @MessageMapping("/message")
    @SendTo("/chatroom/public") // 메세지 처리후, 여기로 다시 보낸다.
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        System.out.println(message.toString());
        return message;
    }

}

package com.chatting.chatserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * client가 SEND할수 있는 경로 /app/message
     * Client에서는 prefix를 붙여 "/app/message"로 발행 요청을 하면 Controller가 해당 메세지를 받아 처리하는데,
     * 메세지가 발행되면 "/chatroom/public"으로 메세지가 전송되는것을 볼수있다.
     * client에서는 해당 주소를 SUBSCRIBE하고 있다가 메세지가 전달되면 화면에 출력한다.
     * 이떄 "/chatroom/public"은 채팅방을 구분하는 값이다.
     * @Payload 애노테이션은 WebSocket메세지를 Message객체로 변환하고 컨트롤러의 메소드 인자로 전달하는 역할
     * @SendTo 애노테이션은 서버가 WebSocket메세지를 처리한 후 다시 클라이언트로 전송할 위치를 정의
     */
    @MessageMapping("/message")
    @SendTo("/chatroom/public") // 메세지 처리후, 여기로 다시 보낸다.
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    /**
     * client에서 /app/private-message로 보냄
     * 요청에 다음과 같은 정보를 담아서 보냄.
     *     private String senderName;
     *     private String receiverName;
     *     private String message;
     *     private String date;
     *     private Status status;
     */
    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        // message에서 누구에게 보내는지 이름을 가져옴.
        // 그런다음 브로커에서 '/user/'+userData.username+'/private'를 구독하고있는 클라리언트들에게 메세제를 보낸다.
        // 각각의 개인유저는 '/user/'+userData.username+'/private'을 구독하고 있음.
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());
        return message;
    }

}

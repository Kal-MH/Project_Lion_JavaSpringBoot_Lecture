package dev.kalmh.websocket.controller;

import dev.kalmh.websocket.model.ChatRoom;
import dev.kalmh.websocket.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatRepository chatRepository;

    public ChatController(ChatRepository chatRepository) {this.chatRepository = chatRepository;}

    //개설된 채팅방 목록 가져오기
    @GetMapping("rooms")
    public @ResponseBody ResponseEntity<List<ChatRoom>> getChatRooms() {
       return ResponseEntity.ok(this.chatRepository.getChatRooms());
    }

    //채팅방 새로 개설하기
    @PostMapping("rooms")
    public @ResponseBody ResponseEntity<ChatRoom> createChatRoom(
            @RequestParam("room-name") String roomName
    ) {
        return ResponseEntity.ok(this.chatRepository.createChatRoom(roomName));
    }

    //현재 채팅방 이름 가져오기
    @GetMapping("room/name")
    public @ResponseBody ResponseEntity<ChatRoom> getRoomName(
            @RequestParam("room-id") String roomId
    ) {
        return ResponseEntity.ok(this.chatRepository.findRoomById(roomId));
    }

    //채팅방에 들어갔을 때, html 렌더링
    @GetMapping("{room-Id}/{user-nickname}")
    public String enterRoom() {return "/chat-room.html";}
}

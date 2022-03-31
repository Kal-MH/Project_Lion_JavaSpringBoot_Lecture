package dev.kalmh.websocket.repository;

import dev.kalmh.websocket.model.ChatRoom;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//채팅방을 만들어서 저장하기 위한 용도
@Repository
public class ChatRepository {
    private List<ChatRoom> chatRooms;

    public ChatRepository() {
        this.chatRooms = new ArrayList<>();
        this.chatRooms.add(new ChatRoom("general", "general"));
    }

    public List<ChatRoom> getChatRooms() {return chatRooms;}

    public ChatRoom createChatRoom(String roomName) {
        //UUID : 유일한 식별자를 생성하는 클래스. 데이터를 구분하고 중복 방지.
        ChatRoom newRoom = new ChatRoom(UUID.randomUUID().toString(), roomName);
        this.chatRooms.add(newRoom);
        return newRoom;
    }
    public ChatRoom findRoomById(String roomId) {
        for(ChatRoom chatRoom : this.chatRooms) {
            if (chatRoom.getRoomId().equals(roomId)) {
                return chatRoom;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}

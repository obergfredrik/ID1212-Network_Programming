package server;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private List<ChatUser> roomUsers;
    private String chatRoomName;

    ChatRoom(String chatRoomName){
        this.roomUsers = new ArrayList<>();
        setChatRoomName(chatRoomName);
    }

    public void addUser(ChatUser user){
        this.roomUsers.add(user);
        user.setChatRoom(this);
    }

    public void removeChatUser(ChatUser user){

        for (int i = 0; i < this.roomUsers.size(); i++) {
            if (this.roomUsers.get(i).getUserName().equals(user.getUserName())) {
                this.roomUsers.remove(i);
                break;
            }
        }

        distributeMessage(user.getUserName() + " has left the chat!");
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }


    /**
     * Distributes the entered chat messages between all other connected users.
     * @param message is the message being distributed.
     */
    void distributeMessage(String message){
        for (ChatUser member: this.roomUsers)
            member.sendMessage(message);
    }

}

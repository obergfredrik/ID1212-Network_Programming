package server;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private List<User> users;
    private String chatRoomName;

    Room(String chatRoomName){
        this.users = new ArrayList<>();
        setChatRoomName(chatRoomName);
    }

    public void addUser(User user){
        this.users.add(user);
        user.setChatRoom(this);
    }

    public void removeUser(User user) {

        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getUserName().equals(user.getUserName())) {
                this.users.remove(i);
                break;
            }
        }
    }

    int getPresentUsers(){
        return users.size();
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public List<User> getUsers() {
        return users;
    }
}

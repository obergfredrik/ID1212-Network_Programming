package server;

import javax.net.ssl.SSLSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatHandler {

    private MessageHandler messageHandler;
    private List<ChatRoom> chatRooms;
    private List<User> users;

    ChatHandler (){
        this.messageHandler = new MessageHandler(this);
        this.users = new ArrayList<>();
        this.chatRooms = new ArrayList<>();
    }

    void newUser(SSLSocket socket){

        User user = new User(socket, this.messageHandler);
        this.users.add(user);


        Thread thread = new Thread(user);
        thread.start();

        System.out.println("Number of connected users: " + this.users.size());
    }

    public boolean joinChatRoom(User user, String chatRoomName){

        ChatRoom chatRoom;

        if ((chatRoom = getChatRoom(chatRoomName)) != null){
            chatRoom.addUser(user);
            user.setChatRoom(chatRoom);
            return true;
        }else
            return false;

    }

    public boolean createChatRoom(User user, String chatRoomName){

        if (checkChatRoomName(chatRoomName))
            return true;

        ChatRoom chatRoom = new ChatRoom(chatRoomName);
        chatRoom.addUser(user);
        this.chatRooms.add(chatRoom);

        return false;
    }

    public ChatRoom getChatRoom(String chatRoomName) {

        for (ChatRoom c: this.chatRooms)
            if (c.getChatRoomName().equals(chatRoomName))
                return c;

        return null;
    }

    boolean checkUserName(String userName){

        for (User u: this.users)
            if (u.getUserName().equals(userName))
                return true;

            return false;
    }

    boolean checkChatRoomName(String chatRoomName){

        for (ChatRoom cr: this.chatRooms)
            if (cr.getChatRoomName().equals(chatRoomName))
                return true;

        return false;
    }

    /**
     * Removes a user and its associated thread from the chatMembers and threads lists.
     * @param user is the chat user being removed
     */
    void removeUser(User user){
        for (int i = 0; i < this.users.size(); i++){
            if(this.users.get(i).equals(user))
                this.users.remove(i);

            if (user.getChatRoom() != null)
                user.getChatRoom().removeUser(user);

            System.out.println("Number of connected users: " + this.users.size());
        }
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }
}

package server;

import javax.net.ssl.SSLSocket;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    private final MessageHandler messageHandler;
    private final List<Room> rooms;
    private final List<User> users;

    Chat(){
        this.messageHandler = new MessageHandler(this);
        this.users = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.rooms.add(new Room("a"));
        getChatRoom("a").addChatFile(new ChatFile("f", new byte[]{'a','b','c','\n','x','a','b','c', '\n'}));
    }

    void newUser(SSLSocket socket){

        User user = new User(socket, this.messageHandler);
        this.users.add(user);
        Thread thread = new Thread(user);
        thread.start();

        System.out.println("Number of connected users: " + this.users.size());
    }

    public boolean joinChatRoom(User user, String chatRoomName){

        Room room;

        if ((room = getChatRoom(chatRoomName)) == null )
            return false;

        checkUserRoomPresence(user);

        room.addUser(user);
        user.setChatRoom(room);

        return true;
    }

    public boolean createChatRoom(User user, String chatRoomName){

        if (chatRoomName.isEmpty() || checkChatRoomName(chatRoomName))
            return true;

        checkUserRoomPresence(user);

        Room room = new Room(chatRoomName);

        room.addUser(user);
        this.rooms.add(room);

        return false;
    }

    private void checkUserRoomPresence(User user){
        if (user.inChatRoom()) {
            messageHandler.leftRoom(user);
            leaveChatRoom(user);
        }
    }


    public Room getChatRoom(String chatRoomName) {

        for (Room c: this.rooms)
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

        for (Room cr: this.rooms)
            if (cr.getChatRoomName().equals(chatRoomName))
                return true;

        return false;
    }

    void leaveChatRoom(User user){

        Room room = user.getChatRoom();

        if(null != room){
            room.removeUser(user);
            user.setChatRoom(null);

            if (0 >= room.getPresentUsers())
                rooms.remove(room);
        }
    }

    /**
     * Removes a user and its associated thread from the chatMembers and threads lists.
     * @param user is the chat user being removed
     */
    void removeUser(User user){

        if (user.inChatRoom())
            messageHandler.leftRoom(user);

        users.remove(user);
        user.deConnect();

        System.out.println("Number of connected users: " + users.size());

    }

    public List<Room> getChatRooms() {
        return rooms;
    }
}

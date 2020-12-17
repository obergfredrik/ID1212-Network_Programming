package server.chat;

import server.service.Messenger;

import javax.net.ssl.SSLSocket;
import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private final Messenger messenger;
    private final List<Room> rooms;
    private final List<User> users;

    public Lobby(){
        this.messenger = new Messenger(this);
        this.users = new ArrayList<>();
        this.rooms = new ArrayList<>();
    }

   public void newUser(SSLSocket socket){

        User user = new User(socket, this.messenger);
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

    public void checkUserRoomPresence(User user){
        if (user.inChatRoom()) {
            messenger.leftRoom(user);
            leaveChatRoom(user);
        }
    }


    public Room getChatRoom(String chatRoomName) {

        for (Room c: this.rooms)
            if (c.getChatRoomName().equals(chatRoomName))
                return c;

        return null;
    }

    public boolean checkUserName(String userName){

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

    public void leaveChatRoom(User user){

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
    public void removeUser(User user){

        if (user.inChatRoom())
            if(1 < user.getChatRoom().getPresentUsers())
                messenger.leftRoom(user);
            else
                rooms.remove(user.getChatRoom());

        users.remove(user);
        user.setLoggedIn(false);

        System.out.println("Number of connected users: " + users.size());
    }

    public List<Room> getChatRooms() {
        return rooms;
    }
}

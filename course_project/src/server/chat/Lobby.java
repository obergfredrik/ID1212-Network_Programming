/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201218
 *  Latest update: -
 *
 */


package server.chat;

import server.service.Messenger;
import javax.net.ssl.SSLSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all user and room interaction in the chat application.
 */
public class Lobby {

    /**
     * Class attributes.
     */
    private final Messenger messenger;
    private final List<Room> rooms;
    private final List<User> users;

    /**
     * A constructor initiating the class attributes.
     */
    public Lobby(){
        this.messenger = new Messenger(this);
        this.users = new ArrayList<>();
        this.rooms = new ArrayList<>();
    }

    /**
     * Creates a new user object and adds it to the users attribute. when a new client has connected to the server.
     *
     * @param socket is the socket handling the interaction with the user.
     */
   public void newUser(SSLSocket socket){

        User user = new User(socket, this.messenger);
        this.users.add(user);
        Thread thread = new Thread(user);
        thread.start();

        System.out.println("Number of connected users: " + this.users.size());
    }

    /**
     * Called when a user wants to join a chat room.
     *
     * @param user is the user making the join request.
     * @param chatRoomName is the name of the chat room the user wishes to join.
     * @return is true if there is a chat room of the given name.
     */
    public boolean joinChatRoom(User user, String chatRoomName){

        Room room;

        if ((room = getChatRoom(chatRoomName)) == null )
            return false;

        checkUserRoomPresence(user);

        room.addUser(user);
        user.setChatRoom(room);

        return true;
    }

    /**
     * Creates a new chat room with a given name if there does not exist a chat room with that name already.
     *
     * @param user is the user creating the chat room.
     * @param chatRoomName is the name of the chat room being created.
     * @return is true if a chat room with the given name already exists and false if the chat room has been created.
     */
    public boolean createChatRoom(User user, String chatRoomName){

        if (chatRoomName.isEmpty() || checkChatRoomName(chatRoomName))
            return true;

        checkUserRoomPresence(user);

        Room room = new Room(chatRoomName);

        room.addUser(user);
        this.rooms.add(room);

        return false;
    }

    /**
     * Checks if a given user is in a chat room and if it is sends a leave message to all other users in the chat room.
     *
     * @param user is whose room presence is being checked.
     */
    public void checkUserRoomPresence(User user){
        if (user.inChatRoom()) {
            messenger.leftRoom(user);
            leaveChatRoom(user);
        }
    }

    /**
     * Checks if a given user name exists in the chat.
     *
     * @param userName is the username of interest.
     * @return is true if the username exists.
     */
    public boolean checkUserName(String userName){

        for (User u: this.users)
            if (u.getUserName().equals(userName))
                return true;

            return false;
    }

    /**
     * Checks if a chat room with a given name exists.
     *
     * @param chatRoomName is the room name of interest.
     * @return is true if a chat room with the given name exists.
     */
    boolean checkChatRoomName(String chatRoomName){

        for (Room cr: this.rooms)
            if (cr.getChatRoomName().equals(chatRoomName))
                return true;

        return false;
    }

    /**
     * Removes a user from the chat room it is in.
     *
     * @param user is the user wanting to leave a chat room.
     */
    public void leaveChatRoom(User user){

        Room room = user.getChatRoom();
        room.removeUser(user);
        user.setChatRoom(null);

        if (room.isEmpty())
            rooms.remove(room);

    }

    /**
     * Removes a user and its associated thread from the users and threads lists.
     *
     * @param user is the user being removed
     */
    public void removeUser(User user) {

        if (user.inChatRoom()) {

            Room room = user.getChatRoom();
            room.removeUser(user);

            if (room.isEmpty())
                rooms.remove(room);
            else
                messenger.leftRoom(user);
        }

        users.remove(user);
        user.setLoggedIn(false);

        System.out.println("Number of connected users: " + users.size());
    }

    /**
     * Searches for a given chat room and returns it if it exists.
     *
     * @param chatRoomName is the name of the wanted chat room.
     * @return is the searched for chat room and null if there does not exist one with the given name.
     */
    public Room getChatRoom(String chatRoomName) {

        for (Room c: this.rooms)
            if (c.getChatRoomName().equals(chatRoomName))
                return c;

        return null;
    }

    /**
     * Retrieves all the existing chat rooms in the chat.
     *
     * @return is the class attribute rooms.
     */
    public List<Room> getChatRooms() {
        return rooms;
    }
}

/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201218
 *  Latest update: -
 *
 */


package server.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information on what users are in the room as well as what files have been uploaded to it.
 */
public class Room {

    /**
     * Class attributes.
     */
    private final List<User> users;
    private final List<ChatFile> files;
    private String chatRoomName;

    /**
     * A constructor instantiating a list of users as well as a list of chatFiles.
     *
     * @param chatRoomName is the name of the chat room.
     */
    Room(String chatRoomName){
        this.users = new ArrayList<>();
        this.files = new ArrayList<>();
        setChatRoomName(chatRoomName);
    }

    /**
     * Adds a user to the chat room.
     *
     * @param user is the user being added.
     */
    public void addUser(User user){
        this.users.add(user);
        user.setChatRoom(this);
    }

    /**
     * Removes a user from the chat room.
     *
     * @param user is the user being removed.
     */
    public void removeUser(User user) {

        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getUserName().equals(user.getUserName())) {
                this.users.remove(i);
                break;
            }
        }
    }

    /**
     * Adds a chat file to the room.
     *
     * @param file is the file being added.
     */
    public void addChatFile(ChatFile file) {

        if (hasFile(file.getName()))
            getChatFile(file.getName()).setData(file.getData());
        else
            this.files.add(file);
    }

    /**
     * States if a file of a given name exists in the room.
     *
     * @param fileName is the name of the file of interest.
     * @return is true if the file exists in the room.
     */
    public boolean hasFile(String fileName){

       if(!fileName.isEmpty())
           for (ChatFile c: files)
               if (c.getName().equals(fileName))
                   return true;

        return false;
    }

    /**
     * Returns a file of interest if it exists.
     *
     * @param fileName is the name of the file of interest.
     * @return is the file of interest and null if there exists no file of the given name.
     */
    public ChatFile getChatFile(String fileName){

        for (ChatFile f:files)
            if (f.getName().equals(fileName))
                return f;

        return null;
    }

    /**
     * Lists all the files stored in the current chat room-
     *
     * @return is a string stating what files exists in the chat room.
     */
    public String getFileNames(){

        if (files.isEmpty())
            return "There are no files uploaded to this chat room";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("The chat room has the following files stored:");

        for (ChatFile f: files) {
            stringBuilder.append("\n");
            stringBuilder.append(f.getName());
        }
        return stringBuilder.toString();
    }

    /**
     * States if there are any users in the room.
     *
     * @return is true if there are no users currently in the chat room.
     */
    boolean isEmpty(){
        return users.size() == 0;
    }

    /**
     * A getter for the attribute chatRoomName.
     *
     * @return is the value of the attribute chatRoomName.
     */
    public String getChatRoomName() {
        return chatRoomName;
    }

    /**
     * A setter for the class attribute chatRoomName.
     *
     * @param chatRoomName is the new value of the chatRoomName attribute.
     */
    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    /**
     * A getter for the class attribute users.
     *
     * @return is a list of the users currently in the room.
     */
    public List<User> getUsers() {
        return users;
    }
}

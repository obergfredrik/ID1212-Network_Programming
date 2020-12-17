package server.chat;

import server.model.ChatFile;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private final List<User> users;
    private final List<ChatFile> files;
    private String chatRoomName;

    Room(String chatRoomName){
        this.users = new ArrayList<>();
        this.files = new ArrayList<>();
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

    public void addChatFile(ChatFile file) {

        if (hasFile(file.getName()))
            getChatFile(file.getName()).setData(file.getData());
        else
            this.files.add(file);
    }

    public boolean hasFile(String fileName){

       if(!fileName.isEmpty())
           for (ChatFile c: files)
               if (c.getName().equals(fileName))
                   return true;

        return false;
    }

    public ChatFile getChatFile(String fileName){

        for (ChatFile f:files)
            if (f.getName().equals(fileName))
                return f;

        return null;
    }

    public String getFileNames(){

        if (files.isEmpty())
            return "There are no files uploaded to this chat room";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("The chat room has the following files stored:\n");

        for (ChatFile f: files)
            stringBuilder.append(f.getName() + "\n");

        return stringBuilder.toString();
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

package server;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatHandler {

    private List<ChatRoom> chatRooms;
    private List<ChatUser> chatUsers;

    ChatHandler (){
        this.chatUsers = new ArrayList<>();
        this.chatRooms = new ArrayList<>();
    }

    void newUser(SSLSocket socket){

        ChatUser user = new ChatUser(socket);
        Thread thread = new Thread(user);
        this.chatUsers.add(user);

        try {
            receiveUserName(user);
            sendChatRoomInformation(user);
        } catch (IOException e) {
            e.printStackTrace();
        }


        thread.start();



        System.out.println("Number of connected users: " + this.chatUsers.size());
    }

    void receiveUserName(ChatUser user) throws IOException {

        user.sendMessage("Hello! Please enter your user name: ");


            String userName = user.receiveMessage();
            user.setUserName(userName);

    }

    void sendChatRoomInformation(ChatUser user) throws IOException {

        if (this.chatRooms.size() == 0){

            user.sendMessage("There are no active chat rooms. Enter a name to create and join a new one.");

            try {
                String chatRoomName = user.receiveMessage();
                ChatRoom chatRoom = new ChatRoom(chatRoomName);
                chatRoom.addUser(user);
                this.chatRooms.add(chatRoom);
            } catch (IOException e) {
                e.printStackTrace();
                user.sendMessage("There was an error when trying to create a chat room. Please try again.");
                sendChatRoomInformation(user);
            }
        }else{

            user.sendMessage("The available chat rooms are as follows:");

            for (ChatRoom c : this.chatRooms)
                user.sendMessage(c.getChatRoomName());

            user.sendMessage("\nWrite *join followed by the chat rooms name to join it\n " +
                    "Write *create followed by a chat room name to create and join it\n" +
                    "Write *quit to exit the chat server");

            handleResponse(user);
        }

    }

    void handleResponse(ChatUser user) throws IOException {

        String message = user.receiveMessage();
        String[] commands = message.split(" ", 2);

        switch (commands[0]){
            case "*join":
                getChatRoom(commands[1]);
                break;
            case "*create":
                String chatRoomName = user.receiveMessage();
                ChatRoom chatRoom = new ChatRoom(chatRoomName);
                chatRoom.addUser(user);
                this.chatRooms.add(chatRoom);
                break;
            case "*quit":
                removeUser(user);
                user.sendMessage("Bye!");
                break;
            default:
                user.sendMessage("An unknown command and/or chatroom was entered. Please try again!");
                sendChatRoomInformation(user);

        }


    }


    public ChatRoom getChatRoom(String chatRoomName) {

        for (ChatRoom c: this.chatRooms)
            if (c.getChatRoomName().equals(chatRoomName))
                return c;

        return null;
    }

    /**
     * Removes a user and its associated thread from the chatMembers and threads lists.
     * @param member is the chat member being removed
     */
    void removeUser(ChatUser member){
        for (int i = 0; i < this.chatUsers.size(); i++){
            if(this.chatUsers.get(i).equals(member)){
                this.chatUsers.remove(i);
            }
        }
    }


}

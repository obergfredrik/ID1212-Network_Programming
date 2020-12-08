package server;

import java.util.List;

public class MessageHandler {

    private ChatHandler chatHandler;
    private String[] commands = {"-help", "-join", "-create", "-leave", "-quit", "-rooms", "-location"};
    private String[] commandInfo ={"Lists all available commands.",
            "The user joins a chat room if the command is followed by an existing chat room. ex. \"-join chatRoom\"",
            "The user creates and joins a chat room of a given one word name ex. \"-create chatroom\"",
            "The user leaves the current chat room.",
            "The user quits the chat server completely.",
            "Lists all available chat rooms",
            "States where you are on the server"};

    MessageHandler(ChatHandler chatHandler){
        this.chatHandler = chatHandler;
    }

    void handleMessage(String message, User user){

        String[] split = message.split(" ", 2);

        switch (checkCommand(split[0])){
            case "-help":
                sendCommands(user);
                break;
            case "-join":
                sendJoinResponse(user, split);
                break;
            case "-create":
                sendCreateResponse(user, split);
                break;
            case "-leave":
                sendLeaveResponse(user);
                break;
            case "-quit":
                sendQuitResponse(user);
                break;
            case "-rooms":
                sendChatRoomInformation(user);
                break;
            case "-location":
                sendLocationInformation(user);
                break;
            case "noCommand":
                user.sendMessage("An incorrect command and/or name on a chat room was entered");
        }
    }

    String checkCommand(String message){

        for (String command : this.commands)
            if (command.equals(message))
                return command;

        return "noCommand";
    }

    void sendCreateResponse(User user, String[] createCommand){

        if(createCommand.length > 1)

            if(this.chatHandler.createChatRoom(user,createCommand[1])) {
                user.sendMessage("There is already a chat room with the name \"" + createCommand[1] + "\"" );
            }else
                sendJoinResponse(user, createCommand);
    }

    void sendJoinResponse(User user, String[] joinCommand){

        if(joinCommand.length > 1)

            if(this.chatHandler.joinChatRoom(user,joinCommand[1])) {
                sendLeaveResponse(user);
                distributeMessage(user.getChatRoom(), user, user.getUserName() + " has entered the chat!");
            }else
                user.sendMessage("No chat room exists with name " + "\"" + joinCommand[1] + "\"");
        else
            user.sendMessage("Could not join chat room. No valid chat room name was entered.");
    }

    void sendLocationInformation(User user){

        if (user.getChatRoom() != null)
            user.sendMessage("You are currently in the chat room \"" + user.getChatRoom().getChatRoomName() + "\"");
        else
            user.sendMessage("You are currently in the lobby.");

    }

    void sendCommands(User user){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("These are the available commands:\n\n");

      for(int i = 0; i < this.commands.length; i++)
          stringBuilder.append(this.commands[i] + "\n" + this.commandInfo[i] + "\n");

      user.sendMessage(stringBuilder.toString());
    }


    void noChatRoom(User user, String chatRoomName){
        user.sendMessage("No chat room exists with name " + "\"" + chatRoomName + "\"");
    }

    boolean updateUserName(User user, String userName){

        if (this.chatHandler.checkUserName(userName)){
            user.sendMessage("There is already a user with the name " + userName + ". Please enter a new one");
            return false;
        }else {
            user.setUserName(userName);
            return true;
        }

    }

    void sendChatRoomInformation(User user){

        List<ChatRoom> chatRooms = this.chatHandler.getChatRooms();

        if (chatRooms.size() == 0)
            user.sendMessage("There are no active chat rooms. Enter a name to create and join a new one.");
        else{
            user.sendMessage("The available chat rooms are as follows:");

            for (ChatRoom c : chatRooms)
                user.sendMessage(c.getChatRoomName());
        }
    }

    void distributeMessage(ChatRoom chatRoom, User user, String message){

        for (User u: chatRoom.getUsers())
            if (!u.getUserName().equals(user.getUserName()))
                u.sendMessage(message);

    }

    void sendLeaveResponse(User user){

        user.leaveChatRoom();
        user.sendMessage("Hi " + user.getUserName() + "! You are currently in the chat lobby. Join or create a new chat to start chatting!");
    }

    void sendQuitResponse(User user){
        this.chatHandler.removeUser(user);
        user.sendMessage("Bye " + user.getUserName() + "! Hope to see you back again soon.");
        user.deConnect();
    }
}

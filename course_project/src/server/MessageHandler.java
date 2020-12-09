package server;

public class MessageHandler {

    private final Chat chat;
    private final Commands commands;
    private final Messages messages;

    MessageHandler(Chat chat){
        this.chat = chat;
        this.commands = new Commands();
        messages = new Messages();
    }

    void handleMessage(String message, User user){

        if (null == message) {
            quitRequest(user);
            return;
        }

        String[] split = message.split(" ", 2);

        switch (commands.checkCommand(split[0])){
            case "-help":
                helpRequest(user);
                break;
            case "-join":
                joinRequest(user, split);
                break;
            case "-create":
                createRequest(user, split);
                break;
            case "-leave":
                leaveRequest(user);
                break;
            case "-quit":
                quitRequest(user);
                break;
            case "-rooms":
                chatRoomsRequest(user);
                break;
            case "-location":
                locationRequest(user);
                break;
            case "-name":
                nameRequest(user, split);
                break;
            case "noCommand":
                if (user.inChatRoom())
                    distributeMessage(user, "[" + user.getUserName() + "] " + message, commands.toUser());
                else
                    user.sendMessage("An incorrect command was entered");
        }
    }


    void helpRequest(User user){
        user.sendMessage(commands.toString());
    }

    void joinRequest(User user, String[] joinCommand){

        if(joinCommand.length > 1 && !joinCommand[1].isEmpty())

            if(chat.joinChatRoom(user,joinCommand[1])) {
                user.sendMessage(messages.joinedChat(user.getUserName(), joinCommand[1]));
                distributeMessage(user, messages.enteredChat(user.getUserName()), commands.notToUser());
            }else
                user.sendMessage(messages.noChatRoom(joinCommand[1]));
        else
            user.sendMessage(messages.joinFailed());
    }


    void createRequest(User user, String[] createCommand){

        if(createCommand.length > 1 && !createCommand[1].isEmpty()) {

             if (this.chat.createChatRoom(user, createCommand[1]))
                user.sendMessage(messages.roomNameOccupied(createCommand[1]));
             else
                user.sendMessage(messages.successfulCreation(user.getUserName(), createCommand[1]));
        }else
            user.sendMessage(messages.failedCreation());
    }

    void leaveRequest(User user){
        distributeMessage(user, messages.leaveMessage(user), commands.notToUser());
        chat.leaveChatRoom(user);
        user.sendMessage(messages.lobby(user));

    }

    void leftRoom(User user){
        distributeMessage(user, messages.leaveMessage(user), commands.notToUser() );
    }

    void locationRequest(User user){

        if (user.getChatRoom() != null)
            user.sendMessage(messages.locationInformation(user));
        else
            user.sendMessage(messages.lobby(user));

    }


   private void nameRequest(User user, String[] nameCommand){

        if (nameCommand.length > 1) {

            if (chat.checkUserName(nameCommand[1])) {
                user.sendMessage(messages.occupiedUserName(nameCommand[1]));
                user.createUserName();
            } else
                user.setUserName(nameCommand[1]);
        }else
            user.sendMessage(messages.emptyUsername());
    }

    void chatRoomsRequest(User user){
        user.sendMessage(messages.chatRooms(chat.getChatRooms()));
    }

    void quitRequest(User user){
        user.sendMessage(messages.quitMessage(user));
        this.chat.removeUser(user);

    }


    void distributeMessage(User user, String message, boolean toUser){

        Room room = user.getChatRoom();

        for (User u: room.getUsers())
            if (!u.getUserName().equals(user.getUserName()))
                u.sendMessage(message);

        if (toUser)
            user.sendMessage(message);
    }
}

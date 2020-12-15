package server;

import java.io.IOException;

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
            case "-h":
                helpRequest(user);
                break;
            case "-j":
                joinRequest(user, split);
                break;
            case "-c":
                createRequest(user, split);
                break;
            case "-l":
                leaveRequest(user);
                break;
            case "-q":
                quitRequest(user);
                break;
            case "-r":
                chatRoomsRequest(user);
                break;
            case "-p":
                positionRequest(user);
                break;
            case "-n":
                nameRequest(user, split);
                break;
            case "-u":
                usersRequest(user);
                break;
            case "-s":
                sendFileRequest(user, message.split(" ", 4));
                break;
            case "-f":
                sendUploadedFiles(user);
                break;
            case "-g":
                getFileRequest(user, split);
                break;
            case "noCommand":
                if (user.inChatRoom())
                    distributeMessage(user, "[" + user.getUserName() + "] " + message, commands.toUser());
                else
                    user.sendMessage("An incorrect command was entered");
        }
    }

    private void getFileRequest(User user, String[] getRequest) {

        if(user.inChatRoom()) {

            if(user.isTransferring()) {

                if(user.getChatRoom().hasFile(getRequest[1])) {
                    try {
                        user.sendFileToClient(user.getChatRoom().getChatFile(getRequest[1]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    user.sendMessage("There is no file called \"" + getRequest[1] + "\" in the chatroom");

                user.setSending(false);
            }else {

                user.setSending(true);
                user.sendMessage("Enter the name of the file you wish to download:");
            }
        }
        else
            user.sendMessage("You need to enter a chat room to download an uploaded file.");

    }

    private void sendUploadedFiles(User user) {
        user.sendMessage(user.getChatRoom().getFileNames());
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

    void positionRequest(User user){

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
            } else {

                if (!user.getUserName().isEmpty())
                    user.sendMessage(messages.userNameUpdated(nameCommand[1]));

                if(user.inChatRoom())
                    distributeMessage(user, messages.changedUsername(user.getUserName(), nameCommand[1]), commands.notToUser());

                user.setUserName(nameCommand[1]);
            }
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

    void usersRequest(User user){

        if(user.inChatRoom())
            user.sendMessage(messages.listUsers(user));
        else
            user.sendMessage(messages.notInChatRoom());
    }

    void distributeMessage(User user, String message, boolean toUser){

        Room room = user.getChatRoom();

        for (User u: room.getUsers())
            if (!u.getUserName().equals(user.getUserName()))
                u.sendMessage(message);

        if (toUser)
            user.sendMessage(message);
    }





    void sendFileRequest(User user, String[] sendRequest)  {

        if(user.inChatRoom()) {

            if(user.isTransferring()){

                if (sendRequest[1].equals("size"))
                    user.sendMessage(messages.toLarge());
                else if (sendRequest[1].equals("file")) {
                    user.sendMessage(messages.noFile(sendRequest[2]));
                }else {
                    try {
                        user.receiveFileFromClient(sendRequest[1], Integer.parseInt(sendRequest[2]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                user.setSending(false);
            }else{
                    user.setSending(true);
                    user.sendMessage(messages.enterFileName());
            }

        }else
            user.sendMessage(messages.notSendFile());
    }
}

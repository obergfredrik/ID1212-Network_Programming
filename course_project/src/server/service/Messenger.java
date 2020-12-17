package server.service;

import server.chat.Lobby;
import server.chat.Room;
import server.chat.User;
import server.service.Commands;
import server.service.Messages;

import java.io.IOException;

public class Messenger {

    private final Lobby lobby;
    private final Commands commands;
    private final Messages messages;

    public Messenger(Lobby lobby){
        this.lobby = lobby;
        this.commands = new Commands();
        messages = new Messages();
    }

    public void handleMessage(String message, User user){

        if (null == message) {
            quitRequest(user);
            return;
        }


        String[] split = message.split(" ", 4);

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
                sendFileRequest(user, split);
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

    public void connectRequest(User user){
        user. sendMessage("Hi and welcome to the chat! Please enter your user name: ");
    }

    public void loginRequest(User user, String userName){

        if (lobby.checkUserName(userName))
            user.sendMessage("There already exists a user with the name \"" + userName + "\". Please choose a new one.");
        else{
             user.setUserName(userName);
             user.setLoggedIn(true);
             user.sendMessage("Hi " + user.getUserName() + "! You are currently in the chat lobby.\nType -h if you need help to get started");
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

                user.setTransferring(false);
            }else {

                user.setTransferring(true);
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

            if(lobby.joinChatRoom(user,joinCommand[1])) {
                user.sendMessage(messages.joinedChat(user.getUserName(), joinCommand[1]));
                distributeMessage(user, messages.enteredChat(user.getUserName()), commands.notToUser());
            }else
                user.sendMessage(messages.noChatRoom(joinCommand[1]));
        else
            user.sendMessage(messages.joinFailed());
    }


    void createRequest(User user, String[] createCommand){

        if(createCommand.length > 1 && !createCommand[1].isEmpty()) {

             if (this.lobby.createChatRoom(user, createCommand[1]))
                user.sendMessage(messages.roomNameOccupied(createCommand[1]));
             else
                user.sendMessage(messages.successfulCreation(user.getUserName(), createCommand[1]));
        }else
            user.sendMessage(messages.failedCreation());
    }

    void leaveRequest(User user){
        distributeMessage(user, messages.leaveMessage(user), commands.notToUser());
        lobby.leaveChatRoom(user);
        user.sendMessage(messages.lobby(user));

    }

    public void leftRoom(User user){
        distributeMessage(user, messages.leaveMessage(user), commands.notToUser() );
    }

    void positionRequest(User user){

        if (user.getChatRoom() != null)
            user.sendMessage(messages.locationInformation(user));
        else
            user.sendMessage(messages.lobby(user));

    }


   private void nameRequest(User user, String[] nameCommand){

        if (nameCommand.length > 1 && !nameCommand[1].isEmpty()) {

            if (!user.getUserName().equals(nameCommand[1]) && lobby.checkUserName(nameCommand[1]))
                user.sendMessage(messages.occupiedUserName(nameCommand[1]));
            else {

                if(user.inChatRoom())
                    distributeMessage(user, messages.changedUsername(user.getUserName(), nameCommand[1]), commands.notToUser());

                user.setUserName(nameCommand[1]);
                user.sendMessage("Your new user name is " + user.getUserName());
            }
        }else
            user.sendMessage(messages.emptyUsername());
    }

    void chatRoomsRequest(User user){
        user.sendMessage(messages.chatRooms(lobby.getChatRooms()));
    }

    void quitRequest(User user){
        user.sendMessage(messages.quitMessage(user));
        this.lobby.removeUser(user);

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
                else if (sendRequest[1].equals("file"))
                    user.sendMessage(messages.noFile(sendRequest[2]));
                else {
                        try {
                            user.receiveFileFromClient(sendRequest[1], Integer.parseInt(sendRequest[2]));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                user.setTransferring(false);
            }else{
                user.setTransferring(true);
                user.sendMessage(messages.enterFileName());
            }

        }else
            user.sendMessage(messages.notSendFile());
    }
}

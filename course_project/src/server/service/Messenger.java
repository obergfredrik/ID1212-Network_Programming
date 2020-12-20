/*
 *  Created by: Fredrik Öberg
 *  Date of creation: 201218
 *  Latest update: -
 *
 */


package server.service;

import server.chat.ChatFile;
import server.chat.Lobby;
import server.chat.Room;
import server.chat.User;
import java.io.IOException;
import java.util.List;

/**
 * Processes the messages received from the user clients and responds according to them.
 */
public class Messenger {

    /**
     * Class attributes.
     */
    private final Lobby lobby;
    private final Commands commands;

    /**
     * A constructor initialising the lobby attribute as well as instantiating the commands attribute.
     *
     * @param lobby is the value of the lobby attribute.
     */
    public Messenger(Lobby lobby){
        this.lobby = lobby;
        this.commands = new Commands();
    }

    /**
     * Called when a new message is received at a user class and processes teh message to se it is a command or a normal message and responds accordingly.
     *
     * @param message is the message received from the user.
     * @param user is the user who received teh message,
     */
    public void handleCommunication(String message, User user){

        if (null == message) {
            handleQuitRequest(user);
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
                handleQuitRequest(user);
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
                handleSendFileRequest(user, split);
                break;
            case "-f":
                sendUploadedFiles(user);
                break;
            case "-g":
                handleFileDownloadRequest(user, split);
                break;
            case "noCommand":
                if (user.inChatRoom())
                    distributeMessage(user, "[" + user.getUserName() + "] " + message, toUser());
                else
                    user.sendMessage("An incorrect command was entered");
        }
    }

    /**
     * Sends a greeting message when a user has connected to the chat server.
     *
     * @param user is the user who has connected to the chat server.
     */
    public void handleConnection(User user){
        user.sendMessage("Hi and welcome to the chat! Please enter your user name: ");
    }

    /**
     * Verifies that a username is valid and is used during the login phase.
     *
     * @param user is the user logging in.
     * @param userName is the proposed username.
     */
    public void handleLogIn(User user, String userName){
        if (lobby.checkUserName(userName))
            user.sendMessage("There already exists a user with the name \"" + userName + "\". Please choose a new one.");
        else{
             user.setUserName(userName);
             user.setLoggedIn(true);
             user.sendMessage("Hi " + user.getUserName() + "! You are currently in the chat lobby.\nType -h if you need help to get started");
        }
    }

    /**
     * Called when a user has quit the server and sends a farewell message to the user in question.
     *
     * @param user is the user who has quit the server.
     */
    public void handleQuitRequest(User user){
        user.sendMessage("Bye \"" + user.getUserName() + "\"! Hope to see you back again soon.");
        this.lobby.removeUser(user);
    }

    /**
     * Sends messages to users of a room when a user has left it.
     *
     * @param user is the user who has left the room in question.
     */
    public void leftRoom(User user){
        distributeMessage(user, user.getUserName() + " has left the chat", notToUser() );
    }


    /**
     * Processes a download request from the client and checks if the user is in a chat room and if the file of interest is in
     * the chat room. The file is then being sent to the user if that is the case.
     *
     * @param user is the user initiating the download.
     * @param getRequest contains the name of the file of interest.
     */
    private void handleFileDownloadRequest(User user, String[] getRequest) {
        if(user.inChatRoom()) {
            if(user.getChatRoom().hasFile(getRequest[1])) {
                try {
                    ChatFile chatFile = user.getChatRoom().getChatFile(getRequest[1]);
                    byte[] data =chatFile.getData();
                    user.sendMessage("OK " + chatFile.getName() + " " + data.length);
                    user.sendFileToClient(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                user.sendMessage("There is no file called \"" + getRequest[1] + "\" in the chatroom");
        }
        else
            user.sendMessage("You need to enter a chat room to download a file.");
    }

    /**
     * Processes a send request and checks if the user is in a chat room. If that is tha case the user is sent an OK response
     * indicating that the user can start the sending process.
     *
     * @param user is the user sending a file.
     * @param sendRequest contains the file name as well as the size of the file.
     */
    private void handleSendFileRequest(User user, String[] sendRequest)  {
        if(user.inChatRoom()) {
            try {
                user.sendMessage("OK");
                user.receiveFileFromClient(sendRequest[1], Integer.parseInt(sendRequest[2]));
            } catch (IOException e) {
                e.printStackTrace();
                user.sendMessage("Could not receive file.");
            }
        }else
            user.sendMessage("ChatRoom");
    }

    /**
     * Sends the names of the files uploaded to the users chat room.
     *
     * @param user is the user interested in the rooms file content,
     */
    private void sendUploadedFiles(User user) {
        user.sendMessage(user.getChatRoom().getFileNames());
    }

    /**
     * Sends a list of the available chat commands.
     *
     * @param user is the client interested in the commands.
     */
    private void helpRequest(User user){
        user.sendMessage(commands.toString());
    }

    /**
     * Enables an user to join a chat room. First it checks that the command is valid and if the room exists. If that is the casethenn
     * the user is placed in that chat room.
     *
     * @param user is the user wanting to join a chat room.
     * @param joinCommand contains the name of the chat room of interest.
     */
    private void joinRequest(User user, String[] joinCommand){
        if(joinCommand.length > 1 && !joinCommand[1].isEmpty())
            if(lobby.joinChatRoom(user,joinCommand[1])) {
                user.sendMessage("Hi " + user.getUserName() + ". You have joined the chat room \"" + joinCommand[1] + "\"");
                distributeMessage(user, user.getUserName() + " has entered the chat!", notToUser());
            }else
                user.sendMessage("No chat room exists with name " + "\"" + joinCommand[1] + "\"");
        else
            user.sendMessage("Could not join chat room. En empty name is not a valid chat room name.");
    }


    /**
     * Creates a new chat room of a given name if one does not already exists. The user automatically joins the room it has created.
     *
     * @param user is the user creating the room.
     * @param createCommand contains the name of the created room.
     */
    private void createRequest(User user, String[] createCommand){
        if(createCommand.length > 1 && !createCommand[1].isEmpty()) {
            if (this.lobby.createChatRoom(user, createCommand[1]))
                user.sendMessage("There is already a chat room with the name \"" + createCommand[1] + "\"");
            else
                user.sendMessage("Hi " + user.getUserName() + "! You have created and entered the chat room " + "\"" + createCommand[1] + "\"");
        }else
            user.sendMessage("To create a chat room you need to enter a name for it");
    }

    /**
     * Enables a user to leave a chat room if it is in one.
     *
     * @param user is the user wanting to leave a chat room.
     */
    private void leaveRequest(User user){
        if (user.inChatRoom()) {
            distributeMessage(user, user.getUserName() + " has left the chat", notToUser());
            lobby.leaveChatRoom(user);
            user.sendMessage("Hi " + user.getUserName() + ". You are currently in the lobby.");
        }else
            user.sendMessage("You are already in the lobby.");

    }

    /**
     * States where a user is in the the chat; either the lobby or what chat room.
     *
     * @param user is the user wanting to now its location.
     */
    private void positionRequest(User user){
        if (user.inChatRoom())
            user.sendMessage("You are currently in the chat room \"" + user.getChatRoom().getChatRoomName() + "\"");
        else
            user.sendMessage("You are currently in the lobby.");

    }


    /**
     * Updates the username of a user if the name does not already exist in the chat.
     *
     * @param user is the user wanting to change its user name.
     * @param nameCommand contains the new user name.
     */
    private void nameRequest(User user, String[] nameCommand){
        if (nameCommand.length > 1 && !nameCommand[1].isEmpty()) {
            if (!user.getUserName().equals(nameCommand[1]) && lobby.checkUserName(nameCommand[1]))
                user.sendMessage("There is already a user with the name " + nameCommand[1] + ". Please enter a new one");
            else {
                if(user.inChatRoom())
                    distributeMessage(user, "\"" + user.getUserName() + "\" has changed name to \"" + nameCommand[1] + "\"", notToUser());

                user.setUserName(nameCommand[1]);
                user.sendMessage("Your new user name is " + user.getUserName());
            }
        }else
            user.sendMessage("User name can not be empty. Please enter a new one");
    }

    private void chatRoomsRequest(User user){
        user.sendMessage(listChatRooms(lobby.getChatRooms()));
    }


    /**
     * Sends the name of every user present in the users chat room.
     *
     * @param user sending the request.
     */
    private void usersRequest(User user){
        if(user.inChatRoom())
            user.sendMessage(listUsers(user));
        else
            user.sendMessage("You are not in a chat room. Join a chat room to list the users in that room.");
    }

    /**
     * Distributes a message to all the members of a chat room.
     *
     * @param user is the user either sending or concerning the message being distributed.
     * @param message is the message being distributed,
     * @param toUser states if the message also is being sent to the user.
     */
   private void distributeMessage(User user, String message, boolean toUser){
        Room room = user.getChatRoom();
        for (User u: room.getUsers())
            if (!u.getUserName().equals(user.getUserName()))
                u.sendMessage(message);

        if (toUser)
            user.sendMessage(message);
    }

    /**
     * Creates a string of all users of the users current chat room.
     *
     * @param user is the user sending the request.
     * @return is the state of the user presence in the chat room.
     */
    private String listUsers(User user){

        Room room = user.getChatRoom();
        List<User> users = room.getUsers();

        if (users.size() == 1)
            return "You are alone in this chat room!";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The following users are in the chat room ");
        stringBuilder.append(room.getChatRoomName());
        stringBuilder.append(":\n");

        for (User u: users) {
            stringBuilder.append("\n");
            stringBuilder.append(u.getUserName());
        }
        return stringBuilder.toString();
    }

    /**
     * Lists all chat rooms currently open in the chat,
     *
     * @param rooms is a list of all currently open rooms
     * @return is the list of all currently open chat rooms.
     */
    private String listChatRooms(List<Room> rooms){

        if (rooms.isEmpty())
            return "There are no active chat rooms. Enter a name to create and join a new one.";
        else{

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The available chat rooms are as follows:");

            for (Room c : rooms) {
                stringBuilder.append("\n");
                stringBuilder.append(c.getChatRoomName());
            }
            return stringBuilder.toString();
        }
    }

    /**
     * States that a message should be sent to the user.
     *
     * @return is a true boolean value.
     */
    private boolean toUser(){
        return true;
    }

    /**
     * States that a message should not be sent to the user.
     *
     * @return is a false boolean value.
     */
    private boolean notToUser(){
        return false;
    }
}

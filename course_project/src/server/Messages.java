package server;

import java.util.List;

public class Messages {

    String joinedChat(String username, String roomName){
        return "Hi " + username+ ". You have joined the chat room \"" + roomName + "\"";
    }

    String enteredChat(String username){
        return username + " has entered the chat!";
    }

    String noChatRoom(String roomName){
        return "No chat room exists with name " + "\"" + roomName + "\"";
    }

    String joinFailed(){
        return "Could not join chat room. En empty name is not a valid chat room name.";
    }

    String roomNameOccupied(String roomName){
        return "There is already a chat room with the name \"" + roomName + "\"";
    }

    String successfulCreation(String username, String roomName){
        return "Hi " + username+ "! You have created and entered the chat room " + "\"" + roomName + "\"";
    }

    String failedCreation(){
        return "To create a chat room you need to enter a name for it";
    }

    String locationInformation(User user){
        return "You are currently in the chat room \"" + user.getChatRoom().getChatRoomName() + "\"";
    }

    String lobby(User user){
        return "Hi " + user.getUserName() + ". You are currently in the lobby.";
    }

    String occupiedUserName(String username){
        return "There is already a user with the name " + username + " or the name is to long. Please enter a new one";
    }

    String emptyUsername(){
        return "User name can not be empty. Please enter a new one";
    }

    String chatRooms(List<Room> rooms){

        if (rooms.isEmpty())
            return "There are no active chat rooms. Enter a name to create and join a new one.";
        else{

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The available chat rooms are as follows:\n");

            for (Room c : rooms)
                stringBuilder.append(c.getChatRoomName() + "\n");

            return stringBuilder.toString();
        }
    }

    String leaveMessage(User user){
        return user.getUserName() + " has left the chat";
    }

    String quitMessage(User user){
        return "Bye " + user.getUserName() + "! Hope to see you back again soon.";
    }

}

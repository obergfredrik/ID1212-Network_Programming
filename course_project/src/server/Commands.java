package server;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    private List<Command> commands;

    Commands(){
        commands = new ArrayList<>();
        commands.add(new Command("-help", "Lists all available commands."));
        commands.add(new Command("-join", "The user joins a chat room if the command is followed by an existing chat room. ex. \"-join chatRoom\""));
        commands.add(new Command("-create", "The user creates and joins a chat room of a given one word name ex. \"-create chatroom\""));
        commands.add(new Command("-leave", "The user leaves the current chat room."));
        commands.add(new Command("-quit", "The user quits the chat server completely."));
        commands.add(new Command("-rooms",  "Lists all available chat rooms"));
        commands.add(new Command("-location", "States where you are on the chat"));
        commands.add(new Command("-name", "Changes the users name to a given one ex. -name name"));
    }

    boolean toUser(){
        return true;
    }

    boolean notToUser(){
        return false;
    }

    String checkCommand(String command){

        for (Command c : commands)
            if (c.getName().equals(command))
                return command;

        return "noCommand";
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("These are the available commands:\n\n");

        for(Command c: commands)
            stringBuilder.append(c.getName() + "\n" + c.getDescription() + "\n");

       return stringBuilder.toString();
    }

    private class Command{

        private String name;
        private String description;

        Command(String name, String description){
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return this.name;
        }

        public String getDescription() {
            return this.description;
        }
    }


}

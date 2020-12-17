package server.service;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    private final List<Command> commands;

    Commands(){
        commands = new ArrayList<>();
        commands.add(new Command("-h", "Lists all available commands."));
        commands.add(new Command("-j", "The user joins a chat room if the command is followed by an existing chat room. ex. \"-join chatRoom\""));
        commands.add(new Command("-c", "The user creates and joins a chat room of a given one word name ex. \"-create chatroom\""));
        commands.add(new Command("-l", "The user leaves the current chat room."));
        commands.add(new Command("-q", "The user quits the chat server completely."));
        commands.add(new Command("-r",  "Lists all available chat rooms"));
        commands.add(new Command("-p", "States your current position in the chat"));
        commands.add(new Command("-n", "Changes the users name to a given one ex. -name name"));
        commands.add(new Command("-u", "Lists the users in the current chat room."));
        commands.add(new Command("-s", "Starts the file sending process."));
        commands.add(new Command("-f", "Lists all available files in the current chat room."));
        commands.add(new Command("-g", "The command for getting a file of a given name ex. -g file.txt"));
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

        private final String name;
        private final String description;

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

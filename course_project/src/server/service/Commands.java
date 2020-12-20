/*
 *  Created by: Fredrik Öberg
 *  Date of creation: 201218
 *  Latest update: -
 *
 */

package server.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and handles the commands used to interact with the chat server.
 */
public class Commands {

    /**
     * Class attributes.
     */
    private final List<Command> commands;

    /**
     * A constructor instantiating all commands used in the chat server.
     */
    Commands(){
        commands = new ArrayList<>();
        commands.add(new Command("-h", "Lists all available commands."));
        commands.add(new Command("-j", "The user joins a chat room if the command is followed by an existing chat room. ex. \"-j chatRoom\""));
        commands.add(new Command("-c", "The user creates and joins a chat room of a given one word name ex. \"-c chatroom\""));
        commands.add(new Command("-l", "The user leaves the current chat room."));
        commands.add(new Command("-q", "The user quits the chat server completely."));
        commands.add(new Command("-r", "Lists all available chat rooms"));
        commands.add(new Command("-p", "States your current position in the chat"));
        commands.add(new Command("-n", "Changes the users name to a given one ex. \"-n name\""));
        commands.add(new Command("-u", "Lists the users in the current chat room."));
        commands.add(new Command("-s", "Enables sending a file from the client to the server ex. \"-s file.txt\""));
        commands.add(new Command("-f", "Lists all available files in the current chat room."));
        commands.add(new Command("-g", "The command for getting a file of a given name ex. \"-g file.txt\""));
    }

    /**
     * Check if a received message is a command or not.
     *
     * @param message is a received message
     * @return is the type of command the message is or noCommand if it is not a command.
     */
    String checkCommand(String message){

        for (Command c : commands)
            if (c.getName().equals(message))
                return message;

        return "noCommand";
    }

    /**
     * Creates a string of all available commands.
     *
     * @return is the commands in the form of a string.
     */
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("These are the available commands:\n");

        for(Command c: commands) {
            stringBuilder.append("\n");
            stringBuilder.append(c.getName());
            stringBuilder.append("\n");
            stringBuilder.append(c.getDescription());
        }

       return stringBuilder.toString();
    }

    /**
     * Contains command information.
     */
    private static class Command{

        /**
         * Class attributes.
         */
        private final String name;
        private final String description;

        /**
         * A constructor setting the name and description of the command.
         *
         * @param name is the name of the command.
         * @param description is the description of the command.
         */
        Command(String name, String description){
            this.name = name;
            this.description = description;
        }

        /**
         * A getter for the class attribute name
         *
         * @return is the value of name.
         */
        public String getName() {
            return this.name;
        }


        /**
         * A getter for the class attribute description.
         *
         * @return is the value of description.
         */
        public String getDescription() {
            return this.description;
        }
    }
}

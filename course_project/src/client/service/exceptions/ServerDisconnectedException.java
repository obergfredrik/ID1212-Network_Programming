/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201217
 *  Latest update: -
 *
 */


package client.service.exceptions;

/**
 * Thrown when the server has disconnected for unknown reasons.
 */
public class ServerDisconnectedException extends Exception{

    /**
     * A constructor.
     *
     * @param message is the error message being logged.
     */
    public ServerDisconnectedException(String message){
        super(message);
    }
}

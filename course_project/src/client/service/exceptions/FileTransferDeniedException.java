/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201217
 *  Latest update: -
 *
 */

package client.service.exceptions;

/**
 * Thrown when when the server the chat client is connected to denies the initiated file transfer.
 */
public class FileTransferDeniedException extends Exception{

    /**
     * A constructor.
     *
     * @param message is the error message being logged.
     */
    public FileTransferDeniedException(String message){
            super(message);
        }
}

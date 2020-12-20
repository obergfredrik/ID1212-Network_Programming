/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201217
 *  Latest update: -
 *
 */

package server.chat;

/**
 * Enables the storage of files in the chat application.
 */
public class ChatFile {

    /**
     * Class attributes.
     */
    private final String name;
    private byte[] data;

    /**
     * A constructor.
     *
     * @param name is the name of the file.
     * @param data contains the data of the file.
     */
   public  ChatFile(String name, byte[] data){
        this.name = name;
        this.data = data;

    }

    /**
     * A setter for the class attribute data.
     *
     * @param data is the new value of data,
     */
    public void setData(byte[] data){
        this.data = data;
    }

    /**
     * A getter for the class attribute name
     *
     * @return is the value of the name attribute,
     */
    public String getName() {
        return name;
    }


    /**
     * A getter for the class attribute name
     *
     * @return is the value of the name attribute,
     */
    public byte[] getData() {
        return data;
    }


}

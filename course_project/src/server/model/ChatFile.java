package server.model;

public class ChatFile {

    private final String name;
    private byte[] data;

   public  ChatFile(String name, byte[] data){
        this.name = name;
        this.data = data;

    }


    public void setData(byte[] data){
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }


}

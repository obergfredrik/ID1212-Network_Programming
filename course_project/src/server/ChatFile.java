package server;

public class ChatFile {

    private String name;
    private byte[] data;

    ChatFile(String name, byte[] data){
        this.name = name;
        this.data = data;

    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }
}

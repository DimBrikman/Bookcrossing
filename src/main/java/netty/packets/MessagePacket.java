package netty.packets;

public class MessagePacket implements Packet {
    private String message;

    public MessagePacket() {
    }

    public MessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

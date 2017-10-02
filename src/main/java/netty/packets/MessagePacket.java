package netty.packets;

public class MessagePacket implements Packet {
    private final String message;

    public MessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

package netty.packets;

public class RejectionPacket implements Packet {
    private final String message;

    public RejectionPacket() {
        message = null;
    }
    public RejectionPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

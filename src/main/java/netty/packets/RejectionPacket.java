package netty.packets;

public class RejectionPacket implements Packet {
    private String message;

    public RejectionPacket() {
    }

    public RejectionPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

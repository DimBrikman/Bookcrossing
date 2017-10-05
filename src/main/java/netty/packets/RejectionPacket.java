package netty.packets;

public class RejectionPacket extends ResponsePacket {
    private final String message;

    public RejectionPacket(RequestPacket request) {
        this(request, null);
    }

    public RejectionPacket(RequestPacket request, String message) {
        super(request);
        this.message = message;
    }

    public String message() {
        return message;
    }
}

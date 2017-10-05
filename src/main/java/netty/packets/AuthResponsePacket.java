package netty.packets;

public class AuthResponsePacket extends ResponsePacket {
    private final boolean status;
    private final String  message;

    public AuthResponsePacket(boolean status, String message, AuthRequestPacket request) {
        super(request);
        this.status  = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}

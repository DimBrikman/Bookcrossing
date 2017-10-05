package netty.packets;

public class RegistrationResponsePacket extends ResponsePacket {
    private final boolean status;
    private final String  message;

    public RegistrationResponsePacket(boolean status, String message, RegistrationRequestPacket request) {
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

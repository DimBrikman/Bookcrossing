package netty.packets;

public class RegistrationResponsePacket implements Packet {
    private RegistrationRequestPacket request;
    private boolean status;
    private String message;

    public RegistrationResponsePacket(boolean status, RegistrationRequestPacket request) {
        this.status = status;
        this.request = request;
    }

    public RegistrationResponsePacket(boolean status, RegistrationRequestPacket request, String message) {
        this.status = status;
        this.request = request;
        this.message = message;
    }

    public boolean isSuccess() {
        return status;
    }

    public RegistrationRequestPacket getRequest() {
        return request;
    }

    public String getMessage() {
        return message;
    }
}

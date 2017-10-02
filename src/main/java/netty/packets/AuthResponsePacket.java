package netty.packets;

public class AuthResponsePacket implements Packet {
    private AuthRequestPacket request;
    private boolean status;
    private String message;

    public AuthResponsePacket(boolean status, AuthRequestPacket request) {
        this.status  = status;
        this.request = request;
    }

    public AuthResponsePacket(boolean status, AuthRequestPacket request, String message) {
        this.status  = status;
        this.request = request;
        this.message = message;
    }

    public boolean isSuccess() {
        return status;
    }

    public AuthRequestPacket getRequest() {
        return request;
    }

    public String getMessage() {
        return message;
    }
}

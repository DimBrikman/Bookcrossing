package netty.packets;

public class AuthResponsePacket implements Packet {
    private AuthRequestPacket authPacket;
    private boolean status;
    private String message;

    public AuthResponsePacket(boolean status, AuthRequestPacket authPacket) {
        this.status = status;
        this.authPacket = authPacket;
    }

    public AuthResponsePacket(boolean status, AuthRequestPacket authPacket, String message) {
        this.status = status;
        this.authPacket = authPacket;
        this.message = message;
    }

    public boolean isSuccess() {
        return status;
    }

    public AuthRequestPacket getAuthPacket() {
        return authPacket;
    }

    public String getMessage() {
        return message;
    }
}

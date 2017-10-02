package netty.packets;

public class AuthResponsePacket extends StatusResponse implements Packet {
    public AuthResponsePacket(boolean status, String message) {
        super(status, message);
    }
}

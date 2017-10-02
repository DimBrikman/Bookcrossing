package netty.packets;

public class RegistrationResponsePacket extends StatusResponse implements Packet {
    public RegistrationResponsePacket(boolean status, String message) {
        super(status, message);
    }
}

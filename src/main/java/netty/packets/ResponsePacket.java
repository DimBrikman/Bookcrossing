package netty.packets;

public abstract class ResponsePacket implements Packet {
    private final long id;

    protected ResponsePacket(RequestPacket request) {
        this.id = request.getId();
    }

    public long getId() {
        return id;
    }
}

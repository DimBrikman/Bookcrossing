package netty.packets;

public abstract class RequestPacket implements Packet {
    private static long idCounter = 0;
    private final  long id;

    protected RequestPacket() {
        id = ++idCounter;
    }

    public long getId() {
        return 0;
    }
}

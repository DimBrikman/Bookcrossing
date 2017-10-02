package netty.packets;

import java.util.Arrays;
import java.util.List;

public class MultiplexedPacket implements Packet {
    private List<Packet> packets;

    public MultiplexedPacket() {
    }

    public MultiplexedPacket(Packet... packets) {
        this.packets.addAll(Arrays.asList(packets));
    }

    public void append(Packet packet) {
        packets.add(packet);
    }

    public List<Packet> packets() {
        return packets;
    }
}

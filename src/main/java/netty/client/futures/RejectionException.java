package netty.client.futures;

import netty.packets.RejectionPacket;

public class RejectionException extends Exception {
    private final RejectionPacket packet;

    RejectionException(RejectionPacket packet) {
        this.packet = packet;
    }

    public RejectionPacket packet() {
        return packet;
    }
}

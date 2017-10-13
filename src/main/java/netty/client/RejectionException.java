package netty.client;

import netty.packets.RejectionPacket;

public class RejectionException extends Exception {
    private final RejectionPacket packet;

    public RejectionException(RejectionPacket packet) {
        this.packet = packet;
    }

    public RejectionPacket packet() {
        return packet;
    }
}

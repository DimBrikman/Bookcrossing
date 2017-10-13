package netty.client.listeners;

import netty.Console;
import netty.packets.RejectionPacket;

public class DefaultRejectionListener extends ServerListener<RejectionPacket> {
    public DefaultRejectionListener() {
        super(RejectionPacket.class);
    }

    @Override
    public void accepted(RejectionPacket packet) {
        Console.println("REJECTED id{" + packet.getId() + "}: " + packet.getMessage());
    }
}

package netty.client.listeners;

import netty.packets.Packet;

public abstract class ServerListener<T extends Packet> {
    private final Class<T> listenPacket;

    public ServerListener(Class<T> listenPacket) {
        this.listenPacket = listenPacket;
    }

    public Class<T> getListeningPacket() {
        return listenPacket;
    }

    public abstract void accepted(T packet);
}

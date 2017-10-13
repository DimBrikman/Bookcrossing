package netty.client.listeners;

import netty.packets.Packet;

import java.util.ArrayList;
import java.util.List;

public class ListenerRegistry {
    private static final ListenerRegistry REGISTRY = new ListenerRegistry();
    private List<ServerListener> listeners = new ArrayList<>();

    private ListenerRegistry() {
    }

    public static ListenerRegistry instance() {
        return REGISTRY;
    }

    public void register(ServerListener listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unchecked")
    public boolean notifyListeners(Packet packet) {
        boolean notified = false;
        for (ServerListener listener : listeners) {
            if (listener.getListeningPacket().equals(packet.getClass())) {
                listener.accepted(packet);
                notified = true;
            }
        }
        return notified;
    }

    public boolean deregister(ServerListener listener) {
        return listeners.remove(listener);
    }
}

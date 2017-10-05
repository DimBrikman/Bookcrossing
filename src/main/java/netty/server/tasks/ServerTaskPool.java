package netty.server.tasks;

import netty.Console;
import netty.packets.*;

import java.util.HashMap;

public class ServerTaskPool {
    private static HashMap<Class<? extends Packet>, Class<? extends ServerTask>> taskPool
             = new HashMap<Class<? extends Packet>, Class<? extends ServerTask>>() {{

        put(AuthRequestPacket.class,         AuthTask.class);
        put(RegistrationRequestPacket.class, RegistrationTask.class);
        put(MessagePacket.class,             MessageTask.class);
    }};

    public static ServerTask taskFor(Packet packet) {
        try {
            return taskPool.get(packet.getClass()).newInstance();
        } catch (Exception e) {
            Console.println("<TASK POOL> ERROR: " + e.getMessage());
            return null;
        }
    }
}

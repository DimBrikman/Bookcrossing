package netty.client.tasks;

import netty.Console;
import netty.packets.*;

import java.util.HashMap;


public class ClientTaskPool {
    private static HashMap<Class<? extends Packet>, Class<? extends ClientTask>> taskPool
             = new HashMap<Class<? extends Packet>, Class<? extends ClientTask>>() {{

    }};

    public static ClientTask taskFor(Packet packet) {
        try {
            return taskPool.get(packet.getClass()).newInstance();
        } catch (Exception e) {
            Console.println("<TASK POOL> ERROR: " + e.getMessage());
            return null;
        }
    }
}

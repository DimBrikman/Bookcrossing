package netty.client.tasks;

import netty.Console;

import java.util.HashMap;

public class TaskResolver {
    private static HashMap<String, Class<? extends ClientTask>> taskPool
            = new HashMap<String, Class<? extends ClientTask>>() {{
        put("u", ClientAuthTask.class);
        put("a", AdminAuthTask.class);
        put("r", RegistrationTask.class);
        put("m", ClientMessageTask.class);
    }};

    public static ClientTask taskFor(String cmd) {
        try {
            return taskPool.get(cmd).newInstance();
        } catch (Exception e) {
            Console.println("<TASK POOL> ERROR: " + e.getMessage());
            return null;
        }
    }
}

package netty.client;

import netty.client.requests.*;

import java.util.HashMap;

public class RequestPool {
    private static HashMap<String, ClientRequest> pool = new HashMap<String, ClientRequest>() {{
        put("1", new ClientAuthRequest());
        put("2", new ClientMessageRequest());
    }};

    public static ClientRequest requestFor(String cmd) {
        return pool.get(cmd);
    }
}

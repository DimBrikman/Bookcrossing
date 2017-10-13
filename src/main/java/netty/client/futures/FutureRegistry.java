package netty.client.futures;

import netty.Console;
import netty.packets.RequestPacket;
import netty.packets.ResponsePacket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unchecked")
public class FutureRegistry {
    private static final FutureRegistry REGISTRY = new FutureRegistry();

    private static volatile HashMap<RequestPacket, ResponseFuture> futures = new HashMap<>();

    private FutureRegistry() {
    }

    public static FutureRegistry instance() {
        return REGISTRY;
    }

    public synchronized ResponseFuture futureFor(RequestPacket request) {
        if (futures.containsKey(request))
            return futures.get(request);
        ResponseFuture future = new ResponseFuture();
        futures.put(request, future);
        Console.println("<REGISTRY> ASSIGN FUTURE FOR: " + request.getClass().getSimpleName());
        return future;
    }

    public synchronized void release(ResponseFuture future) {
        Iterator<Map.Entry<RequestPacket, ResponseFuture>> iterator = futures.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<RequestPacket, ResponseFuture> entry = iterator.next();
            if (entry.getValue().equals(future)) {
                iterator.remove();
                return;
            }
        }
    }

    public synchronized boolean notifyFuture(ResponsePacket response) {
        Console.println("<REGISTRY> NOTIFY FUTURE: " + response.getClass().getSimpleName());
        Iterator<Map.Entry<RequestPacket, ResponseFuture>> iterator = futures.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<RequestPacket, ResponseFuture> entry = iterator.next();
            RequestPacket request = entry.getKey();
            ResponseFuture future = entry.getValue();
            if (isResponseFor(request, response)) {
                iterator.remove();
                future.put(response);
                Console.println("<REGISTRY> FUTURE NOTIFIED");
                return true;
            }
        }
        Console.println("<REGISTRY> NO AWAITING FUTURE FOUND");
        return false;
    }

    public synchronized void cancelFuture(RequestPacket request) {
        futures.get(request).cancel();
    }

    private boolean isResponseFor(RequestPacket request, ResponsePacket response) {
        return request.getId() == response.getId();
    }
}

package netty.client.futures;

import netty.packets.RequestPacket;
import netty.packets.ResponsePacket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unchecked")
public class FutureRegistry {
    private static volatile HashMap<RequestPacket, ResponseFuture> futures = new HashMap<>();

    private FutureRegistry() {
    }

    public static synchronized ResponseFuture futureFor(RequestPacket request) {
        if (futures.containsKey(request))
            return futures.get(request);
        ResponseFuture future = new ResponseFuture();
        futures.put(request, future);
        System.out.println("<REGISTRY> ASSIGN FUTURE FOR: " + request.getClass().getSimpleName());
        return future;
    }

    public static synchronized void deregister(ResponseFuture future) {
        Iterator<Map.Entry<RequestPacket, ResponseFuture>> iterator = futures.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<RequestPacket, ResponseFuture> entry = iterator.next();
            if (entry.getValue().equals(future)) {
                iterator.remove();
                return;
            }
        }
    }

    public static synchronized boolean notifyFuture(ResponsePacket response) {
        System.out.println("<REGISTRY> NOTIFY FUTURE: " + response.getClass().getSimpleName());
        Iterator<Map.Entry<RequestPacket, ResponseFuture>> iterator = futures.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<RequestPacket, ResponseFuture> entry = iterator.next();
            RequestPacket request = entry.getKey();
            ResponseFuture future = entry.getValue();
            if (isResponseFor(request, response)) {
                iterator.remove();
                future.put(response);
                System.out.println("<REGISTRY> NOTIFIED");
                return true;
            }
        }
        System.out.println("<REGISTRY> NO AWAITING FUTURE FOUND");
        return false;
    }

    public static synchronized void cancelFuture(RequestPacket request) {
        futures.get(request).cancel();
    }

    private static boolean isResponseFor(RequestPacket request, ResponsePacket response) {
        return request.getId() == response.getId();
    }
}

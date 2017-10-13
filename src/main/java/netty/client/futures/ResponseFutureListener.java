package netty.client.futures;

import netty.packets.ResponsePacket;

public interface ResponseFutureListener<T extends ResponsePacket> {
    void accepted(ResponseFuture<T> future);
}

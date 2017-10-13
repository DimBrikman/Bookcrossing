package netty.client.futures;

import netty.Console;
import netty.client.RejectionException;
import netty.packets.RejectionPacket;
import netty.packets.ResponsePacket;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ResponseFuture<T extends ResponsePacket> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile T value;

    private boolean isCancelled = false;
    private boolean isDone      = false;

    private ResponseFutureListener<T> listener;

    ResponseFuture() {
    }

    public boolean cancel() {
        if (!isDone) {
            isCancelled = true;
            FutureRegistry.instance().release(this);
            latch.countDown();
            return true;
        }
        return false;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean isDone() {
        return latch.getCount() == 0 && !isCancelled;
    }

    public T get() throws InterruptedException, RejectionException {
        Console.println("<FUTURE> GET AWAIT");
        latch.await();
        if (value instanceof RejectionPacket)
            throw new RejectionException((RejectionPacket) value);
        Console.println("<FUTURE> GET RESULT");
        return value;
    }

    public T get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException, RejectionException {
        Console.println("<FUTURE> GET AWAIT TIMEOUT");
        if (latch.await(timeout, unit)) {
            return get();
        } else {
            throw new TimeoutException();
        }
    }

    public ResponseFuture<T> sync() throws InterruptedException {
        latch.await();
        return this;
    }

    public void addListener(ResponseFutureListener<T> listener) {
        this.listener = listener;
    }

    void put(T value) {
        Console.println("<FUTURE> PUT: " + value.getClass().getSimpleName());
        this.value = value;
        isDone = true;
        latch.countDown();
        if (listener != null) {
            listener.accepted(this);
        }
    }
}

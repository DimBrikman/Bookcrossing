package netty.client.futures;

import netty.packets.RejectionPacket;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ResponseFuture<T> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile T value;

    private boolean isCancelled = false;
    private boolean isDone      = false;

    private ResponseFutureListener<T> listener;

    ResponseFuture() {
    }

    public void cancel() {
        if (!isDone) {
            isCancelled = true;
            FutureRegistry.deregister(this);
            latch.countDown();
            listener.cancelled();
        }
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean isDone() {
        return latch.getCount() == 0 && !isCancelled;
    }

    public T get() throws InterruptedException, RejectionException {
        System.out.println("<FUTURE> GET AWAIT");
        latch.await();
        if (value instanceof RejectionPacket)
            throw new RejectionException((RejectionPacket) value);
        System.out.println("<FUTURE> GET RESULT");
        return value;
    }

    public T get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        if (latch.await(timeout, unit)) {
            return value;
        } else {
            throw new TimeoutException();
        }
    }

    public void addListener(ResponseFutureListener<T> listener) {
        this.listener = listener;
    }

    void put(T value) {
        System.out.println("<FUTURE> PUT: " + value.getClass().getSimpleName());
        this.value = value;
        isDone = true;
        latch.countDown();
        if (listener != null) {
            listener.accepted(this);
        }
    }
}

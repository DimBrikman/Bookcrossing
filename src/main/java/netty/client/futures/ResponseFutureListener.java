package netty.client.futures;

public interface ResponseFutureListener<T> {
    void accepted(ResponseFuture<T> future);
    void cancelled();
}

package netty.client.tasks;

import io.netty.channel.ChannelHandlerContext;
import netty.Console;
import netty.packets.Packet;

import java.util.Objects;

public abstract class ClientTask<T extends Packet> implements Runnable {
    protected ChannelHandlerContext context;
    protected T response;

    public void setContext(T response, ChannelHandlerContext context) {
        this.context  = Objects.requireNonNull(context);
        this.response = response;
    }

    @Override
    public final void run() {
        if (context == null || response == null)
            throw new NullPointerException();
        try {
            process();
        } catch (Throwable t) {
            onException(t);
        }
    }

    public abstract void process();

    public void onException(Throwable t) {
        Console.println("<CLIENT TASK> ERROR: " + this.getClass().getSimpleName() + " -> " + t);
    }
}

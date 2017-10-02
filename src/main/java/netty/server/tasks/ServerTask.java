package netty.server.tasks;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import netty.packets.Packet;
import netty.packets.RejectionPacket;

import java.util.Objects;


public abstract class ServerTask<T extends Packet> implements Runnable {
    protected ChannelHandlerContext context;
    protected T request;

    public void setContext(T request, ChannelHandlerContext context) {
        this.request = request;
        this.context = context;
    }

    public Channel getChannel() {
        return context.channel();
    }

    @Override
    public final void run() {
        if (context == null || request == null)
            throw new NullPointerException();
        try {
            process();
        } catch (Throwable t) {
            onException(t);
        } finally {
            request = null;
            context = null;
        }
    }

    public abstract void process();

    public abstract void onException(Throwable t);

    public ChannelFuture reject(String reason) {
        return context.writeAndFlush(new RejectionPacket(reason));
    }

    @Deprecated
    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}

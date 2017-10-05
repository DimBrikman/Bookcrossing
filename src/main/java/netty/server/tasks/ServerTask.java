package netty.server.tasks;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import netty.packets.Packet;
import netty.packets.RejectionPacket;
import netty.packets.RequestPacket;

import java.util.Objects;

public abstract class ServerTask<T extends Packet> implements Runnable {
    protected ChannelHandlerContext context;
    protected T packet;

    public void setContext(T packet, ChannelHandlerContext context) {
        this.context = Objects.requireNonNull(context);
        this.packet  = packet;
    }

    public Channel getChannel() {
        return context.channel();
    }

    @Override
    public final void run() {
        if (context == null || packet == null)
            throw new NullPointerException();
        try {
            process();
        } catch (Throwable t) {
            onException(t);
        }
    }

    public abstract void process();

    public abstract void onException(Throwable t);

    public ChannelFuture reject(String reason) {
        if (packet instanceof RequestPacket)
            return context.writeAndFlush(new RejectionPacket((RequestPacket) packet, reason));
        return null;
    }

    @Deprecated
    protected void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}

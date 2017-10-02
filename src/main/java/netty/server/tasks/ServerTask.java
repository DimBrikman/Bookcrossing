package netty.server.tasks;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import netty.packets.RejectionPacket;


public abstract class ServerTask implements Runnable {
    protected ChannelHandlerContext context;

    public ServerTask(ChannelHandlerContext context) {
        this.context = context;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    @Override
    public void run() {
        try {
            if (process()) {
                onSuccess();
            } else {
                onFailure();
            }
        } catch (Throwable t) {
            onException(t);
        }
    }

    public abstract boolean process();

    public abstract void onSuccess();

    public abstract void onFailure();

    public abstract void onException(Throwable e);

    public ChannelFuture reject(String reason) {
        return context.writeAndFlush(new RejectionPacket(reason));
    }

    public void rejectAndClose(String reason) {
        ChannelFuture future = reject(reason);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}

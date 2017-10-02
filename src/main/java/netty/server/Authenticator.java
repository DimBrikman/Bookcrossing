package netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.packets.AuthRequestPacket;
import netty.packets.RejectionPacket;
import netty.server.tasks.AuthTask;


public class Authenticator extends ChannelInboundHandlerAdapter {
    private static ServerExecutor<AuthTask> executor = new ServerExecutor<>(3, 5, 100);
    private static final int BAD_ATTEMPTS_THRESHOLD = 3;

    private int badAttempts = 0;

    @Override
    public void channelRegistered(ChannelHandlerContext context) throws Exception {
        System.out.println("<IN> REGISTERED: " + context.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        System.out.println("<IN> UNREGISTERED: " + context.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof AuthRequestPacket) {
            AuthRequestPacket request = (AuthRequestPacket) msg;
            executor.execute(new AuthTask(request, context));
        } else {
            badAttempts++;
            System.out.println(String.format(
                    "<AUTH> BAD PACKET (%d/%d): %s",
                    badAttempts,
                    BAD_ATTEMPTS_THRESHOLD,
                    msg.getClass().getSimpleName()
            ));
            ChannelFuture future = context.writeAndFlush(new RejectionPacket("unauthorized"));
            if (badAttempts == BAD_ATTEMPTS_THRESHOLD) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        System.out.println("<AUTH> ERROR: " + cause);
        context.close();
    }
}

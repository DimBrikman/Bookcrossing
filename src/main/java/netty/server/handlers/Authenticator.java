package netty.server.handlers;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.Console;
import netty.packets.*;
import netty.server.tasks.*;


public class Authenticator extends ChannelInboundHandlerAdapter {
    private static ServerTaskExecutor executor = new ServerTaskExecutor(3, 5, 100, "AUTH");

    private static final int BAD_ATTEMPTS_THRESHOLD = 3;
    private int badAttempts = 0;

    @Override
    public void channelRegistered(ChannelHandlerContext context) throws Exception {
        Console.println("<AUTH> REGISTERED: " + context.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        Console.println("<AUTH> UNREGISTERED: " + context.channel().remoteAddress());
    }

    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof RequestPacket) {
            RequestPacket request = (RequestPacket) msg;
            if (request instanceof AuthRequestPacket || request instanceof RegistrationRequestPacket) {
                ServerTask task = ServerTaskPool.taskFor(request);
                Console.println("<AUTH> TASK " + task.getClass().getSimpleName()
                                + " FOR " + request.getClass().getSimpleName());
                task.setContext(request, context);
                executor.execute(task);
            } else {
                badAttempts++;
                Console.println(String.format(
                        "<AUTH> BAD PACKET (%d/%d): %s",
                        badAttempts,
                        BAD_ATTEMPTS_THRESHOLD,
                        msg.getClass().getSimpleName()
                ));
                ChannelFuture future = context.writeAndFlush(new RejectionPacket(request, "unauthorized"));
                if (badAttempts == BAD_ATTEMPTS_THRESHOLD) {
                    future.addListener(ChannelFutureListener.CLOSE);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        Console.println("<AUTH> ERROR: " + cause);
        context.close();
    }
}

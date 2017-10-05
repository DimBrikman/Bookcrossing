package netty.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.Console;
import netty.client.futures.FutureRegistry;
import netty.packets.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        Console.println("<IN> ACTIVE");
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof ResponsePacket) {
            System.out.println("<IN> ACCEPT RESPONSE: " + msg.getClass().getSimpleName());
            ResponsePacket response = (ResponsePacket) msg;
            FutureRegistry.notifyFuture(response);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        Console.println("<IN> DISCONNECTED");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Console.println("<IN> ERROR: " + cause);
        context.close();
    }
}

package netty.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.Console;
import netty.client.futures.FutureRegistry;
import netty.client.listeners.ListenerRegistry;
import netty.packets.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private static ExecutorService  executor  = Executors.newSingleThreadExecutor();
    private static FutureRegistry   futures   = FutureRegistry.instance();
    private static ListenerRegistry listeners = ListenerRegistry.instance();

    // TODO: asynchronous notification of Listeners and Futures
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        Console.println("<IN> ACCEPT: " + msg.getClass().getSimpleName());
        if (msg instanceof Packet) {
            listeners.notifyListeners((Packet) msg);
            if (msg instanceof ResponsePacket) {
                futures.notifyFuture((ResponsePacket) msg);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Console.println("<IN> ERROR: " + cause);
        context.close();
    }
}

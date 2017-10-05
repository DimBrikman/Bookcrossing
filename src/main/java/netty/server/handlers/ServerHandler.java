package netty.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.Console;
import netty.packets.Packet;
import netty.packets.RequestPacket;
import netty.server.tasks.ServerTask;
import netty.server.tasks.ServerTaskExecutor;
import netty.server.tasks.ServerTaskPool;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static ServerTaskExecutor executor = new ServerTaskExecutor(4, 8, 500, "MAIN");

    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        Console.println("<SERVER> UNREGISTERED: " + context.channel().remoteAddress());
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof Packet) {
            Packet packet = (Packet) msg;
            ServerTask task = ServerTaskPool.taskFor(packet);
            task.setContext(packet, context);
            executor.execute(task);
        } else {
            Console.println("<SERVER> BAD PACKET: " + msg.getClass().getSimpleName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Console.println("<SERVER> ERROR: " + cause);
        context.close();
    }
}

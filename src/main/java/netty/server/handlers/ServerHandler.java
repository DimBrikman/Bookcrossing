package netty.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.Console;
import netty.packets.MultiplexedPacket;
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
    @SuppressWarnings("ConstantConditions")
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof Packet) {
            if (msg instanceof MultiplexedPacket) {
                for (Packet packet : ((MultiplexedPacket) msg).packets())
                    serve(packet, context);
            } else {
                serve((Packet) msg, context);
            }
        } else {
            Console.println("<SERVER> BAD PACKET: " + msg.getClass().getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    private void serve(Packet packet, ChannelHandlerContext context) {
        ServerTask task = ServerTaskPool.taskFor(packet);
        task.setContext(packet, context);
        executor.execute(task);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Console.println("<SERVER> ERROR: " + cause);
        context.close();
    }
}

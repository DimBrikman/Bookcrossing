package netty.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.Console;
import netty.packets.AuthRequestPacket;
import netty.packets.MessagePacket;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        Console.println("<IN> UNREGISTERED: " + context.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof MessagePacket) {
            MessagePacket packet = (MessagePacket) msg;
            Console.println("<IN> READ: " + packet.getMessage());
            context.writeAndFlush(new MessagePacket("(re)" + packet.getMessage()));
        } else if (msg instanceof AuthRequestPacket){
            context.writeAndFlush(new MessagePacket("already authorized"));
        } else {
            Console.println("<IN> BAD PACKET: " + msg.getClass().getSimpleName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Console.println("<IN> ERROR: " + cause);
        context.close();
    }
}

package netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.packets.AuthRequestPacket;
import netty.packets.MessagePacket;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        System.out.println("<IN> UNREGISTERED: " + context.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof MessagePacket) {
            MessagePacket packet = (MessagePacket) msg;
            System.out.println("<IN> READ: " + packet.getMessage());
            context.writeAndFlush(new MessagePacket("(re)" + packet.getMessage()));
        } else if (msg instanceof AuthRequestPacket){
            context.writeAndFlush(new MessagePacket("already authorized"));
        } else {
            System.out.println("<IN> BAD PACKET: " + msg.getClass().getSimpleName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        System.out.println("<IN> ERROR: " + cause);
        context.close();
    }
}

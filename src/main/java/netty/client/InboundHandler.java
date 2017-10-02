package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.packets.AuthResponsePacket;
import netty.packets.MessagePacket;
import netty.packets.RejectionPacket;

public class InboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof AuthResponsePacket) {
            AuthResponsePacket response = (AuthResponsePacket) msg;
            String log = response.isSuccess() ? "<IN> AUTH SUCCESS" : "<IN> AUTH FAILED: " + response.getMessage();
            Console.println(log);
        } else if (msg instanceof MessagePacket) {
            MessagePacket packet = (MessagePacket) msg;
            Console.println("<IN> MESSAGE: " + packet.getMessage());
        } else if (msg instanceof RejectionPacket){
            RejectionPacket packet = (RejectionPacket) msg;
            Console.println("<IN> REJECTED: " + packet.getMessage());
        } else {
            Console.println("<IN> BAD PACKET: " + msg);
        }
        Console.flush();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        Console.println("<IN> DISCONNECTED");
        Console.flush();
        Console.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Console.println("<IN> ERROR: " + cause);
        Console.flush();
        context.close();
    }
}

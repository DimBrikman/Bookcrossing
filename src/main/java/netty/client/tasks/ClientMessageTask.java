package netty.client.tasks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.Console;
import netty.packets.AuthResponsePacket;
import netty.packets.MessagePacket;

import java.io.IOException;

public class ClientMessageTask extends ClientTask<MessagePacket> {
    @Override
    public void process() {

    }

//    public void execute(ChannelHandlerContext context, ChannelPromise promise) {
//        try {
//            String message = Console.readLine();
//            MessagePacket packet = new MessagePacket(message);
//            context.writeAndFlush(packet, promise);
//        } catch (IOException e) {
//            Console.println("<MSG REQ> ERROR: " + e.getMessage());
//        }
//    }
}

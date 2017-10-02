package netty.client.requests;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.client.Console;
import netty.packets.MessagePacket;

import java.io.IOException;

public class ClientMessageRequest extends ClientRequest {

    @Override
    public void execute(ChannelHandlerContext context, ChannelPromise promise) {
        try {
            String message = Console.readLine();
            MessagePacket request = new MessagePacket(message);
            context.writeAndFlush(request, promise);
        } catch (IOException e) {
            Console.println("<MSG REQ> ERROR: " + e.getMessage());
        }
    }
}

package netty.client.tasks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.Console;
import netty.packets.MessagePacket;

import java.io.IOException;

public class ClientMessageTask implements ClientTask {
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

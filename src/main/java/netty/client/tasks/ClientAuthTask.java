package netty.client.tasks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.Console;
import netty.packets.AuthPacket;

import java.io.IOException;

public class ClientAuthTask implements ClientTask {
    @Override
    public void execute(ChannelHandlerContext context, ChannelPromise promise) {
        try {
            Console.print("login: ");
            String login = Console.readLine();
            Console.print("pass: ");
            String pass = Console.readLine();

            AuthPacket packet = new AuthPacket();
            packet.setRequest(new AuthPacket.AuthRequest(login, pass));

            context.writeAndFlush(packet, promise);
        } catch (IOException e) {
            Console.println("<AUTH REQ> ERROR: " + e.getMessage());
        }
    }
}

package netty.client.requests;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.client.Console;
import netty.packets.AuthRequestPacket;

import java.io.IOException;

public class ClientAuthRequest extends ClientRequest {
    @Override
    public void execute(ChannelHandlerContext context, ChannelPromise promise) {
        try {
            Console.print("login: ");
            String login = Console.readLine();
            Console.print("pass: ");
            String pass = Console.readLine();
            AuthRequestPacket request = new AuthRequestPacket(login, pass);
            context.writeAndFlush(request, promise);
        } catch (IOException e) {
            Console.println("<AUTH REQ> ERROR: " + e.getMessage());
        }
    }
}

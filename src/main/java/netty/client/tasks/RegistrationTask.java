package netty.client.tasks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.Console;
import netty.packets.RegistrationRequestPacket;

import java.io.IOException;

public class RegistrationTask implements ClientTask {
    @Override
    public void execute(ChannelHandlerContext context, ChannelPromise promise) {
        try {
            Console.print("login: ");
            String login = Console.readLine();
            Console.print("pass: ");
            String pass = Console.readLine();
            Console.print("pass again: ");
            while (!Console.readLine().equals(pass))
                Console.print("incorrect!\npass again: ");
            RegistrationRequestPacket request = new RegistrationRequestPacket(login, pass);
            context.writeAndFlush(request, promise);
        } catch (IOException e) {
            Console.println("<REGIST REQ> ERROR: " + e.getMessage());
        }
    }
}

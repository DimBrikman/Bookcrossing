package netty.server.tasks;

import io.netty.channel.ChannelHandlerContext;
import netty.annotations.SingleChannelTask;
import netty.Console;
import netty.packets.*;
import netty.server.handlers.ServerHandler;
import netty.server.mock.DatabaseMock;

@SingleChannelTask
public class AuthTask extends ServerTask<AuthRequestPacket> {

    public void process() {
        if (context.pipeline().get("AuthHandler") == null) {
            Console.println("<AUTH TASK> ALREADY AUTH");
            context.writeAndFlush(new AuthResponsePacket(false, "already authorized"));
        } else {
            if (DatabaseMock.validateUser(request.getLogin(), request.getPassword(), 2000)) {
                Console.println("<AUTH TASK> SUCCESS");
                context.writeAndFlush(new AuthResponsePacket(true, "auth success"));
                context.pipeline().addLast(new ServerHandler());
                context.pipeline().remove("AuthHandler");
            } else {
                Console.println("<AUTH TASK> FAILED");
                context.writeAndFlush(new AuthResponsePacket(false, "incorrect login or password"));
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        Console.println("<AUTH TASK> ERROR: " + e.getMessage());
        reject(e.getMessage());
    }
}

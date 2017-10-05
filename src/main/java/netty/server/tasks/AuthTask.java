package netty.server.tasks;

import netty.annotations.SingleChannelTask;
import netty.Console;
import netty.packets.*;
import netty.server.AttributeKeys;
import netty.server.ChannelCache;
import netty.server.handlers.ServerHandler;
import netty.server.mock.DatabaseMock;

@SingleChannelTask
public class AuthTask extends ServerTask<AuthRequestPacket> {

    public void process() {
        if (context.pipeline().get("AuthHandler") == null) {
            Console.println("<AUTH TASK> ALREADY AUTH");
            context.writeAndFlush(new AuthResponsePacket(false, "already authorized", packet));
        } else {
            if (DatabaseMock.validateUser(packet.getLogin(), packet.getPassword(), 1000)) {
                Console.println("<AUTH TASK> SUCCESS");
                ChannelCache cache = new ChannelCache();
                cache.setUser(DatabaseMock.getUser(packet.getLogin(), 0));
                context.channel().attr(AttributeKeys.CHANNEL_CACHE).set(cache);
                context.writeAndFlush(new AuthResponsePacket(true, "auth success", packet));
                context.pipeline().addLast(new ServerHandler());
                context.pipeline().remove("AuthHandler");
            } else {
                Console.println("<AUTH TASK> FAILED");
                context.writeAndFlush(new AuthResponsePacket(false, "incorrect login or password", packet));
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        Console.println("<AUTH TASK> ERROR: " + e.getMessage());
        reject(e.getMessage());
    }
}

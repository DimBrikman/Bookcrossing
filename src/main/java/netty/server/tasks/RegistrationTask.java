package netty.server.tasks;

import netty.Console;
import netty.annotations.SingleChannelTask;
import netty.packets.RegistrationRequestPacket;
import netty.packets.RegistrationResponsePacket;
import netty.server.AttributeKeys;
import netty.server.ChannelCache;
import netty.server.handlers.ServerHandler;
import netty.server.mock.DatabaseMock;

@SingleChannelTask
public class RegistrationTask extends ServerTask<RegistrationRequestPacket> {

    @Override
    public void process() {
        if (DatabaseMock.registerUser(packet.getLogin(), packet.getPassword(), 1000)) {
            Console.println("<REG TASK> SUCCESS");
            ChannelCache cache = new ChannelCache();
            cache.setUser(DatabaseMock.getUser(packet.getLogin(), 0));
            context.channel().attr(AttributeKeys.CHANNEL_CACHE).set(cache);
            context.writeAndFlush(new RegistrationResponsePacket(true, "registration success", packet));
            context.pipeline().addLast(new ServerHandler());
            context.pipeline().remove("AuthHandler");
        } else {
            Console.println("<REG TASK> FAILED");
            context.writeAndFlush(new RegistrationResponsePacket(false, "login already exists", packet));
        }
    }

    @Override
    public void onException(Throwable e) {
        Console.println("<REG TASK> ERROR: " + e.getMessage());
        reject(e.getMessage());
    }
}

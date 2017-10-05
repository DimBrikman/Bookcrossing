package netty.server.tasks;

import netty.Console;
import netty.packets.MessagePacket;
import netty.server.AttributeKeys;
import netty.server.mock.UserMock;

public class MessageTask extends ServerTask<MessagePacket> {

    @Override
    public void process() {
        String message = packet.getMessage();
        UserMock user  = context.channel().attr(AttributeKeys.CHANNEL_CACHE).get().getUser();
        Console.println("<MSG TASK> MSG (" + user.name + "): " + message);
        context.writeAndFlush(new MessagePacket("(re)" + message));
    }

    @Override
    public void onException(Throwable t) {
        Console.println("<MSG TASK> ERROR: " + t.getMessage());
    }
}

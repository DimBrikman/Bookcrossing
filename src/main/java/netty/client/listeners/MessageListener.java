package netty.client.listeners;

import netty.Console;
import netty.packets.MessagePacket;

public class MessageListener extends ServerListener<MessagePacket> {
    public MessageListener() {
        super(MessagePacket.class);
    }

    @Override
    public void accepted(MessagePacket packet) {
        Console.println("<MESSAGE> : " + packet.getMessage());
    }
}

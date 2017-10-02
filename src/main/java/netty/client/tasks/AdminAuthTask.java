package netty.client.tasks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.Console;
import netty.packets.AuthRequestPacket;


public class AdminAuthTask implements ClientTask {
    @Override
    public void execute(ChannelHandlerContext context, ChannelPromise promise) {
        AuthRequestPacket request = new AuthRequestPacket("admin", "admin");
        context.writeAndFlush(request, promise);
    }
}

package netty.client.requests;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import netty.packets.AuthRequestPacket;


public class AdminAuth extends ClientRequest {

    @Override
    public void execute(ChannelHandlerContext context, ChannelPromise promise) {
        AuthRequestPacket request = new AuthRequestPacket("admin", "123");
        context.writeAndFlush(request, promise);
    }
}

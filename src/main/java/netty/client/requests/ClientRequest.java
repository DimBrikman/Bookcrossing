package netty.client.requests;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public abstract class ClientRequest {

    public abstract void execute(ChannelHandlerContext context, ChannelPromise promise);
}

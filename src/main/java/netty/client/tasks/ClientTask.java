package netty.client.tasks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public interface ClientTask {
    void execute(ChannelHandlerContext context, ChannelPromise promise);
}

package netty.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import netty.Console;
import netty.client.tasks.ClientTask;
import netty.client.tasks.TaskResolver;

public class OutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext context, Object msg, ChannelPromise promise) throws Exception {
        ClientTask task = TaskResolver.taskFor((String) msg);
        if (task != null) {
            task.execute(context, promise);
        } else {
            Console.println("<OUT> ERROR: unknown command");
            promise.setSuccess();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Console.println("<OUT> ERROR: " + cause);
        Console.flush();
        context.close();
    }
}

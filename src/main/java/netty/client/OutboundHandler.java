package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import netty.client.requests.ClientRequest;

public class OutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext context, Object msg, ChannelPromise promise) throws Exception {
        ClientRequest request = RequestPool.requestFor((String) msg);
        if (request != null) {
            request.execute(context, promise);
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

package netty.server.tasks;

import io.netty.channel.ChannelHandlerContext;
import netty.annotations.SingleChannelTask;
import netty.Console;
import netty.packets.AuthRequestPacket;
import netty.packets.AuthResponsePacket;
import netty.packets.RejectionPacket;
import netty.server.handlers.ServerHandler;
import netty.server.mock.DatabaseMock;

@SingleChannelTask
public class AuthTask extends ServerTask {
    private final AuthRequestPacket request;

    public AuthTask(AuthRequestPacket request, ChannelHandlerContext context) {
        super(context);
        this.request = request;
    }

    public boolean process() {
        return DatabaseMock.validateUser(request.getLogin(), request.getPassword(), 2000);
    }

    @Override
    public void onSuccess() {
        Console.println("<AUTH TASK> SUCCESS");
        context.writeAndFlush(new AuthResponsePacket(true, "auth success"));
        context.pipeline().addLast(new ServerHandler());
        context.pipeline().remove("AuthHandler");
    }

    @Override
    public void onFailure() {
        Console.println("<AUTH TASK> FAILED");
        context.writeAndFlush(new AuthResponsePacket(false, "incorrect login or password"));
    }

    @Override
    public void onException(Throwable e) {
        Console.println("AUTH TASK> ERROR: " + e.getMessage());
        context.writeAndFlush(new RejectionPacket(e.getMessage()));
    }
}

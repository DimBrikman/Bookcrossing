package netty.server.tasks;

import io.netty.channel.ChannelHandlerContext;
import netty.Console;
import netty.packets.RegistrationRequestPacket;
import netty.packets.RegistrationResponsePacket;
import netty.packets.RejectionPacket;
import netty.server.handlers.ServerHandler;
import netty.server.mock.DatabaseMock;

public class RegistrationTask extends ServerTask {
    private final RegistrationRequestPacket request;

    public RegistrationTask(RegistrationRequestPacket request, ChannelHandlerContext context) {
        super(context);
        this.request = request;
    }

    @Override
    public boolean process() {
        return DatabaseMock.registerUser(request.getLogin(), request.getPassword(), 2000);
    }

    @Override
    public void onSuccess() {
        Console.println("<REGISTR TASK> SUCCESS");
        context.writeAndFlush(new RegistrationResponsePacket(true, request));
        context.pipeline().addLast(new ServerHandler());
        context.pipeline().remove("AuthHandler");
    }

    @Override
    public void onFailure() {
        Console.println("<REGISTR TASK> FAILED");
        context.writeAndFlush(new RegistrationResponsePacket(false, request, "login already exists"));
    }

    @Override
    public void onException(Throwable e) {
        Console.println("REGISTR TASK> ERROR: " + e.getMessage());
        context.writeAndFlush(new RejectionPacket(e.getMessage()));
    }
}

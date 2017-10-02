package netty.server.tasks;

import io.netty.channel.ChannelHandlerContext;
import netty.annotations.SingleTask;
import netty.packets.AuthRequestPacket;
import netty.packets.AuthResponsePacket;
import netty.packets.RejectionPacket;
import netty.server.ServerHandler;

import java.util.HashMap;

@SingleTask
public class AuthTask extends ServerTask {
    private final AuthRequestPacket request;

    public AuthTask(AuthRequestPacket request, ChannelHandlerContext context) {
        super(context);
        this.request = request;
    }

    public boolean process() {
        return validate(request);
    }

    @Override
    public void onSuccess() {
        System.out.println("<AUTH TASK> SUCCESS");
        context.writeAndFlush(new AuthResponsePacket(true, request));
        context.pipeline().addLast(new ServerHandler());
        context.pipeline().remove("AuthHandler");
    }

    @Override
    public void onFailure() {
        System.out.println("<AUTH TASK> FAILED");
        context.writeAndFlush(new AuthResponsePacket(false, request, "incorrect login or password"));
    }

    @Override
    public void onException(Throwable e) {
        System.out.println("AUTH TASK> ERROR: " + e.getMessage());
        context.writeAndFlush(new RejectionPacket(e.getMessage()));
    }

    //**************
    // DATABASE MOCK
    //**************
    private static HashMap<String, String> users = new HashMap<String, String>(){{
        put("admin", "123");
        put("user", "qwerty");
    }};

    private boolean validate(AuthRequestPacket packet) {
        return packet.getPassword().equals(users.get(packet.getLogin()));
    }
}

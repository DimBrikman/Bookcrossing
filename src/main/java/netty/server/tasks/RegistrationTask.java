package netty.server.tasks;

import netty.Console;
import netty.packets.RegistrationRequestPacket;
import netty.packets.RegistrationResponsePacket;
import netty.server.handlers.ServerHandler;
import netty.server.mock.DatabaseMock;

public class RegistrationTask extends ServerTask<RegistrationRequestPacket> {

    @Override
    public void process() {
        if (DatabaseMock.registerUser(request.getLogin(), request.getPassword(), 2000)) {
            Console.println("<REGISTR TASK> SUCCESS");
            context.writeAndFlush(new RegistrationResponsePacket(true, "registration success"));
            context.pipeline().addLast(new ServerHandler());
            context.pipeline().remove("AuthHandler");
        } else {
            Console.println("<REGISTR TASK> FAILED");
            context.writeAndFlush(new RegistrationResponsePacket(false, "login already exists"));
        }
    }

    @Override
    public void onException(Throwable e) {
        Console.println("<REGISTR TASK> ERROR: " + e.getMessage());
        reject(e.getMessage());
    }
}

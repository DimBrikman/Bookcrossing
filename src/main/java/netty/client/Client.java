package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.SocketAddress;

import netty.Console;
import netty.client.futures.FutureRegistry;
import netty.client.futures.ResponseFuture;
import netty.client.handlers.Initializer;
import netty.client.listeners.DefaultRejectionListener;
import netty.client.listeners.ListenerRegistry;
import netty.client.listeners.ServerListener;
import netty.packets.*;

@SuppressWarnings("unchecked")
public class Client {
    private Bootstrap         bootstrap;
    private Channel           channel;
    private SocketAddress     server;
    private NioEventLoopGroup workerGroup;

    private FutureRegistry    futures   = FutureRegistry.instance();
    private ListenerRegistry  listeners = ListenerRegistry.instance();

    public Client(SocketAddress server) throws InterruptedException {
        this.server = server;
        bootstrap   = initBootstrap();
        addListener(new DefaultRejectionListener());
    }

    public ResponseFuture<AuthResponsePacket> login(String login, String password) throws InterruptedException {
        Console.println("<CLIENT> LOGIN");
        initConnection();
        AuthRequestPacket request = new AuthRequestPacket(login, password);
        channel.writeAndFlush(request);
        return futures.futureFor(request);
    }

    public ResponseFuture<RegistrationResponsePacket> register(String login, String password) throws InterruptedException {
        Console.println("<CLIENT> REGISTER");
        initConnection();
        RegistrationRequestPacket request = new RegistrationRequestPacket(login, password);
        channel.writeAndFlush(request);
        return futures.futureFor(request);
    }

    public void sendMessage(String message) {
        Console.println("<CLIENT> SEND MESSAGE");
        channel.writeAndFlush(new MessagePacket(message));
    }

    public <T extends Packet> void addListener(ServerListener<T> listener) {
        listeners.register(listener);
    }

    public boolean removeListener(ServerListener listener) {
        return listeners.deregister(listener);
    }

    public void close() {
        if (channel != null)
            channel.close();
        if (workerGroup != null)
            workerGroup.shutdownGracefully();
    }

    public ChannelFuture closeFuture() {
        if (channel != null)
            return channel.closeFuture();
        return null;
    }

    private Bootstrap initBootstrap() {
        workerGroup = new NioEventLoopGroup();
        return new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(workerGroup)
                .remoteAddress(server)
                .handler(new Initializer());
    }

    private void initConnection() throws InterruptedException {
        try {
            channel = bootstrap.connect().sync().channel();
            Console.println("<CLIENT> CONNECTED TO: " + channel.remoteAddress());
        } catch (InterruptedException e) {
            Console.println("<CLIENT> CONNECTION FAILED");
            throw e;
        }
    }
}

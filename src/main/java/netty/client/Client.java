package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketAddress;

import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.Console;
import netty.client.futures.FutureRegistry;
import netty.client.futures.ResponseFuture;
import netty.client.handlers.ChannelInitHandler;
import netty.client.handlers.InboundHandler;
import netty.packets.AuthRequestPacket;
import netty.packets.AuthResponsePacket;

@SuppressWarnings("unchecked")
public class Client implements Runnable {
    private Bootstrap bootstrap;
    private Channel   channel;
    private NioEventLoopGroup workerGroup;

    private SocketAddress server;

    public Client(SocketAddress server) throws InterruptedException {
        this.server = server;
        bootstrap = initBootstrap();
    }

    public ResponseFuture<AuthResponsePacket> login(String login, String password) throws InterruptedException {
        System.out.println("<CLIENT> LOGIN");
        initConnection();
        AuthRequestPacket request = new AuthRequestPacket(login, password);
        channel.writeAndFlush(request);
        return FutureRegistry.futureFor(request);
    }

    public void close() {
        channel.close();
        workerGroup.shutdownGracefully();
    }

    private Bootstrap initBootstrap() {
        workerGroup = new NioEventLoopGroup();
        return new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(workerGroup)
                .remoteAddress(server)
                .handler(new ChannelInitHandler());
    }

    private void initConnection() throws InterruptedException {
        try {
            channel = bootstrap.connect().sync().channel();
            System.out.println("<CLIENT> CONNECTED TO: " + channel.remoteAddress());
        } catch (InterruptedException e) {
            System.out.println("<CLIENT> CONNECTION FAILED");
            throw e;
        }
    }



    //************************************
    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                    .addLast(new InboundHandler());
                        }
                    });

            Channel channel = bootstrap.connect("localhost", 8090).sync().channel();
            Console.println("CLIENT CONNECTED TO: " + channel.remoteAddress());
            clientMainLoop(channel);
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            Console.println("CLIENT TERMINATED");
        }
    }

    private void clientMainLoop(Channel client) {
        try {
            Console.println(menu());
            String cmd = Console.readLine();
            while (!cmd.equalsIgnoreCase("exit")) {
                if (cmd.equals("0")) {
                    Console.println(menu());
                } else if (cmd.length() > 0) {
                    client.writeAndFlush(cmd).sync();
                }
                cmd = Console.readLine();
            }
        } catch (IOException | InterruptedException e) {
            Console.println("<MAIN LOOP> ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String menu() {
        return new StringBuilder()
                .append("\n")
                .append("(r) - registration\n")
                .append("(u) - user login\n")
                .append("(a) - admin login\n")
                .append("(m) - message\n")
                .append("(0) - menu\n")
                .toString();
    }
}

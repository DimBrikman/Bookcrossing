package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.Console;
import netty.server.handlers.Authenticator;
import netty.server.handlers.Initializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


// TODO: read about TimeoutHandler
public class Server implements Runnable {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Thread(new Server()).start();
    }

    @Override
    public void run() {
        NioEventLoopGroup bossGroup   = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new Initializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture serverFuture = bootstrap.bind("localhost", 8090).sync();
            Console.println("SERVER IS RUNNING...");
            serverFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            Console.println("SERVER TERMINATED");
        }
    }
}

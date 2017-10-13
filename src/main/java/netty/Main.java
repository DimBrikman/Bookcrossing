package netty;

import netty.client.Client;
import netty.client.RejectionException;
import netty.client.listeners.MessageListener;
import netty.client.futures.ResponseFuture;
import netty.packets.AuthResponsePacket;
import netty.packets.RejectionPacket;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) {
        testMessage();
    }

    public static void testMessage() {
        Client client = null;
        try {
            client = new Client(new InetSocketAddress("localhost", 8090));
            client.addListener(new MessageListener());
            client.login("admin", "admin").sync();
            client.sendMessage("hello");
            client.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (client != null)
                client.close();
        }
    }

    public static void testLogin() {
        Client client = null;
        try {
            client = new Client(new InetSocketAddress("localhost", 8090));
            ResponseFuture<AuthResponsePacket> future = client.login("admin", "admin");
            System.out.println("<MAIN> FUTURE ACCEPTED");
            AuthResponsePacket response = future.get(5, TimeUnit.SECONDS);
            System.out.println("<MAIN> GET RESULT");
            System.out.println("<MAIN> AUTH " + (response.isSuccess() ? "SUCCESS" : "FAILED: " + response.getMessage()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RejectionException e) {
            RejectionPacket packet = e.packet();
            System.out.println("<MAIN> REJECTED: " + packet.getMessage());
        } catch (TimeoutException e) {
            System.out.println("<MAIN> TIMEOUT EXCEEDED");
        } finally {
            if (client != null)
                client.close();
        }
    }
}

package netty;

import netty.client.Client;
import netty.client.futures.ResponseFuture;
import netty.packets.AuthResponsePacket;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client(new InetSocketAddress("localhost", 8090));
            ResponseFuture<AuthResponsePacket> future = client.login("admin", "admin");
            System.out.println("<MAIN> FUTURE ACCEPTED");
            StringBuilder builder = new StringBuilder("wait..");
            AuthResponsePacket response = future.get();
            System.out.println("<MAIN> GET RESULT");
            System.out.println(builder);
            if (response.isSuccess()) {
                System.out.println("<MAIN> AUTH SUCCESS");
            } else {
                System.out.println("<MAIN> AUTH FAILED: " + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null)
                client.close();
        }
    }
}

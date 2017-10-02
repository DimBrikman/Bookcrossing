package test;


import netty.packets.AuthRequestPacket;
import netty.packets.AuthResponsePacket;
import netty.server.tasks.AuthTask;
import netty.server.tasks.ServerTask;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Class<? extends ServerTask>> list = new ArrayList<>();

        AuthRequestPacket req1 = new AuthRequestPacket("qwe", "asd");
        AuthRequestPacket req2 = new AuthRequestPacket("rty", "asd");
        AuthResponsePacket resp1 = new AuthResponsePacket(true, req1);
        AuthResponsePacket resp2 = new AuthResponsePacket(false, req2);

        AuthTask task1 = new AuthTask(req1, null);

        list.add(task1.getClass());

        System.out.println(list.contains(req1.getClass()));

    }
}

package netty.packets;

public class AuthRequestPacket implements Packet {
    private String login;
    private String password;

    public AuthRequestPacket(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

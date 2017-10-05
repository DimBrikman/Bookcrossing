package netty.packets;

public class AuthRequestPacket extends RequestPacket {
    private final String login;
    private final String password;

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

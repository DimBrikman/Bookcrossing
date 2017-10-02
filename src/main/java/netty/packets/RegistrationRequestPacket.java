package netty.packets;

public class RegistrationRequestPacket implements Packet {
    private final String login;
    private final String password;

    public RegistrationRequestPacket(String login, String password) {
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

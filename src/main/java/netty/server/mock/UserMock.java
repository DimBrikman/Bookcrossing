package netty.server.mock;

public class UserMock {
    private static int count = 0;
    public final int id;
    public final String name;
    public final String password;

    UserMock(String name, String password) {
        this.id = ++count;
        this.name = name;
        this.password = password;
    }
}

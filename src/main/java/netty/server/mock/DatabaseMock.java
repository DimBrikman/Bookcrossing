package netty.server.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseMock {
    private static volatile List<UserMock> users = new ArrayList<>();

    static {
        users.add(new UserMock("admin", "admin"));
        users.add(new UserMock("user", "user"));
    }

    public static boolean validateUser(String login, String password, long delay) {
        synchronized (users) {
            try {
                return getUser(login, delay).password.equals(password);
            } catch (Exception ignored) {
                return false;
            }
        }
    }

    public static boolean registerUser(String login, String password, long delay) {
        synchronized (users) {
            try {
                if (getUser(login, delay) != null)
                    return false;
                users.add(new UserMock(login, password));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static UserMock getUser(int id, long delay) {
        synchronized (users) {
            for (UserMock user : users) {
                if (user.id == id)
                    return user;
            }
            return null;
        }
    }

    public static UserMock getUser(String login, long delay) {
        synchronized (users) {
            try {
                Thread.sleep(delay);
                Objects.requireNonNull(login);
                for (UserMock user : users) {
                    if (user.name.equals(login))
                        return user;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }
}

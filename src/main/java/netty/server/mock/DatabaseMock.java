package netty.server.mock;

import java.util.HashMap;

public class DatabaseMock {
    private static volatile HashMap<String, String> users = new HashMap<String, String>(){{
        put("admin", "admin");
        put("user", "user");
    }};

    public static synchronized boolean validateUser(String login, String password, long delay) {
        try {
            Thread.sleep(delay);
            return password.equals(users.get(login));
        } catch (InterruptedException ignored) {
            return false;
        }
    }

    public static synchronized boolean registerUser(String login, String password, long delay) {
        try {
            Thread.sleep(delay);
            if (users.containsKey(login))
                return false;
            users.put(login, password);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
}

package netty.server;

import netty.server.mock.UserMock;

public class ChannelCache {
    private UserMock user;

    public UserMock getUser() {
        return user;
    }

    public void setUser(UserMock user) {
        this.user = user;
    }
}

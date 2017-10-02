package netty.packets;

public class AuthPacket implements Packet {
    private AuthRequest    request;
    private StatusResponse response;

    public void setRequest(AuthRequest request) {
        this.request = request;
    }
    public AuthRequest getRequest() {
        return request;
    }

    public void setResponse(StatusResponse response) {
        this.response = response;
    }
    public StatusResponse getResponse() {
        return response;
    }

    public static class AuthRequest {
        private String login;
        private String password;

        public AuthRequest(String login, String password) {
            this.login    = login;
            this.password = password;
        }
        public String getLogin() {
            return login;
        }
        public String getPassword() {
            return password;
        }
    }
}

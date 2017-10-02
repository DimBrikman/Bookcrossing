package netty.packets;


import java.io.Serializable;

public class StatusResponse implements Serializable {
    private final boolean status;
    private final String  message;

    public StatusResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}

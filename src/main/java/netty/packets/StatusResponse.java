package netty.packets;


import java.io.Serializable;

public class StatusResponse implements Serializable {
    private boolean status;
    private String  message;

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

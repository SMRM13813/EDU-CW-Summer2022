package shared.response;

import java.util.HashMap;

public class Response {
    private ResponseStatus status;
    private HashMap<String, Object> data;
    private String errorMessage;
    // Another field for messages from server to client

    public Response() {}

    public Response(ResponseStatus status) {
        this.status = status;
        data = new HashMap<>();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void addData(String dataName, Object data) {
        this.data.put(dataName, data);
    }

    public Object getData(String dataName) {
        return this.data.get(dataName);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

package shared.request;

import java.util.HashMap;

public class RequestFromServer {
    private RequestType requestType;

    private HashMap<String, Object> data;

    public RequestFromServer() {}

    public RequestFromServer(RequestType requestType) {
        this.requestType = requestType;

        data = new HashMap<>();
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void addData(String dataName, Object data) {
        this.data.put(dataName, data);
    }

    public Object getData(String dataName) {
        return this.data.get(dataName);
    }

    @Override
    public String toString() {
        return "RequestFromServer{" +
                "requestType=" + requestType +
                ", data=" + data +
                '}';
    }
}

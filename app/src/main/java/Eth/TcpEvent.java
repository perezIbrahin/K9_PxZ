package Eth;

public class TcpEvent {

    private TcpEventType eventType;
    private Object payload;

    public TcpEvent(TcpEventType eventType, Object payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public TcpEventType getTcpEventType() {
        return this.eventType;
    }

    public Object getPayload() {
        return this.payload;
    }
}

package com.lyz.helper;

public class MessageEvent {
    public enum EventType {
        add,
        remove,
        replace
    }

    private EventType eventType;
    private String msg;
    private Object obj;
    private int intMsg;

    public MessageEvent(EventType type) {
        this.eventType = type;
    }

    public MessageEvent(EventType type, String msg) {
        this.eventType = type;
        this.msg = msg;
    }

    public MessageEvent(EventType type, int intMsg) {
        this.eventType = type;
        this.intMsg = intMsg;
    }

    public MessageEvent(EventType type, Object obj) {
        this.eventType = type;
        this.obj = obj;
    }

    public MessageEvent(EventType type, int intMsg, Object obj) {
        this.eventType = type;
        this.intMsg = intMsg;
        this.obj = obj;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getIntMsg() {
        return intMsg;
    }

    public void setIntMsg(int intMsg) {
        this.intMsg = intMsg;
    }
}

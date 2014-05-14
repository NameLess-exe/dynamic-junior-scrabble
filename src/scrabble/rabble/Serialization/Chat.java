package com.leoxiong.clientservertest.Serialization;

/**
 * Created by Leo on 5/14/2014.
 */
public class Chat extends Sendable {

    private static final Type TYPE = Type.PING;
    private String who;

    public String getWho() {
        return who;
    }

    private String data;

    public Chat(String who, String data) {
        this.who = who;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}

package com.leoxiong.clientservertest.Serialization;

/**
 * Created by Leo on 5/14/2014.
 */
public class Ping extends Sendable {

    private static final Type TYPE = Type.PING;
    private String data;

    public Ping(String data) {
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

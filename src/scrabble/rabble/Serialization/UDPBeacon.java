package com.leoxiong.clientservertest.Serialization;

import java.net.InetAddress;

/**
 * Created by Leo on 5/14/2014.
 */
public class UDPBeacon extends Sendable {
    private static final Type TYPE = Type.UDP_BEACON;
    private String ip;
    private int port;

    public UDPBeacon(InetAddress ip, int port) {
        this.ip = ip.getHostAddress();
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
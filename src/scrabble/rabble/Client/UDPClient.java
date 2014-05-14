package com.leoxiong.clientservertest.Client;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.leoxiong.clientservertest.Config.GlobalConfig;
import com.leoxiong.clientservertest.Serialization.Sendable;
import com.leoxiong.clientservertest.Serialization.UDPBeacon;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Leo on 5/13/2014.
 */
public class UDPClient {
    private static final String TAG = "UDPClient";

    private InetAddress group;
    private int port;
    private MulticastSocket multicastSocket;
    private Thread listnerThread;
    private ServerDiscovered callback;

    public UDPClient(InetAddress group, int port, final ServerDiscovered callback) {
        this.group = group;
        this.port = port;
        this.callback = callback;
        try {
            multicastSocket = new MulticastSocket(GlobalConfig.MULTICAST_PORT);
            multicastSocket.joinGroup(InetAddress.getByName(GlobalConfig.MULTICAST_GROUP));
        } catch (IOException e) {
            Log.e(TAG, String.format("Failed to create multicast socket: %1$s", e.getMessage()));
        }
        this.listnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    try {
                        multicastSocket.receive(packet);
                        Log.v(TAG, new String(buffer).trim());
                        callback.discovered(new Gson().fromJson(new String(buffer).trim(), UDPBeacon.class));
                    } catch (IOException e) {
                        Log.e(TAG, String.format("IOException occurred while receiving datagram packet: %1$s", e.getMessage()));
                    } catch (JsonSyntaxException e) {
                        Log.e(TAG, String.format("JsonSynxtaxException while deserializing: %1$s", e.getMessage()));
                    }
                }
            }
        });
    }

    public interface ServerDiscovered {
        void discovered(Sendable action);
    }

    public void listen() {
        this.listnerThread.start();

        Log.i(TAG, "UDPListener listening");
    }

    public void destroy() {
        this.listnerThread.interrupt();
        this.listnerThread = null;

        Log.i(TAG, "UDPListener destroyed");
    }
}

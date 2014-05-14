package com.leoxiong.clientservertest.Server;

import android.content.Context;
import android.util.Log;
import com.leoxiong.clientservertest.Serialization.UDPBeacon;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Leo on 5/13/2014.
 */
public class UDPServer {
    private static final String TAG = "UDPBeacon";
    private static final long BEACON_INTERVAL = 5000;

    private Context context;
    private InetAddress group;
    private int port;
    private TimerTask beaconTask;
    private Timer beaconTimer;
    private TCPServer tcpServer;
    private MulticastSocket multicastSocket;
    private DatagramPacket packet;

    public UDPServer(Context context, TCPServer tcpServer, InetAddress group, int port) {
        // Initialize member variables
        this.context = context;
        this.tcpServer = tcpServer;
        this.group = group;
        this.port = port;

        // Setup multicast socket
        try {
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
            Log.v(TAG, String.format("Multicast socket created: %1$s,:%2$s", group, port));
        } catch (IOException e) {
            Log.e(TAG, String.format("Failed to create multicast socket: %1$s", e.getMessage()));
        }

        // Setup datagram packets
        byte[] data = new UDPBeacon(tcpServer.getIp(), tcpServer.getPort()).toBytes();
        this.packet = new DatagramPacket(data, data.length, group, port);

    }

    public void resumeBeacon() {
        // Setup beacon timer
        this.beaconTimer = new Timer();
        this.beaconTask = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            multicastSocket.send(packet);
                        } catch (IOException e) {
                            Log.e(TAG, String.format("IOException occurred while sending beacon packet: %1$s", e.getMessage()));
                        }
                    }
                }).start();
            }
        };
        this.beaconTimer.schedule(beaconTask, 0, BEACON_INTERVAL);
        Log.v(TAG, String.format("Beacon resumed, scheduled for every %1$sms", BEACON_INTERVAL));
    }

    public void pauseBeacon() {
        beaconTask.cancel();
        this.beaconTimer.cancel();
        Log.v(TAG, "Beacon paused");
    }

    public void destroy() {
        pauseBeacon();

        multicastSocket.close();
        Log.v(TAG, "Beacon destroyed");
    }
}

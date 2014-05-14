package com.leoxiong.clientservertest.Server;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;
import android.util.Log;
import com.leoxiong.clientservertest.Client.ClientService;
import com.leoxiong.clientservertest.Serialization.Ping;
import com.leoxiong.clientservertest.Serialization.Sendable;
import com.leoxiong.clientservertest.Serialization.Type;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Leo on 5/13/2014.
 */
public class TCPServer {
    private static final String TAG = "TCPServer";

    private Context context;
    private InetAddress ip;
    private int port;
    private Thread listener;
    private ServerSocket serverSocket;
    private ArrayList<Client> clients;
    private LocalBroadcastManager localBroadcastManager;

    public TCPServer(Context context, int port) {
        this.context = context;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        try {
            // TODO formatIpAddress deprecated
            //noinspection deprecation
            this.ip = InetAddress.getByName(Formatter.formatIpAddress(((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress()));
            Log.i(TAG, String.format("Local IP: %1$s", this.ip.getHostAddress()));
        } catch (UnknownHostException e) {
            Log.e(TAG, "Unable to retrieve local IP address.");
        }
        this.port = port;
        this.listener = new Thread();
        this.clients = new ArrayList<Client>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.e(TAG, String.format("Failed to create server socket: %1$s", e.getMessage()));
        }
        this.listener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.v(TAG, "Waiting for client");
                        Client client = new Client(serverSocket.accept());
                        client.start();
                        clients.add(client);
                        Log.v(TAG, "New client connected");
                    } catch (IOException e) {
                        Log.e(TAG, String.format("IOException occurred while accepting new clients: %1s", e.getMessage()));
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public InetAddress getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public TCPServer setIP(InetAddress ip) {
        this.ip = ip;
        return this;
    }

    public void listen() {
        this.listener.start();
        Log.v(TAG, String.format("TCPServer listening on %1$s:%2$s", ip, port));
    }

    public void broadcast(byte[] data) {
        for (Client c : clients)
            c.send(data);
    }

    public void broadcast(String data) {
        broadcast(data.getBytes());
    }

    public void destroy() {
        // TODO
        Log.v(TAG, "TCPServer destroyed");
    }

    private class Client extends Thread {
        private Socket client;
        private ObjectInputStream is;
        private ObjectOutputStream os;

        private Client(Socket client) {
            this.client = client;
        }


        @Override
        public void run() {
            // Initialize streams
            try {
                // Create output stream, flush, then create input stackoverflow.com/a/9826835
                this.os = new ObjectOutputStream(client.getOutputStream());
                this.os.flush();
                this.is = new ObjectInputStream(client.getInputStream());
                Log.i(TAG, "Server created streams");
                os.writeObject(new Ping("ohai :D"));
                while (true) {
                    try {
                        Object obj = is.readObject();
                        Ping ping = (Ping) obj;
                        Log.i(TAG, String.format("Data received on the TCPServer: %1$s", ping.toString()));
                        Ping pong = new Ping(ping.getData());
                        os.writeObject(new Ping(String.format("PONG from server! %1$s", pong.getData())));
                        os.flush();
                        Log.i(TAG, String.format("pong-ed"));
                    } catch (ClassNotFoundException e) {
                        Log.i(TAG, String.format("ClassNotFoundException during TCPServer input: %1$s", e.getMessage()));
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, String.format("IOException occurred for clients on server: %1$s", e.getMessage()));
                e.printStackTrace();
            }
        }

        public void send(byte[] data) {
            try {
                os.write(data);
                os.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error sending data to client");
            }
        }

        public void send(Serializable data) {
            try {
                os.writeObject(data);
                os.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error sending data to client");
            }
        }
    }
}

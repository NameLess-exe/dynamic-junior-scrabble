package com.leoxiong.clientservertest.Client;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import com.leoxiong.clientservertest.Serialization.Ping;
import com.leoxiong.clientservertest.Serialization.Sendable;

import java.io.*;
import java.net.Socket;

/**
 * Created by Leo on 5/14/2014.
 */
public class TCPClient {

    private static final String TAG = "TCPClient";

    private Context context;
    private String ip;
    private int port;
    private Thread client;
    private Socket socket;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private TCPEvent callback;

    public TCPClient(Context context, String ip, int port, TCPEvent callback) {
        this.context = context;
        this.ip = ip;
        this.port = port;
        this.callback = callback;
    }

    public void connect() {
        client = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    // Create output stream, flush, then create input stackoverflow.com/a/9826835
                    os = new ObjectOutputStream(socket.getOutputStream());
                    os.flush();
                    is = new ObjectInputStream(socket.getInputStream());
                    Log.i(TAG, "Stream created");

                    while (true) {
                        try {
                            Log.i(TAG, "Waiting for data");
                            Ping pong = (Ping) is.readObject();
                            Log.i(TAG, "Data received");
                            callback.OnObjectReceived(new Ping(pong.getData()));
                        } catch (ClassNotFoundException e) {
                            Log.i(TAG, String.format("ClassNotFoundException during TCPClient input: %1$s", e.getMessage()));
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, String.format("IOException occurred: %1$s", e.getMessage()));
                    e.printStackTrace();
                }
            }
        });
        client.start();
        Log.i(TAG, "Client thread started");
    }

    public void send(Sendable data) {
        try {
            os.writeObject(data);
            Log.v(TAG, "Successfully sent data");
        } catch (IOException e) {
            Log.e(TAG, String.format("IOException occurred while sending TCP object: %1$s", e.getMessage()));
        }
    }

    public interface TCPEvent {
        void OnObjectReceived(Sendable data);
    }
}

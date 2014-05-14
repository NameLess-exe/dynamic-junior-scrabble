package com.leoxiong.clientservertest.Client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.leoxiong.clientservertest.Config.GlobalConfig;
import com.leoxiong.clientservertest.Serialization.Sendable;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Leo on 5/13/2014.
 */
public class ClientService extends Service {
    public static final String ACTION = "ACTION";
    private UDPClient.ServerDiscovered serverDiscovered = new UDPClient.ServerDiscovered() {
        @Override
        public void discovered(Sendable action) {
            Intent broadcastIntent = new Intent(ACTION);
            broadcastIntent.putExtra(ACTION, action);
            localBroadcastManager.sendBroadcast(broadcastIntent);
        }
    };
    private static final String TAG = "ClientService";
    private UDPClient udpListener;
    private TCPClient tcpClient;
    private LocalBroadcastManager localBroadcastManager;
    private IBinder binder = new LocalBinder();

    public TCPClient getTcpClient() {
        return tcpClient;
    }

    public UDPClient getUdpListener() {
        return udpListener;
    }

    @Override
    public void onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        try {
            this.udpListener = new UDPClient(InetAddress.getByName(GlobalConfig.MULTICAST_GROUP), GlobalConfig.MULTICAST_PORT, serverDiscovered);
        } catch (UnknownHostException e) {
            Log.e(TAG, String.format("Invalid multicast address: %1$s", GlobalConfig.MULTICAST_GROUP));
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.udpListener.listen();

        Log.i(TAG, "ClientService started");
        Toast.makeText(this, "ClientService started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        Log.i(TAG, "ClientService destroyed");
        Toast.makeText(this, "ClientService destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void connect(String ip, int port) {
        tcpClient = new TCPClient(this, ip, port, tcpEvent);
        tcpClient.connect();
    }

    private TCPClient.TCPEvent tcpEvent = new TCPClient.TCPEvent() {
        @Override
        public void OnObjectReceived(Sendable data) {
            Intent broadcastIntent = new Intent(ACTION);
            broadcastIntent.putExtra(ACTION, data);
            localBroadcastManager.sendBroadcast(broadcastIntent);
        }
    };

    public class LocalBinder extends Binder {
        public ClientService getInstance() {
            return ClientService.this;
        }
    }
}

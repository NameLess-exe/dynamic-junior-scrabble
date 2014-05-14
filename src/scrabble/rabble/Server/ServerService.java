package com.leoxiong.clientservertest.Server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.leoxiong.clientservertest.Config.GlobalConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Leo on 5/13/2014.
 */
public class ServerService extends Service {
    private static final String TAG = "ServerService";

    private TCPServer tcpServer;
    private UDPServer udpBeacon;
    private LocalBinder binder = new LocalBinder();

    public TCPServer getTcpServer() {
        return tcpServer;
    }

    public UDPServer getUdpBeacon() {
        return udpBeacon;
    }

    @Override
    public void onCreate() {
        tcpServer = new TCPServer(this, GlobalConfig.TCP_SERVER_PORT);
        try {
            udpBeacon = new UDPServer(this, tcpServer, InetAddress.getByName(GlobalConfig.MULTICAST_GROUP), GlobalConfig.MULTICAST_PORT);
        } catch (UnknownHostException e) {
            Log.e(TAG, String.format("Invalid multicast address: %1$s", GlobalConfig.MULTICAST_GROUP));
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tcpServer.listen();
        udpBeacon.resumeBeacon();

        Toast.makeText(this, "ServerService started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        udpBeacon.destroy();
        tcpServer.destroy();

        Toast.makeText(this, "ServerService destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public ServerService getInstance() {
            return ServerService.this;
        }
    }
}

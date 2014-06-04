package scrabble.rabble.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Created by Leo on 5/29/2014.
 */
public class ClientService extends Service {
    private static final String TAG = "ClientService";

    private final LocalBinder binder = new LocalBinder();

    private Client client = new Client();
    private EventListener eventListener;
    private Listener listener;

    public void connect(final String ip, final int tcpPort, final int udpPort) {
        client.start();
        Network.register(client);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    client.connect(Network.TCP_TIMEOUT, ip, tcpPort, udpPort);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public void send(Object data) {
        client.sendTCP(data);
    }

    public void registerListener(final EventListener eventListener) {
        if (this.listener != null)
            client.removeListener(this.listener);
        this.eventListener = eventListener;
        this.listener = new Listener() {
            @Override
            public void connected(Connection connection) {
                eventListener.onConnect(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                eventListener.onDisconnect(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                eventListener.onDataReceived(connection, object);
            }
        };
        client.addListener(this.listener);
    }

    public void disconnect() {
        client.stop();
    }

    @Override
    public void onDestroy() {
        try {
            client.dispose();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        super.onDestroy();
    }

    public void discover() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<InetAddress> inetAddress = client.discoverHosts(Network.UDP_PORT, Network.DISCOVERY_TIMEOUT);
                if (inetAddress.size() > 0)
                    eventListener.onDiscoverHost(inetAddress);
                return null;
            }
        }.execute();
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public ClientService getInstance() {
            return ClientService.this;
        }
    }
}

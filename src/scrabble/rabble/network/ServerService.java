package scrabble.rabble.network;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * Created by Leo on 5/29/2014.
 */
public class ServerService extends Service {
    private static final String TAG = "ServerService";

    private final LocalBinder binder = new LocalBinder();

    private Server server = new Server();
    private Listener listener;

    @Override
    public void onCreate() {
        super.onCreate();

        Network.register(server);
    }

    public void bind() throws IOException {
        server.bind(Network.TCP_PORT, Network.UDP_PORT);
        server.start();
    }

    @Override
    public void onDestroy() {
        try {
            server.dispose();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        super.onDestroy();
    }

    public void registerListener(Listener listener) {
        if (this.listener != null)
            server.removeListener(this.listener);
        this.listener = listener;
        server.addListener(this.listener);
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void stop(){
        server.stop();
        server.close();
    }

    public class LocalBinder extends Binder {
        public ServerService getInstance() {
            return ServerService.this;
        }
    }
}

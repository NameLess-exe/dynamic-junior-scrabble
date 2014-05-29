package scrabble.rabble.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

import org.apache.http.conn.util.InetAddressUtils;

import scrabble.rabble.model.Player;
import android.os.Message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by Leo on 5/29/2014.
 */
public class Network {

    // These are the ports which the server and client are communicating through. Make sure these are not blocked by
    // firewalls. Port numbers must be > 1024;
    public static final int UDP_PORT = 7070;
    public static final int TCP_PORT = 6060;

    // Depending on the network quality, you may wish to increase the timeouts.
    public static final int TCP_TIMEOUT = 2000;
    public static final int DISCOVERY_TIMEOUT = 2000;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        // Add new objects to be sent over the network below
        kryo.register(Message.class);
        kryo.register(Player.class);
    }

    public static String getLocalIPv4() throws SocketException {
        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                if (!inetAddress.isLoopbackAddress()) {
                    if (InetAddressUtils.isIPv4Address(inetAddress.getHostAddress()))
                        return inetAddress.getHostAddress();
                }
            }
        }
        return null;
    }
}

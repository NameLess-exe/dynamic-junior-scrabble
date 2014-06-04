package scrabble.rabble.network;

import java.net.InetAddress;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

/**
 * Created by Leo on 5/30/2014.
 */
public interface EventListener {

    void onConnect(Connection connection);

    void onDisconnect(Connection connection);

    void onDataReceived(Connection connection, Object data);

    void onDiscoverHost(List<InetAddress> inetAddresses);
}

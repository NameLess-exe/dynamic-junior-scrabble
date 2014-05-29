package com.esotericsoftware.kryonet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost;

public interface ServerDiscoveryHandler {

	/**
	 * This implementation of {@link com.esotericsoftware.kryonet.ServerDiscoveryHandler} is responsible for
	 * providing the {@link com.esotericsoftware.kryonet.Server} with it's default behavior.
	 */
	public static final ServerDiscoveryHandler DEFAULT = new ServerDiscoveryHandler() {
		private ByteBuffer emptyBuffer = ByteBuffer.allocate(0);

		@Override
		public boolean onDiscoverHost(UdpConnection udp,
				InetSocketAddress fromAddress, Serialization serialization)
				throws IOException {
			udp.datagramChannel.send(emptyBuffer, fromAddress);
			return true;
		}
	};

	/**
	 * Called when the {@link com.esotericsoftware.kryonet.Server} receives a {@link com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost} packet.
	 * 
	 * @param udp
	 *            the {@link com.esotericsoftware.kryonet.UdpConnection}
	 * @param fromAddress
	 *            {@link java.net.InetSocketAddress} the {@link com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost} came from
	 * @param serialization
	 *            the {@link com.esotericsoftware.kryonet.Server}'s {@link com.esotericsoftware.kryonet.Serialization} instance
	 * @return true if a response was sent to {@code fromAddress}, false
	 *         otherwise
	 * @throws java.io.IOException
	 *             from the use of
	 *             {@link java.nio.channels.DatagramChannel#send(java.nio.ByteBuffer, java.net.SocketAddress)}
	 */
	public boolean onDiscoverHost(UdpConnection udp,
                                  InetSocketAddress fromAddress, Serialization serialization)
			throws IOException;

}

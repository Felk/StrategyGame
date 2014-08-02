package de.felk.StrategyGame.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import de.felk.NodeFile.NodeFile;
import de.felk.NodeFile.NoSuchNodeException;
import de.felk.StrategyGame.packets.Packet;
import de.felk.StrategyGame.packets.PacketEnum;

public class ReceiverThread extends Thread {

	private Socket socket;
	private ArrayList<Packet> packets = new ArrayList<Packet>();

	public ReceiverThread(Socket socket) {
		this.socket = socket;
		this.start();
	}

	@Override
	public void run() {

		try {
			DataInputStream stream = new DataInputStream(socket.getInputStream());
			int packetId = 0;
			while (!isInterrupted() && packetId >= 0) {

				packetId = stream.readInt();

				PacketEnum packetEnum = PacketEnum.byId(packetId);
				if (packetEnum == null) {
					System.out.println("received invalid packet id: " + packetId + ", ignored.");
				} else {
					int length = stream.readInt();

					ByteBuffer buffer = ByteBuffer.allocate(length);
					for (int i = 0; i < length; i++) {
						buffer.put(stream.readByte());
					}
					buffer.flip();

					try {
						Packet packet;
						packet = packetEnum.load(new NodeFile(buffer, length));
						addPacket(packet);
						packets.add(packet);
					} catch (NoSuchNodeException e) {
						System.err.println("Could not load packet from node data");
						e.printStackTrace();
					}

				}

			}
		} catch (SocketException e) {
			// System.err.println("SocketException in ReceiverThread");
			// e.printStackTrace();
		} catch (IOException e) {
			// System.err.println("ReceiverThread crashed (maybe due to connection abort)");
			// e.printStackTrace();
		}

		stopThread();

	}

	private void addPacket(Packet packet) {
		synchronized (packets) {
			packets.add(packet);
			System.out.println("received packet " + packet.toString());
		}
	}

	public ArrayList<Packet> getPackets() {
		return packets;
	}

	public void stopThread() {
		interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Could not close socket in ReceiverThread.");
			e.printStackTrace();
		}
	}

}

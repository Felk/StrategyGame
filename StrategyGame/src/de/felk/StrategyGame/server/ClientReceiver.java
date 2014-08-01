package de.felk.StrategyGame.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import de.felk.NodeFile.FileNode;
import de.felk.StrategyGame.network.PacketEnum;

public class ClientReceiver extends Thread {

	private Socket socket;

	public ClientReceiver(Socket socket) {
		this.socket = socket;
		this.start();
	}

	@Override
	public void run() {

		try {
			DataInputStream stream = new DataInputStream(socket.getInputStream());
			int packetId = 0;
			while (!isInterrupted() && packetId >= 0) {

				packetId = stream.read();

				PacketEnum packet = PacketEnum.byId(packetId);
				if (packet == null) {
					System.err.println("received invalid packet id: " + packetId);
				} else {
					System.out.println("Received packet, ID " + packetId);

					// TODO figure out how to properly read from the network stream.
					
					//packet.load(new FileNode(stream.));
					
				}

			}
		} catch (IOException e) {
			System.out.println("ClientReceiver crashed horribly");
			e.printStackTrace();
			interrupt();
		}

		System.out.println("ClientReceiver shutdown");

	}

}

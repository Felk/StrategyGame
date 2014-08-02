package de.felk.StrategyGame.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import de.felk.NodeFile.NodeFile;
import de.felk.StrategyGame.packets.Packet;
import de.felk.StrategyGame.packets.PacketEnum;

public class SenderThread extends Thread {

	private Socket socket;
	private ArrayList<Packet> packets = new ArrayList<Packet>();

	public SenderThread(Socket socket) {
		this.socket = socket;
		this.start();
	}

	@Override
	public void run() {

		DataOutputStream stream;
		try {
			stream = new DataOutputStream(socket.getOutputStream());
			while (!isInterrupted()) {
				synchronized (packets) {

					if (packets.isEmpty()) {
						packets.wait();
					}
					for (Packet packet : packets) {
						try {
							stream.writeInt(PacketEnum.byClass(packet.getClass()).getID());
							NodeFile node = packet.toNode();
							stream.writeInt(node.length());
							stream.write(node.toBuffer().array());
						} catch (IOException e) {
							System.err.println("Could not handle packet " + packet.toString());
							// e.printStackTrace();
						}

					}
					packets.clear();
				}
			}
		} catch (InterruptedException e) {
			// System.err.println("InterruptedException in SenderThread");
			// e.printStackTrace();
		} catch (IOException e) {
			System.err.println("DataOutputStream is unavailable in SenderThread.");
			e.printStackTrace();

		}

		stopThread();

	}

	public void send(Packet packet) {
		synchronized (packets) {
			packets.add(packet);
			packets.notify();
		}
		System.out.println("Sent packet " + packet.toString());
	}

	public void stopThread() {
		interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Could not close SenderThread socket.");
			e.printStackTrace();
		}
	}

}

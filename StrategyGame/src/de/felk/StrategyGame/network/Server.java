package de.felk.StrategyGame.network;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import de.felk.StrategyGame.packets.Packet;
import de.felk.StrategyGame.packets.PacketMap;
import de.felk.StrategyGame.world.World;

public class Server extends Thread {

	private ServerSocket socket;
	private World world;

	private ArrayList<Connection> clients = new ArrayList<Connection>();

	public Server(int port, World world) throws BindException {
		try {
			socket = new ServerSocket(port);
		} catch (BindException e) {
			throw e;
		} catch (IOException e) {
			System.err.println("The server crashed brutally");
			e.printStackTrace();
		}
		this.world = world;
		this.start();
	}

	@Override
	public void run() {

		try {
			while (!isInterrupted()) {
				addConnection(socket.accept());
			}
		} catch (SocketException e) {
			// System.err.println("SocketException in Server");
			// e.printStackTrace();
		} catch (IOException e) {
			System.err.println("The server crashed brutally");
			e.printStackTrace();
		}

		stopThread();
		System.out.println("SHUTDOWN: Server - " + socket);

	}

	private void addConnection(Socket newSocket) {
		Connection c = new Connection(newSocket);
		clients.add(c);
		PacketMap packet = new PacketMap(world);
		c.send(packet);
	}

	public void broadcast(Packet packet) {
		for (Connection c : clients) {
			c.send(packet);
		}
	}

	public void stopThread() {
		for (Connection c : clients) {
			c.close();
		}
		interrupt();
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Could not close Server-Socket");
				e.printStackTrace();
			}
		}
	}

}

package de.felk.StrategyGame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

	private int port;

	private ArrayList<Client> clients = new ArrayList<Client>();

	public Server(int port) {
		this.port = port;
		this.start();
	}

	@Override
	public void run() {

		ServerSocket socket = null;

		try {
			socket = new ServerSocket(port);

			Socket newSocket = null;
			while (!isInterrupted()) {

				newSocket = socket.accept();
				Client c = new Client(newSocket);
				clients.add(c);
			}

			// cleaning all connections up
			for (Client c : clients) {
				c.interrupt();
			}
			socket.close();
		} catch (IOException e) {
			System.out.println("Le server crashed brutally");
			e.printStackTrace();
			interrupt();
		}
		
		System.out.println("Server shutdown");

	}
	
	public void broadcastTest(int i) {
		for (Client c : clients) {
			c.sendTestNumber(i);
		}
		// TODO broadcast something useful I guess
	}

}

package de.felk.StrategyGame.server;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

	private Socket socket;
	private ClientReceiver rec;
	private ClientSender send;

	public Client(Socket socket) {
		this.socket = socket;
		this.start();
		this.rec = new ClientReceiver(socket);
		this.send = new ClientSender(socket);
	}

	@Override
	public void run() {

		while (!isInterrupted()) {

			
			
		}

		rec.interrupt();
		send.interrupt();
		
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Could not close client socket");
			e.printStackTrace();
		}
		
		System.out.println("Client shutdown");
		
	}
	
	public void sendTestNumber(int i) {
		send.send(i);
	}

}

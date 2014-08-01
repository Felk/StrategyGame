package de.felk.StrategyGame.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSender extends Thread {

	private Socket socket;

	public ClientSender(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		while (!isInterrupted()) {

			// TODO Send?

		}

		System.out.println("ClientReceiver shutdown");

	}

	public void send(int i) {

		try {
			DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
			stream.write(i);
			stream.flush();
		} catch (IOException e) {
			System.out.println("could not send");
			e.printStackTrace();
		}

	}

}

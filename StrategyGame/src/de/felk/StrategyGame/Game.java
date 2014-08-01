package de.felk.StrategyGame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.lwjgl.opengl.Display;

import de.felk.StrategyGame.server.Client;
import de.felk.StrategyGame.server.Server;
import de.felk.StrategyGame.world.World;
import de.felk.StrategyGame.world.WorldGenerator;
import static org.lwjgl.opengl.GL11.*;

public class Game {

	private World world;
	private int port = 34543;
	private boolean stopRequested = false;
	private Server server;
	private Client client;

	public Game() {

		server = new Server(port);
		initLocalConnection();

		RenderEngine.init(800, 600);

		world = WorldGenerator.getNew(50, MathHelper.getRandomSeed());

		long lastTime = System.nanoTime();
		while (!isStopRequested()) {
			mainloop((System.nanoTime() - lastTime) / 1000000000d);
			lastTime = System.nanoTime();
		}

		// cleanup
		server.interrupt();
		client.interrupt();

	}

	public void mainloop(double time) {
		if (world == null) {
			System.out.println("no world");
		} else {
			world.update(time);
		}

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		if (world != null) {
			world.render();
		}
		Display.update();

		if (Display.isCloseRequested()) {
			stop();
		}
	}

	public void initLocalConnection() {

		Socket socket = new Socket();
		try {
			System.out.println("now connecting to ::1 ...");
			socket.connect(new InetSocketAddress("::1", port));
			System.out.println("did connect to ::1 !");
			client = new Client(socket);
		} catch (IOException e) {
			System.out.println("connection to ::1 failed");
			e.printStackTrace();
		}
	}

	public void stop() {
		stopRequested = true;
	}

	public boolean isStopRequested() {
		return stopRequested;
	}

}

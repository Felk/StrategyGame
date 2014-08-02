package de.felk.StrategyGame;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import de.felk.JOpenAL.SoundManager;
import de.felk.StrategyGame.network.Connection;
import de.felk.StrategyGame.network.Server;
import de.felk.StrategyGame.packets.Packet;
import de.felk.StrategyGame.packets.PacketChannel;
import de.felk.StrategyGame.packets.PacketConnectionInfo;
import de.felk.StrategyGame.packets.PacketMap;
import de.felk.StrategyGame.world.World;
import de.felk.StrategyGame.world.WorldGenerator;

public class Game {

	private World world;
	private int port = 34543;
	private boolean stopRequested = false;
	private Server server;
	private Connection testConnection;

	private final int FRAMERATE = 60;

	public Game() {

		start();

		World world = WorldGenerator.getNew(200, MathHelper.getRandomSeed());

		try {
			server = new Server(port, world);
		} catch (BindException e) {
			System.err.println("Port is already in use: " + port);
			e.printStackTrace();
			stop();
			System.exit(-1); // TODO this looks bad
		}
		connectToLocalServer();

		long lastTime = System.nanoTime();
		while (!isStopRequested()) {
			double diftime = (System.nanoTime() - lastTime) / 1000000000d;
			lastTime = System.nanoTime();
			mainloop(diftime);
		}

	}

	public void start() {
		RenderEngine.init(1280, 720);
		SoundManager.createAL();
	}

	public void mainloop(double time) {
		if (world == null) {
			ArrayList<Packet> packets = testConnection.get(PacketChannel.MAP);
			if (!packets.isEmpty()) {
				System.out.println("Got MAP Packet! Loading world...");
				PacketMap packetMap = (PacketMap) packets.get(0);
				world = new World(packetMap.worldNode);
				System.out.println("World loaded");
			}
		} else {
			world.update(time);
		}

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		if (world != null) {
			world.render();
		}
		Display.sync(FRAMERATE);
		Display.update();

		if (Display.isCloseRequested()) {
			stop();
		}
	}

	public void connectToLocalServer() {

		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("::1", port));
			testConnection = new Connection(socket);
			testConnection.send(new PacketConnectionInfo(1234));
		} catch (IOException e) {
			System.out.println("connection to ::1 failed");
			e.printStackTrace();
		}
	}

	public void stop() {
		stopRequested = true;
		SoundManager.shutdown();
		Display.destroy();
		if (server != null) {
			server.stopThread();
		}
		if (testConnection != null) {
			testConnection.close();
		}
	}

	public boolean isStopRequested() {
		return stopRequested;
	}

}

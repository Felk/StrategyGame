package de.felk.StrategyGame.world;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.input.Keyboard;

import de.felk.NodeFile.NodeFile;
import de.felk.StrategyGame.RenderHelper;
import de.felk.StrategyGame.TextureBank;
import de.felk.StrategyGame.Vector;

public class World {

	private WorldNode[][] nodes;
	private Vector camPos = new Vector(10, 20, 40);

	public World(WorldNode[][] nodes) {
		this.nodes = nodes;
	}

	public World(NodeFile node) {
		loadNode(node);
	}

	public WorldNode[][] getNodes() {
		return nodes;
	}

	public void update(double time) {

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			camPos.addX((float) (100000 * time));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camPos.addX((float) (-100000 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camPos.addZ((float) (-100000 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camPos.addZ((float) (100000 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			camPos.addY((float) (100000 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			camPos.addY((float) (-100000 * time));
		}

	}

	public void render() {
		glRotatef(30, 1, 0, 0);
		glTranslatef(-camPos.getX(), -camPos.getY(), -camPos.getZ());
		TextureBank.instance.bindTexture("texture.png");
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		glColor4f(1, 1, 1, 1);
		RenderHelper.enableAlphaMask();
		for (int x = 0; x < nodes.length; x++) {
			for (int z = 0; z < nodes[x].length; z++) {
				float h = nodes[x][z].getHeight();
				float hx = 0, hz = 0, hxz = 0;
				if (x + 1 < nodes.length) {
					hx = nodes[x + 1][z].getHeight();
				}
				if (z + 1 < nodes[x].length) {
					hz = nodes[x][z + 1].getHeight();
				}
				if (x + 1 < nodes.length && z + 1 < nodes[x].length) {
					hxz = nodes[x + 1][z + 1].getHeight();
				}

				WorldMaterial mat = nodes[x][z].getMat();

				glTexCoord2f(mat.getId() / 16f, 0);
				glVertex3f(x, h, z);
				glTexCoord2f((mat.getId() + 1) / 16f, 0);
				glVertex3f(x + 1, hx, z);
				glTexCoord2f((mat.getId() + 1) / 16f, 1 / 16f);
				glVertex3f(x + 1, hxz, z + 1);
				glTexCoord2f(mat.getId() / 16f, 1 / 16f);
				glVertex3f(x, hz, z + 1);
			}
		}
		glDisable(GL_TEXTURE_2D);
		glEnd();
	}

	public NodeFile toNode() {
		NodeFile node = new NodeFile();
		int size = nodes.length;
		node.add('s', size);

		int length = size * nodes[0].length;
		int[] mats = new int[length];
		float[] heights = new float[length];

		for (int x = 0; x < nodes.length; x++) {
			for (int z = 0; z < nodes[x].length; z++) {
				mats[x * size + z] = nodes[x][z].getMat().getId();
				heights[x * size + z] = nodes[x][z].getHeight();
			}
		}

		node.add('m', mats);
		node.add('h', heights);

		return node;
	}

	public void loadNode(NodeFile node) {
		int size = node.getInt('s');
		int[] mats = node.getIntArray('m');
		float[] heights = node.getFloatArray('h');

		WorldNode[][] nodes = new WorldNode[size][(mats.length / size)];
		for (int x = 0; x < nodes.length; x++) {
			for (int z = 0; z < nodes[x].length; z++) {
				nodes[x][z] = new WorldNode(WorldMaterial.byID(mats[x * size + z]), heights[x * size + z]);
			}
		}
		this.nodes = nodes;
	}

}

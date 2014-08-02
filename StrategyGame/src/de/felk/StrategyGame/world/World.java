package de.felk.StrategyGame.world;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import de.felk.NodeFile.NodeFile;
import de.felk.StrategyGame.RenderHelper;
import de.felk.StrategyGame.TextureBank;
import de.felk.StrategyGame.Vector;
import de.felk.StrategyGame.render.FontAlignment;
import de.felk.StrategyGame.render.FontRenderer;

public class World {

	private WorldNode[][] nodes;
	private Vector camPos = new Vector(10, 50, 40);

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
			camPos.addX((float) (40 * time));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camPos.addX((float) (-40 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camPos.addZ((float) (-40 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camPos.addZ((float) (40 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			camPos.addY((float) (20 * time));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			camPos.addY((float) (-20 * time));
		}

	}

	public void render() {
		glLoadIdentity();
		// GLU.gluPerspective(45, Display.getWidth() / Display.getHeight(), 1, 1000);
		glRotatef(35, 1, 0, 0);
		//glRotatef(45, 0, 1, 0);
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
		glEnd();

		RenderHelper.enableAlphaMask();
		glDisable(GL_DEPTH_TEST);

		// switch to orthogonal
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glColor4f(1f, 0.5f, 0.5f, 1f);

		TextureBank.instance.bindTexture("font.png");

		FontRenderer.renderString("Teststring! Lorem ipsum dolor sit amet, consecetur ...", 10, 10, 32, 0.05f, FontAlignment.LEFT);

		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);

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

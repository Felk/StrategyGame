package de.felk.StrategyGame.world;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import de.felk.StrategyGame.RenderHelper;
import de.felk.StrategyGame.TextureBank;
import de.felk.StrategyGame.Vector;

public class World {

	private WorldNode[][] nodes;
	private Vector camPos = new Vector(10, 20, 40);

	public World(WorldNode[][] nodes) {
		this.nodes = nodes;
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

				glTexCoord2f(mat.getTexId() / 16f, 0);
				glVertex3f(x, h, z);
				glTexCoord2f((mat.getTexId() + 1) / 16f, 0);
				glVertex3f(x + 1, hx, z);
				glTexCoord2f((mat.getTexId() + 1) / 16f, 1 / 16f);
				glVertex3f(x + 1, hxz, z + 1);
				glTexCoord2f(mat.getTexId() / 16f, 1 / 16f);
				glVertex3f(x, hz, z + 1);
			}
		}
		glDisable(GL_TEXTURE_2D);
		glEnd();
	}
}

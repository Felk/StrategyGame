package de.felk.StrategyGame;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

public class RenderEngine {

	private static DisplayMode displayMode;

	public static void init(int width, int height) {
		try {
			DisplayMode[] displayModes = Display.getAvailableDisplayModes();

			for (DisplayMode mode : displayModes) {
				// boolean flag1 = mode.getFrequency() == Display.getDisplayMode().getFrequency();
				boolean flag2 = mode.getBitsPerPixel() == Display.getDisplayMode().getBitsPerPixel();
				boolean flag3 = mode.getHeight() == height;
				boolean flag4 = mode.getWidth() == width;

				if (flag2 && flag3 && flag4) {
					Display.setDisplayMode(mode);
					displayMode = mode;
				}

			}

			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		glViewport(0, 0, displayMode.getWidth(), displayMode.getHeight());
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Reset The Projection Matrix
		// glFrustum(0, 1, 0, 1, 0, 1);
		// glOrtho(0.0D, displayMode.getWidth(), 0, displayMode.getHeight(), 1f, -1f);
		GLU.gluPerspective(45, displayMode.getWidth() / displayMode.getHeight(), 1, 10000);
		
		glMatrixMode(GL_MODELVIEW);
		// glLoadIdentity();
		
		Display.setVSyncEnabled(true);

	}
}

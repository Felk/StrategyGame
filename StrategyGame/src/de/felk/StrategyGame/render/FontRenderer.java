package de.felk.StrategyGame.render;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.felk.StrategyGame.TextureBank;
import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {

	private static File fontWidths = new File("res/font.widths");
	private static int[] widths = new int[16 * 16];

	static {

		byte[] bytes = new byte[(int) fontWidths.length()];
		try {
			FileInputStream stream = new FileInputStream(fontWidths);
			stream.read(bytes);
			stream.close();
		} catch (IOException e) {
			System.err.println("Could not load font widths file :(");
			e.printStackTrace();
		}

		String s = new String(bytes).replaceAll("\\s+", "");
		String[] splitted = s.split(",");
		for (int i = 0; i < widths.length; i++) {
			if (i >= splitted.length) {
				System.err.println("Widths file does not contain enough widths! " + splitted.length);
			} else {
				// System.out.println(">" + splitted[i] + "<");
				widths[i] = Integer.valueOf(splitted[i]);
			}
		}

	}

	public static void renderString(String string, float posX, float posY, float size, float spacing, FontAlignment align) {

		TextureBank.instance.bindTexture("font.png");

		int width = 0;
		for (char c : string.toCharArray()) {
			width += widths[c];
		}

		width *= size / 64f;
		width *= spacing + 1;

		if (align == FontAlignment.CENTER) {
			posX -= width / 2;
		} else if (align == FontAlignment.RIGHT) {
			posX -= width;
		}

		// System.out.println("posX: " + posX);

		int progressX = 0;

		// I AM STUPID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! :
		// glEnable(GL_QUADS); is WRONG!
		glBegin(GL_QUADS);
		for (char c : string.toCharArray()) {
			renderChar(c, posX + progressX, posY, size);
			progressX += widths[c] * size / 64f + spacing * size;
			//System.out.println(progressX);
		}
		glEnd();

	}

	private static void renderChar(char c, float posX, float posY, float size) {

		// System.out.println("Rendering char " + c + " at " + posX + " " + posY + " size " + size);

		float texCoordX = (c % 16) / 16f;
		float texCoordY = (c / 16) / 16f;
		float step = 1 / 16f;
		// System.out.println("texCoords: " + (texCoordX * 16) + " " + (texCoordY * 16) + " step " + step);

		glTexCoord2f(texCoordX, texCoordY);
		glVertex3f(posX, posY, -1);

		glTexCoord2f(texCoordX + step, texCoordY);
		glVertex3f(posX + size, posY, -1);

		glTexCoord2f(texCoordX + step, texCoordY + step);
		glVertex3f(posX + size, posY + size, -1);

		glTexCoord2f(texCoordX, texCoordY + step);
		glVertex3f(posX, posY + size, -1);

	}
}

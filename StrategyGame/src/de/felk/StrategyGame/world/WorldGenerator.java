package de.felk.StrategyGame.world;

public class WorldGenerator {

	public static World getNew(int size, long seed) {

		int iSeed = (int) (seed ^ (seed << 13) ^ (seed << 29));

		SimplexNoise noise = new SimplexNoise(iSeed);

		WorldNode[][] nodes = new WorldNode[size][size];

		for (int x = 0; x < nodes.length; x++) {
			for (int y = 0; y < nodes[x].length; y++) {
				WorldMaterial mat = WorldMaterial.GRASS;
				float h = (float) (0.5f * (noise.noise(x / 10f, y / 10f) + 1));
				// h is now between 0 and 1

				if (h < 0.4) {
					mat = WorldMaterial.SEA;
				} else if (h < 0.9) {
					mat = WorldMaterial.GRASS;
				} else {
					mat = WorldMaterial.ROCK;
				}
				nodes[x][y] = new WorldNode(mat, h * 3);
			}
		}

		World w = new World(nodes);

		return w;

	}
}

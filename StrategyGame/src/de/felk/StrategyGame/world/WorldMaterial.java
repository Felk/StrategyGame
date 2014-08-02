package de.felk.StrategyGame.world;

public enum WorldMaterial {
	GRASS(0), SEA(1), ROCK(2), RIVER(3), COAST(4), WOOD(5), ROAD(6);

	private int id;

	WorldMaterial(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static WorldMaterial byID(int id) {
		for (WorldMaterial mat : values()) {
			if (mat.getId() == id) {
				return mat;
			}
		}
		return null;
	}
}

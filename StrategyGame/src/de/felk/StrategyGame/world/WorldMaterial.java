package de.felk.StrategyGame.world;

public enum WorldMaterial {
	GRASS(0), SEA(1), ROCK(2), RIVER(3), COAST(4), WOOD(5), ROAD(6);

	private int textureID;

	WorldMaterial(int textureID) {
		this.textureID = textureID;
	}
	
	public int getTexId() {
		return textureID;
	}

}

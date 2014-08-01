package de.felk.StrategyGame.world;

public class WorldNode {

	private WorldMaterial mat;
	private float height;
	
	public WorldNode(WorldMaterial mat, float height) {
		setMat(mat);
		setHeight(height);
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float h) {
		this.height = h;
	}
	
	public WorldMaterial getMat() {
		return mat;
	}
	
	public void setMat(WorldMaterial mat) {
		this.mat = mat;
	}
	
}

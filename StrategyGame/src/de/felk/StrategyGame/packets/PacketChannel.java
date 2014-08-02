package de.felk.StrategyGame.packets;

public enum PacketChannel {
	CONN_INFO(0), MAP(1), GAME(2);

	private int id;

	PacketChannel(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}
}

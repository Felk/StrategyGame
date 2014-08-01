package de.felk.StrategyGame.network;

import de.felk.NodeFile.FileNode;

public enum PacketEnum {
	ConnectionInfo(0, PacketConnectionInfo.class), Map(1, PacketMap.class);

	private Class<? extends Packet> clazz;
	private int id;

	PacketEnum(int id, Class<? extends Packet> clazz) {
		this.id = id;
		this.clazz = clazz;
	}

	public Packet load(FileNode node) throws InstantiationException, IllegalAccessException {
		Packet p = clazz.newInstance();
		p.loadNode(node);
		return p;
	}

	public static PacketEnum byId(int id) {
		for (PacketEnum p : values()) {
			if (p.id == id) {
				return p;
			}
		}
		return null;
	}

}

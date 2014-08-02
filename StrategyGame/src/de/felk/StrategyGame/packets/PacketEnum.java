package de.felk.StrategyGame.packets;

import de.felk.NodeFile.NodeFile;

public enum PacketEnum {
	CONN_INFO(1, PacketChannel.CONN_INFO, PacketConnectionInfo.class),
	MAP(2, PacketChannel.MAP, PacketMap.class);

	private Class<? extends Packet> clazz;
	private int id;
	private PacketChannel channel;

	PacketEnum(int id, PacketChannel channel, Class<? extends Packet> clazz) {
		this.id = id;
		this.channel = channel;
		this.clazz = clazz;
	}

	public Packet load(NodeFile node) {
		Packet p = null;
		try {
			p = clazz.newInstance();
			p.loadNode(node);
		} catch (InstantiationException | IllegalAccessException e) {
			System.err.println("Error calling FileNode constructor by packet class. Make sure every Packet has a default (empty) constructor!");
			e.printStackTrace();
		}
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

	public static PacketEnum byClass(Class<? extends Packet> clazz) {
		for (PacketEnum packet : values()) {
			if (packet.clazz.equals(clazz)) {
				return packet;
			}
		}
		return null;
	}

	public int getID() {
		return id;
	}

	public PacketChannel getChannel() {
		return channel;
	}

}

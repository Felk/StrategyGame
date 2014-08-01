package de.felk.StrategyGame.network;

import de.felk.NodeFile.FileNode;

public class PacketConnectionInfo extends Packet {

	private int version;
	
	public PacketConnectionInfo(int version) {
		this.version = version;
	}
	
	@Override
	public void loadNode(FileNode node) {
		version = node.getInt('v');
	}

	@Override
	public FileNode saveNode() {
		FileNode node = new FileNode();
		node.add('v', version);
		return node;
	}

}

package de.felk.StrategyGame.packets;

import de.felk.NodeFile.NodeFile;
import de.felk.StrategyGame.world.World;

public class PacketMap extends Packet {

	public NodeFile worldNode;

	public PacketMap() {
	}

	public PacketMap(World world) {
		this.worldNode = world.toNode();
	}

	@Override
	public void loadNode(NodeFile node) {
		this.worldNode = node.getNode('w');
	}

	@Override
	public NodeFile toNode() {
		NodeFile node = new NodeFile();
		node.add('w', worldNode);
		return node;
	}

}

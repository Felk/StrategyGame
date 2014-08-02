package de.felk.StrategyGame.packets;

import de.felk.NodeFile.NodeFile;

public abstract class Packet {

	public abstract NodeFile toNode();

	public abstract void loadNode(NodeFile node);

}

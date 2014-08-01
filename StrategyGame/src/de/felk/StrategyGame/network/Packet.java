package de.felk.StrategyGame.network;

import de.felk.NodeFile.FileNode;

public abstract class Packet {

	public abstract void loadNode(FileNode node);
	public abstract FileNode saveNode();
	
	
}

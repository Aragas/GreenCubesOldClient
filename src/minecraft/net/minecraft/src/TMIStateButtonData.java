package net.minecraft.src;

class TMIStateButtonData {
	public static final String COPYRIGHT = "All of TooManyItems except for thesmall portion excerpted from the original Minecraft game is copyright 2011Marglyph. TooManyItems is free for personal use only. Do not redistributeTooManyItems, including in mod packs, and do not use TooManyItems' sourcecode or graphics in your own mods.";
	public int state;
	public int action;
	public static final int STATE = 0;
	public static final int CLEAR = 1;

	public TMIStateButtonData(int i, int j) {
		state = i;
		action = j;
	}
}

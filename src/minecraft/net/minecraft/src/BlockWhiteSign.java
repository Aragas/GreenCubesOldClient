package net.minecraft.src;

import java.util.Random;

public class BlockWhiteSign extends BlockSign {

	protected BlockWhiteSign(int i, Class class1, boolean flag) {
		super(i, class1, flag);
		blockIndexInTexture = GreenTextures.whiteStoneTexture;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return Item.whiteSign.shiftedIndex;
	}
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, Material, Item

public class BlockBookshelf extends Block {

	public BlockBookshelf(int i, int j) {
		super(i, j, Material.wood);
	}

	@Override
	public int getBlockTextureFromSide(int i) {
		if(i <= 1) {
			return 4;
		} else {
			return blockIndexInTexture;
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 3;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return Item.book.shiftedIndex;
	}

	// GreenCubes start
	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return world.multiplayerWorld;
	}
	// GreenCubes end
}

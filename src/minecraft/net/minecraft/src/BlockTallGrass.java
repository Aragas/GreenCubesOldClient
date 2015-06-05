// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockFlower, ColorizerGrass, ColorizerFoliage, IBlockAccess, 
//            WorldChunkManager, BiomeGenBase, Item, World, 
//            EntityPlayer, ItemStack, ItemShears, StatList, 
//            Block

public class BlockTallGrass extends BlockFlower {

	protected BlockTallGrass(int i, int j) {
		super(i, j, Material.tallGrass);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if(j == 1) {
			return blockIndexInTexture;
		}
		if(j == 2) {
			return blockIndexInTexture + 16 + 1;
		}
		if(j == 0) {
			return blockIndexInTexture + 16;
		} else {
			return blockIndexInTexture;
		}
	}

	@Override
	public int getBlockColor() {
		double d = 0.5D;
		double d1 = 1.0D;
		return ColorizerGrass.getGrassColor(d, d1);
	}

	@Override
	public int getRenderColor(int i) {
		if(i == 0) {
			return 0xffffff;
		} else {
			return ColorizerFoliage.getFoliageColorBasic();
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = iblockaccess.getBlockMetadata(i, j, k);
		if(l == 0) {
			return 0xffffff;
		} else {
			int c1 = 0;
			int c2 = 0;
			int c3 = 0;
			for(l = -2; l <= 2; l++) {
				for(int i1 = -2; i1 <= 2; i1++) {
					int j1 = iblockaccess.getBiomeAt(i + i1, j, k + l).getBiomeGrassColor();
					c1 += (j1 & 0xff0000) >> 16;
					c2 += (j1 & 0xff00) >> 8;
					c3 += j1 & 0xff;
				}
			}
			return (c1 / 25 & 0xff) << 16 | (c2 / 25 & 0xff) << 8 | c3 / 25 & 0xff;
		}
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		if(random.nextInt(8) == 0) {
			return Item.seeds.shiftedIndex;
		} else {
			return -1;
		}
	}

	@Override
	public int func_40198_a(int i, Random random) {
		return 1 + random.nextInt(i * 2 + 1);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		if(!world.multiplayerWorld && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
			dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.tallGrass, 1, l));
		} else {
			super.harvestBlock(world, entityplayer, i, j, k, l);
		}
	}
}

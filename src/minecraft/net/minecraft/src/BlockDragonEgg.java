// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, Material, World, BlockSand, 
//            EntityFallingSand, EntityPlayer

public class BlockDragonEgg extends Block {

	public BlockDragonEgg(int i, int j) {
		super(i, j, Material.field_41056_z);
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		func_41055_g(world, i, j, k);
	}

	private void func_41055_g(World world, int i, int j, int k) {
		int l = i;
		int i1 = j;
		int j1 = k;
		if(BlockSand.canFallBelow(world, l, i1 - 1, j1) && i1 >= 0) {
			byte byte0 = 32;
			if(BlockSand.fallInstantly || !world.checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0)) {
				world.setBlockWithNotify(i, j, k, 0);
				for(; BlockSand.canFallBelow(world, i, j - 1, k) && j > 0; j--) {
				}
				if(j > 0) {
					world.setBlockWithNotify(i, j, k, blockID);
				}
			} else {
				EntityFallingSand entityfallingsand = new EntityFallingSand(world, i + 0.5F, j + 0.5F, k + 0.5F, blockID);
				world.entityJoinedWorld(entityfallingsand);
			}
		}
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		func_41054_h(world, i, j, k);
		return true;
	}

	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer, int face) {
		func_41054_h(world, i, j, k);
	}

	private void func_41054_h(World world, int i, int j, int k) {
		if(world.getBlockId(i, j, k) != blockID) {
			return;
		}
		if(world.multiplayerWorld) {
			return;
		}
		for(int l = 0; l < 1000; l++) {
			int i1 = (i + world.rand.nextInt(16)) - world.rand.nextInt(16);
			int j1 = (j + world.rand.nextInt(8)) - world.rand.nextInt(8);
			int k1 = (k + world.rand.nextInt(16)) - world.rand.nextInt(16);
			if(world.getBlockId(i1, j1, k1) == 0) {
				world.setBlockAndMetadataWithNotify(i1, j1, k1, blockID, world.getBlockMetadata(i, j, k));
				world.setBlockWithNotify(i, j, k, 0);
				char c = '\200';
				for(int l1 = 0; l1 < c; l1++) {
					double d = world.rand.nextDouble();
					float f = (world.rand.nextFloat() - 0.5F) * 0.2F;
					float f1 = (world.rand.nextFloat() - 0.5F) * 0.2F;
					float f2 = (world.rand.nextFloat() - 0.5F) * 0.2F;
					double d1 = i1 + (i - i1) * d + (world.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
					double d2 = (j1 + (j - j1) * d + world.rand.nextDouble() * 1.0D) - 0.5D;
					double d3 = k1 + (k - k1) * d + (world.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
					world.spawnParticle("portal", d1, d2, d3, f, f1, f2);
				}

				return;
			}
		}

	}

	@Override
	public int tickRate() {
		return 3;
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return super.canPlaceBlockAt(world, i, j, k);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 27;
	}
}

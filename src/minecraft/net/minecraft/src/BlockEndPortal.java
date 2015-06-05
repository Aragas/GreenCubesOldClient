// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockContainer, TileEntityEndPortal, Entity, EntityPlayer, 
//            World, WorldProvider, Material, TileEntity, 
//            IBlockAccess, AxisAlignedBB

public class BlockEndPortal extends BlockContainer {

	public static boolean field_41051_a = false;

	protected BlockEndPortal(int i, Material material) {
		super(i, 0, material);
		setLightValue(1.0F);
	}

	@Override
	public TileEntity getBlockEntity() {
		return new TileEntityEndPortal();
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		float f = 0.0625F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(l != 0) {
			return false;
		} else {
			return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
		}
	}

	@Override
	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
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
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		if(entity.ridingEntity == null && entity.riddenByEntity == null && (entity instanceof EntityPlayer) && !world.multiplayerWorld) {
			((EntityPlayer) entity).func_40182_b(1);
		}
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		double d = i + random.nextFloat();
		double d1 = j + 0.8F;
		double d2 = k + random.nextFloat();
		double d3 = 0.0D;
		double d4 = 0.0D;
		double d5 = 0.0D;
		world.spawnParticle("smoke", d, d1, d2, d3, d4, d5);
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		if(field_41051_a) {
			return;
		}
		if(world.worldProvider.worldType != 0) {
			world.setBlockWithNotify(i, j, k, 0);
			return;
		} else {
			return;
		}
	}

}

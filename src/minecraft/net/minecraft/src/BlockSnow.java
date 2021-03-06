// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import gnu.trove.iterator.TIntObjectIterator;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, Material, World, AxisAlignedBB, 
//            IBlockAccess, Item, EntityItem, ItemStack, 
//            StatList, EntityPlayer, EnumSkyBlock

public class BlockSnow extends Block {

	protected BlockSnow(int i, int j) {
		super(i, j, Material.snow);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		setTickOnLoad(true);

	}

	@Override
	public int getRenderType() {
		return 34;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
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
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = iblockaccess.getBlockMetadata(i, j, k) & 7;
		float f = (2 * (1 + l)) / 16F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		int l = world.getBlockId(i, j - 1, k);
		if(l == 0 || !Block.blocksList[l].isOpaqueCube()) {
			return false;
		} else {
			return world.getBlockMaterial(i, j - 1, k).getIsSolid();
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		int level = 0;
		int l = world.getBlockMetadata(i, j, k);
		if(l >= 7)
			level = 6;
		else if(l == 6)
			level = 5;
		else if(l == 5)
			level = 4;
		else if(l == 4)
			level = 3;
		else if(l == 3)
			level = 2;
		else if(l == 2)
			level = 1;
		if(level > 0 && entity instanceof EntityLiving) {
			EntityLiving el = (EntityLiving) entity;
			int ml = 0;
			if(el.activeBuffs.size() > 0) {
				TIntObjectIterator<BuffActive> iterator = el.activeBuffs.iterator();
				while(iterator.hasNext()) {
					iterator.advance();
					BuffActive ab = iterator.value();
					for(BuffEffect be : ab.effects) {
						if(be.type == BuffEffectType.SNOW_BOOST)
							ml += be.multipler;
					}
				}
			}
			level = Math.max(0, level - ml);
		}
		if(level > 0) {
			entity.motionX *= (1.0 - 0.1 * level);
			entity.motionZ *= (1.0 - 0.1 * level);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		func_314_h(world, i, j, k);
	}

	private boolean func_314_h(World world, int i, int j, int k) {
		if(!canPlaceBlockAt(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockWithNotify(i, j, k, 0);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		int i1 = Item.snowball.shiftedIndex;
		float f = 0.7F;
		double d = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
		double d1 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
		double d2 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5D;
		EntityItem entityitem = new EntityItem(world, i + d, j + d1, k + d2, new ItemStack(i1, 1, 0));
		entityitem.delayBeforeCanPickup = 10;
		world.entityJoinedWorld(entityitem);
		world.setBlockWithNotify(i, j, k, 0);
		entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return Item.snowball.shiftedIndex;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if(world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) > 11) {
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockWithNotify(i, j, k, 0);
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(l == 1) {
			return true;
		} else {
			return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
		}
	}

}

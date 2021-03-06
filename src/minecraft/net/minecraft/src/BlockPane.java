// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, IBlockAccess, World, Material, 
//            AxisAlignedBB

public class BlockPane extends Block {

	private final boolean breaks;
	protected int sideTexture;
	protected int renderStage;
	protected int cutTexture;
	protected boolean[] connects = new boolean[4];
	protected boolean useCut = false;

	protected BlockPane(int id, int texture, int sideTexture, int cutTexture, Material material, boolean breaks) {
		super(id, texture, material);
		this.sideTexture = sideTexture;
		this.breaks = breaks;
		this.cutTexture = cutTexture;
	}
	
	@Override
	public int getBlockTextureFromSide(int i) {
		if(i == 0 || i == 1)
			return useCut ? cutTexture : sideTexture;
		if(renderStage == 1) {
			if(i == 4 || i == 5)
				return sideTexture;
		} else {
			if(i == 2 || i == 3)
				return sideTexture;
		}
		return blockIndexInTexture;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		if(!breaks)
			return 0;
		else
			return super.idDropped(i, random, j);
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
		return 18;
	}

	public boolean setRenderStage(IBlockAccess iblockaccess, int x, int y, int z, int stage) {
		this.renderStage = stage;
		if(stage == 0) {
			connects[0] = shouldConnect(iblockaccess.getBlockId(x, y, z - 1));
			connects[1] = shouldConnect(iblockaccess.getBlockId(x, y, z + 1));
			connects[2] = shouldConnect(iblockaccess.getBlockId(x - 1, y, z));
			connects[3] = shouldConnect(iblockaccess.getBlockId(x + 1, y, z));
		}
		useCut = false;
		final float PIXEL = 0.0625f;
		if(stage == 0) { // 0 is rendering by Z direction
			if(connects[0] && connects[1] || !connects[0] && !connects[1] && !connects[2] && !connects[3])
				setBlockBounds(0.5f - PIXEL, 0, 0, 0.5F + PIXEL, 1, 1);
			else if(connects[0])
				setBlockBounds(0.5f - PIXEL, 0, 0, 0.5F + PIXEL, 1, 0.5F + PIXEL);
			else if(connects[1])
				setBlockBounds(0.5f - PIXEL, 0, 0.5F - PIXEL, 0.5F + PIXEL, 1, 1);
			else
				return false;
		} else if(stage == 1) {
			useCut = connects[0] || connects[1];
			if(connects[2] && connects[3] || !connects[0] && !connects[1] && !connects[2] && !connects[3]) {
				setBlockBounds(0, 0, 0.5f - PIXEL, 1, 1, 0.5F + PIXEL);
				useCut = useCut || !connects[0] && !connects[1] && !connects[2] && !connects[3];
			} else if(connects[2]) {
				setBlockBounds(0, 0, 0.5f - PIXEL, 0.5F + PIXEL, 1, 0.5F + PIXEL);
			} else if(connects[3]) {
				setBlockBounds(0.5f - PIXEL, 0, 0.5F - PIXEL, 1, 1, 0.5F + PIXEL);
			} else
				return false;
		}
		return true;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(renderStage == 1 && connects[0] && connects[1]) {
			if(l == 4 && connects[3])
				return false;
			if(l == 5 && connects[2])
				return false;
		}
		if(renderStage == 0 && connects[2] && connects[3]) {
			if(l == 2 && connects[1])
				return false;
			if(l == 3 && connects[0])
				return false;
		}
		if(renderStage == 0 && (l == 4 || l == 5))
			return true;
		if(renderStage == 1 && (l == 2 || l == 3))
			return true;
		int id = iblockaccess.getBlockId(i, j, k);
		if(l != 1 && l != 0) {
			if(shouldConnect(id))
				return false;
		} else {
			Block b = Block.blocksList[id];
			if(b != null && b instanceof BlockPane) {
				return !(shouldConnect(iblockaccess.getBlockId(i, j, k - 1)) == connects[0] &&
						shouldConnect(iblockaccess.getBlockId(i, j, k + 1)) == connects[1] &&
						shouldConnect(iblockaccess.getBlockId(i - 1, j, k)) == connects[2] &&
						shouldConnect(iblockaccess.getBlockId(i + 1, j, k)) == connects[3]);
			}
		}
		return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
	}

	@Override
	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		boolean flag = shouldConnect(world.getBlockId(i, j, k - 1));
		boolean flag1 = shouldConnect(world.getBlockId(i, j, k + 1));
		boolean flag2 = shouldConnect(world.getBlockId(i - 1, j, k));
		boolean flag3 = shouldConnect(world.getBlockId(i + 1, j, k));
		if(flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1) {
			setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		} else if(flag2 && !flag3) {
			setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		} else if(!flag2 && flag3) {
			setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		if(flag && flag1 || !flag2 && !flag3 && !flag && !flag1) {
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		} else if(flag && !flag1) {
			setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		} else if(!flag && flag1) {
			setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		float f = 0.4375F;
		float f1 = 0.5625F;
		float f2 = 0.4375F;
		float f3 = 0.5625F;
		boolean flag = shouldConnect(iblockaccess.getBlockId(i, j, k - 1));
		boolean flag1 = shouldConnect(iblockaccess.getBlockId(i, j, k + 1));
		boolean flag2 = shouldConnect(iblockaccess.getBlockId(i - 1, j, k));
		boolean flag3 = shouldConnect(iblockaccess.getBlockId(i + 1, j, k));
		if(flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1) {
			f = 0.0F;
			f1 = 1.0F;
		} else if(flag2 && !flag3) {
			f = 0.0F;
		} else if(!flag2 && flag3) {
			f1 = 1.0F;
		}
		if(flag && flag1 || !flag2 && !flag3 && !flag && !flag1) {
			f2 = 0.0F;
			f3 = 1.0F;
		} else if(flag && !flag1) {
			f2 = 0.0F;
		} else if(!flag && flag1) {
			f3 = 1.0F;
		}
		setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	public boolean shouldConnect(int i) {
		if(Block.opaqueCubeLookup[i] || i == blockID || i == Block.glass.blockID)
			return true;
		Block b = Block.blocksList[i];
		return b != null && (b instanceof BlockPane || b instanceof BlockGlass);
	}
	
	@Override
	protected void init() {
		if(Item.itemsList[blockID] == null)
			new ItemBlockPane(blockID - 256);
		super.init();
	}
}

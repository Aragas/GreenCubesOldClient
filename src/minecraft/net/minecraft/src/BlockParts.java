package net.minecraft.src;

import java.util.ArrayList;

public class BlockParts extends Block implements IBlockMadeOf {

	private boolean tracing = false;
	public int metadata;
	public Block source;
	public boolean drawing = false;

	public BlockParts(int i, Block source) {
		this(i, source, 0);
	}

	public BlockParts(int i, Block source, int sourceMetadata) {
		super(i, source.blockMaterial);
		setStepSound(source.stepSound);
		setHardness(source.blockHardness);
		setResistance(source.blockResistance / 3F);
		this.blockIndexInTexture = source.blockIndexInTexture;
		this.source = source;
		this.metadata = sourceMetadata;
	}

	@Override
	public Block getBlockMadeOf() {
		return source;
	}

	@Override
	public int getRenderType() {
		return 40;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBoundsByHoldenData(1);
	}

	@Override
	public int getBlockTextureFromSide(int i) {
		return source.getBlockTextureFromSideAndMetadata(i, metadata);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z) {
		if(tracing)
			return;
		minX = minY = minZ = maxX = maxY = maxZ = 0.5F;
		int data = iblockaccess.getBlockMetadata(x, y, z);
		if((data & (1 << 0)) != 0)
			appBounds(0, 0, 0, 0.5F, 0.5F, 0.5F);
		if((data & (1 << 1)) != 0)
			appBounds(0.5F, 0, 0, 1F, 0.5F, 0.5F);
		if((data & (1 << 2)) != 0)
			appBounds(0, 0, 0.5F, 0.5F, 0.5F, 1F);
		if((data & (1 << 3)) != 0)
			appBounds(0.5F, 0, 0.5F, 1F, 0.5F, 1F);

		if((data & (1 << 4)) != 0)
			appBounds(0, 0.5F, 0, 0.5F, 1F, 0.5F);
		if((data & (1 << 5)) != 0)
			appBounds(0.5F, 0.5F, 0, 1F, 1F, 0.5F);
		if((data & (1 << 6)) != 0)
			appBounds(0, 0.5F, 0.5F, 0.5F, 1F, 1F);
		if((data & (1 << 7)) != 0)
			appBounds(0.5F, 0.5F, 0.5F, 1F, 1F, 1F);
	}

	protected void appBounds(float x1, float y1, float z1, float x2, float y2, float z2) {
		minX = Math.min(minX, x1);
		minY = Math.min(minY, y1);
		minZ = Math.min(minZ, z1);
		maxX = Math.max(maxX, x2);
		maxY = Math.max(maxY, y2);
		maxZ = Math.max(maxZ, z2);
	}

	@Override
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		int data = world.getBlockMetadata(x, y, z);
		for(int var12 = 0; var12 < 8; ++var12) {
			if((data & (1 << var12)) != 0) {
				setBoundsByHoldenData(data & (1 << var12));
				AxisAlignedBB bb = AxisAlignedBB.getBoundingBoxFromPool(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
				if(axisalignedbb.intersectsWith(bb))
					arraylist.add(bb);
			}
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		this.setBlockBoundsBasedOnState(world, i, j, k);
		return AxisAlignedBB.getBoundingBoxFromPool(i + minX, j + minY, k + minZ, i + maxX, j + maxY, k + maxZ);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		this.setBlockBoundsBasedOnState(world, i, j, k);
		return AxisAlignedBB.getBoundingBoxFromPool(i + minX, j + minY, k + minZ, i + maxX, j + maxY, k + maxZ);
	}

	public static boolean isFullBlock(int data) {
		return (data & 0xFF) == 0xFF;
	}

	public static boolean isBottomHalfBlock(int data) {
		return (data & 0x0F) == 0xF;
	}

	public static boolean isTopHalfBlock(int data) {
		return (data & 0xF0) == 0xF0;
	}

	public static boolean isHalfBlock(int data, BlockFace face) {
		switch(face) {
		case NORTH:
			return (data & 85) == 85;
		case SOUTH:
			return (data & 170) == 170;
		case EAST:
			return (data & 51) == 51;
		case WEST:
			return (data & 204) == 204;
		default:
			break;
		}
		return false;
	}

	public static boolean isHalfBlockBottomPart(int data, BlockFace face) {
		switch(face) {
		case NORTH:
			return (data & 5) == 5;
		case SOUTH:
			return (data & 10) == 10;
		case EAST:
			return (data & 3) == 3;
		case WEST:
			return (data & 12) == 12;
		default:
			break;
		}
		return false;
	}

	public static boolean isHalfBlockTopPart(int data, BlockFace face) {
		switch(face) {
		case NORTH:
			return (data & 80) == 80;
		case SOUTH:
			return (data & 160) == 160;
		case EAST:
			return (data & 48) == 48;
		case WEST:
			return (data & 192) == 192;
		default:
			break;
		}
		return false;
	}

	public static int getDataByHalfBlockFace(BlockFace face) {
		switch(face) {
		case NORTH:
			return 85;
		case SOUTH:
			return 170;
		case EAST:
			return 51;
		case WEST:
			return 204;
		case TOP:
			return 0xF0;
		case DOWN:
			return 0x0F;
		default:
			break;
		}
		return 0;
	}

	public void setBoundsByHalfBlockSide(BlockFace face) {
		switch(face) {
		case SOUTH:
			setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		case WEST:
			setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			break;
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			break;
		case EAST:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			break;
		case TOP:
			setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		case DOWN:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			break;
		default:
			break;
		}
	}

	public void setBoundsByHoldenData(int data) {
		if(data == 1)
			setBlockBounds(0, 0, 0, 0.5F, 0.5F, 0.5F);
		else if(data == 2)
			setBlockBounds(0.5F, 0, 0, 1F, 0.5F, 0.5F);
		else if(data == 4)
			setBlockBounds(0, 0, 0.5F, 0.5F, 0.5F, 1F);
		else if(data == 8)
			setBlockBounds(0.5F, 0, 0.5F, 1F, 0.5F, 1F);
		else if(data == 16)
			setBlockBounds(0, 0.5F, 0, 0.5F, 1F, 0.5F);
		else if(data == 32)
			setBlockBounds(0.5F, 0.5F, 0, 1F, 1F, 0.5F);
		else if(data == 64)
			setBlockBounds(0, 0.5F, 0.5F, 0.5F, 1F, 1F);
		else if(data == 128)
			setBlockBounds(0.5F, 0.5F, 0.5F, 1F, 1F, 1F);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D par5Vec3, Vec3D par6Vec3) {
		tracing = true;
		int data = world.getBlockMetadata(x, y, z);
		MovingObjectPosition[] var7 = new MovingObjectPosition[8];

		for(int var12 = 0; var12 < 8; ++var12) {
			if((data & (1 << var12)) != 0) {
				setBoundsByHoldenData(data & (1 << var12));
				var7[var12] = super.collisionRayTrace(world, x, y, z, par5Vec3, par6Vec3);
			}
		}

		MovingObjectPosition var23 = null;
		double var22 = 0.0D;

		for(int var17 = 0; var17 < var7.length; ++var17) {
			MovingObjectPosition var18 = var7[var17];

			if(var18 != null) {
				double var19 = var18.hitVec.squareDistanceTo(par6Vec3);

				if(var19 > var22) {
					var23 = var18;
					var22 = var19;
				}
			}
		}
		tracing = false;
		return var23;
	}

	@Override
	protected void init() {
		if(Item.itemsList[blockID] == null)
			new ItemBlockParts(blockID - 256, this).setMaxStackSize(256);
		super.init();
	}

	@Override
	public int getMixedBrightnessForBlock(IBlockAccess iblockaccess, int i, int j, int k) {
		if(!drawing)
			return super.getMixedBrightnessForBlock(iblockaccess, i, j, k);
		return 15 << 24 | 15;
	}

}

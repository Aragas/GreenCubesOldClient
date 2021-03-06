package net.minecraft.src;

public class BlockHalfBlock extends Block implements IBlockMadeOf {

	private String blockName;
	private String blockNameOrig;
	public final Block original;

	public BlockHalfBlock(int i, Block original) {
		super(i, original.blockIndexInTexture, original.blockMaterial);
		this.original = original;
		this.blockGlassType = original.blockGlassType;
		setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		opaqueCubeLookup[i] = true;
		setLightOpacity(0);
		setHardness(original.blockHardness);
		setResistance(original.blockResistance);
		setStepSound(original.stepSound);
	}

	public BlockHalfBlock(int i, int j, Block original) {
		this(i, original);
		this.blockIndexInTexture = j;
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		switch(world.getBlockMetadata(i, j, k) & 3) {
		case 0:
			return AxisAlignedBB.getBoundingBoxFromPool(i + 0.5, j, k, i + 1.0, j + 1.0, k + 1.0);
		case 2:
			return AxisAlignedBB.getBoundingBoxFromPool(i, j, k + 0.5, i + 1.0, j + 1.0, k + 1.0);
		case 1:
			return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 0.5, j + 1.0, k + 1.0);
		case 3:
			return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1.0, j + 1.0, k + 0.5);
		}
		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		switch(world.getBlockMetadata(i, j, k) & 3) {
		case 0:
			return AxisAlignedBB.getBoundingBoxFromPool(i + 0.5, j, k, i + 1.0, j + 1.0, k + 1.0);
		case 2:
			return AxisAlignedBB.getBoundingBoxFromPool(i, j, k + 0.5, i + 1.0, j + 1.0, k + 1.0);
		case 1:
			return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 0.5, j + 1.0, k + 1.0);
		case 3:
			return AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1.0, j + 1.0, k + 0.5);
		}
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int d = world.getBlockMetadata(i, j, k) << 2;
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		if(l == 0)
			world.setBlockMetadataWithNotify(i, j, k, 2 | d);
		else if(l == 1)
			world.setBlockMetadataWithNotify(i, j, k, 1 | d);
		else if(l == 2)
			world.setBlockMetadataWithNotify(i, j, k, 3 | d);
		else if(l == 3)
			world.setBlockMetadataWithNotify(i, j, k, 0 | d);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
		switch(blockAccess.getBlockMetadata(i, j, k) & 3) {
		case 0:
			setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		case 2:
			setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			break;
		case 1:
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			break;
		case 3:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			break;
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x, int y, int z, int i) {
		int data = iblockaccess.getBlockMetadata(x, y, z) & 3;
		if(!isBigSide(i, data)) {
			//0 ������ �������
			int x2 = x; //���������� ������������� �����
			int y2 = y;
			int z2 = z;
			if(i == 0)
				y2++;
			else if(i == 1)
				y2--;
			else if(i == 2)
				z2++;
			else if(i == 3)
				z2--;
			else if(i == 4)
				x2++;
			else if(i == 5)
				x2--;
			Block near = Block.blocksList[iblockaccess.getBlockId(x, y, z)];
			if(near == null)
				return true;
			if(near instanceof BlockHalfBlock || near instanceof BlockStairs)
				if((iblockaccess.getBlockMetadata(x2, y2, z2) & 3) == data)
					return (blockGlassType == 0 || near.blockGlassType != blockGlassType) && !near.isOpaqueCube();
		}
		return super.shouldSideBeRendered(iblockaccess, x, y, z, i);
	}

	public static boolean isBigSide(int i, int j) {
		if(j == 2 || j == 3) {
			if(i == 0 || i == 1)
				return false;
			if(i == 5 || i == 4)
				return false;
			return true;
		} else if(j == 0 || j == 1) {
			if(i == 0 || i == 1)
				return false;
			if(i == 5 || i == 4)
				return true;
			return false;
		}
		return true;
	}

	public static BlockFace getDirectionByData(int data) {
		switch(data & 3) {
		case 0:
			return BlockFace.SOUTH;
		case 2:
			return BlockFace.WEST;
		case 1:
			return BlockFace.NORTH;
		case 3:
			return BlockFace.EAST;
		}
		return null;
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
	public Block getBlockMadeOf() {
		return original;
	}

	@Override
	protected void init() {
		if(Item.itemsList[blockID] == null)
			new ItemHalfBlock(blockID - 256);
		super.init();
	}
}

package net.minecraft.src;

public class BlockCoconutsStem extends Block {

	protected int secondTex;

	protected BlockCoconutsStem(int i, int first, int second) {
		super(i, first, Material.plants);
		setBlockUnbreakable();
		this.secondTex = second;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int data = world.getBlockMetadata(x, y, z);
		switch(data & 0x3) {
		case 1:
			setBlockBounds(0.5f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
			break;
		case 2:
			setBlockBounds(0.0f, 0.0f, 0.5f, 1.0f, 1.0f, 0.5f);
			break;
		case 3:
			setBlockBounds(0.0f, 0.0f, 0.5f, 1.0f, 1.0f, 0.5f);
			break;
		case 0:
			setBlockBounds(0.5f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
			break;
		}
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int data) {
		if(side == 5 && data == 1 || side == 3 && data == 3 || side == 4 && data == 0 || side == 2 && data == 2)
			return secondTex;
		return blockIndexInTexture;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	@Override
	public boolean canCollideCheck(int i, boolean flag) {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(l == 0 || l == 1)
			return false;
		if((l == 2 || l == 3) && minX == maxX)
			return false;
		if((l == 4 || l == 5) && minZ == maxZ)
			return false;
		return true;
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
	public int getRenderType() {
		return 0;
	}

}

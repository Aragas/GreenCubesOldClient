package net.minecraft.src;

public class BlockBanner extends BlockOnWall {
	
	protected float depth = 0.5f / 16f;
	protected float width = 1f;
	protected float widthMin = (1 - width) * 0.5f;
	protected float widthMax = (1 + width) * 0.5f;
	public final int[][] textures;

	public BlockBanner(int i, int[][] textures) {
		super(i, Material.wool);
		this.textures = textures;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return textures[(j >> 4) % (textures.length)][(j >> 3) & 1];
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		BlockFace niceFace = BlockFace.getFaceById(l);
		BlockFace face = BlockOnWall.getAttachedSide(iblockaccess.getBlockMetadata(i - niceFace.getModX(), j - niceFace.getModY(), k - niceFace.getModZ()));
		if(face == niceFace)
			return true;
		if(face == niceFace.getOpposite())
			return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
		return false;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		BlockFace face = BlockOnWall.getAttachedSide(iblockaccess.getBlockMetadata(i, j, k));
		switch(face) {
		case NORTH:
			setBlockBounds(1 - depth, widthMin, widthMin, 1, widthMax, widthMax);
			break;
		case SOUTH:
			setBlockBounds(0, widthMin, widthMin, depth, widthMax, widthMax);
			break;
		case EAST:
			setBlockBounds(widthMin, widthMin, 1 - depth, widthMax, widthMax, 1);
			break;
		case WEST:
		default:
			setBlockBounds(widthMin, widthMin, 0, widthMax, widthMax, depth);
			break;
		}
	}

}

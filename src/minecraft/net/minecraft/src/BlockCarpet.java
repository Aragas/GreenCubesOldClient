package net.minecraft.src;

public class BlockCarpet extends Block {

	private boolean renderingItem = false;
	public final int[][] textures;
	
	public BlockCarpet(int id, int[][] textures) {
		super(id, Material.wool);
		this.textures = textures;
		setBlockBounds(0f, 0f, 0f, 1f, 1f / 32f, 1f);
		setRequiresSelfNotify();
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		setBlockBoundsBasedOnState(world, i, j, k);
		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
	}
	
	@Override
	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		world.setBlockMetadata(i, j, k, world.getBlockMetadata(i, j, k) << 4 | 0xF);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
		if(l != 1)
			return false;
		return super.canPlaceBlockOnSide(world, i, j, k, l);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		renderingItem = false;
		int data = iblockaccess.getBlockMetadata(i, j, k);
		setBlockBounds((data & 1) == 1 ? 1f / 16f : 0, 0f, (data & 2) == 2 ? 1f / 16f : 0f, (data & 4) == 4 ? 15f / 16f : 1f, 1f / 32f, (data & 8) == 8 ? 15f / 16f : 1f);
	}
	
	@Override
	public void setBlockBoundsForItemRender() {
		renderingItem = true;
		setBlockBounds(0f, 0f, 0f, 1f, 1f / 32f, 1f);
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return textures[(renderingItem ? j : j >> 4) % textures.length][i == 0 || i == 1 ? 0 : 1];
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityplayer, int face) {
		ItemStack item = entityplayer.getCurrentEquippedItem();
		if(item == null || !(item.getItem() instanceof ItemBlockColored))
			return;
		int data = world.getBlockMetadata(x, y, z);
		int sides = ((data & 0xF) + 1) % 16;
		world.setBlockMetadataWithNotify(x, y, z, (data & 0xF0) + sides);
	}
	
	@Override
	protected void init() {
		if(Item.itemsList[blockID] == null)
			new ItemBlock(blockID - 256) {
				@Override
				public int getPlacedBlockMetadata(int i) {
					return i;
				}
				
				@Override
				public String getItemNameIS(ItemStack itemstack) {
					return (new StringBuilder()).append(super.getItemName()).append(".").append(itemstack.getItemDamage()).toString();
				}
			};
		super.init();
	}
}

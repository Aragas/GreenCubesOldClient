package net.minecraft.src;

import static net.minecraft.src.GreenTextures.glassStepTexture;

import org.greencubes.util.HSVColor;

public class BlockColoredGreenStepGlass extends BlockGreenStepGlass {
	
	public final int[][] colors;
	private int currentPass;
	private boolean renderingItem = false;

	public BlockColoredGreenStepGlass(int id, Block original, boolean up, int[][] colors) {
		super(id, Block.glass.blockIndexInTexture, glassStepTexture, original, up);
		this.colors = colors;
		for(int i = 0; i < colors.length; ++i) {
			HSVColor hsv = HSVColor.fromRGB(colors[i][1]);
			hsv = new HSVColor(hsv.h, Math.min(1.0f, hsv.s / 1.75f), Math.min(1.0f, hsv.v * 1.75f));
			colors[i][1] = hsv.getRGB();
		}
		multipassBlocks[id] = true;
	}
	
	@Override
	protected boolean isShouldBeConnected(IBlockAccess iblockaccess, int x, int y, int z, int i, int d) {
		int x2 = x;
		int y2 = y;
		int z2 = z;
		if(i == 0)
			y2--;
		else if(i == 1)
			y2++;
		else if(i == 2)
			z2--;
		else if(i == 3)
			z2++;
		else if(i == 4)
			x2--;
		else if(i == 5)
			x2++;
		return (iblockaccess.getBlockId(x, y, z) == blockID && !iblockaccess.isBlockOpaqueCube(x2, y2, z2) && (iblockaccess.getBlockId(x2, y2, z2) != blockID));
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return super.getBlockTextureFromSideAndMetadata(i, this.renderingItem ? 0 : j);
	}
	
	@Override
	public boolean setUpPass(int pass) {
		this.currentPass = pass;
		this.renderingItem = false;
		return pass == 0 || pass == 2;
	}
	
	@Override
	public void setBlockBoundsForItemRender() {
		super.setBlockBoundsForItemRender();
		this.currentPass = 0;
		this.renderingItem = true;
	}
	
	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int i) {
		if(currentPass == 2)
			return colors[iblockaccess.getBlockMetadata(x, y, z) % colors.length][0];
		return super.getBlockTexture(iblockaccess, x, y, z, i);
	}
	
	@Override
	public int getRenderColor(int i) {
		return currentPass == 2 ? 0xffffff : colors[i % colors.length][1];
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return currentPass == 2 ? 0xffffff : getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
	}
	
	@Override
	protected void init() {
		if(Item.itemsList[blockID] == null) {
			if(isUp) {
				new ItemGreenStepUp(blockID - 256) {
					@Override
					public int getPlacedBlockMetadata(int i) {
						return i;
					}
					
					@Override
					public String getItemNameIS(ItemStack itemstack) {
						return (new StringBuilder()).append(super.getItemName()).append(".").append(ItemDye.dyeColorNames[BlockWool.getBlockFromDye(itemstack.getItemDamage())]).toString();
					}
				};
			} else {
				new ItemBlockColored(blockID - 256);
			}
		}
		super.init();
	}
}

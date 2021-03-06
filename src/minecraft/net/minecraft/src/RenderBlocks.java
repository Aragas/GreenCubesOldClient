// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            IBlockAccess, Block, MathHelper, BlockRail, 
//            BlockDragonEgg, BlockFence, BlockPane, BlockFenceGate, 
//            BlockCauldron, BlockBrewingStand, ModLoader, BlockEndPortalFrame, 
//            Tessellator, BlockBed, Direction, EntityRenderer, 
//            BlockRedstoneRepeater, BlockPistonBase, BlockPistonExtension, Vec3D, 
//            BlockFire, BlockRedstoneWire, BlockStem, Material, 
//            BlockFluid, World, BlockDoor, BlockGrass, 
//            ChestItemRenderHelper

public class RenderBlocks {

	public static boolean fancyGrass = true;
	public static boolean cfgGrassFix = true;

	public IBlockAccess blockAccess;
	public int overrideBlockTexture = -1;
	public boolean flipTexture = false;
	public boolean renderAllFaces = false;
	public boolean useInventoryTint = true;
	public int uvRotateEast = 0;
	public int uvRotateWest = 0;
	public int uvRotateSouth = 0;
	public int uvRotateNorth = 0;
	public int uvRotateTop = 0;
	public int uvRotateBottom = 0;
	public boolean enableAO;
	public float lightValueOwn;
	public float aoLightValueXNeg;
	public float aoLightValueYNeg;
	public float aoLightValueZNeg;
	public float aoLightValueXPos;
	public float aoLightValueYPos;
	public float aoLightValueZPos;
	public float aoLightValueScratchXYZNNN;
	public float aoLightValueScratchXYNN;
	public float aoLightValueScratchXYZNNP;
	public float aoLightValueScratchYZNN;
	public float aoLightValueScratchYZNP;
	public float aoLightValueScratchXYZPNN;
	public float aoLightValueScratchXYPN;
	public float aoLightValueScratchXYZPNP;
	public float aoLightValueScratchXYZNPN;
	public float aoLightValueScratchXYNP;
	public float aoLightValueScratchXYZNPP;
	public float aoLightValueScratchYZPN;
	public float aoLightValueScratchXYZPPN;
	public float aoLightValueScratchXYPP;
	public float aoLightValueScratchYZPP;
	public float aoLightValueScratchXYZPPP;
	public float aoLightValueScratchXZNN;
	public float aoLightValueScratchXZPN;
	public float aoLightValueScratchXZNP;
	public float aoLightValueScratchXZPP;
	public int aoBrightnessXYZNNN;
	public int aoBrightnessXYNN;
	public int aoBrightnessXYZNNP;
	public int aoBrightnessYZNN;
	public int aoBrightnessYZNP;
	public int aoBrightnessXYZPNN;
	public int aoBrightnessXYPN;
	public int aoBrightnessXYZPNP;
	public int aoBrightnessXYZNPN;
	public int aoBrightnessXYNP;
	public int aoBrightnessXYZNPP;
	public int aoBrightnessYZPN;
	public int aoBrightnessXYZPPN;
	public int aoBrightnessXYPP;
	public int aoBrightnessYZPP;
	public int aoBrightnessXYZPPP;
	public int aoBrightnessXZNN;
	public int aoBrightnessXZPN;
	public int aoBrightnessXZNP;
	public int aoBrightnessXZPP;
	public int aoType = 1;
	public int brightnessTopLeft;
	public int brightnessBottomLeft;
	public int brightnessBottomRight;
	public int brightnessTopRight;
	public float colorRedTopLeft;
	public float colorRedBottomLeft;
	public float colorRedBottomRight;
	public float colorRedTopRight;
	public float colorGreenTopLeft;
	public float colorGreenBottomLeft;
	public float colorGreenBottomRight;
	public float colorGreenTopRight;
	public float colorBlueTopLeft;
	public float colorBlueBottomLeft;
	public float colorBlueBottomRight;
	public float colorBlueTopRight;
	public boolean aoTransparentXYZCPN;
	public boolean aoTransparentXYZPPC;
	public boolean aoTransparentXYZNPC;
	public boolean aoTransparentXYZCPP;
	public boolean aoTransparentXYZNCN;
	public boolean aoTransparentXYZPCP;
	public boolean aoTransparentXYZNCP;
	public boolean aoTransparentXYZPCN;
	public boolean aoTransparentXYZCNN;
	public boolean aoTransparentXYZPNC;
	public boolean aoTransparentXYZNNC;
	public boolean aoTransparentXYZCNP;
	
	private boolean fixUV = false;
	private double minBlockX;
	private double minBlockY;
	private double minBlockZ;
	private double maxBlockX;
	private double maxBlockY;
	private double maxBlockZ;
	private BlockFace fixSide = null;
	
	public RenderBlocks(IBlockAccess iblockaccess) {
		blockAccess = iblockaccess;
	}

	public RenderBlocks() {
	}

	public void clearOverrideBlockTexture() {
		overrideBlockTexture = -1;
	}

	public void renderBlockUsingTexture(Block block, int i, int j, int k, int l) {
		overrideBlockTexture = l;
		renderBlockByRenderType(block, i, j, k);
		overrideBlockTexture = -1;
	}

	public void renderBlockAllFaces(Block block, int i, int j, int k) {
		renderAllFaces = true;
		renderBlockByRenderType(block, i, j, k);
		renderAllFaces = false;
	}

	public boolean renderLightOnBlock(int i, int j, int k, int l) {
		if(this != null) {
			return false;
		}
		byte byte0 = 13;
		byte byte1 = 52;
		byte byte2 = 32;
		int i1 = MathHelper.abs_int(i - byte0);
		int j1 = MathHelper.abs_int(k - byte1);
		if(j1 <= byte2 && i1 <= byte2 && (i1 == byte2 || j1 == byte2) && Block.catchesLight[blockAccess.getBlockId(i, j, k)]) {
			int k1 = 5;
			if(i == byte0 - byte2) {
				k1--;
			}
			if(i == byte0 + byte2) {
				k1++;
			}
			if(k == byte1 - byte2) {
				k1 -= 3;
			}
			if(k == byte1 + byte2) {
				k1 += 3;
			}
			renderCorrectedLightOnBlock(k1, i, j, k);
			return true;
		} else {
			return false;
		}
	}

	public boolean renderBlockByRenderType(Block block, int i, int j, int k) {
		int l = block.getRenderType();
		block.setBlockBoundsBasedOnState(blockAccess, i, j, k);

		if(l == 0) {
			return renderStandardBlock(block, i, j, k);
		}

		if(l == 4) {
			return renderBlockFluids(block, i, j, k);
		}

		if(l == 13) {
			return renderBlockCactus(block, i, j, k);
		}

		if(l == 1) {
			return renderCrossedSquares(block, i, j, k);
		}

		if(l == 19) {
			return renderBlockStem(block, i, j, k);
		}

		if(l == 23) {
			return renderBlockLilyPad(block, i, j, k);
		}

		if(l == 6) {
			return renderBlockCrops(block, i, j, k);
		}

		if(l == 2) {
			return renderBlockTorch(block, i, j, k);
		}

		if(l == 3) {
			return renderBlockFire(block, i, j, k);
		}

		if(l == 5) {
			return renderBlockRedstoneWire(block, i, j, k);
		}

		if(l == 8) {
			return renderBlockLadder(block, i, j, k);
		}

		if(l == 7) {
			return renderBlockDoor(block, i, j, k);
		}

		if(l == 9) {
			return renderBlockMinecartTrack((BlockRail) block, i, j, k);
		}

		if(l == 10) {
			return renderBlockStairs((BlockStairs) block, i, j, k);
		}

		if(l == 11) {
			return renderBlockFence((BlockFence) block, i, j, k);
		}

		if(l == 12) {
			return renderBlockLever(block, i, j, k);
		}

		if(l == 14) {
			return renderBlockBed(block, i, j, k);
		}

		if(l == 15) {
			return renderBlockRepeater(block, i, j, k);
		}

		if(l == 16) {
			return renderPistonBase(block, i, j, k, false);
		}

		if(l == 17) {
			return renderPistonExtension(block, i, j, k, true);
		}

		if(l == 18) {
			return renderBlockPane((BlockPane) block, i, j, k);
		}

		if(l == 20) {
			return renderBlockVine(block, i, j, k);
		}

		if(l == 21) {
			return renderBlockFenceGate((BlockFenceGate) block, i, j, k);
		}

		if(l == 24) {
			return renderBlockCauldron((BlockCauldron) block, i, j, k);
		}

		if(l == 25) {
			return renderBlockBrewingStand((BlockBrewingStand) block, i, j, k);
		}

		if(l == 26) {
			return renderBlockEndPortalFrame(block, i, j, k);
		}
		if(l == 28) {
			return renderBlockCocoa((BlockCocoa) block, i, j, k);
		}
		if(l == 31) {
			return renderBlockRotatedWood(block, i, j, k);
		}
		if(l == 32) {
			return renderBlockWall((BlockWall) block, i, j, k);
		}
		if(l == 33) {
			return renderBlockCoconut(block, i, j, k);
		}
		if(l == 34) {
			return renderBlockSnowSmooth(block, i, j, k);
		}
		if(l == 35) {
			return renderBlockSpikes(block, i, j, k);
		}
		if(l == 36) {
			return renderBlockTile((BlockTile) block, i, j, k);
		}
		if(l == 37) {
			return renderBlockWhiteFence((BlockFence) block, i, j, k);
		}
		if(l == 40) {
			return renderBlockParts((BlockParts) block, i, j, k);
		}
		if(l == 43) {
			return renderBlockFrame((BlockFrame) block, i, j, k);
		}
		if(l == 99)
			return renderBlockPSPortal(block, i, j, k);
		return ModLoader.RenderWorldBlock(this, blockAccess, i, j, k, block, l);
	}
	
	private boolean renderBlockFrame(BlockFrame block, int x, int y, int z) {
		fixUV = true;
		fixSide = BlockOnWall.getAttachedSide(blockAccess.getBlockMetadata(x, y, z));
		boolean b = renderStandardBlock(block, x, y, z);
		block.renderingOverlay = true;
		block.setBlockBoundsBasedOnState(blockAccess, x, y, z);
		b = b | renderStandardBlock(block, x, y, z);
		block.renderingOverlay = false;
		fixUV = false;
		return b;
	}

	private boolean renderBlockPSPortal(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int metadata = blockAccess.getBlockMetadata(i, j, k);
		float f = 1.0F;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		int i1 = block.colorMultiplier(blockAccess, i, j, k);
		float f1 = (i1 >> 16 & 0xff) / 255F;
		float f2 = (i1 >> 8 & 0xff) / 255F;
		float f3 = (i1 & 0xff) / 255F;
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);

		int k1 = blockAccess.getBlockMetadata(i, j, k);
		if(k1 == 0 || k1 == 1) { //X
			int l = block.getBlockTextureFromSideAndMetadata(4, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			tessellator.addVertexWithUV(i + 0.5, j + 0, k + 1, d1, d3);
			tessellator.addVertexWithUV(i + 0.5, j + 1, k + 1, d1, d2);
			tessellator.addVertexWithUV(i + 0.5, j + 1, k + 0, d, d2);
			tessellator.addVertexWithUV(i + 0.5, j + 0, k + 0, d, d3);
			/*
			tessellator.addVertexWithUV(i + 0.5, j + 0, k + 0, d, d3);
			tessellator.addVertexWithUV(i + 0.5, j + 1, k + 0, d, d2);
			tessellator.addVertexWithUV(i + 0.5, j + 1, k + 1, d1, d2);
			tessellator.addVertexWithUV(i + 0.5, j + 0, k + 1, d1, d3);
			*/
		}
		if(k1 == 2 || k1 == 3) {//Z
			int l = block.getBlockTextureFromSideAndMetadata(3, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			tessellator.addVertexWithUV(i + 1, j + 0, k + 0.5, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 1, k + 0.5, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 1, k + 0.5, d, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, k + 0.5, d, d3);
			/*
			tessellator.addVertexWithUV(i + 0, j + 0, k + 0.5, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 1, k + 0.5, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 1, k + 0.5, d1, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, k + 0.5, d1, d3);
			*/
		}
		if(k1 == 4 || k1 == 5) {//Y
			int l = block.getBlockTextureFromSideAndMetadata(3, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			tessellator.addVertexWithUV(i + 1, j + 0.5, k + 0, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 0.5, k + 1, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 0.5, k + 1, d, d2);
			tessellator.addVertexWithUV(i + 0, j + 0.5, k + 0, d, d3);
			/*
			tessellator.addVertexWithUV(i + 0, j + 0.5, k + 0, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 0.5, k + 1, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 0.5, k + 1, d1, d2);
			tessellator.addVertexWithUV(i + 1, j + 0.5, k + 0, d1, d3);
			*/
		}
		return true;
	}

	public boolean renderBlockTrammel(int data, BlockParts block, int x, int y, int z) {
		int undrown = data;
		if(BlockParts.isFullBlock(undrown)) {
			block.setBlockBounds(0, 0, 0, 1, 1, 1);
			renderStandardBlock(block, x, y, z);
			return true;
		}
		if(BlockParts.isTopHalfBlock(undrown)) {
			block.setBoundsByHalfBlockSide(BlockFace.TOP);
			renderStandardBlock(block, x, y, z);
			undrown &= ~(BlockParts.getDataByHalfBlockFace(BlockFace.TOP));
		} else if(BlockParts.isBottomHalfBlock(undrown)) {
			block.setBoundsByHalfBlockSide(BlockFace.DOWN);
			renderStandardBlock(block, x, y, z);
			undrown &= ~(BlockParts.getDataByHalfBlockFace(BlockFace.DOWN));
		} else {
			for(BlockFace face : BlockFace.sideFaces)
				if(BlockParts.isHalfBlock(undrown, face)) {
					block.setBoundsByHalfBlockSide(face);
					renderStandardBlock(block, x, y, z);
					undrown &= ~(BlockParts.getDataByHalfBlockFace(face));
				}
		}
		for(BlockFace face : BlockFace.sideFaces)
			if(BlockParts.isHalfBlockBottomPart(undrown, face)) {
				block.setBoundsByHalfBlockSide(face);
				block.maxY = 0.5F;
				renderStandardBlock(block, x, y, z);
				undrown &= ~(BlockParts.getDataByHalfBlockFace(face) & 0x0F);
			}
		for(BlockFace face : BlockFace.sideFaces)
			if(BlockParts.isHalfBlockTopPart(undrown, face)) {
				block.setBoundsByHalfBlockSide(face);
				block.minY = 0.5F;
				renderStandardBlock(block, x, y, z);
				undrown &= ~(BlockParts.getDataByHalfBlockFace(face) & 0xF0);
			}
		for(int i = 0; i < 8; ++i) {
			if((undrown & (1 << i)) != 0) {
				block.setBoundsByHoldenData(undrown & (1 << i));
				renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
			}
		}
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		return true;
	}

	public boolean renderBlockParts(BlockParts block, int x, int y, int z) {
		int data = blockAccess.getBlockMetadata(x, y, z);
		int undrown = data;
		if(BlockParts.isFullBlock(undrown)) {
			block.setBlockBounds(0, 0, 0, 1, 1, 1);
			renderStandardBlock(block, x, y, z);
			return true;
		}
		if(BlockParts.isTopHalfBlock(undrown)) {
			block.setBoundsByHalfBlockSide(BlockFace.TOP);
			renderStandardBlock(block, x, y, z);
			undrown &= ~(BlockParts.getDataByHalfBlockFace(BlockFace.TOP));
		} else if(BlockParts.isBottomHalfBlock(undrown)) {
			block.setBoundsByHalfBlockSide(BlockFace.DOWN);
			renderStandardBlock(block, x, y, z);
			undrown &= ~(BlockParts.getDataByHalfBlockFace(BlockFace.DOWN));
		} else {
			for(BlockFace face : BlockFace.sideFaces)
				if(BlockParts.isHalfBlock(undrown, face)) {
					block.setBoundsByHalfBlockSide(face);
					renderStandardBlock(block, x, y, z);
					undrown &= ~(BlockParts.getDataByHalfBlockFace(face));
				}
		}
		for(BlockFace face : BlockFace.sideFaces)
			if(BlockParts.isHalfBlockBottomPart(undrown, face)) {
				block.setBoundsByHalfBlockSide(face);
				block.maxY = 0.5F;
				renderStandardBlock(block, x, y, z);
				undrown &= ~(BlockParts.getDataByHalfBlockFace(face) & 0x0F);
			}
		for(BlockFace face : BlockFace.sideFaces)
			if(BlockParts.isHalfBlockTopPart(undrown, face)) {
				block.setBoundsByHalfBlockSide(face);
				block.minY = 0.5F;
				renderStandardBlock(block, x, y, z);
				undrown &= ~(BlockParts.getDataByHalfBlockFace(face) & 0xF0);
			}
		for(int i = 0; i < 8; ++i) {
			if((undrown & (1 << i)) != 0) {
				block.setBoundsByHoldenData(undrown & (1 << i));
				renderStandardBlock(block, x, y, z);
			}
		}
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		return true;
	}

	public boolean renderBlockWall(BlockWall block, int x, int y, int z) {
		boolean var5 = block.canConnectWallTo(this.blockAccess, x - 1, y, z, BlockFace.NORTH);
		boolean var6 = block.canConnectWallTo(this.blockAccess, x + 1, y, z, BlockFace.SOUTH);
		boolean var7 = block.canConnectWallTo(this.blockAccess, x, y, z - 1, BlockFace.EAST);
		boolean var8 = block.canConnectWallTo(this.blockAccess, x, y, z + 1, BlockFace.WEST);
		boolean var9 = var7 && var8 && !var5 && !var6;
		boolean var10 = !var7 && !var8 && var5 && var6;
		boolean var11 = this.blockAccess.isAirBlock(x, y + 1, z);

		if((var9 || var10) && var11) {
			if(var9) {
				block.setBlockBounds(0.3125F, 0.0F, 0.0F, 0.6875F, 0.8125F, 1.0F);
				this.renderStandardBlock(block, x, y, z);
			} else {
				block.setBlockBounds(0.0F, 0.0F, 0.3125F, 1.0F, 0.8125F, 0.6875F);
				this.renderStandardBlock(block, x, y, z);
			}
		} else {
			block.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
			this.renderStandardBlock(block, x, y, z);

			if(var5) {
				block.setBlockBounds(0.0F, 0.0F, 0.3125F, 0.25F, 0.8125F, 0.6875F);
				this.renderStandardBlock(block, x, y, z);
			}

			if(var6) {
				block.setBlockBounds(0.75F, 0.0F, 0.3125F, 1.0F, 0.8125F, 0.6875F);
				this.renderStandardBlock(block, x, y, z);
			}

			if(var7) {
				block.setBlockBounds(0.3125F, 0.0F, 0.0F, 0.6875F, 0.8125F, 0.25F);
				this.renderStandardBlock(block, x, y, z);
			}

			if(var8) {
				block.setBlockBounds(0.3125F, 0.0F, 0.75F, 0.6875F, 0.8125F, 1.0F);
				this.renderStandardBlock(block, x, y, z);
			}
		}

		block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);
		return true;
	}

	private boolean renderWholeTextureMap(BlockTile block, int i, int j, int k) {
		double d = i;
		double d1 = j;
		double d2 = k;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		double d7 = (d + 0.5D) - 0.5D;
		double d8 = d + 0.5D + 0.5D;
		double d9 = (d2 + 0.5D) - 0.5D;
		double d10 = d2 + 0.5D + 0.5D;

		tessellator.addVertexWithUV(d8, d1 + 64D, d10, 0.0, 0.0);
		tessellator.addVertexWithUV(d8, d1, d10, 0.0, 1.0);
		tessellator.addVertexWithUV(d8, d1, d10 + 32, 1.0, 1.0);
		tessellator.addVertexWithUV(d8, d1 + 64D, d10 + 32, 1.0, 0.0);
		return true;
	}

	private boolean renderBlockTile(BlockTile block, int i, int j, int k) {

		double d = i;
		double d1 = j;
		double d2 = k;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int l1 = block.colorMultiplier(blockAccess, i, j, k);
		float f1 = (l1 >> 16 & 0xff) / 255F;
		float f2 = (l1 >> 8 & 0xff) / 255F;
		float f3 = (l1 & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f5 = (f1 * 30F + f2 * 70F) / 100F;
			float f6 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}
		tessellator.setColorOpaque_F(f1, f2, f3);
		int r = blockAccess.getBlockMetadata(i, j, k);
		int t_self = block.getBlockTextureFromSideAndMetadata(0, r);
		if(overrideBlockTexture >= 0)
			t_self = overrideBlockTexture;
		int m = (t_self & 0xf) << 4;
		int l = t_self & RenderEngine.TEX;
		double d3 = m / 256D;
		double d4 = (m + 15.99D) / 256D;
		double d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
		double d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
		double texel_vert = 1D / RenderEngine.TERRAIN_HEIGHT_D;
		double texel_hor = 1D / 256D;
		double d7 = (d + 0.5D) - 0.5D;
		double d8 = d + 0.5D + 0.5D;
		double d9 = (d2 + 0.5D) - 0.5D;
		double d10 = d2 + 0.5D + 0.5D;
		double d11 = d1;
		double d12 = d1;
		double d13 = d1;
		double d14 = d1;
		double d15 = block.mod_down;
		double d16 = block.mod_up;
		int sidetex = block.sidetex;
		if(overrideBlockTexture >= 0)
			sidetex = overrideBlockTexture;
		m = (sidetex & 0xf) << 4;
		l = sidetex & RenderEngine.TEX;
		double d17 = m / 256D;
		double d18 = (m + 15.99D) / 256D;
		double d19 = l / RenderEngine.TERRAIN_HEIGHT_D;
		double d20 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
		float f4 = 0.8f;
		float f5 = 0.6f;
		boolean isBottomValid = false;
		boolean isBottomMono = false;
		boolean is45degree = false;
		//boolean isBottomLog = false;
		int t2 = 0;
		int t5 = 0;
		int t3 = 0;
		int t4 = 0;
		Block bottom = Block.blocksList[blockAccess.getBlockId(i, j - 1, k)];
		if(bottom instanceof BlockGreenStep) {
			if(((BlockGreenStep) bottom).original.blockID == 1)
				;
		}
		int r_1 = blockAccess.getBlockMetadata(i, j - 1, k);
		if(!(bottom == null || !bottom.renderAsNormalBlock() || !bottom.isOpaqueCube() || GCUtil.arrayExactlyContains(BlockTile.unallowedBlocks, bottom.blockID))) {
			isBottomValid = true;
			//if(bottom.blockID == 17 || bottom.blockID == mod_gc.blockLogBao.blockID || bottom.blockID == mod_gc.blockLogPalm.blockID) {
			//	isBottomLog = true;
			//}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			t5 = bottom.getBlockTextureFromSideAndMetadata(5, r_1);
			t3 = bottom.getBlockTextureFromSideAndMetadata(3, r_1);
			t4 = bottom.getBlockTextureFromSideAndMetadata(4, r_1);
			if(t2 == t5 && t3 == t4 && t5 == t3) {
				isBottomMono = true;
			}
		}
		if((d1 + 0.5D - d15) == 0) {
			is45degree = true;
		}
		//East = 2
		//West = 3
		//South = 5
		//North = 4

		//������� ������� �� x
		if(blockAccess.getBlockMetadata(i + 1, j - 1, k) == 1 && blockAccess.getBlockMetadata(i - 1, j - 1, k) == 3) {
			Block x_plus = Block.blocksList[blockAccess.getBlockId(i + 1, j - 1, k)];
			if(x_plus instanceof BlockTile) {
				Block x_minus = Block.blocksList[blockAccess.getBlockId(i - 1, j - 1, k)];
				if(x_minus instanceof BlockTile) {
					// �� 1
					tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d3, d6);
					tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d4, d6);
					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9, d4, d5 + texel_vert * 8);
					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10, d3, d5 + texel_vert * 8);

					// �� 3
					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10, d4, d5 + texel_vert * 8);
					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9, d3, d5 + texel_vert * 8);
					tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d3, d6);
					tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d4, d6);

					tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
					tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d17, d19 + texel_vert * 15);
					tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d17, d20);
					tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d18, d20);
					tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d18, d19 + texel_vert * 15);

					tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
					tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d17, d19 + texel_vert * 15);
					tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d17, d20);
					tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d18, d20);
					tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d18, d19 + texel_vert * 15);

					tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d9, d17 + texel_hor * 15, d20);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d9, d18, d20);
					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d9, d18, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D + 0.0625 - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d9, d17 + texel_hor * 15, d19 + texel_vert * 8);

					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D + 0.0625 - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d9, d18 - texel_hor * 15, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d9, d17, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d9, d17, d20);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d9, d18 - texel_hor * 15, d20);

					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d10, d18 - texel_hor * 15, d20);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d10, d17, d20);
					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d10, d17, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + 0.0625 + (0.5D - d16 + d15 - 0.0625) / 2, d10, d18 - texel_hor * 15, d19 + texel_vert * 8);

					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10, d17 + texel_hor * 15, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10, d18, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d10, d18, d20);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d10, d17 + texel_hor * 15, d20);

					if(!isBottomValid) {
						tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10, d3, d5 + texel_vert * 8);
						tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9, d4, d5 + texel_vert * 8);
						tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d4, d6);
						tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d6);

						tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d6);
						tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d6);
						tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9, d3, d5 + texel_vert * 8);
						tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10, d4, d5 + texel_vert * 8);
						return true;
					}
					t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
					if(overrideBlockTexture >= 0)
						t2 = overrideBlockTexture;
					m = (t2 & 0xf) << 4;
					l = t2 & RenderEngine.TEX;
					d3 = m / 256D;
					d4 = (m + 15.99D) / 256D;
					d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
					d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
					tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
					tessellator.addVertexWithUV(d7 + 0.5D, d1, d9, d3 + texel_hor * 8, d6);
					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d9, d3 + texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16D * texel_vert);

					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d9, d3 + texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16D * texel_vert);
					tessellator.addVertexWithUV(d8 - 0.5D, d1, d9, d3 + texel_hor * 8, d6);
					tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 - d15 * 16 * texel_vert);

					if(!is45degree) {
						if(!isBottomMono) {
							if(overrideBlockTexture >= 0)
								t5 = overrideBlockTexture;
							m = (t5 & 0xf) << 4;
							l = t5 & RenderEngine.TEX;
							d3 = m / 256D;
							d4 = (m + 15.99D) / 256D;
							d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
							d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
						}
						tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
						tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
						tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
						tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
						tessellator.addVertexWithUV(d8, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					}

					if(!isBottomMono) {
						if(overrideBlockTexture >= 0)
							t3 = overrideBlockTexture;
						m = (t3 & 0xf) << 4;
						l = t3 & RenderEngine.TEX;
						d3 = m / 256D;
						d4 = (m + 15.99D) / 256D;
						d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
						d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
					}
					tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
					tessellator.addVertexWithUV(d8 - 0.5D, d1, d10, d4 - texel_hor * 8, d6);
					tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d10, d4 - texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16 * texel_vert);

					tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d10, d3 + texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16 * texel_vert);
					tessellator.addVertexWithUV(d7 + 0.5D, d1, d10, d3 + texel_hor * 8, d6);
					tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);

					if(!is45degree) {
						if(!isBottomMono) {
							if(overrideBlockTexture >= 0)
								t4 = overrideBlockTexture;
							m = (t4 & 0xf) << 4;
							l = t4 & RenderEngine.TEX;
							d3 = m / 256D;
							d4 = (m + 15.99D) / 256D;
							d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
							d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
						}
						tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
						tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
						tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
						tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
						tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					}
					return true;
				}
			}
		}

		if(blockAccess.getBlockMetadata(i, j - 1, k + 1) == 2 && blockAccess.getBlockMetadata(i, j - 1, k - 1) == 0) {
			Block y_plus = Block.blocksList[blockAccess.getBlockId(i, j - 1, k + 1)];
			if(y_plus instanceof BlockTile) {
				Block y_minus = Block.blocksList[blockAccess.getBlockId(i, j - 1, k - 1)];
				if(y_minus instanceof BlockTile) {
					// �� 0
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d3, d5 + texel_vert * 8);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d9, d3, d6);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d9, d4, d6);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d4, d5 + texel_vert * 8);
					//�� 2
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d10, d4, d6);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9 + 0.5D, d4, d5 + texel_vert * 8);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9 + 0.5D, d3, d5 + texel_vert * 8);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d10, d3, d6);

					tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d9, d18, d19 + texel_vert * 15);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d9, d18, d20);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d9, d17, d20);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d9, d17, d19 + texel_vert * 15);

					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d10, d18, d19 + texel_vert * 15);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d10, d18, d20);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d10, d17, d20);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d10, d17, d19 + texel_vert * 15);

					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d10, d18 - texel_hor * 15, d20);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d10, d17, d20);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d17, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d18 - texel_hor * 15, d19 + texel_vert * 8);

					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d18 - texel_hor * 15, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d17, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d9, d17, d20);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + 0.0625, d9, d18 - texel_hor * 15, d20);

					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d9, d18 - texel_hor * 15, d20);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d9, d17, d20);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d17, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d18 - texel_hor * 15, d19 + texel_vert * 8);

					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d18 - texel_hor * 15, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d17, d19 + texel_vert * 8);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d10, d17, d20);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + 0.0625, d10, d18 - texel_hor * 15, d20);

					if(!isBottomValid) {
						tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d10, d3, d6);
						tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9 + 0.5D, d3, d5 + texel_vert * 8);
						tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d9 + 0.5D, d4, d5 + texel_vert * 8);
						tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d10, d4, d6);

						tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d4, d5 + texel_vert * 8);
						tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d9, d4, d6);
						tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d9, d3, d6);
						tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + (d1 + 1.0D - d16 - (d1 + 0.5D - d15 + 0.0625)) / 2, d10 - 0.5D, d3, d5 + texel_vert * 8);

						return true;
					}
					if(!is45degree) {
						t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
						if(overrideBlockTexture >= 0)
							t2 = overrideBlockTexture;
						m = (t2 & 0xf) << 4;
						l = t2 & RenderEngine.TEX;
						d3 = m / 256D;
						d4 = (m + 15.99D) / 256D;
						d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
						d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
						tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
						tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
						tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
						tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
						tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					}
					if(!isBottomMono) {
						if(overrideBlockTexture >= 0)
							t5 = overrideBlockTexture;
						m = (t5 & 0xf) << 4;
						l = t5 & RenderEngine.TEX;
						d3 = m / 256D;
						d4 = (m + 15.99D) / 256D;
						d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
						d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
					}

					tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
					tessellator.addVertexWithUV(d8, d1, d10 - 0.5D, d3 + texel_hor * 8, d6);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d10 - 0.5D, d3 + texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16 * texel_vert);

					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d10 - 0.5D, d3 + texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16 * texel_vert);
					tessellator.addVertexWithUV(d8, d1, d10 - 0.5D, d4 - texel_hor * 8, d6);
					tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
					tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					if(!is45degree) {
						if(!isBottomMono) {
							if(overrideBlockTexture >= 0)
								t3 = overrideBlockTexture;
							m = (t3 & 0xf) << 4;
							l = t3 & RenderEngine.TEX;
							d3 = m / 256D;
							d4 = (m + 15.99D) / 256D;
							d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
							d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
						}
						tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);

						tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
						tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
						tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
						tessellator.addVertexWithUV(d8, d1 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					}
					if(!isBottomMono) {
						if(overrideBlockTexture >= 0)
							t4 = overrideBlockTexture;
						m = (t4 & 0xf) << 4;
						l = t4 & RenderEngine.TEX;
						d3 = m / 256D;
						d4 = (m + 15.99D) / 256D;
						d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
						d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
					}
					tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
					tessellator.addVertexWithUV(d7, d1, d10 - 0.5D, d4 - texel_hor * 8, d6);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d10 - 0.5D, d3 + texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16 * texel_vert);

					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2, d10 - 0.5D, d3 + texel_hor * 8, d6 - (0.5D - d15 + (0.5D - d16 + d15 - 0.0625) / 2) * 16 * texel_vert);
					tessellator.addVertexWithUV(d7, d1, d10 - 0.5D, d3 + texel_hor * 8, d6);
					tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
					tessellator.addVertexWithUV(d7, d1 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
					return true;
				}
			}
		}

		//������� ������� 1
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k + 1) && blockAccess.getBlockMetadata(i, j, k + 1) == 3 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i + 1, j, k) && blockAccess.getBlockMetadata(i + 1, j, k) == 0) {
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d3, d5);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d4, d6);

			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d4, d5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d4, d6);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d18, d19 + texel_vert * 15);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d18, d19 + texel_vert * 15);
			/*
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d17, d19);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d18 - texel_hor * 15, d20);
			
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d3, d5);

				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d4, d5);
				return true;
			}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			if(!is45degree) {
				tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t5 = overrideBlockTexture;
				m = (t5 & 0xf) << 4;
				l = t5 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);

			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t3 = overrideBlockTexture;
				m = (t3 & 0xf) << 4;
				l = t3 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			if(!is45degree) {
				if(!isBottomMono) {
					if(overrideBlockTexture >= 0)
						t4 = overrideBlockTexture;
					m = (t4 & 0xf) << 4;
					l = t4 & RenderEngine.TEX;
					d3 = m / 256D;
					d4 = (m + 15.99D) / 256D;
					d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
					d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
				}
				tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
			return true;
		}
		//������� ������� 2
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k + 1) && blockAccess.getBlockMetadata(i, j, k + 1) == 1 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i - 1, j, k) && blockAccess.getBlockMetadata(i - 1, j, k) == 0) {
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d4, d5);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d4, d5);

			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d3, d5);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d3, d5);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d18, d19 + texel_vert * 15);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d18, d19 + texel_vert * 15);
			/*
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d18, d19);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d17 + texel_hor * 15, d19);
			
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5);

				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d3, d5);
				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d3, d5);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d6);

				return true;
			}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			if(!is45degree) {
				tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
			if(!is45degree) {
				if(!isBottomMono) {
					if(overrideBlockTexture >= 0)
						t5 = overrideBlockTexture;
					m = (t5 & 0xf) << 4;
					l = t5 & RenderEngine.TEX;
					d3 = m / 256D;
					d4 = (m + 15.99D) / 256D;
					d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
					d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
				}
				tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d16 * 16 * texel_vert);
				tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
				tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t3 = overrideBlockTexture;
				m = (t3 & 0xf) << 4;
				l = t3 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t4 = overrideBlockTexture;
				m = (t4 & 0xf) << 4;
				l = t4 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			return true;
		}
		//������� ������� 3
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k - 1) && blockAccess.getBlockMetadata(i, j, k - 1) == 1 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i - 1, j, k) && blockAccess.getBlockMetadata(i - 1, j, k) == 2) {
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d4, d5);

			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d3, d6);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d18, d19 + texel_vert * 15);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d18, d19 + texel_vert * 15);
			/*
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d17, d19);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d17, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d18 - texel_hor * 15, d20);
			
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d18, d19);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d6);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d6);

				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d6);
				return true;
			}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			if(!is45degree) {
				if(!isBottomMono) {
					if(overrideBlockTexture >= 0)
						t5 = overrideBlockTexture;
					m = (t5 & 0xf) << 4;
					l = t5 & RenderEngine.TEX;
					d3 = m / 256D;
					d4 = (m + 15.99D) / 256D;
					d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
					d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
				}
				tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d16 * 16 * texel_vert);
				tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
				tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				if(!isBottomMono) {
					if(overrideBlockTexture >= 0)
						t3 = overrideBlockTexture;
					m = (t3 & 0xf) << 4;
					l = t3 & RenderEngine.TEX;
					d3 = m / 256D;
					d4 = (m + 15.99D) / 256D;
					d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
					d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
				}
				tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
				tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t4 = overrideBlockTexture;
				m = (t4 & 0xf) << 4;
				l = t4 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			return true;
		}
		//������� ������� 4
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k - 1) && blockAccess.getBlockMetadata(i, j, k - 1) == 3 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i + 1, j, k) && blockAccess.getBlockMetadata(i + 1, j, k) == 2) {
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d4, d6);

			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d3, d6);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d3, d6);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d18, d19 + texel_vert * 15);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d18, d19 + texel_vert * 15);
			/*
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d17, d19);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d18 - texel_hor * 15, d20);
			
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d18, d19);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d6);
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d6);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d6);

				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5);
				return true;
			}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);

			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t5 = overrideBlockTexture;
				m = (t5 & 0xf) << 4;
				l = t5 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d13 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			if(!is45degree) {
				if(!isBottomMono) {
					if(overrideBlockTexture >= 0)
						t3 = overrideBlockTexture;
					m = (t3 & 0xf) << 4;
					l = t3 & RenderEngine.TEX;
					d3 = m / 256D;
					d4 = (m + 15.99D) / 256D;
					d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
					d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
				}
				tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
				tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);

				if(!isBottomMono) {
					if(overrideBlockTexture >= 0)
						t4 = overrideBlockTexture;
					m = (t4 & 0xf) << 4;
					l = t4 & RenderEngine.TEX;
					d3 = m / 256D;
					d4 = (m + 15.99D) / 256D;
					d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
					d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
				}
				tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
			return true;
		}
		//���������� ������� 1
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k - 1) && blockAccess.getBlockMetadata(i, j, k - 1) == 3 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i - 1, j, k) && blockAccess.getBlockMetadata(i - 1, j, k) == 0) {
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d3, d5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d4, d5);

			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d4, d5);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d3, d6);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d17, d19);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d18, d19);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d17, d19);
			/*
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d17, d19);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d18 - texel_hor * 15, d20);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15 + 0.0625, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d4, d5);

				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d3, d5);
				return true;
			}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t5 = overrideBlockTexture;
				m = (t5 & 0xf) << 4;
				l = t5 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t3 = overrideBlockTexture;
				m = (t3 & 0xf) << 4;
				l = t3 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d11 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d14 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t4 = overrideBlockTexture;
				m = (t4 & 0xf) << 4;
				l = t4 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			return true;
		}
		//���������� ������� 2
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k - 1) && blockAccess.getBlockMetadata(i, j, k - 1) == 1 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i + 1, j, k) && blockAccess.getBlockMetadata(i + 1, j, k) == 0) {
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d3, d5);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d4, d5);

			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d3, d5);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d3, d5);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d17, d19);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d17 + texel_hor * 15, d19);
			/*
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d17, d19);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d17, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d18 - texel_hor * 15, d20);
					
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15 + 0.0625, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d18, d19);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d3, d5);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d4, d6);
				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d3, d5);

				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d3, d5);
				return true;
			}

			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);

			tessellator.addVertexWithUV(d8, d12 + 0.5D - d15, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t5 = overrideBlockTexture;
				m = (t5 & 0xf) << 4;
				l = t5 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d13 + 0.5D - d15, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t3 = overrideBlockTexture;
				m = (t3 & 0xf) << 4;
				l = t3 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d11 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d14 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t4 = overrideBlockTexture;
				m = (t4 & 0xf) << 4;
				l = t4 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			return true;
		}
		//���������� ������� 3
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k + 1) && blockAccess.getBlockMetadata(i, j, k + 1) == 1 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i + 1, j, k) && blockAccess.getBlockMetadata(i + 1, j, k) == 2) {
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d3, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d3, d5);

			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d4, d6);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d17 + texel_hor * 15, d19);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d18, d19);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d17, d19);
			/*
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16, d10, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d17, d19);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d18 - texel_hor * 15, d20);
			
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15 + 0.0625, d10, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d18, d19);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d6);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d6);

				tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d3, d5);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d6);
				return true;
			}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);

			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t5 = overrideBlockTexture;
				m = (t5 & 0xf) << 4;
				l = t5 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d13 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t3 = overrideBlockTexture;
				m = (t3 & 0xf) << 4;
				l = t3 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d11 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t4 = overrideBlockTexture;
				m = (t4 & 0xf) << 4;
				l = t4 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			return true;
		}
		//���������� ������� 4
		if(blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i, j, k + 1) && blockAccess.getBlockMetadata(i, j, k + 1) == 3 && blockAccess.getBlockId(i, j, k) == blockAccess.getBlockId(i - 1, j, k) && blockAccess.getBlockMetadata(i - 1, j, k) == 2) {

			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d4, d5);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d4, d6);

			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d3, d6);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d18, d19);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d17, d19);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d18, d19);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16, d9, d17, d19);
			/*			
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16, d9, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d17, d19);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d17, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d18 - texel_hor * 15, d20);
			
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15 + 0.0625, d10, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16, d10, d17 + texel_hor * 15, d19);
			*/
			if(!isBottomValid) {
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d6);
				tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d6);

				tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d6);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d4, d5);
				return true;
			}
			t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
			if(overrideBlockTexture >= 0)
				t2 = overrideBlockTexture;
			m = (t2 & 0xf) << 4;
			l = t2 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);

			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t5 = overrideBlockTexture;
				m = (t5 & 0xf) << 4;
				l = t5 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d12 + 1.0D - d16 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t3 = overrideBlockTexture;
				m = (t3 & 0xf) << 4;
				l = t3 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d11 + 1.0D - d16 - 0.0625, d10, d4, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			if(!isBottomMono) {
				if(overrideBlockTexture >= 0)
					t4 = overrideBlockTexture;
				m = (t4 & 0xf) << 4;
				l = t4 & RenderEngine.TEX;
				d3 = m / 256D;
				d4 = (m + 15.99D) / 256D;
				d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
				d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
			}
			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 1.0D - d16 - 0.0625, d9, d3, d5 + texel_vert * 1 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 + 0.5D - d15, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			return true;
		}

		if(r == 0) {
			d11 = d1 + 1.0D - d16;
			d12 = d1 + 0.5D - d15;
			d13 = d1 + 0.5D - d15;
			d14 = d1 + 1.0D - d16;
			tessellator.addVertexWithUV(d8, d11, d10, d3, d5);
			tessellator.addVertexWithUV(d8, d12 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d13 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d14, d10, d4, d5);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 0.0625, d9, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d13, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.0625, d9, d18, d19 + texel_vert * 15);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11, d10, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d17, d19);
			tessellator.addVertexWithUV(d8, d12, d9, d17, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.0625, d9, d18 - texel_hor * 15, d20);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11, d10, d17, d19);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.0625, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d13, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d7, d14, d10, d17 + texel_hor * 15, d19);

		} else if(r == 1) {
			d11 = d1 + 0.5D - d15;
			d12 = d1 + 0.5D - d15;
			d13 = d1 + 1.0D - d16;
			d14 = d1 + 1.0D - d16;
			tessellator.addVertexWithUV(d8, d11 + 0.0625, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d12 + 0.0625, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d14, d10, d3, d5);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12 + 0.0625, d9, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d8, d12, d9, d18, d20);
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d18, d19);
			tessellator.addVertexWithUV(d7, d13, d9, d17 + texel_hor * 15, d19);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.0625, d10, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d8, d11, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d12, d9, d18, d20);
			tessellator.addVertexWithUV(d8, d12 + 0.0625, d9, d18, d19 + texel_vert * 15);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14, d10, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d17, d19);
			tessellator.addVertexWithUV(d8, d11, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.0625, d10, d18 - texel_hor * 15, d20);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13, d9, d18, d19);
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d14, d10, d17, d19);

		} else if(r == 2) {
			d11 = d1 + 0.5D - d15;
			d12 = d1 + 1.0D - d16;
			d13 = d1 + 1.0D - d16;
			d14 = d1 + 0.5D - d15;
			tessellator.addVertexWithUV(d8, d11 + 0.0625, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d12, d9, d4, d5);
			tessellator.addVertexWithUV(d7, d13, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d14 + 0.0625, d10, d3, d6);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12, d9, d18, d19);
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13, d9, d17, d19);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11 + 0.0625, d10, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d8, d11, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d18, d19);
			tessellator.addVertexWithUV(d8, d12, d9, d17 + texel_hor * 15, d19);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.0625, d10, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d7, d14, d10, d17, d20);
			tessellator.addVertexWithUV(d8, d11, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d11 + 0.0625, d10, d18, d19 + texel_vert * 15);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13, d9, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d17, d19);
			tessellator.addVertexWithUV(d7, d14, d10, d17, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.0625, d10, d18 - texel_hor * 15, d20);
		} else if(r == 3) {
			d11 = d1 + 1.0D - d16;
			d12 = d1 + 1.0D - d16;
			d13 = d1 + 0.5D - d15;
			d14 = d1 + 0.5D - d15;
			tessellator.addVertexWithUV(d8, d11, d10, d4, d5);
			tessellator.addVertexWithUV(d8, d12, d9, d3, d5);
			tessellator.addVertexWithUV(d7, d13 + 0.0625, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d14 + 0.0625, d10, d4, d6);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d8, d12, d9, d18 - texel_hor * 15, d19);
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d17, d19);
			tessellator.addVertexWithUV(d7, d13, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d13 + 0.0625, d9, d18 - texel_hor * 15, d20);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d8, d11, d10, d18, d19);
			tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d18, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d17, d20 - texel_vert * 15);
			tessellator.addVertexWithUV(d8, d12, d9, d17, d19);

			tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
			tessellator.addVertexWithUV(d7, d14 + 0.0625, d10, d17 + texel_hor * 15, d20);
			tessellator.addVertexWithUV(d7, d14, d10, d18, d20);
			tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d18, d19);
			tessellator.addVertexWithUV(d8, d11, d10, d17 + texel_hor * 15, d19);

			tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
			tessellator.addVertexWithUV(d7, d13 + 0.0625, d9, d17, d19 + texel_vert * 15);
			tessellator.addVertexWithUV(d7, d13, d9, d17, d20);
			tessellator.addVertexWithUV(d7, d14, d10, d18, d20);
			tessellator.addVertexWithUV(d7, d14 + 0.0625, d10, d18, d19 + texel_vert * 15);

		}
		if(!isBottomValid) {
			if(r == 0) {
				tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d4, d5);
				tessellator.addVertexWithUV(d7, d13, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d12, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d3, d5);
				return true;
			}
			if(r == 1) {
				tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d3, d5);
				tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d8, d12, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d11, d10, d3, d6);
				return true;
			}
			if(r == 2) {
				tessellator.addVertexWithUV(d7, d14, d10, d3, d6);
				tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d4, d5);
				tessellator.addVertexWithUV(d8, d11, d10, d4, d6);
				return true;
			}
			if(r == 3) {
				tessellator.addVertexWithUV(d7, d14, d10, d4, d6);
				tessellator.addVertexWithUV(d7, d13, d9, d3, d6);
				tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d3, d5);
				tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d4, d5);
				return true;
			}
			return true;
		}
		//side 2
		t2 = bottom.getBlockTextureFromSideAndMetadata(2, r_1);
		if(overrideBlockTexture >= 0)
			t2 = overrideBlockTexture;
		m = (t2 & 0xf) << 4;
		l = t2 & RenderEngine.TEX;
		d3 = m / 256D;
		d4 = (m + 15.99D) / 256D;
		d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
		d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
		tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
		if(r == 0) {
			if(!is45degree) {
				tessellator.addVertexWithUV(d8, d12, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
				tessellator.addVertexWithUV(d7, d13, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
		} else if(r == 1) {
			tessellator.addVertexWithUV(d8, d12, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		} else if(r == 2) {
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		} else if(r == 3) {
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d7, d13, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
		}
		//side 5
		if(!isBottomMono) {
			if(overrideBlockTexture >= 0)
				t5 = overrideBlockTexture;
			m = (t5 & 0xf) << 4;
			l = t5 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
		}
		tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
		if(r == 0) {
			tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d13, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
		} else if(r == 1) {
			if(!is45degree) {
				tessellator.addVertexWithUV(d8, d11, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
				tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
				tessellator.addVertexWithUV(d8, d12, d9, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
		} else if(r == 2) {
			tessellator.addVertexWithUV(d8, d11, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d13 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		} else if(r == 3) {
			tessellator.addVertexWithUV(d8, d11 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d8, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d9, d4, d6);
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d9, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		}

		//side 3
		if(!isBottomMono) {
			if(overrideBlockTexture >= 0)
				t3 = overrideBlockTexture;
			m = (t3 & 0xf) << 4;
			l = t3 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
		}
		tessellator.setColorOpaque_F(f1 * f4, f2 * f4, f3 * f4);
		if(r == 0) {
			tessellator.addVertexWithUV(d7, d11 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d14 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		}
		if(r == 1) {
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d10, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d12, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
		}
		if(r == 2) {
			if(!is45degree) {
				tessellator.addVertexWithUV(d7, d14, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
				tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
				tessellator.addVertexWithUV(d8, d11, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
		}
		if(r == 3) {
			tessellator.addVertexWithUV(d7, d13, d10, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d10, d3, d6);
			tessellator.addVertexWithUV(d8, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d8, d12 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		}

		//side 4
		if(!isBottomMono) {
			if(overrideBlockTexture >= 0)
				t4 = overrideBlockTexture;
			m = (t4 & 0xf) << 4;
			l = t4 & RenderEngine.TEX;
			d3 = m / 256D;
			d4 = (m + 15.99D) / 256D;
			d5 = l / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = (l + 15.99D) / RenderEngine.TERRAIN_HEIGHT_D;
		}
		tessellator.setColorOpaque_F(f1 * f5, f2 * f5, f3 * f5);
		if(r == 0) {
			tessellator.addVertexWithUV(d7, d12, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		} else if(r == 1) {
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14 - 0.0625, d10, d4, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
		} else if(r == 2) {
			tessellator.addVertexWithUV(d7, d13 - 0.0625, d9, d3, d5 + texel_vert * 1 + d16 * 16 * texel_vert);
			tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
			tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
			tessellator.addVertexWithUV(d7, d14, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
		} else if(r == 3) {
			if(!is45degree) {
				tessellator.addVertexWithUV(d7, d13, d9, d3, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
				tessellator.addVertexWithUV(d7, d1, d9, d3, d6);
				tessellator.addVertexWithUV(d7, d1, d10, d4, d6);
				tessellator.addVertexWithUV(d7, d14, d10, d4, d5 + texel_vert * 8 + d15 * 16 * texel_vert);
			}
		}
		return true;
	}

	private boolean renderBlockSpikes(Block block, int i, int j, int k) {
		double d = i;
		double d1 = j;
		double d2 = k;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int l1 = block.colorMultiplier(blockAccess, i, j, k);
		float f1 = (l1 >> 16 & 0xff) / 255F;
		float f2 = (l1 >> 8 & 0xff) / 255F;
		float f3 = (l1 & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f5 = (f1 * 30F + f2 * 70F) / 100F;
			float f6 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		int t = block.getBlockTextureFromSideAndMetadata(0, 0);
		int m = (t & 0xf) << 4;
		int l = t & RenderEngine.TEX;
		double d3 = m / 256F;
		double d4 = (m + 15.99F) / 256F;
		double d5 = l / RenderEngine.TERRAIN_HEIGHT_F;
		double d6 = (l + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		double d7 = (d + 0.5D);
		double d8 = d + 0.5D;
		double d9 = (d2 + 0.5D);
		double d10 = d2 + 0.5D;
		double d11 = (d + 0.5D) - 0.50D;
		double d12 = d + 0.5D + 0.50D;
		double d13 = (d2 + 0.5D) - 0.50D;
		double d14 = d2 + 0.5D + 0.50D;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9 + 0.5D, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9 + 0.5D, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10 - 0.5D, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10 - 0.5D, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10 - 0.5D, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10 - 0.5D, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9 + 0.5D, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9 + 0.5D, d4, d5);

		tessellator.addVertexWithUV(d7 + 0.5D, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8 - 0.5D, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8 - 0.5D, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8 - 0.5D, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7 + 0.5D, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7 + 0.5D, d1 + 1.0D, d10, d4, d5);

		tessellator.addVertexWithUV(d12, d1 + 0.5D, d14, d3, d5);
		tessellator.addVertexWithUV(d12, d1 + 0.5D, d13, d3, d6);
		tessellator.addVertexWithUV(d11, d1 + 0.5D, d13, d4, d6);
		tessellator.addVertexWithUV(d11, d1 + 0.5D, d14, d4, d5);
		tessellator.addVertexWithUV(d11, d1 + 0.5D, d14, d3, d5);
		tessellator.addVertexWithUV(d11, d1 + 0.5D, d13, d3, d6);
		tessellator.addVertexWithUV(d12, d1 + 0.5D, d13, d4, d6);
		tessellator.addVertexWithUV(d12, d1 + 0.5D, d14, d4, d5);
		return true;
	}

	private boolean renderBlockCocoa(BlockCocoa BlockCocoa, int x, int y, int z) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(BlockCocoa.getMixedBrightnessForBlock(blockAccess, x, y, z));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int i = blockAccess.getBlockMetadata(x, y, z);
		int j = BlockCocoa.getBlockTextureFromSide(1);
		int k = BlockDirectional.getDirection(i);
		int l = net.minecraft.src.BlockCocoa.func_72219_c(i);
		j = (j + 2) - l;
		int i1 = 4 + l * 2;
		int j1 = 5 + l * 2;
		int k1 = (j & 0xf) << 4;
		int l1 = j & RenderEngine.TEX;
		double d = 15D - i1;
		double d1 = 15D;
		double d2 = 4D;
		double d3 = 4D + j1;
		double d4 = (k1 + d) / 256D;
		double d5 = ((k1 + d1) - 0.01D) / 256D;
		double d6 = (l1 + d2) / RenderEngine.TERRAIN_HEIGHT_D;
		double d7 = ((l1 + d3) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d8 = 0.0D;
		double d9 = 0.0D;

		switch(k) {
		case 2:
			d8 = 8D - (i1 / 2);
			d9 = 1.0D;
			break;

		case 0:
			d8 = 8D - (i1 / 2);
			d9 = 15D - i1;
			break;

		case 3:
			d8 = 15D - i1;
			d9 = 8D - (i1 / 2);
			break;

		case 1:
			d8 = 1.0D;
			d9 = 8D - (i1 / 2);
			break;
		}

		double d10 = x + d8 / 16D;
		double d11 = x + (d8 + i1) / 16D;
		double d12 = y + (12D - j1) / 16D;
		double d13 = y + 0.75D;
		double d14 = z + d9 / 16D;
		double d15 = z + (d9 + i1) / 16D;
		tessellator.addVertexWithUV(d10, d12, d14, d4, d7);
		tessellator.addVertexWithUV(d10, d12, d15, d5, d7);
		tessellator.addVertexWithUV(d10, d13, d15, d5, d6);
		tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
		tessellator.addVertexWithUV(d11, d12, d15, d4, d7);
		tessellator.addVertexWithUV(d11, d12, d14, d5, d7);
		tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
		tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
		tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
		tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
		tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
		tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
		tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
		tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
		tessellator.addVertexWithUV(d11, d13, d15, d5, d6);
		tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
		int i2 = i1;

		if(l >= 2) {
			i2--;
		}

		d4 = (k1 + 0) / 256F;
		d5 = ((k1 + i2) - 0.01D) / 256D;
		d6 = (l1 + 0) / RenderEngine.TERRAIN_HEIGHT_F;
		d7 = ((l1 + i2) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		tessellator.addVertexWithUV(d10, d13, d15, d4, d7);
		tessellator.addVertexWithUV(d11, d13, d15, d5, d7);
		tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
		tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
		tessellator.addVertexWithUV(d10, d12, d14, d4, d6);
		tessellator.addVertexWithUV(d11, d12, d14, d5, d6);
		tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
		tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
		d4 = (k1 + 12) / 256F;
		d5 = ((k1 + 16) - 0.01D) / 256D;
		d6 = (l1 + 0) / RenderEngine.TERRAIN_HEIGHT_D;
		d7 = ((l1 + 4) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		d8 = 8D;
		d9 = 0.0D;

		switch(k) {
		case 2:
			d8 = 8D;
			d9 = 0.0D;
			break;

		case 0:
			d8 = 8D;
			d9 = 12D;
			double d16 = d4;
			d4 = d5;
			d5 = d16;
			break;

		case 3:
			d8 = 12D;
			d9 = 8D;
			double d17 = d4;
			d4 = d5;
			d5 = d17;
			break;

		case 1:
			d8 = 0.0D;
			d9 = 8D;
			break;
		}

		d10 = x + d8 / 16D;
		d11 = x + (d8 + 4D) / 16D;
		d12 = y + 0.75D;
		d13 = y + 1.0D;
		d14 = z + d9 / 16D;
		d15 = z + (d9 + 4D) / 16D;

		if(k == 2 || k == 0) {
			tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
			tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
			tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
			tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
			tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
			tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
			tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
		} else if(k == 1 || k == 3) {
			tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
			tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
			tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
			tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
			tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
			tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
			tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
			tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
		}

		return true;
	}

	private boolean renderBlockRotatedWood(Block block, int l, int j, int k) {
		int i1 = this.blockAccess.getBlockMetadata(l, j, k);
		int i2 = i1 & 0xC;

		if(i2 == 4) {
			uvRotateEast = 1;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 1;
		} else if(i2 == 8) {
			uvRotateSouth = 1;
			uvRotateNorth = 1;

		}

		boolean bool = renderStandardBlock(block, l, j, k);

		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateSouth = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;

		return bool;
	}

	// return l != 0 ? l != 4 ? l != 13 ? l != 1 ? l != 19 ? l != 23 ? l != 6 ?
	// l != 2 ? l != 3 ? l != 5 ? l != 8 ? l != 7 ? l != 9 ? l != 10 ? l != 27 ?
	// l != 11 ? l != 12 ? l != 14 ? l != 15 ? l != 16 ? l != 17 ? l != 18 ? l
	// != 20 ? l != 21 ? l != 24 ? l != 25 ? l != 26 ?
	// ModLoader.RenderWorldBlock(this, blockAccess, i, j, k, block, l) :
	// renderBlockEndPortalFrame(block, i, j, k) :
	// renderBlockBrewingStand((BlockBrewingStand) block, i, j, k) :
	// renderBlockCauldron((BlockCauldron) block, i, j, k) :
	// renderBlockFenceGate((BlockFenceGate) block, i, j, k) :
	// renderBlockVine(block, i, j, k) : renderBlockPane((BlockPane) block, i,
	// j, k) : renderPistonExtension(block, i, j, k, true) :
	// renderPistonBase(block, i, j, k, false) : renderBlockRepeater(block, i,
	// j, k) : renderBlockBed(block, i, j, k) : renderBlockLever(block, i, j, k)
	// : renderBlockFence((BlockFence) block, i, j, k) :
	// func_41088_a((BlockDragonEgg) block, i, j, k) : renderBlockStairs(block,
	// i, j, k) : renderBlockMinecartTrack((BlockRail) block, i, j, k) :
	// renderBlockDoor(block, i, j, k) : renderBlockLadder(block, i, j, k) :
	// renderBlockRedstoneWire(block, i, j, k) : renderBlockFire(block, i, j, k)
	// : renderBlockTorch(block, i, j, k) : renderBlockCrops(block, i, j, k) :
	// renderBlockLilyPad(block, i, j, k) : renderBlockStem(block, i, j, k) :
	// renderBlockReed(block, i, j, k) : renderBlockCactus(block, i, j, k) :
	// renderBlockFluids(block, i, j, k) : renderStandardBlock(block, i, j, k);

	public boolean renderBlockEndPortalFrame(Block block, int i, int j, int k) {
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = l & 3;
		if(i1 == 0) {
			uvRotateTop = 3;
		} else if(i1 == 3) {
			uvRotateTop = 1;
		} else if(i1 == 1) {
			uvRotateTop = 2;
		}
		if(!BlockEndPortalFrame.func_40212_d(l)) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
			renderStandardBlock(block, i, j, k);
			block.setBlockBoundsForItemRender();
			uvRotateTop = 0;
			return true;
		} else {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
			renderStandardBlock(block, i, j, k);
			overrideBlockTexture = 174;
			block.setBlockBounds(0.25F, 0.8125F, 0.25F, 0.75F, 1.0F, 0.75F);
			renderStandardBlock(block, i, j, k);
			clearOverrideBlockTexture();
			block.setBlockBoundsForItemRender();
			uvRotateTop = 0;
			return true;
		}
	}

	public boolean renderBlockBed(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = BlockBed.getDirectionFromMetadata(l);
		boolean flag = BlockBed.isBlockFootOfBed(l);
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		int j1 = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
		tessellator.setBrightness(j1);
		tessellator.setColorOpaque_F(f, f, f);
		int k1 = block.getBlockTexture(blockAccess, i, j, k, 0);
		int l1 = (k1 & 0xf) << 4;
		int i2 = k1 & RenderEngine.TEX;
		double d = l1 / 256F;
		double d1 = ((l1 + 16) - 0.01D) / 256D;
		double d2 = i2 / RenderEngine.TERRAIN_HEIGHT_F;
		double d3 = ((i2 + 16) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d4 = i + block.minX;
		double d5 = i + block.maxX;
		double d6 = j + block.minY + 0.1875D;
		double d7 = k + block.minZ;
		double d8 = k + block.maxZ;
		tessellator.addVertexWithUV(d4, d6, d8, d, d3);
		tessellator.addVertexWithUV(d4, d6, d7, d, d2);
		tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
		tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j + 1, k));
		tessellator.setColorOpaque_F(f1, f1, f1);
		k1 = block.getBlockTexture(blockAccess, i, j, k, 1);
		l1 = (k1 & 0xf) << 4;
		i2 = k1 & RenderEngine.TEX;
		d = l1 / 256F;
		d1 = ((l1 + 16) - 0.01D) / 256D;
		d2 = i2 / RenderEngine.TERRAIN_HEIGHT_F;
		d3 = ((i2 + 16) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		d4 = d;
		d5 = d1;
		d6 = d2;
		d7 = d2;
		d8 = d;
		double d9 = d1;
		double d10 = d3;
		double d11 = d3;
		if(i1 == 0) {
			d5 = d;
			d6 = d3;
			d8 = d1;
			d11 = d2;
		} else if(i1 == 2) {
			d4 = d1;
			d7 = d3;
			d9 = d;
			d10 = d2;
		} else if(i1 == 3) {
			d4 = d1;
			d7 = d3;
			d9 = d;
			d10 = d2;
			d5 = d;
			d6 = d3;
			d8 = d1;
			d11 = d2;
		}
		double d12 = i + block.minX;
		double d13 = i + block.maxX;
		double d14 = j + block.maxY;
		double d15 = k + block.minZ;
		double d16 = k + block.maxZ;
		tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
		tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
		tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
		tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
		k1 = Direction.headInvisibleFace[i1];
		if(flag) {
			k1 = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[i1]];
		}
		byte byte0 = 4;
		switch(i1) {
		case 0: // '\0'
			byte0 = 5;
			break;

		case 1: // '\001'
			byte0 = 3;
			break;

		case 3: // '\003'
			byte0 = 2;
			break;
		}
		if(k1 != 2 && (renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2))) {
			tessellator.setBrightness(block.minZ <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k - 1) : j1);
			tessellator.setColorOpaque_F(f2, f2, f2);
			flipTexture = byte0 == 2;
			renderEastFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 2));
		}
		if(k1 != 3 && (renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3))) {
			tessellator.setBrightness(block.maxZ >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k + 1) : j1);
			tessellator.setColorOpaque_F(f2, f2, f2);
			flipTexture = byte0 == 3;
			renderWestFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 3));
		}
		if(k1 != 4 && (renderAllFaces || block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4))) {
			tessellator.setBrightness(block.minZ <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i - 1, j, k) : j1);
			tessellator.setColorOpaque_F(f3, f3, f3);
			flipTexture = byte0 == 4;
			renderNorthFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 4));
		}
		if(k1 != 5 && (renderAllFaces || block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5))) {
			tessellator.setBrightness(block.maxZ >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i + 1, j, k) : j1);
			tessellator.setColorOpaque_F(f3, f3, f3);
			flipTexture = byte0 == 5;
			renderSouthFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 5));
		}
		flipTexture = false;
		return true;
	}

	public boolean renderBlockBrewingStand(BlockBrewingStand blockbrewingstand, int i, int j, int k) {
		blockbrewingstand.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
		renderStandardBlock(blockbrewingstand, i, j, k);
		overrideBlockTexture = 156;
		blockbrewingstand.setBlockBounds(0.5625F, 0.0F, 0.3125F, 0.9375F, 0.125F, 0.6875F);
		renderStandardBlock(blockbrewingstand, i, j, k);
		blockbrewingstand.setBlockBounds(0.125F, 0.0F, 0.0625F, 0.5F, 0.125F, 0.4375F);
		renderStandardBlock(blockbrewingstand, i, j, k);
		blockbrewingstand.setBlockBounds(0.125F, 0.0F, 0.5625F, 0.5F, 0.125F, 0.9375F);
		renderStandardBlock(blockbrewingstand, i, j, k);
		clearOverrideBlockTexture();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(blockbrewingstand.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int l = blockbrewingstand.colorMultiplier(blockAccess, i, j, k);
		float f1 = (l >> 16 & 0xff) / 255F;
		float f2 = (l >> 8 & 0xff) / 255F;
		float f3 = (l & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f5 = (f1 * 30F + f2 * 70F) / 100F;
			float f6 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		int i1 = blockbrewingstand.getBlockTextureFromSideAndMetadata(0, 0);
		if(overrideBlockTexture >= 0) {
			i1 = overrideBlockTexture;
		}
		int j1 = (i1 & 0xf) << 4;
		int k1 = i1 & RenderEngine.TEX;
		double d = k1 / 256F;
		double d1 = (k1 + 15.99F) / 256F;
		int l1 = blockAccess.getBlockMetadata(i, j, k);
		for(int i2 = 0; i2 < 3; i2++) {
			double d2 = (i2 * 3.1415926535897931D * 2D) / 3D + 1.5707963267948966D;
			double d3 = (j1 + 8F) / RenderEngine.TERRAIN_HEIGHT_F;
			double d4 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			if((l1 & 1 << i2) != 0) {
				d3 = (j1 + 7.99F) / RenderEngine.TERRAIN_HEIGHT_F;
				d4 = (j1 + 0.0F) / RenderEngine.TERRAIN_HEIGHT_F;
			}
			double d5 = i + 0.5D;
			double d6 = i + 0.5D + (Math.sin(d2) * 8D) / 16D;
			double d7 = k + 0.5D;
			double d8 = k + 0.5D + (Math.cos(d2) * 8D) / 16D;
			tessellator.addVertexWithUV(d5, j + 1, d7, d3, d);
			tessellator.addVertexWithUV(d5, j + 0, d7, d3, d1);
			tessellator.addVertexWithUV(d6, j + 0, d8, d4, d1);
			tessellator.addVertexWithUV(d6, j + 1, d8, d4, d);
			tessellator.addVertexWithUV(d6, j + 1, d8, d4, d);
			tessellator.addVertexWithUV(d6, j + 0, d8, d4, d1);
			tessellator.addVertexWithUV(d5, j + 0, d7, d3, d1);
			tessellator.addVertexWithUV(d5, j + 1, d7, d3, d);
		}

		blockbrewingstand.setBlockBoundsForItemRender();
		return true;
	}

	public boolean renderBlockCauldron(BlockCauldron blockcauldron, int i, int j, int k) {
		renderStandardBlock(blockcauldron, i, j, k);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(blockcauldron.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int l = blockcauldron.colorMultiplier(blockAccess, i, j, k);
		float f1 = (l >> 16 & 0xff) / 255F;
		float f2 = (l >> 8 & 0xff) / 255F;
		float f3 = (l & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f6 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f4 = (f1 * 30F + f2 * 70F) / 100F;
			float f7 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f6;
			f2 = f4;
			f3 = f7;
		}
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		char c = '\232';
		float f5 = 0.125F;
		renderSouthFace(blockcauldron, (i - 1.0F) + f5, j, k, c);
		renderNorthFace(blockcauldron, (i + 1.0F) - f5, j, k, c);
		renderWestFace(blockcauldron, i, j, (k - 1.0F) + f5, c);
		renderEastFace(blockcauldron, i, j, (k + 1.0F) - f5, c);
		char c1 = '\213';
		renderTopFace(blockcauldron, i, (j - 1.0F) + 0.25F, k, c1);
		renderBottomFace(blockcauldron, i, (j + 1.0F) - 0.75F, k, c1);
		int i1 = blockAccess.getBlockMetadata(i, j, k);
		if(i1 > 0) {
			char c2 = '\315';
			if(i1 > 3) {
				i1 = 3;
			}
			renderTopFace(blockcauldron, i, (j - 1.0F) + (6F + i1 * 3F) / 16F, k, c2);
		}
		return true;
	}

	public boolean renderBlockTorch(Block block, int i, int j, int k) {
		int l = blockAccess.getBlockMetadata(i, j, k);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d = 0.40000000596046448D;
		double d1 = 0.5D - d;
		double d2 = 0.20000000298023224D;
		if(l == 1) {
			renderTorchAtAngle(block, i - d1, j + d2, k, -d, 0.0D);
		} else if(l == 2) {
			renderTorchAtAngle(block, i + d1, j + d2, k, d, 0.0D);
		} else if(l == 3) {
			renderTorchAtAngle(block, i, j + d2, k - d1, 0.0D, -d);
		} else if(l == 4) {
			renderTorchAtAngle(block, i, j + d2, k + d1, 0.0D, d);
		} else {
			renderTorchAtAngle(block, i, j, k, 0.0D, 0.0D);
		}
		return true;
	}

	public boolean renderBlockRepeater(Block block, int i, int j, int k) {
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = l & 3;
		int j1 = (l & 0xc) >> 2;
		renderStandardBlock(block, i, j, k);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double d = -0.1875D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		switch(i1) {
		case 0: // '\0'
			d4 = -0.3125D;
			d2 = BlockRedstoneRepeater.repeaterTorchOffset[j1];
			break;

		case 1: // '\001'
			d3 = 0.3125D;
			d1 = -BlockRedstoneRepeater.repeaterTorchOffset[j1];
			break;

		case 2: // '\002'
			d4 = 0.3125D;
			d2 = -BlockRedstoneRepeater.repeaterTorchOffset[j1];
			break;

		case 3: // '\003'
			d3 = -0.3125D;
			d1 = BlockRedstoneRepeater.repeaterTorchOffset[j1];
			break;
		}
		renderTorchAtAngle(block, i + d1, j + d, k + d2, 0.0D, 0.0D);
		renderTorchAtAngle(block, i + d3, j + d, k + d4, 0.0D, 0.0D);
		int k1 = block.getBlockTextureFromSide(1);
		int l1 = (k1 & 0xf) << 4;
		int i2 = k1 & RenderEngine.TEX;
		double d5 = l1 / 256F;
		double d6 = (l1 + 15.99F) / 256F;
		double d7 = i2 / RenderEngine.TERRAIN_HEIGHT_F;
		double d8 = (i2 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		double d9 = 0.125D;
		double d10 = i + 1;
		double d11 = i + 1;
		double d12 = i + 0;
		double d13 = i + 0;
		double d14 = k + 0;
		double d15 = k + 1;
		double d16 = k + 1;
		double d17 = k + 0;
		double d18 = j + d9;
		if(i1 == 2) {
			d10 = d11 = i + 0;
			d12 = d13 = i + 1;
			d14 = d17 = k + 1;
			d15 = d16 = k + 0;
		} else if(i1 == 3) {
			d10 = d13 = i + 0;
			d11 = d12 = i + 1;
			d14 = d15 = k + 0;
			d16 = d17 = k + 1;
		} else if(i1 == 1) {
			d10 = d13 = i + 1;
			d11 = d12 = i + 0;
			d14 = d15 = k + 1;
			d16 = d17 = k + 0;
		}
		tessellator.addVertexWithUV(d13, d18, d17, d5, d7);
		tessellator.addVertexWithUV(d12, d18, d16, d5, d8);
		tessellator.addVertexWithUV(d11, d18, d15, d6, d8);
		tessellator.addVertexWithUV(d10, d18, d14, d6, d7);
		return true;
	}

	public void renderPistonBaseAllFaces(Block block, int i, int j, int k) {
		renderAllFaces = true;
		renderPistonBase(block, i, j, k, true);
		renderAllFaces = false;
	}

	public boolean renderPistonBase(Block block, int i, int j, int k, boolean flag) {
		int l = blockAccess.getBlockMetadata(i, j, k);
		boolean flag1 = flag || (l & 8) != 0;
		int i1 = BlockPistonBase.getOrientation(l);
		if(flag1) {
			switch(i1) {
			case 0: // '\0'
				uvRotateEast = 3;
				uvRotateWest = 3;
				uvRotateSouth = 3;
				uvRotateNorth = 3;
				block.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;

			case 1: // '\001'
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;

			case 2: // '\002'
				uvRotateSouth = 1;
				uvRotateNorth = 2;
				block.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;

			case 3: // '\003'
				uvRotateSouth = 2;
				uvRotateNorth = 1;
				uvRotateTop = 3;
				uvRotateBottom = 3;
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;

			case 4: // '\004'
				uvRotateEast = 1;
				uvRotateWest = 2;
				uvRotateTop = 2;
				uvRotateBottom = 1;
				block.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;

			case 5: // '\005'
				uvRotateEast = 2;
				uvRotateWest = 1;
				uvRotateTop = 1;
				uvRotateBottom = 2;
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
				break;
			}
			renderStandardBlock(block, i, j, k);
			uvRotateEast = 0;
			uvRotateWest = 0;
			uvRotateSouth = 0;
			uvRotateNorth = 0;
			uvRotateTop = 0;
			uvRotateBottom = 0;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			switch(i1) {
			case 0: // '\0'
				uvRotateEast = 3;
				uvRotateWest = 3;
				uvRotateSouth = 3;
				uvRotateNorth = 3;
				break;

			case 2: // '\002'
				uvRotateSouth = 1;
				uvRotateNorth = 2;
				break;

			case 3: // '\003'
				uvRotateSouth = 2;
				uvRotateNorth = 1;
				uvRotateTop = 3;
				uvRotateBottom = 3;
				break;

			case 4: // '\004'
				uvRotateEast = 1;
				uvRotateWest = 2;
				uvRotateTop = 2;
				uvRotateBottom = 1;
				break;

			case 5: // '\005'
				uvRotateEast = 2;
				uvRotateWest = 1;
				uvRotateTop = 1;
				uvRotateBottom = 2;
				break;
			}
			renderStandardBlock(block, i, j, k);
			uvRotateEast = 0;
			uvRotateWest = 0;
			uvRotateSouth = 0;
			uvRotateNorth = 0;
			uvRotateTop = 0;
			uvRotateBottom = 0;
		}
		return true;
	}

	public void renderPistonRodUD(double d, double d1, double d2, double d3, double d4, double d5, float f, double d6) {
		int i = 108;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		Tessellator tessellator = Tessellator.instance;
		double d7 = (j + 0) / 256F;
		double d8 = (k + 0) / RenderEngine.TERRAIN_HEIGHT_F;
		double d9 = ((j + d6) - 0.01D) / 256D;
		double d10 = ((k + 4F) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		tessellator.setColorOpaque_F(f, f, f);
		tessellator.addVertexWithUV(d, d3, d4, d9, d8);
		tessellator.addVertexWithUV(d, d2, d4, d7, d8);
		tessellator.addVertexWithUV(d1, d2, d5, d7, d10);
		tessellator.addVertexWithUV(d1, d3, d5, d9, d10);
	}

	public void renderPistonRodSN(double d, double d1, double d2, double d3, double d4, double d5, float f, double d6) {
		int i = 108;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		Tessellator tessellator = Tessellator.instance;
		double d7 = (j + 0) / 256F;
		double d8 = (k + 0) / RenderEngine.TERRAIN_HEIGHT_F;
		double d9 = ((j + d6) - 0.01D) / 256D;
		double d10 = ((k + 4F) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		tessellator.setColorOpaque_F(f, f, f);
		tessellator.addVertexWithUV(d, d2, d5, d9, d8);
		tessellator.addVertexWithUV(d, d2, d4, d7, d8);
		tessellator.addVertexWithUV(d1, d3, d4, d7, d10);
		tessellator.addVertexWithUV(d1, d3, d5, d9, d10);
	}

	public void renderPistonRodEW(double d, double d1, double d2, double d3, double d4, double d5, float f, double d6) {
		int i = 108;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		Tessellator tessellator = Tessellator.instance;
		double d7 = (j + 0) / 256F;
		double d8 = (k + 0) / RenderEngine.TERRAIN_HEIGHT_F;
		double d9 = ((j + d6) - 0.01D) / 256D;
		double d10 = ((k + 4F) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		tessellator.setColorOpaque_F(f, f, f);
		tessellator.addVertexWithUV(d1, d2, d4, d9, d8);
		tessellator.addVertexWithUV(d, d2, d4, d7, d8);
		tessellator.addVertexWithUV(d, d3, d5, d7, d10);
		tessellator.addVertexWithUV(d1, d3, d5, d9, d10);
	}

	public void renderPistonExtensionAllFaces(Block block, int i, int j, int k, boolean flag) {
		renderAllFaces = true;
		renderPistonExtension(block, i, j, k, flag);
		renderAllFaces = false;
	}

	public boolean renderPistonExtension(Block block, int i, int j, int k, boolean flag) {
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = BlockPistonExtension.func_31050_c(l);
		float f = block.getBlockBrightness(blockAccess, i, j, k);
		float f1 = flag ? 1.0F : 0.5F;
		double d = flag ? 16D : 8D;
		switch(i1) {
		case 0: // '\0'
			uvRotateEast = 3;
			uvRotateWest = 3;
			uvRotateSouth = 3;
			uvRotateNorth = 3;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			renderStandardBlock(block, i, j, k);
			renderPistonRodUD(i + 0.375F, i + 0.625F, j + 0.25F, j + 0.25F + f1, k + 0.625F, k + 0.625F, f * 0.8F, d);
			renderPistonRodUD(i + 0.625F, i + 0.375F, j + 0.25F, j + 0.25F + f1, k + 0.375F, k + 0.375F, f * 0.8F, d);
			renderPistonRodUD(i + 0.375F, i + 0.375F, j + 0.25F, j + 0.25F + f1, k + 0.375F, k + 0.625F, f * 0.6F, d);
			renderPistonRodUD(i + 0.625F, i + 0.625F, j + 0.25F, j + 0.25F + f1, k + 0.625F, k + 0.375F, f * 0.6F, d);
			break;

		case 1: // '\001'
			block.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderStandardBlock(block, i, j, k);
			renderPistonRodUD(i + 0.375F, i + 0.625F, ((j - 0.25F) + 1.0F) - f1, (j - 0.25F) + 1.0F, k + 0.625F, k + 0.625F, f * 0.8F, d);
			renderPistonRodUD(i + 0.625F, i + 0.375F, ((j - 0.25F) + 1.0F) - f1, (j - 0.25F) + 1.0F, k + 0.375F, k + 0.375F, f * 0.8F, d);
			renderPistonRodUD(i + 0.375F, i + 0.375F, ((j - 0.25F) + 1.0F) - f1, (j - 0.25F) + 1.0F, k + 0.375F, k + 0.625F, f * 0.6F, d);
			renderPistonRodUD(i + 0.625F, i + 0.625F, ((j - 0.25F) + 1.0F) - f1, (j - 0.25F) + 1.0F, k + 0.625F, k + 0.375F, f * 0.6F, d);
			break;

		case 2: // '\002'
			uvRotateSouth = 1;
			uvRotateNorth = 2;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			renderStandardBlock(block, i, j, k);
			renderPistonRodSN(i + 0.375F, i + 0.375F, j + 0.625F, j + 0.375F, k + 0.25F, k + 0.25F + f1, f * 0.6F, d);
			renderPistonRodSN(i + 0.625F, i + 0.625F, j + 0.375F, j + 0.625F, k + 0.25F, k + 0.25F + f1, f * 0.6F, d);
			renderPistonRodSN(i + 0.375F, i + 0.625F, j + 0.375F, j + 0.375F, k + 0.25F, k + 0.25F + f1, f * 0.5F, d);
			renderPistonRodSN(i + 0.625F, i + 0.375F, j + 0.625F, j + 0.625F, k + 0.25F, k + 0.25F + f1, f, d);
			break;

		case 3: // '\003'
			uvRotateSouth = 2;
			uvRotateNorth = 1;
			uvRotateTop = 3;
			uvRotateBottom = 3;
			block.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
			renderStandardBlock(block, i, j, k);
			renderPistonRodSN(i + 0.375F, i + 0.375F, j + 0.625F, j + 0.375F, ((k - 0.25F) + 1.0F) - f1, (k - 0.25F) + 1.0F, f * 0.6F, d);
			renderPistonRodSN(i + 0.625F, i + 0.625F, j + 0.375F, j + 0.625F, ((k - 0.25F) + 1.0F) - f1, (k - 0.25F) + 1.0F, f * 0.6F, d);
			renderPistonRodSN(i + 0.375F, i + 0.625F, j + 0.375F, j + 0.375F, ((k - 0.25F) + 1.0F) - f1, (k - 0.25F) + 1.0F, f * 0.5F, d);
			renderPistonRodSN(i + 0.625F, i + 0.375F, j + 0.625F, j + 0.625F, ((k - 0.25F) + 1.0F) - f1, (k - 0.25F) + 1.0F, f, d);
			break;

		case 4: // '\004'
			uvRotateEast = 1;
			uvRotateWest = 2;
			uvRotateTop = 2;
			uvRotateBottom = 1;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			renderStandardBlock(block, i, j, k);
			renderPistonRodEW(i + 0.25F, i + 0.25F + f1, j + 0.375F, j + 0.375F, k + 0.625F, k + 0.375F, f * 0.5F, d);
			renderPistonRodEW(i + 0.25F, i + 0.25F + f1, j + 0.625F, j + 0.625F, k + 0.375F, k + 0.625F, f, d);
			renderPistonRodEW(i + 0.25F, i + 0.25F + f1, j + 0.375F, j + 0.625F, k + 0.375F, k + 0.375F, f * 0.6F, d);
			renderPistonRodEW(i + 0.25F, i + 0.25F + f1, j + 0.625F, j + 0.375F, k + 0.625F, k + 0.625F, f * 0.6F, d);
			break;

		case 5: // '\005'
			uvRotateEast = 2;
			uvRotateWest = 1;
			uvRotateTop = 1;
			uvRotateBottom = 2;
			block.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderStandardBlock(block, i, j, k);
			renderPistonRodEW(((i - 0.25F) + 1.0F) - f1, (i - 0.25F) + 1.0F, j + 0.375F, j + 0.375F, k + 0.625F, k + 0.375F, f * 0.5F, d);
			renderPistonRodEW(((i - 0.25F) + 1.0F) - f1, (i - 0.25F) + 1.0F, j + 0.625F, j + 0.625F, k + 0.375F, k + 0.625F, f, d);
			renderPistonRodEW(((i - 0.25F) + 1.0F) - f1, (i - 0.25F) + 1.0F, j + 0.375F, j + 0.625F, k + 0.375F, k + 0.375F, f * 0.6F, d);
			renderPistonRodEW(((i - 0.25F) + 1.0F) - f1, (i - 0.25F) + 1.0F, j + 0.625F, j + 0.375F, k + 0.625F, k + 0.625F, f * 0.6F, d);
			break;
		}
		uvRotateEast = 0;
		uvRotateWest = 0;
		uvRotateSouth = 0;
		uvRotateNorth = 0;
		uvRotateTop = 0;
		uvRotateBottom = 0;
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockLever(Block block, int i, int j, int k) {
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = l & 7;
		boolean flag = (l & 8) > 0;
		Tessellator tessellator = Tessellator.instance;
		boolean flag1 = overrideBlockTexture >= 0;
		if(!flag1) {
			overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
		}
		float f = 0.25F;
		float f1 = 0.1875F;
		float f2 = 0.1875F;
		if(i1 == 5) {
			block.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
		} else if(i1 == 6) {
			block.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
		} else if(i1 == 4) {
			block.setBlockBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0F);
		} else if(i1 == 3) {
			block.setBlockBounds(0.5F - f1, 0.5F - f, 0.0F, 0.5F + f1, 0.5F + f, f2);
		} else if(i1 == 2) {
			block.setBlockBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0F, 0.5F + f, 0.5F + f1);
		} else if(i1 == 1) {
			block.setBlockBounds(0.0F, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
		}
		renderStandardBlock(block, i, j, k);
		if(!flag1) {
			overrideBlockTexture = -1;
		}
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f3 = 1.0F;
		if(Block.lightValue[block.blockID] > 0) {
			f3 = 1.0F;
		}
		tessellator.setColorOpaque_F(f3, f3, f3);
		int j1 = block.getBlockTextureFromSide(0);
		if(overrideBlockTexture >= 0) {
			j1 = overrideBlockTexture;
		}
		int k1 = (j1 & 0xf) << 4;
		int l1 = j1 & RenderEngine.TEX;
		float f4 = k1 / 256F;
		float f5 = (k1 + 15.99F) / 256F;
		float f6 = l1 / RenderEngine.TERRAIN_HEIGHT_F;
		float f7 = (l1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		Vec3D avec3d[] = new Vec3D[8];
		float f8 = 0.0625F;
		float f9 = 0.0625F;
		float f10 = 0.625F;
		avec3d[0] = Vec3D.createVector(-f8, 0.0D, -f9);
		avec3d[1] = Vec3D.createVector(f8, 0.0D, -f9);
		avec3d[2] = Vec3D.createVector(f8, 0.0D, f9);
		avec3d[3] = Vec3D.createVector(-f8, 0.0D, f9);
		avec3d[4] = Vec3D.createVector(-f8, f10, -f9);
		avec3d[5] = Vec3D.createVector(f8, f10, -f9);
		avec3d[6] = Vec3D.createVector(f8, f10, f9);
		avec3d[7] = Vec3D.createVector(-f8, f10, f9);
		for(int i2 = 0; i2 < 8; i2++) {
			if(flag) {
				avec3d[i2].zCoord -= 0.0625D;
				avec3d[i2].rotateAroundX(0.6981317F);
			} else {
				avec3d[i2].zCoord += 0.0625D;
				avec3d[i2].rotateAroundX(-0.6981317F);
			}
			if(i1 == 6) {
				avec3d[i2].rotateAroundY(1.570796F);
			}
			if(i1 < 5) {
				avec3d[i2].yCoord -= 0.375D;
				avec3d[i2].rotateAroundX(1.570796F);
				if(i1 == 4) {
					avec3d[i2].rotateAroundY(0.0F);
				}
				if(i1 == 3) {
					avec3d[i2].rotateAroundY(3.141593F);
				}
				if(i1 == 2) {
					avec3d[i2].rotateAroundY(1.570796F);
				}
				if(i1 == 1) {
					avec3d[i2].rotateAroundY(-1.570796F);
				}
				avec3d[i2].xCoord += i + 0.5D;
				avec3d[i2].yCoord += j + 0.5F;
				avec3d[i2].zCoord += k + 0.5D;
			} else {
				avec3d[i2].xCoord += i + 0.5D;
				avec3d[i2].yCoord += j + 0.125F;
				avec3d[i2].zCoord += k + 0.5D;
			}
		}

		Vec3D vec3d = null;
		Vec3D vec3d1 = null;
		Vec3D vec3d2 = null;
		Vec3D vec3d3 = null;
		for(int j2 = 0; j2 < 6; j2++) {
			if(j2 == 0) {
				f4 = (k1 + 7) / 256F;
				f5 = ((k1 + 9) - 0.01F) / 256F;
				f6 = (l1 + 6) / RenderEngine.TERRAIN_HEIGHT_F;
				f7 = ((l1 + 8) - 0.01F) / RenderEngine.TERRAIN_HEIGHT_F;
			} else if(j2 == 2) {
				f4 = (k1 + 7) / 256F;
				f5 = ((k1 + 9) - 0.01F) / 256F;
				f6 = (l1 + 6) / RenderEngine.TERRAIN_HEIGHT_F;
				f7 = ((l1 + 16) - 0.01F) / RenderEngine.TERRAIN_HEIGHT_F;
			}
			if(j2 == 0) {
				vec3d = avec3d[0];
				vec3d1 = avec3d[1];
				vec3d2 = avec3d[2];
				vec3d3 = avec3d[3];
			} else if(j2 == 1) {
				vec3d = avec3d[7];
				vec3d1 = avec3d[6];
				vec3d2 = avec3d[5];
				vec3d3 = avec3d[4];
			} else if(j2 == 2) {
				vec3d = avec3d[1];
				vec3d1 = avec3d[0];
				vec3d2 = avec3d[4];
				vec3d3 = avec3d[5];
			} else if(j2 == 3) {
				vec3d = avec3d[2];
				vec3d1 = avec3d[1];
				vec3d2 = avec3d[5];
				vec3d3 = avec3d[6];
			} else if(j2 == 4) {
				vec3d = avec3d[3];
				vec3d1 = avec3d[2];
				vec3d2 = avec3d[6];
				vec3d3 = avec3d[7];
			} else if(j2 == 5) {
				vec3d = avec3d[0];
				vec3d1 = avec3d[3];
				vec3d2 = avec3d[7];
				vec3d3 = avec3d[4];
			}
			tessellator.addVertexWithUV(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, f4, f7);
			tessellator.addVertexWithUV(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, f5, f7);
			tessellator.addVertexWithUV(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, f5, f6);
			tessellator.addVertexWithUV(vec3d3.xCoord, vec3d3.yCoord, vec3d3.zCoord, f4, f6);
		}

		return true;
	}

	public boolean renderCorrectedLightOnBlock(int i, int j, int k, int l) {
		Tessellator tessellator = Tessellator.instance;
		for(int i1 = 0; i1 < 6; i1++) {
			int j1 = j;
			int k1 = k;
			int l1 = l;
			if((i1 == 2 || i1 == 3) && (i - 1) / 3 == 1 || (i1 == 4 || i1 == 5) && (i - 1) % 3 == 1) {
				continue;
			}
			int i2 = 252;
			int j2 = 0;
			if(i == 2) {
				j2 = 0;
			}
			if(i == 4) {
				j2 = 1;
			}
			if(i == 8) {
				j2 = 2;
			}
			if(i == 6) {
				j2 = 3;
			}
			if(i == 1) {
				j2 = 0;
			}
			if(i == 3) {
				j2 = 3;
			}
			if(i == 7) {
				j2 = 1;
			}
			if(i == 9) {
				j2 = 2;
			}
			if(i != 2 && i != 4 && i != 6 && i != 8) {
				if(i1 >= 2) {
					if(i == 1) {
						i2--;
						if(i1 == 5 || i1 == 3) {
							continue;
						}
						if(i1 == 4) {
							j2++;
						}
					} else if(i == 3) {
						i2--;
						if(i1 == 5 || i1 == 2) {
							continue;
						}
						if(i1 == 3) {
							j2++;
						}
					} else if(i == 7) {
						i2--;
						if(i1 == 4 || i1 == 3) {
							continue;
						}
						if(i1 == 2) {
							j2++;
						}
					} else if(i == 9) {
						i2--;
						if(i1 == 4 || i1 == 2) {
							continue;
						}
						if(i1 == 5) {
							j2++;
						}
					}
				}
			} else {
				i2--;
			}
			if(i1 == 0) {
				k1 = k + 1;
			}
			if(i1 == 1) {
				k1--;
			}
			if(i1 == 2) {
				j1 = j + 1;
			}
			if(i1 == 3) {
				j1--;
			}
			if(i1 == 4) {
				l1 = l + 1;
			}
			if(i1 == 5) {
				l1--;
			}
			if(!Block.catchesLight[blockAccess.getBlockId(j1, k1, l1)]) {
				char c = '\u4000';
				tessellator.setColorOpaque_I(c);
				tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(j1, k1, l1, 2));
				int k2 = (i2 & 0xf) << 4;
				int l2 = i2 & RenderEngine.TEX;
				double d = (k2 + 0.0D) / 256D;
				double d1 = (k2 + 15.99D) / 256D;
				double d2 = l2 / RenderEngine.TERRAIN_HEIGHT_F;
				double d3 = (l2 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
				double d4 = d;
				double d5 = d1;
				double d6 = d1;
				double d7 = d;
				double d8 = d3;
				double d9 = d3;
				double d10 = d2;
				double d11 = d2;
				if(j2 != 0) {
					if(j2 == 1) {
						d4 = d;
						d5 = d;
						d6 = d1;
						d7 = d1;
						d8 = d2;
						d9 = d3;
						d10 = d3;
						d11 = d2;
					} else if(j2 == 2) {
						d4 = d1;
						d5 = d;
						d6 = d;
						d7 = d1;
						d8 = d2;
						d9 = d2;
						d10 = d3;
						d11 = d3;
					} else if(j2 == 3) {
						d4 = d1;
						d5 = d1;
						d6 = d;
						d7 = d;
						d8 = d3;
						d9 = d2;
						d10 = d2;
						d11 = d3;
					}
				}
				double d12 = 0.0031250000465661287D;
				double d13 = 0.0D - d12;
				double d14 = 1.0D + d12;
				if(i1 == 0) {
					tessellator.addVertexWithUV(j + d13, (k + 1) + d12, l + d13, d5, d9);
					tessellator.addVertexWithUV(j + d13, (k + 1) + d12, l + d14, d6, d10);
					tessellator.addVertexWithUV(j + d14, (k + 1) + d12, l + d14, d7, d11);
					tessellator.addVertexWithUV(j + d14, (k + 1) + d12, l + d13, d4, d8);
				} else if(i1 == 1) {
					tessellator.addVertexWithUV(j + d13, (k + 0) - d12, l + d14, d6, d10);
					tessellator.addVertexWithUV(j + d13, (k + 0) - d12, l + d13, d5, d9);
					tessellator.addVertexWithUV(j + d14, (k + 0) - d12, l + d13, d4, d8);
					tessellator.addVertexWithUV(j + d14, (k + 0) - d12, l + d14, d7, d11);
				} else if(i1 == 2) {
					tessellator.addVertexWithUV((j + 1) + d12, k + d13, l + d14, d6, d10);
					tessellator.addVertexWithUV((j + 1) + d12, k + d13, l + d13, d5, d9);
					tessellator.addVertexWithUV((j + 1) + d12, k + d14, l + d13, d4, d8);
					tessellator.addVertexWithUV((j + 1) + d12, k + d14, l + d14, d7, d11);
				} else if(i1 == 3) {
					tessellator.addVertexWithUV((j + 0) - d12, k + d13, l + d13, d5, d9);
					tessellator.addVertexWithUV((j + 0) - d12, k + d13, l + d14, d6, d10);
					tessellator.addVertexWithUV((j + 0) - d12, k + d14, l + d14, d7, d11);
					tessellator.addVertexWithUV((j + 0) - d12, k + d14, l + d13, d4, d8);
				} else if(i1 == 4) {
					tessellator.addVertexWithUV(j + d13, k + d14, (l + 1) + d12, d6, d10);
					tessellator.addVertexWithUV(j + d13, k + d13, (l + 1) + d12, d5, d9);
					tessellator.addVertexWithUV(j + d14, k + d13, (l + 1) + d12, d4, d8);
					tessellator.addVertexWithUV(j + d14, k + d14, (l + 1) + d12, d7, d11);
				} else if(i1 == 5) {
					tessellator.addVertexWithUV(j + d14, k + d13, (l + 0) - d12, d4, d8);
					tessellator.addVertexWithUV(j + d13, k + d13, (l + 0) - d12, d5, d9);
					tessellator.addVertexWithUV(j + d13, k + d14, (l + 0) - d12, d6, d10);
					tessellator.addVertexWithUV(j + d14, k + d14, (l + 0) - d12, d7, d11);
				}
			}
		}

		return true;
	}

	public boolean renderBlockFire(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.getBlockTextureFromSide(0);
		if(overrideBlockTexture >= 0) {
			l = overrideBlockTexture;
		}
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		int i1 = (l & 0xf) << 4;
		int j1 = l & RenderEngine.TEX;
		double d = i1 / 256F;
		double d1 = (i1 + 15.99F) / 256F;
		double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
		double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		float f = 1.4F;
		if(!blockAccess.isBlockNormalCube(i, j - 1, k) && !Block.fire.canBlockCatchFire(blockAccess, i, j - 1, k)) {
			float f1 = 0.2F;
			float f2 = 0.0625F;
			if((i + j + k & 1) == 1) {
				d = i1 / 256F;
				d1 = (i1 + 15.99F) / 256F;
				d2 = (j1 + 16) / RenderEngine.TERRAIN_HEIGHT_F;
				d3 = (j1 + 15.99F + 16F) / RenderEngine.TERRAIN_HEIGHT_F;
			}
			if((i / 2 + j / 2 + k / 2 & 1) == 1) {
				double d4 = d1;
				d1 = d;
				d = d4;
			}
			if(Block.fire.canBlockCatchFire(blockAccess, i - 1, j, k)) {
				tessellator.addVertexWithUV(i + f1, j + f + f2, k + 1, d1, d2);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, k + 1, d1, d3);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, k + 0, d, d3);
				tessellator.addVertexWithUV(i + f1, j + f + f2, k + 0, d, d2);
				tessellator.addVertexWithUV(i + f1, j + f + f2, k + 0, d, d2);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, k + 0, d, d3);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, k + 1, d1, d3);
				tessellator.addVertexWithUV(i + f1, j + f + f2, k + 1, d1, d2);
			}
			if(Block.fire.canBlockCatchFire(blockAccess, i + 1, j, k)) {
				tessellator.addVertexWithUV((i + 1) - f1, j + f + f2, k + 0, d, d2);
				tessellator.addVertexWithUV((i + 1) - 0, (j + 0) + f2, k + 0, d, d3);
				tessellator.addVertexWithUV((i + 1) - 0, (j + 0) + f2, k + 1, d1, d3);
				tessellator.addVertexWithUV((i + 1) - f1, j + f + f2, k + 1, d1, d2);
				tessellator.addVertexWithUV((i + 1) - f1, j + f + f2, k + 1, d1, d2);
				tessellator.addVertexWithUV((i + 1) - 0, (j + 0) + f2, k + 1, d1, d3);
				tessellator.addVertexWithUV((i + 1) - 0, (j + 0) + f2, k + 0, d, d3);
				tessellator.addVertexWithUV((i + 1) - f1, j + f + f2, k + 0, d, d2);
			}
			if(Block.fire.canBlockCatchFire(blockAccess, i, j, k - 1)) {
				tessellator.addVertexWithUV(i + 0, j + f + f2, k + f1, d1, d2);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, k + 0, d1, d3);
				tessellator.addVertexWithUV(i + 1, (j + 0) + f2, k + 0, d, d3);
				tessellator.addVertexWithUV(i + 1, j + f + f2, k + f1, d, d2);
				tessellator.addVertexWithUV(i + 1, j + f + f2, k + f1, d, d2);
				tessellator.addVertexWithUV(i + 1, (j + 0) + f2, k + 0, d, d3);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, k + 0, d1, d3);
				tessellator.addVertexWithUV(i + 0, j + f + f2, k + f1, d1, d2);
			}
			if(Block.fire.canBlockCatchFire(blockAccess, i, j, k + 1)) {
				tessellator.addVertexWithUV(i + 1, j + f + f2, (k + 1) - f1, d, d2);
				tessellator.addVertexWithUV(i + 1, (j + 0) + f2, (k + 1) - 0, d, d3);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, (k + 1) - 0, d1, d3);
				tessellator.addVertexWithUV(i + 0, j + f + f2, (k + 1) - f1, d1, d2);
				tessellator.addVertexWithUV(i + 0, j + f + f2, (k + 1) - f1, d1, d2);
				tessellator.addVertexWithUV(i + 0, (j + 0) + f2, (k + 1) - 0, d1, d3);
				tessellator.addVertexWithUV(i + 1, (j + 0) + f2, (k + 1) - 0, d, d3);
				tessellator.addVertexWithUV(i + 1, j + f + f2, (k + 1) - f1, d, d2);
			}
			if(Block.fire.canBlockCatchFire(blockAccess, i, j + 1, k)) {
				double d5 = i + 0.5D + 0.5D;
				double d7 = (i + 0.5D) - 0.5D;
				double d9 = k + 0.5D + 0.5D;
				double d11 = (k + 0.5D) - 0.5D;
				double d13 = (i + 0.5D) - 0.5D;
				double d15 = i + 0.5D + 0.5D;
				double d17 = (k + 0.5D) - 0.5D;
				double d20 = k + 0.5D + 0.5D;
				d = i1 / 256F;
				d1 = (i1 + 15.99F) / 256F;
				d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
				d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
				j++;
				f = -0.2F;
				if((i + j + k & 1) == 0) {
					tessellator.addVertexWithUV(d13, j + f, k + 0, d1, d2);
					tessellator.addVertexWithUV(d5, j + 0, k + 0, d1, d3);
					tessellator.addVertexWithUV(d5, j + 0, k + 1, d, d3);
					tessellator.addVertexWithUV(d13, j + f, k + 1, d, d2);
					d = i1 / 256F;
					d1 = (i1 + 15.99F) / 256F;
					d2 = (j1 + 16) / RenderEngine.TERRAIN_HEIGHT_F;
					d3 = (j1 + 15.99F + 16F) / RenderEngine.TERRAIN_HEIGHT_F;
					tessellator.addVertexWithUV(d15, j + f, k + 1, d1, d2);
					tessellator.addVertexWithUV(d7, j + 0, k + 1, d1, d3);
					tessellator.addVertexWithUV(d7, j + 0, k + 0, d, d3);
					tessellator.addVertexWithUV(d15, j + f, k + 0, d, d2);
				} else {
					tessellator.addVertexWithUV(i + 0, j + f, d20, d1, d2);
					tessellator.addVertexWithUV(i + 0, j + 0, d11, d1, d3);
					tessellator.addVertexWithUV(i + 1, j + 0, d11, d, d3);
					tessellator.addVertexWithUV(i + 1, j + f, d20, d, d2);
					d = i1 / 256F;
					d1 = (i1 + 15.99F) / 256F;
					d2 = (j1 + 16) / RenderEngine.TERRAIN_HEIGHT_F;
					d3 = (j1 + 15.99F + 16F) / RenderEngine.TERRAIN_HEIGHT_F;
					tessellator.addVertexWithUV(i + 1, j + f, d17, d1, d2);
					tessellator.addVertexWithUV(i + 1, j + 0, d9, d1, d3);
					tessellator.addVertexWithUV(i + 0, j + 0, d9, d, d3);
					tessellator.addVertexWithUV(i + 0, j + f, d17, d, d2);
				}
			}
		} else {
			double d19 = i + 0.5D + 0.20000000000000001D;
			double d6 = (i + 0.5D) - 0.20000000000000001D;
			double d8 = k + 0.5D + 0.20000000000000001D;
			double d10 = (k + 0.5D) - 0.20000000000000001D;
			double d12 = (i + 0.5D) - 0.29999999999999999D;
			double d14 = i + 0.5D + 0.29999999999999999D;
			double d16 = (k + 0.5D) - 0.29999999999999999D;
			double d18 = k + 0.5D + 0.29999999999999999D;
			tessellator.addVertexWithUV(d12, j + f, k + 1, d1, d2);
			tessellator.addVertexWithUV(d19, j + 0, k + 1, d1, d3);
			tessellator.addVertexWithUV(d19, j + 0, k + 0, d, d3);
			tessellator.addVertexWithUV(d12, j + f, k + 0, d, d2);
			tessellator.addVertexWithUV(d14, j + f, k + 0, d1, d2);
			tessellator.addVertexWithUV(d6, j + 0, k + 0, d1, d3);
			tessellator.addVertexWithUV(d6, j + 0, k + 1, d, d3);
			tessellator.addVertexWithUV(d14, j + f, k + 1, d, d2);
			d = i1 / 256F;
			d1 = (i1 + 15.99F) / 256F;
			d2 = (j1 + 16) / RenderEngine.TERRAIN_HEIGHT_F;
			d3 = (j1 + 15.99F + 16F) / RenderEngine.TERRAIN_HEIGHT_F;
			tessellator.addVertexWithUV(i + 1, j + f, d18, d1, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, d10, d1, d3);
			tessellator.addVertexWithUV(i + 0, j + 0, d10, d, d3);
			tessellator.addVertexWithUV(i + 0, j + f, d18, d, d2);
			tessellator.addVertexWithUV(i + 0, j + f, d16, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, d8, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 0, d8, d, d3);
			tessellator.addVertexWithUV(i + 1, j + f, d16, d, d2);
			d19 = (i + 0.5D) - 0.5D;
			d6 = i + 0.5D + 0.5D;
			d8 = (k + 0.5D) - 0.5D;
			d10 = k + 0.5D + 0.5D;
			d12 = (i + 0.5D) - 0.40000000000000002D;
			d14 = i + 0.5D + 0.40000000000000002D;
			d16 = (k + 0.5D) - 0.40000000000000002D;
			d18 = k + 0.5D + 0.40000000000000002D;
			tessellator.addVertexWithUV(d12, j + f, k + 0, d, d2);
			tessellator.addVertexWithUV(d19, j + 0, k + 0, d, d3);
			tessellator.addVertexWithUV(d19, j + 0, k + 1, d1, d3);
			tessellator.addVertexWithUV(d12, j + f, k + 1, d1, d2);
			tessellator.addVertexWithUV(d14, j + f, k + 1, d, d2);
			tessellator.addVertexWithUV(d6, j + 0, k + 1, d, d3);
			tessellator.addVertexWithUV(d6, j + 0, k + 0, d1, d3);
			tessellator.addVertexWithUV(d14, j + f, k + 0, d1, d2);
			d = i1 / 256F;
			d1 = (i1 + 15.99F) / 256F;
			d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			tessellator.addVertexWithUV(i + 0, j + f, d18, d, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, d10, d, d3);
			tessellator.addVertexWithUV(i + 1, j + 0, d10, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + f, d18, d1, d2);
			tessellator.addVertexWithUV(i + 1, j + f, d16, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, d8, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 0, d8, d1, d3);
			tessellator.addVertexWithUV(i + 0, j + f, d16, d1, d2);
		}
		return true;
	}

	public boolean renderBlockRedstoneWire(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = block.getBlockTextureFromSideAndMetadata(1, l);
		if(overrideBlockTexture >= 0) {
			i1 = overrideBlockTexture;
		}
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		float f1 = l / 15.0F;
		float f2 = f1 * 0.6F + 0.4F;
		if(l == 0) {
			f2 = 0.3F;
		}
		float f3 = f1 * f1 * 0.7F - 0.5F;
		float f4 = f1 * f1 * 0.6F - 0.7F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}
		if(f4 < 0.0F) {
			f4 = 0.0F;
		}
		tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
		int j1 = (i1 & 0xF) << 4;
		int k1 = i1 & RenderEngine.TEX;
		double d = j1 / 256.0F;
		double d2 = (j1 + 15.99F) / 256.0F;
		double d4 = k1 / RenderEngine.TERRAIN_HEIGHT_F;
		double d6 = (k1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		boolean flag = (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i - 1, j, k, 1)) || ((!blockAccess.isBlockNormalCube(i - 1, j, k)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i - 1, j - 1, k, -1)));
		boolean flag1 = (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i + 1, j, k, 3)) || ((!blockAccess.isBlockNormalCube(i + 1, j, k)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i + 1, j - 1, k, -1)));
		boolean flag2 = (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i, j, k - 1, 2)) || ((!blockAccess.isBlockNormalCube(i, j, k - 1)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i, j - 1, k - 1, -1)));
		boolean flag3 = (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i, j, k + 1, 0)) || ((!blockAccess.isBlockNormalCube(i, j, k + 1)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i, j - 1, k + 1, -1)));
		if(!blockAccess.isBlockNormalCube(i, j + 1, k)) {
			if((blockAccess.isBlockNormalCube(i - 1, j, k)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i - 1, j + 1, k, -1))) {
				flag = true;
			}
			if((blockAccess.isBlockNormalCube(i + 1, j, k)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i + 1, j + 1, k, -1))) {
				flag1 = true;
			}
			if((blockAccess.isBlockNormalCube(i, j, k - 1)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i, j + 1, k - 1, -1))) {
				flag2 = true;
			}
			if((blockAccess.isBlockNormalCube(i, j, k + 1)) && (BlockRedstoneWire.isPowerProviderOrWire(blockAccess, i, j + 1, k + 1, -1))) {
				flag3 = true;
			}
		}
		float f5 = i + 0;
		float f6 = i + 1;
		float f7 = k + 0;
		float f8 = k + 1;
		byte byte0 = 0;
		if(((flag) || (flag1)) && (!flag2) && (!flag3)) {
			byte0 = 1;
		}
		if(((flag2) || (flag3)) && (!flag1) && (!flag)) {
			byte0 = 2;
		}
		if(byte0 != 0) {
			d = (j1 + 16) / 256.0F;
			d2 = (j1 + 16 + 15.99F) / 256.0F;
			d4 = k1 / RenderEngine.TERRAIN_HEIGHT_F;
			d6 = (k1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		}
		final float m = 2f / RenderEngine.TERRAIN_MULT;
		if(byte0 == 0) {
			if((flag1) || (flag2) || (flag3) || (flag)) {
				if(!flag) {
					f5 += 0.3125F;
				}
				if(!flag) {
					d += 0.01953125D;
				}
				if(!flag1) {
					f6 -= 0.3125F;
				}
				if(!flag1) {
					d2 -= 0.01953125D;
				}
				if(!flag2) {
					f7 += 0.3125F;
				}
				if(!flag2) {
					d4 += 0.009765625D * m;
				}
				if(!flag3) {
					f8 -= 0.3125F;
				}
				if(!flag3) {
					d6 -= 0.009765625D * m;
				}
			}
			tessellator.addVertexWithUV(f6, j + 0.015625F, f8, d2, d6);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f7, d2, d4);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f7, d, d4);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f8, d, d6);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f8, d2, d6 + 0.03125D * m);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f7, d2, d4 + 0.03125D * m);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f7, d, d4 + 0.03125D * m);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f8, d, d6 + 0.03125D * m);
		} else if(byte0 == 1) {
			tessellator.addVertexWithUV(f6, j + 0.015625F, f8, d2, d6);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f7, d2, d4);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f7, d, d4);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f8, d, d6);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f8, d2, d6 + 0.03125D * m);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f7, d2, d4 + 0.03125D * m);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f7, d, d4 + 0.03125D * m);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f8, d, d6 + 0.03125D * m);
		} else if(byte0 == 2) {
			tessellator.addVertexWithUV(f6, j + 0.015625F, f8, d2, d6);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f7, d, d6);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f7, d, d4);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f8, d2, d4);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f8, d2, d6 + 0.03125D * m);
			tessellator.addVertexWithUV(f6, j + 0.015625F, f7, d, d6 + 0.03125D * m);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f7, d, d4 + 0.03125D * m);
			tessellator.addVertexWithUV(f5, j + 0.015625F, f8, d2, d4 + 0.03125D * m);
		}
		if(!blockAccess.isBlockNormalCube(i, j + 1, k)) {
			double d1 = (j1 + 16) / 256.0F;
			double d3 = (j1 + 16 + 15.99F) / 256.0F;
			double d5 = k1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d7 = (k1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			if((blockAccess.isBlockNormalCube(i - 1, j, k)) && (blockAccess.getBlockId(i - 1, j + 1, k) == Block.redstoneWire.blockID)) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV(i + 0.015625F, j + 1 + 0.021875F, k + 1, d3, d5);
				tessellator.addVertexWithUV(i + 0.015625F, j + 0, k + 1, d1, d5);
				tessellator.addVertexWithUV(i + 0.015625F, j + 0, k + 0, d1, d7);
				tessellator.addVertexWithUV(i + 0.015625F, j + 1 + 0.021875F, k + 0, d3, d7);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV(i + 0.015625F, j + 1 + 0.021875F, k + 1, d3, d5 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 0.015625F, j + 0, k + 1, d1, d5 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 0.015625F, j + 0, k + 0, d1, d7 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 0.015625F, j + 1 + 0.021875F, k + 0, d3, d7 + 0.03125D * m);
			}
			if((blockAccess.isBlockNormalCube(i + 1, j, k)) && (blockAccess.getBlockId(i + 1, j + 1, k) == Block.redstoneWire.blockID)) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 0, k + 1, d1, d7);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 1 + 0.021875F, k + 1, d3, d7);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 1 + 0.021875F, k + 0, d3, d5);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 0, k + 0, d1, d5);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 0, k + 1, d1, d7 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 1 + 0.021875F, k + 1, d3, d7 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 1 + 0.021875F, k + 0, d3, d5 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 1 - 0.015625F, j + 0, k + 0, d1, d5 + 0.03125D * m);
			}
			if((blockAccess.isBlockNormalCube(i, j, k - 1)) && (blockAccess.getBlockId(i, j + 1, k - 1) == Block.redstoneWire.blockID)) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV(i + 1, j + 0, k + 0.015625F, d1, d7);
				tessellator.addVertexWithUV(i + 1, j + 1 + 0.021875F, k + 0.015625F, d3, d7);
				tessellator.addVertexWithUV(i + 0, j + 1 + 0.021875F, k + 0.015625F, d3, d5);
				tessellator.addVertexWithUV(i + 0, j + 0, k + 0.015625F, d1, d5);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV(i + 1, j + 0, k + 0.015625F, d1, d7 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 1, j + 1 + 0.021875F, k + 0.015625F, d3, d7 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 0, j + 1 + 0.021875F, k + 0.015625F, d3, d5 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 0, j + 0, k + 0.015625F, d1, d5 + 0.03125D * m);
			}
			if((blockAccess.isBlockNormalCube(i, j, k + 1)) && (blockAccess.getBlockId(i, j + 1, k + 1) == Block.redstoneWire.blockID)) {
				tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
				tessellator.addVertexWithUV(i + 1, j + 1 + 0.021875F, k + 1 - 0.015625F, d3, d5);
				tessellator.addVertexWithUV(i + 1, j + 0, k + 1 - 0.015625F, d1, d5);
				tessellator.addVertexWithUV(i + 0, j + 0, k + 1 - 0.015625F, d1, d7);
				tessellator.addVertexWithUV(i + 0, j + 1 + 0.021875F, k + 1 - 0.015625F, d3, d7);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV(i + 1, j + 1 + 0.021875F, k + 1 - 0.015625F, d3, d5 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 1, j + 0, k + 1 - 0.015625F, d1, d5 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 0, j + 0, k + 1 - 0.015625F, d1, d7 + 0.03125D * m);
				tessellator.addVertexWithUV(i + 0, j + 1 + 0.021875F, k + 1 - 0.015625F, d3, d7 + 0.03125D * m);
			}
		}
		return true;
	}

	public boolean renderBlockMinecartTrack(BlockRail blockrail, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = blockAccess.getBlockMetadata(i, j, k);
		int i1 = blockrail.getBlockTextureFromSideAndMetadata(0, l);
		if(overrideBlockTexture >= 0) {
			i1 = overrideBlockTexture;
		}
		if(blockrail.getIsPowered()) {
			l &= 7;
		}
		tessellator.setBrightness(blockrail.getMixedBrightnessForBlock(blockAccess, i, j, k));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		int j1 = (i1 & 0xf) << 4;
		int k1 = i1 & RenderEngine.TEX;
		double d = j1 / 256F;
		double d1 = (j1 + 15.99F) / 256F;
		double d2 = k1 / RenderEngine.TERRAIN_HEIGHT_F;
		double d3 = (k1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		double d4 = 0.0625D;
		double d5 = i + 1;
		double d6 = i + 1;
		double d7 = i + 0;
		double d8 = i + 0;
		double d9 = k + 0;
		double d10 = k + 1;
		double d11 = k + 1;
		double d12 = k + 0;
		double d13 = j + d4;
		double d14 = j + d4;
		double d15 = j + d4;
		double d16 = j + d4;
		if(l != 1 && l != 2 && l != 3 && l != 7) {
			if(l == 8) {
				d5 = d6 = i + 0;
				d7 = d8 = i + 1;
				d9 = d12 = k + 1;
				d10 = d11 = k + 0;
			} else if(l == 9) {
				d5 = d8 = i + 0;
				d6 = d7 = i + 1;
				d9 = d10 = k + 0;
				d11 = d12 = k + 1;
			}
		} else {
			d5 = d8 = i + 1;
			d6 = d7 = i + 0;
			d9 = d10 = k + 1;
			d11 = d12 = k + 0;
		}
		if(l != 2 && l != 4) {
			if(l == 3 || l == 5) {
				d14++;
				d15++;
			}
		} else {
			d13++;
			d16++;
		}
		tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
		tessellator.addVertexWithUV(d6, d14, d10, d1, d3);
		tessellator.addVertexWithUV(d7, d15, d11, d, d3);
		tessellator.addVertexWithUV(d8, d16, d12, d, d2);
		tessellator.addVertexWithUV(d8, d16, d12, d, d2);
		tessellator.addVertexWithUV(d7, d15, d11, d, d3);
		tessellator.addVertexWithUV(d6, d14, d10, d1, d3);
		tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
		return true;
	}

	public boolean renderBlockLadder(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.getBlockTextureFromSide(0);
		if(overrideBlockTexture >= 0) {
			l = overrideBlockTexture;
		}
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		tessellator.setColorOpaque_F(f, f, f);
		int i1 = (l & 0xf) << 4;
		int j1 = l & RenderEngine.TEX;
		double d = i1 / 256F;
		double d1 = (i1 + 15.99F) / 256F;
		double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
		double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		int k1 = blockAccess.getBlockMetadata(i, j, k);
		double d4 = 0.0D;
		double d5 = 0.05000000074505806D;
		if(k1 == 5) {
			tessellator.addVertexWithUV(i + d5, (j + 1) + d4, (k + 1) + d4, d, d2);
			tessellator.addVertexWithUV(i + d5, (j + 0) - d4, (k + 1) + d4, d, d3);
			tessellator.addVertexWithUV(i + d5, (j + 0) - d4, (k + 0) - d4, d1, d3);
			tessellator.addVertexWithUV(i + d5, (j + 1) + d4, (k + 0) - d4, d1, d2);
		}
		if(k1 == 4) {
			tessellator.addVertexWithUV((i + 1) - d5, (j + 0) - d4, (k + 1) + d4, d1, d3);
			tessellator.addVertexWithUV((i + 1) - d5, (j + 1) + d4, (k + 1) + d4, d1, d2);
			tessellator.addVertexWithUV((i + 1) - d5, (j + 1) + d4, (k + 0) - d4, d, d2);
			tessellator.addVertexWithUV((i + 1) - d5, (j + 0) - d4, (k + 0) - d4, d, d3);
		}
		if(k1 == 3) {
			tessellator.addVertexWithUV((i + 1) + d4, (j + 0) - d4, k + d5, d1, d3);
			tessellator.addVertexWithUV((i + 1) + d4, (j + 1) + d4, k + d5, d1, d2);
			tessellator.addVertexWithUV((i + 0) - d4, (j + 1) + d4, k + d5, d, d2);
			tessellator.addVertexWithUV((i + 0) - d4, (j + 0) - d4, k + d5, d, d3);
		}
		if(k1 == 2) {
			tessellator.addVertexWithUV((i + 1) + d4, (j + 1) + d4, (k + 1) - d5, d, d2);
			tessellator.addVertexWithUV((i + 1) + d4, (j + 0) - d4, (k + 1) - d5, d, d3);
			tessellator.addVertexWithUV((i + 0) - d4, (j + 0) - d4, (k + 1) - d5, d1, d3);
			tessellator.addVertexWithUV((i + 0) - d4, (j + 1) + d4, (k + 1) - d5, d1, d2);
		}
		return true;
	}

	public boolean renderBlockVine(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int metadata = blockAccess.getBlockMetadata(i, j, k);
		float f = 1.0F;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		int i1 = block.colorMultiplier(blockAccess, i, j, k);
		float f1 = (i1 >> 16 & 0xff) / 255F;
		float f2 = (i1 >> 8 & 0xff) / 255F;
		float f3 = (i1 & 0xff) / 255F;
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);

		int k1 = blockAccess.getBlockMetadata(i, j, k);
		if((k1 & 2) != 0) { //����� X-
			int l = block.getBlockTextureFromSideAndMetadata(5, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			double d4 = 0.05000000074505806D;
			tessellator.addVertexWithUV(i + d4, j + 1, k + 1, d, d2);
			tessellator.addVertexWithUV(i + d4, j + 0, k + 1, d, d3);
			tessellator.addVertexWithUV(i + d4, j + 0, k + 0, d1, d3);
			tessellator.addVertexWithUV(i + d4, j + 1, k + 0, d1, d2);
			if(!blockAccess.isBlockOpaqueCube(i - 1, j, k)) {
				tessellator.addVertexWithUV(i + d4, j + 1, k + 0, d1, d2);
				tessellator.addVertexWithUV(i + d4, j + 0, k + 0, d1, d3);
				tessellator.addVertexWithUV(i + d4, j + 0, k + 1, d, d3);
				tessellator.addVertexWithUV(i + d4, j + 1, k + 1, d, d2);
			}
		}
		if((k1 & 8) != 0) { //�� X+
			int l = block.getBlockTextureFromSideAndMetadata(4, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			double d4 = 0.05000000074505806D;
			tessellator.addVertexWithUV((i + 1) - d4, j + 0, k + 1, d1, d3);
			tessellator.addVertexWithUV((i + 1) - d4, j + 1, k + 1, d1, d2);
			tessellator.addVertexWithUV((i + 1) - d4, j + 1, k + 0, d, d2);
			tessellator.addVertexWithUV((i + 1) - d4, j + 0, k + 0, d, d3);
			if(!blockAccess.isBlockOpaqueCube(i + 1, j, k)) {
				tessellator.addVertexWithUV((i + 1) - d4, j + 0, k + 0, d, d3);
				tessellator.addVertexWithUV((i + 1) - d4, j + 1, k + 0, d, d2);
				tessellator.addVertexWithUV((i + 1) - d4, j + 1, k + 1, d1, d2);
				tessellator.addVertexWithUV((i + 1) - d4, j + 0, k + 1, d1, d3);
			}
		}
		if((k1 & 4) != 0) {//������ Z-
			int l = block.getBlockTextureFromSideAndMetadata(3, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			double d4 = 0.05000000074505806D;
			tessellator.addVertexWithUV(i + 1, j + 0, k + d4, d1, d3);
			tessellator.addVertexWithUV(i + 1, j + 1, k + d4, d1, d2);
			tessellator.addVertexWithUV(i + 0, j + 1, k + d4, d, d2);
			tessellator.addVertexWithUV(i + 0, j + 0, k + d4, d, d3);
			if(!blockAccess.isBlockOpaqueCube(i, j, k - 1)) {
				tessellator.addVertexWithUV(i + 0, j + 0, k + d4, d, d3);
				tessellator.addVertexWithUV(i + 0, j + 1, k + d4, d, d2);
				tessellator.addVertexWithUV(i + 1, j + 1, k + d4, d1, d2);
				tessellator.addVertexWithUV(i + 1, j + 0, k + d4, d1, d3);
			}
		}
		if((k1 & 1) != 0) {//����� Z+
			int l = block.getBlockTextureFromSideAndMetadata(2, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			double d4 = 0.05000000074505806D;
			tessellator.addVertexWithUV(i + 1, j + 1, (k + 1) - d4, d, d2);
			tessellator.addVertexWithUV(i + 1, j + 0, (k + 1) - d4, d, d3);
			tessellator.addVertexWithUV(i + 0, j + 0, (k + 1) - d4, d1, d3);
			tessellator.addVertexWithUV(i + 0, j + 1, (k + 1) - d4, d1, d2);
			if(!blockAccess.isBlockOpaqueCube(i, j, k + 1)) {
				tessellator.addVertexWithUV(i + 0, j + 1, (k + 1) - d4, d1, d2);
				tessellator.addVertexWithUV(i + 0, j + 0, (k + 1) - d4, d1, d3);
				tessellator.addVertexWithUV(i + 1, j + 0, (k + 1) - d4, d, d3);
				tessellator.addVertexWithUV(i + 1, j + 1, (k + 1) - d4, d, d2);
			}
		}
		if(blockAccess.isBlockNormalCube(i, j + 1, k) && block.shouldSideBeRendered(blockAccess, i, j, k, 0)) {
			int l = block.getBlockTextureFromSideAndMetadata(0, metadata);
			if(overrideBlockTexture >= 0)
				l = overrideBlockTexture;
			i1 = (l & 0xf) << 4;
			int j1 = l & RenderEngine.TEX;
			double d = i1 / 256F;
			double d1 = (i1 + 15.99F) / 256F;
			double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
			double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
			double d4 = 0.05000000074505806D;
			tessellator.addVertexWithUV(i + 1, (j + 1) - d4, k + 0, d, d2);
			tessellator.addVertexWithUV(i + 1, (j + 1) - d4, k + 1, d, d3);
			tessellator.addVertexWithUV(i + 0, (j + 1) - d4, k + 1, d1, d3);
			tessellator.addVertexWithUV(i + 0, (j + 1) - d4, k + 0, d1, d2);
		}
		return true;
	}

	public boolean renderBlockPane(BlockPane blockpane, int x, int y, int z) {
		boolean b = false;
		if(blockpane.setRenderStage(blockAccess, x, y, z, 0))
			b = b | renderStandardBlock(blockpane, x, y, z);
		if(blockpane.setRenderStage(blockAccess, x, y, z, 1))
			b = b | renderStandardBlock(blockpane, x, y, z);
		return b;
		/*int l = blockAccess.func_35452_b();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(blockpane.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int i1 = blockpane.colorMultiplier(blockAccess, i, j, k);
		float f1 = (i1 >> 16 & 0xff) / 255F;
		float f2 = (i1 >> 8 & 0xff) / 255F;
		float f3 = (i1 & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f5 = (f1 * 30F + f2 * 70F) / 100F;
			float f6 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		boolean flag = false;
		boolean flag1 = false;
		int l1;
		int i2;
		if(overrideBlockTexture >= 0) {
			l1 = overrideBlockTexture;
			i2 = overrideBlockTexture;
		} else {
			int j1 = blockAccess.getBlockMetadata(i, j, k);
			l1 = blockpane.getBlockTextureFromSideAndMetadata(0, j1);
			i2 = blockpane.func_35299_s();
		}
		int k1 = (l1 & 0xf) << 4;
		int j2 = l1 & RenderEngine.TEX;
		double d = k1 / 256F;
		double d1 = (k1 + 7.99F) / 256F;
		double d2 = (k1 + 15.99F) / 256F;
		double d3 = j2 / RenderEngine.TERRAIN_HEIGHT_F;
		double d4 = (j2 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		int k2 = (i2 & 0xf) << 4;
		int l2 = i2 & RenderEngine.TEX;
		double d5 = (k2 + 7) / 256F;
		double d6 = (k2 + 8.99F) / 256F;
		double d7 = l2 / RenderEngine.TERRAIN_HEIGHT_F;
		double d8 = (l2 + 8) / RenderEngine.TERRAIN_HEIGHT_F;
		double d9 = (l2 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		double d10 = i;
		double d11 = i + 0.5D;
		double d12 = i + 1;
		double d13 = k;
		double d14 = k + 0.5D;
		double d15 = k + 1;
		double d16 = (i + 0.5D) - 0.0625D;
		double d17 = i + 0.5D + 0.0625D;
		double d18 = (k + 0.5D) - 0.0625D;
		double d19 = k + 0.5D + 0.0625D;
		boolean flag2 = blockpane.func_35298_d(blockAccess.getBlockId(i, j, k - 1));
		boolean flag3 = blockpane.func_35298_d(blockAccess.getBlockId(i, j, k + 1));
		boolean flag4 = blockpane.func_35298_d(blockAccess.getBlockId(i - 1, j, k));
		boolean flag5 = blockpane.func_35298_d(blockAccess.getBlockId(i + 1, j, k));
		boolean flag6 = blockpane.shouldSideBeRendered(blockAccess, i, j + 1, k, 1);
		boolean flag7 = blockpane.shouldSideBeRendered(blockAccess, i, j - 1, k, 0);
		if((!flag4 || !flag5) && (flag4 || flag5 || flag2 || flag3)) {
			if(flag4 && !flag5) {
				tessellator.addVertexWithUV(d10, j + 1, d14, d, d3);
				tessellator.addVertexWithUV(d10, j + 0, d14, d, d4);
				tessellator.addVertexWithUV(d11, j + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d11, j + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d11, j + 1, d14, d, d3);
				tessellator.addVertexWithUV(d11, j + 0, d14, d, d4);
				tessellator.addVertexWithUV(d10, j + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d10, j + 1, d14, d1, d3);
				if(!flag3 && !flag2) {
					tessellator.addVertexWithUV(d11, j + 1, d19, d5, d7);
					tessellator.addVertexWithUV(d11, j + 0, d19, d5, d9);
					tessellator.addVertexWithUV(d11, j + 0, d18, d6, d9);
					tessellator.addVertexWithUV(d11, j + 1, d18, d6, d7);
					tessellator.addVertexWithUV(d11, j + 1, d18, d5, d7);
					tessellator.addVertexWithUV(d11, j + 0, d18, d5, d9);
					tessellator.addVertexWithUV(d11, j + 0, d19, d6, d9);
					tessellator.addVertexWithUV(d11, j + 1, d19, d6, d7);
				}
				if(flag6 || j < l - 1 && blockAccess.isAirBlock(i - 1, j + 1, k)) {
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d8);
				}
				if(flag7 || j > 1 && blockAccess.isAirBlock(i - 1, j - 1, k)) {
					tessellator.addVertexWithUV(d10, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, j - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, j - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, j - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d8);
				}
			} else if(!flag4 && flag5) {
				tessellator.addVertexWithUV(d11, j + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d11, j + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d12, j + 0, d14, d2, d4);
				tessellator.addVertexWithUV(d12, j + 1, d14, d2, d3);
				tessellator.addVertexWithUV(d12, j + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d12, j + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d11, j + 0, d14, d2, d4);
				tessellator.addVertexWithUV(d11, j + 1, d14, d2, d3);
				if(!flag3 && !flag2) {
					tessellator.addVertexWithUV(d11, j + 1, d18, d5, d7);
					tessellator.addVertexWithUV(d11, j + 0, d18, d5, d9);
					tessellator.addVertexWithUV(d11, j + 0, d19, d6, d9);
					tessellator.addVertexWithUV(d11, j + 1, d19, d6, d7);
					tessellator.addVertexWithUV(d11, j + 1, d19, d5, d7);
					tessellator.addVertexWithUV(d11, j + 0, d19, d5, d9);
					tessellator.addVertexWithUV(d11, j + 0, d18, d6, d9);
					tessellator.addVertexWithUV(d11, j + 1, d18, d6, d7);
				}
				if(flag6 || j < l - 1 && blockAccess.isAirBlock(i + 1, j + 1, k)) {
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d18, d5, d7);
				}
				if(flag7 || j > 1 && blockAccess.isAirBlock(i + 1, j - 1, k)) {
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, j - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, j - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, j - 0.01D, d18, d5, d7);
				}
			}
		} else {
			tessellator.addVertexWithUV(d10, j + 1, d14, d, d3);
			tessellator.addVertexWithUV(d10, j + 0, d14, d, d4);
			tessellator.addVertexWithUV(d12, j + 0, d14, d2, d4);
			tessellator.addVertexWithUV(d12, j + 1, d14, d2, d3);
			tessellator.addVertexWithUV(d12, j + 1, d14, d, d3);
			tessellator.addVertexWithUV(d12, j + 0, d14, d, d4);
			tessellator.addVertexWithUV(d10, j + 0, d14, d2, d4);
			tessellator.addVertexWithUV(d10, j + 1, d14, d2, d3);
			if(flag6) {
				tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d18, d5, d9);
			} else {
				if(j < l - 1 && blockAccess.isAirBlock(i - 1, j + 1, k)) {
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, (j + 1) + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d8);
				}
				if(j < l - 1 && blockAccess.isAirBlock(i + 1, j + 1, k)) {
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, (j + 1) + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, (j + 1) + 0.01D, d18, d5, d7);
				}
			}
			if(flag7) {
				tessellator.addVertexWithUV(d10, j - 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d12, j - 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, j - 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d10, j - 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d12, j - 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, j - 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d10, j - 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, j - 0.01D, d18, d5, d9);
			} else {
				if(j > 1 && blockAccess.isAirBlock(i - 1, j - 1, k)) {
					tessellator.addVertexWithUV(d10, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, j - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, j - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, j - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d8);
				}
				if(j > 1 && blockAccess.isAirBlock(i + 1, j - 1, k)) {
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, j - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, j - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, j - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, j - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, j - 0.01D, d18, d5, d7);
				}
			}
		}
		if((!flag2 || !flag3) && (flag4 || flag5 || flag2 || flag3)) {
			if(flag2 && !flag3) {
				tessellator.addVertexWithUV(d11, j + 1, d13, d, d3);
				tessellator.addVertexWithUV(d11, j + 0, d13, d, d4);
				tessellator.addVertexWithUV(d11, j + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d11, j + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d11, j + 1, d14, d, d3);
				tessellator.addVertexWithUV(d11, j + 0, d14, d, d4);
				tessellator.addVertexWithUV(d11, j + 0, d13, d1, d4);
				tessellator.addVertexWithUV(d11, j + 1, d13, d1, d3);
				if(!flag5 && !flag4) {
					tessellator.addVertexWithUV(d16, j + 1, d14, d5, d7);
					tessellator.addVertexWithUV(d16, j + 0, d14, d5, d9);
					tessellator.addVertexWithUV(d17, j + 0, d14, d6, d9);
					tessellator.addVertexWithUV(d17, j + 1, d14, d6, d7);
					tessellator.addVertexWithUV(d17, j + 1, d14, d5, d7);
					tessellator.addVertexWithUV(d17, j + 0, d14, d5, d9);
					tessellator.addVertexWithUV(d16, j + 0, d14, d6, d9);
					tessellator.addVertexWithUV(d16, j + 1, d14, d6, d7);
				}
				if(flag6 || j < l - 1 && blockAccess.isAirBlock(i, j + 1, k - 1)) {
					tessellator.addVertexWithUV(d16, j + 1, d13, d6, d7);
					tessellator.addVertexWithUV(d16, j + 1, d14, d6, d8);
					tessellator.addVertexWithUV(d17, j + 1, d14, d5, d8);
					tessellator.addVertexWithUV(d17, j + 1, d13, d5, d7);
					tessellator.addVertexWithUV(d16, j + 1, d14, d6, d7);
					tessellator.addVertexWithUV(d16, j + 1, d13, d6, d8);
					tessellator.addVertexWithUV(d17, j + 1, d13, d5, d8);
					tessellator.addVertexWithUV(d17, j + 1, d14, d5, d7);
				}
				if(flag7 || j > 1 && blockAccess.isAirBlock(i, j - 1, k - 1)) {
					tessellator.addVertexWithUV(d16, j, d13, d6, d7);
					tessellator.addVertexWithUV(d16, j, d14, d6, d8);
					tessellator.addVertexWithUV(d17, j, d14, d5, d8);
					tessellator.addVertexWithUV(d17, j, d13, d5, d7);
					tessellator.addVertexWithUV(d16, j, d14, d6, d7);
					tessellator.addVertexWithUV(d16, j, d13, d6, d8);
					tessellator.addVertexWithUV(d17, j, d13, d5, d8);
					tessellator.addVertexWithUV(d17, j, d14, d5, d7);
				}
			} else if(!flag2 && flag3) {
				tessellator.addVertexWithUV(d11, j + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d11, j + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d11, j + 0, d15, d2, d4);
				tessellator.addVertexWithUV(d11, j + 1, d15, d2, d3);
				tessellator.addVertexWithUV(d11, j + 1, d15, d1, d3);
				tessellator.addVertexWithUV(d11, j + 0, d15, d1, d4);
				tessellator.addVertexWithUV(d11, j + 0, d14, d2, d4);
				tessellator.addVertexWithUV(d11, j + 1, d14, d2, d3);
				if(!flag5 && !flag4) {
					tessellator.addVertexWithUV(d17, j + 1, d14, d5, d7);
					tessellator.addVertexWithUV(d17, j + 0, d14, d5, d9);
					tessellator.addVertexWithUV(d16, j + 0, d14, d6, d9);
					tessellator.addVertexWithUV(d16, j + 1, d14, d6, d7);
					tessellator.addVertexWithUV(d16, j + 1, d14, d5, d7);
					tessellator.addVertexWithUV(d16, j + 0, d14, d5, d9);
					tessellator.addVertexWithUV(d17, j + 0, d14, d6, d9);
					tessellator.addVertexWithUV(d17, j + 1, d14, d6, d7);
				}
				if(flag6 || j < l - 1 && blockAccess.isAirBlock(i, j + 1, k + 1)) {
					tessellator.addVertexWithUV(d16, j + 1, d14, d5, d8);
					tessellator.addVertexWithUV(d16, j + 1, d15, d5, d9);
					tessellator.addVertexWithUV(d17, j + 1, d15, d6, d9);
					tessellator.addVertexWithUV(d17, j + 1, d14, d6, d8);
					tessellator.addVertexWithUV(d16, j + 1, d15, d5, d8);
					tessellator.addVertexWithUV(d16, j + 1, d14, d5, d9);
					tessellator.addVertexWithUV(d17, j + 1, d14, d6, d9);
					tessellator.addVertexWithUV(d17, j + 1, d15, d6, d8);
				}
				if(flag7 || j > 1 && blockAccess.isAirBlock(i, j - 1, k + 1)) {
					tessellator.addVertexWithUV(d16, j, d14, d5, d8);
					tessellator.addVertexWithUV(d16, j, d15, d5, d9);
					tessellator.addVertexWithUV(d17, j, d15, d6, d9);
					tessellator.addVertexWithUV(d17, j, d14, d6, d8);
					tessellator.addVertexWithUV(d16, j, d15, d5, d8);
					tessellator.addVertexWithUV(d16, j, d14, d5, d9);
					tessellator.addVertexWithUV(d17, j, d14, d6, d9);
					tessellator.addVertexWithUV(d17, j, d15, d6, d8);
				}
			}
		} else {
			tessellator.addVertexWithUV(d11, j + 1, d15, d, d3);
			tessellator.addVertexWithUV(d11, j + 0, d15, d, d4);
			tessellator.addVertexWithUV(d11, j + 0, d13, d2, d4);
			tessellator.addVertexWithUV(d11, j + 1, d13, d2, d3);
			tessellator.addVertexWithUV(d11, j + 1, d13, d, d3);
			tessellator.addVertexWithUV(d11, j + 0, d13, d, d4);
			tessellator.addVertexWithUV(d11, j + 0, d15, d2, d4);
			tessellator.addVertexWithUV(d11, j + 1, d15, d2, d3);
			if(flag6) {
				tessellator.addVertexWithUV(d17, j + 1, d15, d6, d9);
				tessellator.addVertexWithUV(d17, j + 1, d13, d6, d7);
				tessellator.addVertexWithUV(d16, j + 1, d13, d5, d7);
				tessellator.addVertexWithUV(d16, j + 1, d15, d5, d9);
				tessellator.addVertexWithUV(d17, j + 1, d13, d6, d9);
				tessellator.addVertexWithUV(d17, j + 1, d15, d6, d7);
				tessellator.addVertexWithUV(d16, j + 1, d15, d5, d7);
				tessellator.addVertexWithUV(d16, j + 1, d13, d5, d9);
			} else {
				if(j < l - 1 && blockAccess.isAirBlock(i, j + 1, k - 1)) {
					tessellator.addVertexWithUV(d16, j + 1, d13, d6, d7);
					tessellator.addVertexWithUV(d16, j + 1, d14, d6, d8);
					tessellator.addVertexWithUV(d17, j + 1, d14, d5, d8);
					tessellator.addVertexWithUV(d17, j + 1, d13, d5, d7);
					tessellator.addVertexWithUV(d16, j + 1, d14, d6, d7);
					tessellator.addVertexWithUV(d16, j + 1, d13, d6, d8);
					tessellator.addVertexWithUV(d17, j + 1, d13, d5, d8);
					tessellator.addVertexWithUV(d17, j + 1, d14, d5, d7);
				}
				if(j < l - 1 && blockAccess.isAirBlock(i, j + 1, k + 1)) {
					tessellator.addVertexWithUV(d16, j + 1, d14, d5, d8);
					tessellator.addVertexWithUV(d16, j + 1, d15, d5, d9);
					tessellator.addVertexWithUV(d17, j + 1, d15, d6, d9);
					tessellator.addVertexWithUV(d17, j + 1, d14, d6, d8);
					tessellator.addVertexWithUV(d16, j + 1, d15, d5, d8);
					tessellator.addVertexWithUV(d16, j + 1, d14, d5, d9);
					tessellator.addVertexWithUV(d17, j + 1, d14, d6, d9);
					tessellator.addVertexWithUV(d17, j + 1, d15, d6, d8);
				}
			}
			if(flag7) {
				tessellator.addVertexWithUV(d17, j, d15, d6, d9);
				tessellator.addVertexWithUV(d17, j, d13, d6, d7);
				tessellator.addVertexWithUV(d16, j, d13, d5, d7);
				tessellator.addVertexWithUV(d16, j, d15, d5, d9);
				tessellator.addVertexWithUV(d17, j, d13, d6, d9);
				tessellator.addVertexWithUV(d17, j, d15, d6, d7);
				tessellator.addVertexWithUV(d16, j, d15, d5, d7);
				tessellator.addVertexWithUV(d16, j, d13, d5, d9);
			} else {
				if(j > 1 && blockAccess.isAirBlock(i, j - 1, k - 1)) {
					tessellator.addVertexWithUV(d16, j, d13, d6, d7);
					tessellator.addVertexWithUV(d16, j, d14, d6, d8);
					tessellator.addVertexWithUV(d17, j, d14, d5, d8);
					tessellator.addVertexWithUV(d17, j, d13, d5, d7);
					tessellator.addVertexWithUV(d16, j, d14, d6, d7);
					tessellator.addVertexWithUV(d16, j, d13, d6, d8);
					tessellator.addVertexWithUV(d17, j, d13, d5, d8);
					tessellator.addVertexWithUV(d17, j, d14, d5, d7);
				}
				if(j > 1 && blockAccess.isAirBlock(i, j - 1, k + 1)) {
					tessellator.addVertexWithUV(d16, j, d14, d5, d8);
					tessellator.addVertexWithUV(d16, j, d15, d5, d9);
					tessellator.addVertexWithUV(d17, j, d15, d6, d9);
					tessellator.addVertexWithUV(d17, j, d14, d6, d8);
					tessellator.addVertexWithUV(d16, j, d15, d5, d8);
					tessellator.addVertexWithUV(d16, j, d14, d5, d9);
					tessellator.addVertexWithUV(d17, j, d14, d6, d9);
					tessellator.addVertexWithUV(d17, j, d15, d6, d8);
				}
			}
		}
		return true;
		*/
	}

	public boolean renderCrossedSquares(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int l = block.colorMultiplier(blockAccess, i, j, k);
		float f1 = (l >> 16 & 0xff) / 255F;
		float f2 = (l >> 8 & 0xff) / 255F;
		float f3 = (l & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f5 = (f1 * 30F + f2 * 70F) / 100F;
			float f6 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		double d = i;
		double d1 = j;
		double d2 = k;
		if(block == Block.tallGrass) {
			long l1 = (i * 0x2fc20f) ^ k * 0x6ebfff5L ^ j;
			l1 = l1 * l1 * 0x285b825L + l1 * 11L;
			d += (((l1 >> 16 & 15L) / 15F) - 0.5D) * 0.5D;
			d1 += (((l1 >> 20 & 15L) / 15F) - 1.0D) * 0.20000000000000001D;
			d2 += (((l1 >> 24 & 15L) / 15F) - 0.5D) * 0.5D;
		}
		renderCrossedSquares(block, blockAccess.getBlockMetadata(i, j, k), d, d1, d2);
		return true;
	}

	public boolean renderBlockStem(Block block, int i, int j, int k) {
		BlockStem blockstem = (BlockStem) block;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(blockstem.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int l = blockstem.colorMultiplier(blockAccess, i, j, k);
		float f1 = (l >> 16 & 0xff) / 255F;
		float f2 = (l >> 8 & 0xff) / 255F;
		float f3 = (l & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f5 = (f1 * 30F + f2 * 70F) / 100F;
			float f6 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		blockstem.setBlockBoundsBasedOnState(blockAccess, i, j, k);
		int i1 = blockstem.func_35296_f(blockAccess, i, j, k);
		if(i1 < 0) {
			renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(i, j, k), blockstem.maxY, i, j, k);
		} else {
			renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(i, j, k), 0.5D, i, j, k);
			renderBlockStemBig(blockstem, blockAccess.getBlockMetadata(i, j, k), i1, blockstem.maxY, i, j, k);
		}
		return true;
	}

	public boolean renderBlockCrops(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		renderBlockCropsImpl(block, blockAccess.getBlockMetadata(i, j, k), i, j - 0.0625F, k);
		return true;
	}

	public void renderTorchAtAngle(Block block, double d, double d1, double d2, double d3, double d4) {
		Tessellator tessellator = Tessellator.instance;
		int i = block.getBlockTextureFromSide(0);
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		float f = j / 256F;
		float f1 = (j + 15.99F) / 256F;
		float f2 = k / RenderEngine.TERRAIN_HEIGHT_F;
		float f3 = (k + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		double d5 = f + 0.02734375D;
		double d6 = f2 + 0.0234375D / RenderEngine.TERRAIN_MULT;
		double d7 = f + 0.03515625D;
		double d8 = f2 + 0.03125D / RenderEngine.TERRAIN_MULT;
		d += 0.5D;
		d2 += 0.5D;
		double d9 = d - 0.5D;
		double d10 = d + 0.5D;
		double d11 = d2 - 0.5D;
		double d12 = d2 + 0.5D;
		double d13 = 0.0625D;
		double d14 = 0.625D;
		tessellator.addVertexWithUV((d + d3 * (1.0D - d14)) - d13, d1 + d14, (d2 + d4 * (1.0D - d14)) - d13, d5, d6);
		tessellator.addVertexWithUV((d + d3 * (1.0D - d14)) - d13, d1 + d14, d2 + d4 * (1.0D - d14) + d13, d5, d8);
		tessellator.addVertexWithUV(d + d3 * (1.0D - d14) + d13, d1 + d14, d2 + d4 * (1.0D - d14) + d13, d7, d8);
		tessellator.addVertexWithUV(d + d3 * (1.0D - d14) + d13, d1 + d14, (d2 + d4 * (1.0D - d14)) - d13, d7, d6);
		tessellator.addVertexWithUV(d - d13, d1 + 1.0D, d11, f, f2);
		tessellator.addVertexWithUV((d - d13) + d3, d1 + 0.0D, d11 + d4, f, f3);
		tessellator.addVertexWithUV((d - d13) + d3, d1 + 0.0D, d12 + d4, f1, f3);
		tessellator.addVertexWithUV(d - d13, d1 + 1.0D, d12, f1, f2);
		tessellator.addVertexWithUV(d + d13, d1 + 1.0D, d12, f, f2);
		tessellator.addVertexWithUV(d + d3 + d13, d1 + 0.0D, d12 + d4, f, f3);
		tessellator.addVertexWithUV(d + d3 + d13, d1 + 0.0D, d11 + d4, f1, f3);
		tessellator.addVertexWithUV(d + d13, d1 + 1.0D, d11, f1, f2);
		tessellator.addVertexWithUV(d9, d1 + 1.0D, d2 + d13, f, f2);
		tessellator.addVertexWithUV(d9 + d3, d1 + 0.0D, d2 + d13 + d4, f, f3);
		tessellator.addVertexWithUV(d10 + d3, d1 + 0.0D, d2 + d13 + d4, f1, f3);
		tessellator.addVertexWithUV(d10, d1 + 1.0D, d2 + d13, f1, f2);
		tessellator.addVertexWithUV(d10, d1 + 1.0D, d2 - d13, f, f2);
		tessellator.addVertexWithUV(d10 + d3, d1 + 0.0D, (d2 - d13) + d4, f, f3);
		tessellator.addVertexWithUV(d9 + d3, d1 + 0.0D, (d2 - d13) + d4, f1, f3);
		tessellator.addVertexWithUV(d9, d1 + 1.0D, d2 - d13, f1, f2);
	}

	public void renderCrossedSquares(Block block, int i, double d, double d1, double d2) {
		Tessellator tessellator = Tessellator.instance;
		int j = block.getBlockTextureFromSideAndMetadata(0, i);
		if(overrideBlockTexture >= 0) {
			j = overrideBlockTexture;
		}
		int k = (j & 0xf) << 4;
		int l = j & RenderEngine.TEX;
		double d3 = k / 256F;
		double d4 = (k + 15.99F) / 256F;
		double d5 = l / RenderEngine.TERRAIN_HEIGHT_F;
		double d6 = (l + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		double d7 = (d + 0.5D) - 0.45D;
		double d8 = d + 0.5D + 0.45D;
		double d9 = (d2 + 0.5D) - 0.45D;
		double d10 = d2 + 0.5D + 0.45D;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d4, d5);
	}

	public void renderBlockStemSmall(Block block, int i, double d, double d1, double d2, double d3) {
		Tessellator tessellator = Tessellator.instance;
		int j = block.getBlockTextureFromSideAndMetadata(0, i);
		if(overrideBlockTexture >= 0) {
			j = overrideBlockTexture;
		}
		int k = (j & 0xf) << 4;
		int l = j & RenderEngine.TEX;
		double d4 = k / 256F;
		double d5 = (k + 15.99F) / 256F;
		double d6 = l / RenderEngine.TERRAIN_HEIGHT_F;
		double d7 = (l + 15.989999771118164D * d) / RenderEngine.TERRAIN_HEIGHT_D;
		double d8 = (d1 + 0.5D) - 0.44999998807907104D;
		double d9 = d1 + 0.5D + 0.44999998807907104D;
		double d10 = (d3 + 0.5D) - 0.44999998807907104D;
		double d11 = d3 + 0.5D + 0.44999998807907104D;
		tessellator.addVertexWithUV(d8, d2 + d, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d2 + 0.0D, d10, d4, d7);
		tessellator.addVertexWithUV(d9, d2 + 0.0D, d11, d5, d7);
		tessellator.addVertexWithUV(d9, d2 + d, d11, d5, d6);
		tessellator.addVertexWithUV(d9, d2 + d, d11, d4, d6);
		tessellator.addVertexWithUV(d9, d2 + 0.0D, d11, d4, d7);
		tessellator.addVertexWithUV(d8, d2 + 0.0D, d10, d5, d7);
		tessellator.addVertexWithUV(d8, d2 + d, d10, d5, d6);
		tessellator.addVertexWithUV(d8, d2 + d, d11, d4, d6);
		tessellator.addVertexWithUV(d8, d2 + 0.0D, d11, d4, d7);
		tessellator.addVertexWithUV(d9, d2 + 0.0D, d10, d5, d7);
		tessellator.addVertexWithUV(d9, d2 + d, d10, d5, d6);
		tessellator.addVertexWithUV(d9, d2 + d, d10, d4, d6);
		tessellator.addVertexWithUV(d9, d2 + 0.0D, d10, d4, d7);
		tessellator.addVertexWithUV(d8, d2 + 0.0D, d11, d5, d7);
		tessellator.addVertexWithUV(d8, d2 + d, d11, d5, d6);
	}

	public boolean renderBlockLilyPad(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.blockIndexInTexture;
		if(overrideBlockTexture >= 0) {
			l = overrideBlockTexture;
		}
		int i1 = (l & 0xf) << 4;
		int j1 = l & RenderEngine.TEX;
		float f = 0.015625F;
		double d = i1 / 256F;
		double d1 = (i1 + 15.99F) / 256F;
		double d2 = j1 / RenderEngine.TERRAIN_HEIGHT_F;
		double d3 = (j1 + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		long l1 = (i * 0x2fc20f) ^ k * 0x6ebfff5L ^ j;
		l1 = l1 * l1 * 0x285b825L + l1 * 11L;
		int k1 = (int) (l1 >> 16 & 3L);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f1 = i + 0.5F;
		float f2 = k + 0.5F;
		float f3 = (k1 & 1) * 0.5F * (1 - ((k1 / 2) % 2) * 2);
		float f4 = (k1 + 1 & 1) * 0.5F * (1 - (((k1 + 1) / 2) % 2) * 2);
		tessellator.setColorOpaque_I(block.getBlockColor());
		tessellator.addVertexWithUV((f1 + f3) - f4, j + f, f2 + f3 + f4, d, d2);
		tessellator.addVertexWithUV(f1 + f3 + f4, j + f, (f2 - f3) + f4, d1, d2);
		tessellator.addVertexWithUV((f1 - f3) + f4, j + f, f2 - f3 - f4, d1, d3);
		tessellator.addVertexWithUV(f1 - f3 - f4, j + f, (f2 + f3) - f4, d, d3);
		tessellator.setColorOpaque_I((block.getBlockColor() & 0xfefefe) >> 1);
		tessellator.addVertexWithUV(f1 - f3 - f4, j + f, (f2 + f3) - f4, d, d3);
		tessellator.addVertexWithUV((f1 - f3) + f4, j + f, f2 - f3 - f4, d1, d3);
		tessellator.addVertexWithUV(f1 + f3 + f4, j + f, (f2 - f3) + f4, d1, d2);
		tessellator.addVertexWithUV((f1 + f3) - f4, j + f, f2 + f3 + f4, d, d2);
		return true;
	}

	public void renderBlockStemBig(Block block, int i, int j, double d, double d1, double d2, double d3) {
		Tessellator tessellator = Tessellator.instance;
		int k = block.getBlockTextureFromSideAndMetadata(0, i) + 16;
		if(overrideBlockTexture >= 0) {
			k = overrideBlockTexture;
		}
		int l = (k & 0xf) << 4;
		int i1 = k & RenderEngine.TEX;
		double d4 = l / 256F;
		double d5 = (l + 15.99F) / 256F;
		double d6 = i1 / RenderEngine.TERRAIN_HEIGHT_F;
		double d7 = (i1 + 15.989999771118164D * d) / RenderEngine.TERRAIN_HEIGHT_D;
		double d8 = (d1 + 0.5D) - 0.5D;
		double d9 = d1 + 0.5D + 0.5D;
		double d10 = (d3 + 0.5D) - 0.5D;
		double d11 = d3 + 0.5D + 0.5D;
		double d12 = d1 + 0.5D;
		double d13 = d3 + 0.5D;
		if(((j + 1) / 2) % 2 == 1) {
			double d14 = d5;
			d5 = d4;
			d4 = d14;
		}
		if(j < 2) {
			tessellator.addVertexWithUV(d8, d2 + d, d13, d4, d6);
			tessellator.addVertexWithUV(d8, d2 + 0.0D, d13, d4, d7);
			tessellator.addVertexWithUV(d9, d2 + 0.0D, d13, d5, d7);
			tessellator.addVertexWithUV(d9, d2 + d, d13, d5, d6);
			tessellator.addVertexWithUV(d9, d2 + d, d13, d5, d6);
			tessellator.addVertexWithUV(d9, d2 + 0.0D, d13, d5, d7);
			tessellator.addVertexWithUV(d8, d2 + 0.0D, d13, d4, d7);
			tessellator.addVertexWithUV(d8, d2 + d, d13, d4, d6);
		} else {
			tessellator.addVertexWithUV(d12, d2 + d, d11, d4, d6);
			tessellator.addVertexWithUV(d12, d2 + 0.0D, d11, d4, d7);
			tessellator.addVertexWithUV(d12, d2 + 0.0D, d10, d5, d7);
			tessellator.addVertexWithUV(d12, d2 + d, d10, d5, d6);
			tessellator.addVertexWithUV(d12, d2 + d, d10, d5, d6);
			tessellator.addVertexWithUV(d12, d2 + 0.0D, d10, d5, d7);
			tessellator.addVertexWithUV(d12, d2 + 0.0D, d11, d4, d7);
			tessellator.addVertexWithUV(d12, d2 + d, d11, d4, d6);
		}
	}

	public void renderBlockCropsImpl(Block block, int i, double d, double d1, double d2) {
		Tessellator tessellator = Tessellator.instance;
		int j = block.getBlockTextureFromSideAndMetadata(0, i);
		if(overrideBlockTexture >= 0) {
			j = overrideBlockTexture;
		}
		int k = (j & 0xf) << 4;
		int l = j & RenderEngine.TEX;
		double d3 = k / 256F;
		double d4 = (k + 15.99F) / 256F;
		double d5 = l / RenderEngine.TERRAIN_HEIGHT_F;
		double d6 = (l + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		double d7 = (d + 0.5D) - 0.25D;
		double d8 = d + 0.5D + 0.25D;
		double d9 = (d2 + 0.5D) - 0.5D;
		double d10 = d2 + 0.5D + 0.5D;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d4, d5);
		d7 = (d + 0.5D) - 0.5D;
		d8 = d + 0.5D + 0.5D;
		d9 = (d2 + 0.5D) - 0.25D;
		d10 = d2 + 0.5D + 0.25D;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d4, d5);
	}

	public boolean renderBlockSnowSmooth(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;

		float normal_value = -1.0F;

		int l = block.colorMultiplier(blockAccess, i, j, k);
		float f = (l >> 16 & 0xFF) / 255.0F;
		float f1 = (l >> 8 & 0xFF) / 255.0F;
		float f2 = (l & 0xFF) / 255.0F;
		boolean flag = block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1);
		boolean flag1 = block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0);
		boolean[] aflag = new boolean[4];
		aflag[0] = block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2);
		aflag[1] = block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3);
		aflag[2] = block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4);
		aflag[3] = block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5);
		if(!flag && !flag1 && !aflag[0] && !aflag[1] && !aflag[2] && !aflag[3])
			return false;
		boolean flag2 = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		double d = 0.0D;
		double d1 = 1.0D;
		Material material = block.blockMaterial;
		int i1 = 7 - blockAccess.getBlockMetadata(i, j, k);
		double d2 = getFluidHeightForSnow(i, j, k, material);
		double d3 = getFluidHeightForSnow(i, j, k + 1, material);
		double d4 = getFluidHeightForSnow(i + 1, j, k + 1, material);
		double d5 = getFluidHeightForSnow(i + 1, j, k, material);
		if((renderAllFaces) || (flag)) {
			flag2 = true;
			int j1 = block.getBlockTextureFromSideAndMetadata(1, i1);
			float f8 = (float) BlockFluid.func_293_a(blockAccess, i, j, k, material);
			if(f8 > -999.0F) {
				j1 = block.getBlockTextureFromSideAndMetadata(2, i1);
			}
			int i2 = (j1 & 0xF) << 4;
			int k2 = j1 & RenderEngine.TEX;
			double d6 = (i2 + 8.0D) / 256.0D;
			double d7 = (k2 + 8.0D) / RenderEngine.TERRAIN_HEIGHT_D;
			if(f8 < -999.0F) {
				f8 = 0.0F;
			} else {
				d6 = (i2 + 16) / 256.0F;
				d7 = (k2 + 16) / RenderEngine.TERRAIN_HEIGHT_F;
			}
			double d9 = MathHelper.sin(f8) * 8.0F / 256.0D;
			double d11 = MathHelper.cos(f8) * 8.0F / 256.0D;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
			double d9_1 = d9 / RenderEngine.TERRAIN_MULT;
			double d11_1 = d11 / RenderEngine.TERRAIN_MULT;

			float f9 = 1.0F;
			float curr_normal = 0.0F;
			float offset = 0.0F;
			tessellator.setColorOpaque_F(f4 * f9 * f, f4 * f9 * f1, f4 * f9 * f2);
			if((d2 >= 0.88D) && (d2 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 0, j + d2, k + 0, d6 - d11 - d9, d7 - d11_1 + d9_1);
			tessellator.setNormal(0.0F, curr_normal, 0.0F);
			if((d3 >= 0.88D) && (d3 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 0, j + d3, k + 1, d6 - d11 + d9, d7 + d11_1 + d9_1);
			tessellator.setNormal(0.0F, curr_normal, 0.0F);
			if((d4 >= 0.88D) && (d4 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 1, j + d4, k + 1, d6 + d11 + d9, d7 + d11_1 - d9_1);
			tessellator.setNormal(0.0F, curr_normal, 0.0F);
			if((d5 >= 0.88D) && (d5 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 1, j + d5, k + 0, d6 + d11 - d9, d7 - d11_1 - d9_1);
		}

		if((renderAllFaces) || (flag1)) {
			tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
			float f7 = 1.0F;
			tessellator.setColorOpaque_F(f3 * f7, f3 * f7, f3 * f7);
			renderBottomFace(block, i, j, k, block.getBlockTextureFromSide(0));
			flag2 = true;
		}
		for(int k1 = 0; k1 < 4; k1++) {
			int l1 = i;
			int j2 = j;
			int l2 = k;
			if(k1 == 0) {
				l2--;
			}
			if(k1 == 1) {
				l2++;
			}
			if(k1 == 2) {
				l1--;
			}
			if(k1 == 3) {
				l1++;
			}
			int i3 = block.getBlockTextureFromSideAndMetadata(k1 + 2, i1);
			int j3 = (i3 & 0xF) << 4;
			int k3 = i3 & RenderEngine.TEX;
			if((!renderAllFaces) && (!aflag[k1]))
				continue;
			double d15;
			double d8;
			double d10;
			double d12;
			double d14;
			double d13;
			if(k1 == 0) {
				d8 = d2;
				d10 = d5;
				d12 = i;
				d14 = i + 1;
				d13 = k;
				d15 = k;
			} else {
				if(k1 == 1) {
					d8 = d4;
					d10 = d3;
					d12 = i + 1;
					d14 = i;
					d13 = k + 1;
					d15 = k + 1;
				} else {
					if(k1 == 2) {
						d8 = d3;
						d10 = d2;
						d12 = i;
						d14 = i;
						d13 = k + 1;
						d15 = k;
					} else {
						d8 = d5;
						d10 = d4;
						d12 = i + 1;
						d14 = i + 1;
						d13 = k;
						d15 = k + 1;
					}
				}
			}
			flag2 = true;
			double d16 = (j3 + 0) / 256.0F;
			double d17 = (j3 + 16 - 0.01D) / 256.0D;
			double d18 = (k3 + (1.0D - d8) * 16.0D) / RenderEngine.TERRAIN_HEIGHT_D;
			double d19 = (k3 + (1.0D - d10) * 16.0D) / RenderEngine.TERRAIN_HEIGHT_D;
			double d20 = (k3 + 16 - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, l1, j2, l2));
			float f10 = 1.0F;
			if(k1 < 2) {
				f10 *= f5;
			} else {
				f10 *= f6;
			}

			tessellator.setNormal(0.0F, 0.0F, 0.0F);

			tessellator.setColorOpaque_F(f4 * f10 * f, f4 * f10 * f1, f4 * f10 * f2);
			tessellator.addVertexWithUV(d12, j + d8, d13, d16, d18);
			tessellator.addVertexWithUV(d14, j + d10, d15, d17, d19);
			tessellator.addVertexWithUV(d14, j + 0, d15, d17, d20);
			tessellator.addVertexWithUV(d12, j + 0, d13, d16, d20);
		}

		block.minY = d;
		block.maxY = d1;
		return flag2;
	}

	public boolean renderBlockFluids(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;

		float normal_value = -1.0F;

		int l = block.colorMultiplier(blockAccess, i, j, k);
		float f = (l >> 16 & 0xFF) / 255.0F;
		float f1 = (l >> 8 & 0xFF) / 255.0F;
		float f2 = (l & 0xFF) / 255.0F;
		boolean flag = block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1);
		boolean flag1 = block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0);
		boolean[] aflag = new boolean[4];
		aflag[0] = block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2);
		aflag[1] = block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3);
		aflag[2] = block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4);
		aflag[3] = block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5);
		if(!flag && !flag1 && !aflag[0] && !aflag[1] && !aflag[2] && !aflag[3])
			return false;
		boolean flag2 = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		double d = 0.0D;
		double d1 = 1.0D;
		Material material = block.blockMaterial;
		int i1 = blockAccess.getBlockMetadata(i, j, k);
		double d2 = getFluidHeight(i, j, k, material);
		double d3 = getFluidHeight(i, j, k + 1, material);
		double d4 = getFluidHeight(i + 1, j, k + 1, material);
		double d5 = getFluidHeight(i + 1, j, k, material);
		if((renderAllFaces) || (flag)) {
			flag2 = true;
			int j1 = block.getBlockTextureFromSideAndMetadata(1, i1);
			float f8 = (float) BlockFluid.func_293_a(blockAccess, i, j, k, material);
			if(f8 > -999.0F) {
				j1 = block.getBlockTextureFromSideAndMetadata(2, i1);
			}
			int i2 = (j1 & 0xF) << 4;
			int k2 = j1 & RenderEngine.TEX;
			double d6 = (i2 + 8.0D) / 256.0D;
			double d7 = (k2 + 8.0D) / RenderEngine.TERRAIN_HEIGHT_D;
			if(f8 < -999.0F) {
				f8 = 0.0F;
			} else {
				d6 = (i2 + 16) / 256.0F;
				d7 = (k2 + 16) / RenderEngine.TERRAIN_HEIGHT_F;
			}
			double d9 = MathHelper.sin(f8) * 8.0F / 256.0D;
			double d11 = MathHelper.cos(f8) * 8.0F / 256.0D;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
			double d9_1 = d9 / RenderEngine.TERRAIN_MULT;
			double d11_1 = d11 / RenderEngine.TERRAIN_MULT;

			float f9 = 1.0F;
			float curr_normal = 0.0F;
			float offset = 0.0F;
			tessellator.setColorOpaque_F(f4 * f9 * f, f4 * f9 * f1, f4 * f9 * f2);
			if((d2 >= 0.88D) && (d2 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 0, j + d2, k + 0, d6 - d11 - d9, d7 - d11_1 + d9_1);
			tessellator.setNormal(0.0F, curr_normal, 0.0F);
			if((d3 >= 0.88D) && (d3 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 0, j + d3, k + 1, d6 - d11 + d9, d7 + d11_1 + d9_1);
			tessellator.setNormal(0.0F, curr_normal, 0.0F);
			if((d4 >= 0.88D) && (d4 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 1, j + d4, k + 1, d6 + d11 + d9, d7 + d11_1 - d9_1);
			tessellator.setNormal(0.0F, curr_normal, 0.0F);
			if((d5 >= 0.88D) && (d5 <= 0.89D))
				tessellator.setNormal(0.0F, normal_value, 0.0F);
			else
				tessellator.setNormal(0.0F, curr_normal, 0.0F);
			tessellator.addVertexWithUV(i + 1, j + d5, k + 0, d6 + d11 - d9, d7 - d11_1 - d9_1);
		}

		if((renderAllFaces) || (flag1)) {
			tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
			float f7 = 1.0F;
			tessellator.setColorOpaque_F(f3 * f7, f3 * f7, f3 * f7);
			renderBottomFace(block, i, j, k, block.getBlockTextureFromSide(0));
			flag2 = true;
		}
		for(int k1 = 0; k1 < 4; k1++) {
			int l1 = i;
			int j2 = j;
			int l2 = k;
			if(k1 == 0) {
				l2--;
			}
			if(k1 == 1) {
				l2++;
			}
			if(k1 == 2) {
				l1--;
			}
			if(k1 == 3) {
				l1++;
			}
			int i3 = block.getBlockTextureFromSideAndMetadata(k1 + 2, i1);
			int j3 = (i3 & 0xF) << 4;
			int k3 = i3 & RenderEngine.TEX;
			if((!renderAllFaces) && (!aflag[k1]))
				continue;
			double d15;
			double d8;
			double d10;
			double d12;
			double d14;
			double d13;
			if(k1 == 0) {
				d8 = d2;
				d10 = d5;
				d12 = i;
				d14 = i + 1;
				d13 = k;
				d15 = k;
			} else {
				if(k1 == 1) {
					d8 = d4;
					d10 = d3;
					d12 = i + 1;
					d14 = i;
					d13 = k + 1;
					d15 = k + 1;
				} else {
					if(k1 == 2) {
						d8 = d3;
						d10 = d2;
						d12 = i;
						d14 = i;
						d13 = k + 1;
						d15 = k;
					} else {
						d8 = d5;
						d10 = d4;
						d12 = i + 1;
						d14 = i + 1;
						d13 = k;
						d15 = k + 1;
					}
				}
			}
			flag2 = true;
			double d16 = (j3 + 0) / 256.0F;
			double d17 = (j3 + 16 - 0.01D) / 256.0D;
			double d18 = (k3 + (1.0D - d8) * 16.0D) / RenderEngine.TERRAIN_HEIGHT_D;
			double d19 = (k3 + (1.0D - d10) * 16.0D) / RenderEngine.TERRAIN_HEIGHT_D;
			double d20 = (k3 + 16 - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, l1, j2, l2));
			float f10 = 1.0F;
			if(k1 < 2) {
				f10 *= f5;
			} else {
				f10 *= f6;
			}

			tessellator.setNormal(0.0F, 0.0F, 0.0F);

			tessellator.setColorOpaque_F(f4 * f10 * f, f4 * f10 * f1, f4 * f10 * f2);
			tessellator.addVertexWithUV(d12, j + d8, d13, d16, d18);
			tessellator.addVertexWithUV(d14, j + d10, d15, d17, d19);
			tessellator.addVertexWithUV(d14, j + 0, d15, d17, d20);
			tessellator.addVertexWithUV(d12, j + 0, d13, d16, d20);
		}

		block.minY = d;
		block.maxY = d1;
		return flag2;
	}

	public float getFluidHeight(int i, int j, int k, Material material) {
		int l = 0;
		float f = 0.0F;
		for(int i1 = 0; i1 < 4; i1++) {
			int j1 = i - (i1 & 1);
			int k1 = k - (i1 >> 1 & 1);
			if(blockAccess.getBlockMaterial(j1, j + 1, k1) == material) {
				return 1.0F;
			}
			Material material1 = blockAccess.getBlockMaterial(j1, j, k1);
			if(material1 == material) {
				int l1 = blockAccess.getBlockMetadata(j1, j, k1);
				if(l1 >= 8 || l1 == 0) {
					f += BlockFluid.getFluidHeightPercent(l1) * 10F;
					l += 10;
				}
				f += BlockFluid.getFluidHeightPercent(l1);
				l++;
			} else if(!material1.isSolid()) {
				f++;
				l++;
			}
		}

		return 1.0F - f / l;
	}

	public float getFluidHeightForSnow(int i, int j, int k, Material material) {
		int l = 0;
		float f = 0.0F;
		for(int i1 = 0; i1 < 4; i1++) {
			int j1 = i - (i1 & 1);
			int k1 = k - (i1 >> 1 & 1);
			if(blockAccess.getBlockMaterial(j1, j + 1, k1) == material) {
				return 1.0F;
			}
			Material material1 = blockAccess.getBlockMaterial(j1, j, k1);
			if(material1 == material) {
				int l1 = 7 - blockAccess.getBlockMetadata(j1, j, k1);
				if(l1 >= 8 || l1 == 0) {
					f += BlockFluid.getFluidHeightPercent(l1) * 10F;
					l += 10;
				}
				f += BlockFluid.getFluidHeightPercent(l1);
				l++;
			} else if(!material1.isSolid()) {
				f++;
				l++;
			}
		}

		return 1.0F - f / l;
	}

	public void renderBlockFallingSand(Block block, World world, int i, int j, int k) {
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
		float f4 = 1.0F;
		float f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}
		tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
		renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(0));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}
		tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
		renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(1));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}
		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(2));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}
		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(3));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}
		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(4));
		f5 = 1.0F;
		if(f5 < f4) {
			f5 = f4;
		}
		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(5));
		tessellator.draw();
	}

	public boolean renderStandardBlock(Block block, int x, int y, int z) {
		int color = block.colorMultiplier(blockAccess, x, y, z);
		float red = (color >> 16 & 0xff) / 255F;
		float green = (color >> 8 & 0xff) / 255F;
		float blue = (color & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f3 = (red * 30F + green * 59F + blue * 11F) / 100F;
			float f4 = (red * 30F + green * 70F) / 100F;
			float f5 = (red * 30F + blue * 70F) / 100F;
			red = f3;
			green = f4;
			blue = f5;
		}
		return !Minecraft.isAmbientOcclusionEnabled() || Block.lightValue[block.blockID] != 0 ? renderStandardBlockWithColorMultiplier(block, x, y, z, red, green, blue) : renderStandardBlockWithAmbientOcclusion(block, x, y, z, red, green, blue);
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block block, int x, int y, int z, float red, float green, float blue) {
		enableAO = true;
		boolean flag = false;
		float f3 = lightValueOwn;
		float f10 = lightValueOwn;
		float f17 = lightValueOwn;
		float f24 = lightValueOwn;
		boolean applyColorToBottom = true;
		boolean applyColorToTop = true;
		boolean flag3 = true;
		boolean flag4 = true;
		boolean flag5 = true;
		boolean flag6 = true;
		lightValueOwn = block.getLightFade(blockAccess, x, y, z);
		aoLightValueXNeg = block.getLightFade(blockAccess, x - 1, y, z);
		aoLightValueYNeg = block.getLightFade(blockAccess, x, y - 1, z);
		aoLightValueZNeg = block.getLightFade(blockAccess, x, y, z - 1);
		aoLightValueXPos = block.getLightFade(blockAccess, x + 1, y, z);
		aoLightValueYPos = block.getLightFade(blockAccess, x, y + 1, z);
		aoLightValueZPos = block.getLightFade(blockAccess, x, y, z + 1);
		int brightnessSelf = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
		int brightnessXNeg = brightnessSelf;
		int brightnessYNeg = brightnessSelf;
		int brightnessZNeg = brightnessSelf;
		int brightnessXPos = brightnessSelf;
		int brightnessYPos = brightnessSelf;
		int brightnessZPos = brightnessSelf;
		if(block.minY <= 0.0D)
			brightnessYNeg = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
		if(block.maxY >= 1.0D)
			brightnessYPos = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
		if(block.minX <= 0.0D)
			brightnessXNeg = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
		if(block.maxX >= 1.0D)
			brightnessXPos = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
		if(block.minZ <= 0.0D)
			brightnessZNeg = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
		if(block.maxZ >= 1.0D)
			brightnessZPos = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(0xf000f);
		aoTransparentXYZPPC = Block.transparentMaterial[blockAccess.getBlockId(x + 1, y + 1, z)];
		aoTransparentXYZPNC = Block.transparentMaterial[blockAccess.getBlockId(x + 1, y - 1, z)];
		aoTransparentXYZPCP = Block.transparentMaterial[blockAccess.getBlockId(x + 1, y, z + 1)];
		aoTransparentXYZPCN = Block.transparentMaterial[blockAccess.getBlockId(x + 1, y, z - 1)];
		aoTransparentXYZNPC = Block.transparentMaterial[blockAccess.getBlockId(x - 1, y + 1, z)];
		aoTransparentXYZNNC = Block.transparentMaterial[blockAccess.getBlockId(x - 1, y - 1, z)];
		aoTransparentXYZNCN = Block.transparentMaterial[blockAccess.getBlockId(x - 1, y, z - 1)];
		aoTransparentXYZNCP = Block.transparentMaterial[blockAccess.getBlockId(x - 1, y, z + 1)];
		aoTransparentXYZCPP = Block.transparentMaterial[blockAccess.getBlockId(x, y + 1, z + 1)];
		aoTransparentXYZCPN = Block.transparentMaterial[blockAccess.getBlockId(x, y + 1, z - 1)];
		aoTransparentXYZCNP = Block.transparentMaterial[blockAccess.getBlockId(x, y - 1, z + 1)];
		aoTransparentXYZCNN = Block.transparentMaterial[blockAccess.getBlockId(x, y - 1, z - 1)];
		if(block.blockIndexInTexture == 3 || block.blockIndexInTexture == GreenTextures.grassStepTexture) {
			flag6 = false;
			flag5 = false;
			flag4 = false;
			flag3 = false;
			applyColorToBottom = false;
		}
		if(overrideBlockTexture >= 0) {
			flag6 = false;
			flag5 = false;
			flag4 = false;
			flag3 = false;
			applyColorToBottom = false;
			applyColorToTop = false;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0)) {
			float multipler = 1.0F;
			if(block.minY > 0)
				multipler = (float) (1.0F - block.minY);
			float f4;
			float f11;
			float f18;
			float f25;
			if(aoType > 0) {
				if(block.minY <= 0.0D)
					y--;
				aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
				aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
				aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
				aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
				aoLightValueScratchXYNN = block.getLightFade(blockAccess, x - 1, y, z);
				aoLightValueScratchYZNN = block.getLightFade(blockAccess, x, y, z - 1);
				aoLightValueScratchYZNP = block.getLightFade(blockAccess, x, y, z + 1);
				aoLightValueScratchXYPN = block.getLightFade(blockAccess, x + 1, y, z);
				if(!aoTransparentXYZCNN && !aoTransparentXYZNNC) {
					aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
					aoBrightnessXYZNNN = aoBrightnessXYNN;
				} else {
					aoLightValueScratchXYZNNN = block.getLightFade(blockAccess, x - 1, y, z - 1);
					aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
				}
				if(!aoTransparentXYZCNP && !aoTransparentXYZNNC) {
					aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
					aoBrightnessXYZNNP = aoBrightnessXYNN;
				} else {
					aoLightValueScratchXYZNNP = block.getLightFade(blockAccess, x - 1, y, z + 1);
					aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
				}
				if(!aoTransparentXYZCNN && !aoTransparentXYZPNC) {
					aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
					aoBrightnessXYZPNN = aoBrightnessXYPN;
				} else {
					aoLightValueScratchXYZPNN = block.getLightFade(blockAccess, x + 1, y, z - 1);
					aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
				}
				if(!aoTransparentXYZCNP && !aoTransparentXYZPNC) {
					aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
					aoBrightnessXYZPNP = aoBrightnessXYPN;
				} else {
					aoLightValueScratchXYZPNP = block.getLightFade(blockAccess, x + 1, y, z + 1);
					aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
				}
				if(block.minY <= 0.0D) {
					y++;
				}
				f4 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + aoLightValueYNeg) / 4F;
				f25 = (aoLightValueScratchYZNP + aoLightValueYNeg + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4F;
				f18 = (aoLightValueYNeg + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4F;
				f11 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + aoLightValueYNeg + aoLightValueScratchYZNN) / 4F;
				brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, brightnessYNeg);
				brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, brightnessYNeg);
				brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, brightnessYNeg);
				brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, brightnessYNeg);
			} else {
				f25 = aoLightValueYNeg;
				f18 = aoLightValueYNeg;
				f11 = aoLightValueYNeg;
				f4 = aoLightValueYNeg;
				brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = brightnessYNeg;
			}
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (applyColorToBottom ? red : 1.0F) * 0.5F;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (applyColorToBottom ? green : 1.0F) * 0.5F;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (applyColorToBottom ? blue : 1.0F) * 0.5F;
			colorRedTopLeft *= f4;
			colorGreenTopLeft *= f4;
			colorBlueTopLeft *= f4;
			colorRedBottomLeft *= f11;
			colorGreenBottomLeft *= f11;
			colorBlueBottomLeft *= f11;
			colorRedBottomRight *= f18;
			colorGreenBottomRight *= f18;
			colorBlueBottomRight *= f18;
			colorRedTopRight *= f25;
			colorGreenTopRight *= f25;
			colorBlueTopRight *= f25;
			renderBottomFace(block, x, y, z, block.getBlockTexture(blockAccess, x, y, z, 0));
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1)) {
			float cTL;
			float cBL;
			float cBR;
			float cTR;
			if(aoType > 0) {
				if(block.maxY >= 1.0D) {
					y++;
				}
				aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
				aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
				aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
				aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
				aoLightValueScratchXYNP = block.getLightFade(blockAccess, x - 1, y, z);
				aoLightValueScratchXYPP = block.getLightFade(blockAccess, x + 1, y, z);
				aoLightValueScratchYZPN = block.getLightFade(blockAccess, x, y, z - 1);
				aoLightValueScratchYZPP = block.getLightFade(blockAccess, x, y, z + 1);
				if(!aoTransparentXYZCPN && !aoTransparentXYZNPC) {
					aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
					aoBrightnessXYZNPN = aoBrightnessXYNP;
				} else {
					aoLightValueScratchXYZNPN = block.getLightFade(blockAccess, x - 1, y, z - 1);
					aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
				}
				if(!aoTransparentXYZCPN && !aoTransparentXYZPPC) {
					aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
					aoBrightnessXYZPPN = aoBrightnessXYPP;
				} else {
					aoLightValueScratchXYZPPN = block.getLightFade(blockAccess, x + 1, y, z - 1);
					aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
				}
				if(!aoTransparentXYZCPP && !aoTransparentXYZNPC) {
					aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
					aoBrightnessXYZNPP = aoBrightnessXYNP;
				} else {
					aoLightValueScratchXYZNPP = block.getLightFade(blockAccess, x - 1, y, z + 1);
					aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
				}
				if(!aoTransparentXYZCPP && !aoTransparentXYZPPC) {
					aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
					aoBrightnessXYZPPP = aoBrightnessXYPP;
				} else {
					aoLightValueScratchXYZPPP = block.getLightFade(blockAccess, x + 1, y, z + 1);
					aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
				}
				if(block.maxY >= 1.0D) {
					y--;
				}
				cTR = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + aoLightValueYPos) / 4F;
				cTL = (aoLightValueScratchYZPP + aoLightValueYPos + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4F;
				cBL = (aoLightValueYPos + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4F;
				cBR = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + aoLightValueYPos + aoLightValueScratchYZPN) / 4F;
				brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, brightnessYPos);
				brightnessTopLeft = getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, brightnessYPos);
				brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, brightnessYPos);
				brightnessBottomRight = getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, brightnessYPos);
			} else {
				cTR = aoLightValueYPos;
				cBR = aoLightValueYPos;
				cBL = aoLightValueYPos;
				cTL = aoLightValueYPos;
				brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = brightnessYPos;
			}
			float c_1 = cTL;
			float c_2 = cBL;
			float c_3 = cBR;
			float c_4 = cTR;
			//if(block.maxZ < 1 || block.maxX < 1) {
			//	c_1 = (float) (cTL * (block.maxX * block.maxZ) + cTR * (1 - block.maxX) * (block.maxZ) + cBL * (1 - block.maxX) * (1 - block.maxZ) + cBR * (block.maxX) * (1 - block.maxZ));  
			//}
			//if(block.maxZ < 1 || block.minX > 0) {
			//	c_4 = (float) (cTL * (block.maxZ) * (1 - block.maxX) + cTR * (block.maxZ) * (1 - block.minX) + cBL * (block.minX) * (1 - block.maxZ) + cBR * (1 - block.minX) * (1 - block.maxZ));
			//}
			//if(block.minX > 0 || block.minZ > 0) {
			//c_3 = (float) ((f12 - (f12 - f19) * (1 - block.minX))+ (f26 - (f26 - f19) * (1 - block.minZ))) / 2.0F;
			//}
			//if(block.maxX < 1 || block.minZ > 0) {
			//c_2 = (float) ((f19 - (f19 - f12) * block.maxX) + (f5 - (f5 - f12) * (1 - block.minZ))) / 2.0F; 
			//}
			//c_1 = c_2 = c_3 = c_4 = 1;
			//int tl = brightnessTopLeft;
			//int tr = brightnessTopRight;
			//int bl = brightnessBottomLeft;
			//int br = brightnessBottomRight;
			//brightnessTopLeft = brightnessTopRight = brightnessBottomRight = brightnessBottomLeft;
			//brightnessTopLeft = this.mixAoBrightness(tl, tr, bl, br, block.maxX * block.maxZ, (1 - block.maxX) * (block.maxZ), (1 - block.maxX) * (1 - block.maxZ), (block.maxX) * (1 - block.maxZ));
			//brightnessTopRight = this.mixAoBrightness(tl, tr, bl, br, (block.maxZ) * (1 - block.maxX), (block.maxZ) * (1 - block.minX), (block.minX) * (1 - block.maxZ), (1 - block.minX) * (1 - block.maxZ));
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = applyColorToTop ? red : 1.0F;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = applyColorToTop ? green : 1.0F;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = applyColorToTop ? blue : 1.0F;
			colorRedTopLeft *= c_1;
			colorGreenTopLeft *= c_1;
			colorBlueTopLeft *= c_1;
			colorRedBottomLeft *= c_2;
			colorGreenBottomLeft *= c_2;
			colorBlueBottomLeft *= c_2;
			colorRedBottomRight *= c_3;
			colorGreenBottomRight *= c_3;
			colorBlueBottomRight *= c_3;
			colorRedTopRight *= c_4;
			colorGreenTopRight *= c_4;
			colorBlueTopRight *= c_4;
			renderTopFace(block, x, y, z, block.getBlockTexture(blockAccess, x, y, z, 1));
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2)) {
			float f6;
			float f13;
			float f20;
			float f27;
			if(aoType > 0) {
				if(block.minZ <= 0.0D) {
					z--;
				}
				aoLightValueScratchXZNN = block.getLightFade(blockAccess, x - 1, y, z);
				aoLightValueScratchYZNN = block.getLightFade(blockAccess, x, y - 1, z);
				aoLightValueScratchYZPN = block.getLightFade(blockAccess, x, y + 1, z);
				aoLightValueScratchXZPN = block.getLightFade(blockAccess, x + 1, y, z);
				aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
				aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
				aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
				aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
				if(!aoTransparentXYZNCN && !aoTransparentXYZCNN) {
					aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
					aoBrightnessXYZNNN = aoBrightnessXZNN;
				} else {
					aoLightValueScratchXYZNNN = block.getLightFade(blockAccess, x - 1, y - 1, z);
					aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
				}
				if(!aoTransparentXYZNCN && !aoTransparentXYZCPN) {
					aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
					aoBrightnessXYZNPN = aoBrightnessXZNN;
				} else {
					aoLightValueScratchXYZNPN = block.getLightFade(blockAccess, x - 1, y + 1, z);
					aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
				}
				if(!aoTransparentXYZPCN && !aoTransparentXYZCNN) {
					aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
					aoBrightnessXYZPNN = aoBrightnessXZPN;
				} else {
					aoLightValueScratchXYZPNN = block.getLightFade(blockAccess, x + 1, y - 1, z);
					aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
				}
				if(!aoTransparentXYZPCN && !aoTransparentXYZCPN) {
					aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
					aoBrightnessXYZPPN = aoBrightnessXZPN;
				} else {
					aoLightValueScratchXYZPPN = block.getLightFade(blockAccess, x + 1, y + 1, z);
					aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
				}
				if(block.minZ <= 0.0D) {
					z++;
				}
				f6 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + aoLightValueZNeg + aoLightValueScratchYZPN) / 4F;
				f13 = (aoLightValueZNeg + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4F;
				f20 = (aoLightValueScratchYZNN + aoLightValueZNeg + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4F;
				f27 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + aoLightValueZNeg) / 4F;
				brightnessTopLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, brightnessZNeg);
				brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, brightnessZNeg);
				brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, brightnessZNeg);
				brightnessTopRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, brightnessZNeg);
			} else {
				f27 = aoLightValueZNeg;
				f20 = aoLightValueZNeg;
				f13 = aoLightValueZNeg;
				f6 = aoLightValueZNeg;
				brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = brightnessZNeg;
			}
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag3 ? red : 1.0F) * 0.8F;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag3 ? green : 1.0F) * 0.8F;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag3 ? blue : 1.0F) * 0.8F;
			colorRedTopLeft *= f6;
			colorGreenTopLeft *= f6;
			colorBlueTopLeft *= f6;
			colorRedBottomLeft *= f13;
			colorGreenBottomLeft *= f13;
			colorBlueBottomLeft *= f13;
			colorRedBottomRight *= f20;
			colorGreenBottomRight *= f20;
			colorBlueBottomRight *= f20;
			colorRedTopRight *= f27;
			colorGreenTopRight *= f27;
			colorBlueTopRight *= f27;
			int k2 = block.getBlockTexture(blockAccess, x, y, z, 2);
			renderEastFace(block, x, y, z, k2);
			if(cfgGrassFix && (k2 == 3 || k2 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) {
				colorRedTopLeft *= red;
				colorRedBottomLeft *= red;
				colorRedBottomRight *= red;
				colorRedTopRight *= red;
				colorGreenTopLeft *= green;
				colorGreenBottomLeft *= green;
				colorGreenBottomRight *= green;
				colorGreenTopRight *= green;
				colorBlueTopLeft *= blue;
				colorBlueBottomLeft *= blue;
				colorBlueBottomRight *= blue;
				colorBlueTopRight *= blue;
				renderEastFace(block, x, y, z, k2 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3)) {
			float f7;
			float f14;
			float f21;
			float f28;
			if(aoType > 0) {
				if(block.maxZ >= 1.0D) {
					z++;
				}
				aoLightValueScratchXZNP = block.getLightFade(blockAccess, x - 1, y, z);
				aoLightValueScratchXZPP = block.getLightFade(blockAccess, x + 1, y, z);
				aoLightValueScratchYZNP = block.getLightFade(blockAccess, x, y - 1, z);
				aoLightValueScratchYZPP = block.getLightFade(blockAccess, x, y + 1, z);
				aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
				aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
				aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
				aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
				if(!aoTransparentXYZNCP && !aoTransparentXYZCNP) {
					aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
					aoBrightnessXYZNNP = aoBrightnessXZNP;
				} else {
					aoLightValueScratchXYZNNP = block.getLightFade(blockAccess, x - 1, y - 1, z);
					aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
				}
				if(!aoTransparentXYZNCP && !aoTransparentXYZCPP) {
					aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
					aoBrightnessXYZNPP = aoBrightnessXZNP;
				} else {
					aoLightValueScratchXYZNPP = block.getLightFade(blockAccess, x - 1, y + 1, z);
					aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
				}
				if(!aoTransparentXYZPCP && !aoTransparentXYZCNP) {
					aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
					aoBrightnessXYZPNP = aoBrightnessXZPP;
				} else {
					aoLightValueScratchXYZPNP = block.getLightFade(blockAccess, x + 1, y - 1, z);
					aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
				}
				if(!aoTransparentXYZPCP && !aoTransparentXYZCPP) {
					aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
					aoBrightnessXYZPPP = aoBrightnessXZPP;
				} else {
					aoLightValueScratchXYZPPP = block.getLightFade(blockAccess, x + 1, y + 1, z);
					aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
				}
				if(block.maxZ >= 1.0D) {
					z--;
				}
				f7 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + aoLightValueZPos + aoLightValueScratchYZPP) / 4F;
				f28 = (aoLightValueZPos + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4F;
				f21 = (aoLightValueScratchYZNP + aoLightValueZPos + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4F;
				f14 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + aoLightValueZPos) / 4F;
				brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, brightnessZPos);
				brightnessTopRight = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, brightnessZPos);
				brightnessBottomRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, brightnessZPos);
				brightnessBottomLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, brightnessZPos);
			} else {
				f28 = aoLightValueZPos;
				f21 = aoLightValueZPos;
				f14 = aoLightValueZPos;
				f7 = aoLightValueZPos;
				brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = brightnessZPos;
			}
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag4 ? red : 1.0F) * 0.8F;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag4 ? green : 1.0F) * 0.8F;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag4 ? blue : 1.0F) * 0.8F;
			colorRedTopLeft *= f7;
			colorGreenTopLeft *= f7;
			colorBlueTopLeft *= f7;
			colorRedBottomLeft *= f14;
			colorGreenBottomLeft *= f14;
			colorBlueBottomLeft *= f14;
			colorRedBottomRight *= f21;
			colorGreenBottomRight *= f21;
			colorBlueBottomRight *= f21;
			colorRedTopRight *= f28;
			colorGreenTopRight *= f28;
			colorBlueTopRight *= f28;
			int l2 = block.getBlockTexture(blockAccess, x, y, z, 3);
			renderWestFace(block, x, y, z, block.getBlockTexture(blockAccess, x, y, z, 3));
			if(cfgGrassFix && (l2 == 3 || l2 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) // GreenCubes
			// grass
			// step
			{
				colorRedTopLeft *= red;
				colorRedBottomLeft *= red;
				colorRedBottomRight *= red;
				colorRedTopRight *= red;
				colorGreenTopLeft *= green;
				colorGreenBottomLeft *= green;
				colorGreenBottomRight *= green;
				colorGreenTopRight *= green;
				colorBlueTopLeft *= blue;
				colorBlueBottomLeft *= blue;
				colorBlueBottomRight *= blue;
				colorBlueTopRight *= blue;
				renderWestFace(block, x, y, z, l2 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4)) {
			float f8;
			float f15;
			float f22;
			float f29;
			if(aoType > 0) {
				if(block.minX <= 0.0D) {
					x--;
				}
				aoLightValueScratchXYNN = block.getLightFade(blockAccess, x, y - 1, z);
				aoLightValueScratchXZNN = block.getLightFade(blockAccess, x, y, z - 1);
				aoLightValueScratchXZNP = block.getLightFade(blockAccess, x, y, z + 1);
				aoLightValueScratchXYNP = block.getLightFade(blockAccess, x, y + 1, z);
				aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
				aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
				aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
				aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
				if(!aoTransparentXYZNCN && !aoTransparentXYZNNC) {
					aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
					aoBrightnessXYZNNN = aoBrightnessXZNN;
				} else {
					aoLightValueScratchXYZNNN = block.getLightFade(blockAccess, x, y - 1, z - 1);
					aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
				}
				if(!aoTransparentXYZNCP && !aoTransparentXYZNNC) {
					aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
					aoBrightnessXYZNNP = aoBrightnessXZNP;
				} else {
					aoLightValueScratchXYZNNP = block.getLightFade(blockAccess, x, y - 1, z + 1);
					aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
				}
				if(!aoTransparentXYZNCN && !aoTransparentXYZNPC) {
					aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
					aoBrightnessXYZNPN = aoBrightnessXZNN;
				} else {
					aoLightValueScratchXYZNPN = block.getLightFade(blockAccess, x, y + 1, z - 1);
					aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
				}
				if(!aoTransparentXYZNCP && !aoTransparentXYZNPC) {
					aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
					aoBrightnessXYZNPP = aoBrightnessXZNP;
				} else {
					aoLightValueScratchXYZNPP = block.getLightFade(blockAccess, x, y + 1, z + 1);
					aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
				}
				if(block.minX <= 0.0D) {
					x++;
				}
				f29 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + aoLightValueXNeg + aoLightValueScratchXZNP) / 4F;
				f8 = (aoLightValueXNeg + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4F;
				f15 = (aoLightValueScratchXZNN + aoLightValueXNeg + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4F;
				f22 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + aoLightValueXNeg) / 4F;
				brightnessTopRight = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, brightnessXNeg);
				brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, brightnessXNeg);
				brightnessBottomLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, brightnessXNeg);
				brightnessBottomRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, brightnessXNeg);
			} else {
				f29 = aoLightValueXNeg;
				f22 = aoLightValueXNeg;
				f15 = aoLightValueXNeg;
				f8 = aoLightValueXNeg;
				brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = brightnessXNeg;
			}
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag5 ? red : 1.0F) * 0.6F;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag5 ? green : 1.0F) * 0.6F;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag5 ? blue : 1.0F) * 0.6F;
			colorRedTopLeft *= f8;
			colorGreenTopLeft *= f8;
			colorBlueTopLeft *= f8;
			colorRedBottomLeft *= f15;
			colorGreenBottomLeft *= f15;
			colorBlueBottomLeft *= f15;
			colorRedBottomRight *= f22;
			colorGreenBottomRight *= f22;
			colorBlueBottomRight *= f22;
			colorRedTopRight *= f29;
			colorGreenTopRight *= f29;
			colorBlueTopRight *= f29;
			int i3 = block.getBlockTexture(blockAccess, x, y, z, 4);
			renderNorthFace(block, x, y, z, i3);
			if(cfgGrassFix && (i3 == 3 || i3 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) // GreenCubes
			// grass
			// step
			{
				colorRedTopLeft *= red;
				colorRedBottomLeft *= red;
				colorRedBottomRight *= red;
				colorRedTopRight *= red;
				colorGreenTopLeft *= green;
				colorGreenBottomLeft *= green;
				colorGreenBottomRight *= green;
				colorGreenTopRight *= green;
				colorBlueTopLeft *= blue;
				colorBlueBottomLeft *= blue;
				colorBlueBottomRight *= blue;
				colorBlueTopRight *= blue;
				renderNorthFace(block, x, y, z, i3 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5)) {
			float f9;
			float f16;
			float f23;
			float f30;
			if(aoType > 0) {
				if(block.maxX >= 1.0D) {
					x++;
				}
				aoLightValueScratchXYPN = block.getLightFade(blockAccess, x, y - 1, z);
				aoLightValueScratchXZPN = block.getLightFade(blockAccess, x, y, z - 1);
				aoLightValueScratchXZPP = block.getLightFade(blockAccess, x, y, z + 1);
				aoLightValueScratchXYPP = block.getLightFade(blockAccess, x, y + 1, z);
				aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
				aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
				aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
				aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
				if(!aoTransparentXYZPNC && !aoTransparentXYZPCN) {
					aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
					aoBrightnessXYZPNN = aoBrightnessXZPN;
				} else {
					aoLightValueScratchXYZPNN = block.getLightFade(blockAccess, x, y - 1, z - 1);
					aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
				}
				if(!aoTransparentXYZPNC && !aoTransparentXYZPCP) {
					aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
					aoBrightnessXYZPNP = aoBrightnessXZPP;
				} else {
					aoLightValueScratchXYZPNP = block.getLightFade(blockAccess, x, y - 1, z + 1);
					aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
				}
				if(!aoTransparentXYZPPC && !aoTransparentXYZPCN) {
					aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
					aoBrightnessXYZPPN = aoBrightnessXZPN;
				} else {
					aoLightValueScratchXYZPPN = block.getLightFade(blockAccess, x, y + 1, z - 1);
					aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
				}
				if(!aoTransparentXYZPPC && !aoTransparentXYZPCP) {
					aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
					aoBrightnessXYZPPP = aoBrightnessXZPP;
				} else {
					aoLightValueScratchXYZPPP = block.getLightFade(blockAccess, x, y + 1, z + 1);
					aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
				}
				if(block.maxX >= 1.0D) {
					x--;
				}
				f9 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + aoLightValueXPos + aoLightValueScratchXZPP) / 4F;
				f30 = (aoLightValueXPos + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4F;
				f23 = (aoLightValueScratchXZPN + aoLightValueXPos + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4F;
				f16 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + aoLightValueXPos) / 4F;
				brightnessTopLeft = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, brightnessXPos);
				brightnessTopRight = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, brightnessXPos);
				brightnessBottomRight = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, brightnessXPos);
				brightnessBottomLeft = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, brightnessXPos);
			} else {
				f30 = aoLightValueXPos;
				f23 = aoLightValueXPos;
				f16 = aoLightValueXPos;
				f9 = aoLightValueXPos;
				brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = brightnessXPos;
			}
			colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag6 ? red : 1.0F) * 0.6F;
			colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag6 ? green : 1.0F) * 0.6F;
			colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag6 ? blue : 1.0F) * 0.6F;
			colorRedTopLeft *= f9;
			colorGreenTopLeft *= f9;
			colorBlueTopLeft *= f9;
			colorRedBottomLeft *= f16;
			colorGreenBottomLeft *= f16;
			colorBlueBottomLeft *= f16;
			colorRedBottomRight *= f23;
			colorGreenBottomRight *= f23;
			colorBlueBottomRight *= f23;
			colorRedTopRight *= f30;
			colorGreenTopRight *= f30;
			colorBlueTopRight *= f30;
			int j3 = block.getBlockTexture(blockAccess, x, y, z, 5);
			renderSouthFace(block, x, y, z, j3);
			if(cfgGrassFix && (j3 == 3 || j3 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) // GreenCubes
			// grass
			// step
			{
				colorRedTopLeft *= red;
				colorRedBottomLeft *= red;
				colorRedBottomRight *= red;
				colorRedTopRight *= red;
				colorGreenTopLeft *= green;
				colorGreenBottomLeft *= green;
				colorGreenBottomRight *= green;
				colorGreenTopRight *= green;
				colorBlueTopLeft *= blue;
				colorBlueBottomLeft *= blue;
				colorBlueBottomRight *= blue;
				colorBlueTopRight *= blue;
				renderSouthFace(block, x, y, z, j3 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		enableAO = false;
		return flag;
	}

	private int mixAoBrightness(int topLeft, int topRight, int bottomLeft, int bottomRight, double topLeftPart, double topRightPart, double bottomRightPart, double bottomLeftPart) {
		int var13 = (int) ((topLeft >> 16 & 255) * topLeftPart + (topRight >> 16 & 255) * topRightPart + (bottomLeft >> 16 & 255) * bottomRightPart + (bottomRight >> 16 & 255) * bottomLeftPart) & 255;
		int var14 = (int) ((topLeft & 255) * topLeftPart + (topRight & 255) * topRightPart + (bottomLeft & 255) * bottomRightPart + (bottomRight & 255) * bottomLeftPart) & 255;
		return var13 << 16 | var14;
	}

	public int getAoBrightness(int i, int j, int k, int l) {
		if(i == 0) {
			i = l;
		}
		if(j == 0) {
			j = l;
		}
		if(k == 0) {
			k = l;
		}
		return i + j + k + l >> 2 & 0xff00ff;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block block, int i, int j, int k, float f, float f1, float f2) {
		enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f4 * f;
		float f8 = f4 * f1;
		float f9 = f4 * f2;
		float f10 = f3;
		float f11 = f5;
		float f12 = f6;
		float f13 = f3;
		float f14 = f5;
		float f15 = f6;
		float f16 = f3;
		float f17 = f5;
		float f18 = f6;
		if(block != Block.grass && block != Block.blockHalfBlockGrass && block != Block.blockStepGrass && block != Block.blockStepGrassUp) { // GreenCubes
			f10 = f3 * f;
			f11 = f5 * f;
			f12 = f6 * f;
			f13 = f3 * f1;
			f14 = f5 * f1;
			f15 = f6 * f1;
			f16 = f3 * f2;
			f17 = f5 * f2;
			f18 = f6 * f2;
		}
		int l = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0)) {
			tessellator.setBrightness(block.minY <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k) : l);
			tessellator.setColorOpaque_F(f10, f13, f16);
			renderBottomFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 0));
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1)) {
			tessellator.setBrightness(block.maxY >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j + 1, k) : l);
			tessellator.setColorOpaque_F(f7, f8, f9);
			renderTopFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 1));
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2)) {
			tessellator.setBrightness(block.minZ <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k - 1) : l);
			tessellator.setColorOpaque_F(f11, f14, f17);
			int i1 = block.getBlockTexture(blockAccess, i, j, k, 2);
			renderEastFace(block, i, j, k, i1);
			if(cfgGrassFix && (i1 == 3 || i1 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) // GreenCubes
			// grass
			// step
			{
				tessellator.setColorOpaque_F(f11 * f, f14 * f1, f17 * f2);
				renderEastFace(block, i, j, k, i1 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3)) {
			tessellator.setBrightness(block.maxZ >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k + 1) : l);
			tessellator.setColorOpaque_F(f11, f14, f17);
			int j1 = block.getBlockTexture(blockAccess, i, j, k, 3);
			renderWestFace(block, i, j, k, j1);
			if(cfgGrassFix && (j1 == 3 || j1 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) // Greencubes
			// grass
			// step
			{
				tessellator.setColorOpaque_F(f11 * f, f14 * f1, f17 * f2);
				renderWestFace(block, i, j, k, j1 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4)) {
			tessellator.setBrightness(block.minX <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i - 1, j, k) : l);
			tessellator.setColorOpaque_F(f12, f15, f18);
			int k1 = block.getBlockTexture(blockAccess, i, j, k, 4);
			renderNorthFace(block, i, j, k, k1);
			if(cfgGrassFix && (k1 == 3 || k1 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) // GreenCubes
			{
				tessellator.setColorOpaque_F(f12 * f, f15 * f1, f18 * f2);
				renderNorthFace(block, i, j, k, k1 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5)) {
			tessellator.setBrightness(block.maxX >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i + 1, j, k) : l);
			tessellator.setColorOpaque_F(f12, f15, f18);
			int l1 = block.getBlockTexture(blockAccess, i, j, k, 5);
			renderSouthFace(block, i, j, k, l1);
			if(cfgGrassFix && (l1 == 3 || l1 == GreenTextures.grassStepTexture) && overrideBlockTexture < 0) // GreenCubes
			{
				tessellator.setColorOpaque_F(f12 * f, f15 * f1, f18 * f2);
				renderSouthFace(block, i, j, k, l1 == 3 ? 38 : GreenTextures.grassstepoverlay);
			}
			flag = true;
		}
		return flag;
	}

	public boolean renderBlockCactus(Block block, int i, int j, int k) {
		int l = block.colorMultiplier(blockAccess, i, j, k);
		float f = (l >> 16 & 0xff) / 255F;
		float f1 = (l >> 8 & 0xff) / 255F;
		float f2 = (l & 0xff) / 255F;
		if(EntityRenderer.anaglyphEnable) {
			float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
			float f4 = (f * 30F + f1 * 70F) / 100F;
			float f5 = (f * 30F + f2 * 70F) / 100F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}
		return renderBlockCactusImpl(block, i, j, k, f, f1, f2);
	}

	public boolean renderBlockCactusImpl(Block block, int i, int j, int k, float f, float f1, float f2) {
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f3 * f;
		float f8 = f4 * f;
		float f9 = f5 * f;
		float f10 = f6 * f;
		float f11 = f3 * f1;
		float f12 = f4 * f1;
		float f13 = f5 * f1;
		float f14 = f6 * f1;
		float f15 = f3 * f2;
		float f16 = f4 * f2;
		float f17 = f5 * f2;
		float f18 = f6 * f2;
		float f19 = 0.0625F;
		int l = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0)) {
			tessellator.setBrightness(block.minY <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k) : l);
			tessellator.setColorOpaque_F(f7, f11, f15);
			renderBottomFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 0));
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1)) {
			tessellator.setBrightness(block.maxY >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j + 1, k) : l);
			tessellator.setColorOpaque_F(f8, f12, f16);
			renderTopFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 1));
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2)) {
			tessellator.setBrightness(block.minZ <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k - 1) : l);
			tessellator.setColorOpaque_F(f9, f13, f17);
			tessellator.setTranslationF(0.0F, 0.0F, f19);
			renderEastFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 2));
			tessellator.setTranslationF(0.0F, 0.0F, -f19);
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3)) {
			tessellator.setBrightness(block.maxZ >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k + 1) : l);
			tessellator.setColorOpaque_F(f9, f13, f17);
			tessellator.setTranslationF(0.0F, 0.0F, -f19);
			renderWestFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 3));
			tessellator.setTranslationF(0.0F, 0.0F, f19);
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4)) {
			tessellator.setBrightness(block.minX <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i - 1, j, k) : l);
			tessellator.setColorOpaque_F(f10, f14, f18);
			tessellator.setTranslationF(f19, 0.0F, 0.0F);
			renderNorthFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 4));
			tessellator.setTranslationF(-f19, 0.0F, 0.0F);
			flag = true;
		}
		if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5)) {
			tessellator.setBrightness(block.maxX >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i + 1, j, k) : l);
			tessellator.setColorOpaque_F(f10, f14, f18);
			tessellator.setTranslationF(-f19, 0.0F, 0.0F);
			renderSouthFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 5));
			tessellator.setTranslationF(f19, 0.0F, 0.0F);
			flag = true;
		}
		return flag;
	}

	public boolean renderBlockFence(BlockFence blockfence, int i, int j, int k) {
		boolean flag = false;
		float f = 0.375F; // ������ �������� � ������
		float f1 = 0.625F; // ������ �������� � �������
		blockfence.setBlockBounds(f, 0.0F, f, f1, 1.0F, f1);
		renderStandardBlock(blockfence, i, j, k);
		flag = true;
		boolean flag1 = false;
		boolean flag2 = false;
		if(blockfence.canAttachTo(blockAccess, i - 1, j, k) || blockfence.canAttachTo(blockAccess, i + 1, j, k)) {
			flag1 = true;
		}
		if(blockfence.canAttachTo(blockAccess, i, j, k - 1) || blockfence.canAttachTo(blockAccess, i, j, k + 1)) {
			flag2 = true;
		}
		boolean flag3 = blockfence.canAttachTo(blockAccess, i - 1, j, k);
		boolean flag4 = blockfence.canAttachTo(blockAccess, i + 1, j, k);
		boolean flag5 = blockfence.canAttachTo(blockAccess, i, j, k - 1);
		boolean flag6 = blockfence.canAttachTo(blockAccess, i, j, k + 1);
		if(!flag1 && !flag2) {
			flag1 = true;
		}
		f = 0.4375F; // ������ ������� ������ �������
		f1 = 0.5625F; // ������� ������� ������ �������
		float f2 = 0.75F; // ������� ������� ������� �������
		float f3 = 0.9375F; // ������ ������� ������� �������
		float f4 = flag3 ? 0.0F : f;
		float f5 = flag4 ? 1.0F : f1;
		float f6 = flag5 ? 0.0F : f;
		float f7 = flag6 ? 1.0F : f1;
		if(flag1) {
			blockfence.setBlockBounds(f4, f2, f, f5, f3, f1); // ������� �������
																// �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		if(flag2) {
			blockfence.setBlockBounds(f, f2, f6, f1, f3, f7); // ������� �������
																// �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		f2 = 0.375F;
		f3 = 0.5625F;
		if(flag1) {
			blockfence.setBlockBounds(f4, f2, f, f5, f3, f1); // �������
			// ������ �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		if(flag2) {
			blockfence.setBlockBounds(f, f2, f6, f1, f3, f7); // �������
			// ������ �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		blockfence.setBlockBoundsBasedOnState(blockAccess, i, j, k);
		return flag;
	}

	public boolean renderBlockWhiteFence(BlockFence blockfence, int i, int j, int k) {
		boolean flag = false;
		float f = 0.375F; // ������ �������� � ������
		float f1 = 0.625F; // ������ �������� � �������
		blockfence.setBlockBounds(f, 0.0F, f, f1, 1.0F, f1);
		renderStandardBlock(blockfence, i, j, k);
		flag = true;
		boolean flag1 = false;
		boolean flag2 = false;
		if(blockfence.canAttachTo(blockAccess, i - 1, j, k) || blockfence.canAttachTo(blockAccess, i + 1, j, k)) {
			flag1 = true;
		}
		if(blockfence.canAttachTo(blockAccess, i, j, k - 1) || blockfence.canAttachTo(blockAccess, i, j, k + 1)) {
			flag2 = true;
		}
		boolean flag3 = blockfence.canAttachTo(blockAccess, i - 1, j, k);
		boolean flag4 = blockfence.canAttachTo(blockAccess, i + 1, j, k);
		boolean flag5 = blockfence.canAttachTo(blockAccess, i, j, k - 1);
		boolean flag6 = blockfence.canAttachTo(blockAccess, i, j, k + 1);
		if(!flag1 && !flag2) {
			flag1 = true;
		}
		f = 0.4375F; // ������ ������� ������ �������, ������� �������
		f1 = 0.5625F; // ������� ������� ������ �������
		float f2 = 0.75F; // ������ ������� ������� �������
		float f3 = 0.9375F; // ������� ������� ������� �������
		float f4 = flag3 ? 0.0F : f;
		float f5 = flag4 ? 1.0F : f1;
		float f6 = flag5 ? 0.0F : f;
		float f7 = flag6 ? 1.0F : f1;
		if(flag1) {
			blockfence.setBlockBounds(f4, f2, f, f5, f3, f1); // ������� �������
																// �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		if(flag2) {
			blockfence.setBlockBounds(f, f2, f6, f1, f3, f7); // ������� �������
																// �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		f2 = 0.0625F; // ������ ������� �� ������ ������ �����
		f3 = 0.25F; // ������� ������� �� ������ ������ �����
		if(flag1) {
			blockfence.setBlockBounds(f4, f2, f, f5, f3, f1); // ������� ������
																// �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		if(flag2) {
			blockfence.setBlockBounds(f, f2, f6, f1, f3, f7); // ������� ������
																// �����
			renderStandardBlock(blockfence, i, j, k);
			flag = true;
		}
		blockfence.setBlockBoundsBasedOnState(blockAccess, i, j, k);
		return flag;
	}

	public boolean func_41088_a(BlockDragonEgg blockdragonegg, int i, int j, int k) {
		boolean flag = false;
		int l = 0;
		for(int i1 = 0; i1 < 8; i1++) {
			int j1 = 0;
			byte byte0 = 1;
			if(i1 == 0) {
				j1 = 2;
			}
			if(i1 == 1) {
				j1 = 3;
			}
			if(i1 == 2) {
				j1 = 4;
			}
			if(i1 == 3) {
				j1 = 5;
				byte0 = 2;
			}
			if(i1 == 4) {
				j1 = 6;
				byte0 = 3;
			}
			if(i1 == 5) {
				j1 = 7;
				byte0 = 5;
			}
			if(i1 == 6) {
				j1 = 6;
				byte0 = 2;
			}
			if(i1 == 7) {
				j1 = 3;
			}
			float f = j1 / 16F;
			float f1 = 1.0F - l / 16F;
			float f2 = 1.0F - (l + byte0) / 16F;
			l += byte0;
			blockdragonegg.setBlockBounds(0.5F - f, f2, 0.5F - f, 0.5F + f, f1, 0.5F + f);
			renderStandardBlock(blockdragonegg, i, j, k);
		}

		flag = true;
		blockdragonegg.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return flag;
	}

	public boolean renderBlockFenceGate(BlockFenceGate block, int x, int y, int z) {
		boolean var5 = true;
		int data = this.blockAccess.getBlockMetadata(x, y, z);
		boolean isOpen = BlockFenceGate.isFenceGateOpen(data);
		int direction = BlockDirectional.getDirection(data);
		float var9 = 0.375F;
		float var10 = 0.5625F;
		float var11 = 0.75F;
		float var12 = 0.9375F;
		float var13 = 0.3125F;
		float var14 = 1.0F;

		if((direction == 2 || direction == 0) && BlockWall.isWall(blockAccess.getBlockId(x - 1, y, z)) && BlockWall.isWall(blockAccess.getBlockId(x + 1, y, z)) || (direction == 3 || direction == 1) && BlockWall.isWall(blockAccess.getBlockId(x, y, z - 1)) && BlockWall.isWall(blockAccess.getBlockId(x, y, z + 1))) {
			var9 -= 0.1875F;
			var10 -= 0.1875F;
			var11 -= 0.1875F;
			var12 -= 0.1875F;
			var13 -= 0.1875F;
			var14 -= 0.1875F;
		}

		this.renderAllFaces = true;
		float var15;
		float var17;
		float var16;
		float var18;

		if(direction != 3 && direction != 1) {
			var15 = 0.0F;
			var16 = 0.125F;
			var17 = 0.4375F;
			var18 = 0.5625F;
			block.setBlockBounds(var15, var13, var17, var16, var14, var18);
			this.renderStandardBlock(block, x, y, z);
			var15 = 0.875F;
			var16 = 1.0F;
			block.setBlockBounds(var15, var13, var17, var16, var14, var18);
			this.renderStandardBlock(block, x, y, z);
		} else {
			this.uvRotateTop = 1;
			var15 = 0.4375F;
			var16 = 0.5625F;
			var17 = 0.0F;
			var18 = 0.125F;
			block.setBlockBounds(var15, var13, var17, var16, var14, var18);
			this.renderStandardBlock(block, x, y, z);
			var17 = 0.875F;
			var18 = 1.0F;
			block.setBlockBounds(var15, var13, var17, var16, var14, var18);
			this.renderStandardBlock(block, x, y, z);
			this.uvRotateTop = 0;
		}

		if(isOpen) {
			if(direction == 2 || direction == 0) {
				this.uvRotateTop = 1;
			}

			if(direction == 3) {
				block.setBlockBounds(0.8125F, var9, 0.0F, 0.9375F, var12, 0.125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.8125F, var9, 0.875F, 0.9375F, var12, 1.0F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.5625F, var9, 0.0F, 0.8125F, var10, 0.125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.5625F, var9, 0.875F, 0.8125F, var10, 1.0F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.5625F, var11, 0.0F, 0.8125F, var12, 0.125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.5625F, var11, 0.875F, 0.8125F, var12, 1.0F);
				this.renderStandardBlock(block, x, y, z);
			} else if(direction == 1) {
				block.setBlockBounds(0.0625F, var9, 0.0F, 0.1875F, var12, 0.125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.0625F, var9, 0.875F, 0.1875F, var12, 1.0F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.1875F, var9, 0.0F, 0.4375F, var10, 0.125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.1875F, var9, 0.875F, 0.4375F, var10, 1.0F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.1875F, var11, 0.0F, 0.4375F, var12, 0.125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.1875F, var11, 0.875F, 0.4375F, var12, 1.0F);
				this.renderStandardBlock(block, x, y, z);
			} else if(direction == 0) {
				block.setBlockBounds(0.0F, var9, 0.8125F, 0.125F, var12, 0.9375F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.875F, var9, 0.8125F, 1.0F, var12, 0.9375F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.0F, var9, 0.5625F, 0.125F, var10, 0.8125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.875F, var9, 0.5625F, 1.0F, var10, 0.8125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.0F, var11, 0.5625F, 0.125F, var12, 0.8125F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.875F, var11, 0.5625F, 1.0F, var12, 0.8125F);
				this.renderStandardBlock(block, x, y, z);
			} else if(direction == 2) {
				block.setBlockBounds(0.0F, var9, 0.0625F, 0.125F, var12, 0.1875F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.875F, var9, 0.0625F, 1.0F, var12, 0.1875F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.0F, var9, 0.1875F, 0.125F, var10, 0.4375F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.875F, var9, 0.1875F, 1.0F, var10, 0.4375F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.0F, var11, 0.1875F, 0.125F, var12, 0.4375F);
				this.renderStandardBlock(block, x, y, z);
				block.setBlockBounds(0.875F, var11, 0.1875F, 1.0F, var12, 0.4375F);
				this.renderStandardBlock(block, x, y, z);
			}
		} else if(direction != 3 && direction != 1) {
			var15 = 0.375F;
			var16 = 0.5F;
			var17 = 0.4375F;
			var18 = 0.5625F;
			block.setBlockBounds(var15, var9, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
			var15 = 0.5F;
			var16 = 0.625F;
			block.setBlockBounds(var15, var9, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
			var15 = 0.625F;
			var16 = 0.875F;
			block.setBlockBounds(var15, var9, var17, var16, var10, var18);
			this.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(var15, var11, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
			var15 = 0.125F;
			var16 = 0.375F;
			block.setBlockBounds(var15, var9, var17, var16, var10, var18);
			this.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(var15, var11, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
		} else {
			this.uvRotateTop = 1;
			var15 = 0.4375F;
			var16 = 0.5625F;
			var17 = 0.375F;
			var18 = 0.5F;
			block.setBlockBounds(var15, var9, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
			var17 = 0.5F;
			var18 = 0.625F;
			block.setBlockBounds(var15, var9, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
			var17 = 0.625F;
			var18 = 0.875F;
			block.setBlockBounds(var15, var9, var17, var16, var10, var18);
			this.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(var15, var11, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
			var17 = 0.125F;
			var18 = 0.375F;
			block.setBlockBounds(var15, var9, var17, var16, var10, var18);
			this.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(var15, var11, var17, var16, var12, var18);
			this.renderStandardBlock(block, x, y, z);
		}

		this.renderAllFaces = false;
		this.uvRotateTop = 0;
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return var5;
	}

	public boolean renderBlockCoconut(Block block, int x, int y, int z) {
		int data = blockAccess.getBlockMetadata(x, y, z);
		data &= 3;
		boolean flag = false;
		if(data == 0) { // -z
			block.setBlockBounds(0.5f, 0.625f, 0.0f, 0.75f + 0.125f, 0.875f + 0.125f, 0.25f + 0.125f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.1875f - 0.0625f, 0.5625f, 0.0f, 0.4375f + 0.0625f, 0.8125f + 0.125f, 0.25f + 0.125f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.375f - 0.0625f, 0.50f, 0.25f + 0.125f, 0.625f + 0.0625f, 0.75f + 0.125f, 0.5f + 0.25f);
			renderStandardBlock(block, x, y, z);
			flag = true;
		} else if(data == 1) { // +z
			uvRotateTop = 3;
			block.setBlockBounds(0.5f, 0.625f, 0.75f - 0.125f, 0.75f + 0.125f, 0.875f + 0.125f, 1.0f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.1875f - 0.0625f, 0.5625f, 0.75f - 0.125f, 0.4375f + 0.0625f, 0.8125f + 0.125f, 1.0f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.375f - 0.0625f, 0.50f, 0.5f - 0.25f, 0.625f + 0.0625f, 0.75f + 0.125f, 0.75f - 0.125f);
			renderStandardBlock(block, x, y, z);
			flag = true;
		} else if(data == 2) { // +x
			uvRotateTop = 3;
			block.setBlockBounds(0.75f - 0.125f, 0.625f, 0.5f, 1f, 0.875f + 0.125f, 0.75f + 0.125f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.75f - 0.125f, 0.5625f, 0.1875f - 0.0625f, 1f, 0.8125f + 0.125f, 0.4375f + 0.0625f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.5f - 0.25f, 0.50f, 0.375f - 0.0625f, 0.75f - 0.125f, 0.75f + 0.125f, 0.625f + 0.0625f);
			renderStandardBlock(block, x, y, z);
			flag = true;
		} else if(data == 3) { // -x
			block.setBlockBounds(0.0f, 0.625f, 0.5f, 0.25f + 0.125f, 0.875f + 0.125f, 0.75f + 0.125f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.0f, 0.5625f, 0.1875f - 0.0625f, 0.25f + 0.125f, 0.8125f + 0.125f, 0.4375f + 0.0625f);
			renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.25f + 0.125f, 0.50f, 0.375f - 0.0625f, 0.5f + 0.25f, 0.75f + 0.125f, 0.625f + 0.0625f);
			renderStandardBlock(block, x, y, z);
			flag = true;
		}
		uvRotateTop = 0;
		block.setBlockBounds(0.0f, 0.50f, 0.0f, 1.0f, 1.0f, 1.0f);
		return flag;
	}

	public boolean renderBlockStairs(BlockStairs block, int x, int y, int z) {
		block.func_82541_d(this.blockAccess, x, y, z);
		this.renderStandardBlock(block, x, y, z);
		boolean var5 = block.func_82542_g(this.blockAccess, x, y, z);
		;
		this.renderStandardBlock(block, x, y, z);
		if(var5 && block.func_82544_h(this.blockAccess, x, y, z))
			this.renderStandardBlock(block, x, y, z);
		return true;
	}

	public boolean renderBlockDoor(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		BlockDoor blockdoor = (BlockDoor) block;
		boolean flag = false;
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		int l = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
		tessellator.setBrightness(block.minY <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k) : l);
		tessellator.setColorOpaque_F(f, f, f);
		renderBottomFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 0));
		flag = true;
		tessellator.setBrightness(block.maxY >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j + 1, k) : l);
		tessellator.setColorOpaque_F(f1, f1, f1);
		renderTopFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 1));
		flag = true;
		tessellator.setBrightness(block.minZ <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k - 1) : l);
		tessellator.setColorOpaque_F(f2, f2, f2);
		int i1 = block.getBlockTexture(blockAccess, i, j, k, 2);
		if(i1 < 0) {
			flipTexture = true;
			i1 = -i1;
		}
		renderEastFace(block, i, j, k, i1);
		flag = true;
		flipTexture = false;
		tessellator.setBrightness(block.maxZ >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i, j, k + 1) : l);
		tessellator.setColorOpaque_F(f2, f2, f2);
		i1 = block.getBlockTexture(blockAccess, i, j, k, 3);
		if(i1 < 0) {
			flipTexture = true;
			i1 = -i1;
		}
		renderWestFace(block, i, j, k, i1);
		flag = true;
		flipTexture = false;
		tessellator.setBrightness(block.minX <= 0.0D ? block.getMixedBrightnessForBlock(blockAccess, i - 1, j, k) : l);
		tessellator.setColorOpaque_F(f3, f3, f3);
		i1 = block.getBlockTexture(blockAccess, i, j, k, 4);
		if(i1 < 0) {
			flipTexture = true;
			i1 = -i1;
		}
		renderNorthFace(block, i, j, k, i1);
		flag = true;
		flipTexture = false;
		tessellator.setBrightness(block.maxX >= 1.0D ? block.getMixedBrightnessForBlock(blockAccess, i + 1, j, k) : l);
		tessellator.setColorOpaque_F(f3, f3, f3);
		i1 = block.getBlockTexture(blockAccess, i, j, k, 5);
		if(i1 < 0) {
			flipTexture = true;
			i1 = -i1;
		}
		renderSouthFace(block, i, j, k, i1);
		flag = true;
		flipTexture = false;
		return flag;
	}

	private void saveBlockDimensionsAndFix(Block block) {
		this.minBlockX = block.minX;
		this.minBlockY = block.minY;
		this.minBlockZ = block.minZ;
		this.maxBlockX = block.maxX;
		this.maxBlockY = block.maxY;
		this.maxBlockZ = block.maxZ;
		switch(fixSide.getId()) {
		case 0:
		case 1:
			block.minX = 0.0d;
			block.minZ = 0.0d;
			block.maxX = 1.0d;
			block.maxZ = 1.0d;
			break;
		case 2:
		case 3:
			block.minY = 0.0d;
			block.minX = 0.0d;
			block.maxY = 1.0d;
			block.maxX = 1.0d;
			break;
		case 4:
		case 5:
			block.minZ = 0.0d;
			block.minY = 0.0d;
			block.maxZ = 1.0d;
			block.maxY = 1.0d;
			break;
		default:
			block.minX = 0.0d;
			block.minY = 0.0d;
			block.minZ = 0.0d;
			block.maxX = 1.0d;
			block.maxY = 1.0d;
			block.maxZ = 1.0d;
			break;
		}
	}
	
	private void resetBlockDimensions(Block block) {
		block.minX = this.minBlockX;
		block.minY = this.minBlockY;
		block.minZ = this.minBlockZ;
		block.maxX = this.maxBlockX;
		block.maxY = this.maxBlockY;
		block.maxZ = this.maxBlockZ;
	}
	
	public void renderBottomFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		if(fixUV) {
			saveBlockDimensionsAndFix(block);
		}
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		double d3 = (j + block.minX * 16D) / 256D;
		double d4 = ((j + block.maxX * 16D) - 0.01D) / 256D;
		double d5 = (k + block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d6 = ((k + block.maxZ * 16D) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (j + 0.0F) / 256F;
			d4 = (j + 15.99F) / 256F;
		}
		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d5 = (k + 0.0F) / RenderEngine.TERRAIN_HEIGHT_F;
			d6 = (k + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		}
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		if(uvRotateBottom == 2) {
			d3 = (j + block.minZ * 16D) / 256D;
			d5 = ((k + 16) - block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = (j + block.maxZ * 16D) / 256D;
			d6 = ((k + 16) - block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateBottom == 1) {
			d3 = ((j + 16) - block.maxZ * 16D) / 256D;
			d5 = (k + block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = ((j + 16) - block.minZ * 16D) / 256D;
			d6 = (k + block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateBottom == 3) {
			d3 = ((j + 16) - block.minX * 16D) / 256D;
			d4 = ((j + 16) - block.maxX * 16D - 0.01D) / 256D;
			d5 = ((k + 16) - block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = ((k + 16) - block.maxZ * 16D - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		if(fixUV)
			resetBlockDimensions(block);
		double d11 = d + block.minX;
		double d12 = d + block.maxX;
		double d13 = d1 + block.minY;
		double d14 = d2 + block.minZ;
		double d15 = d2 + block.maxZ;
		if(enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		} else {
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		}
	}

	public void renderTopFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		if(fixUV)
			saveBlockDimensionsAndFix(block);
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		double d3 = (j + block.minX * 16D) / 256D;
		double d4 = ((j + block.maxX * 16D) - 0.01D) / 256D;
		double d5 = (k + block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d6 = ((k + block.maxZ * 16D) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (j + 0.0F) / 256F;
			d4 = (j + 15.99F) / 256F;
		}
		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d5 = (k + 0.0F) / RenderEngine.TERRAIN_HEIGHT_F;
			d6 = (k + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		}
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		if(uvRotateTop == 1) {
			d3 = (j + block.minZ * 16D) / 256D;
			d5 = ((k + 16) - block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = (j + block.maxZ * 16D) / 256D;
			d6 = ((k + 16) - block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateTop == 2) {
			d3 = ((j + 16) - block.maxZ * 16D) / 256D;
			d5 = (k + block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = ((j + 16) - block.minZ * 16D) / 256D;
			d6 = (k + block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateTop == 3) {
			d3 = ((j + 16) - block.minX * 16D) / 256D;
			d4 = ((j + 16) - block.maxX * 16D - 0.01D) / 256D;
			d5 = ((k + 16) - block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = ((k + 16) - block.maxZ * 16D - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		if(fixUV)
			resetBlockDimensions(block);
		double d11 = d + block.minX;
		double d12 = d + block.maxX;
		double d13 = d1 + block.maxY;
		double d14 = d2 + block.minZ;
		double d15 = d2 + block.maxZ;
		if(enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		} else {
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		}
	}

	public void renderEastFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		if(fixUV)
			saveBlockDimensionsAndFix(block);
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		double d3 = (j + (1.0f - block.maxX) * 16D) / 256D;
		double d4 = ((j + (1.0f - block.minX) * 16D) - 0.01D) / 256D;
		double d5 = ((k + 16) - block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d6 = ((k + 16) - block.minY * 16D - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		if(flipTexture) {
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (j + 0.0F) / 256F;
			d4 = (j + 15.99F) / 256F;
		}
		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (k + 0.0F) / RenderEngine.TERRAIN_HEIGHT_F;
			d6 = (k + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		}
		double d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(uvRotateEast == 2) {
			d3 = (j + block.minY * 16D) / 256D;
			d5 = ((k + 16) - block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = (j + block.maxY * 16D) / 256D;
			d6 = ((k + 16) - block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(uvRotateEast == 1) {
			d3 = ((j + 16) - block.maxY * 16D) / 256D;
			d5 = (k + block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = ((j + 16) - block.minY * 16D) / 256D;
			d6 = (k + block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(uvRotateEast == 3) {
			d3 = ((j + 16) - block.minX * 16D) / 256D;
			d4 = ((j + 16) - block.maxX * 16D - 0.01D) / 256D;
			d5 = (k + block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = ((k + block.minY * 16D) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}
		if(fixUV)
			resetBlockDimensions(block);
		double d12 = d + block.minX;
		double d13 = d + block.maxX;
		double d14 = d1 + block.minY;
		double d15 = d1 + block.maxY;
		double d16 = d2 + block.minZ;
		if(enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
		} else {
			tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
			tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
			tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
			tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
		}
	}

	public void renderWestFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		if(fixUV)
			saveBlockDimensionsAndFix(block);
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		double d3 = (j + block.minX * 16D) / 256D;
		double d4 = ((j + block.maxX * 16D) - 0.01D) / 256D;
		double d5 = ((k + 16) - block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d6 = ((k + 16) - block.minY * 16D - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		if(flipTexture) {
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (j + 0.0F) / 256F;
			d4 = (j + 15.99F) / 256F;
		}
		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (k + 0.0F) / RenderEngine.TERRAIN_HEIGHT_F;
			d6 = (k + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		}
		double d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(uvRotateWest == 1) {
			d3 = (j + block.minY * 16D) / 256D;
			d6 = ((k + 16) - block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = (j + block.maxY * 16D) / 256D;
			d5 = ((k + 16) - block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(uvRotateWest == 2) {
			d3 = ((j + 16) - block.maxY * 16D) / 256D;
			d5 = (k + block.minX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = ((j + 16) - block.minY * 16D) / 256D;
			d6 = (k + block.maxX * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(uvRotateWest == 3) {
			d3 = ((j + 16) - block.minX * 16D) / 256D;
			d4 = ((j + 16) - block.maxX * 16D - 0.01D) / 256D;
			d5 = (k + block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = ((k + block.minY * 16D) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}
		if(fixUV)
			resetBlockDimensions(block);
		double d12 = d + block.minX;
		double d13 = d + block.maxX;
		double d14 = d1 + block.minY;
		double d15 = d1 + block.maxY;
		double d16 = d2 + block.maxZ;
		if(enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d12, d15, d16, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d13, d14, d16, d4, d6);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d13, d15, d16, d8, d10);
		} else {
			tessellator.addVertexWithUV(d12, d15, d16, d3, d5);
			tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
			tessellator.addVertexWithUV(d13, d14, d16, d4, d6);
			tessellator.addVertexWithUV(d13, d15, d16, d8, d10);
		}
	}

	public void renderNorthFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		if(fixUV)
			saveBlockDimensionsAndFix(block);
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		double d3 = (j + block.minZ * 16D) / 256D;
		double d4 = ((j + block.maxZ * 16D) - 0.01D) / 256D;
		double d5 = ((k + 16) - block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d6 = ((k + 16) - block.minY * 16D - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		if(flipTexture) {
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d3 = (j + 0.0F) / 256F;
			d4 = (j + 15.99F) / 256F;
		}
		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (k + 0.0F) / RenderEngine.TERRAIN_HEIGHT_F;
			d6 = (k + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		}
		double d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(uvRotateNorth == 1) {
			d3 = (j + block.minY * 16D) / 256D;
			d5 = ((k + 16) - block.maxZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = (j + block.maxY * 16D) / 256D;
			d6 = ((k + 16) - block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(uvRotateNorth == 2) {
			d3 = ((j + 16) - block.maxY * 16D) / 256D;
			d5 = (k + block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = ((j + 16) - block.minY * 16D) / 256D;
			d6 = (k + block.maxZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(uvRotateNorth == 3) {
			d3 = ((j + 16) - block.minZ * 16D) / 256D;
			d4 = ((j + 16) - block.maxZ * 16D - 0.01D) / 256D;
			d5 = (k + block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = ((k + block.minY * 16D) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}
		if(fixUV)
			resetBlockDimensions(block);
		double d12 = d + block.minX;
		double d13 = d1 + block.minY;
		double d14 = d1 + block.maxY;
		double d15 = d2 + block.minZ;
		double d16 = d2 + block.maxZ;
		if(enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
		} else {
			tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
			tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
		}
	}

	public void renderSouthFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(overrideBlockTexture >= 0) {
			i = overrideBlockTexture;
		}
		if(fixUV)
			saveBlockDimensionsAndFix(block);
		int j = (i & 0xf) << 4;
		int k = i & RenderEngine.TEX;
		double d3 = (j + (1.0f - block.maxZ) * 16D) / 256D;
		double d4 = ((j + (1.0f - block.minZ) * 16D) - 0.01D) / 256D;
		double d5 = ((k + 16) - block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
		double d6 = ((k + 16) - block.minY * 16D - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
		if(flipTexture) {
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d3 = (j + 0.0F) / 256F;
			d4 = (j + 15.99F) / 256F;
		}
		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (k + 0.0F) / RenderEngine.TERRAIN_HEIGHT_F;
			d6 = (k + 15.99F) / RenderEngine.TERRAIN_HEIGHT_F;
		}
		double d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(uvRotateSouth == 2) {
			d3 = (j + block.minY * 16D) / 256D;
			d5 = ((k + 16) - block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = (j + block.maxY * 16D) / 256D;
			d6 = ((k + 16) - block.maxZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(uvRotateSouth == 1) {
			d3 = ((j + 16) - block.maxY * 16D) / 256D;
			d5 = (k + block.maxZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d4 = ((j + 16) - block.minY * 16D) / 256D;
			d6 = (k + block.minZ * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(uvRotateSouth == 3) {
			d3 = ((j + 16) - block.minZ * 16D) / 256D;
			d4 = ((j + 16) - block.maxZ * 16D - 0.01D) / 256D;
			d5 = (k + block.maxY * 16D) / RenderEngine.TERRAIN_HEIGHT_D;
			d6 = ((k + block.minY * 16D) - 0.01D) / RenderEngine.TERRAIN_HEIGHT_D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}
		if(fixUV)
			resetBlockDimensions(block);
		double d12 = d + block.maxX;
		double d13 = d1 + block.minY;
		double d14 = d1 + block.maxY;
		double d15 = d2 + block.minZ;
		double d16 = d2 + block.maxZ;
		if(enableAO) {
			tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
			tessellator.setBrightness(brightnessTopLeft);
			tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
			tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
			tessellator.setBrightness(brightnessBottomLeft);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
			tessellator.setBrightness(brightnessBottomRight);
			tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
			tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
			tessellator.setBrightness(brightnessTopRight);
			tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
		} else {
			tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
			tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
		}
	}

	public void renderBlockOnInventory(Block block, int i, float f) {
		Tessellator tessellator = Tessellator.instance;
		boolean flag = block.blockID == Block.grass.blockID || block == Block.blockHalfBlockGrass || block == Block.blockStepGrass || block == Block.blockStepGrassUp;
		if(useInventoryTint) {
			int j = block.getRenderColor(i);
			if(flag) {
				j = 0xffffff;
			}
			float f1 = (j >> 16 & 0xff) / 255F;
			float f3 = (j >> 8 & 0xff) / 255F;
			float f7 = (j & 0xff) / 255F;
			GL11.glColor4f(f1 * f, f3 * f, f7 * f, 1.0F);
		}
		int k = block.getRenderType();
		if(k != 0 && k != 16 && k != 31 && k != 34 && k != 38 && k != 40) {
			if(k == 1) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1F, 0.0F);
				renderCrossedSquares(block, i, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(k == 19) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1F, 0.0F);
				block.setBlockBoundsForItemRender();
				renderBlockStemSmall(block, i, block.maxY, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(k == 23) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1F, 0.0F);
				block.setBlockBoundsForItemRender();
				tessellator.draw();
			} else if(k == 13) {
				block.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				float f2 = 0.0625F;
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1F, 0.0F);
				renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, -1F);
				tessellator.setTranslationF(0.0F, 0.0F, f2);
				renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
				tessellator.setTranslationF(0.0F, 0.0F, -f2);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator.setTranslationF(0.0F, 0.0F, -f2);
				renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
				tessellator.setTranslationF(0.0F, 0.0F, f2);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(-1F, 0.0F, 0.0F);
				tessellator.setTranslationF(f2, 0.0F, 0.0F);
				renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
				tessellator.setTranslationF(-f2, 0.0F, 0.0F);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.setTranslationF(-f2, 0.0F, 0.0F);
				renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
				tessellator.setTranslationF(f2, 0.0F, 0.0F);
				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if(k == 22) {
				ChestItemRenderHelper.instance.renderInInventory(block, i, f);
				GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
			} else if(k == 41) {
				NormalChestItemRenderHelper.instance.renderInInventory(block, i, f);
				GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
			} else if(k == 6) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1F, 0.0F);
				renderBlockCropsImpl(block, i, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(k == 2) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1F, 0.0F);
				renderTorchAtAngle(block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				tessellator.draw();
			} else if(k == 10) {
				for(int l = 0; l < 2; l++) {
					block.setBlockBoundsForItemRender();
					if(l == 0) {
						block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
					}
					if(l == 1) {
						block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
					}
					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1F, 0.0F);
					renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1F);
					renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1F, 0.0F, 0.0F);
					renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

			} else if(k == 27) {
				int i1 = 0;
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				tessellator.startDrawingQuads();
				for(int i2 = 0; i2 < 8; i2++) {
					int j2 = 0;
					byte byte0 = 1;
					if(i2 == 0) {
						j2 = 2;
					}
					if(i2 == 1) {
						j2 = 3;
					}
					if(i2 == 2) {
						j2 = 4;
					}
					if(i2 == 3) {
						j2 = 5;
						byte0 = 2;
					}
					if(i2 == 4) {
						j2 = 6;
						byte0 = 3;
					}
					if(i2 == 5) {
						j2 = 7;
						byte0 = 5;
					}
					if(i2 == 6) {
						j2 = 6;
						byte0 = 2;
					}
					if(i2 == 7) {
						j2 = 3;
					}
					float f10 = j2 / 16F;
					float f11 = 1.0F - i1 / 16F;
					float f12 = 1.0F - (i1 + byte0) / 16F;
					i1 += byte0;
					block.setBlockBounds(0.5F - f10, f12, 0.5F - f10, 0.5F + f10, f11, 0.5F + f10);
					tessellator.setNormal(0.0F, -1F, 0.0F);
					renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
					tessellator.setNormal(0.0F, 0.0F, -1F);
					renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
					tessellator.setNormal(-1F, 0.0F, 0.0F);
					renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
				}

				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if(k == 11) {
				for(int j1 = 0; j1 < 4; j1++) {
					float f4 = 0.125F;
					if(j1 == 0) {
						block.setBlockBounds(0.5F - f4, 0.0F, 0.0F, 0.5F + f4, 1.0F, f4 * 2.0F);
					}
					if(j1 == 1) {
						block.setBlockBounds(0.5F - f4, 0.0F, 1.0F - f4 * 2.0F, 0.5F + f4, 1.0F, 1.0F);
					}
					f4 = 0.0625F;
					if(j1 == 2) {
						block.setBlockBounds(0.5F - f4, 1.0F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 1.0F - f4, 1.0F + f4 * 2.0F);
					}
					if(j1 == 3) {
						block.setBlockBounds(0.5F - f4, 0.5F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 0.5F - f4, 1.0F + f4 * 2.0F);
					}
					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1F, 0.0F);
					renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1F);
					renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1F, 0.0F, 0.0F);
					renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if(k == 21) {
				for(int k1 = 0; k1 < 3; k1++) {
					float f5 = 0.0625F;
					if(k1 == 0) {
						block.setBlockBounds(0.5F - f5, 0.3F, 0.0F, 0.5F + f5, 1.0F, f5 * 2.0F);
					}
					if(k1 == 1) {
						block.setBlockBounds(0.5F - f5, 0.3F, 1.0F - f5 * 2.0F, 0.5F + f5, 1.0F, 1.0F);
					}
					f5 = 0.0625F;
					if(k1 == 2) {
						block.setBlockBounds(0.5F - f5, 0.5F, 0.0F, 0.5F + f5, 1.0F - f5, 1.0F);
					}
					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1F, 0.0F);
					renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1F);
					renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1F, 0.0F, 0.0F);
					renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if(k == 37) {
				for(int j1 = 0; j1 < 4; j1++) {
					float f4 = 0.125F;
					if(j1 == 0) {
						block.setBlockBounds(0.5F - f4, 0.0F, 0.0F, 0.5F + f4, 1.0F, f4 * 2.0F);
					}
					if(j1 == 1) {
						block.setBlockBounds(0.5F - f4, 0.0F, 1.0F - f4 * 2.0F, 0.5F + f4, 1.0F, 1.0F);
					}
					f4 = 0.0625F;
					if(j1 == 2) {
						block.setBlockBounds(0.5F - f4, 1.0F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 1.0F - f4, 1.0F + f4 * 2.0F);
					}
					if(j1 == 3) {
						block.setBlockBounds(0.5F - f4, 0.5F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 0.5F - f4, 1.0F + f4 * 2.0F);
					}
					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1F, 0.0F);
					renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1F);
					renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1F, 0.0F, 0.0F);
					renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}

				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else if(k == 32) {
				for(int var14 = 0; var14 < 2; ++var14) {
					if(var14 == 0) {
						block.setBlockBounds(0.0F, 0.0F, 0.3125F, 1.0F, 0.8125F, 0.6875F);
					}

					if(var14 == 1) {
						block.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
					}

					GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, -1.0F, 0.0F);
					this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, i));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, i));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, -1.0F);
					this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, i));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 0.0F, 1.0F);
					this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, i));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(-1.0F, 0.0F, 0.0F);
					this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, i));
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.setNormal(1.0F, 0.0F, 0.0F);
					this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, i));
					tessellator.draw();
					GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				}
			} else {
				ModLoader.RenderInvBlock(this, block, i, k);
			}
		} else {
			if(k == 16) {
				i = 1;
			}
			block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1F, 0.0F);
			renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, i));
			tessellator.draw();
			if(flag && useInventoryTint) {
				int l1 = block.getRenderColor(i);
				float f6 = (l1 >> 16 & 0xff) / 255F;
				float f8 = (l1 >> 8 & 0xff) / 255F;
				float f9 = (l1 & 0xff) / 255F;
				GL11.glColor4f(f6 * f, f8 * f, f9 * f, 1.0F);
			}
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, i));
			tessellator.draw();
			if(flag && useInventoryTint) {
				GL11.glColor4f(f, f, f, 1.0F);
			}
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1F);
			renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1F, 0.0F, 0.0F);
			renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, i));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
	}

	public static boolean renderItemIn3d(int i) {
		return i == 31 || i == 34 || i == 37 || i == 38 || i == 32 || i == 40 || i == 41 || (i != 0 ? i != 13 ? i != 10 ? i != 11 ? i != 27 ? i != 22 ? i != 21 ? ModLoader.RenderBlockIsItemFull3D(i) : true : true : true : true : true : true : true);
	}

}

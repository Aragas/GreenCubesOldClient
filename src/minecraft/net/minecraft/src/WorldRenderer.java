// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.*;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            MathHelper, AxisAlignedBB, RenderItem, Chunk, 
//            ChunkCache, RenderBlocks, IBlockAccess, Tessellator, 
//            Block, TileEntityRenderer, Entity, ICamera, 
//            World

public class WorldRenderer {

	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;

	public static List<WorldRenderer> lastUpdatedRenderers = new LinkedList<WorldRenderer>();

	public World worldObj;
	private int glRenderList = -1;
	public int posX = -999;
	public int posY;
	public int posZ;
	public int sizeWidth;
	public int sizeHeight;
	public int sizeDepth;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	public int posXClip;
	public int posYClip;
	public int posZClip;
	public boolean isInFrustum = false;
	public boolean skipRenderPass[] = new boolean[3];
	public int posXPlus;
	public int posYPlus;
	public int posZPlus;
	public float rendererRadius;
	public boolean needsUpdate = false;
	public AxisAlignedBB rendererBoundingBox;
	public int chunkIndex;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
	private boolean isInitialized = false;
	public List<TileEntity> tileEntityRenderers = new ArrayList<TileEntity>();
	private List<TileEntity> tileEntities;
	private int bytesDrawn;

	public String lastUpdateCause = null;

	public WorldRenderer(World world, List<TileEntity> worldTileEntities, int x, int y, int z, int l, int i1) {
		tileEntities = worldTileEntities;
		worldObj = world;
		sizeWidth = sizeHeight = sizeDepth = l;
		rendererRadius = MathHelper.sqrt_float(sizeWidth * sizeWidth + sizeHeight * sizeHeight + sizeDepth * sizeDepth) / 2.0F;
		glRenderList = i1;
		setPosition(x, y, z);
	}

	public void setPosition(int i, int j, int k) {
		if(i == posX && j == posY && k == posZ) {
			return;
		} else {
			setDontDraw();
			posX = i;
			posY = j;
			posZ = k;
			posXPlus = i + sizeWidth / 2;
			posYPlus = j + sizeHeight / 2;
			posZPlus = k + sizeDepth / 2;
			posXClip = i & 0x3ff;
			posYClip = j;
			posZClip = k & 0x3ff;
			posXMinus = i - posXClip;
			posYMinus = j - posYClip;
			posZMinus = k - posZClip;
			float f = 6F;
			rendererBoundingBox = AxisAlignedBB.getBoundingBox(i - f, j - f, k - f, (i + sizeWidth) + f, (j + sizeHeight) + f, (k + sizeDepth) + f);
			GL11.glNewList(glRenderList + 3, GL11.GL_COMPILE);
			Render.renderAABB(AxisAlignedBB.getBoundingBoxFromPool(posXClip - f, posYClip - f, posZClip - f, (posXClip + sizeWidth) + f, (posYClip + sizeHeight) + f, (posZClip + sizeDepth) + f));
			GL11.glEndList();
			markDirty();
			return;
		}
	}

	private void setupGLTranslation() {
		GL11.glTranslatef(posXClip, posYClip, posZClip);
	}

	public void updateRenderer() {
		if(!needsUpdate)
			return;
		long start = System.currentTimeMillis();
		//lastUpdatedRenderers.add(this);
		//if(lastUpdatedRenderers.size() > 50)
		//	lastUpdatedRenderers.remove(0);
		chunksUpdated++;
		int l = posX + sizeWidth;
		int i1 = posY + sizeHeight;
		int j1 = posZ + sizeDepth;
		skipRenderPass[0] = true;
		skipRenderPass[1] = true;
		skipRenderPass[2] = true;

		Chunk.isLit = false;
		HashSet<TileEntity> hashset = new HashSet<TileEntity>();
		hashset.addAll(tileEntityRenderers);
		tileEntityRenderers.clear();
		int l1 = 1;
		ChunkCache chunkcache = new ChunkCache(worldObj, posX - l1, posY - l1, posZ - l1, l + l1, i1 + l1, j1 + l1);
		RenderBlocks renderblocks = new RenderBlocks(chunkcache);
		bytesDrawn = 0;
		for(int renderPass = 0; renderPass < 3; renderPass++) {
			GL11.glNewList(glRenderList + renderPass, GL11.GL_COMPILE);
			boolean haveNextPassBlocks = false;
			boolean wereBlocksRendered = false;
			boolean isDrawing = false;
			for(int j2 = posY; j2 < i1; j2++) {
				for(int k2 = posZ; k2 < j1; k2++) {
					for(int l2 = posX; l2 < l; l2++) {
						int i3 = chunkcache.getBlockId(l2, j2, k2);
						if(i3 <= 0)
							continue;
						if(!isDrawing) {
							isDrawing = true;
							GL11.glPushMatrix();
							setupGLTranslation();
							float f = 1.000001F;
							GL11.glTranslatef((-sizeDepth) / 2.0F, (-sizeHeight) / 2.0F, (-sizeDepth) / 2.0F);
							GL11.glScalef(f, f, f);
							GL11.glTranslatef(sizeDepth / 2.0F, sizeHeight / 2.0F, sizeDepth / 2.0F);
							tessellator.startDrawingQuads();
							tessellator.setTranslationD(-posX, -posY, -posZ);
						}
						if(renderPass == 0 && Block.isBlockContainer[i3]) {
							TileEntity tileentity = chunkcache.getBlockTileEntity(l2, j2, k2);
							if(TileEntityRenderer.instance.hasSpecialRenderer(tileentity))
								tileEntityRenderers.add(tileentity);
						}
						Block block = Block.blocksList[i3];
						if(block != null) {
							if(Block.multipassBlocks[i3]) {
								haveNextPassBlocks = true;
								if(block.setUpPass(renderPass))
									wereBlocksRendered |= renderblocks.renderBlockByRenderType(block, l2, j2, k2);
								continue;
							}
							int j3 = block.getRenderBlockPass();
							if(renderPass == 0 && renderblocks.renderLightOnBlock(l2, j2, k2, renderPass))
								wereBlocksRendered = true; 
							if(j3 != renderPass) {
								haveNextPassBlocks = true;
								continue;
							}
							if(j3 == renderPass)
								wereBlocksRendered |= renderblocks.renderBlockByRenderType(block, l2, j2, k2);
						}
					}
				}
			}

			if(isDrawing) {
				bytesDrawn += tessellator.draw();
				GL11.glPopMatrix();
			} else
				wereBlocksRendered = false;
			GL11.glEndList();
			tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
			if(wereBlocksRendered)
				skipRenderPass[renderPass] = false;
			if(!haveNextPassBlocks)
				break;
		}
		HashSet<TileEntity> hashset1 = new HashSet<TileEntity>();
		hashset1.addAll(tileEntityRenderers);
		hashset1.removeAll(hashset);
		tileEntities.addAll(hashset1);
		hashset.removeAll(tileEntityRenderers);
		tileEntities.removeAll(hashset);
		isChunkLit = Chunk.isLit;
		isInitialized = true;
		long end = System.currentTimeMillis();
		//if(end - start > 15) {
		//	Minecraft.theMinecraft.chat.processInput("Chunk at " + posX + "," + posY + "," + posZ + " causes lag: " + (end - start) + " ms", 0);
		//}
	}

	public float distanceToEntitySquared(Entity entity) {
		float f = (float) (entity.posX - posXPlus);
		float f1 = (float) (entity.posY - posYPlus);
		float f2 = (float) (entity.posZ - posZPlus);
		return f * f + f1 * f1 + f2 * f2;
	}

	public void setDontDraw() {
		for(int i = 0; i < 3; i++)
			skipRenderPass[i] = true;
		isInFrustum = false;
		isInitialized = false;
	}

	public void stopRendering() {
		setDontDraw();
		worldObj = null;
	}

	public int getGLCallListForPass(int i) {
		if(!isInFrustum)
			return -1;
		if(!skipRenderPass[i])
			return glRenderList + i;
		else
			return -1;
	}

	public void updateInFrustrum(ICamera icamera) {
		isInFrustum = icamera.isBoundingBoxInFrustum(rendererBoundingBox);
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(glRenderList + 3);
	}

	public boolean skipAllRenderPasses() {
		if(!isInitialized)
			return false;
		else
			return skipRenderPass[0] && skipRenderPass[1] && skipRenderPass[2];
	}

	public void markDirty() {
		needsUpdate = true;
	}
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, RenderBlocks, EntityItem, MathHelper, 
//            ItemStack, Block, Item, ItemPotion, 
//            Tessellator, RenderManager, RenderEngine, FontRenderer, 
//            Entity

public class RenderItem extends Render {

	private static final RenderItem instance = new RenderItem();

	private RenderBlocks renderBlocks;
	private Random random;
	public boolean useItemColor;
	public float zLevel;
	private int seed;

	public static RenderItem getInstance() {
		return instance;
	}

	private RenderItem() {
		renderBlocks = new RenderBlocks();
		random = new Random();
		seed = random.nextInt();
		useItemColor = true;
		zLevel = 0.0F;
		shadowSize = 0.15F;
		field_194_c = 0.75F;
	}

	private List<ItemStack> cache = new ArrayList<ItemStack>();
	
	public void doRenderItem(EntityItem entityitem, double d, double d1, double d2, float f, float f1) {
		doRenderItem(entityitem.item, d, d1, d2, f1, f1, entityitem.age, entityitem.field_804_d, false);
	}

	public void doRenderItem(ItemStack itemstack, double d, double d1, double d2, float f, float f1, int age, float randomOffset, boolean noRotate) {
		Profiler.startSection("RenderItem");
		RenderHelper.enableStandardItemLighting();
		Item item = itemstack.getItem();
		if(item.useMultirender(itemstack, ItemRenderType.HANDS)) {
			cache = item.getMultirender(itemstack, ItemRenderType.HANDS);
		} else {
			cache.clear();
			cache.add(itemstack);
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		for(int i10 = 0; i10 < cache.size(); ++i10) {
			itemstack = cache.get(i10);
			item = itemstack.getItem();
			random.setSeed(seed);
			GL11.glPushMatrix();
			float f2 = noRotate ? 0 : MathHelper.sin((age + f1) / 10F + randomOffset) * 0.1F + 0.1F;
			float f3 = noRotate ? 0 : ((age + f1) / 20F + randomOffset) * 57.29578F;
			int renderedCount;
			if(itemstack.stackSize > 20) {
				renderedCount = 4;
			} else if(itemstack.stackSize > 5) {
				renderedCount = 3;
			} else if(itemstack.stackSize > 1) {
				renderedCount = 2;
			} else {
				renderedCount = 1;
			}
			GL11.glTranslatef((float) d, (float) d1 + f2, (float) d2);
			// GreenCubes extend to 4096 blocks
			if((itemstack.itemID < 256 || (itemstack.itemID < Block.blocksLength && Block.blocksList[itemstack.itemID] != null)) && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
				GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
				loadTexture("/terrain.png");
				float f4 = noRotate ? 0.5f : 0.25F;
				int j = Block.blocksList[itemstack.itemID].getRenderType();
				if(j == 1 || j == 19 || j == 12 || j == 2) {
					f4 = 0.5F;
				}
				if(!noRotate)
					GL11.glScalef(f4, f4, f4);
				for(int l = 0; l < renderedCount; l++) {
					GL11.glPushMatrix();
					if(l > 0) {
						float f7 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f4;
						float f11 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f4;
						float f14 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f4;
						GL11.glTranslatef(f7, f11, f14);
					}
					float f8 = 1.0F;
					renderBlocks.renderBlockOnInventory(Block.blocksList[itemstack.itemID], itemstack.getItemDamage(), f8);
					GL11.glPopMatrix();
				}

			} else if(Minecraft.isFancyGraphicsEnabled()) {
				GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
				float f4 = noRotate ? 1f : 0.25F;
				GL11.glScalef(f4, f4, f4);
				for(int l = 0; l < renderedCount; l++) {
					GL11.glPushMatrix();
					if(l > 0) {
						//float f7 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / 2F;
						//float f11 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / 0.5F;
						//float f14 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / 0.5F;
						GL11.glTranslatef(0.15F * l * random.nextFloat(), 0.25F * l * random.nextFloat(), 0.3F * l * random.nextFloat());
					}
					renderItemFancy(itemstack);
					GL11.glPopMatrix();
				}
			} else if(itemstack.itemID == Item.potion.shiftedIndex) {
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				char c = '\215';
				loadTexture("/gui/items.png");
				float f5 = 1.0F;
				if(useItemColor) {
					int i1 = Item.itemsList[itemstack.itemID].getColorFromIS(itemstack);
					float f9 = (i1 >> 16 & 0xff) / 255F;
					float f12 = (i1 >> 8 & 0xff) / 255F;
					float f15 = (i1 & 0xff) / 255F;
					GL11.glColor4f(f9 * f5, f12 * f5, f15 * f5, 1.0F);
				}
				func_40267_a(c, renderedCount);
				if(useItemColor) {
					GL11.glColor4f(f5, f5, f5, 1.0F);
					func_40267_a(itemstack.getIconIndex(), renderedCount);
				}
			} else {
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				int i = itemstack.getIconIndex();
				// GreenCubes extend to 4096 blocks
				boolean block = false;
				if(itemstack.itemID < 256 || (itemstack.itemID < Block.blocksLength && Block.blocksList[itemstack.itemID] != null)) {
					block = true;
					loadTexture("/terrain.png");
				} else {
					loadTexture("/gui/items.png");
				}
				if(useItemColor) {
					int k = Item.itemsList[itemstack.itemID].getColorFromIS(itemstack);
					float f6 = (k >> 16 & 0xff) / 255F;
					float f10 = (k >> 8 & 0xff) / 255F;
					float f13 = (k & 0xff) / 255F;
					float f16 = 1.0F;
					GL11.glColor4f(f6 * f16, f10 * f16, f13 * f16, 1.0F);
				}
				if(itemstack.isGlowing())
					renderWithGlow(i, renderedCount, block, itemstack);
				else
					func_40267_a(i, renderedCount, block);
			}
			GL11.glPopMatrix();
		}
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.disableStandardItemLighting();
		Profiler.endSection();
	}

	private void renderItemFancy(ItemStack itemstack) {
		if(useItemColor) {
			int k = Item.itemsList[itemstack.itemID].getColorFromIS(itemstack);
			float f6 = (k >> 16 & 0xff) / 255F;
			float f10 = (k >> 8 & 0xff) / 255F;
			float f13 = (k & 0xff) / 255F;
			float f16 = 1.0F;
			GL11.glColor4f(f6 * f16, f10 * f16, f13 * f16, 1.0F);
		}
		Minecraft mc = Minecraft.theMinecraft;
		ItemRenderer itemRenderer = mc.entityRenderer.itemRenderer;
		do {
			// GreenCubes 4096 blocks extension
			boolean block = false;
			if(itemstack.itemID < 256 || (itemstack.itemID < Block.blocksLength && Block.blocksList[itemstack.itemID] != null)) {
				block = true;
				if(itemRenderer.renderListsBlocks.containsKey(itemstack.getIconIndex())) {
					Profiler.startSection("Hit");
					GL11.glRotatef(-335F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
					GL11.glCallList(itemRenderer.renderListsBlocks.get(itemstack.getIconIndex()));
					Profiler.endSection();
					break;
				} else {
					Profiler.startSection("Miss");
					//if(itemstack.itemID != 54 && itemstack.itemID != 261)
					//	System.out.println("Miss block: " + itemstack.itemID + "," + itemstack.getItemDamage());
					GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, mc.renderEngine.getTexture("/terrain.png"));
				}
			} else {
				if(itemRenderer.renderListsItems.containsKey(itemstack.getIconIndex())) {
					Profiler.startSection("Hit");
					GL11.glRotatef(-335F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
					GL11.glCallList(itemRenderer.renderListsItems.get(itemstack.getIconIndex()));
					Profiler.endSection();
					break;
				} else {
					Profiler.startSection("Miss");
					//if(itemstack.itemID != 54 && itemstack.itemID != 261)
					//	System.out.println("Miss item: " + itemstack.itemID + "," + itemstack.getItemDamage());
					GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, mc.renderEngine.getTexture("/gui/items.png"));
				}
			}
			Tessellator tessellator = Tessellator.instance;
			int j = itemstack.getIconIndex();
			double f = (((j % 16) * 16) + 0.0d) / 256d;
			double f1 = (((j % 16) * 16) + 15.99d) / 256d;
			double f2 = (((j / 16) * 16) + 0.0d) / (block ? RenderEngine.TERRAIN_HEIGHT_D : RenderEngine.ITEMS_HEIGHT_D);
			double f3 = (((j / 16) * 16) + 15.99d) / (block ? RenderEngine.TERRAIN_HEIGHT_D : RenderEngine.ITEMS_HEIGHT_D);
			float f4 = 0.0F;
			float f5 = 0.3F;
			GL11.glTranslatef(-f4, -f5, 0.0F);
			float f6 = 1.5F;
			GL11.glScalef(f6, f6, f6);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			itemRenderer.func_40686_a(tessellator, f1, f2, f, f3);
			Profiler.endSection();
		} while(false);
		if(itemstack != null && itemstack.isGlowing()) {
			Profiler.startSection("Glow");
			Tessellator tessellator = Tessellator.instance;
			GL11.glDepthFunc(514);
			GL11.glDisable(2896 /* GL_LIGHTING */);
			mc.renderEngine.bindTexture(mc.renderEngine.getTexture("%blur%/misc/glint.png"));
			GL11.glEnable(3042 /* GL_BLEND */);
			GL11.glBlendFunc(768, 1);
			if(itemstack.nbtData != null && itemstack.nbtData.hasKey("Glow")) {
				int color = itemstack.nbtData.getInteger("Glow");
				GL11.glColor4f(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f, 1.0f);
			} else
				GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
			GL11.glMatrixMode(5890 /* GL_TEXTURE */);
			GL11.glPushMatrix();
			float f8 = 0.125F;
			GL11.glScalef(f8, f8, f8);
			float f9 = ((System.currentTimeMillis() % 3000L) / 3000F) * 8F;
			GL11.glTranslatef(f9, 0.0F, 0.0F);
			GL11.glRotatef(-50F, 0.0F, 0.0F, 1.0F);
			itemRenderer.func_40686_a(tessellator, 0.0F, 0.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(f8, f8, f8);
			f9 = ((System.currentTimeMillis() % 4873L) / 4873F) * 8F;
			GL11.glTranslatef(-f9, 0.0F, 0.0F);
			GL11.glRotatef(10F, 0.0F, 0.0F, 1.0F);
			itemRenderer.func_40686_a(tessellator, 0.0F, 0.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
			GL11.glMatrixMode(5888 /* GL_MODELVIEW0_ARB */);
			GL11.glDisable(3042 /* GL_BLEND */);
			GL11.glEnable(2896 /* GL_LIGHTING */);
			GL11.glDepthFunc(515);
			Profiler.endSection();
		}
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
	}

	private void func_40267_a(int i, int j) {
		func_40267_a(i, j, false);
	}

	private void renderWithGlow(int i, int j, boolean block, ItemStack itemstack) {
		Tessellator tessellator = Tessellator.instance;
		float f = ((i % 16) * 16 + 0) / 256F;
		float f1 = ((i % 16) * 16 + 16) / 256F;
		float f2 = ((i / 16) * 16 + 0) / (block ? RenderEngine.TERRAIN_HEIGHT_F : RenderEngine.ITEMS_HEIGHT_F);
		float f3 = ((i / 16) * 16 + 16) / (block ? RenderEngine.TERRAIN_HEIGHT_F : RenderEngine.ITEMS_HEIGHT_F);
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		for(int k = 0; k < j; k++) {
			GL11.glPushMatrix();
			if(k > 0) {
				float f7 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				float f8 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				float f9 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				GL11.glTranslatef(f7, f8, f9);
			}
			GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
			tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
			tessellator.addVertexWithUV(f4 - f5, 1.0F - f6, 0.0D, f1, f2);
			tessellator.addVertexWithUV(0.0F - f5, 1.0F - f6, 0.0D, f, f2);
			tessellator.draw();
			Profiler.startSection("Glow");
			GL11.glDepthFunc(514);
			GL11.glDisable(2896 /* GL_LIGHTING */);
			loadTexture("%blur%/misc/glint.png");
			GL11.glEnable(3042 /* GL_BLEND */);
			GL11.glBlendFunc(768, 1);
			if(itemstack.nbtData != null && itemstack.nbtData.hasKey("Glow")) {
				int color = itemstack.nbtData.getInteger("Glow");
				GL11.glColor4f(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f, 1.0f);
			} else
				GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
			float f9 = ((System.currentTimeMillis() % (3000 + 0 * 1873)) / (3000F + (0 * 1873))) * 256F;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, (f9 + 20 * f4) * 0.00390625F, (0 + (float) 20) * 0.00390625F);
			tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, (f9 + 20 + (float) 20 * 4) * 0.00390625F, (0 + (float) 20) * 0.00390625F);
			tessellator.addVertexWithUV(f4 - f5, 1.0F - f6, 0.0D, (f9 + 20) * 0.00390625F, (0 + 0.0F) * 0.00390625F);
			tessellator.addVertexWithUV(0.0F - f5, 1.0F - f6, 0.0D, (f9 + 0.0F) * 0.00390625F, (0 + 0.0F) * 0.00390625F);
			tessellator.draw();
			f9 = ((System.currentTimeMillis() % (3000 + 1 * 1873)) / (3000F + (1 * 1873))) * 256F;
			;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, (f9 + 20 * f4) * 0.00390625F, (0 + (float) 20) * 0.00390625F);
			tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, (f9 + 20 + (float) 20 * -1) * 0.00390625F, (0 + (float) 20) * 0.00390625F);
			tessellator.addVertexWithUV(f4 - f5, 1.0F - f6, 0.0D, (f9 + 20) * 0.00390625F, (0 + 0.0F) * 0.00390625F);
			tessellator.addVertexWithUV(0.0F - f5, 1.0F - f6, 0.0D, (f9 + 0.0F) * 0.00390625F, (0 + 0.0F) * 0.00390625F);
			tessellator.draw();
			GL11.glDisable(3042 /* GL_BLEND */);
			GL11.glDepthMask(true);
			GL11.glEnable(2896 /* GL_LIGHTING */);
			GL11.glDepthFunc(515);
			Profiler.endSection();
			GL11.glPopMatrix();
		}
	}

	private void func_40267_a(int i, int j, boolean block) {
		Tessellator tessellator = Tessellator.instance;
		float f = ((i % 16) * 16 + 0) / 256F;
		float f1 = ((i % 16) * 16 + 16) / 256F;
		float f2 = ((i / 16) * 16 + 0) / (block ? RenderEngine.TERRAIN_HEIGHT_F : RenderEngine.ITEMS_HEIGHT_F);
		float f3 = ((i / 16) * 16 + 16) / (block ? RenderEngine.TERRAIN_HEIGHT_F : RenderEngine.ITEMS_HEIGHT_F);
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		for(int k = 0; k < j; k++) {
			GL11.glPushMatrix();
			if(k > 0) {
				float f7 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				float f8 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				float f9 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				GL11.glTranslatef(f7, f8, f9);
			}
			GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
			tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
			tessellator.addVertexWithUV(f4 - f5, 1.0F - f6, 0.0D, f1, f2);
			tessellator.addVertexWithUV(0.0F - f5, 1.0F - f6, 0.0D, f, f2);
			tessellator.draw();
			GL11.glPopMatrix();
		}

	}

	public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack item, int l, int i1) {
		// GreenCubes extend to 4096 blocks
		int i = item.itemID;
		int j = item.itemDamage;
		int k = item.getIconIndex();
		if((i < 256 || (i < Block.blocksLength && Block.blocksList[i] != null)) && RenderBlocks.renderItemIn3d(Block.blocksList[i].getRenderType())) {
			int j1 = i;
			renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
			Block block = Block.blocksList[j1];
			GL11.glPushMatrix();
			GL11.glTranslatef(l - 2, i1 + 3, -3F + zLevel);
			GL11.glScalef(10F, 10F, 10F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1F);
			GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
			int i2 = Item.itemsList[i].getColorFromIS(item);
			float f3 = (i2 >> 16 & 0xff) / 255F;
			float f6 = (i2 >> 8 & 0xff) / 255F;
			float f8 = (i2 & 0xff) / 255F;
			if(useItemColor) {
				GL11.glColor4f(f3, f6, f8, 1.0F);
			}
			GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
			renderBlocks.useInventoryTint = useItemColor;
			renderBlocks.renderBlockOnInventory(block, j, 1.0F);
			renderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
		} else if(i == Item.potion.shiftedIndex) {
			GL11.glDisable(2896 /* GL_LIGHTING */);
			renderengine.bindTexture(renderengine.getTexture("/gui/items.png"));
			char c = '\215';
			int l1 = Item.itemsList[i].getColorFromIS(item);
			float f1 = (l1 >> 16 & 0xff) / 255F;
			float f4 = (l1 >> 8 & 0xff) / 255F;
			float f7 = (l1 & 0xff) / 255F;
			if(useItemColor) {
				GL11.glColor4f(f1, f4, f7, 1.0F);
			}
			renderTexturedQuad(l, i1, (c % 16) * 16, (c / 16) * 16, 16, 16);
			if(useItemColor) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			renderTexturedQuad(l, i1, (k % 16) * 16, (k / 16) * 16, 16, 16);
			GL11.glEnable(2896 /* GL_LIGHTING */);
		} else if(k >= 0) {
			GL11.glDisable(2896 /* GL_LIGHTING */);
			// GreenCubes extend to 4096 blocks
			boolean block = false;
			if(i < 256 || (i < Block.blocksLength && Block.blocksList[i] != null)) {
				block = true;
				renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
			} else {
				renderengine.bindTexture(renderengine.getTexture("/gui/items.png"));
			}
			int k1 = Item.itemsList[i].getColorFromIS(item);
			float f = (k1 >> 16 & 0xff) / 255F;
			float f2 = (k1 >> 8 & 0xff) / 255F;
			float f5 = (k1 & 0xff) / 255F;
			if(useItemColor) {
				GL11.glColor4f(f, f2, f5, 1.0F);
			}
			renderTexturedQuad(l, i1, (k % 16) * 16, (k / 16) * 16, 16, 16, block);
			GL11.glEnable(2896 /* GL_LIGHTING */);
		}
		GL11.glEnable(2884 /* GL_CULL_FACE */);
	}
	
	public void renderItemInWorld(RenderEngine renderengine, ItemStack itemstack) {
		if(itemstack == null) {
			return;
		}
		Item item = itemstack.getItem();
		if(item.useMultirender(itemstack, ItemRenderType.INVENTORY)) {
			List<ItemStack> list = item.getMultirender(itemstack, ItemRenderType.INVENTORY);
			if(list != null) {
				for(int n = 0; n < list.size(); ++n)
					renderItemInWorld(renderengine, list.get(n));
				return;
			}
		}
		drawItemIntoGui(null, renderengine, itemstack, 0, 0);
		if(itemstack != null && itemstack.isGlowing()) {
			GL11.glDepthFunc(516);
			GL11.glDisable(2896 /* GL_LIGHTING */);
			GL11.glDepthMask(false);
			renderengine.bindTexture(renderengine.getTexture("%blur%/misc/glint.png"));
			GL11.glEnable(3042 /* GL_BLEND */);
			GL11.glBlendFunc(774, 774);
			if(itemstack.nbtData != null && itemstack.nbtData.hasKey("Glow")) {
				int color = itemstack.nbtData.getInteger("Glow");
				GL11.glColor4f(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f, 1.0f);
			} else
				GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
			zLevel += 1f / 128f;
			func_40266_a(0, - 2, - 2, 20, 20);
			zLevel -= 1f / 128f;
			GL11.glDisable(3042 /* GL_BLEND */);
			GL11.glDepthMask(true);
			GL11.glEnable(2896 /* GL_LIGHTING */);
			GL11.glDepthFunc(515);
		}
	}

	public void renderItemIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j) {
		if(itemstack == null) {
			return;
		}
		Item item = itemstack.getItem();
		if(item.useMultirender(itemstack, ItemRenderType.INVENTORY)) {
			List<ItemStack> list = item.getMultirender(itemstack, ItemRenderType.INVENTORY);
			if(list != null) {
				for(int n = 0; n < list.size(); ++n)
					renderItemIntoGUI(fontrenderer, renderengine, list.get(n), i, j);
				return;
			}
		}
		drawItemIntoGui(fontrenderer, renderengine, itemstack, i, j);
		if(itemstack != null && itemstack.isGlowing()) {
			GL11.glDepthFunc(516);
			GL11.glDisable(2896 /* GL_LIGHTING */);
			GL11.glDepthMask(false);
			renderengine.bindTexture(renderengine.getTexture("%blur%/misc/glint.png"));
			zLevel -= 50F;
			GL11.glEnable(3042 /* GL_BLEND */);
			GL11.glBlendFunc(774, 774);
			if(itemstack.nbtData != null && itemstack.nbtData.hasKey("Glow")) {
				int color = itemstack.nbtData.getInteger("Glow");
				GL11.glColor4f(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f, 1.0f);
			} else
				GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
			func_40266_a(i * 0x19b4ca14 + j * 0x1eafff1, i - 2, j - 2, 20, 20);
			GL11.glDisable(3042 /* GL_BLEND */);
			GL11.glDepthMask(true);
			zLevel += 50F;
			GL11.glEnable(2896 /* GL_LIGHTING */);
			GL11.glDepthFunc(515);
		}
	}

	private void func_40266_a(int i, int j, int k, int l, int i1) {
		for(int j1 = 0; j1 < 2; j1++) {
			if(j1 == 0) {
				GL11.glBlendFunc(768, 1);
			}
			if(j1 == 1) {
				GL11.glBlendFunc(768, 1);
			}
			float f = 0.00390625F;
			float f1 = 0.00390625F;
			float f2 = ((System.currentTimeMillis() % (3000 + j1 * 1873)) / (3000F + (j1 * 1873))) * 256F;
			float f3 = 0.0F;
			Tessellator tessellator = Tessellator.instance;
			float f4 = 4F;
			if(j1 == 1) {
				f4 = -1F;
			}
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(j + 0, k + i1, zLevel, (f2 + i1 * f4) * f, (f3 + i1) * f1);
			tessellator.addVertexWithUV(j + l, k + i1, zLevel, (f2 + l + i1 * f4) * f, (f3 + i1) * f1);
			tessellator.addVertexWithUV(j + l, k + 0, zLevel, (f2 + l) * f, (f3 + 0.0F) * f1);
			tessellator.addVertexWithUV(j + 0, k + 0, zLevel, (f2 + 0.0F) * f, (f3 + 0.0F) * f1);
			tessellator.draw();
		}

	}

	public void renderItemOverlayIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j) {
		if(itemstack == null) {
			return;
		}
		if(itemstack.stackSize > 1) {
			String s = (new StringBuilder()).append("").append(itemstack.stackSize).toString();
			GL11.glDisable(2896 /* GL_LIGHTING */);
			GL11.glDisable(2929 /* GL_DEPTH_TEST */);
			fontrenderer.drawStringWithShadow(s, (i + 19) - 2 - fontrenderer.getStringWidth(s), j + 6 + 3, 0xffffff);
			GL11.glEnable(2896 /* GL_LIGHTING */);
			GL11.glEnable(2929 /* GL_DEPTH_TEST */);
		}
		if(itemstack.isItemDamaged()) {
			int k = (int) Math.round(13D - (itemstack.getItemDamageForDisplay() * 13D) / itemstack.getMaxDamage());
			int l = (int) Math.round(255D - (itemstack.getItemDamageForDisplay() * 255D) / itemstack.getMaxDamage());
			GL11.glDisable(2896 /* GL_LIGHTING */);
			GL11.glDisable(2929 /* GL_DEPTH_TEST */);
			GL11.glDisable(3553 /* GL_TEXTURE_2D */);
			Tessellator tessellator = Tessellator.instance;
			int i1 = 255 - l << 16 | l << 8;
			int j1 = (255 - l) / 4 << 16 | 0x3f00;
			renderQuad(tessellator, i + 2, j + 13, 13, 2, 0);
			renderQuad(tessellator, i + 2, j + 13, 12, 1, j1);
			renderQuad(tessellator, i + 2, j + 13, k, 1, i1);
			GL11.glEnable(3553 /* GL_TEXTURE_2D */);
			GL11.glEnable(2896 /* GL_LIGHTING */);
			GL11.glEnable(2929 /* GL_DEPTH_TEST */);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	private void renderQuad(Tessellator tessellator, int i, int j, int k, int l, int i1) {
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(i1);
		tessellator.addVertex(i + 0, j + 0, 0.0D);
		tessellator.addVertex(i + 0, j + l, 0.0D);
		tessellator.addVertex(i + k, j + l, 0.0D);
		tessellator.addVertex(i + k, j + 0, 0.0D);
		tessellator.draw();
	}

	public void renderTexturedQuad(int i, int j, int k, int l, int i1, int j1) {
		renderTexturedQuad(i, j, k, l, i1, j1, false);
	}

	public void renderTexturedQuad(int i, int j, int k, int l, int i1, int j1, boolean block) {
		float f = 0.00390625F;
		float f1 = 0.00390625F / ((block ? RenderEngine.TERRAIN_HEIGHT_F : RenderEngine.ITEMS_HEIGHT_F) / 256.0F);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(i + 0, j + j1, zLevel, (k + 0) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + j1, zLevel, (k + i1) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + 0, zLevel, (k + i1) * f, (l + 0) * f1);
		tessellator.addVertexWithUV(i + 0, j + 0, zLevel, (k + 0) * f, (l + 0) * f1);
		tessellator.draw();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		doRenderItem((EntityItem) entity, d, d1 + (entity.worldObj.multiplayerWorld ? 0.25d : 0.0d), d2, f, f1);
	}
}

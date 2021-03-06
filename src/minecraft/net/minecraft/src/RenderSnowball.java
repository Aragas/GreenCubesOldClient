// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, Tessellator, EntityPotion, PotionHelper, 
//            RenderManager, Entity

public class RenderSnowball extends Render {

	private int itemIconIndex;
	private float scale;

	public RenderSnowball(int i) {
		this(i, 0.5f);
	}

	public RenderSnowball(int i, float scale) {
		itemIconIndex = i;
		this.scale = scale;
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glScalef(scale, scale, scale);
		loadTexture("/gui/items.png");
		Tessellator tessellator = Tessellator.instance;
		if(itemIconIndex == 154) {
			int i = PotionHelper.func_40358_a(((EntityPotion) entity).func_40088_g(), false);
			float f2 = (i >> 16 & 0xff) / 255F;
			float f3 = (i >> 8 & 0xff) / 255F;
			float f4 = (i & 0xff) / 255F;
			GL11.glColor3f(f2, f3, f4);
			GL11.glPushMatrix();
			func_40265_a(tessellator, 141);
			GL11.glPopMatrix();
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
		}
		func_40265_a(tessellator, itemIconIndex);
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glPopMatrix();
	}

	private void func_40265_a(Tessellator tessellator, int i) {
		float f = ((i % 16) * 16 + 0) / 256F;
		float f1 = ((i % 16) * 16 + 16) / 256F;
		float f2 = ((i / 16) * 16 + 0) / RenderEngine.ITEMS_HEIGHT_F;
		float f3 = ((i / 16) * 16 + 16) / RenderEngine.ITEMS_HEIGHT_F;
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
		tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
		tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, f1, f2);
		tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, f, f2);
		tessellator.draw();
	}
}

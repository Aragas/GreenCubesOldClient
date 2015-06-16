// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerDispenser, FontRenderer, RenderEngine, 
//            InventoryPlayer, TileEntityDispenser

public class GuiDispenser extends GuiContainer {

	private TileEntityDispenser tileentitydispenser;

	public GuiDispenser(InventoryPlayer inventoryplayer, TileEntityDispenser tileentitydispenser) {
		super(new ContainerDispenser(inventoryplayer, tileentitydispenser));
		this.tileentitydispenser = tileentitydispenser;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// GreenCubes start
		fontRenderer.drawString(tileentitydispenser.getInvName(), 60, 6, 0x404040);
		fontRenderer.drawString("���������", 8, (ySize - 96) + 2, 0x404040);
		// GreenCubes end
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/gui/trap.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(k);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}
}

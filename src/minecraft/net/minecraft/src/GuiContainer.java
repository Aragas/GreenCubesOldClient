package net.minecraft.src;

import java.util.List;

import net.minecraft.client.Minecraft;

import org.greencubes.gui.FancyGUI;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainer extends GuiScreen {
	/** Stacks renderer. Icons, stack size, health, etc... */
	protected static RenderItem itemRenderer = RenderItem.getInstance();

	/** The X size of the inventory window in pixels. */
	protected int xSize;

	/** The Y size of the inventory window in pixels. */
	protected int ySize;

	/** A list of the players inventory slots. */
	public Container inventorySlots;

	/**
	 * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiLeft;

	/**
	 * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiTop;
	private TMIController tmi;

	protected boolean highlightItems = true;

	public GuiContainer(Container par1Container) {
		xSize = 176;
		ySize = 166;
		tmi = null;
		inventorySlots = par1Container;
		tmi = new TMIController(this, itemRenderer);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		mc.thePlayer.craftingInventory = inventorySlots;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		drawDefaultBackground();
		int i = guiLeft;
		int j = guiTop;
		drawGuiContainerBackgroundLayer(par3, par1, par2);
		RenderHelper.func_41089_c();
		GL11.glPushMatrix();
		GL11.glTranslatef(i, j, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		Slot slot = null;
		int k = 240;
		int l = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, k / 1.0F, l / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		for(int k1 = 0; k1 < inventorySlots.inventorySlots.size(); k1++) {
			Slot slot1 = inventorySlots.inventorySlots.get(k1);
			drawSlotInventory(slot1);

			if(isMouseOverSlot(slot1, par1, par2)) {
				slot = slot1;
				if(highlightItems) {
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					int l1 = slot1.xDisplayPosition;
					int i1 = slot1.yDisplayPosition;
					drawGradientRect(l1, i1, l1 + 16, i1 + 16, 0x80ffffff, 0x80ffffff);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
				}
			}
		}

		drawGuiContainerForegroundLayer(par1 - i, par2 - j);
		GL11.glEnable(GL11.GL_LIGHTING);
		InventoryPlayer inventoryplayer = mc.thePlayer.inventory;

		if(inventoryplayer.getItemStack() != null) {
			ItemStack itemstack = TMIUtils.getValidItem(inventoryplayer.getItemStack());
			GL11.glTranslatef(0.0F, 0.0F, 32F);
			zLevel = 200F;
			itemRenderer.zLevel = 200F;
			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, par1 - i - 8, par2 - j - 8);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, par1 - i - 8, par2 - j - 8);
			zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
		}

		GL11.glTranslatef(-i, -j, 0.0F);
		tmi.onEnterFrame(par1, par2, xSize, ySize);
		tmi.showToolTip(par1, par2);
		GL11.glTranslatef(i, j, 0.0F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		if(inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			List<String> list = TMIUtils.itemDisplayNameMultiline(slot.getStack(), TMIConfig.getInstance().isEnabled());

			if(!list.isEmpty()) {

				int i2 = (par1 - i);
				int k2 = par2 - j;
				
				FancyGUI.getInstance().renderScaledTooltip(i2, k2, list.toArray(new String[list.size()]), width - i, height - j, 4);
			}
		}

		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everythin in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected abstract void drawGuiContainerBackgroundLayer(float f, int i, int j);

	/**
	 * Draws an inventory slot
	 */
	private void drawSlotInventory(Slot par1Slot) {
		int i = par1Slot.xDisplayPosition;
		int j = par1Slot.yDisplayPosition;
		ItemStack itemstack = par1Slot.getStack();
		boolean flag = false;
		zLevel = 100F;
		itemRenderer.zLevel = 100F;

		if(itemstack == null) {
			int k = par1Slot.getBackgroundIconIndex();

			if(k >= 0) {
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glDisable(GL11.GL_LIGHTING);
				mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
				drawTexturedModalRectItem(i, j, (k % 16) * 16, (k / 16) * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				flag = true;
			}
		}

		if(!flag) {
			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
		}

		itemRenderer.zLevel = 0.0F;
		zLevel = 0.0F;
	}
	
	public void drawTexturedModalRectItem(int i, int j, int k, int l, int i1, int j1) {
		float f = 0.00390625F;
		float f1 = 0.00390625F / RenderEngine.ITEMS_MULT;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(i + 0, j + j1, zLevel, (k + 0) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + j1, zLevel, (k + i1) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + 0, zLevel, (k + i1) * f, (l + 0) * f1);
		tessellator.addVertexWithUV(i + 0, j + 0, zLevel, (k + 0) * f, (l + 0) * f1);
		tessellator.draw();
	}

	/**
	 * Returns the slot at the given coordinates or null if there is none.
	 */
	private Slot getSlotAtPosition(int par1, int par2) {
		for(int i = 0; i < inventorySlots.inventorySlots.size(); i++) {
			Slot slot = inventorySlots.inventorySlots.get(i);

			if(isMouseOverSlot(slot, par1, par2)) {
				return slot;
			}
		}

		return null;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		int i = xSize;
		int j = ySize;
		int k = guiLeft;
		int l = guiTop;
		Slot slot = getSlotAtPosition(par1, par2);
		Minecraft minecraft = mc;
		Container container = inventorySlots;
		boolean flag = par1 >= k && par2 >= l && par1 <= k + i && par2 <= l + j;

		if(par3 >= 0 && par3 <= 2) {
			tmi.replacementClickHandler(par1, par2, par3, flag, slot, minecraft, container);
		}
	}

	/**
	 * Returns if the passed mouse position is over the specified slot.
	 */
	private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3) {
		int i = guiLeft;
		int j = guiTop;
		par2 -= i;
		par3 -= j;
		return par2 >= par1Slot.xDisplayPosition - 1 && par2 < par1Slot.xDisplayPosition + 16 + 1 && par3 >= par1Slot.yDisplayPosition - 1 && par3 < par1Slot.yDisplayPosition + 16 + 1;
	}

	protected void handleMouseClick(Slot par1Slot, int par2, int par3, boolean par4) {
		if(par1Slot != null) {
			par2 = par1Slot.slotNumber;
		}

		mc.playerController.windowClick(inventorySlots.windowId, par2, par3, par4, mc.thePlayer);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2) {
		if(!tmi.handleKeypress(par1, par2) && (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)) {
			mc.thePlayer.closeScreen();
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed() {
		if(mc.thePlayer != null) {
			inventorySlots.onCraftGuiClosed(mc.thePlayer);
			mc.playerController.func_20086_a(inventorySlots.windowId, mc.thePlayer);
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		if(!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead) {
			mc.thePlayer.closeScreen();
		}
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() {
		int i = (Mouse.getEventX() * width) / mc.displayWidth;
		int j = height - (Mouse.getEventY() * height) / mc.displayHeight - 1;
		tmi.handleScrollWheel(i, j);
		super.handleMouseInput();
	}
}

package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiGift extends GuiContainer {

	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private StringBuilder giftTitle = new StringBuilder();
	private final boolean editable;
	private static final int maxWidth = 130;
	private static final int maxLength = 40;

	private int cursorCycle = 0;

	public GuiGift(IInventory iinventory, IInventory iinventory1, String title, boolean editable) {
		super(new ContainerChest(iinventory, iinventory1));
		upperChestInventory = iinventory;
		lowerChestInventory = iinventory1;
		allowUserInput = false;
		char c = '\336';
		int i = c - 108;
		ySize = i + 18;
		giftTitle.append(title);
		this.editable = editable;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(lowerChestInventory.getInvName(), 8, 6, 0x404040);
		fontRenderer.drawString(upperChestInventory.getInvName(), 8, (ySize - 96) + 2, 0x404040);
		fontRenderer.drawString(giftTitle.toString() + (editable && (cursorCycle / 10) % 2 == 0 ? "\247f_" : ""), 30, 23, 0xffffff);
	}

	@Override
	protected void keyTyped(char c, int i) {
		if(i == 1 || (i == mc.gameSettings.keyBindInventory.keyCode && !editable)) {
			if(editable)
				mc.getSendQueue().addToSendQueue(new Packet203GiftWindowTitle(inventorySlots.windowId, giftTitle.toString()));
			mc.thePlayer.closeScreen();
		} else if(editable) {
			if(i == Keyboard.KEY_BACK) {
				if(giftTitle.length() > 0) {
					giftTitle.deleteCharAt(giftTitle.length() - 1);
					while(giftTitle.length() > 0 && giftTitle.charAt(giftTitle.length() - 1) == '\247') {
						giftTitle.deleteCharAt(giftTitle.length() - 1);
					}
					while(giftTitle.length() > 1 && giftTitle.charAt(giftTitle.length() - 2) == '\247') {
						giftTitle.deleteCharAt(giftTitle.length() - 1);
						giftTitle.deleteCharAt(giftTitle.length() - 1);
					}
				}
			} else if(ChatAllowedCharacters.allowedCharacters.indexOf(c) >= 0) {
				int currWidth = fontRenderer.getStringWidth(giftTitle);
				if(giftTitle.length() < maxLength) {
					float charWidth = fontRenderer.charWidth[ChatAllowedCharacters.allowedCharacters.indexOf(c) + 32];
					if(currWidth + charWidth <= maxWidth) {
						giftTitle.append(c);
					}
				}
			}
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		cursorCycle++;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gc_images/gui/containergift.png"));
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, 18 + 17);
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/container.png"));
		drawTexturedModalRect(l, i1 + 18 + 17, 0, 126, xSize, 96);
	}

}

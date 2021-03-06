// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.THashMap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.greencubes.gui.FancyGUI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.jme3.math.ColorRGBA;

public class GuiIngame extends Gui {
	
	private static final long GREETINGS_APPEAR_TIME = 1000L;
	private static final long GREETINGS_FADE_TIME = 1000L;
	private static final long GREETINGS_TIME = 8000L;

	private static RenderItem itemRenderer = RenderItem.getInstance();
	private Random rand = new Random();
	private Minecraft mc;
	public String field_933_a = null;
	private int updateCounter = 0;
	private String recordPlaying = "";
	private int recordPlayingUpFor = 0;
	private boolean recordIsPlaying = false;
	public float damageGuiPartialTime;
	float prevVignetteBrightness = 1.0F;
	public ArrayList<?> chatMessageList = new ArrayList<Object>(); // �� ������� ��� ������ ��� ������������� � �����-�� �������...

	// GreenCubes Chat start
	private boolean clickedOnMove = false;
	public boolean inSettings = false;
	public boolean editingName = false;
	private int editingCursor = 0;
	private StringBuilder unsavedName = null;
	private int channelScroll = 0;
	// GreenCubes quests
	private GCNotify currentNotify;
	private boolean hideNotify = false;
	public GCNotify newNotify;
	// GreenCubes end

	public final Map<String, String> questData = new THashMap<String, String>();
	
	public ItemStack itemStackDescription;
	
	private String greetingsMessage;
	private long greetingsStart;
	private ColorRGBA greetingsColor;

	public GuiIngame(Minecraft minecraft) {
		mc = minecraft;
	}
	
	public void setGreetingsMessage(String message, ColorRGBA baseColor) {
		greetingsMessage = message;
		greetingsStart = System.currentTimeMillis();
		greetingsColor = baseColor;
	}

	public void renderGameOverlay(float f, boolean flag, int i, int j) {
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();
		int mouseX = (Mouse.getEventX() * k) / mc.displayWidth;
		int mouseY = l - (Mouse.getEventY() * l) / mc.displayHeight - 1;
		boolean inGui = mc.currentScreen instanceof GuiChat;

		FontRenderer fontrenderer = mc.fontRenderer;
		mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		if(Minecraft.isFancyGraphicsEnabled())
			renderVignette(mc.thePlayer.getEntityBrightness(f), k, l);
		else
			GL11.glBlendFunc(770, 771);
		ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(3);
		if(mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.itemID == Block.pumpkin.blockID)
			renderPumpkinBlur(k, l);
		if(!mc.thePlayer.isPotionActive(Potion.potionConfusion)) {
			float f1 = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * f;
			if(f1 > 0.0F)
				renderPortalOverlay(f1, k, l);
		}
		
		if(greetingsMessage != null) {
			int mw = fontrenderer.getStringWidth(greetingsMessage) / 2;
			GL11.glPushMatrix();
			GL11.glScalef(2, 2, 1);
			if(greetingsStart + GREETINGS_APPEAR_TIME > System.currentTimeMillis()) {
				greetingsColor.a = (float) (System.currentTimeMillis() - greetingsStart) / (float) GREETINGS_APPEAR_TIME * 0.9f + 0.1f;
			} else if(greetingsStart + GREETINGS_APPEAR_TIME + GREETINGS_TIME > System.currentTimeMillis()) {
				// Nothing
			} else if(greetingsStart + GREETINGS_APPEAR_TIME + GREETINGS_TIME + GREETINGS_FADE_TIME > System.currentTimeMillis()) {
				greetingsColor.a = 1f - (float) (System.currentTimeMillis() - greetingsStart - GREETINGS_APPEAR_TIME - GREETINGS_TIME) / (float) GREETINGS_FADE_TIME * 0.9f;
			} else {
				greetingsMessage = null;
			}
			if(greetingsMessage != null)
				fontrenderer.drawStringWithShadow(greetingsMessage, k / 4 - mw, 15, greetingsColor.asIntARGB());
			GL11.glPopMatrix();
		}
		
		String[] renderTooltip = null;
		
		if(!mc.playerController.func_35643_e()) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
			InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
			zLevel = -90F;
			drawTexturedModalRect(k / 2 - 91, l - 22, 0, 0, 182, 22);
			drawTexturedModalRect((k / 2 - 91 - 1) + inventoryplayer.currentItem * 20, l - 22 - 1, 0, 22, 24, 22);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(775, 769);
			drawTexturedModalRect(k / 2 - 7, l / 2 - 7, 0, 0, 16, 16);
			GL11.glDisable(GL11.GL_BLEND);
			boolean flag2 = (mc.thePlayer.heartsLife / 3) % 2 == 1;
			if(mc.thePlayer.heartsLife < 10)
				flag2 = false;
			int i2 = mc.thePlayer.getEntityHealth();
			int j3 = mc.thePlayer.prevHealth;
			rand.setSeed(updateCounter * 0x4c627);
			boolean flag4 = false;
			FoodStats foodstats = mc.thePlayer.getFoodStats();
			// GreenCubes start
			int i5 = foodstats.getFoodLevel();
			if(mc.isMultiplayerWorld()) // � ������������ ����� �� 5, �.�. ������ ������� ����� 100 �������, � �� 20
				i5 /= 5;
			// GreenCubes end
			int k5 = foodstats.getPrevFoodLevel();
			func_41039_c();
			if(mc.playerController.shouldDrawHUD()) {
				int k6 = k / 2 - 91;
				int j7 = k / 2 + 91;
				int k8 = l - 32;
				int j9 = k8 - 10;
				int armorValue = mc.thePlayer.getPlayerArmorValue();
				int j10 = -1;
				if(mc.thePlayer.isPotionActive(Potion.potionRegeneration))
					j10 = updateCounter % 25;
				for(int l10 = 0; l10 < 10; l10++) {
					if(armorValue > 0) {
						int k11 = k6 + l10 * 8;
						if(l10 * 2 + 1 < armorValue) 
							drawTexturedModalRect(k11, j9, 34, 9, 9, 9);
						else if(l10 * 2 + 1 == armorValue)
							drawTexturedModalRect(k11, j9, 25, 9, 9, 9);
						else if(l10 * 2 + 1 > armorValue)
							drawTexturedModalRect(k11, j9, 16, 9, 9, 9);
					}
					int l11 = 16;
					if(mc.thePlayer.isPotionActive(Potion.potionPoison))
						l11 += 36;
					int k12 = 0;
					if(flag2)
						k12 = 1;
					int j13 = k6 + l10 * 8;
					int k13 = k8;
					if(i2 <= 4)
						k13 += rand.nextInt(2);
					if(l10 == j10)
						k13 -= 2;
					byte byte5 = 0;
					if(mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
						byte5 = 5;
					drawTexturedModalRect(j13, k13, 16 + k12 * 9, 9 * byte5, 9, 9);
					if(flag2) {
						if(l10 * 2 + 1 < j3)
							drawTexturedModalRect(j13, k13, l11 + 54, 9 * byte5, 9, 9);
						else if(l10 * 2 + 1 == j3)
							drawTexturedModalRect(j13, k13, l11 + 63, 9 * byte5, 9, 9);
					}
					if(l10 * 2 + 1 < i2)
						drawTexturedModalRect(j13, k13, l11 + 36, 9 * byte5, 9, 9);
					else if(l10 * 2 + 1 == i2)
						drawTexturedModalRect(j13, k13, l11 + 45, 9 * byte5, 9, 9);
				}
				/* ����� ����� */
				/* ����� ������ */
				for(int i11 = 0; i11 < 10; i11++) {
					int i12 = k8;// ��������� �� ������
					int l12 = 16;// ����������� � �������� ������
					byte byte4 = 0;
					if(mc.thePlayer.isPotionActive(Potion.potionHunger)) {
						l12 += 36;
						byte4 = 13;// byte4 - ����� � �������� ����� ��� ������� ����� ��� ����������
					}
					if(mc.thePlayer.getFoodStats().getFoodSaturationLevel() <= 0.0F && updateCounter % (i5 * 3 + 1) == 0)
						i12 += rand.nextInt(3) - 1;// +������ � ��������� �� ������ ���� ���������� 0
					if(flag4)
						byte4 = 1;
					int l13 = j7 - i11 * 8 - 9;
					drawTexturedModalRect(l13, i12, 16 + byte4 * 9, 27, 9, 9);// �������� ������
					if(flag4) {
						if(i11 * 2 + 1 < k5)
							drawTexturedModalRect(l13, i12, l12 + 54, 27, 9, 9);// �������������� �����
						else if(i11 * 2 + 1 == k5)
							drawTexturedModalRect(l13, i12, l12 + 63, 27, 9, 9);// �������������� �����, ���������
					}
					if(i11 * 2 + 1 < i5)// i5 - ������� ������� �������
						drawTexturedModalRect(l13, i12, l12 + 36, 27, 9, 9);// �����
					else if(i11 * 2 + 1 == i5)
						drawTexturedModalRect(l13, i12, l12 + 45, 27, 9, 9);// ��������� ������
				}
				/* ����� ����� */
				if(mc.thePlayer.isInsideOfMaterial(Material.water)) {
					int j11 = (int) Math.ceil(((mc.thePlayer.func_41001_Z() - 2) * 10D) / 300D);
					int j12 = (int) Math.ceil((mc.thePlayer.func_41001_Z() * 10D) / 300D) - j11;
					for(int i13 = 0; i13 < j11 + j12; i13++) {
						if(i13 < j11)
							drawTexturedModalRect(j7 - i13 * 8 - 9, j9, 16, 18, 9, 9);
						else
							drawTexturedModalRect(j7 - i13 * 8 - 9, j9, 25, 18, 9, 9);
					}

				}
			}
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
			RenderHelper.func_41089_c();
			for(int l6 = 0; l6 < 9; l6++) {
				int k7 = (k / 2 - 90) + l6 * 20 + 2;
				int i8 = l - 16 - 3;
				renderInventorySlot(l6, k7, i8, f);
			}
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
			if(mc.thePlayer.activeBuffs.size() > 0) {
				int k7 = (k / 2 - 90) + 180 + 3;
				int i8 = l - 19;
				int n = 0;
				TIntObjectIterator<BuffActive> iterator = mc.thePlayer.activeBuffs.iterator();
				while(iterator.hasNext()) {
					iterator.advance();
					BuffActive buff = iterator.value();
					if(buff != null && buff.buff != null && buff.buff.getTextureFramed() != null) {
						renderBuff(buff, k7 + n * 19, i8);
						if(inGui && mouseX > k7 + n * 19 && mouseX < k7 + n * 19 + 19 && mouseY > i8 && mouseY < i8 + 19) {
							renderTooltip = buff.buff.getBuffDescription(buff).split("\n");
						}
						n++;
					}
				}
			}
			
			// Party icon
			/*
			FancyGUI.getInstance().enableMode();
			FancyGUI.getInstance().renderInterface(k - 19, l - 19, 17, 17, 305, 339, 34, 34);
			FancyGUI.getInstance().renderInterface(k - 18, l - 18, 16, 16, 373, 347, 32, 32);
			FancyGUI.getInstance().renderInterface(k - 19, l - 19, 17, 17, 305, 374, 34, 34);
			mc.fontRenderer.drawStringWithShadow(ChatColor.GOLD + "P", (k - 19 + 19) - 4 - mc.fontRenderer.getStringWidth("P"), l - 19 + 8, 0x0AC80A);
			*/
			// Journal icon
			/*
			FancyGUI.getInstance().enableMode();
			FancyGUI.getInstance().renderInterface(k - 19, l - 19, 17, 17, 305, 339, 34, 34);
			FancyGUI.getInstance().renderInterface(k - 18, l - 18, 16, 16, 340, 347, 32, 32);
			FancyGUI.getInstance().renderInterface(k - 19, l - 19, 17, 17, 305, 374, 34, 34);
			mc.fontRenderer.drawStringWithShadow(ChatColor.GOLD + "J", (k - 19 + 19) - 4 - mc.fontRenderer.getStringWidth("J"), l - 19 + 8, 0x0AC80A);
			FancyGUI.getInstance().enableMode();*/
			//String tt = "\2476����������� � ������!\n\n" +
			//"\247f�� �������� ����������� � ������ �� ������ \247r99446666[\247rff66c016G\247rfff7f7f7C\247r99446666] \247rffea8df7Rena4ka\247f. ����� ������� �����������, ������� \247bP\247f ��� �������� � ���� \247b/p accept\247f.";
			//FancyGUI.getInstance().renderScaledTooltip(k - 19, l - 19, GChat.wrapText(tt, 250, 0, false), k, l, 4);
			
			//FancyGUI.getInstance().disableMode();
		}
		if(mc.thePlayer.getSleepTimer() > 0) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			int i1 = mc.thePlayer.getSleepTimer();
			float f3 = i1 / 100F;
			if(f3 > 1.0F)
				f3 = 1.0F - (i1 - 100) / 10F;
			int j2 = (int) (220F * f3) << 24 | 0x101020;
			drawRect(0, 0, k, l, j2);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
		GL11.glPushMatrix();
		int shift = -54;
		if(mc.gameSettings.showDebugInfo && mc.gameSettings.showDebugProfilerChart) {
			fontrenderer.drawStringWithShadow((new StringBuilder()).append(mc.debug).toString(), 2, 2, 0xffffff);
			fontrenderer.drawStringWithShadow(mc.debugInfoRenders(), 2, 12, 0xffffff);
			fontrenderer.drawStringWithShadow(mc.func_6262_n(), 2, 22, 0xffffff);
			fontrenderer.drawStringWithShadow(mc.debugInfoEntities(), 2, 32, 0xffffff);
			fontrenderer.drawStringWithShadow(mc.func_21002_o(), 2, 42, 0xffffff);
			fontrenderer.drawStringWithShadow(mc.debugInfoTiles(), 2, 52, 0xffffff);
			long l1 = Runtime.getRuntime().maxMemory();
			long l2 = Runtime.getRuntime().totalMemory();
			long l4 = Runtime.getRuntime().freeMemory();
			long l5 = l2 - l4;
			String s1 = (new StringBuilder()).append("������: ").append((l5 * 100L) / l1).append("% (").append(l5 / 1024L / 1024L).append("MB) �� ").append(l1 / 1024L / 1024L).append("MB").toString();
			drawString(fontrenderer, s1, k - fontrenderer.getStringWidth(s1) - 2, 2, 0xe0e0e0);
			s1 = (new StringBuilder()).append("���������� ������: ").append((l2 * 100L) / l1).append("% (").append(l2 / 1024L / 1024L).append("MB)").toString();
			drawString(fontrenderer, s1, k - fontrenderer.getStringWidth(s1) - 2, 12, 0xe0e0e0);
			/*int i12 = 0;
			for(int i11 = WorldRenderer.lastUpdatedRenderers.size() - 1; i11 >= 0; --i11) {
				WorldRenderer wr = WorldRenderer.lastUpdatedRenderers.get(i11);
				s1 = "WR(" + wr.posX + "," + wr.posY + "," + wr.posZ + ", U: " + wr.lastUpdateCause + ")";
				drawString(fontrenderer, s1, k - fontrenderer.getStringWidth(s1) - 2, 12 + (i12++) * 10, 0xe0e0e0);
			}*/

			drawString(fontrenderer, String.format("x: %.2f", this.mc.thePlayer.posX), 2, 64, 0xffffff);
			drawString(fontrenderer, String.format("y: %.2f (����, %.2f �����)", this.mc.thePlayer.boundingBox.minY, this.mc.thePlayer.posY), 2, 72, 0xffffff);
			drawString(fontrenderer, String.format("z: %.2f", this.mc.thePlayer.posZ), 2, 80, 0xffffff);
			drawString(fontrenderer, (new StringBuilder()).append("������� �����: ").append(Direction.CardinalDirection[MathHelper.floor_double(((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3]).toString(), 2, 88, 0xffffff);
			drawString(fontrenderer, new StringBuilder().append("��� ���������: ").append((mc.theWorld.getBiomeAt(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.posY - 1.0D), MathHelper.floor_double(mc.thePlayer.posZ)).getBiomeTranslatedName())).toString(), 2, 96, 0xffffff);
		} else if(mc.gameSettings.showDebugInfo) {
			//Test.parseFile();
			fontrenderer.drawStringWithShadow(new StringBuilder().append("\2472Green\247fCubes").append(mc.thePlayer.flyControl.isFlying() ? " ~ Flying" : "").toString(), 2, 2, 0xffffff);
			long l1 = Runtime.getRuntime().maxMemory();
			long l2 = Runtime.getRuntime().totalMemory();
			long l4 = Runtime.getRuntime().freeMemory();
			long l5 = l2 - l4;
			String s1 = (new StringBuilder()).append("������: ").append((l5 * 100L) / l1).append("% (").append(l5 / 1024L / 1024L).append("MB) �� ").append(l1 / 1024L / 1024L).append("MB").toString();
			drawString(fontrenderer, s1, k - fontrenderer.getStringWidth(s1) - 2, 2, 0xe0e0e0);
			s1 = (new StringBuilder()).append("���������� ������: ").append((l2 * 100L) / l1).append("% (").append(l2 / 1024L / 1024L).append("MB)").toString();
			drawString(fontrenderer, s1, k - fontrenderer.getStringWidth(s1) - 2, 12, 0xe0e0e0);
			drawString(fontrenderer, mc.debug, k - fontrenderer.getStringWidth(mc.debug) - 2, 22, 0xe0e0e0);
			drawString(fontrenderer, String.format("x: %.2f", this.mc.thePlayer.posX), 2, 64 + shift, 0xffffff);
			drawString(fontrenderer, String.format("y: %.2f (����, %.2f �����)", this.mc.thePlayer.boundingBox.minY, this.mc.thePlayer.posY), 2, 72 + shift, 0xffffff);
			drawString(fontrenderer, String.format("z: %.2f", this.mc.thePlayer.posZ), 2, 80 + shift, 0xffffff);
			drawString(fontrenderer, (new StringBuilder()).append("������� �����: ").append(Direction.CardinalDirection[MathHelper.floor_double(((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3]).toString(), 2, 88 + shift, 0xffffff);
			drawString(fontrenderer, new StringBuilder().append("��� ���������: ").append((mc.theWorld.getBiomeAt(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.posY - 1.0D), MathHelper.floor_double(mc.thePlayer.posZ)).getBiomeTranslatedName())).toString(), 2, 96 + shift, 0xffffff);
		} else {
			fontrenderer.drawStringWithShadow(new StringBuilder().append("\2472Green\247fCubes (X: ").append(MathHelper.floor_double(this.mc.thePlayer.posX)).append(", Y: ").append(MathHelper.floor_double(this.mc.thePlayer.boundingBox.minY)).append(", Z: ").append(MathHelper.floor_double(mc.thePlayer.posZ)).append(", ").append(Direction.CardinalDirection[MathHelper.floor_double(((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3]).append(')').append(mc.thePlayer.flyControl.isFlying() ? " ~ Flying" : "").toString(), 2, 2, 0xffffff);
		}
		GL11.glPopMatrix();
		if(!mc.playerController.isInCreativeMode() && mc.isMultiplayerWorld()) {
			int c = mc.thePlayer.getFoodStats().getFoodLevel();
			fontrenderer.drawStringWithShadow(c + "", k / 2 + 92, l - 33, (c >= 150 ? 40 : c < -40 ? 255 : (c <= 50 ? Math.abs((int) (c * 1.7) - 140) : 0)) << 16 | (c >= 150 ? 255 : c < -40 ? 40 : (c >= 10 ? (int) (c * 1.7) - 15 : 0)) << 8 | 40);
		}
		
		if(itemStackDescription != null) {
			List<String> list = TMIUtils.itemDisplayNameMultiline(itemStackDescription, TMIConfig.getInstance().isEnabled());
			if(!list.isEmpty())
				FancyGUI.getInstance().renderScaledTooltip(k / 2, l / 2, list.toArray(new String[list.size()]), k, l, 5);
		}
		if(renderTooltip != null) {
			FancyGUI.getInstance().renderScaledTooltip(mouseX, mouseY, renderTooltip, k, l, 4);
		}
		
		if(recordPlayingUpFor > 0) {
			float f2 = recordPlayingUpFor - f;
			int k1 = (int) ((f2 * 256F) / 20F);
			if(k1 > 255) {
				k1 = 255;
			}
			if(k1 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef(k / 2, l - 48, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(770, 771);
				int k2 = 0xffffff;
				if(recordIsPlaying) {
					k2 = Color.HSBtoRGB(f2 / 50F, 0.7F, 0.6F) & 0xffffff;
				}
				fontrenderer.drawString(recordPlaying, -fontrenderer.getStringWidth(recordPlaying) / 2, -4, k2 + (k1 << 24));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}
		Profiler.startSection("Chat");
		// GreenCubes Chat start
		int byte2 = mc.chat.lines;
		byte leftPadding = 2;
		boolean showInterface = inGui || mc.chat.locked;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, l - 48, 0.0F);
		if(showInterface)
			drawRect(leftPadding, 8, leftPadding + 320, -byte2 * 9 + 8, mc.chat.bgColor);
		for(int j7 = mc.chat.chatScroll; j7 < mc.chat.activeTab.lines.size() && j7 < byte2 + mc.chat.chatScroll; j7++) {
			if(mc.chat.activeTab.lines.get(j7).age < 200 || showInterface) {
				double d = mc.chat.activeTab.lines.get(j7).age / 200D;
				d = 1.0D - d;
				d *= 10D;
				if(d < 0.0D)
					d = 0.0D;
				else if(d > 1.0D)
					d = 1.0D;
				d *= d;
				int l9 = (int) ((mc.chat.bgColor >> 24) * d);
				if(showInterface)
					l9 = mc.chat.bgColor >> 24;
				if(l9 > 0) {
					int k12 = (-j7 + mc.chat.chatScroll) * 9;
					GLine line = mc.chat.activeTab.lines.get(j7);
					String s1 = line.text;
					if(!showInterface)
						drawRect(leftPadding, k12 - 1, leftPadding + 320, k12 + 8, (l9 << 24) + (mc.chat.bgColor & 0xFFFFFF));
					int spaces = 0; // GCUtil.count(s1, ' ');
					float spaceWidth = 0; // mc.chat.activeTab.lines.get(j7).canWide ? (float) (GChat.chatWindowWidth - mc.fontRenderer.getStringWidth(s1)) / spaces : 0;
					//if(spaceWidth > 5)
					//	spaceWidth = 0;
					line.isUsed = true;
					GL11.glPushMatrix();
					GL11.glTranslatef(leftPadding + 2, k12, 0);
					fontrenderer.drawStringWithShadow(s1, 0, 0, 0xffffff, spaceWidth, spaces);
					GL11.glPopMatrix();
				}
			}
		}
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glPopMatrix();
		if(showInterface) {
			if(clickedOnMove) {
				int x = Mouse.getEventX() * k * 2 / mc.displayWidth;
				int y = Mouse.getEventY() * l * 2 / mc.displayHeight;
				int tY = (48 + mc.chat.lines * 9 + 5) * 2 - 23;
				int line = (((y + 25) / 2) - 5 - 48) / 9;
				if(line != mc.chat.lines && line > 0)
					mc.chat.lines = line;
			}
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1f);
			GL11.glTranslatef(leftPadding, l - 48 - mc.chat.lines * 9 + 8, 0.0F);
			GL11.glScalef(0.5f, 0.5f, 0.0f);
			GL11.glTranslatef(0, -25, 0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gc_images/gui/chat.png"));
			drawChatInterface(0, 0, 0, 0, 640, 25);
			drawChatInterface(3, 0, 0, 28, 25, 25);
			drawChatInterface(59, 0, 75, 28, 25, 25);
			drawChatInterface(87, 0, 100, 28, 25, 25);
			drawChatInterface(115 + 3 * 156, 0, 587, 28, 25, 25);
			drawChatInterface(115 + 3 * 156 + 25 + 3, 0, 587 + 25, 28, 25, 25);
			for(int n = mc.chat.tabScroll; n < 3 + mc.chat.tabScroll && n < mc.chat.tabs.size(); n++) {
				int padd = n - mc.chat.tabScroll;
				GTab tab = mc.chat.tabs.get(n);
				drawChatInterface(115 + padd * 157, 0, 125 + (mc.chat.activeTab == tab ? 308 : tab.hasNewMessage ? 154 : 0), 28, 154, 25);
			}
			if(!mc.chat.locked)
				drawChatInterface(31, 0, 25, 28, 25, 25);
			else
				drawChatInterface(31, 0, 50, 28, 25, 25);
			GL11.glScalef(2f, 2f, 0.0f);
			for(int n = mc.chat.tabScroll; n < 3 + mc.chat.tabScroll && n < mc.chat.tabs.size(); n++) {
				int padd = n - mc.chat.tabScroll;
				GTab tab = mc.chat.tabs.get(n);
				drawCenteredString(fontrenderer, tab.name, (115 + padd * 159 + 154 / 2) / 2, 2, 0xefefef);
			}
			if(inSettings) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glTranslatef(0, 12.5f, 0);
				drawRectSimple(0, 0, 100, 15 * 9 + 13, (0xFF << 24) | (mc.chat.bgColor & 0xFFFFFF));
				drawRectSimple(5, 22, 95, 32, 0xffe4e4e4);
				drawRectSimple(5, 22, 95, 21, 0xff151515);
				drawRectSimple(96, 22, 95, 32, 0xff000000);
				drawRectSimple(5, 33, 95, 32, 0xff000000);
				drawRectSimple(5, 22, 4, 32, 0xff151515);
				GChannel[] channels = mc.chat.channels.values().toArray(new GChannel[0]);
				GL11.glScalef(0.5f, 0.5f, 0.0f);
				GL11.glColor4f(1, 1, 1, 1f);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gc_images/gui/chat.png"));
				drawChatInterface((200 - 154) / 2, 15 * 9 * 2, 125 + 308, 28, 154, 25);
				if(!mc.chat.activeTab.isSystem)
					drawChatInterface((200 - 154) / 2, 15 * 9 * 2 - 26, 125 + 308, 28, 154, 25);
				for(int p = channelScroll; p < 7 + channelScroll && p < channels.length; ++p) {
					GChannel c = channels[p];
					if(c.type == GChannel.Type.SYSTEM && mc.chat.activeTab.isSystem)
						drawChatInterface(10, (42 + (p - channelScroll) * 10) * 2, 60, 53, 20, 20);
					else if(c.attachedTabs.contains(mc.chat.activeTab))
						drawChatInterface(10, (42 + (p - channelScroll) * 10) * 2, 20, 53, 20, 20);
					else
						drawChatInterface(10, (42 + (p - channelScroll) * 10) * 2, 0, 53, 20, 20);
				}
				if(mc.chat.activeTabIndex > 0)
					drawChatInterface(3, 2, 100, 28, 25, 25);
				if(mc.chat.activeTabIndex < mc.chat.tabs.size() - 1)
					drawChatInterface(200 - 3 - 25, 2, 587, 28, 25, 25);
				GL11.glScalef(2, 2, 0);
				if(!mc.chat.activeTab.isSystem)
					drawCenteredString(fontrenderer, "������� �������", 50, 15 * 9 + 2 - 13, 0xefefef);
				drawCenteredString(fontrenderer, "���������", 50, 15 * 9 + 2, 0xefefef);
				fontrenderer.drawString(editingName ? unsavedName.toString() : mc.chat.activeTab.name, 7, 23, 0x000000);
				if(editingName) {
					if(updateCounter % 12 > 4)
						fontrenderer.drawString("_", 7 + fontrenderer.getStringPartWidth(unsavedName, editingCursor), 24, 0x000000);
				}
				drawString(fontrenderer, "������:", 5, 33, 0xefefef);
				for(int p = channelScroll; p < 7 + channelScroll && p < channels.length; ++p) {
					drawString(fontrenderer, (channels[p].type != GChannel.Type.SYSTEM ? channels[p].userId + ". " : "") + channels[p].name, 16, 43 + (p - channelScroll) * 10, 0xffffff);
				}
				drawCenteredString(fontrenderer, "���������", 50, 2, 0xefefef);
				drawString(fontrenderer, "��� �������:", 5, 12, 0xefefef);
			}
			GL11.glPopMatrix();
		}
		Profiler.endSection();
		if(!hideNotify && newNotify != null && currentNotify != null)
			hideNotify = true;
		if(hideNotify && currentNotify != null) {
			currentNotify.animation -= 1F / 50F;
			if(currentNotify.animation <= 0) {
				currentNotify = null;
				hideNotify = false;
			}
		}
		if(currentNotify == null && newNotify != null) {
			currentNotify = newNotify;
			hideNotify = false;
			newNotify = null;
		}
		if(currentNotify != null) {
			Profiler.startSection("Notify");
			if(currentNotify.animation < 1.0 && !hideNotify)
				currentNotify.animation += 1F / 50F;
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1f);
			GL11.glScalef((float) scaledresolution.scaledWidthD / mc.displayWidth, (float) scaledresolution.scaledHeightD / mc.displayHeight, 0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gc_images/gui/quests.png"));
			drawNotifySquare(mc.displayWidth - 370, currentNotify.animation * 49F - 49F, 369, 49, zLevel, 98, 149, 370, 49);
			if(currentNotify.item != null)
				drawNotifySquare(mc.displayWidth - 370 + 8, currentNotify.animation * 49F - 49F + 8, 32, 32, zLevel, 469, 149, 34, 33);
			int h = 6;
			int w = currentNotify.item != null ? 49 : 17;
			if(currentNotify.formattedText == null) {
				currentNotify.text = String.format(currentNotify.text, GameSettings.func_41085_c(mc.gameSettings.keyBindNotify.keyCode));
				currentNotify.formattedText = GChat.wrapText(currentNotify.text, (360 - w) / 2, 0, false);
			}
			GL11.glTranslatef(mc.displayWidth - 370 + w, currentNotify.animation * 49F - 49F + h, zLevel);
			GL11.glScalef(2, 2, 0);
			for(int i1 = 0; i1 < currentNotify.formattedText.length && i1 < 2; ++i1) {
				String s = currentNotify.formattedText[i1];
				drawString(fontrenderer, s, 0, i1 * 10, 0xefefef);
			}
			GL11.glPopMatrix();
			if(currentNotify.item != null) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				RenderHelper.func_41089_c();
				GL11.glPushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				itemRenderer.zLevel = 100;
				itemRenderer.renderItemIntoGUI(fontrenderer, mc.renderEngine, currentNotify.item, (mc.displayWidth - 370 + 8) / 2, (int) ((currentNotify.animation * 49F - 49F + 8) / 2));
				itemRenderer.zLevel = 0;
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				RenderHelper.disableStandardItemLighting();
				GL11.glPopMatrix();
			}
			Profiler.endSection();
		}
		if(questData.size() > 0) {
			Profiler.startSection("Quests");
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1f);
			GL11.glScalef((float) scaledresolution.scaledWidthD / mc.displayWidth, (float) scaledresolution.scaledHeightD / mc.displayHeight, 0);
			GL11.glTranslatef(mc.displayWidth, mc.displayHeight * 0.3f, zLevel);
			GL11.glScalef(2, 2, 0);
			Iterator<Entry<String, String>> iterator = questData.entrySet().iterator();
			int width = 0;
			while(iterator.hasNext())
				width = Math.max(width, fontrenderer.getStringWidth(iterator.next().getValue()));
			width += 5;
			iterator = questData.entrySet().iterator();
			int n = 0;
			boolean notify = false;
			while(iterator.hasNext()) {
				Entry<String, String> e = iterator.next();
				if(e.getKey().equals("Notify")) {
					notify = true;
					continue;
				}
				drawString(fontrenderer, e.getValue(), -width, n++ * 10, 0xefefef);
			}
			if(notify) {
				String s = "������� " + GameSettings.func_41085_c(mc.gameSettings.keyBindNotify.keyCode) + " ����� ��������� ����� �������";
				drawString(fontrenderer, s, -mc.fontRenderer.getStringWidth(s) - 5, ++n * 10, 0xefefef);
			}
			GL11.glPopMatrix();
			Profiler.endSection();
		}
	}

	public void renderBuff(BuffActive buff, int x, int y) {
		FancyGUI.getInstance().enableMode();
		FancyGUI.getInstance().renderInterface(x - 1, y - 1, 17, 17, 305, 339, 34, 34);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(buff.buff.getTextureFramed()));
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(x, y, zLevel, 0, 0);
		t.addVertexWithUV(x, 16 + y, zLevel, 0, 1);
		t.addVertexWithUV(x + 16, y + 16, zLevel, 1, 1);
		t.addVertexWithUV(x + 16, y, zLevel, 1, 0);
		t.draw();
		if(buff.timeLeft != -1) {
			String s = GCUtil.getMaxTimeString(buff.timeLeft);
			mc.fontRenderer.drawStringWithShadow(s, (x + 19) - 4 - mc.fontRenderer.getStringWidth(s), y + 6, 0x0AC80A);
		}
		FancyGUI.getInstance().renderInterface(x - 1, y - 1, 17, 17, 305, 374, 34, 34);
		FancyGUI.getInstance().disableMode();
	}

	public void notifyAnswer() {
		if(currentNotify != null)
			hideNotify = true;
		mc.getSendQueue().addToSendQueue(new Packet208NotifyAnswer());
	}

	public static void drawNotifySquare(double x1, double y1, double width, double height, double zLevel, int texX, int texY, int texWidth, int texHeight) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		drawQuestSquare2(x1, y1, width, height, zLevel, texX, texY, texWidth, texHeight);
		t.draw();
	}

	public static void drawQuestSquare2(double x1, double y1, double width, double height, double zLevel, int texX, int texY, int texWidth, int texHeight) {
		double heightT = (texHeight - 0.0001D) / 512.0D;
		double widthT = (texWidth - 0.0001D) / 512.0D;
		double tX = texX / 512.0D;
		double tY = texY / 512.0D;
		Tessellator t = Tessellator.instance;
		t.addVertexWithUV(x1, y1, zLevel, tX, tY);
		t.addVertexWithUV(x1, y1 + height, zLevel, tX, tY + heightT);
		t.addVertexWithUV(x1 + width, y1 + height, zLevel, tX + widthT, tY + heightT);
		t.addVertexWithUV(x1 + width, y1, zLevel, tX + widthT, tY);
	}

	public void keyForName(char c, int i) {
		if(i == Keyboard.KEY_LEFT) {
			if(editingCursor > 0)
				editingCursor--;
		} else if(i == Keyboard.KEY_RIGHT) {
			if(editingCursor < unsavedName.length())
				editingCursor++;
		} else if(i == Keyboard.KEY_DELETE) {
			if(unsavedName.length() > editingCursor)
				unsavedName.deleteCharAt(editingCursor);
		} else if(i == Keyboard.KEY_BACK) {
			if(editingCursor > 0)
				unsavedName.deleteCharAt(--editingCursor);
		} else if(i == Keyboard.KEY_ESCAPE) {
			editingName = false;
			unsavedName = null;
		} else {
			int id = ChatAllowedCharacters.allowedCharacters.indexOf(c);
			if(id >= 0) {
				float newWidth = mc.fontRenderer.getStringWidth(unsavedName) + mc.fontRenderer.charWidth[id + 32];
				if(newWidth <= GChat.tabWidth)
					unsavedName.insert(editingCursor++, c);
			}
		}
	}

	public boolean scroll() {
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int x = Mouse.getEventX() * scaledresolution.getScaledWidth() * 2 / mc.displayWidth;
		int y = Mouse.getEventY() * scaledresolution.getScaledHeight() * 2 / mc.displayHeight;
		int tY = (48 + mc.chat.lines * 9 + 5) * 2 - 23;
		if(y >= tY && y <= tY + 25) {
			int k1 = Mouse.getDWheel();
			if(k1 > 0) {
				if(mc.chat.tabScroll < mc.chat.tabs.size() - 1)
					mc.chat.tabScroll++;
			} else if(k1 < 0) {
				if(mc.chat.tabScroll > 0)
					mc.chat.tabScroll--;
			}
			return true;
		} else if(inSettings && y <= tY + 23 && x >= 3 * 2 && x <= (3 + 100) * 2) {
			int k1 = Mouse.getDWheel();
			if(k1 > 0) {
				if(channelScroll > 0)
					channelScroll--;
			} else if(k1 < 0) {
				if(channelScroll < mc.chat.activeTab.attachedChannels.size() - 1)
					channelScroll++;
			}
			return true;
		}
		return false;
	}

	public void mouseClicked(int x, int y, int key) {
		if(key == 0) {
			ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			x = Mouse.getEventX() * scaledresolution.getScaledWidth() * 2 / mc.displayWidth;
			y = Mouse.getEventY() * scaledresolution.getScaledHeight() * 2 / mc.displayHeight;
			int tY = (48 + mc.chat.lines * 9 + 5) * 2 - 23;
			int tX = 6;
			if(y >= tY && y <= tY + 25) {
				if(x >= tX && x <= tX + 25)
					clickedOnMove = true;
				else if(x >= tX + 28 && x <= tX + 28 + 25) {
					if(mc.chat.locked)
						mc.chat.locked = false;
					else
						mc.chat.locked = true;
					mc.chat.saveConfig();
				} else if(x >= tX + 28 * 2 && x <= tX + 28 * 2 + 25) {
					if(inSettings)
						inSettings = false;
					else {
						unsavedName = null;
						editingName = false;
						inSettings = true;
						channelScroll = 0;
					}
				} else if(x >= tX + 28 * 3 && x <= tX + 28 * 3 + 25) {
					if(mc.chat.tabScroll > 0)
						mc.chat.tabScroll--;
				} else if(x >= tX + 28 * 4 && x <= tX + 28 * 4 + 154) {
					int index = mc.chat.tabScroll;
					if(mc.chat.tabs.size() > index) {
						GTab tab = mc.chat.tabs.get(index);
						mc.chat.activeTab = tab;
						mc.chat.activeTabIndex = index;
						mc.chat.activeTab.hasNewMessage = false;
						if(inSettings)
							inSettings = false;
					}
				} else if(x >= tX + 28 * 4 + 157 && x <= tX + 28 * 4 + 157 + 154) {
					int index = mc.chat.tabScroll + 1;
					if(mc.chat.tabs.size() > index) {
						GTab tab = mc.chat.tabs.get(index);
						mc.chat.activeTab = tab;
						mc.chat.activeTabIndex = index;
						mc.chat.activeTab.hasNewMessage = false;
						if(inSettings)
							inSettings = false;
					}
				} else if(x >= tX + 28 * 4 + 157 * 2 && x <= tX + 28 * 4 + 157 * 2 + 154) {
					int index = mc.chat.tabScroll + 2;
					if(mc.chat.tabs.size() > index) {
						GTab tab = mc.chat.tabs.get(index);
						mc.chat.activeTab = tab;
						mc.chat.activeTabIndex = index;
						mc.chat.activeTab.hasNewMessage = false;
						if(inSettings)
							inSettings = false;
					}
				} else if(x >= tX + 28 * 4 + 157 * 3 && x <= tX + 28 * 4 + 157 * 3 + 25) {
					if(mc.chat.tabScroll < mc.chat.tabs.size() - 1)
						mc.chat.tabScroll++;
				} else if(x >= tX + 28 * 5 + 157 * 3 && x <= tX + 28 * 5 + 157 * 3 + 25) {
					String nameBase = "����� �������";
					String nameNew = "����� �������";
					boolean matches = false;
					int n = 1;
					do {
						boolean matched = false;
						for(int i = 0; i < mc.chat.tabs.size(); ++i) {
							if(mc.chat.tabs.get(i).name.equals(nameNew)) {
								nameNew = nameBase + " " + (++n);
								matched = true;
								break;
							}
						}
						matches = matched;
					} while(matches);
					GTab newTab = new GTab(mc.chat, nameNew);
					mc.chat.tabs.add(newTab);
					mc.chat.activeTab = newTab;
					mc.chat.activeTabIndex = mc.chat.tabs.size() - 1;
					if(mc.chat.activeTabIndex - 2 >= mc.chat.tabScroll)
						mc.chat.tabScroll = mc.chat.activeTabIndex - 2;
					if(inSettings)
						inSettings = false;
					mc.chat.saveConfig();
				}
			} else if(inSettings && y <= tY + 23 && x >= 3 * 2 && x <= (3 + 100) * 2) {
				y = tY - y - 3;
				x -= 3 * 2;
				y /= 2;
				x /= 2;
				if(x >= 5 && x <= 95 && y >= 22 && y <= 32) {
					editingName = true;
					editingCursor = mc.chat.activeTab.name.length();
					unsavedName = new StringBuilder(mc.chat.activeTab.name);
				} else if(x >= 11 && x <= 100 - 12 && y >= 15 * 9 + 1 && y <= 15 * 9 + 13) {
					if(unsavedName != null && unsavedName.length() > 0)
						mc.chat.activeTab.name = unsavedName.toString();
					editingName = false;
					inSettings = false;
					mc.chat.saveConfig();
				} else if(x >= 11 && x <= 100 - 12 && y >= 15 * 9 + 1 - 13 && y <= 15 * 9 + 13 - 13) {
					if(!mc.chat.activeTab.isSystem && mc.chat.tabs.size() > 1) {
						mc.chat.tabs.remove(mc.chat.activeTab);
						Iterator<GChannel> i = mc.chat.activeTab.attachedChannels.values().iterator();
						while(i.hasNext()) {
							GChannel c = i.next();
							c.attachedTabs.remove(mc.chat.activeTab);
						}
						mc.chat.activeTab = mc.chat.tabs.get(0);
						mc.chat.activeTabIndex = 0;
						mc.chat.activeTab.hasNewMessage = false;
						mc.chat.saveConfig();
						if(mc.chat.activeTabIndex - 2 >= mc.chat.tabScroll)
							mc.chat.tabScroll = mc.chat.activeTabIndex - 2;
						if(mc.chat.activeTabIndex < mc.chat.tabScroll)
							mc.chat.tabScroll = mc.chat.activeTabIndex;
					}
					editingName = false;
					inSettings = false;
				} else if(x >= 4 && x <= 96 && y >= 43) {
					int channel = (y - 43) / 10;
					// System.out.println("Channel: " + channel);
					if(channel <= 6 && channel >= 0) {
						GChannel[] channels = mc.chat.channels.values().toArray(new GChannel[0]);
						channel += channelScroll;
						if(channel >= 0 && channel < channels.length) {
							GChannel c = channels[channel];
							if(c.type != GChannel.Type.SYSTEM || !mc.chat.activeTab.isSystem) {
								if(c.attachedTabs.contains(mc.chat.activeTab)) {
									mc.chat.activeTab.attachedChannels.remove(c.id);
									c.attachedTabs.remove(mc.chat.activeTab);
									mc.chat.activeTab.readyToJoin.remove(Integer.valueOf(c.id));
									if(mc.chat.activeTab.activeChannel == c)
										mc.chat.activeTab.activeChannel = null;
									if(mc.chat.activeTab.isSystem && c.type == GChannel.Type.GENERAL)
										mc.chat.activeTab.generalLeaved.add(c.id);
								} else {
									mc.chat.activeTab.attachedChannels.put(c.id, c);
									c.attachedTabs.add(mc.chat.activeTab);
								}
								mc.chat.saveConfig();
							}
						}
					}
				} else if(y >= 1 && y <= 12) {
					if(x >= 1 && x <= 12) {
						if(mc.chat.activeTabIndex > 0) {
							mc.chat.tabs.remove(mc.chat.activeTabIndex);
							mc.chat.tabs.add(--mc.chat.activeTabIndex, mc.chat.activeTab);
							if(mc.chat.activeTabIndex - 2 >= mc.chat.tabScroll)
								mc.chat.tabScroll = mc.chat.activeTabIndex - 2;
							if(mc.chat.activeTabIndex < mc.chat.tabScroll)
								mc.chat.tabScroll = mc.chat.activeTabIndex;
							mc.chat.saveConfig();
						}
					} else if(x >= 82 && x <= 95) {
						if(mc.chat.activeTabIndex < mc.chat.tabs.size() - 1) {
							mc.chat.tabs.remove(mc.chat.activeTabIndex);
							mc.chat.tabs.add(++mc.chat.activeTabIndex, mc.chat.activeTab);
							if(mc.chat.activeTabIndex - 2 >= mc.chat.tabScroll)
								mc.chat.tabScroll = mc.chat.activeTabIndex - 2;
							if(mc.chat.activeTabIndex < mc.chat.tabScroll)
								mc.chat.tabScroll = mc.chat.activeTabIndex;
							mc.chat.saveConfig();
						}
					}
				}
			}
		}
	}

	public void mouseMovedOrUp(int x, int y, int key) {
		if(key == 0 && clickedOnMove) {
			if(!Mouse.getEventButtonState()) {
				clickedOnMove = false;
				mc.chat.saveConfig();
				return;
			}
		}
	}

	public void drawChatInterface(int i, int j, int k, int l, int i1, int j1) {
		float f = 1f / 640f;
		float f1 = 1f / 73f;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(i + 0, j + j1, zLevel, (k + 0) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + j1, zLevel, (k + i1) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + 0, zLevel, (k + i1) * f, (l + 0) * f1);
		tessellator.addVertexWithUV(i + 0, j + 0, zLevel, (k + 0) * f, (l + 0) * f1);
		tessellator.draw();
	}

	private void func_41039_c() {
		if(RenderDragon.field_41038_a == null) {
			return;
		}
		EntityDragon entitydragon = RenderDragon.field_41038_a;
		RenderDragon.field_41038_a = null;
		FontRenderer fontrenderer = mc.fontRenderer;
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int i = scaledresolution.getScaledWidth();
		char c = '\266';
		int j = i / 2 - c / 2;
		int k = (int) (((float) entitydragon.func_41010_ax() / (float) entitydragon.getMaxHealth()) * (c + 1));
		byte byte0 = 12;
		drawTexturedModalRect(j, byte0, 0, 74, c, 5);
		drawTexturedModalRect(j, byte0, 0, 74, c, 5);
		if(k > 0) {
			drawTexturedModalRect(j, byte0, 0, 79, k, 5);
		}
		String s = "����";
		fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, byte0 - 10, 0xff00ff);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, mc.renderEngine.getTexture("/gui/icons.png"));
	}

	private void renderPumpkinBlur(int i, int j) {
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(3008 /* GL_ALPHA_TEST */);
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, j, -90D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(i, j, -90D, 1.0D, 1.0D);
		tessellator.addVertexWithUV(i, 0.0D, -90D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
		GL11.glEnable(3008 /* GL_ALPHA_TEST */);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderVignette(float f, int i, int j) {
		f = 1.0F - f;
		if(f < 0.0F) {
			f = 0.0F;
		}
		if(f > 1.0F) {
			f = 1.0F;
		}
		prevVignetteBrightness += (f - prevVignetteBrightness) * 0.01D;
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(0, 769);
		GL11.glColor4f(prevVignetteBrightness, prevVignetteBrightness, prevVignetteBrightness, 1.0F);
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, j, -90D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(i, j, -90D, 1.0D, 1.0D);
		tessellator.addVertexWithUV(i, 0.0D, -90D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(770, 771);
	}

	private void renderPortalOverlay(float f, int i, int j) {
		if(f < 1.0F) {
			f *= f;
			f *= f;
			f = f * 0.8F + 0.2F;
		}
		GL11.glDisable(3008 /* GL_ALPHA_TEST */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, mc.renderEngine.getTexture("/terrain.png"));
		float f1 = (Block.portal.blockIndexInTexture % 16) / 16F;
		float f2 = (Block.portal.blockIndexInTexture / 16) / 16F / RenderEngine.TERRAIN_MULT;
		float f3 = (Block.portal.blockIndexInTexture % 16 + 1) / 16F;
		float f4 = (Block.portal.blockIndexInTexture / 16 + 1) / 16F / RenderEngine.TERRAIN_MULT;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, j, -90D, f1, f4);
		tessellator.addVertexWithUV(i, j, -90D, f3, f4);
		tessellator.addVertexWithUV(i, 0.0D, -90D, f3, f2);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90D, f1, f2);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
		GL11.glEnable(3008 /* GL_ALPHA_TEST */);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderInventorySlot(int i, int j, int k, float f) {
		ItemStack itemstack = mc.thePlayer.inventory.mainInventory[i];
		if(itemstack == null) {
			return;
		}
		float f1 = itemstack.animationsToGo - f;
		if(f1 > 0.0F) {
			GL11.glPushMatrix();
			float f2 = 1.0F + f1 / 5F;
			GL11.glTranslatef(j + 8, k + 12, 0.0F);
			GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
			GL11.glTranslatef(-(j + 8), -(k + 12), 0.0F);
		}
		itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, j, k);
		if(f1 > 0.0F) {
			GL11.glPopMatrix();
		}
		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, j, k);
	}

	public void updateTick() {
		if(recordPlayingUpFor > 0)
			recordPlayingUpFor--;
		updateCounter++;
		// GreenCubes Chat start
		mc.chat.tick();
	}

	public void clearChatMessages() {
	}

	public void addChatMessage(String s) {
		mc.chat.processInput(s, -1);
	}

	public void setRecordPlayingMessage(String s) {
		recordPlaying = (new StringBuilder()).append("������ ������: ").append(s).toString(); // GreenCubes
		recordPlayingUpFor = 60;
		recordIsPlaying = true;
	}

	public void addChatMessageTranslate(String s) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		String s1 = stringtranslate.translateKey(s);
		addChatMessage(s1);
	}

}

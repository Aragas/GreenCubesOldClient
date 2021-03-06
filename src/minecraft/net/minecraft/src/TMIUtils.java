package net.minecraft.src;

import java.io.*;
import java.lang.reflect.Field;
import java.text.*;
import java.util.*;

import net.minecraft.client.Minecraft;

import org.greencubes.items.unique.DecorItemQuality;
import org.greencubes.items.unique.NamedColor;
import org.lwjgl.input.Keyboard;

public class TMIUtils {
	public static final String COPYRIGHT = "All of TooManyItems except for thesmall portion excerpted from the original Minecraft game is copyright 2011Marglyph. TooManyItems is free for personal use only. Do not redistributeTooManyItems, including in mod packs, and do not use TooManyItems' sourcecode or graphics in your own mods.";
	public static final String CONFIG_FILENAME = "TooManyItems.txt";
	public static final int SPAWNER_ID = 52;
	public static boolean haveReplacedItems = false;

	public TMIUtils() {
	}

	public static Minecraft getMinecraft() {
		try {
			Field field;
			try {
				field = (net.minecraft.client.Minecraft.class).getDeclaredField("theMinecraft");
			} catch (NoSuchFieldException nosuchfieldexception) {
				field = (net.minecraft.client.Minecraft.class).getDeclaredField("a");
			}
			field.setAccessible(true);
			return (Minecraft) field.get(null);
		} catch (IllegalAccessException illegalaccessexception) {
			illegalaccessexception.printStackTrace();
		} catch (NoSuchFieldException nosuchfieldexception) {
			nosuchfieldexception.printStackTrace();
		}

		return null;
	}

	public static void loadPreferences(TMIConfig tmiconfig) {
		try {
			Map map = tmiconfig.getSettings();
			File file = new File(Minecraft.getAppDir("minecraft"), "TooManyItems.txt");

			if(file.exists()) {
				BufferedReader bufferedreader = new BufferedReader(new FileReader(file));

				do {
					String s;

					if((s = bufferedreader.readLine()) == null) {
						break;
					}

					String as[] = s.split(":", 2);

					if(as.length > 1 && map.containsKey(as[0])) {
						map.put(as[0], as[1]);
					}
				} while(true);

				bufferedreader.close();

				for(int i = 0; i < tmiconfig.getNumSaves(); i++) {
					tmiconfig.decodeState(i, (String) map.get((new StringBuilder()).append("save").append(i + 1).toString()));
				}
			}
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	public static void savePreferences(TMIConfig tmiconfig) {
		try {
			Map map = tmiconfig.getSettings();
			File file = new File(Minecraft.getAppDir("minecraft"), "TooManyItems.txt");
			PrintWriter printwriter = new PrintWriter(new FileWriter(file));
			String s;

			for(Iterator iterator = map.keySet().iterator(); iterator.hasNext(); printwriter.println((new StringBuilder()).append(s).append(":").append((String) map.get(s)).toString())) {
				s = (String) iterator.next();
			}

			printwriter.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	public static void loadItems(TMIConfig tmiconfig) {
		List list = tmiconfig.getItems();
		list.clear();

		try {
			Class class1 = Class.forName("RedPowerBase");
			Block block = (Block) class1.getDeclaredField("blockMicro").get(null);
			Field field = (net.minecraft.src.Block.class).getDeclaredField("bQ");
			TMIItemInfo.setMaxDamageException(field.getInt(block), 32000);
		} catch (ClassNotFoundException classnotfoundexception) {
		} catch (NoClassDefFoundError noclassdeffounderror) {
		} catch (Exception exception) {
			System.out.println(exception);
		}
		TMIItemInfo.setMaxDamageException(Block.blockLeavesBao.blockID, 0);
		TMIItemInfo.setMaxDamageException(Block.blockSaplingBao.blockID, 0);
		TMIItemInfo.setMaxDamageException(Block.blockSaplingPalm.blockID, 0);
		TMIItemInfo.setMaxDamageException(Block.blockAppleTreeSapling.blockID, 0);
		TMIItemInfo.setMaxDamageException(Block.blockHalfBlockGlass.blockID, 0);
		TMIItemInfo.setMaxDamageException(Block.blockBananaSapling.blockID, 0);
		TMIItemInfo.setMaxDamageException(Block.blockSakuraSapling.blockID, 0);
		Item aitem[] = Item.itemsList;
		int i = aitem.length;

		for(int k = 0; k < i; k++) {
			Item item = aitem[k];

			if(item == null || TMIItemInfo.isHidden(item.shiftedIndex)) {
				continue;
			}

			HashSet hashset = new HashSet();
			int l = tmiconfig.areDamageVariantsShown() ? 16 : 0;
			int i1 = TMIItemInfo.getMaxDamageException(item.shiftedIndex);

			if(i1 != -1) {
				l = i1;
			}

			boolean flag = false;

			for(int j1 = 0; j1 <= l; j1++) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), j1);

				try {
					int k1 = item.getIconIndex(itemstack1);
					String s = item.getTranslatedName(itemstack1);

					if(itemDisplayName(itemstack1).equals("Unnamed")) {
						if(j1 == 0) {
							break;
						}

						continue;
					}

					if(item.shiftedIndex < Block.blocksList.length && Block.blocksList[item.shiftedIndex] != null) {
						try {
							Block.blocksList[item.shiftedIndex].getBlockTextureFromSideAndMetadata(1, j1);
						} catch (Exception exception1) {
							continue;
						}
					}

					boolean flag1 = !TMIConfig.isMultiplayer() && (item.shiftedIndex == 99 || item.shiftedIndex == 100) && j1 < 16;
					String s1 = (new StringBuilder()).append(s).append("@").append(k1).toString();

					if(!hashset.contains(s1) || TMIItemInfo.isShown(item.shiftedIndex, j1) || flag1) {
						list.add(itemstack1);
						hashset.add(s1);
					}
				} catch (NullPointerException nullpointerexception) {
				} catch (IndexOutOfBoundsException indexoutofboundsexception) {
				}
			}
			if(item == Item.lightBoots || item == Item.lightHelmet || item == Item.lightLeggins) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), 0);
				itemstack1.nbtData = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				itemstack1.nbtData.setTag("Effects", effects);
				NBTTagCompound e = new NBTTagCompound();
				effects.setTag(e);
				e.setInteger("T", 1);
				e.setBoolean("S", true);
				e.setBoolean("F", true);
				itemstack1.nbtData.setByte("DQ", (byte) DecorItemQuality.UNUSUAL_WEAR.id);
				int k1 = item.getIconIndex(itemstack1);
				String s = item.getItemNameIS(itemstack1);
				if(!itemDisplayName(itemstack1).equals("Unnamed")) {
					list.add(itemstack1);
				}
			} else if(item == Item.lightChestPlate) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), 0);
				itemstack1.nbtData = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				itemstack1.nbtData.setTag("Effects", effects);
				NBTTagCompound e = new NBTTagCompound();
				effects.setTag(e);
				e.setInteger("T", 1);
				e.setBoolean("S", true);
				itemstack1.nbtData.setByte("DQ", (byte) DecorItemQuality.UNUSUAL_WEAR.id);
				int k1 = item.getIconIndex(itemstack1);
				String s = item.getItemNameIS(itemstack1);
				if(!itemDisplayName(itemstack1).equals("Unnamed")) {
					list.add(itemstack1);
				}
			} else if(item == Item.darkBoots || item == Item.darkHelmet || item == Item.darkLeggins) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), 0);
				itemstack1.nbtData = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				itemstack1.nbtData.setTag("Effects", effects);
				NBTTagCompound e = new NBTTagCompound();
				effects.setTag(e);
				e.setInteger("T", 2);
				e.setBoolean("S", true);
				e.setBoolean("F", true);
				itemstack1.nbtData.setByte("DQ", (byte) DecorItemQuality.UNUSUAL_WEAR.id);
				int k1 = item.getIconIndex(itemstack1);
				String s = item.getItemNameIS(itemstack1);
				if(!itemDisplayName(itemstack1).equals("Unnamed")) {
					list.add(itemstack1);
				}
			} else if(item == Item.darkChestPlate) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), 0);
				itemstack1.nbtData = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				itemstack1.nbtData.setTag("Effects", effects);
				NBTTagCompound e = new NBTTagCompound();
				effects.setTag(e);
				e.setInteger("T", 2);
				e.setBoolean("S", true);
				itemstack1.nbtData.setByte("DQ", (byte) DecorItemQuality.UNUSUAL_WEAR.id);
				int k1 = item.getIconIndex(itemstack1);
				String s = item.getItemNameIS(itemstack1);
				if(!itemDisplayName(itemstack1).equals("Unnamed")) {
					list.add(itemstack1);
				}
			} else if(item == Item.BOOTS_BONE || item == Item.CHESTPIECE_BONE || item == Item.PANTS_BONE) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), 0);
				itemstack1.nbtData = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				itemstack1.nbtData.setTag("Effects", effects);
				NBTTagCompound e = new NBTTagCompound();
				effects.setTag(e);
				e.setInteger("T", 3);
				e.setBoolean("S", true);
				e.setBoolean("F", true);
				itemstack1.nbtData.setByte("DQ", (byte) DecorItemQuality.UNUSUAL_WEAR.id);
				int k1 = item.getIconIndex(itemstack1);
				String s = item.getItemNameIS(itemstack1);
				if(!itemDisplayName(itemstack1).equals("Unnamed")) {
					list.add(itemstack1);
				}
			} else if(item == Item.HEAD_BONE) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), 0);
				itemstack1.nbtData = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				itemstack1.nbtData.setTag("Effects", effects);
				NBTTagCompound e = new NBTTagCompound();
				effects.setTag(e);
				e.setInteger("T", 3);
				e.setBoolean("S", true);
				itemstack1.nbtData.setByte("DQ", (byte) DecorItemQuality.UNUSUAL_WEAR.id);
				int k1 = item.getIconIndex(itemstack1);
				String s = item.getItemNameIS(itemstack1);
				if(!itemDisplayName(itemstack1).equals("Unnamed")) {
					list.add(itemstack1);
				}
			} else if(item == Item.HEAD_CROWN_BONE) {
				ItemStack itemstack1 = new ItemStack(item, item.getItemStackLimit(), 0);
				itemstack1.nbtData = new NBTTagCompound();
				NBTTagList effects = new NBTTagList();
				itemstack1.nbtData.setTag("Effects", effects);
				NBTTagCompound e = new NBTTagCompound();
				effects.setTag(e);
				e.setInteger("T", 4);
				e.setBoolean("S", true);
				//itemstack1.nbtData.setByte("DQ", (byte) DecorItemQuality.UNUSUAL_WEAR.id);
				int k1 = item.getIconIndex(itemstack1);
				String s = item.getItemNameIS(itemstack1);
				if(!itemDisplayName(itemstack1).equals("Unnamed")) {
					list.add(itemstack1);
				}
			} else if(item == Item.GLOWING_MOD) {
				for(NamedColor color : NamedColor.orderedColors)
					list.add(((ItemGlowModifier) item).generate(1, color));
			}
		}

		/*if(tmiconfig.areDamageVariantsShown()) {
			ItemStack itemstack;

			for(Iterator iterator = TMIItemInfo.potionValues.iterator(); iterator.hasNext(); list.add(itemstack)) {
				int j = ((Integer) iterator.next()).intValue();
				itemstack = new ItemStack(TMIItemInfo.addItemOffset(117), 64, j);
			}
		}*/
	}

	public static void safeReportException(Exception exception) {
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(".yyyyMMdd.HHmmss");
			StringBuffer stringbuffer = new StringBuffer();
			simpledateformat.format(new Date(), stringbuffer, new FieldPosition(1));
			String s = (new StringBuilder()).append("tmi").append(stringbuffer.toString()).append(".txt").toString();
			File file = new File(Minecraft.getAppDir("minecraft"), s);
			PrintWriter printwriter = new PrintWriter(new FileWriter(file));
			printwriter.print("[code]TMI Version: 12w17a 2012-04-26\n");
			exception.printStackTrace(printwriter);
			printwriter.println("[/code]");
			printwriter.close();
		} catch (Exception exception1) {
			System.out.println("Error during safeReportException:");
			exception1.printStackTrace();
		}
	}

	public static List<String> itemDisplayNameMultiline(ItemStack itemstack, boolean flag) {
		List<String> list = null;

		if(isValidItem(itemstack)) {
			try {
				list = itemstack.getDescription();
			} catch (Exception exception) {
				//exception.printStackTrace();
			}
		}

		if(list == null || list.size() == 0) {
			list = new ArrayList();
			list.add(TMIItemInfo.getFallbackName(itemstack.itemID, itemstack.getItemDamage()));
		}

		String s = list.get(0).trim();

		if(s.length() == 0) {
			s = TMIItemInfo.getFallbackName(itemstack.itemID, itemstack.getItemDamage());
			list.set(0, s);
		}

		if(flag && itemstack != null) {
			s = (new StringBuilder()).append(s).append(" \2477(ID:").append(itemstack.itemID).toString();

			if(itemstack.getItemDamage() != 0) {
				s = (new StringBuilder()).append(s).append(",").append(itemstack.getItemDamage()).toString();
			}
			s += ")";
			list.set(0, s);
		}

		return list;
	}

	public static String itemDisplayName(ItemStack itemstack) {
		List<String> list = itemDisplayNameMultiline(itemstack, false);
		return list.get(0);
	}

	public static boolean isValidItem(ItemStack itemstack) {
		return itemstack == null || itemstack.itemID >= 0 && itemstack.itemID < Item.itemsList.length && Item.itemsList[itemstack.itemID] != null;
	}

	public static ItemStack getValidItem(ItemStack itemstack) {
		if(isValidItem(itemstack)) {
			return itemstack;
		} else {
			return new ItemStack(Item.itemsList[52]);
		}
	}

	public static String getValidItemDisplayName(ItemStack itemstack) {
		if(isValidItem(itemstack)) {
			return itemDisplayName(itemstack);
		} else {
			return "Undefined Item";
		}
	}

	public static void deleteHeldItem() {
		getMinecraft().thePlayer.inventory.setItemStack((ItemStack) null);
	}

	public static ItemStack getHeldItem() {
		return getMinecraft().thePlayer.inventory.getItemStack();
	}

	public static void giveStack(ItemStack itemstack, TMIConfig tmiconfig) {
		giveStack(itemstack, tmiconfig, itemstack.stackSize);
	}

	public static void giveStack(ItemStack itemstack, TMIConfig tmiconfig, int i) {
		ItemStack itemstack1 = copyStack(itemstack, i);
		Minecraft minecraft = getMinecraft();

		if(TMIConfig.isMultiplayer()) {
			NumberFormat numberformat = NumberFormat.getIntegerInstance();
			numberformat.setGroupingUsed(false);
			MessageFormat messageformat = new MessageFormat((String) tmiconfig.getSettings().get("give-command"));
			messageformat.setFormatByArgumentIndex(1, numberformat);
			messageformat.setFormatByArgumentIndex(2, numberformat);
			messageformat.setFormatByArgumentIndex(3, numberformat);
			Object aobj[] = {minecraft.thePlayer.username, Integer.valueOf(itemstack1.itemID), Integer.valueOf(itemstack1.stackSize), Integer.valueOf(itemstack1.getItemDamage())};
			minecraft.thePlayer.sendChatMessage(messageformat.format((aobj)));
		} else {
			minecraft.thePlayer.inventory.addItemStackToInventory(itemstack1);
		}
	}

	public static ItemStack copyStack(ItemStack itemstack, int i) {
		if(itemstack == null) {
			return null;
		} else {
			itemstack.stackSize += i;
			return itemstack.splitStack(i);
		}
	}

	public static ItemStack copyStack(ItemStack itemstack) {
		if(itemstack == null) {
			return null;
		} else {
			return copyStack(itemstack, itemstack.stackSize);
		}
	}

	public static void updateUnlimitedItems() {
		if(TMIConfig.isMultiplayer() || !TMIConfig.getInstance().isEnabled()) {
			return;
		}

		ItemStack aitemstack[] = getMinecraft().thePlayer.inventory.mainInventory;
		int i = aitemstack.length;

		for(int j = 0; j < i; j++) {
			ItemStack itemstack1 = aitemstack[j];

			if(itemstack1 == null) {
				continue;
			}

			if(itemstack1.stackSize < 0 || itemstack1.stackSize > 64) {
				itemstack1.stackSize = 111;
			}

			if(itemstack1.getItemDamage() < 0) {
				setStackDamage(itemstack1, -32000);
			}
		}

		ItemStack itemstack = getHeldItem();

		if(itemstack != null && itemstack.stackSize > 64) {
			itemstack.stackSize = -1;
		}
	}

	public static void setStackDamage(ItemStack itemstack, int i) {
		try {
			Field afield[] = (net.minecraft.src.ItemStack.class).getDeclaredFields();
			int j = afield.length;
			int k = 0;

			do {
				if(k >= j) {
					break;
				}

				Field field = afield[k];

				if(field.getName().equals("e")) {
					field.setAccessible(true);
					field.setInt(itemstack, i);
					break;
				}

				k++;
			} while(true);
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	public static void deleteItemsOfType(ItemStack itemstack, GuiContainer guicontainer) {
		for(int i = 0; i < guicontainer.inventorySlots.inventorySlots.size(); i++) {
			Slot slot = guicontainer.inventorySlots.inventorySlots.get(i);

			if(slot == null) {
				continue;
			}

			ItemStack itemstack1 = slot.getStack();

			if(itemstack1 != null && itemstack1.itemID == itemstack.itemID && itemstack1.getItemDamage() == itemstack.getItemDamage()) {
				slot.putStack((ItemStack) null);
			}
		}
	}

	public static boolean shiftKey() {
		return Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42);
	}

	public static boolean isCreativeMode() {
		return getMinecraft().playerController.isInCreativeMode();
	}

	public static void setCreativeMode(boolean flag) {
		Minecraft minecraft = getMinecraft();

		try {
			if(TMIConfig.isMultiplayer()) {
				minecraft.thePlayer.sendChatMessage((new StringBuilder()).append("/gamemode ").append(flag ? "1" : "0").toString());
			} else {
				if(flag) {
					minecraft.playerController = new PlayerControllerCreative(minecraft);
				} else {
					minecraft.playerController = new PlayerControllerSP(minecraft);
				}

				minecraft.thePlayer.capabilities.allowFlying = flag;
				minecraft.thePlayer.capabilities.depleteBuckets = flag;
				minecraft.thePlayer.capabilities.disableDamage = flag;
				minecraft.thePlayer.fallDistance = 0.0F;
				Field afield[] = minecraft.theWorld.worldInfo.getClass().getDeclaredFields();
				int i = afield.length;

				for(int j = 0; j < i; j++) {
					Field field = afield[j];

					if(field.getName().equals("q")) {
						field.setAccessible(true);
						field.setInt(minecraft.theWorld.worldInfo, flag ? 1 : 0);
					}
				}
			}
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	public static boolean isRaining() {
		return getMinecraft().theWorld.isRaining();
	}

	public static void setRaining(boolean flag) {
		if(TMIConfig.isMultiplayer()) {
			getMinecraft().thePlayer.sendChatMessage("/toggledownfall");
		} else {
			getMinecraft().theWorld.worldInfo.setIsRaining(flag);
			getMinecraft().theWorld.worldInfo.setRainTime(flag ? 18000 : 0x2bf20);
		}
	}

	public static long getTime() {
		return getMinecraft().theWorld.worldInfo.getWorldTime();
	}

	public static void setTime(long l) {
		if(TMIConfig.isMultiplayer()) {
			getMinecraft().thePlayer.sendChatMessage((new StringBuilder()).append("/time set ").append(l).toString());
		} else {
			getMinecraft().theWorld.worldInfo.setWorldTime(l);
		}
	}

	public static void setHourForward(int i) {
		long l = (getTime() / 24000L) * 24000L;
		long l1 = l + 24000L + (i * 1000);
		setTime(l1);
	}

	public static void logWithTrace(String s) {
		System.out.println(s);
		StackTraceElement astacktraceelement[] = Thread.currentThread().getStackTrace();

		for(int i = 0; i < astacktraceelement.length; i++) {
			System.out.println(astacktraceelement[i].toString());
		}
	}

	public static void logWithTrace(String s, int i) {
		System.out.println(s);
		StackTraceElement astacktraceelement[] = Thread.currentThread().getStackTrace();

		for(int j = 0; j < astacktraceelement.length && j < i; j++) {
			System.out.println(astacktraceelement[j].toString());
		}
	}

	public static void setPlayerHealth(int i) {
		getMinecraft().thePlayer.fireResistance = i;
	}

	public static void incrementDifficulty() {
		getMinecraft().gameSettings.setOptionValue(EnumOptions.DIFFICULTY, 1);
	}

	public static String getDifficultyString() {
		return getMinecraft().gameSettings.getKeyBinding(EnumOptions.DIFFICULTY);
	}

	public static void suppressAchievementNotice() {
		try {
			Field afield[] = getMinecraft().guiAchievement.getClass().getDeclaredFields();
			int i = afield.length;

			for(int j = 0; j < i; j++) {
				Field field = afield[j];

				if(field.getName().equals("g")) {
					field.setAccessible(true);
					field.setInt(getMinecraft().guiAchievement, 0);
				}
			}
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

	public static int maxStackSize(int i) {
		return Item.itemsList[i].getItemStackLimit();
	}

	public static List getEnchantments(ItemStack itemstack) {
		ArrayList arraylist = new ArrayList();

		if(itemstack != null) {
			NBTTagList nbttaglist = itemstack.getEnchantmentInfo();

			if(nbttaglist != null) {
				for(int i = 0; i < nbttaglist.size(); i++) {
					short word0 = ((NBTTagCompound) nbttaglist.get(i)).getShort("id");
					short word1 = ((NBTTagCompound) nbttaglist.get(i)).getShort("lvl");
					int ai[] = {word0, word1};
					arraylist.add(ai);
				}
			}
		}

		return arraylist;
	}

	public static boolean addEnchantment(ItemStack itemstack, int i, int j) {
		if(i < Enchantment.enchantmentsList.length && Enchantment.enchantmentsList[i] != null) {
			itemstack.addEnchantment(Enchantment.enchantmentsList[i], j);
			return true;
		} else {
			return false;
		}
	}

	public static void fastTransfer(int i, int j, Container container) {
		Minecraft minecraft = getMinecraft();
		ItemStack itemstack = getHeldItem();
		minecraft.playerController.windowClick(container.windowId, i, j, false, minecraft.thePlayer);
		int k = container.inventorySlots.size() - 36;
		boolean flag = i < k;
		int l = 0;
		int i1 = k;
		int j1 = k - 1;
		int k1 = container.inventorySlots.size();

		if(flag) {
			l = k;
			i1 = container.inventorySlots.size();
			j1 = -1;
			k1 = k;
		}

		boolean flag1 = true;

		do {
			if(l >= i1 || j1 >= k1) {
				break;
			}

			Slot slot = container.inventorySlots.get(l);

			if(slot != null) {
				ItemStack itemstack1 = slot.getStack();

				if(itemstack1 != null && itemstack1.itemID == itemstack.itemID && itemstack1.getItemDamage() == itemstack.getItemDamage()) {
					Slot slot1;

					if(flag1) {
						slot1 = container.inventorySlots.get(i);
					} else {
						slot1 = container.inventorySlots.get(j1);
					}

					if(slot1 == null) {
						j1++;
						flag1 = false;
						continue;
					}

					ItemStack itemstack2 = slot1.getStack();

					if(itemstack2 == null) {
						slot1.putStack(itemstack1);
						slot.putStack((ItemStack) null);
					} else if(itemstack2.itemID == itemstack1.itemID && itemstack2.getItemDamage() == itemstack1.getItemDamage()) {
						int l1 = itemstack1.stackSize + itemstack2.stackSize;
						int i2 = Item.itemsList[itemstack1.itemID].getItemStackLimit();
						int j2 = l1 - i2;

						if(j2 > 0) {
							itemstack2.stackSize = i2;
							itemstack1.stackSize = j2;
							j1++;
							flag1 = false;
							continue;
						}

						itemstack2.stackSize = l1;
						slot.putStack((ItemStack) null);
					} else {
						j1++;
						flag1 = false;
						continue;
					}
				}
			}

			l++;
		} while(true);
	}

	public static void replaceCustomItems() {
		if(haveReplacedItems) {
			return;
		}

		if(Item.itemsList[99] != null && (Item.itemsList[99] instanceof ItemBlock)) {
			Item.itemsList[99] = null;
			Item.itemsList[99] = (new TMIItemMushroomCap(99 - TMIItemInfo.itemOffset)).setItemName("mushroom");
		}

		if(Item.itemsList[100] != null && (Item.itemsList[100] instanceof ItemBlock)) {
			Item.itemsList[100] = null;
			Item.itemsList[100] = (new TMIItemMushroomCap(100 - TMIItemInfo.itemOffset)).setItemName("mushroom");
		}

		haveReplacedItems = true;
	}
}

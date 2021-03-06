package net.minecraft.src;

import static net.minecraft.src.GreenTextures.*;

import java.util.List;

import org.greencubes.util.collections.FastList;

public class ItemFrame extends Item {
	
	protected static final int[] textures = {frame_item_0, frame_item_1, frame_item_2, frame_item_3, frame_item_4, frame_item_5, frame_item_6, frame_item_7, frame_item_8, frame_item_9,
		frame_item_10, frame_item_11, frame_item_12, frame_item_13, frame_item_14, frame_item_15, frame_item_16, frame_item_17, frame_item_18, frame_item_19, frame_item_20, frame_item_21,
		frame_item_22, frame_item_23, frame_item_24, frame_item_25, frame_item_26, frame_item_27, frame_item_28, frame_item_29, frame_item_30, frame_item_31};

	protected static final int glassOverlay = frame_item_overlay_glass;
	protected static final int iceOverlay = frame_item_overlay_ice;
	
	protected static final ItemStack[][] items = new ItemStack[textures.length * 2][];
	
	public ItemFrame(int i) {
		super(i);
	}

	@Override
	public int getIconFromDamage(int i) {
		return textures[i % textures.length];
	}
	
	@Override
	public void appendDescription(ItemStack itemstack, List<String> list) {
		super.appendDescription(itemstack, list);
		list.add(StringTranslate.getInstance().translateKeyFormat("itemdesc.frame.prefix.base", new Object[] {StringTranslate.getInstance().translateKey("itemdesc.frame.base." + (itemstack.getItemDamage() & 0xF))}));
		switch((itemstack.getItemDamage() >> 4) & 1) {
		case 0:
			list.add(StringTranslate.getInstance().translateKeyFormat("itemdesc.frame.prefix.bg", new Object[] {StringTranslate.getInstance().translateKey("itemdesc.frame.bg.leather")}));
			break;
		case 1:
			list.add(StringTranslate.getInstance().translateKeyFormat("itemdesc.frame.prefix.bg", new Object[] {StringTranslate.getInstance().translateKey("itemdesc.frame.bg.velvet")}));
			break;
		}
		switch((itemstack.getItemDamage() >> 5) & 1) {
		case 0:
			list.add(StringTranslate.getInstance().translateKeyFormat("itemdesc.frame.prefix.overlay", new Object[] {StringTranslate.getInstance().translateKey("itemdesc.frame.overlay.glass")}));
			break;
		case 1:
			list.add(StringTranslate.getInstance().translateKeyFormat("itemdesc.frame.prefix.overlay", new Object[] {StringTranslate.getInstance().translateKey("itemdesc.frame.overlay.ice")}));
			break;
		}
	}
	
	@Override
	public int getIconIndex(ItemStack itemstack) {
		if(itemstack.hasNBTData() && itemstack.getNBTData().hasKey("FrameLayer")) {
			switch(itemstack.getNBTData().getInteger("FrameLayer")) {
			case 0:
				return frame_item_overlay_glass;
			case 1:
				return frame_item_overlay_ice;
			}
		}
		return super.getIconIndex(itemstack);
	}
	
	@Override
	public boolean useMultirender(ItemStack source, ItemRenderType renderType) {
		return !source.hasNBTData() || !source.getNBTData().hasKey("Multirendering");
	}
	
	@Override
	public List<ItemStack> getMultirender(ItemStack source, ItemRenderType renderType) {
		return new FastList<ItemStack>(items[source.getItemDamage() % items.length]);
	}
	
	@Override
	public void init() {
		for(int i = 0; i < textures.length; ++i) {
			ItemStack[] list = new ItemStack[2];
			list[0] = new ItemStack(Item.FRAME.shiftedIndex, 1, i, new NBTTagCompound() {{
				setBoolean("Multirendering", true);
			}});
			list[1] = new ItemStack(Item.FRAME.shiftedIndex, 1, i, new NBTTagCompound() {{
				setBoolean("Multirendering", true);
				setInteger("FrameLayer", 0);
			}});
			items[i + 0] = list;
		}
		for(int i = 0; i < textures.length; ++i) {
			ItemStack[] list = new ItemStack[2];
			list[0] = new ItemStack(Item.FRAME.shiftedIndex, 1, i, new NBTTagCompound() {{
				setBoolean("Multirendering", true);
			}});
			list[1] = new ItemStack(Item.FRAME.shiftedIndex, 1, i, new NBTTagCompound() {{
				setBoolean("Multirendering", true);
				setInteger("FrameLayer", 1);
			}});
			items[i + 32] = list;
		}
	}
}

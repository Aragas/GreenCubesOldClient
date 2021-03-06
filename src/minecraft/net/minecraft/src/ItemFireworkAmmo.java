package net.minecraft.src;

import java.util.List;

import org.greencubes.util.RGBUtils;

public class ItemFireworkAmmo extends Item {

	protected ItemFireworkAmmo(int i) {
		super(i);
	}
	
	@Override
	public void appendAttributes(ItemStack itemstack, List<String> list) {
		super.appendAttributes(itemstack, list);
		if(itemstack.hasNBTData()) {
			NBTTagCompound var5 = itemstack.nbtData.getCompoundTag("Fireworks");
			if(var5 != null) {
				if(var5.hasKey("Flight"))
					list.add(StatCollector.translateToLocal("item.fireworks.flight") + " " + var5.getByte("Flight"));
				NBTTagList var6 = var5.getTagList("Explosions");
				if(var6 != null && var6.size() > 0) {
					for(int var7 = 0; var7 < var6.size(); ++var7) {
						NBTTagCompound var8 = (NBTTagCompound) var6.get(var7);
						String name = StatCollector.translateToLocal("item.fireworksCharge.type." + var8.getByte("Type")).trim();
						if(var8.hasKey("FadeColors")) {
							String firstHalf = "\247rff" + RGBUtils.toHexString(var8.getIntArray("Colors")[0]) + name.substring(0, name.length() / 2);
							String secondHalf = "\247rff" + RGBUtils.toHexString(var8.getIntArray("FadeColors")[0]) + name.substring(name.length() / 2, name.length());
							name = firstHalf + secondHalf;
						} else {
							name = "\247rff" + RGBUtils.toHexString(var8.getIntArray("Colors")[0]) + name;
						}
						list.add("������: " + name);
					}
				}
			}
		} else {
			list.add(StatCollector.translateToLocal("item.fireworks.broken"));
		}
	}

}

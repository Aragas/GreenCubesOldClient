package net.minecraft.src;

import java.util.List;

public class ItemNameMod extends Item {

	public ItemNameMod(int i) {
		super(i);
	}
	
	@Override
	public void appendDescription(ItemStack itemstack, List<String> list) {
		list.add("\247rffd2d2d2��� �������������� ����������� ������� \247b/mod name");
	}
	
	@Override
	public void appendAttributes(ItemStack itemstack, List<String> list) {
		if(itemstack.nbtData != null && itemstack.nbtData.hasKey("ModValue")) {
			list.add("\247rffd2d2d2����� ������������� �� �������� ������ ��� ��� ��:");
			list.add("\247rffcccccc\"" + itemstack.nbtData.getString("ModValue") + "\"");
		}
	}

}

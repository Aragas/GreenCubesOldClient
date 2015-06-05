package net.minecraft.src;

import java.util.List;

public class ItemPremiumCoupon extends ItemCoupon {

	public ItemPremiumCoupon(int i) {
		super(i);
	}

	@Override
	public String getTranslatedName(ItemStack itemstack) {
		int damage = itemstack.getItemDamage();
		switch(damage) {
		case 7:
			return "����� �� �������, 7 ����";
		case 14:
			return "����� �� �������, 14 ����";
		case 31:
			return "����� �� �������, 31 ����";
		case 62:
			return "����� �� �������, 62 ���";
		}
		return super.getTranslatedName(itemstack);
	}

	@Override
	public void appendDescription(ItemStack itemstack, List list) {
		int damage = itemstack.getItemDamage();
		list.add("\247rffd2d2d2���������� �������-������� �� " + damage + " ����");
	}

}

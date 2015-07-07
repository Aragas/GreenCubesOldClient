package net.minecraft.src;

import java.util.List;

public class ItemPotionGC extends Item {

	public ItemPotionGC(int i) {
		super(i);
	}
	
	@Override
	public void appendAttributes(ItemStack itemstack, List<String> list) {
		switch(itemstack.itemDamage) {
		case 0:
			list.add("\247rffaaffff������: ���� �������");
			list.add("     \2477�������� �������: +15%");
			list.add("     \2477�������� ������������: +15%");
			list.add("     \2477����� ��������: 15 �����");
			list.add("     \2477������������ ����� ��������: 2 ����");
			break;
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.drink;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		return itemstack;
	}
}

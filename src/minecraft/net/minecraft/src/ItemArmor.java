package net.minecraft.src;

import java.util.List;
import java.util.Locale;

public class ItemArmor extends ItemWearable {
	
	private final EnumArmorMaterial material;
	public final float damageReduceAmount;
	private ItemStack toFix;

	public ItemArmor(int id, EnumArmorMaterial material, int texture, int slot, int toFix) {
		super(id, texture, slot);
		this.material = material;
		this.damageReduceAmount = material.damageReduction(slot);
		if(toFix != -1)
			this.toFix = new ItemStack(toFix, 1, 0);
		setMaxDamage(material.durability(slot));
	}
	
	@Override
	public void appendDescription(ItemStack itemstack, List<String> list) {
		super.appendDescription(itemstack, list);
		StringBuilder sb = new StringBuilder();
		sb.append("\2477����� (");
		switch(slot) {
		case 0:
			sb.append("������");
			break;
		case 1:
			sb.append("����");
			break;
		case 2:
			sb.append("�����");
			break;
		case 3:
			sb.append("�����");
			break;
		case 4:
			sb.append("��������");
			break;
		}
		sb.append(')');
		if(toFix != null) {
			sb.append(", �������: ");
			sb.append(toFix.getItem().getTranslatedName(toFix));
		}
		list.add(sb.toString());
	}
	
	@Override
	public void appendAttributes(ItemStack itemstack, List<String> list) {
		super.appendAttributes(itemstack, list);
		list.add(String.format(Locale.US, "\2477���������� �����: \247f%.1f \2477/ \247f%.1f", (float) (itemstack.getMaxDamage() - itemstack.itemDamage) / (float) itemstack.getMaxDamage() * this.damageReduceAmount, this.damageReduceAmount).toString());
	}

}

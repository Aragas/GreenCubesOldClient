package net.minecraft.src;

import java.util.List;

public class ItemArrow extends Item {
	
	protected final float damageMultipler;
	protected final float accuracy;

	public ItemArrow(int i, float damageMultipler, float accuracy) {
		super(i);
		this.damageMultipler = damageMultipler;
		this.accuracy = accuracy;
	}

	@Override
	public void appendAttributes(ItemStack itemstack, List<String> list) {
		list.add("\2477����: " + damageMultipler);
		list.add("\2477��������: " + accuracy);
		super.appendAttributes(itemstack, list);
	}
}

package net.minecraft.src;

import java.util.List;

import org.greencubes.items.unique.DecorItemQuality;

public class ItemSpeedBoots extends ItemCloth {

	protected final float speed;

	public ItemSpeedBoots(int i, int j, float speed) {
		super(i, j, 3);
		this.speed = speed;
		setMaxDamage(1200);
	}
	
	@Override
	public boolean noDrop() {
		return true;
	}

	@Override
	public boolean isUnbreakable() {
		return true;
	}
	
	@Override
	public DecorItemQuality getDecorQuality() {
		return DecorItemQuality.IMPROOVED;
	}

	@Override
	public void appendAttributes(ItemStack itemstack, List<String> list) {
		float speed = this.speed;
		if(itemstack.nbtData != null && itemstack.nbtData.hasKey("RunSpeed"))
			speed = itemstack.nbtData.getFloat("RunSpeed");
		if(speed > 0.0f)
			list.add("\247rffaaffff+" + ((int) (speed * 100)) + "% � �������� ����");
		else if(speed < 0.0f)
			list.add("\247rffff4444-" + ((int) (speed * -100)) + "% � �������� ����");
	}

}

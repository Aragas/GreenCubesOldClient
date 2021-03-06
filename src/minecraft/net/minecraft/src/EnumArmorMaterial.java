// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            ItemArmor

public enum EnumArmorMaterial {
	
	CLOTH(5, new float[] {1.0F, 2.0F, 1.0F, 0.3F}),
	CHAIN(7, new float[] {2.5F, 4.0F, 3.0F, 2.0F}),
	IRON(15, new float[] {1.0F, 2.5F, 2.0F, 1.0F}),
	GOLD(7, new float[] {2.5F, 4.0F, 3.0F, 2.0F}),
	DIAMOND(33, new float[] {2.0F, 4.0F, 2.5F, 1.5F});
	
	private static final float[][] protectTable = {
		{1.0F, 2.0F, 1.0F, 0.3F},
		{2.5F, 4.0F, 3.0F, 2.0F},
		{1.0F, 2.5F, 2.0F, 1.0F},
		{2.0F, 4.0F, 2.5F, 1.5F}};

	private static final int maxDamageArray[] = {11, 16, 15, 13};
	private int durabilityMultiplier;
	private float damageReduction[];
	private int enchantability;

	private EnumArmorMaterial(int j, float[] baseProtect) {
		durabilityMultiplier = j;
		damageReduction = baseProtect;
	}

	public int durability(int i) {
		return maxDamageArray[i] * durabilityMultiplier;
	}

	public float damageReduction(int i) {
		return damageReduction[i];
	}

	public int getEnchantability() {
		return enchantability;
	}
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            Item, EntityPig, ItemStack, EntityLiving

public class ItemSaddle extends Item {

	public ItemSaddle(int i) {
		super(i);
	}

	@Override
	public void useItemOnEntity(ItemStack itemstack, EntityLiving entityliving) {
		if(entityliving instanceof EntityPig) {
			EntityPig entitypig = (EntityPig) entityliving;
			if(!entitypig.getSaddled()) {
				entitypig.setSaddled(true);
				itemstack.stackSize--;
			}
		}
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		useItemOnEntity(itemstack, entityliving);
		return true;
	}
}

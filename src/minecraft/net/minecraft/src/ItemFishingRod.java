// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import org.greencubes.items.unique.DecorItemQuality;

// Referenced classes of package net.minecraft.src:
//            Item, EntityPlayer, EntityFishHook, ItemStack, 
//            World

public class ItemFishingRod extends Item {

	public ItemFishingRod(int i) {
		super(i);
		setMaxDamage(64);
		setMaxStackSize(1);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(entityplayer.fishEntity != null) {
			int i = entityplayer.fishEntity.catchFish();
			itemstack.damageItem(i, entityplayer);
			entityplayer.swingItem();
		} else {
			world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if(!world.multiplayerWorld) {
				world.entityJoinedWorld(new EntityFishHook(world, entityplayer));
			}
			entityplayer.swingItem();
		}
		return itemstack;
	}

	@Override
	public DecorItemQuality getDecorQuality() {
		return DecorItemQuality.NORMAL;
	}

	@Override
	public boolean noDrop() {
		return true;
	}
}

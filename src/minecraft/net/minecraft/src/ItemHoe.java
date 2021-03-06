// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.List;

import org.greencubes.items.unique.DecorItemQuality;

// Referenced classes of package net.minecraft.src:
//            Item, EnumToolMaterial, EntityPlayer, World, 
//            Block, BlockGrass, StepSound, ItemStack

public class ItemHoe extends Item {

	protected ItemStack toFix;
	protected DecorItemQuality quality;
	
	public ItemHoe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i);
		maxStackSize = 1;
		setMaxDamage(enumtoolmaterial.getMaxUses());
	}
	
	public ItemHoe setFixItem(int id) {
		if(id != -1)
			this.toFix = new ItemStack(id, 1, 0);
		return this;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		if(!entityplayer.func_35190_e(i, j, k)) {
			return false;
		}
		int i1 = world.getBlockId(i, j, k);
		int j1 = world.getBlockId(i, j + 1, k);
		if(l != 0 && j1 == 0 && i1 == Block.grass.blockID || i1 == Block.dirt.blockID) {
			Block block = Block.tilledField;
			world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.stepSoundDir2(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			if(world.multiplayerWorld) {
				return true;
			} else {
				world.setBlockWithNotify(i, j, k, block.blockID);
				itemstack.damageItem(1, entityplayer);
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public void appendDescription(ItemStack itemstack, List<String> list) {
		super.appendDescription(itemstack, list);
		StringBuilder sb = new StringBuilder();
		sb.append("\2477������");
		if(toFix != null) {
			sb.append(", �������: ");
			sb.append(toFix.getItem().getTranslatedName(toFix));
		}
		list.add(sb.toString());
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	public ItemHoe setDecor(DecorItemQuality q) {
		this.quality = q;
		return this;
	}
	
	@Override
	public boolean noDrop() {
		return this.quality != null;
	}
	
	@Override
	public boolean isUnbreakable() {
		return this.quality != null;
	}
	
	@Override
	public DecorItemQuality getDecorQuality() {
		return this.quality;
	}
}

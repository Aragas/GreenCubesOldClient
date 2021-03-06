// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            IInventory, ItemStack

public class Slot {

	private final int slotIndex;
	public final IInventory inventory;
	public int slotNumber;
	public int xDisplayPosition;
	public int yDisplayPosition;

	public Slot(IInventory iinventory, int slot, int x, int y) {
		inventory = iinventory;
		slotIndex = slot;
		xDisplayPosition = x;
		yDisplayPosition = y;
	}

	public void onPickupFromSlot(ItemStack itemstack) {
		onSlotChanged();
	}

	public boolean isItemValid(ItemStack itemstack) {
		return !itemstack.isUndropable() || inventory == Minecraft.theMinecraft.thePlayer.inventory;
	}

	public ItemStack getStack() {
		return inventory.getStackInSlot(slotIndex);
	}

	public boolean getHasStack() {
		return getStack() != null;
	}

	public void putStack(ItemStack itemstack) {
		inventory.setInventorySlotContents(slotIndex, itemstack);
		onSlotChanged();
	}

	public void onSlotChanged() {
		inventory.onInventoryChanged();
	}

	public int getSlotStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	public int getBackgroundIconIndex() {
		return -1;
	}

	public ItemStack decrStackSize(int i) {
		return inventory.decrStackSize(slotIndex, i);
	}
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            Container, InventoryCrafting, InventoryCraftResult, SlotCrafting, 
//            InventoryPlayer, Slot, CraftingManager, IInventory, 
//            World, EntityPlayer, Block, ItemStack

public class ContainerWorkbench extends Container {

	public InventoryCrafting craftMatrix;
	public IInventory craftResult;
	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;

	public ContainerWorkbench(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
		craftMatrix = new InventoryCrafting(this, 3, 3);
		craftResult = new InventoryCraftResult();
		worldObj = world;
		posX = i;
		posY = j;
		posZ = k;
		addSlot(new SlotCrafting(inventoryplayer.player, craftMatrix, craftResult, 0, 124, 35));
		for(int l = 0; l < 3; l++) {
			for(int k1 = 0; k1 < 3; k1++) {
				addSlot(new Slot(craftMatrix, k1 + l * 3, 30 + k1 * 18, 17 + l * 18));
			}

		}

		for(int i1 = 0; i1 < 3; i1++) {
			for(int l1 = 0; l1 < 9; l1++) {
				Slot s = new Slot(inventoryplayer, l1 + i1 * 9 + 9, 8 + l1 * 18, 84 + i1 * 18);
				addSlot(s);
				playerRightSlots.set(i1 * 9 + l1 + 9, s);
			}

		}

		for(int j1 = 0; j1 < 9; j1++) {
			Slot s = new Slot(inventoryplayer, j1, 8 + j1 * 18, 142);
			addSlot(s);
			playerRightSlots.set(j1, s);
		}

		onCraftMatrixChanged(craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {
		if(!worldObj.multiplayerWorld)
			craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix));
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		super.onCraftGuiClosed(entityplayer);
		if(worldObj.multiplayerWorld) {
			return;
		}
		for(int i = 0; i < 9; i++) {
			ItemStack itemstack = craftMatrix.getStackInSlot(i);
			if(itemstack != null) {
				entityplayer.dropPlayerItem(itemstack);
			}
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		if(worldObj.getBlockId(posX, posY, posZ) != Block.workbench.blockID) {
			return false;
		}
		return entityplayer.getDistanceSq(posX + 0.5D, posY + 0.5D, posZ + 0.5D) <= 64D;
	}

	@Override
	public ItemStack shiftTransfer(int i) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(i);
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(i == 0) {
				if(!putToPlayerInvenoty(itemstack1, false)) {
					return null;
				}
			} else if(i >= 10 && i < 37) {
				if(!mergeItemStack(itemstack1, 37, 46, false)) {
					return null;
				}
			} else if(i >= 37 && i < 46) {
				if(!mergeItemStack(itemstack1, 10, 37, false)) {
					return null;
				}
			} else if(!mergeItemStack(itemstack1, 10, 46, false)) {
				return null;
			}
			if(itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if(itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(itemstack1);
			} else {
				return null;
			}
		}
		return itemstack;
	}
}

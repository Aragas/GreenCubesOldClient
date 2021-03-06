// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer, Block

public class TileEntityNormalChest extends TileEntity implements IInventory {

	private ItemStack chestContents[];

	public TileEntityNormalChest() {
		chestContents = new ItemStack[36];
	}

	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return chestContents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(chestContents[i] != null) {
			if(chestContents[i].stackSize <= j) {
				ItemStack itemstack = chestContents[i];
				chestContents[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = chestContents[i].splitStack(j);
			if(chestContents[i].stackSize == 0) {
				chestContents[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		chestContents[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	public String getTexture() {
		return ((BlockNormalChest) this.getBlockType()).texture;
	}

	@Override
	public String getInvName() {
		return "Chest";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		chestContents = new ItemStack[getSizeInventory()];
		for(int i = 0; i < nbttaglist.size(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if(j >= 0 && j < chestContents.length) {
				chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < chestContents.length; i++) {
			if(chestContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public int getInventoryStackLimit() {
		return 32768;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
}

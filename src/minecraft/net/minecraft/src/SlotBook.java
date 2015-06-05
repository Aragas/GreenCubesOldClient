package net.minecraft.src;

/**
 * GreenCubes
 * Books mod
 * @author Rena
 *
 */
public class SlotBook extends Slot {

	public SlotBook(IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return super.isItemValid(itemstack) && (itemstack.itemID == Item.book.shiftedIndex || itemstack.getItem() instanceof ItemReadableBook);
	}
}

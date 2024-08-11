package anmao.mc.index.block.index;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class IndexItemHandler extends ItemStackHandler {
    private final int inputSlot;
    private final int outputSlot;
    public IndexItemHandler(int size){
        super(size);
        this.inputSlot = 0;
        this.outputSlot = 1;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot  == inputSlot) {
            return super.insertItem(slot, stack, simulate);
        }
        return ItemStack.EMPTY;
    }
    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == outputSlot){
            return super.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    public @NotNull ItemStack insertItemEX(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }
    public @NotNull ItemStack extractItemEX(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }
}

package anmao.mc.index.screen;

import anmao.mc.amlib.constant$data$Table.PlayerCDT;
import com.mojang.logging.LogUtils;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public abstract class AbstractContainerMenuCore extends AbstractContainerMenu {
    public static final Logger LOG = LogUtils.getLogger();
    private final int inventorySlotCount;
    protected AbstractContainerMenuCore(@Nullable MenuType<?> pMenuType, int pContainerId,int inventorySlotCount) {
        super(pMenuType, pContainerId);
        this.inventorySlotCount = inventorySlotCount;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int ind) {
        Slot sourceSlot = slots.get(ind);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (ind < PlayerCDT.VANILLA_FIRST_SLOT_INDEX + PlayerCDT.VANILLA_SLOT_COUNT){
            if (!moveItemStackTo(sourceStack,PlayerCDT.INVENTORY_FIRST_SLOT_INDEX,PlayerCDT.INVENTORY_FIRST_SLOT_INDEX+inventorySlotCount,false)){
                return ItemStack.EMPTY;
            }
        }else if (ind < PlayerCDT.INVENTORY_FIRST_SLOT_INDEX+inventorySlotCount){
            if (!moveItemStackTo(sourceStack,PlayerCDT.VANILLA_FIRST_SLOT_INDEX,PlayerCDT.VANILLA_FIRST_SLOT_INDEX + PlayerCDT.VANILLA_SLOT_COUNT,false)){
                return ItemStack.EMPTY;
            }
        }else {
            LOG.error("Error Inventory slot index");
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0){
            sourceSlot.set(ItemStack.EMPTY);
        }else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player,sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public abstract boolean stillValid(@NotNull Player pPlayer);

    @Override
    public void slotsChanged(@NotNull Container pContainer) {
        super.slotsChanged(pContainer);
    }

    public void addPlayerInventory(Inventory playerInv,int x,int y,int space){
        for (int i= 0; i<3;i++){
            for (int m = 0;m<9;m++){
                this.addSlot(new Slot(playerInv,m+i*9+9,x+m*space,y+i*space));
            }
        }
    }
    public void addPlayerHotBar(Inventory inventory,int x,int y,int space){
        for (int i = 0;i<9;i++){
            this.addSlot(new Slot(inventory,i,x+i*space,y));
        }
    }
}

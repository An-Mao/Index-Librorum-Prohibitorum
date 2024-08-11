package anmao.mc.index.block;

import anmao.mc.index.Index;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MenuBlockEntityCore extends BlockEntity implements MenuProvider {
    protected final String inventoryInputItemSaveKey = Index.MOD_ID + ".inventory.in";
    protected final String inventoryOutputItemSaveKey = Index.MOD_ID + ".inventory.out";
    protected final ItemStackHandler inputItems;
    protected final ItemStackHandler outputItems;

    public LazyOptional<IItemHandler> inputLazyOptional = LazyOptional.empty();
    public LazyOptional<IItemHandler> outputLazyOptional = LazyOptional.empty();
    public MenuBlockEntityCore(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState,int inputItemNumber,int outputItemNumber) {
        super(pType, pPos, pBlockState);
        inputItems = new ItemStackHandler(inputItemNumber){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        outputItems = new ItemStackHandler(outputItemNumber){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }
    public ItemStack getInputSlotItem(int ind){
        return getInputItemsHandler().getStackInSlot(ind);
    }
    public ItemStack getOutputSlotItem(int ind){
        return getOutputItemsHandler().getStackInSlot(ind);
    }
    public ItemStackHandler getInputItemsHandler() {
        return inputItems;
    }
    public ItemStackHandler getOutputItemsHandler() {
        return outputItems;
    }
    @Override
    public abstract @NotNull Component getDisplayName();
    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer);


    //--------------------------------------------------------------------------
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return inputLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
    @Override
    public void onLoad() {
        super.onLoad();
        inputLazyOptional = LazyOptional.of(() -> inputItems);
        outputLazyOptional = LazyOptional.of(() -> outputItems);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputLazyOptional.invalidate();
        outputLazyOptional.invalidate();
    }

    //--------------------------------------------------------------------------


    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put(inventoryInputItemSaveKey, inputItems.serializeNBT(pRegistries));
        pTag.put(inventoryOutputItemSaveKey, outputItems.serializeNBT(pRegistries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        inputItems.deserializeNBT(pRegistries,pTag.getCompound(inventoryInputItemSaveKey));
        outputItems.deserializeNBT(pRegistries,pTag.getCompound(inventoryOutputItemSaveKey));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider holders) {
        loadAdditional(tag, holders);
        //super.handleUpdateTag(tag, holders);
    }
}

package anmao.mc.index.block.index;

import anmao.mc.amlib.server.ServerSupports;
import anmao.mc.index.block.MenuBlockEntityCore;
import anmao.mc.index.config.Config;
import anmao.mc.index.datatype.IndexData;
import anmao.mc.index.datatype.IndexEnchantData;
import anmao.mc.index.register.BlockEntityRegister;
import anmao.mc.index.register.NetRegister;
import anmao.mc.index.screen.index.IndexMenu;
import anmao.mc.net.core.NetCore;
import anmao.mc.net.core.NetPack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;

public class IndexBlockEntity extends MenuBlockEntityCore {
    private final String enchantDatSaveKey ="index.enchants",
            progressSaveKey = "index.progress",
            mpSaveKey = "index.mp";
    private int progress = 0,
            maxProgress = 25,
            mp = 0;

    protected final ContainerData data;
    private final BlockPos blockPos;
    private Player player;
    private final IndexData enchantData;
    public IndexBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegister.INDEX.get(), pPos, pBlockState,1,1);
        //System.out.println("IndexBlockEntity::"+Minecraft.getInstance().level +" s::"+ServerSupports.getOverworldLevel());
        enchantData = new IndexData();
        this.blockPos = pPos;
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> IndexBlockEntity.this.progress;
                    case 1 -> IndexBlockEntity.this.maxProgress;
                    case 2 -> IndexBlockEntity.this.blockPos.getX();
                    case 3 -> IndexBlockEntity.this.blockPos.getY();
                    case 4 -> IndexBlockEntity.this.blockPos.getZ();
                    case 5 -> IndexBlockEntity.this.mp;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int i1) {
                switch (i){
                    case 0 -> IndexBlockEntity.this.progress = i1;
                    case 1 -> IndexBlockEntity.this.maxProgress = i1;
                }
            }
            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    @Override
    public void setLevel(@NotNull Level pLevel) {
        super.setLevel(pLevel);
        if (pLevel.isClientSide){
            enchantData.setLevel(Minecraft.getInstance().level);
        }else {
            enchantData.setLevel(pLevel);
        }
    }

    public ItemStack getInputItem(){
        return getInputSlotItem(0);
    }
    public ItemStack getOutputItem(){
        return getOutputSlotItem(0);
    }
    private void resetProgress() {
        progress = 0;
    }
    private boolean isDone() {
        return progress >= maxProgress;
    }
    private void addProgress() {
        progress++;
    }
    public void drops(){
        /*
        SimpleContainer inventory = new SimpleContainer(this.items.getSlots());
        for (int i= 0;i < this.items.getSlots();i++){
            inventory.setItem(i,this.items.getStackInSlot(i));
        }
        if (this.level != null) {
            Containers.dropContents(this.level,this.worldPosition,inventory);
        }

         */
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.nu.index");
    }


    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        this.player = player;

        return new IndexMenu(i,inventory,this,this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        saveDat(pTag);
    }
    private CompoundTag saveDat(CompoundTag tag){
        tag.put(enchantDatSaveKey,enchantData.formatEnchants());
        tag.putInt(progressSaveKey,progress);
        tag.putInt(mpSaveKey,mp);
        return tag;
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        loadDat(pTag);
    }

    private void loadDat(CompoundTag tag){
        enchantData.loadEnchants(tag.getList(enchantDatSaveKey, Tag.TAG_COMPOUND));
        progress = tag.getInt(progressSaveKey);
        mp = tag.getInt(mpSaveKey);
    }

    public CompoundTag getNetPack(){
        return saveDat(new CompoundTag());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag dat = super.getUpdateTag(pRegistries);
        return saveDat(dat);
    }


    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (addMP())return;
        if (checkItem()){
            addProgress();
            setChanged(level,blockPos,blockState);
            if (isDone()) {
                outPutItem();
                resetProgress();
            }
        }else {
            resetProgress();
        }
    }

    private boolean addMP(){
        int base = 0;
        if (getInputItem().getItem() == Items.LAPIS_LAZULI){
            base = 1;
        }else if (getInputItem().getItem() == Items.LAPIS_BLOCK){
            base = 9;
        }
        if (base > 0){
            mp+= getInputItem().getCount() * base;
            //getItemStackHandler().extractItem(aInputSlotIndex, 1, false);
            getInputItemsHandler().setStackInSlot(0, ItemStack.EMPTY);
            return true;
        }
        return false;
    }
    private void outPutItem() {
        ItemStack inItem = getInputItem().copy();
        if (inItem.isEmpty() || inItem.getCount() < 1)return;
        ItemStack lOutItem = getOutputItem();
        if (!lOutItem.isEmpty() && lOutItem.getCount() >= lOutItem.getMaxStackSize())return;
        ItemEnchantments ed;
        if (inItem.is(Items.ENCHANTED_BOOK) && (lOutItem.is(Items.BOOK) || lOutItem.isEmpty())){
            ed = inItem.get(DataComponents.STORED_ENCHANTMENTS);
            if (lOutItem.isEmpty()){
                lOutItem = new ItemStack(Items.BOOK,1);
            }else{
                lOutItem.setCount(lOutItem.getCount()+1);
            }
        }else if (lOutItem.isEmpty()){
            ed = inItem.getEnchantments();
            lOutItem = inItem.copy();
            lOutItem.setCount(1);
            lOutItem.set(DataComponents.ENCHANTMENTS,null);
        } else ed = null;

        if (ed == null)return;
        ed.keySet().forEach(enchantmentHolder -> enchantData.addEnchant(enchantmentHolder.getRegisteredName(),ed.getLevel(enchantmentHolder)));
        getInputItemsHandler().extractItem(0, 1, false);
        getOutputItemsHandler().setStackInSlot(0, lOutItem);
        updateToClient();
    }
    private boolean checkItem(){
        ItemStack in = getInputItem();
        if (in == ItemStack.EMPTY) return false;
        if (getOutputItem() != ItemStack.EMPTY) return in.is(Items.ENCHANTED_BOOK) && getOutputItem().is(Items.BOOK);
        return !in.getEnchantments().isEmpty() || in.getItem() == Items.ENCHANTED_BOOK;
    }
    public HashMap<Enchantment, IndexEnchantData> getSaveEnchants(){
        return enchantData.getEnchantData();
    }
    public void handlePacket(CompoundTag msg) {
        if (getLevel() == null)return;
        ItemStack newItem = getInputItem().copy();
        if (!newItem.isEmpty() && (newItem.getEnchantments().isEmpty() || newItem.getItem() == Items.BOOK)) {
            if (!getOutputItem().isEmpty()) return;
            if (newItem.getItem() == Items.BOOK) {
                newItem = new ItemStack(Items.ENCHANTED_BOOK);
            }
            Registry<Enchantment> reg = getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            ItemEnchantments.Mutable  selectEnchants = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            ListTag datas = msg.getList("enchants", Tag.TAG_COMPOUND);

            int needMp = 0;
            for (int i = 0; i < datas.size(); i++) {
                CompoundTag c = datas.getCompound(i);
                Optional<Holder.Reference<Enchantment>> enchantment = reg.getHolder(ResourceLocation.parse(c.getString(IndexData.ID)));

                if (enchantment.isPresent()) {
                    if (enchantment.get().get().canEnchant(newItem)){
                        int lvl = Math.min(c.getInt(IndexData.LVL), enchantment.get().get().getMaxLevel());
                        if (Config.instance.getDatas().indexMpUse > 0) {
                            needMp += lvl * Config.instance.getDatas().indexMpUse;
                        }
                        selectEnchants.set(enchantment.get(), lvl);
                        if (newItem.getItem() == Items.ENCHANTED_BOOK) {
                            newItem.set(DataComponents.STORED_ENCHANTMENTS, selectEnchants.toImmutable());
                        }else {
                            newItem.set(DataComponents.ENCHANTMENTS, selectEnchants.toImmutable());
                        }
                    }
                }
            }
            if (needMp > mp) return;
            mp -= needMp;
            ItemStack finalItem = newItem.copy();
            ItemStack input = getInputItem();
            input.shrink(1);
            getInputItemsHandler().setStackInSlot(0, input);
            getOutputItemsHandler().setStackInSlot(0, finalItem);
            updateToClient();
        }
    }
    private void updateToClient(){
        setChanged();
        if (getLevel() != null){
            getLevel().sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        }
        if (this.player != null) {
            CompoundTag data = getNetPack();

            NetCore.sendToPlayer(NetPack.createClientPack(data, NetRegister.INDEX_BLOCK), (ServerPlayer) this.player);
        }
    }
    public boolean shouldRenderFace(Direction pFace) {
        return Block.shouldRenderFace(this.getBlockState(), getLevel(), this.getBlockPos(), pFace, this.getBlockPos().relative(pFace));
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.DOWN)  return outputLazyOptional.cast(); else return inputLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}

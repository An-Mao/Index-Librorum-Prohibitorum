package anmao.mc.index.block;

import anmao.mc.index.block.index.IndexBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityBlockCore extends BaseEntityBlock {
    public ItemStack dropItem;
    protected EntityBlockCore(Properties pProperties) {
        super(pProperties);
    }
    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState);

    @Override
    public @NotNull BlockState playerWillDestroy(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
        if (!pLevel.isClientSide){
            if (pPlayer.hasCorrectToolForDrops(pState) && pLevel.getBlockEntity(pPos) instanceof IndexBlockEntity indexBlockEntity) {
                dropItem = new ItemStack(pState.getBlock(),1);
                dropItem.applyComponents(indexBlockEntity.collectComponents());
                ItemEntity itementity = new ItemEntity(
                        pLevel, (double)pPos.getX() + 0.5, (double)pPos.getY() + 0.5, (double)pPos.getZ() + 0.5, dropItem
                );
                itementity.setDefaultPickUpDelay();
                pLevel.addFreshEntity(itementity);
            }
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return pState;
    }
    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState pState, LootParams.@NotNull Builder pParams) {
        List<ItemStack> drops = new ArrayList<>();
        if(dropItem != null){
            drops.add(dropItem);
            return drops;
        }else{
            return super.getDrops(pState, pParams);
        }
    }
}

package anmao.mc.index.block.index;

import anmao.mc.index.block.EntityBlockCore;
import anmao.mc.index.register.BlockEntityRegister;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IndexBlock extends EntityBlockCore {
    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);
    public IndexBlock() {
        super(Properties.of()
                .destroyTime(10f)
                .lightLevel(value -> 9)
                .noOcclusion());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            //pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
            ((ServerPlayer)pPlayer).openMenu(pState.getMenuProvider(pLevel, pPos),pPos);
            return InteractionResult.CONSUME;
        }
    }
    private void openScreen(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof IndexBlockEntity indexBlockEntity) {
            ((ServerPlayer)pPlayer).openMenu(indexBlockEntity, pPos);
            pPlayer.awardStat(Stats.ENCHANT_ITEM);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new IndexBlockEntity(blockPos,blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide){
            return null;
        }
        return createTickerHelper(pBlockEntityType, BlockEntityRegister.INDEX.get(), (level, blockPos, blockState, blockEntityIndex) -> blockEntityIndex.tick(level,blockPos,blockState));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        CustomData dat = pStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (dat != null) {
            CompoundTag nbt = dat.copyTag();
            int i = nbt.getList("index.enchants", Tag.TAG_COMPOUND).size();

            pTooltipComponents.add(Component.translatable("tooltip.index.index.save.all").withStyle(ChatFormatting.RED).append(Component.literal(String.valueOf(i)).withStyle(ChatFormatting.RED)));
            i = nbt.getInt("index.mp");
            pTooltipComponents.add(Component.translatable("tooltip.index.index.mp").withStyle(ChatFormatting.DARK_AQUA).append(Component.literal(String.valueOf(i)).withStyle(ChatFormatting.DARK_AQUA)));
            //id:"index:index",index.enchants:[{id:"minecraft:density",max:5,xp:"16"},{id:"minecraft:thorns",max:3,xp:"4"},{id:"minecraft:power",max:5,xp:"16"}],index.inventory.in:{Items:[]},index.inventory.out:{Items:[{Slot:0b,count:2,id:"minecraft:book"}]},index.mp:0,index.progress:0
        }
        pTooltipComponents.add(Component.translatable("tooltip.index.index.item.desc").withStyle(ChatFormatting.DARK_BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.index.index.item.desc1").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}

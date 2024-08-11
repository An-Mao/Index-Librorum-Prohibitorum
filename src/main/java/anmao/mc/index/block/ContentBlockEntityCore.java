package anmao.mc.index.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ContentBlockEntityCore extends MenuBlockEntityCore implements WorldlyContainer {
    public ContentBlockEntityCore(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState,int inputItemNumber,int outputItemNumber) {
        super(pType, pPos, pBlockState, inputItemNumber,outputItemNumber);
    }
}

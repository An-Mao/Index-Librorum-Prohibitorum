package anmao.mc.index.network;

import anmao.mc.index.block.index.IndexBlockEntity;
import anmao.mc.index.screen.index.IndexScreen;
import anmao.mc.net.core.NetHandle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class IndexBlockNet extends NetHandle {

    @Override
    public void client(CustomPayloadEvent.Context context, CompoundTag dat) {
        if (Minecraft.getInstance().screen instanceof IndexScreen screen) {
            screen.handlePacket(dat);
        }
        super.client(context, dat);
    }

    @Override
    public void server(CustomPayloadEvent.Context context, CompoundTag dat) {
        ServerPlayer sender = context.getSender();
        if (sender != null){
            if (sender.level() instanceof ServerLevel serverLevel) {
                IndexBlockEntity blockEntity = (IndexBlockEntity) serverLevel.getBlockEntity(new BlockPos(dat.getInt("be.x"), dat.getInt("be.y"), dat.getInt("be.z")));
                if (blockEntity != null) {
                    blockEntity.handlePacket(dat);
                }
            }
        }
        super.server(context, dat);
    }
}

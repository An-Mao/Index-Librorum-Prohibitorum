package anmao.mc.index.register;

import anmao.mc.index.Index;
import anmao.mc.index.block.index.IndexBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Index.MOD_ID);

    public static final RegistryObject<BlockEntityType<IndexBlockEntity>> INDEX =
            BLOCK_ENTITIES.register("index",()->BlockEntityType.Builder.of(IndexBlockEntity::new, BlockRegister.INDEX.get()).build(null));
    public static void reg(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}

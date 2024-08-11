package anmao.mc.index.register;

import anmao.mc.index.Index;
import anmao.mc.index.block.index.IndexBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Index.MOD_ID);

    public static final RegistryObject<Block> INDEX = registryBlock("index", IndexBlock::new);


    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registryBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registryBlockItem(String name, RegistryObject<T> block) {
        return ItemRegister.ITEMS.register(name,()->new BlockItem(block.get(),new Item.Properties()));
    }
    public static void reg(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}

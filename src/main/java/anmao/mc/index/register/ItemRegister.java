package anmao.mc.index.register;

import anmao.mc.index.Index;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Index.MOD_ID);
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }


    public static class CreativeTabs {
        private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Index.MOD_ID);
        public static final RegistryObject<CreativeModeTab> TAB = TABS.register("index_tab",()-> CreativeModeTab.builder().icon(()->new ItemStack(BlockRegister.INDEX.get()))
                .title(Component.translatable("create_tab.index_tab"))
                .displayItems((pParameters, pOutput) -> {
                    pOutput.accept(BlockRegister.INDEX.get());
                })
                .build());

        public static void reg(IEventBus eventBus){
            TABS.register(eventBus);
        }
    }

}

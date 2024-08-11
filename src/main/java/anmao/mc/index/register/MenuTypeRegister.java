package anmao.mc.index.register;

import anmao.mc.index.Index;
import anmao.mc.index.screen.index.IndexMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegister {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Index.MOD_ID);
    public static final RegistryObject<MenuType<IndexMenu>> INDEX_MENU = registryMenuType("index_menu", IndexMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registryMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name,()->new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));

        //return MENUS.register(name,()-> IForgeMenuType.create(factory));
    }
    public static void reg(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}

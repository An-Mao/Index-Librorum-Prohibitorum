package anmao.mc.index.event;

import anmao.mc.index.Index;
import anmao.mc.index.register.MenuTypeRegister;
import anmao.mc.index.screen.index.IndexScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@Mod.EventBusSubscriber(modid = Index.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(()->MenuScreens.register(MenuTypeRegister.INDEX_MENU.get(), IndexScreen::new));
    }
}

package anmao.mc.index.block;

import anmao.mc.index.Index;
import anmao.mc.index.block.index.IndexBlockEntityRender;
import anmao.mc.index.register.BlockEntityRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Index.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class RenderClientEvent {
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegister.INDEX.get(), IndexBlockEntityRender::new);
    }
}
